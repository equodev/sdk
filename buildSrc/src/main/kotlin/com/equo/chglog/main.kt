package com.equo.chglog

import com.equo.env.getenvOrDefault
import com.equo.gsutil.GSUtil
import com.equo.properties.Properties
import com.equo.properties.toStringWithoutComments
import com.equo.string.runCommand
import com.equo.version.Version
import org.gradle.api.Project

val gobin: String = getenvOrDefault("GOBIN", "/go/bin")

fun gitChglog(configPath: String, projectVersion: String): String {
    val tagPattern = "v"
    val CI_PROJECT_URL = getenvOrDefault("CI_PROJECT_URL")

    return ("$gobin/git-chglog " +
            "-c .$configPath/config.yml " +
            "-t .$configPath/CHANGELOG.tpl.md " +
            "--tag-filter-pattern \"^${tagPattern}\" " +
            "--repository-url $CI_PROJECT_URL " +
            "--no-case " +
            "--next-tag $tagPattern$projectVersion $tagPattern$projectVersion")
            .runCommand() ?: ""
}

fun copyConfigChangelog(project: Project, bucket: String, configPath: String) {
    val targetConfig = project.file(".chglog")
    if (!targetConfig.exists()) {
        targetConfig.mkdir()
    }
    GSUtil().copy("gs://$bucket/master/$configPath", targetConfig.name, "-R")
}

fun appendChangelogEnvFile(project: Project, projectVersion: String) {
    val changelogEnvFile = project.file("changelog-var.env")
    changelogEnvFile.createNewFile()
    val changelogProps = Properties(changelogEnvFile)
    changelogProps.setProperty("MAJOR", Version(projectVersion).getMajor())
    changelogProps.setProperty("project_version", projectVersion)
    changelogEnvFile.appendText(changelogProps.toStringWithoutComments())
}