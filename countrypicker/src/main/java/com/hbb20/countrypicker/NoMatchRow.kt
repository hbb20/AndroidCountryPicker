package com.hbb20.countrypicker

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import kotlinx.android.synthetic.main.cp_no_match_row.view.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class NoMatchRow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.cp_no_match_row, this)
    }

    lateinit var noMatchAckText: String
        @ModelProp set

    @AfterPropsSet
    fun updateViews() {
        tvMsg.text = noMatchAckText
    }
}