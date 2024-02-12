package com.example.myapplication

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //replaceFragment(home())

       // binding.bottomNavigationView.setOnItemSelectedListener {
         //   when(it.ItemId){
           //     R.id.home -> replaceFragment(home())
             //   R.id.save -> replaceFragment(save())
               // R.id.profile -> replaceFragment(profile())

                //else -> {



             //   }
           // }
        //    true
    //    }
    //}
    //private fun replaceFragment(fragment : Fragment){
      //  val fragmentManager = supportFragmentManager
        //val fragmentTransaction = fragmentManager.beginTransaction()
        //fragmentTransaction.replace(R.id.frame_layout,fragment)
        //fragmentTransaction.commit()
    }
}