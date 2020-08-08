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
import com.hbb20.countrypicker.dialog.CPDialogHelper
import com.hbb20.countrypicker.helper.CPCountryDetector
import com.hbb20.countrypicker.models.CPCountry
import com.hbb20.countrypicker.models.CPDataStore
import timber.log.Timber
import java.util.*

class CountryPickerViewHelper(
    val context: Context,
    val dataStore: CPDataStore,
    val viewConfig: CPViewConfig,
    val dialogConfig: CPDialogConfig,
    val listConfig: CPListConfig,
    val rowConfig: CPRowConfig,
    val isInEditMode: Boolean = false
) {
    private val _selectedCountry = MutableLiveData<CPCountry>()

    val selectedCountry: LiveData<CPCountry?> = _selectedCountry
    val countryDetector = CPCountryDetector(context)
    var attachedViewComponentGroup: ViewComponentGroup? = null

    init {
        setInitialCountry(
            viewConfig.initialSelectionMode,
            viewConfig.initialSpecificCountry,
            viewConfig.countryDetectSources
        )

        selectedCountry.observeForever { selectedCountry: CPCountry? ->
            refreshDataOnView()
        }
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
    private fun setCountryForAlphaCode(countryCode: String?) {
        val country = dataStore.countryList.firstOrNull {
            it.alpha2.equals(countryCode, true) || it.alpha3.equals(countryCode, true)
        }
        _selectedCountry.postValue(country)
    }

    private fun clearSelection() {
        _selectedCountry.postValue(null)
    }

    fun setAutoDetectedCountry(countryDetectSources: List<CPCountryDetector.Source> = viewConfig.countryDetectSources) {
        val detectedAlpha2 =
            if (isInEditMode) "US" else countryDetector.detectCountry(countryDetectSources)
        val detectedCountry = dataStore.countryList.firstOrNull {
            it.alpha2.toLowerCase(Locale.ROOT) == detectedAlpha2?.toLowerCase(
                Locale.ROOT
            )
        }
        _selectedCountry.postValue(detectedCountry)
    }

    fun launchDialog() {
        val dialogHelper = CPDialogHelper(dataStore, dialogConfig, listConfig, rowConfig) {
            _selectedCountry.postValue(it)
        }
        dialogHelper.createDialog(context).show()
    }

    fun attachViewComponents(
        container: ViewGroup,
        tvCountryInfo: TextView,
        tvEmojiFlag: TextView? = null,
        imgFlag: ImageView? = null
    ) {
        container.setOnClickListener { launchDialog() }
        this.attachedViewComponentGroup
        refreshDataOnView()
    }

    fun refreshDataOnView() {
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