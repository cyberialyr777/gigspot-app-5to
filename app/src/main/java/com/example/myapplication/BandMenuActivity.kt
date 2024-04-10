package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.ActivityBandMenuBinding

class BandMenuActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBandMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBandMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeBandFragment())

        binding.bottomNavigationView2.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home1 -> replaceFragment(HomeBandFragment())
                R.id.add1 -> replaceFragment(AddBandFragment())
                R.id.profile1 -> replaceFragment(ProfileBandFragment())
                else -> {
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout1,fragment)
        fragmentTransaction.commit()
    }
}