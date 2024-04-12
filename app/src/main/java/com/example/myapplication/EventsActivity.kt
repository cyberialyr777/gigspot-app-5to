package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.databinding.ActivityEventsBinding
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class EventsActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var dbreferes : DatabaseReference
    private lateinit var binding: ActivityEventsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = intent.getStringExtra("ID_EVENTO").toString()
        binding = ActivityEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cosultaEvento(id)

        val BuyTicket = binding.button

        BuyTicket.setOnClickListener {
            startActivity(Intent(this, TicketActivity::class.java))
        }

        val textview = findViewById<TextView>(R.id.textView)

        textview.setOnClickListener {
            val intent = Intent(this, ProfileBandUserActivity::class.java)
            startActivity(intent)
        }
    }

    private fun cosultaEvento(id: String){
        Log.w("Consulta", id)
        Log.w("Consulta", "inicio")
        dbreferes = FirebaseDatabase.getInstance().getReference("eventos")
        dbreferes.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.w("Consulta", "snapshto")
                for(dataSnapshot in snapshot.children){
                    Log.i("Consulta", "${dataSnapshot}")
                    val evento = dataSnapshot.getValue(Event::class.java)
                    Log.w("Consulta", "$evento")
                    if(evento != null){
                        Log.w("Consulta", "if")
                        consultaBanda(evento.emailBand!!)
                        Picasso.get().load(evento.imagen).into(binding.imageView2)
                        binding.textView8.text = evento.titulo
                        binding.textView2.text = evento.date
                        binding.textView9.text = evento.time
                        binding.textView3.text = evento.place
                        binding.textView7.text = evento.price
                        binding.textView4.text = evento.description
                        Log.w("Consulta", "${evento.titulo}")
                        binding.imageButton3.setOnClickListener{
                            val intent = Intent(this@EventsActivity, ProfileBandUserActivity::class.java)
                            intent.putExtra("ID_EVENTO", evento.emailBand)
                            startActivity(intent)
                            Toast.makeText(this@EventsActivity, evento.emailBand, Toast.LENGTH_SHORT).show()
                        }
                        binding.button.setOnClickListener {

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
        dbreferes.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.w("Consulta", "snapshto")
                for(dataSnapshot in snapshot.children){
                    Log.i("Consulta", "${dataSnapshot}")
                    val user = dataSnapshot.getValue(BandaModelo::class.java)
                    Log.w("Consulta", "$user")
                    if(user != null){
                        Log.w("Consulta", "if")
                        // Picasso.get().load(user.imagen).into(binding.imageButton3)
                        binding.textView.text = user.bandName
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}