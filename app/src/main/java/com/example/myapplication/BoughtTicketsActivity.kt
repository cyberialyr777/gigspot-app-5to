package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityBoughtTicketsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.myapplication.Modelos.Event

class BoughtTicketsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBoughtTicketsBinding
    private lateinit var newArray: ArrayList<Event> //
    private lateinit var dbReference: DatabaseReference
    private lateinit var myAdapter: CustomAdapterEventsUser
    private val TAG = "BoughtTicketsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoughtTicketsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        newArray = arrayListOf()
        init()
        getDataTickets("")
    }

    private fun init() {
        binding.boughtTicketsRecycler.layoutManager = LinearLayoutManager(this)
        myAdapter = CustomAdapterEventsUser(newArray) { event ->
            // Acciones cuando se selecciona un evento
        }
        binding.boughtTicketsRecycler.adapter = myAdapter
    }

    private fun getDataTickets(query: String) {
        dbReference = FirebaseDatabase.getInstance().getReference("eventos") // Cambia a la referencia de tickets
        if (query.isNotEmpty()) {
            dbReference.orderByChild("titulo").startAt(query).endAt("${query}/uf8ff").addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    newArray.clear()
                    if (snapshot.exists()) {
                        for (dataSnapshot in snapshot.children) {
                            val data = dataSnapshot.getValue(Event::class.java) // Cambia a la clase de Ticket si lo prefieres
                            data?.let {
                                newArray.add(it)
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })
        } else {
            dbReference.addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    newArray.clear()
                    if (snapshot.exists()) {
                        for (dataSnapshot in snapshot.children) {
                            val data = dataSnapshot.getValue(Event::class.java)
                            data?.let {
                                newArray.add(it)
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy :")
    }
}
