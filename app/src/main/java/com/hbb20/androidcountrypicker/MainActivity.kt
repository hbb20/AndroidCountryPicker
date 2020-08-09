package com.hbb20.androidcountrypicker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.hbb20.CountryPickerView
import com.hbb20.androidcountrypicker.test.TestActivity
import com.hbb20.androidcountrypicker.test.XMLValidator
import com.hbb20.countrypicker.config.CPViewConfig
import com.hbb20.countrypicker.models.CPCountry
import com.hbb20.countrypicker.view.CPViewHelper
import com.hbb20.countrypicker.view.prepareCustomCountryPickerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //        EmojiCompat.init(BundledEmojiCompatConfig(this))
        refreshView()
        configureCPView()
        setCustomView()
    }

    private fun setCustomViewUsingHelper() {
        val container = findViewById<RelativeLayout>(R.id.rlCustomViewContainter)
        val textView = findViewById<TextView>(R.id.tvCustomViewInfo)
        val viewHelper =
            CPViewHelper(
                this, cpViewConfig = CPViewConfig(
                    initialSelection = CPViewConfig.InitialSelection.SpecificCountry("IN"),
                    cpFlagProvider = null
                )
            )
        viewHelper.attachViewComponents(container, textView)
        viewHelper.cpViewConfig.viewTextGenerator = { country ->
            "${country.name} (${country.alpha3.toUpperCase()})"
        }
    }

    private fun setCustomView() {
        val container = findViewById<RelativeLayout>(R.id.rlCustomViewContainter)
        val textView = findViewById<TextView>(R.id.tvCustomViewInfo)
        val helper = prepareCustomCountryPickerView(
            containerViewGroup = container,
            tvSelectedCountryInfo = textView,
            allowClearSelection = true,
            initialSelection = CPViewConfig.InitialSelection.SpecificCountry("IND")
        )
        helper.selectedCountry.observe(this, Observer { country ->
            Toast.makeText(this, "Country selected: ${country?.currencyName}", Toast.LENGTH_SHORT)
                .show()
        })
    }

    private fun configureCPView() {
        val countryPicker = findViewById<CountryPickerView>(R.id.countryPicker)
        countryPicker.helper.cpViewConfig.viewTextGenerator = { cpCountry: CPCountry ->
            cpCountry.alpha2
        }
        countryPicker.helper.refreshView()
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
