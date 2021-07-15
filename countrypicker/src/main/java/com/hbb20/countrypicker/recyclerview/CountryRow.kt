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
import com.hbb20.contrypicker.flagprovider.CPFlagImageProvider
import com.hbb20.contrypicker.flagprovider.DefaultEmojiFlagProvider
import com.hbb20.countrypicker.R
import com.hbb20.countrypicker.config.CPRowConfig
import com.hbb20.countrypicker.databinding.CpCountryRowBinding
import com.hbb20.countrypicker.models.CPCountry

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
internal class CountryRow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    enum class FlagView { NONE, EMOJI, IMAGE }

    private var binding: CpCountryRowBinding

    init {
        inflate(context, R.layout.cp_country_row, this)
        binding = CpCountryRowBinding.bind(this.findViewById(R.id.countryRow))
    }

    lateinit var country: CPCountry
        @ModelProp set

    var rowConfig = CPRowConfig()
        @ModelProp set

    @CallbackProp
    fun clickListener(clickListener: ((CPCountry) -> Unit)?) {
        setOnClickListener { clickListener?.invoke(country) }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        binding.countryRow.setOnClickListener(l)
    }

    @AfterPropsSet
    fun updateViews() {
        binding.tvPrimaryText.text = rowConfig.primaryTextGenerator.invoke(country)
        setSecondaryText(rowConfig.secondaryTextGenerator?.invoke(country))
        setHighlightedInfo()
        applyFlag()

        // apply config
        applyTextSize()
    }

    private fun setSecondaryText(secondaryText: String?) {
        if (secondaryText == null) {
            binding.tvSecondaryText.visibility = View.GONE
        } else {
            binding.tvSecondaryText.visibility = View.VISIBLE
            binding.tvSecondaryText.text = secondaryText
        }
    }

    private fun setHighlightedInfo() {
        val highlightedText = rowConfig.highlightedTextGenerator?.invoke(country)
        if (highlightedText != null) {
            binding.tvHighlightedInfo.visibility = View.VISIBLE
            binding.tvHighlightedInfo.text = highlightedText
        } else {
            binding.tvHighlightedInfo.visibility = View.GONE
        }
    }

    private fun applyFlag() {
        val flagProvider = rowConfig.cpFlagProvider
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
                    binding.tvEmojiFlag.text = flagEmoji
                }
                is CPFlagImageProvider -> {
                    showFlag(FlagView.IMAGE)
                    binding.imgFlag.setImageResource(flagProvider.getFlag(country.alpha2))
                }
            }
        } else {
            showFlag(FlagView.NONE)
        }
    }

    private fun showFlag(flagView: FlagView) {
        val viewToShow = when (flagView) {
            FlagView.NONE -> null
            FlagView.EMOJI -> binding.tvEmojiFlag
            FlagView.IMAGE -> binding.imgFlag
        }

        if (viewToShow != null) {
            for (view in setOf(binding.imgFlag, binding.tvEmojiFlag)) {
                if (view == viewToShow) {
                    view.visibility = View.VISIBLE
                } else {
                    view.visibility = View.GONE
                }
            }
        } else {
            binding.flagHolder.visibility = View.GONE
        }
    }

    private fun applyTextSize() {
        val emojiSize: Float =
            if (rowConfig.secondaryTextGenerator == null) binding.tvPrimaryText.textSize else binding.tvPrimaryText.textSize * 1.3f
        binding.tvEmojiFlag.setTextSize(TypedValue.COMPLEX_UNIT_PX, emojiSize)
    }
}
