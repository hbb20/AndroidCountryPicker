import datagenerator.SupportedLanguage

data class LanguageTranslation(
    val language: SupportedLanguage,
    val countryNameTranslations: MutableMap<String, String>,
    var messageGroup: MessageGroup? = null
)