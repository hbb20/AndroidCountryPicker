package com.hbb20


fun getSampleDataStore(): CPDataStore {
    val masterCountryList = mutableListOf<CPCountry>()
    masterCountryList.add(
        CPCountry(
            name = "Afghanistan",
            englishName = "Afghanistan",
            alpha2 = "AF",
            alpha3 = "AFG",
            phoneCode = 93,
            demonym = "Afghans",
            capitalEnglishName = "Kabul",
            areaKM2 = "652230",
            population = 36373176,
            currencyCode = "AFN",
            currencyName = "Afghan Afghani",
            currencySymbol = "؋",
            cctld = "af",
            flagEmoji = "\uD83C\uDDE6\uD83C\uDDEB"
        )
    )

    masterCountryList.add(
        CPCountry(
            name = "Australia",
            englishName = "Australia",
            alpha2 = "AU",
            alpha3 = "AUS",
            phoneCode = 61,
            demonym = "Australians",
            capitalEnglishName = "Canberra",
            areaKM2 = "7741220",
            population = 24772247,
            currencyCode = "AUD",
            currencyName = "Australian Dollar",
            currencySymbol = "$",
            cctld = "au",
            flagEmoji = "\uD83C\uDDE6\uD83C\uDDFA"
        )
    )
    masterCountryList.add(
        CPCountry(
            name = "Ghana",
            englishName = "Ghana",
            alpha2 = "GH",
            alpha3 = "GHA",
            phoneCode = 233,
            demonym = "Ghanaians",
            capitalEnglishName = "Accra",
            areaKM2 = "238533",
            population = 29463643,
            currencyCode = "GHS",
            currencyName = "Ghanaian New Cedi",
            currencySymbol = "GH₵",
            cctld = "gh",
            flagEmoji = "\uD83C\uDDEC\uD83C\uDDED"
        )
    )
    masterCountryList.add(
        CPCountry(
            name = "India",
            englishName = "India",
            alpha2 = "IN",
            alpha3 = "IND",
            phoneCode = 91,
            demonym = "Indians",
            capitalEnglishName = "New Delhi",
            areaKM2 = "3287263",
            population = 1354051854,
            currencyCode = "INR",
            currencyName = "Indian Rupee",
            currencySymbol = "₹",
            cctld = "in",
            flagEmoji = "\uD83C\uDDEE\uD83C\uDDF3"
        )
    )
    masterCountryList.add(
        CPCountry(
            name = "Sri Lanka",
            englishName = "Sri Lanka",
            alpha2 = "LK",
            alpha3 = "LKA",
            phoneCode = 94,
            demonym = "Sri Lankans",
            capitalEnglishName = "Sri Jayewardenepura Kotte",
            areaKM2 = "65610",
            population = 20950041,
            currencyCode = "LKR",
            currencyName = "Sri Lanka Rupee",
            currencySymbol = "රු",
            cctld = "lk",
            flagEmoji = "\uD83C\uDDF1\uD83C\uDDF0"
        )
    )
    masterCountryList.add(
        CPCountry(
            name = "Russian Federation",
            englishName = "Russian Federation",
            alpha2 = "RU",
            alpha3 = "RUS",
            phoneCode = 7,
            demonym = "Russians",
            capitalEnglishName = "Moscow",
            areaKM2 = "17098242",
            population = 143964709,
            currencyCode = "RUB",
            currencyName = "Russian Rouble",
            currencySymbol = "\u20BD",
            cctld = "ru",
            flagEmoji = "\uD83C\uDDF7\uD83C\uDDFA"
        )
    )
    masterCountryList.add(
        CPCountry(
            name = "Togo",
            englishName = "Togo",
            alpha2 = "TG",
            alpha3 = "TGO",
            phoneCode = 228,
            demonym = "Togolese",
            capitalEnglishName = "Lome",
            areaKM2 = "56785",
            population = 7990926,
            currencyCode = "XOF",
            currencyName = "Cfa Franc Bceao",
            currencySymbol = "CFA",
            cctld = "tg",
            flagEmoji = "\uD83C\uDDF9\uD83C\uDDEC"
        )
    )
    masterCountryList.add(
        CPCountry(
            name = "United States",
            englishName = "United States",
            alpha2 = "US",
            alpha3 = "USA",
            phoneCode = 1,
            demonym = "Americans",
            capitalEnglishName = "Washington, D.C.",
            areaKM2 = "9826675",
            population = 326766748,
            currencyCode = "USD",
            currencyName = "United States Dollar",
            currencySymbol = "$",
            cctld = "us",
            flagEmoji = "\uD83C\uDDFA\uD83C\uDDF8"
        )
    )
    masterCountryList.add(
        CPCountry(
            name = "South Africa",
            englishName = "South Africa",
            alpha2 = "ZA",
            alpha3 = "ZAF",
            phoneCode = 27,
            demonym = "South Africans",
            capitalEnglishName = "Cape Town",
            areaKM2 = "1219090",
            population = 57398421,
            currencyCode = "ZAR",
            currencyName = "South African Rand",
            currencySymbol = "R",
            cctld = "za",
            flagEmoji = "\uD83C\uDDFF\uD83C\uDDE6"
        )
    )
    masterCountryList.add(
        CPCountry(
            name = "Zimbabwe",
            englishName = "Zimbabwe",
            alpha2 = "ZW",
            alpha3 = "ZWE",
            phoneCode = 263,
            demonym = "Zimbabweans",
            capitalEnglishName = "Harare",
            areaKM2 = "390757",
            population = 16913261,
            currencyCode = "ZWD",
            currencyName = "Zimbabwe Dollar",
            currencySymbol = "$",
            cctld = "zw",
            flagEmoji = "\uD83C\uDDFF\uD83C\uDDFC"
        )
    )
    return CPDataStore(
        masterCountryList,
        CPDataStore.MessageCollection(
            "No matching result found",
            "Search...",
            "Select a country",
            "Country"
        )
    )
}