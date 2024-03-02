import java.util.*

plugins {
    java
    jacoco
    checkstyle
    id("io.github.gradle-nexus.publish-plugin") version "1.3.0"
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "jacoco")
    apply(plugin = "checkstyle")

    group = "dev.equo"

    repositories {
        mavenCentral()
    }

    jacoco {
        toolVersion = "0.8.11"
    }

    checkstyle {
        toolVersion = "10.3.3"
    }

    java.sourceCompatibility = JavaVersion.VERSION_11
    java.targetCompatibility = JavaVersion.VERSION_11
}

subprojects {
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    tasks.jacocoTestReport {
        sourceDirectories.setFrom(files(sourceSets["main"].allSource.srcDirs))
        classDirectories.setFrom(files(sourceSets["main"].output))

        reports {
            html.required = false
            xml.required = false
            csv.required = false
        }
    }
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl = uri("${property("release_url")}")
            snapshotRepositoryUrl = uri("${property("snapshot_url")}")
            val ossrhUsername = providers
                    .environmentVariable("OSSRH_USERNAME")
            val ossrhPassword = providers
                    .environmentVariable("OSSRH_PASSWORD")
            if (ossrhUsername.isPresent && ossrhPassword.isPresent) {
                username = ossrhUsername.get()
                password = ossrhPassword.get()
            }
        }
    }
}

dependencies {
    // https://mvnrepository.com/artifact/com.google.cloud/libraries-bom
    implementation("com.google.cloud:libraries-bom:26.33.0")
    implementation("com.google.cloud:google-cloud-storage")
}

// tasks to build jacoco report with results of all subprojects
tasks.register<JacocoReport>("jacocoRootReport") {
    val jacocoReportTasks = subprojects.map { subproject ->
        subproject.tasks["jacocoTestReport"] as JacocoReport
    }

    val executionDataMap = jacocoReportTasks.map { jacocoReport ->
        jacocoReport.executionData.filter {
            it.exists()
        }.files
    }
    executionData.setFrom(executionDataMap)

    subprojects.forEach { subproject ->
        sourceDirectories.from(files(subproject.sourceSets["main"].allSource.srcDirs))
        classDirectories.from(files(subproject.sourceSets["main"].output))
    }

    reports {
        html.required = true
        xml.required = true
        csv.required = true
    }

    // Generates reports even when there's no text.exec data, to avoid pipeline failing because of missing artifacts
    setOnlyIf {
        true
    }
}

fun Properties(file: File): Properties {
    val props = Properties()
    props.load(file.inputStream())
    return props
}

fun Properties.toStringWithoutComments(): String {
    val output = StringBuilder()
    this.entries.forEach {
        output.append("${it.key}=${it.value}\n")
    }
    return output.toString()
}

val projectVersion = "${properties["project_version"]}"
val gobin: String? = System.getenv("GOBIN")
val userHome: String? = System.getProperty("user.home")
val GCS_BUCKET: String = System.getenv("GCS_BUCKET") ?: ""

object Logger {
    private const val ANSI_ESCAPE = "\u001b"
    private const val RESET = "$ANSI_ESCAPE[0m"

    private fun print(text: String, vararg codes: Int) {
        // https://upload.wikimedia.org/wikipedia/commons/3/34/ANSI_sample_program_output.png
        val format = "$ANSI_ESCAPE[${codes.joinToString(";")}m"
        println("$format $text $RESET")
    }

    fun info(text: String) {
        this.print(text, 34, 1)
    }

    fun code(text: String) {
        this.print("$ $text", 32, 1)
    }
}

class Version(version: String) {
    private var major: String
    private var minor: String? = null
    private var patch: String? = null

    init {
        val list = version.split('.')
        major = list[0]
        if (list.size > 1) {
            minor = list[1]
        }
        if (list.size > 2) {
            patch = list[2]
        }
    }

    fun getMajor(): String {
        return major
    }

    fun removePatch(): String {
        return "$major.$minor"
    }
}

fun getMajor(): String {
    val MAJOR = System.getenv("MAJOR") ?: ""
    return MAJOR.ifBlank {
        Version(projectVersion).getMajor()
    }
}

class GSUtil {
    private val gcloudHome: String = System.getenv("GOOGLE_CLOUD_HOME") ?: "/google-cloud-sdk"
    private val gsutil = "$gcloudHome/bin/gsutil"
    private val headers = HashMap<String, String>()

    private fun flatMapHeaders(): String {
        var result = ""
        headers.entries.forEach {
            result += """-h "${it.key}":" ${it.value}" """
        }
        return result
    }

    private fun run(command: String): String {
        val command_ = "$gsutil ${flatMapHeaders()} $command"
        Logger.code(command_)
        return command_.runCommand() ?: ""
    }

    fun addHeader(key: String, value: String): GSUtil {
        headers[key] = value
        return this
    }

    fun setCORS(corsJsonFile: String, bucketName: String) {
        run("cors set $corsJsonFile gs://$bucketName")
    }

    fun makeBucket(bucketName: String): String {
        // mb - Make buckets
        return run("mb gs://$bucketName")
    }

    fun cat(path: String): String {
        // cat - Concatenate object content to stdout
        return run("cat gs://$path")
    }

    fun copy(src_url: String, dst_url: String, options: String = ""): String {
        // cp - Copy files and objects
        // The ``-R`` and ``-r`` options are synonymous. They enable directories,
        // buckets, and bucket subdirectories to be copied recursively.
        return run("cp $options $src_url $dst_url")
    }
}

fun String.runCommand(
        workingDir: File = File("."),
        timeoutAmount: Long = 60,
        timeoutUnit: TimeUnit = TimeUnit.SECONDS
): String? = runCatching {
    ProcessBuilder("\\s".toRegex().split(this))
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start().also { it.waitFor(timeoutAmount, timeoutUnit) }
            .inputStream.bufferedReader().readText()
}.onFailure { it.printStackTrace() }.getOrNull()

fun writeNewFile(filename: String, content: String) {
    val file = File(filename)
    if (!file.exists()) {
        file.createNewFile()
    }
    file.writeBytes(content.toByteArray())
    Logger.info("File $filename created successfully!")
    Logger.info("File content: $content")
}

fun mkdir(folder: String) {
    if (!File(folder).exists()) {
        File(folder).mkdir()
    }
}

tasks.register("print-coverage") {
    val jacocoRootReport = File("build/reports/jacoco/jacocoRootReport/jacocoRootReport.csv")
    var totalInstructions = 0
    var totalInstructionCovered = 0

    jacocoRootReport.forEachLine { line ->
        val fields = line.split(",")

        fun getColumn(column: Int): Int {
            return fields.getOrNull(column)?.toIntOrNull() ?: 0
        }

        val instructionMissed = getColumn(3)
        val instructionCovered = getColumn(4)

        totalInstructions += instructionMissed + instructionCovered
        totalInstructionCovered += instructionCovered
    }

    val percentageInstructionsCovered = if (totalInstructions != 0) {
        100 * totalInstructionCovered.toDouble() / totalInstructions
    } else {
        0.0
    }

    Logger.info("------------")
    Logger.info("$totalInstructionCovered / $totalInstructions instructions covered")
    Logger.info("%.2f %% covered".format(percentageInstructionsCovered))
    Logger.info("------------")
}

fun gitChglog(configPath: String): String {
    val tagPattern = "v"
    val ciProjectURL = System.getenv("CI_PROJECT_URL")

    return ("$gobin/git-chglog " +
            "-c .$configPath/config.yml " +
            "-t .$configPath/CHANGELOG.tpl.md " +
            "--tag-filter-pattern \"^${tagPattern}\" " +
            "--repository-url $ciProjectURL " +
            "--no-case " +
            "--next-tag $tagPattern$projectVersion $tagPattern$projectVersion")
            .runCommand() ?: ""
}

fun copyConfigChangelog(bucket: String, configPath: String) {
    val targetConfig = ".chglog"
    mkdir(targetConfig)
    GSUtil().copy("gs://$bucket/master/$configPath", targetConfig, "-R")
}

fun appendChangelogEnvFile(major: String) {
    val changelogEnvFile = File("changelog-var.env")
    changelogEnvFile.createNewFile()
    val changelogProps = Properties(changelogEnvFile)
    changelogProps.setProperty("MAJOR", major)
    changelogProps.setProperty("project_version", projectVersion)
    changelogEnvFile.appendText(changelogProps.toStringWithoutComments())
}

tasks.register("changelog") {
    val GCS_CI_FILES = "equo-ci-files"
    val CHGLOG_CONFIG = "conf-gitlab-1"
    val configPath = "chglog/$CHGLOG_CONFIG"

    copyConfigChangelog(GCS_CI_FILES, configPath)

    val major = getMajor()
    appendChangelogEnvFile(major)

    val changelogFilename = "changelog.md"
    val changelog = gitChglog(configPath) +
            GSUtil().cat("$GCS_BUCKET/$major/$changelogFilename")

    writeNewFile(changelogFilename, changelog)
    Logger.info(changelog)
}

tasks.register("getMajor") {
    println(getMajor())
}
