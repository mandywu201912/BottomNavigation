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

        //點擊把stockId導到stockDetailActivity
//        startActivity(Intent(this, AddActivity::class.java))

//        val adapter = HoldingAdapter(this, )
//
//        val holdingAdapter = HoldingAdapter{
//            val intent = Intent(context, StockDetailActivity::class.java)
//            intent.putExtra(it.stockId)
//            startActivity(intent)
//        }

        val holdingAdapter =context?.let { HoldingAdapter(it) }

        recyclerView.adapter = holdingAdapter

        lifecycle.coroutineScope.launch {
            viewModel.fullHolding().collect {
                holdingAdapter!!.submitList(it)
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

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
