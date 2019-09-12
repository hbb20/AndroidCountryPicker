package com.hbb20.countrypicker

import android.content.Context
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.lang.Exception
import java.util.*

internal lateinit var baseList: HashMap<String, BaseCountry>

fun loadDataStoreFromXML(
    context: Context,
    language: CPLanguage
): CPDataStore {
    loadBaseList(context)
    //    var countries = mutableListOf<CPCountry>()
    //    var tempDialogTitle = ""
    //    var tempSearchHint = ""
    //    var tempNoResultAck = ""
    //    try {
    //        val xmlFactoryObject = XmlPullParserFactory.newInstance()
    //        val xmlPullParser = xmlFactoryObject.newPullParser()
    //        val ins = context.resources.openRawResource(
    //            context.resources
    //                .getIdentifier(
    //                    "ccp_" + language.toString().toLowerCase(Locale.ROOT),
    //                    "raw", context.packageName
    //                )
    //        )
    //        xmlPullParser.setInput(ins, "UTF-8")
    //        var event = xmlPullParser.eventType
    //        while (event != XmlPullParser.END_DOCUMENT) {
    //            val name = xmlPullParser.name
    //            when (event) {
    //                XmlPullParser.START_TAG -> {
    //                }
    //                XmlPullParser.END_TAG -> if (name == "country") {
    //                    val ccpCountry = CCPCountry()
    //                    ccpCountry.setNameCode(
    //                        xmlPullParser.getAttributeValue(
    //                            null,
    //                            "name_code"
    //                        ).toUpperCase()
    //                    )
    //                    ccpCountry.setPhoneCode(xmlPullParser.getAttributeValue(null, "phone_code"))
    //                    ccpCountry.setEnglishName(xmlPullParser.getAttributeValue(null, "english_name"))
    //                    ccpCountry.setName(xmlPullParser.getAttributeValue(null, "name"))
    //                    countries.add(ccpCountry)
    //                } else if (name == "ccp_dialog_title") {
    //                    tempDialogTitle = xmlPullParser.getAttributeValue(null, "translation")
    //                } else if (name == "ccp_dialog_search_hint_message") {
    //                    tempSearchHint = xmlPullParser.getAttributeValue(null, "translation")
    //                } else if (name == "ccp_dialog_no_result_ack_message") {
    //                    tempNoResultAck = xmlPullParser.getAttributeValue(null, "translation")
    //                }
    //            }
    //            event = xmlPullParser.next()
    //        }
    //        loadedLibraryMasterListLanguage = language
    //    } catch (e: XmlPullParserException) {
    //        e.printStackTrace()
    //    } catch (e: IOException) {
    //        e.printStackTrace()
    //    } catch (e: Exception) {
    //        e.printStackTrace()
    //    } finally {
    //
    //    }
    //
    //    //if anything went wrong, countries will be loaded for english language
    //    if (countries.size == 0) {
    //        loadedLibraryMasterListLanguage = CountryCodePicker.Language.ENGLISH
    //        countries = getLibraryMasterCountriesEnglish()
    //    }
    //
    //    dialogTitle = if (tempDialogTitle.length > 0) tempDialogTitle else "Select a country"
    //    searchHintMessage = if (tempSearchHint.length > 0) tempSearchHint else "Search..."
    //    noResultFoundAckMessage =
    //        if (tempNoResultAck.length > 0) tempNoResultAck else "Results not found"
    //    loadedLibraryMaterList = countries
    //
    //    // sort list
    //    Collections.sort<CCPCountry>(loadedLibraryMaterList)
    return CPDataStore(
        cpLanguage = language, masterCountryList = emptyList(), dialogTitle = "",
        searchHint = "", noResultAck = ""
    )
}

/**
 * this will load the base list only if it's not already initialized.
 */
fun loadBaseList(context: Context) {
    if (!::baseList.isInitialized) {
        val countryList = mutableMapOf<String, BaseCountry>()
        val xmlPullParser = getRawXMLPullParser(context = context, fileName = baseListFileName)!!
        var event = xmlPullParser.eventType
        while (event != XmlPullParser.END_DOCUMENT) {
            val name = xmlPullParser.name
            when (event) {
                XmlPullParser.END_TAG ->
                    if (name == xmlCountryKey) {
                        val nameCode = xmlPullParser.getAttributeValue(null, xmlAlpha2Key)
                        val alpha3 = xmlPullParser.getAttributeValue(null, xmlAlpha3Key)
                        val phoneCode = xmlPullParser.getAttributeValue(null, xmlPhoneCodeKey)
                        val englishName = xmlPullParser.getAttributeValue(null, xmlEnglishNameKey)
                        countryList[nameCode] =
                            BaseCountry(englishName, nameCode, alpha3, phoneCode.toShort())
                    }
            }
            event = xmlPullParser.next()
        }
    }
}

/**
 * This will create an XML Pull Parser factory.
 */
fun getRawXMLPullParser(
    context: Context,
    fileName: String
): XmlPullParser? {
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
    }catch (exception: Exception){
        null
    }
}
