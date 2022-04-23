package com.bigmiracle.bottomnavigation.Activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bigmiracle.bottomnavigation.Adapter.StockDetailAdapter
import com.bigmiracle.bottomnavigation.Constants
import com.bigmiracle.bottomnavigation.Database.Application
import com.bigmiracle.bottomnavigation.Network.priceDataService
import com.bigmiracle.bottomnavigation.R
import com.bigmiracle.bottomnavigation.ViewModels.DataViewModel
import com.bigmiracle.bottomnavigation.ViewModels.DataViewModelFactory
import com.bigmiracle.bottomnavigation.databinding.ActivityStockDetailBinding
import com.bigmiracle.testapi.model2.priceDataResponse
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit.*

class StockDetailActivity : BaseActivity(), StockDetailAdapter.CallbackInterface {

    private var binding: ActivityStockDetailBinding? = null
    private lateinit var stockId: String
    private lateinit var recyclerView: RecyclerView
    private var currentPrice :Double = 0.0
    private var total: Int = 0

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

        getPriceData(stockId)

//        val stockDetailAdapter = StockDetailAdapter(currentPrice)
//        recyclerView.adapter = stockDetailAdapter
//
//        lifecycle.coroutineScope.launch {
//            viewModel.holdingForStockId(stockId).collect {
//                stockDetailAdapter!!.submitList(it)
//            }
//        }
    }




    private fun getPriceData(stockId: String) {
        val retrofit: Retrofit= Retrofit.Builder()
            .baseUrl(Constants.NSTOCK_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: priceDataService  = retrofit.create<priceDataService>(priceDataService::class.java)
        val listCall: Call<priceDataResponse> = service.getData(stockId)

        listCall.enqueue(object: Callback<priceDataResponse> {
            override fun onResponse(response: Response<priceDataResponse>?, retrofit: Retrofit?) {
                if( response!!.isSuccess ){
                    val dataList : priceDataResponse= response.body()
                    currentPrice = dataList.priceData[0].nowPrice.toDouble()


                    val stockDetailAdapter = StockDetailAdapter(currentPrice)
                    recyclerView.adapter = stockDetailAdapter
                    binding?.totalStockProfit?.text = total.toString()
                    binding?.nowPrice?.text = currentPrice.toString()
                    binding?.upDown?.text = dataList.priceData[0].upDown
                    binding?.upDownRatio?.text = dataList.priceData[0].upDownRatio


                    lifecycle.coroutineScope.launch {
                        viewModel.holdingForStockId(stockId).collect {
                            stockDetailAdapter!!.submitList(it)

                        }
                    }

                    Log.i("price data response result","$currentPrice")

                }
            }

            override fun onFailure(t: Throwable?) {
                TODO("Not yet implemented")
            }
        })
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

    override fun passResultCallback(totalProfit: Int) {
        total = totalProfit

        Log.i("totalProfit","$totalProfit")
    }


}