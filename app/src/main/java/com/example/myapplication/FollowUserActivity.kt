package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Modelos.FollowModelo
import com.example.myapplication.Profiles.ProfileBandUserActivity
import com.example.myapplication.databinding.ActivityFollowBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FollowUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFollowBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var newArray: ArrayList<FollowModelo>
    private lateinit var dbreferes : DatabaseReference
    lateinit var myAdapter: CustomAdapterFollowUsers
    val TAG = "Follow Actvity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        newArray = arrayListOf()

        init()
        getDataFollow()

    }

    private fun init() {
        binding.eventoRecyclerFollow.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        myAdapter= CustomAdapterFollowUsers(newArray){ evento ->
            onSelectEvent(evento)
        }
        binding.eventoRecyclerFollow.adapter = myAdapter
    }

    fun onSelectEvent(event: FollowModelo){
        val id = event.emailBand
        val intent = Intent(this, ProfileBandUserActivity::class.java)
        intent.putExtra("ID_EVENTO", id)
        startActivity(intent)
        Log.i("selectEvent", "${id}")
    }

    private fun getDataFollow() {
        dbreferes = FirebaseDatabase.getInstance().getReference("follows")
        Log.d("firebaseResul","Exitoso sin query")
        dbreferes.orderByChild("emailUser").equalTo(auth.currentUser?.email).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("DataCheck", "newArray size: ${newArray.size}")

                newArray.clear()
                if(snapshot.exists()){
                    for(dataSnapshot in snapshot.children){
                        Log.d("firebaseResul","Exitoso  datasnapshot")
                        val data = dataSnapshot.getValue(FollowModelo::class.java)
                        data?.let{
                            newArray.add(it)
                        }

                    }

                    myAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("firebase", "Failed to read value.", error.toException())
                Toast.makeText(this@FollowUserActivity, "Error en la lectura de datos: ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG,"onStart: ")
    }

    override fun onResume(){
        super.onResume()
        Log.d(TAG,"onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG,"onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG,"onStop: ")
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy :")
    }
}