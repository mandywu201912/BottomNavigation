package com.bigmiracle.bottomnavigation.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bigmiracle.bottomnavigation.Adapter.SearchListAdapter
import com.bigmiracle.bottomnavigation.Data.Datasource
import com.bigmiracle.bottomnavigation.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {

    private var binding: FragmentSecondBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSecondBinding.inflate(inflater, container, false)

        val stockList = Datasource().loadAllStocks()
        binding?.recyclerView?.adapter = SearchListAdapter(activity!!.applicationContext, stockList)

        return binding?.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val stockList = Datasource().loadAllStocks()
//        val recyclerView = binding?.recyclerView
//        binding?.recyclerView?.adapter = SearchListAdapter(activity!!.applicationContext, stockList)
//        recyclerView?.setHasFixedSize(true)



    }

}