package com.bigmiracle.bottomnavigation.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bigmiracle.bottomnavigation.databinding.FragmentFirstBinding


class FirstFragment : Fragment() {

    private var binding: FragmentFirstBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentFirstBinding.inflate(inflater, container, false)







        return binding?.root



//        class MainActivity : AppCompatActivity() {
//            private val vocabulary = arrayListOf("apple", "application", "appal", "appalachia", "apposite")
//            override fun onCreate(savedInstanceState: Bundle?) {
//                super.onCreate(savedInstanceState)
//                setContentView(R.layout.activity_main)
//                val adapter = ArrayAdapter(this, R.layout.simple_dropdown_item_1line, vocabulary)
//                auto_complete_text.threshold = 1
//                auto_complete_text.setAdapter(adapter)
//            }
//        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




    }

    companion object {

    }
}