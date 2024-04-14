package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Modelos.Event
import com.example.myapplication.Modelos.SaveModelo
import com.example.myapplication.databinding.FragmentSaveBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class SaveFragment : Fragment() {
    val TAG = "SaveFragment"
    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentSaveBinding ?= null
    val storage = FirebaseStorage.getInstance()
    lateinit var myAdapter: CustomAdapterSaveUser
    private lateinit var dbreferes : DatabaseReference
    private val binding get() = _binding
    private lateinit var newArray: ArrayList<SaveModelo>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d(TAG,"onCreate: ")
        _binding = FragmentSaveBinding.inflate(inflater,container,false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        newArray = arrayListOf()
        init()
        getDataEventos()
    }

    private fun init() {
        binding?.recyclreView?.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        myAdapter= CustomAdapterSaveUser(newArray){ evento ->
            onSelectEvent(evento)
        }
        binding?.recyclreView?.adapter = myAdapter
    }
    fun onSelectEvent(event: SaveModelo){
        val id = event.idEvento
        val intent = Intent(requireContext(), EventsActivity::class.java)
        intent.putExtra("ID_EVENTO", id)
        startActivity(intent)
    }

    private fun getDataEventos() {
        dbreferes = FirebaseDatabase.getInstance().getReference("saves")
        dbreferes.orderByChild("emailUser").equalTo(auth.currentUser?.email).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("firebaseResul","Exitoso  onDataChange")
                newArray.clear()
                if(snapshot.exists()){
                    for(dataSnapshot in snapshot.children){
                        Log.d("firebaseResul","Exitoso  datasnapshot")
                        val data = dataSnapshot.getValue(SaveModelo::class.java)
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