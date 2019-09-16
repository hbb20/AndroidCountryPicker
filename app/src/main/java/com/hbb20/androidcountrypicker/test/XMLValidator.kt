package com.hbb20.androidcountrypicker.test

import android.content.Context
import com.hbb20.countrypicker.*
import org.xmlpull.v1.XmlPullParser


class XMLValidator {
    val nameCodeList = mutableListOf<String>()

    fun checkAllXMLFiles(context: Context): List<Problem> {
        val problems = mutableListOf<Problem>()
        problems.addAll(checkBaseList(context))
        for (language in listOf(CPLanguage.ENGLISH, CPLanguage.AFRIKAANS))
            problems.addAll(
                checkTranslationFile(
                    context,
                    language.translationFileName,
                    nameCodeList
                )
            )
        problems.addAll(checkTranslationFile(context, xmlNewLanguageTemplateFileName, nameCodeList))
        return problems
    }

    private fun checkTranslationFile(
        context: Context,
        fileName: String,
        baseNameCodeLise: List<String>
    ): List<Problem> {
        val problems = mutableListOf<Problem>()
        val xmlPullParser = getRawXMLPullParser(context = context, fileName = fileName)

        //if pullParser is null means file is missing.
        if (xmlPullParser == null) {
            problems.add(
                Problem(
                    category = ProblemCategory.MISSING_FILE,
                    fileName = fileName,
                    solution = "[$fileName] Add translation file under raw/ directory. (Use [$xmlNewLanguageTemplateFileName] as a template)"
                )
            )
            return problems
        }
        var event = xmlPullParser.eventType
        var extraEntriesProblemAdded = false
        var entryCounter = 0
        var unverifiedTranslationCount = 0
        val existingMessageKeys = mutableSetOf<String>()
        val expectedMessagesKeys = setOf(
            xmlDialogNoResultAckMessageKey,
            xmlDialogSearchHintMessageKey,
            xmlDialogTitleKey,
            xmlEmptySelectionText
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
                                    category = ProblemCategory.MISSING_PROPERTY,
                                    fileName = fileName,
                                    solution = "[#$entryCounter] Add Alpha2 name code for entry."
                                )
                            )
                        } else {
                            //report extra entries
                            if (entryCounter > baseNameCodeLise.size) {
                                if (!extraEntriesProblemAdded) {
                                    problems.add(
                                        Problem(
                                            category = ProblemCategory.EXTRA_ENTRIES,
                                            fileName = fileName,
                                            solution = "Remove extra entries: Make sure [$fileName] is in sync with [$xmlBaseListFileName]."
                                        )
                                    )
                                    extraEntriesProblemAdded = true
                                }
                            } else {
                                //if there is no extra entry found, then check name code is in sync with base list
                                val baseCountryAlpha2 = nameCodeList[entryCounter]
                                if (baseCountryAlpha2 != alpha2NameCode) {
                                    problems.add(
                                        Problem(
                                            category = ProblemCategory.INVALID_ORDER,
                                            fileName = fileName,
                                            solution = "[$alpha2NameCode] found instead of [${baseNameCodeLise[entryCounter]}]. "
                                        )
                                    )
                                }
                            }

                            //translation
                            if (translation.isNullOrBlank()) {
                                problems.add(
                                    Problem(
                                        category = ProblemCategory.MISSING_PROPERTY,
                                        fileName = fileName,
                                        solution = "[$alpha2NameCode -> $xmlTranslationKey]"
                                    )
                                )
                            } else if (translation.contains(xmlTodoTag) && fileName != xmlNewLanguageTemplateFileName) {
                                // if xmlTodoTag was not removed then translation is not update (except template file)
                                problems.add(
                                    Problem(
                                        category = ProblemCategory.MISSING_PROPERTY,
                                        fileName = fileName,
                                        solution = "[$alpha2NameCode -> $xmlTranslationKey] : Remove `$xmlTodoTag` and add valid translation."
                                    )
                                )
                            }

                            //verified
                            if (verified.isNullOrBlank()) {
                                problems.add(
                                    Problem(
                                        category = ProblemCategory.MISSING_PROPERTY,
                                        fileName = fileName,
                                        solution = "[$alpha2NameCode -> $xmlVerifiedKey]"
                                    )
                                )
                            } else {
                                when (verified) {
                                    xmlVerifiedYESValue -> {
                                    }
                                    xmlVerifiedNOValue -> {
                                        unverifiedTranslationCount++
                                    }
                                    null -> {
                                        problems.add(
                                            Problem(
                                                category = ProblemCategory.MISSING_PROPERTY,
                                                fileName = fileName,
                                                solution = "[$alpha2NameCode -> $xmlVerifiedKey]"
                                            )
                                        )
                                    }
                                    else -> {
                                        problems.add(
                                            Problem(
                                                category = ProblemCategory.INVALID_VALUE,
                                                fileName = fileName,
                                                solution = "[$alpha2NameCode -> $xmlVerifiedKey]"
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
                        if (translation.isNullOrBlank()) {
                            problems.add(
                                Problem(
                                    category = ProblemCategory.MISSING_PROPERTY,
                                    fileName = fileName,
                                    solution = "[$name -> $xmlTranslationKey]"
                                )
                            )
                        } else if (translation.contains(xmlTodoTag) && fileName != xmlNewLanguageTemplateFileName) {
                            // if xmlTodoTag was not removed then translation is not update (except template file)
                            problems.add(
                                Problem(
                                    category = ProblemCategory.MISSING_PROPERTY,
                                    fileName = fileName,
                                    solution = "[$name -> $xmlTranslationKey] : Remove `$xmlTodoTag` and add valid translation."
                                )
                            )
                        }

                        if (translation.isNullOrBlank()) {
                            problems.add(
                                Problem(
                                    category = ProblemCategory.MISSING_PROPERTY,
                                    fileName = fileName,
                                    solution = "[$name] : Add translation."
                                )
                            )
                        }

                        when (xmlPullParser.getAttributeValue(null, xmlVerifiedKey)) {
                            xmlVerifiedYESValue -> {
                            }
                            xmlVerifiedNOValue -> {
                                unverifiedTranslationCount++
                            }
                            null -> {
                                problems.add(
                                    Problem(
                                        category = ProblemCategory.MISSING_PROPERTY,
                                        fileName = fileName,
                                        solution = "[$name -> $xmlVerifiedKey]"
                                    )
                                )
                            }
                            else -> {
                                problems.add(
                                    Problem(
                                        category = ProblemCategory.INVALID_VALUE,
                                        fileName = fileName,
                                        solution = "[$name -> $xmlVerifiedKey]"
                                    )
                                )
                            }
                        }
                    }
            }
            event = xmlPullParser.next()
        }

        //unverified translation
        if (unverifiedTranslationCount > 0 && fileName != xmlNewLanguageTemplateFileName) {
            problems.add(
                Problem(
                    category = ProblemCategory.UNVERIFIED_ENTRIES,
                    fileName = fileName,
                    solution = "[$unverifiedTranslationCount entries] needs translation verification."
                )
            )
        }

        //missing message entries
        for (expectedMessageKey in expectedMessagesKeys) {
            if (expectedMessageKey !in existingMessageKeys) {
                problems.add(
                    Problem(
                        category = ProblemCategory.MISSING_PROPERTY,
                        fileName = fileName,
                        solution = "[$expectedMessageKey] : Add message entry."
                    )
                )
            }
        }

        return problems
    }

    private fun checkBaseList(context: Context): List<Problem> {
        val problems = mutableListOf<Problem>()
        val xmlPullParser = getRawXMLPullParser(context = context, fileName = xmlBaseListFileName)

        //if pullParser is null means file is missing.
        if (xmlPullParser == null) {
            problems.add(
                Problem(
                    category = ProblemCategory.MISSING_FILE,
                    fileName = xmlBaseListFileName,
                    solution = "Add $xmlBaseListFileName file under raw/ directory."
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
                                    category = ProblemCategory.MISSING_PROPERTY,
                                    fileName = xmlBaseListFileName,
                                    solution = "[#$entryCounter] Add $xmlAlpha2Key for entry"
                                )
                            )
                        } else {
                            //check alpha 2 format
                            if (!verifyAlphaCodeRegex(alpha2NameCode, 2)) {
                                problems.add(
                                    Problem(
                                        category = ProblemCategory.INVALID_VALUE,
                                        fileName = xmlBaseListFileName,
                                        solution = "[$alpha2NameCode] : Correct alpha2 code for entry #$entryCounter."
                                    )
                                )
                            }

                            //duplicate name code check
                            if (nameCodeList.contains(alpha2NameCode)) {
                                problems.add(
                                    Problem(
                                        category = ProblemCategory.DUPLICATE_ENTRY,
                                        fileName = xmlBaseListFileName,
                                        solution = "[$alpha2NameCode] : Remove duplicate entries."
                                    )
                                )
                            }
                            nameCodeList.add(alpha2NameCode)

                            //check order
                            if (lastCheckedCode > alpha2NameCode) {
                                problems.add(
                                    Problem(
                                        category = ProblemCategory.INVALID_ORDER,
                                        fileName = xmlBaseListFileName,
                                        solution = "Correct order. [$alpha2NameCode] should appear before [$lastCheckedCode]."
                                    )
                                )
                            }
                            lastCheckedCode = alpha2NameCode

                            //alpha 3
                            if (alpha3NameCode.isNullOrBlank()) {
                                problems.add(
                                    Problem(
                                        category = ProblemCategory.MISSING_PROPERTY,
                                        fileName = xmlBaseListFileName,
                                        solution = "[$alpha2NameCode] : Add Alpha 3 name code"
                                    )
                                )
                            } else if (!verifyAlphaCodeRegex(alpha3NameCode, 3)) {
                                problems.add(
                                    Problem(
                                        category = ProblemCategory.INVALID_VALUE,
                                        fileName = xmlBaseListFileName,
                                        solution = "[$alpha2NameCode] : Correct alpha3 code."
                                    )
                                )
                            }

                            //phone code
                            if (phoneCode.isNullOrBlank()) {
                                problems.add(
                                    Problem(
                                        category = ProblemCategory.MISSING_PROPERTY,
                                        fileName = xmlBaseListFileName,
                                        solution = "[$alpha2NameCode] : Add phoneCode"
                                    )
                                )
                            } else {
                                val numericPhoneCode = phoneCode.toIntOrNull()
                                if (numericPhoneCode == null || numericPhoneCode > 999) {
                                    problems.add(
                                        Problem(
                                            category = ProblemCategory.INVALID_VALUE,
                                            fileName = xmlBaseListFileName,
                                            solution = "[$alpha2NameCode] : Correct phone code."
                                        )
                                    )
                                }
                            }

                            //english name
                            if (englishName.isNullOrBlank()) {
                                problems.add(
                                    Problem(
                                        category = ProblemCategory.MISSING_PROPERTY,
                                        fileName = xmlBaseListFileName,
                                        solution = "[$alpha2NameCode] : Add English name"
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
