package com.hbb20

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.hbb20.countrypicker.R
import com.hbb20.countrypicker.models.CPCountry

class CountryPickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    //    val dataStore = CPDataStoreGenerator.generate(resources)
    val tvCountryInfo: TextView by lazy { findViewById<TextView>(R.id.tvCountryInfo) }

    init {
        LayoutInflater.from(context).inflate(R.layout.cp_country_picker_view, this, true)

        //        val country = dataStore.countryList[0]
        tvCountryInfo.text = resources.getString(R.string.cp_selection_place_holder)
    }

    data class State(val country: CPCountry?)
}