package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeBandFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeBandFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBandBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener referencia al ImageView
        val imageView2: ImageView = view.findViewById(R.id.imageView2)

        // Agregar un click listener al imageView2
        imageView2.setOnClickListener {
            // Cuando se hace clic en imageView2, iniciar la EventsActivity
            val intent = Intent(requireActivity(), EventBandActivity::class.java)
            startActivity(intent)
        }
    }

    private fun filterData(query: String){
        getDataEventos(query)
    }
    fun onSelectEvent(event: Event){
        val nombreEvento = event.titulo
        val id = event.id
        val intent = Intent(requireContext(), EventBandActivity::class.java)
        intent.putExtra("ID_EVENTO", id)
        startActivity(intent)
        Log.i("selectEvent", "${nombreEvento}")
    }

    private fun getDataEventos(query: String) {
        dbreferes = FirebaseDatabase.getInstance().getReference("eventos")
        if(query.isNotEmpty()){
            dbreferes.orderByChild("titulo").startAt(query).endAt("${query}/uf8ff").addValueEventListener(object : ValueEventListener{
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
            }
    }
}