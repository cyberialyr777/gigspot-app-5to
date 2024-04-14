package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.databinding.ActivityEventsBinding
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.Modelos.BandaModelo
import com.example.myapplication.Modelos.Event
import com.example.myapplication.Modelos.FollowModelo
import com.example.myapplication.Modelos.SaveModelo
import com.example.myapplication.Profiles.ProfileBandUserActivity
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
        auth = FirebaseAuth.getInstance()
        binding = ActivityEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cosultaEvento(id)
        consultaSave2(auth.currentUser?.email!!, id)

        val BuyTicket = binding.button

        BuyTicket.setOnClickListener {
            startActivity(Intent(this, TicketActivity::class.java))
        }

        val textview = findViewById<TextView>(R.id.textView)
    }

    private fun cosultaEvento(id: String){
        Log.w("Consulta", id)
        Log.w("Consulta", "inicio")
        dbreferes = FirebaseDatabase.getInstance().getReference("eventos")
        var dbreferes2 = FirebaseDatabase.getInstance().getReference("usuario")
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
                        consultaBanda(evento.emailBand!!, id)
                        Picasso.get().load(evento.imagen).into(binding.imageView2)
                        binding.textView8.text = evento.titulo
                        binding.textView2.text = evento.date
                        binding.textView9.text = evento.time
                        binding.textView3.text = evento.place
                        binding.textView7.text = evento.price
                        binding.textView4.text = evento.description
                        Log.w("Consulta", "${evento.titulo}")

                        dbreferes2.orderByChild("email").equalTo(evento.emailBand).addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onDataChange(snapshot2: DataSnapshot) {
                                for(dataSnapshot2 in snapshot2.children){
                                    val band = dataSnapshot2.getValue(BandaModelo::class.java)
                                    val imageProfileUrl = band?.imageProfile
                                    if (!imageProfileUrl.isNullOrEmpty()) {
                                        Picasso.get().load(imageProfileUrl).into(binding.imageButton3)
                                    }
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })

                        binding.imageButton3.setOnClickListener{
                            val intent = Intent(this@EventsActivity, ProfileBandUserActivity::class.java)
                            intent.putExtra("ID_EVENTO", evento.emailBand)
                            startActivity(intent)
                        }
                        binding.button.setOnClickListener {
                            val intent = Intent(this@EventsActivity,PaymentActivity::class.java)
                            intent.putExtra("ID_EVENTO", id)
                            startActivity(intent)
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun consultaBanda(email: String, idEvento: String){
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
                        binding.textView.text = user.bandName

                        binding.button5.setOnClickListener {
                            consultaSave(user.email!!,auth.currentUser?.email!!, idEvento)
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun save(email: String, emailBand: String, idEvento: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("saves")
        val id = databaseReference.push().key
        val save = SaveModelo(id, idEvento, emailBand ,email)
        databaseReference.child(id!!).setValue(save).addOnSuccessListener {
            binding.button5.setBackgroundResource(R.drawable.custom_for_buttons)
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            binding.button5.setBackgroundResource(R.drawable.follow_btn)
            Toast.makeText(this, "Fail save", Toast.LENGTH_SHORT).show()
        }
    }


    private fun consultaSave(emailUser: String, emailBand: String, idEvento: String) {
        Log.w("Consulta", "inicio")
        val dbReference = FirebaseDatabase.getInstance().getReference("saves")

        dbReference.orderByChild("idEvento").equalTo(idEvento)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach { dataSnapshot ->
                            val save = dataSnapshot.getValue(SaveModelo::class.java)
                            save?.let {
                                val saveRef = dbReference.child(it.id!!)
                                saveRef.removeValue()
                                    .addOnSuccessListener {
                                        Log.i("EliminarUsuario", "Guardado eliminado exitosamente")
                                        binding.button5.setBackgroundResource(R.drawable.follow_btn)
                                        Toast.makeText(this@EventsActivity, "Unsave", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener { error ->
                                        Log.e("EliminarUsuario", "Error al eliminar guardado: ${error.message}")
                                    }
                            }
                        }
                    } else {
                        save(emailUser, emailBand, idEvento)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Consulta", "Error en consulta: ${error.message}")
                }
            })
    }


    private fun consultaSave2(emailUser: String, idEvento: String){
        Log.w("Consulta", "inicio")
        dbreferes = FirebaseDatabase.getInstance().getReference("saves")
        dbreferes.orderByChild("idEvento").equalTo(idEvento).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.w("Consulta", "snapshto")
                val user = snapshot.getValue(FollowModelo::class.java)
                Log.w("Consulta", "$user")
                if(user != null){
                    binding.button5.setBackgroundResource(R.drawable.custom_for_buttons)
                }else{
                    binding.button5.setBackgroundResource(R.drawable.follow_btn)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}