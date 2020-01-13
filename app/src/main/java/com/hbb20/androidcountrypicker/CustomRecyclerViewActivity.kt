package com.hbb20.androidcountrypicker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hbb20.CPDataStoreGenerator
import com.hbb20.countrypicker.CPRecyclerViewHelper
import kotlinx.android.synthetic.main.activity_custom_recycler_view.*

class CustomRecyclerViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_recycler_view)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        //        recyclerView.layoutManager = LinearLayoutManager(this)
        //        recyclerView.adapter = CPRecyclerViewAdapter(this, CPDataStoreGenerator.generate(this))
        CPRecyclerViewHelper.load(
            recyclerView, CPDataStoreGenerator.generate(this), preferredCountryCodes = "IN,US"
        )
        CPRecyclerViewHelper.load(
            recyclerView,
            CPDataStoreGenerator.generate(this),
            preferredCountryCodes = "IN,US",
            filterQuery = "United"
        )
    }


}
