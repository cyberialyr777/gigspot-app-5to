package com.example.myapplication

import DatabaseOperaton
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActiviyPruebaFormularioBdBinding
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

public class PruebaFormularioActivity : AppCompatActivity(){

    private lateinit var binding: ActiviyPruebaFormularioBdBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActiviyPruebaFormularioBdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener(){
            val nombre = binding.UserName.text.toString()
            val apellido = binding.LastName.text.toString()

            val conn = DatabaseOperaton.getConnection()
            if (conn != null) {
                DatabaseOperaton.insert(connection = conn,nombre,apellido)
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }
 }