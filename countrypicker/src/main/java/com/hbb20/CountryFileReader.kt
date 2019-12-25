package com.hbb20

import android.content.Context
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

interface CountryFileReading {
    fun loadDataStoreFromXML(
        context: Context,
        language: CPLanguage
    ): CPDataStore

    fun getRawXMLPullParser(
        context: Context,
        fileName: String
    ): XmlPullParser
}

object CountryFileReader : CountryFileReading {
    private lateinit var baseList: HashMap<String, BaseCountry>
    override fun loadDataStoreFromXML(
        context: Context,
        language: CPLanguage
    ): CPDataStore {
        loadBaseList(context)
        val xmlPullParser =
            getRawXMLPullParser(context = context, fileName = language.translationFileName)
        //key: alpha2, value: translation
        val translations = mutableMapOf<String, String>()
        var noResultAckMsg = ""
        var searchHintText = ""
        var dialogTitleText = ""
        var emptySelectionText = ""
        var event = xmlPullParser.eventType
        while (event != XmlPullParser.END_DOCUMENT) {
            val name = xmlPullParser.name
            when (event) {
                XmlPullParser.END_TAG ->
                    when (name) {
                        xmlCountryKey -> {
                            val alpha2NameCode = xmlPullParser.getAttributeValue(
                                null,
                                xmlAlpha2Key
                            )
                            val translation = xmlPullParser.getAttributeValue(
                                null,
                                xmlTranslationKey
                            )
                            translations[alpha2NameCode] = translation
                        }
                        xmlDialogTitleKey ->
                            dialogTitleText = xmlPullParser.getAttributeValue(
                                null,
                                xmlTranslationKey
                            )
                        xmlDialogNoResultAckMessageKey ->
                            noResultAckMsg = xmlPullParser.getAttributeValue(
                                null,
                                xmlTranslationKey
                            )
                        xmlDialogSearchHintMessageKey ->
                            searchHintText = xmlPullParser.getAttributeValue(
                                null,
                                xmlTranslationKey
                            )
                        xmlEmptySelectionTextKey ->
                            emptySelectionText = xmlPullParser.getAttributeValue(
                                null,
                                xmlTranslationKey
                            )

                    }
            }
            event = xmlPullParser.next()
        }

        val masterCountryList = translations.mapNotNull { translation ->
            baseList[translation.key]?.let { baseCountry ->
                CPCountry(
                    name = translation.value,
                    englishName = baseCountry.englishName,
                    alpha2Code = baseCountry.alpha2,
                    alpha3Code = baseCountry.alpha3,
                    phoneCode = baseCountry.phoneCode
                )
            }
        }

        return CPDataStore(
            cpLanguage = language,
            countryList = masterCountryList,
            dialogTitle = dialogTitleText,
            searchHint = searchHintText,
            noResultAck = noResultAckMsg,
            emptySelectionText = emptySelectionText
        )
    }

    /**
     * this will load the base list only if it's not already initialized.
     */
    fun loadBaseList(context: Context) {
        if (!::baseList.isInitialized) {
            val countryList = mutableMapOf<String, BaseCountry>()
            val xmlPullParser = getRawXMLPullParser(
                context = context,
                fileName = xmlBaseListFileName
            )
            var event = xmlPullParser.eventType
            while (event != XmlPullParser.END_DOCUMENT) {
                val name = xmlPullParser.name
                when (event) {
                    XmlPullParser.END_TAG ->
                        if (name == xmlCountryKey) {
                            val nameCode = xmlPullParser.getAttributeValue(null, xmlAlpha2Key)
                            val alpha3 = xmlPullParser.getAttributeValue(null, xmlAlpha3Key)
                            val phoneCode = xmlPullParser.getAttributeValue(
                                null,
                                xmlPhoneCodeKey
                            )
                            val englishName = xmlPullParser.getAttributeValue(
                                null,
                                xmlEnglishNameKey
                            )
                            countryList[nameCode] =
                                BaseCountry(
                                    englishName = englishName,
                                    alpha2 = nameCode,
                                    alpha3 = alpha3,
                                    phoneCode = phoneCode.toShort()
                                )
                        }
                }
                event = xmlPullParser.next()
            }
            baseList = HashMap(countryList)
        }
    }

    /**
     * This will create an XML Pull Parser factory.
     */
    override fun getRawXMLPullParser(
        context: Context,
        fileName: String
    ): XmlPullParser {
        val xmlFactoryObject = XmlPullParserFactory.newInstance()
        val xmlPullParser = xmlFactoryObject.newPullParser()
        return try {
            val ins = context.resources.openRawResource(
                context.resources
                    .getIdentifier(
                        fileName.removeSuffix(".xml"),
                        "raw", context.packageName
                    )
            )
            xmlPullParser.setInput(ins, "UTF-8")
            xmlPullParser
        } catch (exception: Exception) {
            throw exception
        }
    }
}
