package com.bigmiracle.bottomnavigation.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bigmiracle.bottomnavigation.Activity.StockDetailActivity
import com.bigmiracle.bottomnavigation.Adapter.HoldingAdapter
import com.bigmiracle.bottomnavigation.Constants
import com.bigmiracle.bottomnavigation.Database.Application
import com.bigmiracle.bottomnavigation.Database.HoldingEntity
import com.bigmiracle.bottomnavigation.Network.priceDataService
import com.bigmiracle.bottomnavigation.ViewModels.DataViewModel
import com.bigmiracle.bottomnavigation.ViewModels.DataViewModelFactory
import com.bigmiracle.bottomnavigation.databinding.FragmentFirstBinding
import com.bigmiracle.testapi.model2.priceDataResponse
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit.*


class FirstFragment : Fragment() {

    private var binding: FragmentFirstBinding? = null
    private lateinit var recyclerView: RecyclerView
    private var stockIdArray: List<String>? = null


    private val viewModel: DataViewModel by activityViewModels {
        DataViewModelFactory(
            (activity?.application as Application).database.dataDao()
        )
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding?.root



    }

    private fun groupStockList(holdingList: List<HoldingEntity>): List<HoldingEntity>{

        var newList = transform(holdingList)
        stockIdArray = newList.map { it.stockId }

        Log.i("stock array","$stockIdArray")
        return newList
    }



    private fun getPriceData(stockId: String){
        val retrofit: Retrofit= Retrofit.Builder()
            .baseUrl("https://api.nstock.tw")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: priceDataService= retrofit.create<priceDataService>(priceDataService::class.java)
        val listCall: Call<priceDataResponse> = service.getData(stockId)

        listCall.enqueue(object: Callback<priceDataResponse> {
            override fun onResponse(response: Response<priceDataResponse>?, retrofit: Retrofit?) {
                if( response!!.isSuccess ){
                    val dataList : priceDataResponse= response.body()
                    populateRecyclerView(dataList)

                    Log.i("price data response result","$dataList")
                }
            }

            override fun onFailure(t: Throwable?) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun populateRecyclerView(priceDataResponse: priceDataResponse){

        recyclerView = binding?.recyclerView!!
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        var holdingAdapter = HoldingAdapter(priceDataResponse.priceData)


//        val holdingAdapter =context?.let { HoldingAdapter(it) }

        recyclerView.adapter = holdingAdapter

        lifecycle.coroutineScope.launch {
            viewModel.fullHolding().collect {


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


    private suspend fun getStockId(): String? {


        var separator = ","
        var string =stockIdArray?.joinToString(separator)

        return string


    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //取得holding的股名array
        var string = ""
        var separator = ","

        lifecycleScope.launch {
            string = viewModel.getDistinctStockId().joinToString(separator)
            getPriceData(string)
        }



//        recyclerView = binding?.recyclerView!!
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())



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






    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
