package com.hbb20.countrypicker.helper

import android.content.res.TypedArray
import com.hbb20.countrypicker.R
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import org.junit.Test

class CountryPickerListAttrReaderKtTest {

    @Test
    fun `preferred countries attr is set in config`() {
        val attr: TypedArray = mockk(relaxed = true) {
            every { getString(R.styleable.CountryPickerView_cpList_preferredCountryCodes) } returns "IN,US"
        }

        val listConfig = readListConfigFromAttrs(attr)
        assertEquals("Must match the attribute value", "IN,US", listConfig.preferredCountryCodes)
    }
}
