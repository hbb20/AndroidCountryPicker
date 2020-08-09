package com.hbb20.countrypicker.view

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.emoji.text.EmojiCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hbb20.countrypicker.CPFlagImageProvider
import com.hbb20.countrypicker.DefaultEmojiFlagProvider
import com.hbb20.countrypicker.config.*
import com.hbb20.countrypicker.datagenerator.CPDataStoreGenerator
import com.hbb20.countrypicker.dialog.CPDialogHelper
import com.hbb20.countrypicker.helper.CPCountryDetector
import com.hbb20.countrypicker.models.CPCountry
import com.hbb20.countrypicker.models.CPDataStore
import timber.log.Timber
import java.util.*

class CountryPickerViewHelper(
    val context: Context,
    val dataStore: CPDataStore = CPDataStoreGenerator.generate(context),
    val viewConfig: CPViewConfig = CPViewConfig(),
    val dialogConfig: CPDialogConfig = CPDialogConfig(),
    val listConfig: CPListConfig = CPListConfig(),
    val rowConfig: CPRowConfig = CPRowConfig(cpFlagProvider = viewConfig.cpFlagProvider),
    val isInEditMode: Boolean = false
) {
    private val _selectedCountry = MutableLiveData<CPCountry>()
    val selectedCountry: LiveData<CPCountry?> = _selectedCountry
    val countryDetector = CPCountryDetector(context)
    var attachedViewComponentGroup: ViewComponentGroup? = null
    var onCountryUpdateListener: ((CPCountry?) -> Unit)? = null

    init {
        setInitialCountry(
            viewConfig.initialSelectionMode,
            viewConfig.initialSpecificCountry,
            viewConfig.countryDetectSources
        )
    }

    fun setInitialCountry(
        initialSelectionMode: CPInitialSelectionMode,
        initialSpecificCountry: String?,
        countryDetectSources: List<CPCountryDetector.Source>
    ) {
        when (initialSelectionMode) {
            CPInitialSelectionMode.Empty -> clearSelection()
            CPInitialSelectionMode.AutoDetectCountry -> setAutoDetectedCountry(countryDetectSources)
            CPInitialSelectionMode.SpecificCountry -> setCountryForAlphaCode(initialSpecificCountry)
        }
    }

    /**
     * CountryCode can be alpha2 or alpha3 code
     */
    fun setCountryForAlphaCode(countryCode: String?) {
        val country = dataStore.countryList.firstOrNull {
            it.alpha2.equals(countryCode, true) || it.alpha3.equals(countryCode, true)
        }
        setCountry(country)
    }

    fun clearSelection() {
        setCountry(null)
    }

    fun setAutoDetectedCountry(countryDetectSources: List<CPCountryDetector.Source> = viewConfig.countryDetectSources) {
        val detectedAlpha2 =
            if (isInEditMode) "US" else countryDetector.detectCountry(countryDetectSources)
        val detectedCountry = dataStore.countryList.firstOrNull {
            it.alpha2.toLowerCase(Locale.ROOT) == detectedAlpha2?.toLowerCase(
                Locale.ROOT
            )
        }
        setCountry(detectedCountry)
    }

    fun launchDialog() {
        val dialogHelper = CPDialogHelper(dataStore, dialogConfig, listConfig, rowConfig) {
            setCountry(it)
        }
        dialogHelper.createDialog(context).show()
    }

    fun attachViewComponents(
        container: ViewGroup,
        tvCountryInfo: TextView,
        tvEmojiFlag: TextView? = null,
        imgFlag: ImageView? = null
    ) {
        this.attachedViewComponentGroup =
            ViewComponentGroup(container, tvCountryInfo, tvEmojiFlag, imgFlag)
        attachedViewComponentGroup?.container?.setOnClickListener { launchDialog() }
        refreshView()
    }

    fun setCountry(cpCountry: CPCountry?) {
        _selectedCountry.value = cpCountry
        refreshView()
        onCountryUpdateListener?.invoke(cpCountry)
    }

    fun refreshView() {
        val selectedCountry = _selectedCountry.value
        attachedViewComponentGroup?.apply {
            // text
            tvCountryInfo.text =
                selectedCountry?.name ?: dataStore.messageGroup.selectionPlaceholderText

            val flagProvider = viewConfig.cpFlagProvider
            if (flagProvider is DefaultEmojiFlagProvider) {
                val flagEmoji = when {
                    flagProvider.useEmojiCompat -> EmojiCompat.get()
                        .process(selectedCountry?.flagEmoji ?: " ")
                    else -> selectedCountry?.flagEmoji ?: " "
                }
                tvEmojiFlag?.setText(flagEmoji) ?: kotlin.run {
                    Timber.e("No tvEmojiFlag provided to load emoji flag")
                }
            } else if (flagProvider is CPFlagImageProvider) {
                val flagResId = selectedCountry?.let { flagProvider.getFlag(it.alpha2) }
                when {
                    flagResId != null -> {
                        imgFlag?.isVisible = true
                        imgFlag?.setImageResource(flagResId) ?: kotlin.run {
                            Timber.e("No imgFlag provided to load flag image")
                        }
                    }
                    else -> {
                        imgFlag?.isVisible = false
                    }
                }
            }
        }
    }

    class ViewComponentGroup(
        val container: ViewGroup,
        val tvCountryInfo: TextView,
        val tvEmojiFlag: TextView? = null,
        val imgFlag: ImageView? = null
    )
}