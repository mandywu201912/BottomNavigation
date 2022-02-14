package com.bigmiracle.bottomnavigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bigmiracle.bottomnavigation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private val firstFragment = FirstFragment()
    private val secondFragment = SecondFragment()
    private val thirdFragment = ThirdFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.bottomNavigation?.itemIconTintList = null
        replaceFragment(firstFragment)
        
        binding?.bottomNavigation?.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.firstFragment -> replaceFragment(firstFragment)
                R.id.secondFragment -> replaceFragment(secondFragment)
                R.id.thirdFragment -> replaceFragment(thirdFragment)
            }
            true
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