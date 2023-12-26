package com.hbb20.countrypicker.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.emoji.text.EmojiCompat
import com.hbb20.countrypicker.R
import com.hbb20.countrypicker.compose.CountryFlag.EmojiFlag
import com.hbb20.countrypicker.compose.CountryFlag.ImageFlag
import com.hbb20.countrypicker.datagenerator.CPDataStoreGenerator
import com.hbb20.countrypicker.datagenerator.CountryFileReading
import com.hbb20.countrypicker.flagprovider.CPFlagImageProvider
import com.hbb20.countrypicker.flagprovider.CPFlagProvider
import com.hbb20.countrypicker.flagprovider.DefaultEmojiFlagProvider
import com.hbb20.countrypicker.helper.CPCountryDetector
import com.hbb20.countrypicker.models.CPCountry
import com.hbb20.countrypicker.models.CPDataStore

@OptIn(ExperimentalMaterialApi::class)
val countryMasterListTransformer = { countryList: List<CPCountry> ->
    var finalList = countryList
    // to keep only a few countries
    val countriesToInclude =
        hashSetOf("IN", "PK", "US", "GB", "CA", "AU", "NZ", "DE", "FR", "ES", "IT")
    finalList = finalList.filter { countriesToInclude.contains(it.alpha2) }

    // to remove only a few countries
    val countriesToExclude = hashSetOf("GB")
    finalList = finalList.filterNot { countriesToExclude.contains(it.alpha2) }

    // to sort by a particular order
    finalList = finalList.sortedBy { it.phoneCode.toString() }

    // to modify any property of country
    finalList = finalList.map { country ->
        if (country.alpha2 == "IN") {
            country.copy(name = "Bharat")
        } else {
            country
        }
    }

    finalList
}

@Composable
fun CountryPicker(
    alpha2Code: String?,
    modifier: Modifier = Modifier,
    cpDataStore: CPDataStore = rememberCPDataStore(),
    flagProvider: CPFlagProvider? = DefaultEmojiFlagProvider(),
    selectedCountryLayout: @Composable ((
        country: CPCountry?,
        countryFlag: CountryFlag?,
        emptySelectionText: String,
        modifier: Modifier,
        showCountryPickerDialog: () -> Unit
    ) -> Unit) = { country, countryFlag, emptySelectionText, modifier, showCountryPickerDialog ->
        DefaultSelectedCountryLayout(
            country,
            countryFlag,
            emptySelectionText,
            modifier,
            showCountryPickerDialog
        )
    },
    pickerDialog: @Composable ((
        cpDataStore: CPDataStore,
        flagProvider: CPFlagProvider?,
        onDismissRequest: () -> Unit,
        onCountrySelected: (CPCountry?) -> Unit,
    ) -> Unit) = { cpDataStore,
                   flagProvider,
                   onDismissRequest,
                   onCountrySelected ->
        CountryPickerDialog(
            cpDataStore = cpDataStore,
            flagProvider = flagProvider,
            onDismissRequest = onDismissRequest,
            onCountrySelected = onCountrySelected
        )
    },
    onCountrySelected: (CPCountry?) -> Unit,
) {
    val (showPickerDialog, setShowPickerDialog) = remember { mutableStateOf(false) }
    val launchPickerDialog = remember { { setShowPickerDialog(true) } }
    val country = rememberCountry(alpha2Code, cpDataStore)
    val countryFlag = rememberCountryFlag(country, flagProvider)
    selectedCountryLayout(
        country,
        countryFlag,
        cpDataStore.messageGroup.selectionPlaceholderText,
        modifier,
        launchPickerDialog,
    )
    if (showPickerDialog) {
        pickerDialog(
            cpDataStore,
            flagProvider,
            { setShowPickerDialog(false) }) {
            onCountrySelected(it)
            setShowPickerDialog(false)
        }
    }
}


@Preview
@Composable
fun CountryLayoutPreview() {
    val (selectedCountry, setSelectedCountry) = remember { mutableStateOf<String?>("IN") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        CountryPicker(
            alpha2Code = selectedCountry,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp),
            onCountrySelected = { setSelectedCountry(it?.alpha2) }
        )
    }
}

@Composable
private fun DefaultSelectedCountryLayout(
    country: CPCountry?,
    countryFlag: CountryFlag?,
    emptySelectionText: String,
    modifier: Modifier,
    showCountryPickerDialog: () -> Unit,
) {
    val textToShow = country?.name ?: emptySelectionText
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable { showCountryPickerDialog() }
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        val textStyle = MaterialTheme.typography.body1
        if (countryFlag != null) {
            CountryFlagLayout(countryFlag, emojiFlagTextStyle = textStyle)
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(text = textToShow, style = textStyle, color = LocalContentColor.current)
        Icon(
            Icons.Default.ArrowDropDown,
            contentDescription = null,
            tint = LocalContentColor.current,
        )
    }
}

@Composable
fun CountryFlagLayout(
    countryFlag: CountryFlag?,
    modifier: Modifier = Modifier,
    emojiFlagTextStyle: TextStyle = MaterialTheme.typography.body1,
    imageFlagHeight: Dp = emojiFlagTextStyle.fontSize.toDp(),
) {
    when (countryFlag) {
        is EmojiFlag -> {
            Text(
                text = countryFlag.emoji.toString(),
                style = emojiFlagTextStyle,
                color = LocalContentColor.current,
                modifier = modifier
            )
        }

        is ImageFlag -> {
            Image(
                painter = painterResource(id = countryFlag.flagImageRes),
                contentDescription = null,
                modifier = modifier.height(imageFlagHeight)
            )
        }

        null -> { /* do nothing */
        }
    }
}

@Composable
fun rememberCountryFlag(
    country: CPCountry?,
    flagProvider: CPFlagProvider? = DefaultEmojiFlagProvider()
) = remember(country, flagProvider) {
    if (country != null && flagProvider != null) {
        when (flagProvider) {
            is DefaultEmojiFlagProvider -> {
                val emoji = when {
                    flagProvider.useEmojiCompat -> EmojiCompat.get().process(country.flagEmoji)
                    else -> country.flagEmoji
                }
                EmojiFlag(emoji)
            }

            is CPFlagImageProvider -> ImageFlag(flagProvider.getFlag(country.alpha2))
            else -> null
        }
    } else null
}

sealed class CountryFlag {
    data class EmojiFlag(val emoji: CharSequence) : CountryFlag()
    data class ImageFlag(@DrawableRes val flagImageRes: Int) : CountryFlag()
}

@Composable
fun rememberCPDataStore(
    countryFileReader: CountryFileReading = CPDataStoreGenerator.defaultCountryFileReader,
    countryListTransformer: ((List<CPCountry>) -> List<CPCountry>)? = null,
): CPDataStore {
    val context = LocalContext.current
    return remember(countryFileReader) {
        CPDataStoreGenerator.generate(
            context,
            countryFileReader = countryFileReader,
            countryListTransformer = countryListTransformer
        )
    }
}

@Composable
fun CountryPickerDialog(
    cpDataStore: CPDataStore = rememberCPDataStore(),
    flagProvider: CPFlagProvider? = DefaultEmojiFlagProvider(),
    quickAccessCountries: List<String>? = rememberQuickAccessCountries(),
    showFilter: Boolean = cpDataStore.countryList.size >= 6,
    fillHeight: Boolean = showFilter,
    allowClearSelection: Boolean = true,
    queryFilter: (country: CPCountry, filterQuery: String) -> Boolean = { country, filterQuery ->
        defaultCountrySearchFilter(country, filterQuery)
    },
    countryRowLayout: @Composable ((
        country: CPCountry,
        flagProvider: CPFlagProvider?,
        onClicked: (CPCountry) -> Unit,
    ) -> Unit) = { country, flagProvider, onClicked ->
        CountryListItemRowLayout(country, flagProvider, onClicked)
    },
    onDismissRequest: () -> Unit,
    onCountrySelected: (CPCountry?) -> Unit,
) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        DefaultCountryPickerDialogContent(
            quickAccessCountriesCodes = quickAccessCountries,
            cpDataStore = cpDataStore,
            onDismissRequest = onDismissRequest,
            onCountrySelected = onCountrySelected,
            showFilter = showFilter,
            fillHeight = fillHeight,
            flagProvider = flagProvider,
            allowClearSelection = allowClearSelection,
            queryFilter = queryFilter,
            countryRowLayout = countryRowLayout,
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun DefaultCountryPickerDialogContent(
    cpDataStore: CPDataStore,
    flagProvider: CPFlagProvider?,
    quickAccessCountriesCodes: List<String>?,
    onCountrySelected: (CPCountry?) -> Unit,
    showFilter: Boolean,
    fillHeight: Boolean,
    allowClearSelection: Boolean,
    modifier: Modifier = Modifier,
    queryFilter: (country: CPCountry, filterQuery: String) -> Boolean = { country, filterQuery ->
        defaultCountrySearchFilter(country, filterQuery)
    },
    searchFieldLayout: @Composable ((
        searchQuery: String,
        setSearchQuery: (String) -> Unit,
        cpDataStore: CPDataStore
    ) -> Unit) = { searchQuery, setSearchQuery, cpDataStore ->
        DefaultSearchField(searchQuery, setSearchQuery, cpDataStore)
    },
    countryRowLayout: @Composable ((
        country: CPCountry,
        flagProvider: CPFlagProvider?,
        onClicked: (CPCountry) -> Unit,
    ) -> Unit) = { country, flagProvider, onClicked ->
        CountryListItemRowLayout(country, flagProvider, onClicked)
    },
    onDismissRequest: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(0.8f)
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colors.surface,
    ) {
        val countryList = remember(cpDataStore) {
            cpDataStore.countryList
        }
        val scrollState = rememberLazyListState()
        val quickAccessCountries = remember(quickAccessCountriesCodes, countryList) {
            quickAccessCountriesCodes.orEmpty().mapNotNull { alpha2 ->
                countryList.firstOrNull { it.alpha2.equals(alpha2, ignoreCase = true) }
            }
        }
        val (searchQuery, setSearchQuery) = remember { mutableStateOf("") }
        val filteredList = remember(countryList, searchQuery) {
            countryList.filter { queryFilter(it, searchQuery) }
        }
        val filteredQuickAccessCountries = remember(showFilter, quickAccessCountries, searchQuery) {
            if (showFilter == false) emptyList()
            else quickAccessCountries.filter { queryFilter(it, searchQuery) }
                .distinctBy(CPCountry::alpha2)
        }
        LaunchedEffect(key1 = filteredList, key2 = filteredQuickAccessCountries) {
            scrollState.scrollToItem(0)
        }
        Column {
            if (showFilter) {
                searchFieldLayout(searchQuery, setSearchQuery, cpDataStore)
            }
            LazyColumn(modifier = Modifier.weight(1f, fill = fillHeight), state = scrollState) {
                if (filteredQuickAccessCountries.isNotEmpty()) {
                    filteredQuickAccessCountries.forEach { country ->
                        item("quickAccess-" + country.alpha2) {
                            countryRowLayout(country, flagProvider) {
                                onCountrySelected(it)
                                onDismissRequest()
                            }
                        }
                    }
                    item { Divider() }
                }
                filteredList.forEach { country ->
                    item(country.alpha2) {
                        countryRowLayout(country, flagProvider) {
                            onCountrySelected(it)
                            onDismissRequest()
                        }
                    }
                }
            }
            if (allowClearSelection) {
                TextButton(
                    onClick = {
                        onCountrySelected(null)
                        onDismissRequest()
                    },
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                ) {
                    Text(
                        text = cpDataStore.messageGroup.clearSelectionText,
                        style = MaterialTheme.typography.button,
                        color = MaterialTheme.colors.onBackground
                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
private fun DefaultSearchField(
    searchQuery: String,
    setSearchQuery: (String) -> Unit,
    cpDataStore: CPDataStore
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Icon(
                painter = painterResource(id = R.drawable.ic_cp_search),
                contentDescription = null,
                modifier = Modifier.padding(16.dp),
                tint = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
            )
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { setSearchQuery(it) },
                    textStyle = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onSurface),
                    cursorBrush = SolidColor(
                        TextFieldDefaults.outlinedTextFieldColors()
                            .cursorColor(isError = false).value
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onAny = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                if (searchQuery.isEmpty()) {
                    Text(
                        text = cpDataStore.messageGroup.searchHint,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Composable
private fun CountryListItemRowLayout(
    country: CPCountry,
    flagProvider: CPFlagProvider?,
    onClicked: (CPCountry) -> Unit,
) {
    val countryFlag = rememberCountryFlag(country, flagProvider)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .clickable {
                onClicked(country)
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (countryFlag != null) {
            CountryFlagLayout(countryFlag)
            Spacer(modifier = Modifier.width(16.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = country.name,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,
            )
        }
    }
}

fun defaultCountrySearchFilter(
    country: CPCountry,
    filterQuery: String,
) = if (filterQuery.isBlank()) true
else {
    val properties = listOf(
        country.name,
        country.englishName,
        country.alpha2,
        country.alpha3,
    )
    properties.any { it.contains(filterQuery, ignoreCase = true) }
}

@Preview
@Composable
fun PreviewSomeDialogContent() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray)
                .padding(20.dp),
            contentAlignment = Alignment.Center,
        ) {
            DefaultCountryPickerDialogContent(
                cpDataStore = rememberCPDataStore(countryListTransformer = countryMasterListTransformer),
                flagProvider = DefaultEmojiFlagProvider(),
                quickAccessCountriesCodes = null,
                showFilter = true,
                fillHeight = false,
                allowClearSelection = true,
                queryFilter = { cpCountry, query -> defaultCountrySearchFilter(cpCountry, query) },
                onDismissRequest = {},
                onCountrySelected = {},
            )
        }
    }
}

@Composable
fun rememberCountry(
    countryCode: String?,
    cpDataStore: CPDataStore = rememberCPDataStore()
): CPCountry? {
    return remember(countryCode, cpDataStore) {
        cpDataStore.countryList.firstOrNull { it.alpha2.equals(countryCode, ignoreCase = true) }
    }
}

/**
 * For state management,
 * if you need to hoist the selected country code outside of compose
 * then use CPCountryDetector::detectCountry()
 */
@Composable
fun rememberAutoDetectedCountryCode(
    sourceOrder: List<CPCountryDetector.Source> = listOf(
        CPCountryDetector.Source.SIM,
        CPCountryDetector.Source.NETWORK,
        CPCountryDetector.Source.LOCALE
    )
): String? {
    val context = LocalContext.current
    return remember(sourceOrder) {
        CPCountryDetector(context).detectCountry(sourceOrder)
    }
}

@Composable
fun rememberQuickAccessCountries(): List<String> {
    val countryDetector = CPCountryDetector(LocalContext.current)
    return remember {
        listOf(
            countryDetector.detectSIMCountry(),
            countryDetector.detectNetworkCountry(),
            countryDetector.detectLocaleCountry()
        ).filterNotNull().distinct()
    }
}


//