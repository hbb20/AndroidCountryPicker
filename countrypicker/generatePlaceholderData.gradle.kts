tasks.register("generatePlaceholderData") {
    group = "automation"
    description = "Generate placehodler info using master country list and supported languages."

    doLast {
        val generator = CPPlaceHolderDataGenerator("$rootDir")
        generator.generate()
    }
}
