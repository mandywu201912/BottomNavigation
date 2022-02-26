package com.bigmiracle.bottomnavigation.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bigmiracle.bottomnavigation.R
import com.bigmiracle.bottomnavigation.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    private var binding: ActivityAddBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupActionBar()
    }

    private fun setupActionBar() {
        setSupportActionBar(binding?.toolbarAddActivity)

        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.icon_arrow_back)
            supportActionBar!!.title="新增"
        }
        binding?.toolbarAddActivity?.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}