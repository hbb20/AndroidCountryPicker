tasks.register("generateCPAndroidResource") {
    group = "automation"
    description = "Generate android resources from raw files."

    doLast {
        val extractor = CPDataExtractor("$rootDir")
        extractor.generate()
    }
}
