package com.hbb20.countrypicker.helper

import android.content.res.TypedArray
import com.hbb20.countrypicker.R.styleable.*
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CountryPickerDialogAttrReaderKtTest {

    @Test
    fun `allow search = false set in config`() {
        val attr: TypedArray = mockk(relaxed = true) {
            every { getBoolean(CountryPickerView_cpDialog_allowSearch, any()) } returns false
        }
        val dialogConfig = readDialogConfigFromAttrs(attr)

        assertFalse("Should be false because attribute is set to false", dialogConfig.allowSearch)
    }

    @Test
    fun `allow search = true set in config`() {
        val attr: TypedArray = mockk(relaxed = true) {
            every { getBoolean(CountryPickerView_cpDialog_allowSearch, any()) } returns true
        }
        val dialogConfig = readDialogConfigFromAttrs(attr)

        assertTrue("Should be true because attribute is set to true", dialogConfig.allowSearch)
    }

    @Test
    fun `allow clear selection = false set in config`() {
        val attr: TypedArray = mockk(relaxed = true) {
            every {
                getBoolean(
                    CountryPickerView_cpDialog_allowClearSelection,
                    any()
                )
            } returns false
        }
        val dialogConfig = readDialogConfigFromAttrs(attr)

        assertFalse(
            "Should be false because attribute is set to false",
            dialogConfig.allowClearSelection
        )
    }

    @Test
    fun `allow clear selection = true set in config`() {
        val attr: TypedArray = mockk(relaxed = true) {
            every { getBoolean(CountryPickerView_cpDialog_allowClearSelection, any()) } returns true
        }
        val dialogConfig = readDialogConfigFromAttrs(attr)

        assertTrue(
            "Should be true because attribute is set to true",
            dialogConfig.allowClearSelection
        )
    }

    @Test
    fun `show title = false set in config`() {
        val attr: TypedArray = mockk(relaxed = true) {
            every { getBoolean(CountryPickerView_cpDialog_showTitle, any()) } returns false
        }
        val dialogConfig = readDialogConfigFromAttrs(attr)

        assertFalse("Should be false because attribute is set to false", dialogConfig.showTitle)
    }

    @Test
    fun `show title = true set in config`() {
        val attr: TypedArray = mockk(relaxed = true) {
            every { getBoolean(CountryPickerView_cpDialog_showTitle, any()) } returns true
        }
        val dialogConfig = readDialogConfigFromAttrs(attr)

        assertTrue("Should be true because attribute is set to true", dialogConfig.showTitle)
    }

    @Test
    fun `show fullscreen = false set in config`() {
        val attr: TypedArray = mockk(relaxed = true) {
            every { getBoolean(CountryPickerView_cpDialog_showFullScreen, any()) } returns false
        }
        val dialogConfig = readDialogConfigFromAttrs(attr)

        assertFalse(
            "Should be false because attribute is set to false",
            dialogConfig.showFullScreen
        )
    }

    @Test
    fun `show fullscreen = true set in config`() {
        val attr: TypedArray = mockk(relaxed = true) {
            every { getBoolean(CountryPickerView_cpDialog_showFullScreen, any()) } returns true
        }
        val dialogConfig = readDialogConfigFromAttrs(attr)

        assertTrue("Should be true because attribute is set to true", dialogConfig.showFullScreen)
    }
}
