package com.hbb20.countrypicker

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class CountryFileReaderKtTest {

    @Test
    fun `test load base`() {
        loadBaseList(RuntimeEnvironment.systemContext)
        assertEquals("q", "q")
    }
}