package com.bigmiracle.bottomnavigation.Activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bigmiracle.bottomnavigation.Adapter.StockDetailAdapter
import com.bigmiracle.bottomnavigation.Constants
import com.bigmiracle.bottomnavigation.Database.Application
import com.bigmiracle.bottomnavigation.R
import com.bigmiracle.bottomnavigation.ViewModels.DataViewModel
import com.bigmiracle.bottomnavigation.ViewModels.DataViewModelFactory
import com.bigmiracle.bottomnavigation.databinding.ActivityStockDetailBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class StockDetailActivity : BaseActivity() {

    private var binding: ActivityStockDetailBinding? = null
    private lateinit var stockId: String
    private lateinit var recyclerView: RecyclerView

    private val viewModel: DataViewModel by viewModels {
        DataViewModelFactory(
            (this.application as Application).database.dataDao()
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if(intent.hasExtra(Constants.STOCK_ID)){
            stockId = intent.getStringExtra(Constants.STOCK_ID).toString()
        }

        setupActionBar()

        recyclerView = binding?.recyclerView!!
        recyclerView.layoutManager = LinearLayoutManager(this)

        val stockDetailAdapter = StockDetailAdapter({})

        recyclerView.adapter = stockDetailAdapter

        lifecycle.coroutineScope.launch {
            viewModel.holdingForStockId(stockId).collect {
                stockDetailAdapter!!.submitList(it)
            }
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(binding?.toolbarDetailActivity)

        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.icon_arrow_back)
            supportActionBar!!.title="$stockId"
        }
        binding?.toolbarDetailActivity?.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}