package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentHomeBandBinding
import com.example.myapplication.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment(), SearchView.OnQueryTextListener {
    private lateinit var auth: FirebaseAuth
    private lateinit var newArray: ArrayList<Event>
    private lateinit var dbreferes : DatabaseReference
    lateinit var myAdapter: CustomAdapterEventsUser
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    val TAG = "HomeFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        Log.d(TAG,"onCreate: ")
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        newArray = arrayListOf()
        binding.buscador?.setOnQueryTextListener(this)
        init()
        getDataEventos("")
        Log.d(TAG, "onViewCreated: ")


    }

    private fun init() {
        binding.eventoRecycler?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        myAdapter= CustomAdapterEventsUser(newArray){ evento ->
            onSelectEvent(evento)
        }
        binding.eventoRecycler?.adapter = myAdapter
    }

    private fun filterData(query: String){
        getDataEventos(query)
    }
    fun onSelectEvent(event: Event){
        val nombreEvento = event.titulo
        Log.i("selectEvent", "${nombreEvento}")
    }

    private fun getDataEventos(query: String) {
        dbreferes = FirebaseDatabase.getInstance().getReference("eventos")
        if(query.isNotEmpty()){
            dbreferes.orderByChild("emailBand").startAt(query).endAt("${query}/uf8ff").addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("firebaseResul","Exitoso snapshot")
                    newArray.clear()
                    if(snapshot.exists()){
                        for (dataSnapshot in snapshot.children){
                            val data = dataSnapshot.getValue(Event::class.java)
                            data?.let{
                                newArray.add(it)
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("firebase", "Failed to read value.", error.toException())                }

            })
        }else{
            Log.d("firebaseResul","Exitoso sin query")
            dbreferes.orderByChild("emailBand").addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("firebaseResul","Exitoso  onDataChange")
                    newArray.clear()
                    if(snapshot.exists()){
                        for(dataSnapshot in snapshot.children){
                            Log.d("firebaseResul","Exitoso  datasnapshot")
                            val data = dataSnapshot.getValue(Event::class.java)
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
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()){
            filterData(query)
        }
        return true
    }
    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let { filterData(it) }
        return true
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
