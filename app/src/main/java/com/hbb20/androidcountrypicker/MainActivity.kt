package com.hbb20.androidcountrypicker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hbb20.CountryPickerView
import com.hbb20.androidcountrypicker.test.TestActivity
import com.hbb20.androidcountrypicker.test.XMLValidator
import com.hbb20.countrypicker.config.CPViewConfig
import com.hbb20.countrypicker.models.CPCountry
import com.hbb20.countrypicker.view.CountryPickerViewHelper
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

    private fun setCustomView() {
        val container = findViewById<RelativeLayout>(R.id.rlCustomViewContainter)
        val textView = findViewById<TextView>(R.id.tvCustomViewInfo)
        val viewHelper =
            CountryPickerViewHelper(
                this, viewConfig = CPViewConfig(
                    initialSelection = CPViewConfig.InitialSelection.SpecificCountry("IN"),
                    cpFlagProvider = null
                )
            )
        viewHelper.attachViewComponents(container, textView)
        viewHelper.viewConfig.viewTextGenerator = { country ->
            "${country.name} (${country.alpha3.toUpperCase()})"
        }
    }

    private fun configureCPView() {
        val countryPicker = findViewById<CountryPickerView>(R.id.countryPicker)
        countryPicker.helper.viewConfig.viewTextGenerator = { cpCountry: CPCountry ->
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
