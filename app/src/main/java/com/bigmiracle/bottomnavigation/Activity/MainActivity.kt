package com.bigmiracle.bottomnavigation.Activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.bigmiracle.bottomnavigation.Fragment.FirstFragment
import com.bigmiracle.bottomnavigation.Fragment.SecondFragment
import com.bigmiracle.bottomnavigation.R
import com.bigmiracle.bottomnavigation.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private var binding: ActivityMainBinding? = null
    private val firstFragment = FirstFragment()
    private val secondFragment = SecondFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupActionBar()

        binding?.bottomNavigation?.itemIconTintList = null


        replaceFragment(firstFragment)
        
        binding?.bottomNavigation?.setOnItemSelectedListener {
            when(it.itemId){
                R.id.firstFragment -> replaceFragment(firstFragment)
                R.id.secondFragment -> replaceFragment(secondFragment)


            }
            true
        }

        binding?.buttonAddOne?.setOnClickListener {
            startActivity(Intent(this,AddActivity::class.java))
        }


    }

    override fun onBackPressed() {
        doubleBackToExit()
    }

    private fun setupActionBar(){
        setSupportActionBar(binding?.toolbarMainActivity)

        if (supportActionBar != null){
            supportActionBar!!.title = "Stockify"
        }
    }

    private fun replaceFragment(fragment: Fragment){
        if(fragment !=null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)

            transaction.commit()
        }
    }
}