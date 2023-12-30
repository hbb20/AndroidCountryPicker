package com.hbb20.androidcountrypicker.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hbb20.androidcountrypicker.R
import com.hbb20.androidcountrypicker.compose.theme.DemoTheme
import com.hbb20.contrypicker.flagpack1.FlagPack1
import com.hbb20.countrypicker.compose.CountryFlagLayout
import com.hbb20.countrypicker.compose.CountryPicker
import com.hbb20.countrypicker.compose.CountryPickerDialog
import com.hbb20.countrypicker.compose.rememberAutoDetectedCountryCode
import com.hbb20.countrypicker.compose.rememberCPDataStore
import com.hbb20.countrypicker.compose.rememberCountry
import com.hbb20.countrypicker.compose.rememberCountryFlag
import com.hbb20.countrypicker.flagprovider.CPFlagImageProvider
import com.hbb20.countrypicker.flagprovider.DefaultEmojiFlagProvider

class ComposeDemoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    val (countryCode, setCountryCode) = remember { mutableStateOf<String?>("In") }
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        CountryPicker(alpha2Code = countryCode) {
                            setCountryCode(it?.alpha2)
                        }
                        OutOfBox()
                        AutoDetectedInitialCountry()
                        UseFlagPack()
                        UseCustomFlagDrawables()
                        UseNoFlag()
                        PhoneCodePicker()
                        CountryCurrencyPicker()
                    }
                }
            }
        }
    }

    @Composable
    private fun OutOfBox() {
        CardTemplate(
            "Out of box",
            "This is how it works without any other config changes",
        ) {
            val (countryCode, setCountryCode) = remember { mutableStateOf<String?>(null) }
            CountryPicker(alpha2Code = countryCode) {
                setCountryCode(it?.alpha2)
            }
        }
    }

    @Composable
    private fun AutoDetectedInitialCountry() {
        CardTemplate(
            "Initial auto detected country",
            "Detect country and set it as initial country. " +
                "Optionally pass order of sources (SIM, NETWORK, LOCALE) for country detection.",
        ) {
            val initialCountryCode = rememberAutoDetectedCountryCode()
            // Note: for state management,
            // if you need to hoist the selected country code outside of compose
            // then use CPCountryDetector::detectCountry()
            val (countryCode, setCountryCode) = remember { mutableStateOf(initialCountryCode) }
            CountryPicker(alpha2Code = countryCode) {
                setCountryCode(it?.alpha2)
            }
        }
    }

    @Composable
    private fun UseFlagPack() {
        CardTemplate(
            "Use flag pack",
            "Use flag pack images instead of emoji flags",
        ) {
            val initialCountryCode = rememberAutoDetectedCountryCode()
            val (countryCode, setCountryCode) = remember { mutableStateOf(initialCountryCode) }
            CountryPicker(
                alpha2Code = countryCode,
                flagProvider =
                    CPFlagImageProvider(
                        FlagPack1.alpha2ToFlag,
                        FlagPack1.missingFlagPlaceHolder,
                    ),
            ) {
                setCountryCode(it?.alpha2)
            }
        }
    }

    @Composable
    private fun UseNoFlag() {
        CardTemplate(
            "Dont show flag",
            "Dont show flags",
        ) {
            val initialCountryCode = rememberAutoDetectedCountryCode()
            val (countryCode, setCountryCode) = remember { mutableStateOf(initialCountryCode) }
            CountryPicker(
                alpha2Code = countryCode,
                flagProvider = null,
            ) {
                setCountryCode(it?.alpha2)
            }
        }
    }

    @Composable
    private fun UseCustomFlagDrawables() {
        CardTemplate(
            "Custom flag images",
            "Use your flag drawables",
        ) {
            val initialCountryCode = rememberAutoDetectedCountryCode()
            val (countryCode, setCountryCode) = remember { mutableStateOf(initialCountryCode) }
            CountryPicker(
                alpha2Code = countryCode,
                flagProvider =
                    CPFlagImageProvider(
                        // map of alpha2 to drawable resource id
                        alpha2ToFlag =
                            mapOf(
                                "IN" to R.drawable.ic_flag_green_outlint,
                                "gb" to R.drawable.ic_flag_yellow,
                                "US" to R.drawable.ic_flag_blue,
                            ),
                        // drawable resource id for missing flag
                        missingFlagPlaceHolder = R.drawable.ic_flag,
                    ),
                pickerDialog = { cpDataStore, flagProvider, onDismissRequest, onCountrySelected ->
                    CountryPickerDialog(
                        cpDataStore = cpDataStore,
                        flagProvider = flagProvider,
                        onDismissRequest = onDismissRequest,
                        onCountrySelected = onCountrySelected,
                        quickAccessCountries = listOf("IN", "us", "GB"),
                    )
                },
            ) {
                setCountryCode(it?.alpha2)
            }
        }
    }

    @Composable
    private fun PhoneCodePicker() {
        CardTemplate(
            "Use as Country Phone Code Picker",
            "Commonly used to select country with phone input box. " +
                "Note: This does not auto format the phone number." +
                " Phone number formatting is beyond the scope of this library.",
        ) {
            val initialCountryCode = rememberAutoDetectedCountryCode()
            val cpDataStore = rememberCPDataStore()
            val (countryCode, setCountryCode) = remember { mutableStateOf(initialCountryCode) }
            val (phoneNumber, setPhoneNumber) = remember { mutableStateOf("") }
            val (showPickerDialog, setShowPickerDialog) = remember { mutableStateOf(false) }
            val flagProvider = DefaultEmojiFlagProvider()

            Row(
                modifier = Modifier.height(IntrinsicSize.Min),
                verticalAlignment = CenterVertically,
            ) {
                //
                Row(
                    modifier =
                        Modifier
                            .fillMaxHeight()
                            .clickable { setShowPickerDialog(true) }
                            .padding(8.dp),
                    verticalAlignment = CenterVertically,
                ) {
                    val country = rememberCountry(countryCode)
                    val flag = rememberCountryFlag(country, flagProvider)

                    CountryFlagLayout(flag)
                    Spacer(modifier = Modifier.padding(4.dp))
                    val text =
                        if (country == null) {
                            cpDataStore.messageGroup.selectionPlaceholderText
                        } else {
                            "+${country.phoneCode} "
                        }
                    Text(text = text)
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_drop_down),
                        contentDescription = null,
                        modifier =
                            Modifier
                                .padding(4.dp)
                                .size(16.dp),
                    )
                }
                TextField(
                    value = phoneNumber,
                    onValueChange = { setPhoneNumber(it) },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                )
            }

            if (showPickerDialog) {
                CountryPickerDialog(
                    cpDataStore = cpDataStore,
                    flagProvider = flagProvider,
                    onDismissRequest = { setShowPickerDialog(false) },
                    countryRowLayout = { country, flagProvider, onClicked ->
                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 48.dp)
                                    .clickable { onClicked(country) }
                                    .padding(12.dp),
                            verticalAlignment = CenterVertically,
                        ) {
                            CountryFlagLayout(rememberCountryFlag(country, flagProvider))
                            Spacer(modifier = Modifier.padding(4.dp))
                            Text(
                                text = country.name,
                                modifier = Modifier.weight(1f),
                            )
                            Text(text = "+${country.phoneCode}")
                        }
                    },
                    queryFilter = { country, query ->
                        val properties =
                            listOf<String>(
                                country.name,
                                country.englishName,
                                country.alpha2,
                                country.alpha3,
                                country.phoneCode.toString(),
                            )
                        properties.any { it.contains(query, ignoreCase = true) }
                    },
                ) {
                    setCountryCode(it?.alpha2)
                    // your logic to use phone code
                }
            }
        }
    }

    @Composable
    private fun CountryCurrencyPicker() {
        CardTemplate(
            "Use as Country's Currency Picker",
            "Commonly used to select currency with money input. " +
                "\nNote: This does not auto format the phone number." +
                " Phone number formatting is beyond the scope of this library.",
        ) {
            val initialCountryCode = rememberAutoDetectedCountryCode()
            val (countryCode, setCountryCode) = remember { mutableStateOf(initialCountryCode) }
            val (amount, setAmount) = remember { mutableStateOf("") }
            CountryPicker(
                alpha2Code = countryCode,
                cpDataStore =
                    rememberCPDataStore { originalCountryList ->
                        originalCountryList.sortedBy { it.currencyName }
                            .filterNot { it.currencyCode.isBlank() }
                    },
                selectedCountryLayout = { country, flag, emptySelectionText, modifier, showCountryPickerDialog ->
                    Row(
                        modifier = Modifier.height(IntrinsicSize.Min),
                        verticalAlignment = CenterVertically,
                    ) {
                        Row(
                            modifier =
                                modifier
                                    .fillMaxHeight()
                                    .clickable { showCountryPickerDialog() }
                                    .padding(8.dp),
                            verticalAlignment = CenterVertically,
                        ) {
                            CountryFlagLayout(flag)
                            Spacer(modifier = Modifier.padding(4.dp))
                            val text =
                                if (country == null) {
                                    emptySelectionText
                                } else {
                                    "${country.currencyCode} ${country.currencySymbol}"
                                }
                            Text(text = text)
                        }
                        TextField(
                            value = amount,
                            onValueChange = { setAmount(it) },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        )
                    }
                },
                pickerDialog = { cpDataStore, flagProvider, onDismissRequest, onCountrySelected ->
                    CountryPickerDialog(
                        cpDataStore = cpDataStore,
                        flagProvider = flagProvider,
                        onDismissRequest = onDismissRequest,
                        countryRowLayout = { country, flagProvider, onClicked ->
                            Row(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .heightIn(min = 48.dp)
                                        .clickable { onClicked(country) }
                                        .padding(12.dp),
                                verticalAlignment = CenterVertically,
                            ) {
                                CountryFlagLayout(rememberCountryFlag(country, flagProvider))
                                Spacer(modifier = Modifier.padding(4.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${country.currencyName} (${country.currencySymbol})",
                                        style = MaterialTheme.typography.subtitle2,
                                    )
                                    Text(
                                        text = country.name,
                                        style = MaterialTheme.typography.body2,
                                    )
                                }
                                Text(
                                    text = "${country.currencyCode}",
                                    style = MaterialTheme.typography.subtitle1,
                                )
                            }
                        },
                        queryFilter = { country, query ->
                            val sanitizedQuery = query.trim().removePrefix("+")
                            val properties =
                                listOf<String>(
                                    country.name,
                                    country.englishName,
                                    country.alpha2,
                                    country.alpha3,
                                    country.currencyName,
                                    country.currencyCode,
                                    country.currencySymbol,
                                )
                            properties.any { it.contains(sanitizedQuery, ignoreCase = true) }
                        },
                        onCountrySelected = onCountrySelected,
                    )
                },
            ) {
                setCountryCode(it?.alpha2)
            }
        }
    }

    @Composable
    private fun CardTemplate(
        title: String,
        body: String,
        countryPickerLayout: @Composable () -> Unit,
    ) {
        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.surface)
                    .padding(16.dp),
            backgroundColor = MaterialTheme.colors.surface,
        ) {
            Surface(color = MaterialTheme.colors.surface) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.onSurface,
                    )
                    Text(
                        text = body,
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface,
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    countryPickerLayout()
                }
            }
        }
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MaterialTheme {
        Greeting("Android")
    }
}
