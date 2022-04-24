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
    private var groupList: List<HoldingEntity> = listOf()


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.coroutineScope.launch {
            viewModel.fullHolding().collect {
                groupList = groupStockList(it)

                stockIdArray = groupList.map { it.stockId }
                var separator = ","
                var string = stockIdArray?.joinToString(separator)

                if (string != null) {
                    getPriceData(string)
                }
            }
        }
    }


    private fun getPriceData(stockId: String) {
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

                    recyclerView = binding?.recyclerView!!
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())

                    var holdingAdapter = HoldingAdapter(dataList.priceData)
                    recyclerView.adapter = holdingAdapter


                    holdingAdapter!!.submitList(groupList)

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
            }

            override fun onFailure(t: Throwable?) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun groupStockList(holdingList: List<HoldingEntity>): List<HoldingEntity>{

        return transform(holdingList)
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
