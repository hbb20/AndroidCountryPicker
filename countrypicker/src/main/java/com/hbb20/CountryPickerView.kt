package com.hbb20

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.hbb20.countrypicker.R
import com.hbb20.countrypicker.config.CPInitialSelectionMode
import com.hbb20.countrypicker.config.CPListConfig
import com.hbb20.countrypicker.config.CPRowConfig
import com.hbb20.countrypicker.config.CPViewConfig
import com.hbb20.countrypicker.datagenerator.CPDataStoreGenerator
import com.hbb20.countrypicker.dialog.CPDialogConfig
import com.hbb20.countrypicker.dialog.CPDialogHelper
import com.hbb20.countrypicker.helper.CPCountryDetector
import com.hbb20.countrypicker.helper.readDialogConfigFromAttrs
import com.hbb20.countrypicker.helper.readListConfigFromAttrs
import com.hbb20.countrypicker.helper.readViewConfigFromAttrs
import com.hbb20.countrypicker.models.CPCountry
import java.util.*

class CountryPickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    val dataStore = CPDataStoreGenerator.generate(context)
    val tvCountryInfo: TextView by lazy { findViewById<TextView>(R.id.tvCountryInfo) }
    val tvEmojiFlag: TextView by lazy { findViewById<TextView>(R.id.tvEmojiFlag) }
    var rowConfig: CPRowConfig = CPRowConfig()
    var viewConfig: CPViewConfig = CPViewConfig()
    var listConfig: CPListConfig = CPListConfig()
    var dialogConfig: CPDialogConfig = CPDialogConfig()
    var selectedCountry: CPCountry? = null
    val countryDetector = CPCountryDetector(context)

    init {
        attrs?.let { readAttrs(it) }
        //        selectedCountry = dataStore.countryList.first { it.alpha2 == "IN" }
        selectInitialCountry()
        setOnClickListener { launchDialog() }
        refresh()
    }

    /**
     * Sets selected country based on InitialSelectionMode
     */
    private fun selectInitialCountry() {
        selectedCountry = when (viewConfig.initialSelectionMode) {
            CPInitialSelectionMode.Empty -> null
            CPInitialSelectionMode.AutoDetectCountry -> autoDetectCountry()
            CPInitialSelectionMode.SpecificCountry -> dataStore.countryList.firstOrNull {
                it.alpha2.equals(
                    viewConfig.initialSpecificCountry,
                    true
                )
            }
        }
    }

    private fun autoDetectCountry(): CPCountry? {
        val detectedAlpha2 =
            if (isInEditMode) "US" else countryDetector.detectCountry(viewConfig.countryDetectSources)
        val detectedCountry = dataStore.countryList.firstOrNull {
            it.alpha2.toLowerCase(Locale.ROOT) == detectedAlpha2?.toLowerCase(
                Locale.ROOT
            )
        }
        return detectedCountry
    }

    private fun readAttrs(attrs: AttributeSet) {
        applyLayout(attrs)
        val styledAttrs =
            context.theme.obtainStyledAttributes(attrs, R.styleable.CountryPickerView, 0, 0)
        styledAttrs?.let {
            dialogConfig = readDialogConfigFromAttrs(styledAttrs)
            listConfig = readListConfigFromAttrs(styledAttrs)
            viewConfig = readViewConfigFromAttrs(styledAttrs)
            styledAttrs.recycle()
        }
    }

    /**
     * If width is match_parent, 0
     */
    private fun applyLayout(attrs: AttributeSet) {
        val xmlWidth =
            attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_width")
        val wrapContentValues =
            setOf(ViewGroup.LayoutParams.WRAP_CONTENT.toString(), "wrap_content")
        val layoutFile =
            if (xmlWidth in wrapContentValues) R.layout.cp_country_picker_view
            else R.layout.cp_country_picker_view_constrained
        LayoutInflater.from(context)
            .inflate(layoutFile, this, true)
    }

    private fun launchDialog() {
        val dialogHelper = CPDialogHelper(dataStore, dialogConfig, listConfig, rowConfig) {
            selectedCountry = it
            refresh()
        }
        dialogHelper.createDialog(context).show()
    }

    fun refresh() {
        tvCountryInfo.text =
            selectedCountry?.name ?: dataStore.messageGroup.selectionPlaceholderText
        refreshFlag()
    }

    /**
     * Show empty string if null country
     */
    private fun refreshFlag() {
        val country = selectedCountry
        tvEmojiFlag.text = if (country == null) {
            " "
        } else {
            country.flagEmoji
        }
    }

    data class State(val country: CPCountry?)
}