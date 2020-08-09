package com.hbb20

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.hbb20.countrypicker.R
import com.hbb20.countrypicker.config.CPRowConfig
import com.hbb20.countrypicker.datagenerator.CPDataStoreGenerator
import com.hbb20.countrypicker.helper.readDialogConfigFromAttrs
import com.hbb20.countrypicker.helper.readListConfigFromAttrs
import com.hbb20.countrypicker.helper.readViewConfigFromAttrs
import com.hbb20.countrypicker.view.CPViewHelper

class CountryPickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    val tvCountryInfo: TextView by lazy { findViewById<TextView>(R.id.tvCountryInfo) }
    val tvEmojiFlag: TextView by lazy { findViewById<TextView>(R.id.tvEmojiFlag) }
    val imgFlag: ImageView by lazy { findViewById<ImageView>(R.id.imgFlag) }
    var helper: CPViewHelper

    init {
        applyLayout(attrs)
        helper = prepareHelperFromAttr(attrs)
        helper.attachViewComponents(this, tvCountryInfo, tvEmojiFlag, imgFlag)
    }

    private fun prepareHelperFromAttr(attrs: AttributeSet?): CPViewHelper {
        val styledAttrs =
            context.theme.obtainStyledAttributes(attrs, R.styleable.CountryPickerView, 0, 0)
        val dataStore = CPDataStoreGenerator.generate(context)
        val dialogConfig = readDialogConfigFromAttrs(styledAttrs)
        val listConfig = readListConfigFromAttrs(styledAttrs)
        val viewConfig = readViewConfigFromAttrs(styledAttrs)
        val rowConfig = CPRowConfig()
        return CPViewHelper(
            context,
            dataStore,
            viewConfig,
            dialogConfig,
            listConfig,
            rowConfig,
            isInEditMode
        )
    }

    /**
     * If width is match_parent, 0
     */
    private fun applyLayout(attrs: AttributeSet?) {
        val xmlWidth =
            attrs?.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_width")
        val wrapContentValues =
            setOf(ViewGroup.LayoutParams.WRAP_CONTENT.toString(), "wrap_content")
        val layoutFile =
            if (xmlWidth == null || xmlWidth in wrapContentValues) R.layout.cp_country_picker_view
            else R.layout.cp_country_picker_view_constrained
        LayoutInflater.from(context)
            .inflate(layoutFile, this, true)
    }
}