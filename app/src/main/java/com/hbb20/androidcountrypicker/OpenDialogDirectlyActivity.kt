package com.hbb20.androidcountrypicker

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hbb20.CPDataStoreGenerator

class OpenDialogDirectlyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_dialog_directly)
    }

    fun openCPDialog(view: View) {
        val cpDataStore = CPDataStoreGenerator.generate(resources)
    }
}
