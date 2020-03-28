package com.hbb20.androidcountrypicker

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hbb20.CPDataStoreGenerator
import com.hbb20.countrypicker.CustomFlagImageProvider
import com.hbb20.countrypicker.loadCountries
import kotlinx.android.synthetic.main.activity_custom_recycler_view.*

class CustomRecyclerViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_recycler_view)
        setupRecyclerView()
    }


    private fun setupRecyclerView() {
        val dataStore = CPDataStoreGenerator.generate(resources)
        //        val recyclerViewHelper = CPRecyclerViewHelper(
        //            recyclerView,
        //            dataStore,
        //            cpRecyclerViewConfig = CPRecyclerViewConfig(
        //                highlightedTextGenerator = { "+${it.phoneCode}" },
        //                flagProvider = DefaultEmojiFlagProvider()
        //            ),
        //            onCountryClickListener = { cpCountry ->
        //                Toast.makeText(
        //                    this,
        //                    "Selected ${cpCountry.englishName}",
        //                    Toast.LENGTH_LONG
        //                ).show()
        //            },
        //            queryEditText = etQuery
        //        )

        recyclerView.loadCountries(
            resources = resources,
            filterQueryEditText = etQuery,
            highlightedTextGenerator = { country -> country.alpha2 },
            customDataStoreModifier = { dataStore ->
                dataStore.countryList.retainAll { it.currencyCode == "USD" }
                dataStore.messageGroup.noMatchMsg = "Sorry, nothing is here"
            },
            flagProvider = MyFlagProvider()
        ) { selectedCountry ->
            Toast.makeText(
                this,
                "Selected ${selectedCountry.englishName}",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}

class MyFlagProvider : CustomFlagImageProvider() {
    override fun getFlag(alpha2Code: String): Int {
        return R.drawable.ic_flag
    }

}
