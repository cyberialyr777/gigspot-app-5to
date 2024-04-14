package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.myapplication.Profiles.ProfileBandFragment
import com.example.myapplication.databinding.ActivityBandMenuBinding

class BandMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBandMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBandMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Verificar si hay un intent con targetFragment y selectedAddress
        val intent = intent
        val targetFragment = intent.getStringExtra("targetFragment")
        val targetFragment2 = intent.getStringExtra("targetFragment2")
        val selectedAddress = intent.getStringExtra("selectedAddress")


        if (targetFragment == "AddBandFragment") {
            val addBandFragment = AddBandFragment()
            val args = Bundle()
            args.putString("selectedAddress", selectedAddress)
            addBandFragment.arguments = args
            replaceFragment(addBandFragment)
            binding.bottomNavigationView2.setSelectedItemId(R.id.add1)
        } else {
            replaceFragment(HomeBandFragment())
        }

        if (targetFragment2 == "ProfileBandFragment") {
            val profileBandFragment = ProfileBandFragment()
            replaceFragment(profileBandFragment)
            binding.bottomNavigationView2.setSelectedItemId(R.id.profile1)
        } else {
            replaceFragment(HomeBandFragment())
        }


        // Configurar el listener para el bottom navigation view
        binding.bottomNavigationView2.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home1 -> replaceFragment(HomeBandFragment())
                R.id.add1 -> replaceFragment(AddBandFragment())
                R.id.profile1 -> replaceFragment(ProfileBandFragment())
                else -> false
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout1, fragment)
            .addToBackStack(null)
            .commit()
    }
}
