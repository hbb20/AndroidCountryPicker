package com.hbb20.androidcountrypicker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.hbb20.androidcountrypicker.test.TestActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun launchTestActivity(view: View) {
        startActivity(Intent(this, TestActivity::class.java))
    }
}
