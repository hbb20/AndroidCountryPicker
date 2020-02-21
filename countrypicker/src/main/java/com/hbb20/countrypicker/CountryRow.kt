package com.hbb20.countrypicker

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import androidx.emoji.text.EmojiCompat
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.hbb20.CPCountry
import com.hbb20.countrypicker.config.CPRecyclerViewConfig
import kotlinx.android.synthetic.main.cp_country_row.view.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class CountryRow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    enum class FlagView { NONE, EMOJI, IMAGE }

    init {
        inflate(context, R.layout.cp_country_row, this)
    }

    lateinit var country: CPCountry
        @ModelProp set

    var recyclerViewConfig = CPRecyclerViewConfig()
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
        tvCountryName.text = CPTextUtil.prepare(recyclerViewConfig.rowTextTemplate, country)
        setHighlightedInfo()
        applyFlag()

        //apply config
        applyTextSize()
    }

    private fun setHighlightedInfo() {
        val template = recyclerViewConfig.highlightedTextTemplate
        if (template != null) {
            tvHighlightedInfo.visibility = View.VISIBLE
            tvHighlightedInfo.text = CPTextUtil.prepare(template, country)
        } else {
            tvHighlightedInfo.visibility = View.GONE
        }
    }

    private fun applyFlag() {
        val flagProvider = recyclerViewConfig.flagProvider
        if (flagProvider != null) {
            when (flagProvider) {
                is DefaultEmojiFlagProvider -> {
                    showFlag(FlagView.EMOJI)
                    val flagEmoji =
                        if (flagProvider.useEmojiCompat) {
                            EmojiCompat.get().process(country.flagEmoji)
                        } else {
                            country.flagEmoji
                        }
                    tvEmojiFlag.text = flagEmoji
                }
                is CustomFlagImageProvider -> {
                    showFlag(FlagView.IMAGE)
                    imgFlag.setImageResource(flagProvider.getFlagResIdForAlphaCode(country.alpha2))
                }
            }
        } else {
            showFlag(FlagView.NONE)
        }
    }

    private fun showFlag(flagView: FlagView) {
        val viewToShow = when (flagView) {
            FlagView.NONE -> null
            FlagView.EMOJI -> tvEmojiFlag
            FlagView.IMAGE -> imgFlag
        }

        if (viewToShow != null) {
            for (view in setOf(imgFlag, tvEmojiFlag)) {
                if (view == viewToShow) {
                    view.visibility = View.VISIBLE
                } else {
                    view.visibility = View.GONE
                }
            }
        } else {
            flagHolder.visibility = View.GONE
        }
    }

    private fun applyTextSize() {
        tvCountryName.setTextSize(TypedValue.COMPLEX_UNIT_SP, recyclerViewConfig.rowFontSizeInSP)
        tvEmojiFlag.setTextSize(TypedValue.COMPLEX_UNIT_SP, recyclerViewConfig.rowFontSizeInSP)
        tvHighlightedInfo.setTextSize(
            TypedValue.COMPLEX_UNIT_SP,
            recyclerViewConfig.rowFontSizeInSP
        )
    }

}