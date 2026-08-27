package com.hbb20.countrypicker.config

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CPDialogConfigTest {
    @Test
    fun `when resize mode is auto and full screen is false, wrap mode is applied`() {
        val config = CPDialogConfig(showFullScreen = false, sizeMode = SizeMode.Auto)
        assertEquals(SizeMode.Wrap, config.getApplicableResizeMode())
    }

    @Test
    fun `when resize mode is auto and full screen is true, unchange mode is applied`() {
        val config = CPDialogConfig(showFullScreen = true, sizeMode = SizeMode.Auto)
        assertEquals(SizeMode.Unchanged, config.getApplicableResizeMode())
    }

    @Test
    fun `when resize mode is wrap then wrap mode is applied`() {
        val config = CPDialogConfig(showFullScreen = true, sizeMode = SizeMode.Wrap)
        assertEquals(SizeMode.Wrap, config.getApplicableResizeMode())
    }

    @Test
    fun `when resize mode is unchange then unchange mode is applied `() {
        val config = CPDialogConfig(showFullScreen = false, sizeMode = SizeMode.Unchanged)
        assertEquals(SizeMode.Unchanged, config.getApplicableResizeMode())
    }
}
