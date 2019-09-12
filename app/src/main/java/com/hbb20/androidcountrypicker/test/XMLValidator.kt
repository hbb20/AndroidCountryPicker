package com.hbb20.androidcountrypicker.test

import android.content.Context
import com.hbb20.countrypicker.*
import org.xmlpull.v1.XmlPullParser


class XMLValidator {
    val nameCodeList = mutableListOf<String>()

    fun checkAllXMLFiles(context: Context): List<Problem> {
        val problems = mutableListOf<Problem>()
        problems.addAll(checkBaseList(context))
        problems.addAll(checkTranslationFile(context, CPLanguage.ENGLISH, nameCodeList))
        return problems
    }

    private fun checkTranslationFile(
        context: Context,
        language: CPLanguage,
        nameCodeAndNamePairList: List<String>
    ): List<Problem> {
        val fileName = language.translationFileName
        val problems = mutableListOf<Problem>()
        val xmlPullParser = getRawXMLPullParser(context = context, fileName = fileName)

        //if pullParser is null means file is missing.
        if (xmlPullParser == null) {
            problems.add(
                Problem(
                    title = ProblemTitle.MISSING_FILE,
                    desc = "No file named $fileName found.",
                    fileName = fileName,
                    solution = "$language: Add $fileName file under raw/ directory."
                )
            )
            return problems
        }
        var event = xmlPullParser.eventType
        var extraEntriesProblemAdded = false
        var entryCounter = 0
        var unverifiedTranslationCount = 0
        var existingMessageKeys = mutableSetOf<String>()
        val expectedMessagesKeys = setOf(
            xmlDialogNoResultAckMessageKey,
            xmlDialogSearchHintMessageKey,
            xmlDialogTitleKey,
            xmlEmptySelectionMessageLongKey,
            xmlEmptySelectionMessageShortKey
        )
        while (event != XmlPullParser.END_DOCUMENT) {
            val name = xmlPullParser.name
            when (event) {
                XmlPullParser.END_TAG ->
                    if (name == xmlCountryKey) {
                        val alpha2NameCode = xmlPullParser.getAttributeValue(null, xmlAlpha2Key)
                        val translation = xmlPullParser.getAttributeValue(null, xmlTranslationKey)
                        val verified = xmlPullParser.getAttributeValue(null, xmlVerifiedKey)

                        //check primary key
                        if (alpha2NameCode.isNullOrBlank()) {
                            problems.add(
                                Problem(
                                    title = ProblemTitle.MISSING_PROPERTY,
                                    desc = "Alpha 2 namecode. ",
                                    fileName = fileName,
                                    solution = "Add Alpha2 name code for Country entry at position #$entryCounter or remove entry."
                                )
                            )
                        } else {
                            if (entryCounter > nameCodeAndNamePairList.size) {
                                if (!extraEntriesProblemAdded) {
                                    problems.add(
                                        Problem(
                                            title = ProblemTitle.EXTRA_ENTRIES,
                                            desc = "More entries than baselist.",
                                            fileName = fileName,
                                            solution = "Remove extra entries: Make sure $fileName is in sync with $baseListFileName."
                                        )
                                    )
                                    extraEntriesProblemAdded = true
                                }
                            } else {
                                //if there is no extra entry found
                                val baseCountryAlpha2 = nameCodeList[entryCounter]
                                if (baseCountryAlpha2 != alpha2NameCode) {
                                    problems.add(
                                        Problem(
                                            title = ProblemTitle.INVALID_VALUE,
                                            desc = "Found $alpha2NameCode, Expected ${nameCodeAndNamePairList[entryCounter]}",
                                            fileName = baseListFileName,
                                            solution = "$alpha2NameCode instead of ${nameCodeAndNamePairList[entryCounter]}. Correct entries and order."
                                        )
                                    )
                                }
                            }

                            //translation
                            if (translation.isNullOrBlank()) {
                                problems.add(
                                    Problem(
                                        title = ProblemTitle.MISSING_PROPERTY,
                                        desc = "Translation for $alpha2NameCode",
                                        fileName = fileName,
                                        solution = "$alpha2NameCode : Add translation"
                                    )
                                )
                            }

                            //verified
                            if (verified.isNullOrBlank()) {
                                problems.add(
                                    Problem(
                                        title = ProblemTitle.MISSING_PROPERTY,
                                        desc = "Verified",
                                        fileName = fileName,
                                        solution = "$alpha2NameCode : Add `$xmlVerifiedKey` = [Y/N] property for $alpha2NameCode"
                                    )
                                )
                            } else {
                                when (verified) {
                                    xmlVerifiedYESValue -> {
                                    }
                                    xmlVerifiedNOValue -> {
                                        unverifiedTranslationCount++
                                    }
                                    else -> {
                                        problems.add(
                                            Problem(
                                                title = ProblemTitle.INVALID_VALUE,
                                                desc = "$verified is not a valid value for verified.",
                                                fileName = fileName,
                                                solution = "$alpha2NameCode : Set value of $xmlVerifiedKey to [$xmlVerifiedYESValue] if translation is verified otherwise set [$xmlVerifiedNOValue]."
                                            )
                                        )
                                    }
                                }
                            }
                            entryCounter++
                        }
                    } else if (name in expectedMessagesKeys) {
                        existingMessageKeys.add(name)
                        val translation = xmlPullParser.getAttributeValue(null, xmlTranslationKey)
                        val verified = xmlPullParser.getAttributeValue(null, xmlVerifiedKey)
                        if (translation.isNullOrBlank()) {
                            problems.add(
                                Problem(
                                    title = ProblemTitle.MISSING_PROPERTY,
                                    desc = "Missing translation for $name",
                                    fileName = fileName,
                                    solution = "$name : Add translation"
                                )
                            )
                        }

                        when (verified) {
                            xmlVerifiedYESValue -> {
                            }
                            xmlVerifiedNOValue -> {
                                unverifiedTranslationCount++
                            }
                            else -> {
                                problems.add(
                                    Problem(
                                        title = ProblemTitle.INVALID_VALUE,
                                        desc = "$verified is not a valid value for verified.",
                                        fileName = fileName,
                                        solution = "$name : Set value of $xmlVerifiedKey to [$xmlVerifiedYESValue] if translation is verified otherwise set [$xmlVerifiedNOValue]."
                                    )
                                )
                            }
                        }
                    }
            }
            event = xmlPullParser.next()
        }

        //unverified translation
        if (unverifiedTranslationCount > 0) {
            problems.add(
                Problem(
                    title = ProblemTitle.UNVERIFIED_ENTRIES,
                    desc = "$unverifiedTranslationCount country names are not verified.",
                    fileName = fileName,
                    solution = "Verify $unverifiedTranslationCount translations and make verified = [Y]"
                )
            )
        }

        //missing message entries
        for(expectedMessageKey in expectedMessagesKeys){
            if(expectedMessageKey  !in existingMessageKeys){
                problems.add(
                    Problem(
                        title = ProblemTitle.MISSING_PROPERTY,
                        desc = "Missing $expectedMessageKey",
                        fileName = fileName,
                        solution = "Add $expectedMessageKey to the `a_message_list`."
                    )
                )
            }
        }

        return problems
    }

    private fun checkBaseList(context: Context): List<Problem> {
        val problems = mutableListOf<Problem>()
        val xmlPullParser = getRawXMLPullParser(context = context, fileName = baseListFileName)

        //if pullParser is null means file is missing.
        if (xmlPullParser == null) {
            problems.add(
                Problem(
                    title = ProblemTitle.MISSING_FILE,
                    desc = "No file named $baseListFileName found.",
                    fileName = baseListFileName,
                    solution = "Add $baseListFileName file under raw/ directory."
                )
            )
            return problems
        }

        var event = xmlPullParser.eventType
        var entryCounter = 1
        var lastCheckedCode = ""
        while (event != XmlPullParser.END_DOCUMENT) {
            val name = xmlPullParser.name
            when (event) {
                XmlPullParser.END_TAG ->
                    if (name == xmlCountryKey) {
                        val alpha2NameCode = xmlPullParser.getAttributeValue(null, xmlAlpha2Key)
                        val alpha3NameCode = xmlPullParser.getAttributeValue(null, xmlAlpha3Key)
                        val phoneCode = xmlPullParser.getAttributeValue(null, xmlPhoneCodeKey)
                        val englishName = xmlPullParser.getAttributeValue(null, xmlEnglishNameKey)

                        //check primary key
                        if (alpha2NameCode.isNullOrBlank()) {
                            problems.add(
                                Problem(
                                    title = ProblemTitle.MISSING_PROPERTY,
                                    desc = "Alpha 2 namecode. ",
                                    fileName = baseListFileName,
                                    solution = "Add Alpha2 name code for Country entry at position #$entryCounter"
                                )
                            )
                        } else {
                            //check alpha 2 format
                            if (!verifyAlphaCodeRegex(alpha2NameCode, 2)) {
                                problems.add(
                                    Problem(
                                        title = ProblemTitle.INVALID_VALUE,
                                        desc = "Alpha2 name code: $alpha2NameCode.",
                                        fileName = baseListFileName,
                                        solution = "$alpha2NameCode : Correct alpha2 code for Country entry at position #$entryCounter. It should be 2 UPPER case alphabetic characters e.g. `IN`"
                                    )
                                )
                            }

                            //duplicate name code check
                            if (nameCodeList.contains(alpha2NameCode)) {
                                problems.add(
                                    Problem(
                                        title = ProblemTitle.DUPLICATE_ENTRY,
                                        desc = "alpha2NameCode : $alpha2NameCode",
                                        fileName = baseListFileName,
                                        solution = "$alpha2NameCode : Remove duplicate entries."
                                    )
                                )
                            }
                            nameCodeList.add(alpha2NameCode)

                            //check order
                            if (lastCheckedCode > alpha2NameCode) {
                                problems.add(
                                    Problem(
                                        title = ProblemTitle.INVALID_ORDER,
                                        desc = "$alpha2NameCode: ($alpha2NameCode) should appear before ($lastCheckedCode) ",
                                        fileName = baseListFileName,
                                        solution = "Correct orders of country ($alpha2NameCode) and/or ($lastCheckedCode)"
                                    )
                                )
                            }
                            lastCheckedCode = alpha2NameCode

                            //alpha 3
                            if (alpha3NameCode.isNullOrBlank()) {
                                problems.add(
                                    Problem(
                                        title = ProblemTitle.MISSING_PROPERTY,
                                        desc = "Alpha3 namecode for $alpha2NameCode",
                                        fileName = baseListFileName,
                                        solution = "$alpha2NameCode : Add Alpha 3 name code"
                                    )
                                )
                            } else if (!verifyAlphaCodeRegex(alpha3NameCode, 3)) {
                                problems.add(
                                    Problem(
                                        title = ProblemTitle.INVALID_VALUE,
                                        desc = "alpha3 name code for $alpha2NameCode",
                                        fileName = baseListFileName,
                                        solution = "$alpha2NameCode : Correct alpha3 code. 3 UPPER case alphabetic characters e.g. `IND`."
                                    )
                                )
                            }

                            //phone code
                            if (phoneCode.isNullOrBlank()) {
                                problems.add(
                                    Problem(
                                        title = ProblemTitle.MISSING_PROPERTY,
                                        desc = "PhoneCode",
                                        fileName = baseListFileName,
                                        solution = "$alpha2NameCode : Add phoneCode"
                                    )
                                )
                            } else {
                                val numericPhoneCode = phoneCode.toIntOrNull()
                                if (numericPhoneCode == null || numericPhoneCode > 999) {
                                    problems.add(
                                        Problem(
                                            title = ProblemTitle.INVALID_VALUE,
                                            desc = "Invalid phoneCode: $phoneCode.",
                                            fileName = baseListFileName,
                                            solution = "$alpha2NameCode : Correct phone code. A number < 1000."
                                        )
                                    )
                                }
                            }

                            //english name
                            if (englishName.isNullOrBlank()) {
                                problems.add(
                                    Problem(
                                        title = ProblemTitle.MISSING_PROPERTY,
                                        desc = "EnglishName",
                                        fileName = baseListFileName,
                                        solution = "$alpha2NameCode : Add English name"
                                    )
                                )
                            }
                        }
                    }
            }
            event = xmlPullParser.next()
        }
        return problems
    }

    private fun verifyAlphaCodeRegex(
        alphaCode: String,
        alphaCodeLength: Int
    ): Boolean {
        val pattern = when (alphaCodeLength) {
            2 -> Regex("^[A-Z]{2}\$")
            3 -> Regex("^[A-Z]{3}\$")
            else -> throw IllegalArgumentException("alphaCodeLength can be 2 or 3 only.")
        }
        return pattern.matchEntire(alphaCode) != null
    }
}
