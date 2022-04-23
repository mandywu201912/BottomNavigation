package com.bigmiracle.bottomnavigation.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bigmiracle.bottomnavigation.Adapter.StockDetailAdapter
import com.bigmiracle.bottomnavigation.Database.Application
import com.bigmiracle.bottomnavigation.Database.HoldingEntity
import com.bigmiracle.bottomnavigation.ViewModels.DataViewModel
import com.bigmiracle.bottomnavigation.ViewModels.DataViewModelFactory
import com.bigmiracle.bottomnavigation.databinding.FragmentStockDetailBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class StockDetailFragment : Fragment() {


    lateinit var holding: HoldingEntity
    private val navigationArgs: StockDetailFragmentArgs by navArgs()

    private var binding: FragmentStockDetailBinding? = null
    private lateinit var recyclerView: RecyclerView


    private val viewModel: DataViewModel by activityViewModels {
        DataViewModelFactory(
            (activity?.application as Application).database.dataDao()
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentStockDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stockId = navigationArgs.stockId
        recyclerView = binding!!.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val stockDetailAdapter = StockDetailAdapter({})
        recyclerView.adapter = stockDetailAdapter


        lifecycle.coroutineScope.launch {
            viewModel.holdingForStockId(stockId).collect(){
                stockDetailAdapter.submitList(it)
            }
        }



    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        recyclerView = binding!!.recyclerView
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())


//        val stockDetailAdapter = StockDetailAdapter({})
//        recyclerView.adapter = stockDetailAdapter
//
//        lifecycle.coroutineScope.launch {
//            viewModel.holdingForStockId(stockId).collect(){
//                stockDetailAdapter.submitList(it)
//            }
//        }
//
//    }
//
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}