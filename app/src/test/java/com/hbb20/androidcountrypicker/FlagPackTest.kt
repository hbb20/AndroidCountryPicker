package com.hbb20.androidcountrypicker

import com.hbb20.contrypicker.flagpack1.FlagPack1
import com.hbb20.countrypicker.models.allCountryAlpha2List
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
class FlagPackTest {

    // add new flag pack here
    val allFlagPacks = listOf("FlagPack1" to FlagPack1)

    @Test
    fun `list missing flags in flagpack`() {
        val missingFlags = mutableListOf<Pair<String, String>>()
        allFlagPacks.forEach { (flagPackName, flagPack) ->
            allCountryAlpha2List.forEach { alpha2 ->
                if (flagPack.missingFlagPlaceHolder == flagPack.getFlag(alpha2)) {
                    missingFlags.add(flagPackName to alpha2)
                }
            }
        }

        if (missingFlags.isEmpty()) {
            println("Great!!! There is no missing flag in any flag pack")
        } else {
            missingFlags.groupBy { it.first }.forEach { (flagPackName, missingFlagAlpha2) ->
                println("$flagPackName is missing following flag(s): " +
                        missingFlagAlpha2.joinToString { it.second }
                )
            }
        }
    }
}
