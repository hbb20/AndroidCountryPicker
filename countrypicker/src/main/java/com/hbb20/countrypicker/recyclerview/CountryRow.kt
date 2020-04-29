package com.hbb20.countrypicker.recyclerview

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
import com.hbb20.countrypicker.CustomCPFlagImageProvider
import com.hbb20.countrypicker.DefaultEmojiFlagProvider
import com.hbb20.countrypicker.R
import com.hbb20.countrypicker.config.CPCountryRowConfig
import com.hbb20.countrypicker.models.CPCountry
import kotlinx.android.synthetic.main.cp_country_row.view.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
internal class CountryRow @JvmOverloads constructor(
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

    var recyclerViewConfig = CPCountryRowConfig()
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
        tvMainText.text = recyclerViewConfig.mainTextGenerator.invoke(country)
        setSecondaryText(recyclerViewConfig.secondaryTextGenerator?.invoke(country))
        setHighlightedInfo()
        applyFlag()

        //apply config
        applyTextSize()
    }

    private fun setSecondaryText(secondaryText: String?) {
        if (secondaryText == null) {
            tvSecondaryText.visibility = View.GONE
        } else {
            tvSecondaryText.visibility = View.VISIBLE
            tvSecondaryText.text = secondaryText
        }
    }

    private fun setHighlightedInfo() {
        val highlightedText = recyclerViewConfig.highlightedTextGenerator?.invoke(country)
        if (highlightedText != null) {
            tvHighlightedInfo.visibility = View.VISIBLE
            tvHighlightedInfo.text = highlightedText
        } else {
            tvHighlightedInfo.visibility = View.GONE
        }
    }

    private fun applyFlag() {
        val flagProvider = recyclerViewConfig.CPFlagProvider
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
                is CustomCPFlagImageProvider -> {
                    showFlag(FlagView.IMAGE)
                    imgFlag.setImageResource(flagProvider.getFlag(country.alpha2))
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
        val emojiSize: Float =
            if (recyclerViewConfig.secondaryTextGenerator == null) tvMainText.textSize else tvMainText.textSize * 1.3f
        tvEmojiFlag.setTextSize(TypedValue.COMPLEX_UNIT_PX, emojiSize)
    }

}