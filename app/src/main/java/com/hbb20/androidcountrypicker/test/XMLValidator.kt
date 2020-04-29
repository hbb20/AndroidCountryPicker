package com.hbb20.androidcountrypicker.test

import android.content.Context
import com.hbb20.countrypicker.models.CPLanguage
import com.hbb20.countrypicker.models.xmlLanguageFileName
import com.hbb20.countrypicker.models.xmlNewLanguageTemplateFileName


class XMLValidator {
    val nameCodeList = mutableListOf<String>()

    fun hasAnyCriticalIssue(context: Context): Boolean {
        val problems = checkAllXMLFiles(context)
        return problems.any { it.category != ProblemCategory.UNVERIFIED_ENTRIES }
    }

    fun checkAllXMLFiles(context: Context): List<Problem> {
        val problems = mutableListOf<Problem>()
        problems.addAll(checkLanguageOrder())
        problems.addAll(checkBaseList(context))
        for (language in CPLanguage.values())
            problems.addAll(
                checkTranslationFile(
                    context,
                    language.translationFileName,
                    nameCodeList
                )
            )
        problems.addAll(
            checkTranslationFile(
                context,
                xmlNewLanguageTemplateFileName, nameCodeList
            )
        )
        return problems
    }

    private fun checkLanguageOrder(): Collection<Problem> {
        val problems = mutableListOf<Problem>()
        var lastLanguage: CPLanguage? = null
        for (language in CPLanguage.values()) {
            lastLanguage?.let {
                if (it.name > language.name) {
                    problems.add(
                        Problem(
                            category = ProblemCategory.INVALID_ORDER,
                            fileName = xmlLanguageFileName,
                            solution = "Reorder ${it.name} and ${language.name}."
                        )
                    )
                }
            }
            lastLanguage = language
        }
        return problems
    }

    private fun checkTranslationFile(
        context: Context,
        fileName: String,
        baseNameCodeLise: List<String>
    ): List<Problem> {
        val problems = mutableListOf<Problem>()
        return problems
    }

    private fun checkBaseList(context: Context): List<Problem> {
        val problems = mutableListOf<Problem>()
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
