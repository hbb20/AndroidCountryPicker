package com.hbb20.androidcountrypicker

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import com.hbb20.CountryPickerView
import com.hbb20.androidcountrypicker.databinding.ActivityCountryPickerViewDemoBinding
import com.hbb20.contrypicker.flagpack1.FlagPack1
import com.hbb20.countrypicker.config.CPViewConfig
import com.hbb20.countrypicker.flagprovider.CPFlagImageProvider
import com.hbb20.countrypicker.models.CPCountry
import com.hbb20.countrypicker.view.prepareCustomCountryPickerView

class CountryPickerViewDemoActivity : AppCompatActivity() {
    lateinit var binding: ActivityCountryPickerViewDemoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountryPickerViewDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Country Picker View Demo"
        setupCountryPickerView()
        setupFlagCountryPickers()
        setupCpCustomText()
        setupCustomCountryPickerViewCallback()
        //        setupCustomCountryPickerViewLivedata()
    }

    private fun setupCpCustomText() {
        binding.cpCustomText.cpViewHelper.cpViewConfig.viewTextGenerator = { country ->
            "${country.name} (${country.currencyCode} - ${country.currencySymbol})"
        }
        binding.cpCustomText.cpViewHelper.refreshView()

        binding.cpNoText.cpViewHelper.cpViewConfig.viewTextGenerator = { "" }
        binding.cpNoText.cpViewHelper.refreshView()
    }

    private fun setupFlagCountryPickers() {
        binding.cpFlagPack.changeFlagProvider(
            CPFlagImageProvider(
                FlagPack1.alpha2ToFlag,
                FlagPack1.missingFlagPlaceHolder
            )
        )
        binding.cpNoFlag.changeFlagProvider(null)
    }

    private fun setupCustomCountryPickerViewCallback() {
        // bind views
        val customCPContainer =
            findViewById<ConstraintLayout>(R.id.customCountryPickerViewContainer)
        val customCPSelectedCountryTextView = findViewById<TextView>(R.id.tvSelectedCountry)
        val customCPEmojiTextView = findViewById<TextView>(R.id.tvSelectedCountryEmojiFlag)

        prepareCustomCountryPickerView(
            containerViewGroup = customCPContainer,
            tvSelectedCountryInfo = customCPSelectedCountryTextView,
            tvSelectedCountryEmojiFlag = customCPEmojiTextView,
            initialSelection = CPViewConfig.InitialSelection.AutoDetectCountry()
        ) { selectedCountry: CPCountry? ->
            // listen to change through callback
            // your code to handle selected country
        }
    }

    private fun setupCustomCountryPickerViewLivedata() {
        val lifecycleOwner: LifecycleOwner = this

        // bind views
        val customCPContainer =
            findViewById<ConstraintLayout>(R.id.customCountryPickerViewContainer)
        val customCPSelectedCountryTextView = findViewById<TextView>(R.id.tvSelectedCountry)
        val customCPEmojiTextView = findViewById<TextView>(R.id.tvSelectedCountryEmojiFlag)

        val cpViewHelper = prepareCustomCountryPickerView(
            containerViewGroup = customCPContainer,
            tvSelectedCountryInfo = customCPSelectedCountryTextView,
            tvSelectedCountryEmojiFlag = customCPEmojiTextView
        )

        // observe live data
        cpViewHelper.selectedCountry.observe(
            lifecycleOwner,
            { selectedCountry: CPCountry? ->
                // observe live data
                // your code to handle selected country
            }
        )

        // Modify CPViewConfig if you need. Access cpViewConfig through `cpViewHelper`
        cpViewHelper.cpViewConfig.viewTextGenerator = { cpCountry: CPCountry ->
            "${cpCountry.name} (${cpCountry.alpha2})"
        }
        // make sure to refresh view once view configuration is changed
        cpViewHelper.refreshView()

        // Modify CPDialogConfig if you need. Access cpDialogConfig through `countryPicker.cpViewHelper`
        // cpViewHelper.cpDialogConfig.

        // Modify CPListConfig if you need. Access cpListConfig through `countryPicker.cpViewHelper`
        // cpViewHelper.cpListConfig.

        // Modify CPRowConfig if you need. Access cpRowConfig through `countryPicker.cpViewHelper`
        // cpViewHelper.cpRowConfig.
    }

    private fun setupCountryPickerView() {
        val countryPicker = findViewById<CountryPickerView>(R.id.cpWrapContent)

        // Modify CPViewConfig if you need. Access cpViewConfig through `cpViewHelper`
        countryPicker.cpViewHelper.cpViewConfig.viewTextGenerator = { cpCountry: CPCountry ->
            "${cpCountry.name} (${cpCountry.alpha2})"
        }
        // make sure to refresh view once view configuration is changed
        countryPicker.cpViewHelper.refreshView()

        // Modify CPDialogConfig if you need. Access cpDialogConfig through `countryPicker.cpViewHelper`
        // countryPicker.cpViewHelper.cpDialogConfig.

        // Modify CPListConfig if you need. Access cpListConfig through `countryPicker.cpViewHelper`
        // countryPicker.cpViewHelper.cpListConfig.

        // Modify CPRowConfig if you need. Access cpRowConfig through `countryPicker.cpViewHelper`
        // countryPicker.cpViewHelper.cpRowConfig.
    }
}
