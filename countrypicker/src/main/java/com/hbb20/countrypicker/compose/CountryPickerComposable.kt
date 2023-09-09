package com.hbb20.countrypicker.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.emoji.text.EmojiCompat
import com.hbb20.countrypicker.datagenerator.CPDataStoreGenerator
import com.hbb20.countrypicker.datagenerator.CountryFileReading
import com.hbb20.countrypicker.flagprovider.CPFlagImageProvider
import com.hbb20.countrypicker.flagprovider.CPFlagProvider
import com.hbb20.countrypicker.flagprovider.DefaultEmojiFlagProvider
import com.hbb20.countrypicker.models.CPDataStore

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CountryLayout(
    alpha2Code: String?,
    cpDataStore: CPDataStore = rememberCPDataStore(),
    emptySelectionText: String = cpDataStore.messageGroup.selectionPlaceholderText,
    flagProvider: CPFlagProvider? = DefaultEmojiFlagProvider()
) {
    val country = cpDataStore.countryList.firstOrNull { it.alpha2.equals(alpha2Code, ignoreCase = true) }
    val countryFlag = if (country != null && flagProvider != null) {
        when (flagProvider) {
            is DefaultEmojiFlagProvider -> {
                val emoji = when {
                    flagProvider.useEmojiCompat -> EmojiCompat.get()
                        .process(country.flagEmoji)

                    else -> country.flagEmoji
                }
                CountryFlag.EmojiFlag(emoji)
            }

            is CPFlagImageProvider -> {
                CountryFlag.ImageFlag(flagProvider.getFlag(country.alpha2))
            }
            else -> null
        }
    } else null

    val  textToShow = country?.name ?: emptySelectionText

    Row(verticalAlignment = Alignment.CenterVertically) {
        val textStyle = MaterialTheme.typography.button
        if (countryFlag != null) {
            when (countryFlag) {
                is CountryFlag.EmojiFlag -> {
                    Text(
                        text = countryFlag.emoji.toString(),
                        style = textStyle,
                        color = LocalContentColor.current
                    )
                }

                is CountryFlag.ImageFlag -> {
                    Image(
                        painter = painterResource(id = countryFlag.flagImageRes),
                        contentDescription = null,
                        modifier = Modifier.height(textStyle.lineHeight.toDp())
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        Text(text = textToShow, style = textStyle, color = LocalContentColor.current)
    }

}

sealed class CountryFlag {
    data class EmojiFlag(val emoji: CharSequence) : CountryFlag()
    data class ImageFlag(@DrawableRes val flagImageRes: Int) : CountryFlag()
}

@Composable
fun rememberCPDataStore(
    customMasterCountries: String = CPDataStoreGenerator.defaultMasterCountries,
    customExcludedCountries: String = CPDataStoreGenerator.defaultExcludedCountries,
    countryFileReader: CountryFileReading = CPDataStoreGenerator.defaultCountryFileReader,
    useCache: Boolean = CPDataStoreGenerator.defaultUseCache
): CPDataStore {
    val context = LocalContext.current
    return remember {
        CPDataStoreGenerator.generate(
            context,
            customMasterCountries = customMasterCountries,
            customExcludedCountries = customExcludedCountries,
            countryFileReader = countryFileReader,
            useCache = useCache
        )
    }
}

@Preview
@Composable
fun CountryPickerPreview() {
    Surface(color = MaterialTheme.colors.surface) {
        CountryLayout("gb")
    }
}
//