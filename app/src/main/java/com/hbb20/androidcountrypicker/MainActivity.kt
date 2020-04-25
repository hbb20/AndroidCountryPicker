package com.hbb20.androidcountrypicker

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hbb20.CountryPickerView
import com.hbb20.androidcountrypicker.test.TestActivity
import com.hbb20.androidcountrypicker.test.XMLValidator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //        EmojiCompat.init(BundledEmojiCompatConfig(this))
        refreshView()
        configureCPView()
    }

    private fun configureCPView() {
        val countryPicker = findViewById<CountryPickerView>(R.id.countryPicker)
        countryPicker.dialogConfig.showTitle = false
        countryPicker.recyclerViewConfig.preferredCountryCodes = "IN,US,UK,GB"
        countryPicker.rowConfig.CPFlagProvider = null
        countryPicker.rowConfig.mainTextGenerator = { country -> country.capitalEnglishName }
        countryPicker.rowConfig.secondaryTextGenerator = { country -> country.englishName }
        countryPicker.rowConfig.highlightedTextGenerator = { country -> country.flagEmoji }
    }

    private fun refreshView() {
        if (XMLValidator().hasAnyCriticalIssue(this)) {
            criticalErrorGroup.visibility = View.VISIBLE
            contentView.visibility = View.GONE
        } else {
            criticalErrorGroup.visibility = View.GONE
            contentView.visibility = View.VISIBLE
        }
    }

    fun launchTestActivity(view: View? = null) {
        startActivity(Intent(this, TestActivity::class.java))
    }

    fun openDialogDirectly(view: View) {
        startActivity(Intent(this, OpenDialogDirectlyActivity::class.java))
    }

    fun loadInEpoxyRecyclerView(view: View) {
        startActivity(Intent(this, CustomRecyclerViewActivity::class.java))
    }
}
