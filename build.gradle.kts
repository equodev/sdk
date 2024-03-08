import com.equo.chglog.appendChangelogEnvFile
import com.equo.chglog.copyConfigChangelog
import com.equo.env.getenvOrDefault
import com.equo.file.writeNewFile
import com.equo.chglog.gitChglog
import com.equo.gsutil.GSUtil
import com.equo.logger.Logger
import com.equo.version.Version

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

val projectVersion = "${properties["project_version"]}"
val version = Version(projectVersion)

tasks.register("print-coverage") {
    val jacocoRootReport = file("build/reports/jacoco/jacocoRootReport/jacocoRootReport.csv")
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

tasks.register("changelog") {
    val GCS_BUCKET = getenvOrDefault("GCS_BUCKET")
    val GCS_CI_FILES = "equo-ci-files"
    val CHGLOG_CONFIG = "conf-gitlab-1"
    val configPath = "chglog/$CHGLOG_CONFIG"

    copyConfigChangelog(project, GCS_CI_FILES, configPath)

    val major = version.getMajor()
    appendChangelogEnvFile(project, major)

    val changelogFilename = "changelog.md"
    val changelog = gitChglog(configPath, projectVersion) +
            GSUtil().cat("$GCS_BUCKET/core/$major/$changelogFilename")

    writeNewFile(project, changelogFilename, changelog.toByteArray())
    Logger.info(changelog)
}

tasks.register("getMajor") {
    println(version.getMajor())
}