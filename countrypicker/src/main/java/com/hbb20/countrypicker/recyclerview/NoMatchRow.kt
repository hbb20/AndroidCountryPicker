package com.hbb20.countrypicker.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.hbb20.countrypicker.R
import com.hbb20.countrypicker.databinding.CpNoMatchRowBinding

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class NoMatchRow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var binding: CpNoMatchRowBinding

    init {
        inflate(context, R.layout.cp_no_match_row, this)
        binding = CpNoMatchRowBinding.bind(this.findViewById(R.id.rowHolder))
    }

    lateinit var noMatchAckText: String
        @ModelProp set

    @AfterPropsSet
    fun updateViews() {
        binding.tvMsg.text = noMatchAckText
    }
}
