class BaseCountry(
    val alpha2: String,
    val alpha3: String,
    val englishName: String,
    val demonym: String,
    val capitalEnglishName: String,
    val areaKM2: String,
    val population: Long,
    val currencyCode: String,
    val currencyName: String,
    val currencySymbol: String,
    val cctld: String,
    val flagEmoji: String,
    val phoneCode: Int
) {
    constructor(
        IP2LocationInfoCountry: IP2LocationInfoCountry,
        flagEmoji: String,
        phoneCode: Int
    ) : this(
        IP2LocationInfoCountry.alpha2,
        IP2LocationInfoCountry.alpha3,
        IP2LocationInfoCountry.englishName,
        IP2LocationInfoCountry.demonym,
        IP2LocationInfoCountry.capitalEnglishName,
        IP2LocationInfoCountry.areaKM2,
        IP2LocationInfoCountry.population.toLong(),
        IP2LocationInfoCountry.currencyCode,
        IP2LocationInfoCountry.currencyName,
        IP2LocationInfoCountry.currencySymbol,
        IP2LocationInfoCountry.cctld,
        flagEmoji,
        phoneCode
    )

    constructor(
        additionalCountryInfo: AdditionalCountryInfo,
        flagEmoji: String,
        phoneCode: Int
    ) : this(
        additionalCountryInfo.alpha2,
        additionalCountryInfo.alpha3,
        additionalCountryInfo.englishName,
        additionalCountryInfo.demonym,
        additionalCountryInfo.capitalEnglishName,
        additionalCountryInfo.areaKM2,
        additionalCountryInfo.population.toLong(),
        additionalCountryInfo.currencyCode,
        additionalCountryInfo.currencyName,
        additionalCountryInfo.currencySymbol,
        additionalCountryInfo.cctld,
        flagEmoji,
        phoneCode
    )
}