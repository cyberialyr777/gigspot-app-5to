package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.Modelos.BandaModelo
import com.example.myapplication.Modelos.Event
import com.example.myapplication.databinding.ActivityPatmentBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class PaymentActivity: AppCompatActivity() {
    private lateinit var binding: ActivityPatmentBinding
    private lateinit var dbreferes : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getStringExtra("ID_EVENTO").toString()

        binding.Buy.setOnClickListener {
            if(checkAllField()){

            }
        }
        cosultaEvento(id)
    }

    private fun checkAllField(): Boolean{
        if(binding.password2.text.toString() == ""){
            binding.password2.error = "this field is required"
            return false
        }
        if(binding.password2.length() != 16){
            binding.password2.error = "the card number must be 16 caracters"
            return false
        }
        if(binding.password3.text.toString() == ""){
            binding.password3.error = "Select the day of Expiration"
            return false
        }
        return true
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
                        consultaBanda(evento.emailBand!!, evento.titulo!!)
                        binding.total.text = evento.price
                        binding.amount.setText("1")

                        binding.amount.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                                // No se necesita hacer nada aquí
                            }

                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                val cantidad = s?.toString()?.toIntOrNull() ?: 1
                                val precioInt = evento.price!!.toInt()

                                if (cantidad == 0) {
                                    binding.amount.setText("1")
                                    binding.amount.setSelection(binding.amount.text.length)
                                    return
                                }

                                val total = cantidad * precioInt
                                binding.total.text = total.toString()
                            }

                            override fun afterTextChanged(s: Editable?) {
                                // No se necesita hacer nada aquí
                            }
                        })

                        Log.w("Consulta", "${evento.titulo}")

                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }



    private fun consultaBanda(email: String, titulo: String){
        Log.w("Consulta", "inicio")
        dbreferes = FirebaseDatabase.getInstance().getReference("usuario")
        dbreferes.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.w("Consulta", "snapshto")
                for(dataSnapshot in snapshot.children){
                    Log.i("Consulta", "${dataSnapshot}")
                    val user = dataSnapshot.getValue(BandaModelo::class.java)
                    Log.w("Consulta", "$user")
                    if(user != null){
                        Log.w("Consulta", "if")
                        binding.ConcertTitle.setText(titulo + " By: " + user.bandName)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


}