package com.hbb20.androidcountrypicker

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.hbb20.CPDataStoreGenerator
import com.hbb20.countrypicker.launchCountryPickerDialog
import com.hbb20.countrypicker.loadCountries

class OpenDialogDirectlyActivity : AppCompatActivity() {
    lateinit var dialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_dialog_directly)
        //        prepareDialog()
    }

    private fun prepareDialog() {
        val cpDataStore = CPDataStoreGenerator.generate(resources)
        dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.cp_dialog, null, false)
        dialog.window?.setContentView(dialogView)
        val recyclerView: RecyclerView = dialogView.findViewById(R.id.rvCountryList)
        val etQuery: EditText = dialogView.findViewById(R.id.etQuery)
        val imgClearQuery: ImageView = dialogView.findViewById(R.id.imgClearQuery)
        val tvTitle: TextView = dialogView.findViewById(R.id.tvTitle)
        val btnClearSelection: Button = dialogView.findViewById(R.id.btnClearSelection)
        fun refreshClearQueryButton() {
            btnClearSelection.isVisible = etQuery.text.isNotEmpty()
        }
        tvTitle.text = cpDataStore.messageGroup.dialogTitle
        btnClearSelection.text = cpDataStore.messageGroup.clearSelectionText
        btnClearSelection.setOnClickListener {
            Toast.makeText(this, "Cleared Selection", Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }
        imgClearQuery.setOnClickListener {
            etQuery.setText("")
        }
        etQuery.doOnTextChanged { _, _, _, _ -> refreshClearQueryButton() }
        recyclerView.loadCountries(
            preferredCountryCodes = "IN,US,PK",
            filterQueryEditText = etQuery
        ) { selectedCountry ->
            Toast.makeText(this, "Selected country ${selectedCountry.alpha2}", Toast.LENGTH_LONG)
                .show()
            dialog.dismiss()
        }
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
