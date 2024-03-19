import com.equo.env.getenvOrDefault
import com.equo.file.writeNewFile
import java.text.SimpleDateFormat
import java.util.*

plugins {
    `maven-publish`
    signing
}

apply(plugin = "signing")

java {
    withJavadocJar()
    withSourcesJar()

    registerFeature("osgiOptional") {
        usingSourceSet(sourceSets["main"])
    }

    registerFeature("apacheFelixOptional") {
        usingSourceSet(sourceSets["main"])
    }
}

repositories {
    maven("https://dl.equo.dev/middleware/mvn/release")
    maven("https://dl.equo.dev/chromium-swt-ee/equo-gpl/mvn")
    mavenLocal()
}

version = "${properties["project_version"]}"
val isSnapshotVersion = "$version".endsWith("SNAPSHOT")
val isReleaseVersion = !isSnapshotVersion

fun getCommitBranch() = System.getenv("CI_COMMIT_BRANCH") ?: ""

fun isReleaseBranch(): Boolean {
    return getCommitBranch() == "main"
}

fun isDevelopBranch(): Boolean {
    return getCommitBranch() == "develop"
}

val chromium_version = "116.0.3"

dependencies {
    implementation("com.equo:com.equo.chromium") {
        version {
            strictly(chromium_version)
        }
    }
    runtimeOnly("com.equo:com.equo.chromium.cef.gtk.linux.x86_64:$chromium_version")
    runtimeOnly("com.equo:com.equo.chromium.cef.win32.win32.x86_64:$chromium_version")
    runtimeOnly("com.equo:com.equo.chromium.cef.cocoa.macosx.x86_64:$chromium_version")
    implementation("com.equo:com.equo.middleware.bom:1.3.4")
    "apacheFelixOptionalImplementation"("org.apache.felix:org.apache.felix.framework:7.0.5")
    "apacheFelixOptionalImplementation"("org.apache.felix:org.apache.felix.atomos:1.0.0")
    "osgiOptionalImplementation"("org.eclipse.platform:org.eclipse.osgi.services:3.11.100")
    "osgiOptionalImplementation"("org.eclipse.platform:org.eclipse.osgi.util:3.7.200")
    "apacheFelixOptionalImplementation"("org.apache.felix:org.apache.felix.scr:2.2.6")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.1")
    testImplementation("org.assertj:assertj-core:3.+")
    testImplementation("org.mockito:mockito-core:5.7.0")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}

publishing {
    publications {
        create(rootProject.name, MavenPublication::class) {
            from(components["java"])
            artifactId = "${project.group}.${rootProject.name}"
            val githubRepo = "${property("github_repo")}"

            pom {
                name = "Equo SDK"
                description = "Create modern browser-based desktop apps with Java"
                url = "https://github.com/$githubRepo"

                licenses {
                    license {
                        name = "MIT License"
                        url = "https://opensource.org/licenses/MIT"
                    }
                }

                developers {
                    developer {
                        name = "Guido Modarelli"
                        email = "gmodarelli@equo.dev"
                        organization = "Equo"
                        organizationUrl = "https://equo.dev"
                    }
                }

                scm {
                    connection = "scm:git:git://github.com/$githubRepo.git"
                    developerConnection = "scm:git:ssh://github.com:$githubRepo.git"
                    url = "https://github.com/$githubRepo"
                }

                issueManagement {
                    url = "https://github.com/$githubRepo/issues"
                }
            }
        }
    }
}

tasks.withType<Sign>().configureEach {
    onlyIf {
        isReleaseVersion && isReleaseBranch()
    }
}

fun decodeBase64(content: String): ByteArray {
    return Base64.getDecoder().decode(content)
}

signing {
    setRequired({
        isReleaseVersion && gradle.taskGraph.hasTask("publish")
    })
    val signingKeyInBase64Provider = providers
            .environmentVariable("GPG_SIGNING_KEY")
    val signingPassphrase = providers
            .environmentVariable("GPG_SIGNING_PASSPHRASE")
    if (signingKeyInBase64Provider.isPresent && signingPassphrase.isPresent) {
        val signingKeyInBase64 = signingKeyInBase64Provider.get()
        val signingKey = String(decodeBase64(signingKeyInBase64))
        useInMemoryPgpKeys(signingKey, signingPassphrase.get())
        sign(publishing.publications[rootProject.name])
    }
}

tasks.jar {
    dependsOn("javadocJar", "sourcesJar")
    onlyIf {
        isReleaseBranch() || isDevelopBranch() || isSnapshotVersion
    }
    manifest {
        attributes["Name"] = project.name
        attributes["Manifest-Version"] = "${project.version}"
        attributes["Implementation-Title"] = "$group.${rootProject.name}"
        attributes["Implementation-Version"] = "${project.version}"
        attributes["Implementation-Vendor"] = "Equo"
        attributes["Created-By"] = "Gradle ${gradle.gradleVersion}"
        attributes["Build-Timestamp"] = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .format(Date())

        val props = System.getProperties()
        attributes["Build-Jdk"] = "${props["java.version"]} " +
                "(${props["java.vendor"]} ${props["java.vm.version"]})"
        attributes["Build-OS"] = "${props["os.name"]} ${props["os.arch"]} ${props["os.version"]}"
    }

    doLast {
        if (isSnapshotVersion) return@doLast

        val pkAlias = getenvOrDefault("EquoSDKKeyAlias")
        val keystoreFileContentBase64 = getenvOrDefault("EquoSDKStoreFileContent")
        val keystoreFileContent = decodeBase64(keystoreFileContentBase64)
        val keystoreFileName = "key_store.p12"
        val keystoreFile = writeNewFile(project, keystoreFileName, keystoreFileContent, false)
        val storePassword = getenvOrDefault("EquoSDKStorePassword")
        val keyPassword = getenvOrDefault("EquoSDKKeyPassword")
        val keystoreType = getenvOrDefault("EquoSDKStoreType")

        val buildDir = layout.buildDirectory.get()
        val libsDir = "$buildDir/libs"

        file(libsDir).listFiles()
                ?.filter {
                    it.path.endsWith(".jar")
                }
                ?.forEach {
                    ant.withGroovyBuilder {
                        "signjar"(
                                "jar" to it,
                                "destDir" to libsDir,
                                "alias" to pkAlias,
                                "keystore" to keystoreFile.absolutePath,
                                "storepass" to storePassword,
                                "keypass" to keyPassword,
                                "storetype" to keystoreType,
                                "preservelastmodified" to "true"
                        )
                    }
                }
    }
}