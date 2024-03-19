package com.example.myapplication

import DatabaseOperaton
import android.app.DownloadManager.Query
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

public class PruebaFormularioActivity : AppCompatActivity(){
    private lateinit var binding: ActiviyPruebaFormularioBdBinding


    /*
    private lateinit var recyclerView: RecyclerView
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var myAdapter: RecyclerView.Adapter<*>
    */


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActiviyPruebaFormularioBdBinding.inflate(layoutInflater)
        setContentView(binding.root)

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