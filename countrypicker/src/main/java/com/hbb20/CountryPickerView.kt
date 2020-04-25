package com.hbb20

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.hbb20.countrypicker.R
import com.hbb20.countrypicker.config.CPCountryRowConfig
import com.hbb20.countrypicker.config.CPRecyclerViewConfig
import com.hbb20.countrypicker.datagenerator.CPDataStoreGenerator
import com.hbb20.countrypicker.dialog.CPDialogConfig
import com.hbb20.countrypicker.dialog.CPDialogHelper
import com.hbb20.countrypicker.models.CPCountry

class CountryPickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    val dataStore = CPDataStoreGenerator.generate(context)
    val tvCountryInfo: TextView by lazy { findViewById<TextView>(R.id.tvCountryInfo) }
    var rowConfig: CPCountryRowConfig = CPCountryRowConfig()
    var recyclerViewConfig: CPRecyclerViewConfig = CPRecyclerViewConfig()
    var dialogConfig: CPDialogConfig = CPDialogConfig()
    var selectedCountry: CPCountry? = null

    init {
        applyLayout(attrs)
        selectedCountry = dataStore.countryList.first { it.alpha2 == "IN" }
        setOnClickListener {
            launchDialog()
        }
        refresh()
    }

    /**
     * If width is match_parent, 0
     */
    private fun applyLayout(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.cp_country_picker_view, this, true)
        val xmlWidth =
            attrs?.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_width")
        //at run time, match parent value returns LayoutParams.MATCH_PARENT ("-1"), for some android xml preview it returns "fill_parent"
        if (attrs != null && xmlWidth != null && (xmlWidth == LayoutParams.MATCH_PARENT.toString() || xmlWidth == "fill_parent" || xmlWidth == "match_parent")) {
            tvCountryInfo.text = xmlWidth
        } else {
            tvCountryInfo.text = xmlWidth ?: "null"
        }
    }

    private fun launchDialog() {
        val dialogHelper = CPDialogHelper(dataStore, dialogConfig, recyclerViewConfig, rowConfig) {
            selectedCountry = it
            refresh()
        }
        dialogHelper.createDialog(context).show()
    }

    fun refresh() {
        tvCountryInfo.text =
            selectedCountry?.name ?: dataStore.messageGroup.selectionPlaceholderText
    }

    data class State(val country: CPCountry?)
}