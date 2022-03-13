package com.bigmiracle.bottomnavigation.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bigmiracle.bottomnavigation.Adapter.RecordAdapter
import com.bigmiracle.bottomnavigation.Database.RecordApplication
import com.bigmiracle.bottomnavigation.ViewModels.RecordViewModel
import com.bigmiracle.bottomnavigation.ViewModels.RecordViewModelFactory
import com.bigmiracle.bottomnavigation.databinding.FragmentSecondBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

public class SecondFragment : Fragment() {

    private var binding: FragmentSecondBinding? = null

    private lateinit var recyclerView: RecyclerView

    private val viewModel: RecordViewModel by activityViewModels {
        RecordViewModelFactory(
            (activity?.application as RecordApplication).database.recordDao()
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding?.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding?.recyclerView!!
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val recordAdapter = RecordAdapter({})

//        val recordAdapter = RecordAdapter({
//            val action = FullScheduleFragmentDirections
//                .actionFullScheduleFragmentToStopScheduleFragment(
//                    stopName = it.stopName
//                )
//            view.findNavController().navigate(action)
//        })
        recyclerView.adapter = recordAdapter

        lifecycle.coroutineScope.launch {
            viewModel.fullRecord().collect {
                recordAdapter.submitList(it)
            }
        }
    }



//        val stockList = Datasource().loadAllStocks()
//        val recyclerView = binding?.recyclerView
//        binding?.recyclerView?.adapter = SearchListAdapter(activity!!.applicationContext, stockList)
//        recyclerView?.setHasFixedSize(true)

}