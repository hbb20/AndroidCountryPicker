package com.hbb20


fun getSampleDataStore(cpLanguage: CPLanguage): CPDataStore {
    val masterCountryList = mutableListOf<CPCountry>()
    masterCountryList.add(
        CPCountry(
            name = "Afghanistan",
            englishName = "Afghanistan",
            alpha2Code = "AF",
            alpha3Code = "AFG",
            phoneCode = 93
        )
    )
    masterCountryList.add(
        CPCountry(
            name = "Australia",
            englishName = "Australia",
            alpha2Code = "AU",
            alpha3Code = "AUS",
            phoneCode = 61
        )
    )
    masterCountryList.add(
        CPCountry(
            name = "Ghana",
            englishName = "Ghana",
            alpha2Code = "GH",
            alpha3Code = "GHA",
            phoneCode = 233
        )
    )
    masterCountryList.add(
        CPCountry(
            name = "India",
            englishName = "India",
            alpha2Code = "IN",
            alpha3Code = "IND",
            phoneCode = 91
        )
    )
    masterCountryList.add(
        CPCountry(
            name = "Sri Lanka",
            englishName = "Sri Lanka",
            alpha2Code = "LK",
            alpha3Code = "LKA",
            phoneCode = 94
        )
    )
    masterCountryList.add(
        CPCountry(
            name = "Russian Federation",
            englishName = "Russian Federation",
            alpha2Code = "RU",
            alpha3Code = "RUS",
            phoneCode = 7
        )
    )
    masterCountryList.add(
        CPCountry(
            name = "Togo",
            englishName = "Togo",
            alpha2Code = "TG",
            alpha3Code = "TGO",
            phoneCode = 228
        )
    )
    masterCountryList.add(
        CPCountry(
            name = "United States",
            englishName = "United States (USA)",
            alpha2Code = "US",
            alpha3Code = "USA",
            phoneCode = 1
        )
    )
    masterCountryList.add(
        CPCountry(
            name = "South Africa",
            englishName = "South Africa",
            alpha2Code = "ZA",
            alpha3Code = "ZAF",
            phoneCode = 27
        )
    )
    masterCountryList.add(
        CPCountry(
            name = "Zimbabwe",
            englishName = "Zimbabwe",
            alpha2Code = "ZW",
            alpha3Code = "ZWE",
            phoneCode = 263
        )
    )
    return CPDataStore(
        cpLanguage,
        masterCountryList,
        "Select a country",
        emptySelectionText = "Country",
        noResultAck = "No matching result found",
        searchHint = "Search..."
    )
}