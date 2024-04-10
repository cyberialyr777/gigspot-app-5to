package com.example.myapplication

import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivitySesionesConsultaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SessionesActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySesionesConsultaBinding
    lateinit var sesionesDBHelper: mySQliteHelper
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var newArray: ArrayList<SessionsModelo>


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySesionesConsultaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sesionesDBHelper = mySQliteHelper(this)
        auth = FirebaseAuth.getInstance()
        recyclerView = binding.recyclerView

        sesionesDBHelper = mySQliteHelper(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        displayuser()
    }

    private fun displayuser(){
        val newCursor: Cursor? = sesionesDBHelper.gettext()
        newArray = ArrayList<SessionsModelo>()
        if (newCursor != null) {
            while (newCursor.moveToNext()){
                val id = newCursor.getInt(0)
                val email = newCursor.getString(1)
                val pass = newCursor.getString(2)
                newArray.add(SessionsModelo(id,email,pass))
            }
        }
        recyclerView.adapter = CustomAdapterSessions(newArray)
    }
}