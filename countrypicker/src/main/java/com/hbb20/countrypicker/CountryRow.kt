package com.hbb20.countrypicker

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.hbb20.CPCountry
import kotlinx.android.synthetic.main.cp_country_row.view.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class CountryRow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.cp_country_row, this)
    }

    lateinit var country: CPCountry
        @ModelProp set


    @CallbackProp
    fun clickListener(clickListener: ((CPCountry) -> Unit)?) {
        setOnClickListener { clickListener?.invoke(country) }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        countryRow.setOnClickListener(l)
    }

    @AfterPropsSet
    fun updateViews() {
        tvCountryName.text = country.flagEmoji + country.name
        tvEmoji.text = country.flagEmoji
    }

}