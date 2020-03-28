package com.hbb20.androidcountrypicker

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.epoxy.EpoxyRecyclerView
import com.hbb20.CPDataStoreGenerator
import com.hbb20.countrypicker.CPRecyclerViewHelper

class OpenDialogDirectlyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_dialog_directly)
    }

    fun openCPDialog(view: View) {
        val cpDataStore = CPDataStoreGenerator.generate(resources)
        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.cp_dialog, null, false)
        dialog.window?.setContentView(dialogView)

        val erv: EpoxyRecyclerView = dialogView.findViewById(R.id.ervCountryList)
        val etQuery: EditText = dialogView.findViewById(R.id.etQuery)
        val imgClearQuery: ImageView = dialogView.findViewById(R.id.imgClearQuery)
        val tvTitle: TextView = dialogView.findViewById(R.id.tvTitle)
        val btnClearSelection: Button = dialogView.findViewById(R.id.btnClearSelection)

        tvTitle.text = cpDataStore.messageGroup.dialogTitle
        btnClearSelection.text = cpDataStore.messageGroup.clearSelectionText

        CPRecyclerViewHelper.load(erv, cpDataStore, onCountryClickListener = {
            Toast.makeText(this, "Country Selected", Toast.LENGTH_LONG).show()
            dialog.dismiss()
        })

        btnClearSelection.setOnClickListener {
            Toast.makeText(this, "Selection Cleared", Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }

        dialog.show()
    }
}
