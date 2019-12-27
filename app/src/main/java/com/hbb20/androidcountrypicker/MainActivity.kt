package com.hbb20.androidcountrypicker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hbb20.androidcountrypicker.test.TestActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadCP()

        //todo: open this
        //        if(resources.getBoolean(R.bool.launch_test_immediately)){
        //            launchTestActivity()
        //        }
    }

    private fun loadCP() {
        Log.d("DataStore", "generated")
    }

    fun launchTestActivity(view: View? = null) {
        startActivity(Intent(this, TestActivity::class.java))
    }
}
