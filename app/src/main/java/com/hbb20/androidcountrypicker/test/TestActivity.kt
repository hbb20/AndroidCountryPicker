package com.hbb20.androidcountrypicker.test

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hbb20.androidcountrypicker.R
import com.hbb20.countrypicker.logd
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        supportActionBar?.title = "XML Resource Test"
        startTest()
    }

    private fun startTest() {
        showProgressRing()
        val problems = XMLValidator().checkAllXMLFiles(this)
        if (problems.isEmpty()) {
            showPassScreen()
        } else {
            showProblems(problems)
        }
    }

    private fun showProblems(problems: List<Problem>) {
        problemsRecyclerView.visibility = View.VISIBLE
        testProgressRing.visibility = View.VISIBLE
        testPassViews.visibility = View.GONE
        Toast.makeText(
            this,
            "Opps. There is something wrong. Please check logs.",
            Toast.LENGTH_SHORT
        ).show()
        logd("List of problems")
        problems.forEach {
            logd(it.solution)
        }
    }

    private fun showPassScreen() {
        problemsRecyclerView.visibility = View.GONE
        testProgressRing.visibility = View.GONE
        testPassViews.visibility = View.VISIBLE
    }

    private fun showProgressRing() {
        problemsRecyclerView.visibility = View.GONE
        testProgressRing.visibility = View.VISIBLE
        testPassViews.visibility = View.GONE
    }
}
