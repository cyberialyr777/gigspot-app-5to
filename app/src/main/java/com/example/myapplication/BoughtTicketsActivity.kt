package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Modelos.BoughtModelo
import com.example.myapplication.databinding.ActivityBoughtTicketsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.auth.FirebaseAuth

class BoughtTicketsActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityBoughtTicketsBinding
    private lateinit var newArray: ArrayList<BoughtModelo>
    private lateinit var myAdapter: CustomAdapterBougth
    private lateinit var dbreferes : DatabaseReference
    private val TAG = "BoughtTicketsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoughtTicketsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        newArray = arrayListOf()
        init()
        getDataEventos()
    }

    private fun init() {
        binding.boughtTicketsRecycler.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)
        myAdapter = CustomAdapterBougth(newArray){ evento ->
            onSelectEvent(evento)
        }
        binding.boughtTicketsRecycler.adapter = myAdapter
    }

    fun onSelectEvent(event: BoughtModelo){
        val id = event.id
        val intent = Intent(this, ReceiptTicketActivity::class.java)
        intent.putExtra("ID_EVENTO", id)
        startActivity(intent)
    }

    private fun getDataEventos() {
        dbreferes = FirebaseDatabase.getInstance().getReference("bought")
        dbreferes.orderByChild("emailUser").equalTo(auth.currentUser?.email).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("firebaseResul","Exitoso  onDataChange")
                newArray.clear()
                if(snapshot.exists()){
                    for(dataSnapshot in snapshot.children){
                        Log.d("firebaseResul","Exitoso  datasnapshot")
                        val data = dataSnapshot.getValue(BoughtModelo::class.java)
                        data?.let {
                            newArray.add(it)
                        }
                    }
                    myAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("firebase", "Failed to read value.", error.toException())                }

        })
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
