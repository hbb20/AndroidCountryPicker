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
    val phoneCode: Int?
) {
    constructor(
        infoCountry: InfoCountry,
        flagEmoji: String,
        phoneCode: Int?
    ) : this(
        infoCountry.alpha2,
        infoCountry.alpha3,
        infoCountry.englishName,
        infoCountry.demonym,
        infoCountry.capitalEnglishName,
        infoCountry.areaKM2,
        infoCountry.population.toLong(),
        infoCountry.currencyCode,
        infoCountry.currencyName,
        infoCountry.currencySymbol,
        infoCountry.cctld,
        flagEmoji,
        phoneCode
    )
}