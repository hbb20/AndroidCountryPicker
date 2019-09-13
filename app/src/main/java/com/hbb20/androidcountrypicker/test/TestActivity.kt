package com.hbb20.androidcountrypicker.test

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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

    /**
     * hide progressring and success icon
     * setup recycler view
     * show problems
     */
    private fun showProblems(problems: List<Problem>) {
        //set visibilities
        problemsRecyclerView.visibility = View.VISIBLE
        testProgressRing.visibility = View.GONE
        testPassViews.visibility = View.GONE

        //set recyclerview
        problemsRecyclerView.layoutManager = LinearLayoutManager(this)


        logd("====== List of CP problems =====")
        problems.forEach {
            it.log()
        }

        val problemsRVAdapter = prepareProblemRVAdapter(problems)
        problemsRecyclerView.adapter = problemsRVAdapter
    }

    private fun prepareProblemRVAdapter(problems: List<Problem>): ProblemsRVAdapter {
        val rvItems = mutableListOf<ProblemRVItem>()
        problems.groupBy { it.fileName }.forEach { (fileName, fileProblems) ->
            rvItems.add(ProblemFileRVItem(fileName))
            fileProblems.groupBy { it.category }.forEach { (categoryName, categoryProblems) ->
                rvItems.add(ProblemCategoryRVItem(categoryName.text))
                categoryProblems.forEach {
                    rvItems.add(ProblemInfoRVItem(it))
                }
            }
        }
        return ProblemsRVAdapter(this, rvItems)
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
