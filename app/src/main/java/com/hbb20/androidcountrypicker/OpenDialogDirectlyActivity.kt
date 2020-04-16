package com.hbb20.androidcountrypicker

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hbb20.countrypicker.launchCountryPickerDialog

class OpenDialogDirectlyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_dialog_directly)
    }

    fun openCPDialog(view: View) {
        launchCountryPickerDialog { selectedCountry ->
            Toast.makeText(
                this,
                selectedCountry?.alpha2,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
