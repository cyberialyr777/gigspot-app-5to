package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.myapplication.Profiles.ProfileBandFragment
import com.example.myapplication.Profiles.ProfileFragment
import com.example.myapplication.databinding.ActivityBottomNavigationBinding


class BottomNavigationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBottomNavigationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val targetFragment = intent.getStringExtra("targetFragment")

        if (targetFragment == "ProfileFragment") {
            val profileBandFragment = ProfileFragment()
            replaceFragment(profileBandFragment)
            binding.bottomNavigation.setSelectedItemId(R.id.bottom_profile)
        } else {
            replaceFragment(HomeFragment())
        }

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.bottom_home -> replaceFragment(HomeFragment())
                R.id.bottom_profile-> replaceFragment(ProfileFragment())
                R.id.bottom_save-> replaceFragment(SaveFragment())
                else -> false
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }
}