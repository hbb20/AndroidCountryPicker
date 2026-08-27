package com.hbb20.androidcountrypicker

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hbb20.androidcountrypicker.databinding.ActivityOpenDialogDirectlyBinding
import com.hbb20.contrypicker.flagpack1.FlagPack1
import com.hbb20.countrypicker.dialog.launchCountryPickerDialog
import com.hbb20.countrypicker.flagprovider.CPFlagImageProvider
import com.hbb20.countrypicker.models.CPCountry

class OpenDialogDirectlyActivity : AppCompatActivity() {
    lateinit var binding: ActivityOpenDialogDirectlyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpenDialogDirectlyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.btnCustomMasterList.setOnClickListener {
            launchCountryPickerDialog(
                customMasterCountries = "ca,us,mx",
            ) { selectedCountry ->
                // your code here
            }
        }

        binding.btnCustomExcludedList.setOnClickListener {
            launchCountryPickerDialog(
                customExcludedCountries = "jp",
            ) { selectedCountry ->
                // your code here
            }
        }

        binding.btnNewCountry.setOnClickListener {
            launchCountryPickerDialog(
                customDataStoreModifier = { dataStore ->
                    dataStore.countryList.add(
                        CPCountry(
                            "pd",
                            "prd",
                            "Paradise",
                            "Angels",
                            "Heart",
                            "20",
                            20,
                            "NW",
                            "NobelWork",
                            "N",
                            "pd",
                            "\uD83C\uDFC1",
                            0,
                            "Paradise",
                        ),
                    )
                },
            ) { selectedCountry ->
                // your code here
            }
        }

        binding.btnChangeCountryName.setOnClickListener {
            launchCountryPickerDialog(
                customDataStoreModifier = { dataStore ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        dataStore.countryList.replaceAll { if (it.alpha2 == "IN") it.copy(name = "Bharat") else it }
                    }
                },
            ) { selectedCountry ->
                // your code here
            }
        }

        binding.btnPreferredCountries.setOnClickListener {
            launchCountryPickerDialog(
                preferredCountryCodes = "IN,JP,US,CA",
            ) { selectedCountry ->
                // your code here
            }
        }

        binding.btnFlagPack1.setOnClickListener {
            launchCountryPickerDialog(
                cpFlagProvider =
                    CPFlagImageProvider(
                        FlagPack1.alpha2ToFlag,
                        FlagPack1.missingFlagPlaceHolder,
                    ),
            ) { selectedCountry ->
                // your code here
            }
        }

        binding.btnFlagNone.setOnClickListener {
            launchCountryPickerDialog(
                cpFlagProvider = null,
            ) { selectedCountry ->
                // your code here
            }
        }

        binding.btnChangeText.setOnClickListener {
            launchCountryPickerDialog(
                primaryTextGenerator = { cpCountry -> "${cpCountry.name} (${cpCountry.alpha2})" },
                secondaryTextGenerator = { cpCountry -> cpCountry.capitalEnglishName },
                highlightedTextGenerator = { cpCountry -> "+${cpCountry.phoneCode}" },
            ) { selectedCountry ->
                // your code here
            }
        }

        binding.btnHideSearch.setOnClickListener {
            launchCountryPickerDialog(
                allowSearch = false,
            ) { selectedCountry ->
                // your code here
            }
        }

        binding.btnAllowClearSelection.setOnClickListener {
            launchCountryPickerDialog(
                allowClearSelection = true,
            ) { selectedCountry ->
                // your code here
            }
        }

        binding.btnDontAllowClearSelection.setOnClickListener {
            launchCountryPickerDialog(
                allowClearSelection = false,
            ) { selectedCountry ->
                // your code here
            }
        }
    }
}
