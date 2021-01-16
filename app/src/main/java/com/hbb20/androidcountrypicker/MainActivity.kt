package com.hbb20.androidcountrypicker

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hbb20.CountryPickerView
import com.hbb20.androidcountrypicker.databinding.ActivityMainBinding
import com.hbb20.countrypicker.CPFlagImageProvider
import com.hbb20.countrypicker.models.CPCountry

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //        EmojiCompat.init(BundledEmojiCompatConfig(this))
        refreshView()
        configureCPView()
    }

    private fun configureCPView() {
        val countryPicker = findViewById<CountryPickerView>(R.id.countryPicker)
        countryPicker.cpViewHelper.cpViewConfig.viewTextGenerator = { cpCountry: CPCountry ->
            cpCountry.alpha2
        }
        countryPicker.cpViewHelper.cpViewConfig.cpFlagProvider = object : CPFlagImageProvider() {
            override fun getFlag(alpha2Code: String): Int {
                return R.drawable.ic_flag_black
            }

        }
        //        countryPicker.helper.refreshView()
    }

    private fun refreshView() {

    }

    fun openDialogDirectly(view: View) {
        startActivity(Intent(this, OpenDialogDirectlyActivity::class.java))
    }

    fun loadInEpoxyRecyclerView(view: View) {
        startActivity(Intent(this, CustomRecyclerViewActivity::class.java))
    }

    fun openCpViewDemo(view: View) {
        startActivity(Intent(this, CountryPickerViewDemoActivity::class.java))
    }
}
