import com.equo.env.getenvOrDefault
import com.equo.file.writeNewFile
import java.text.SimpleDateFormat
import java.util.*

plugins {
    `maven-publish`
    signing
}

apply(plugin = "signing")

// Chromium version could not be the same for Cef or Swt artifact.
// See https://docs.equo.dev/chromium/116.x/reference/release-notes.html for more details.
val chromiumSwtVersion = "116.0.6"
val chromiumCefVersion = "116.0.6"

java {
    withJavadocJar()
    withSourcesJar()

    registerFeature("osgiSupport") {
        usingSourceSet(sourceSets["main"])
    }

    // Init variants for x86_64
    registerFeature("chromiumLinux") {
        usingSourceSet(sourceSets["main"])
        capability("dev.equo", "os-support", chromiumCefVersion)
        capability("dev.equo", "core-chromium-x86_64-linux", chromiumCefVersion)
    }
    registerFeature("chromiumWindows") {
        usingSourceSet(sourceSets["main"])
        capability("dev.equo", "os-support", chromiumCefVersion)
        capability("dev.equo", "core-chromium-x86_64-windows", chromiumCefVersion)
    }
    registerFeature("chromiumMac") {
        usingSourceSet(sourceSets["main"])
        capability("dev.equo", "os-support", chromiumCefVersion)
        capability("dev.equo", "core-chromium-x86_64-osx", chromiumCefVersion)
    }

    // Init variants for x86
    registerFeature("chromiumX86Windows") {
        usingSourceSet(sourceSets["main"])
        capability("dev.equo", "os-support", chromiumCefVersion)
        capability("dev.equo", "core-chromium-x86-windows", chromiumCefVersion)
    }

    // Init variants for aarch64
    registerFeature("chromiumAarch64Linux") {
        usingSourceSet(sourceSets["main"])
        capability("dev.equo", "os-support", chromiumCefVersion)
        capability("dev.equo", "core-chromium-aarch64-linux", chromiumCefVersion)
    }
    registerFeature("chromiumAarch64Mac") {
        usingSourceSet(sourceSets["main"])
        capability("dev.equo", "os-support", chromiumCefVersion)
        capability("dev.equo", "core-chromium-aarch64-osx", chromiumCefVersion)
    }
}

repositories {
    maven("https://dl.equo.dev/chromium-swt-ee/equo-gpl/mvn")
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

dependencies {
    implementation("com.equo:com.equo.chromium") {
        version {
            strictly(chromiumSwtVersion)
        }
    }
    // Add x86_64 dependecies
    "chromiumLinuxRuntimeOnly"("com.equo:com.equo.chromium.cef.gtk.linux.x86_64:$chromiumCefVersion")
    "chromiumWindowsRuntimeOnly"("com.equo:com.equo.chromium.cef.win32.win32.x86_64:$chromiumCefVersion")
    "chromiumMacRuntimeOnly"("com.equo:com.equo.chromium.cef.cocoa.macosx.x86_64:$chromiumCefVersion")
    // Add x86 dependecies
    // "chromiumX86WindowsRuntimeOnly"("com.equo:com.equo.chromium.cef.win32.win32.x86:$chromium_cef_version")
    // Add aarch64 dependecies
    "chromiumAarch64LinuxRuntimeOnly"("com.equo:com.equo.chromium.cef.gtk.linux.aarch64:$chromiumCefVersion")
    "chromiumAarch64MacRuntimeOnly"("com.equo:com.equo.chromium.cef.cocoa.macosx.aarch64:$chromiumCefVersion")

    implementation("dev.equo:com.equo.middleware.bom:1.3.5")
    "osgiSupportImplementation"("org.apache.felix:org.apache.felix.framework:7.0.5")
    "osgiSupportImplementation"("org.apache.felix:org.apache.felix.atomos:1.0.0")
    "osgiSupportRuntimeOnly"("org.eclipse.platform:org.eclipse.osgi.services:3.11.100")
    "osgiSupportRuntimeOnly"("org.eclipse.platform:org.eclipse.osgi.util:3.7.200")
    "osgiSupportRuntimeOnly"("org.apache.felix:org.apache.felix.scr:2.2.6")
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
