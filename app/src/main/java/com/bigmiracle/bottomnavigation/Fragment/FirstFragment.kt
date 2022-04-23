package com.bigmiracle.bottomnavigation.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bigmiracle.bottomnavigation.Activity.StockDetailActivity
import com.bigmiracle.bottomnavigation.Adapter.HoldingAdapter
import com.bigmiracle.bottomnavigation.Constants
import com.bigmiracle.bottomnavigation.Database.Application
import com.bigmiracle.bottomnavigation.Database.HoldingEntity
import com.bigmiracle.bottomnavigation.ViewModels.DataViewModel
import com.bigmiracle.bottomnavigation.ViewModels.DataViewModelFactory
import com.bigmiracle.bottomnavigation.databinding.FragmentFirstBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class FirstFragment : Fragment() {

    private var binding: FragmentFirstBinding? = null
    private lateinit var recyclerView: RecyclerView


    private val viewModel: DataViewModel by activityViewModels {
        DataViewModelFactory(
            (activity?.application as Application).database.dataDao()
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding?.root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding?.recyclerView!!
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        val holdingAdapter =context?.let { HoldingAdapter(it) }

        recyclerView.adapter = holdingAdapter

        lifecycle.coroutineScope.launch {
            viewModel.fullHolding().collect {

//                fun execute(stockList: List<HoldingEntity>): List<HoldingEntity>{
//                    return groupStockList.transform(stockList)
//                }
//
                holdingAdapter!!.submitList(groupStockList(it))
            }
        }

        if (holdingAdapter != null) {
            holdingAdapter.setOnClickListener(object: HoldingAdapter.OnClickListener{
                override fun onClick(position: Int, model: HoldingEntity) {
                    val intent = Intent(context, StockDetailActivity::class.java)
                    intent.putExtra(Constants.STOCK_ID,model.stockId)
                    startActivity(intent)
                }
            })
        }
    }

    private fun transform(stockList: List<HoldingEntity>) = stockList
        .groupBy { it.stockId }
        .values
        .map {
            it.reduce { acc, holdingEntity ->
                HoldingEntity(
                    stockId = holdingEntity.stockId,
                    stockName = holdingEntity.stockName,
                    date = holdingEntity.date,
                    share = acc.share + holdingEntity.share,
                    transactionTypeCode = holdingEntity.transactionTypeCode,
                    transactionType = holdingEntity.transactionType,
                    price = ((acc.outcome + holdingEntity.outcome).toDouble())/((acc.share+holdingEntity.share).toDouble())

                )
            }
        }

    private fun groupStockList(holdingList: List<HoldingEntity>): List<HoldingEntity>{
        return transform(holdingList)
    }


//    private fun getPriceData(stockId: String) {
//        val retrofit: Retrofit= Retrofit.Builder()
//            .baseUrl(Constants.NSTOCK_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val service: priceDataService= retrofit.create<priceDataService>(priceDataService::class.java)
//
//        lifecycleScope.launch {
//            var listStockId = viewModel.getDistinctStockCode()
//
//
//        }
//
//
//
//        val listCall: Call<priceDataResponse> = service.getDatas(stockId)
//
//        listCall.enqueue(object: Callback<priceDataResponse> {
//            override fun onResponse(response: Response<priceDataResponse>?, retrofit: Retrofit?) {
//                if( response!!.isSuccess ){
//                    val dataList : priceDataResponse= response.body()
//                    currentPrice = dataList.priceData[0].nowPrice.toDouble()
//
//
//                    val stockDetailAdapter = StockDetailAdapter(currentPrice)
//                    recyclerView.adapter = stockDetailAdapter
//                    binding?.totalStockProfit?.text = total.toString()
//                    binding?.nowPrice?.text = currentPrice.toString()
//                    binding?.upDown?.text = dataList.priceData[0].upDown
//                    binding?.upDownRatio?.text = dataList.priceData[0].upDownRatio
//
//
//                    lifecycle.coroutineScope.launch {
//                        viewModel.holdingForStockId(stockId).collect {
//                            stockDetailAdapter!!.submitList(it)
//
//                        }
//                    }
//
//                    Log.i("price data response result","$currentPrice")
//
//                }
//            }
//
//            override fun onFailure(t: Throwable?) {
//                TODO("Not yet implemented")
//            }
//        })
//    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
