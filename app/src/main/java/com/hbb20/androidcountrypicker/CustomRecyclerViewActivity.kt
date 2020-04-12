package com.hbb20.androidcountrypicker

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hbb20.countrypicker.loadCountries
import kotlinx.android.synthetic.main.activity_custom_recycler_view.*

class CustomRecyclerViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_recycler_view)
        setupRecyclerView()
    }


    private fun setupRecyclerView() {
        recyclerView.loadCountries(
            filterQueryEditText = etQuery
        ) { selectedCountry ->
            Toast.makeText(this, selectedCountry.name, Toast.LENGTH_SHORT).show()
        }
    }
}
