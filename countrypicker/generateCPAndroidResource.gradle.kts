tasks.register("generateCPAndroidResource") {
    group = "automation"
    description = "Generate android resources from raw files."

    doLast {
        val releaseTag = "build-${BuildData.versionCode}"
        val releaseTitle = "v${BuildData.versionName}"
        //        val releaseBodyFile = createGithubReleaseBody()
    }
}