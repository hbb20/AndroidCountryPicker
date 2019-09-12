package com.hbb20.androidcountrypicker.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.hbb20.androidcountrypicker.R
import com.hbb20.countrypicker.logd
import kotlin.math.log

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
    }

    fun startTest(view: View) {
        val problems = XMLValidator().checkAllXMLFiles(this)
        if(problems.isEmpty()){
            Toast.makeText(this, "All files are in good shape. Good job.", Toast.LENGTH_SHORT).show()
        }else {
            Toast.makeText(this, "Opps. There is something wrong. Please check logs.", Toast.LENGTH_SHORT).show()
            logd("List of problems")
            problems.forEach {
                logd(it.solution)
            }
        }
    }
}
