package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
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
        val selectedAddress = intent.getStringExtra("selectedAddress")

        // Verificar si targetFragment es "AddBandFragment" para cargar AddBandFragment
        if (targetFragment == "AddBandFragment") {
            val addBandFragment = AddBandFragment()

            // Pasar la dirección seleccionada como argumento a AddBandFragment
            val args = Bundle()
            args.putString("selectedAddress", selectedAddress)
            addBandFragment.arguments = args

            // Cargar el fragmento de AddBandFragment
            replaceFragment(addBandFragment)

            // Configurar el botón de navegación a "add1" para reflejar la selección
            binding.bottomNavigationView2.setSelectedItemId(R.id.add1)
        } else {
            // Cargar el fragmento por defecto (HomeBandFragment)
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
            .replace(R.id.frame_layout1, fragment) // Reemplaza en el contenedor adecuado
            .addToBackStack(null)
            .commit()
    }
}
