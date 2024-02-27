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
import android.content.Context
import android.widget.Toast
public class PruebaFormularioActivity : AppCompatActivity(){

    private lateinit var binding: ActiviyPruebaFormularioBdBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActiviyPruebaFormularioBdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener(){
            /*val nombre = binding.UserName.text.toString()
            val apellido = binding.LastName.text.toString()
            val connx = DatabaseOperaton()
            val conn = connx.getConnection()
            if (conn != null) {
                connx.insert(connection = conn,nombre,apellido)
                startActivity(Intent(this, LoginActivity::class.java))
            }*/
            val connx = DatabaseOperaton()
            val conn = connx.getConnection()
            if (conn != null) {
                connx.insert(connection = conn,"nombre","apellido")

                val context: Context = applicationContext
                val mensaje = "insert exitoso"
                val duracion = Toast.LENGTH_SHORT

                val toast = Toast.makeText(context, mensaje, duracion)
                toast.show()
            }
        }
    }
 }