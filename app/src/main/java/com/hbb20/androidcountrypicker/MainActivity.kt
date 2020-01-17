package com.hbb20.androidcountrypicker

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hbb20.androidcountrypicker.test.TestActivity
import com.hbb20.androidcountrypicker.test.XMLValidator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //        EmojiCompat.init(BundledEmojiCompatConfig(this))
        refreshView()
    }

    private fun refreshView() {
        if (XMLValidator().hasAnyCriticalIssue(this)) {
            criticalErrorGroup.visibility = View.VISIBLE
            contentView.visibility = View.GONE
        } else {
            criticalErrorGroup.visibility = View.GONE
            contentView.visibility = View.VISIBLE
        }
    }

    fun launchTestActivity(view: View? = null) {
        startActivity(Intent(this, TestActivity::class.java))
    }

    fun openCustomDataStoreActivity(view: View) {

    }

    fun loadInEpoxyRecyclerView(view: View) {
        startActivity(Intent(this, CustomRecyclerViewActivity::class.java))
    }
}
