package com.example.myapplication

import DatabaseOperaton
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActiviyPruebaFormularioBdBinding
import java.sql.PreparedStatement
import java.sql.SQLException



public class PruebaFormularioActivity : AppCompatActivity(){
    private lateinit var binding: ActiviyPruebaFormularioBdBinding

    /*
    private lateinit var recyclerView: RecyclerView
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var myAdapter: RecyclerView.Adapter<*>


    private lateinit var nombre: EditText
    private lateinit var apellido: EditText
    private lateinit var usuario: EditText
    private lateinit var email: EditText
    private lateinit var contraseña: EditText
    private lateinit var edad: EditText
    */

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActiviyPruebaFormularioBdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.SendButton.setOnClickListener(){
            val miObjeto = DatabaseOperaton()
            miObjeto.conexion()
        }
        /*
        nombre = findViewById(binding.name.id)
        apellido = findViewById(binding.lastname.id)
        usuario = findViewById(binding.username.id)
        email = findViewById(binding.email.id)
        contraseña = findViewById(binding.password.id)
        edad = findViewById(binding.age.id)

        binding.SendButton.setOnClickListener(){
            val edadInt = edad.text.toString().toInt()

            try {
                val addPersona: PreparedStatement? = connectionSQL.dbConeccion()?.prepareStatement("INSERT INTO personas (userName, email, name, lastName, age) VALUES (?,?,?,?,?)")
                addPersona?.setString(1, usuario.text.toString())
                addPersona?.setString(2, email.text.toString())
                addPersona?.setString(3, nombre.text.toString())
                addPersona?.setString(4, apellido.text.toString())
                addPersona?.setInt(5, edadInt)

                Toast.makeText(this, "Persona añadida correctamente", Toast.LENGTH_SHORT).show()
            }catch (ex: SQLException){
                Toast.makeText(this, "Error Persona", Toast.LENGTH_SHORT).show()
            }
        }
        */
    }



    /*
    private fun getRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("http://127.0.0.1:8000/api/getAdministrador/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun searchById(query:Int){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(API::class.java).getPersonas("$query")
            val specificID = call.body()
            if (call.isSuccessful){
                //show view
            }else{
                //show nothing
            }

        }
    }


    private fun getAllData() {
        Api.retrofitService.getAllData().enqueue(object : Callback<List<PersonaModel>> {
            override fun onResponse(call: Call<List<PersonaModel>>, response: Response<List<PersonaModel>>) {
                if (response.isSuccessful) {
                    recyclerView = findViewById<RecyclerView>(R.id.recycleView).apply {
                        myAdapter = PersonaAdapter(response.body()!!)
                        layoutManager = manager
                        adapter = myAdapter
                    }
                }
            }

            override fun onFailure(call: Call<List<PersonaModel>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }*/

 }