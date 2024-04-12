package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.example.myapplication.databinding.ActivityEventBandBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class EventBandActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityEventBandBinding
    private lateinit var dbreferes : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = intent.getStringExtra("ID_EVENTO").toString()
        binding = ActivityEventBandBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cosultaEvento(id)

        binding.imageButton4.setOnClickListener {
            val intent = Intent(this, HomeBandFragment::class.java)
            startActivity(intent)
        }

        binding.button2.setOnClickListener {
            val intent = Intent(this, EditBandActivity::class.java)
            startActivity(intent)
        }
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show()

    }

    private fun cosultaEvento(id: String){
        Log.w("Consulta", id)
        Log.w("Consulta", "inicio")
        dbreferes = FirebaseDatabase.getInstance().getReference("eventos")
        dbreferes.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.w("Consulta", "snapshto")
                for(dataSnapshot in snapshot.children){
                    Log.i("Consulta", "${dataSnapshot}")
                    val evento = dataSnapshot.getValue(Event::class.java)
                    Log.w("Consulta", "$evento")
                    if(evento != null){
                        Log.w("Consulta", "if")
                        consultaBanda(evento.emailBand!!)
                        Picasso.get().load(evento.imagen).into(binding.imagenSeleccionada)
                        binding.titulo.text = evento.titulo
                        binding.fecha.text = evento.date
                        binding.hora.text = evento.time
                        binding.lugar.text = evento.place
                        binding.precio.text = evento.price
                        binding.descripcion.text = evento.description
                        Log.w("Consulta", "${evento.titulo}")
                        binding.imageButton3.setOnClickListener{
                            Toast.makeText(this@EventBandActivity, evento.emailBand, Toast.LENGTH_SHORT).show()
                        }
                        binding.button.setOnClickListener {
                            eliminarDato(id)
                            Toast.makeText(this@EventBandActivity, "Successfully deleted", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun consultaBanda(email: String){
        Log.w("Consulta", "inicio")
        dbreferes = FirebaseDatabase.getInstance().getReference("usuario")
        dbreferes.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.w("Consulta", "snapshto")
                for(dataSnapshot in snapshot.children){
                    Log.i("Consulta", "${dataSnapshot}")
                    val user = dataSnapshot.getValue(BandaModelo::class.java)
                    Log.w("Consulta", "$user")
                    if(user != null){
                        Log.w("Consulta", "if")
                        // Picasso.get().load(user.imagen).into(binding.imageButton3)
                        binding.nombreBanda.text = user.bandName
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun eliminarDato(clave: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("eventos")
        val nodoAEliminar = dbRef.child(clave)

        nodoAEliminar.removeValue()
            .addOnSuccessListener {
                startActivity(Intent(this@EventBandActivity,HomeBandFragment::class.java))
            }
            .addOnFailureListener { error ->
                Log.e("Firebase", "Error al eliminar el nodo: ${error.message}")
            }
    }
}