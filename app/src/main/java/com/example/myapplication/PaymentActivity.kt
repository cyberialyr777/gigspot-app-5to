package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.Modelos.BandaModelo
import com.example.myapplication.Modelos.BoughtModelo
import com.example.myapplication.Modelos.Event
import com.example.myapplication.Modelos.EventModelo
import com.example.myapplication.databinding.ActivityPatmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Base64
import java.io.ByteArrayOutputStream




class PaymentActivity: AppCompatActivity() {
    private lateinit var binding: ActivityPatmentBinding
    private lateinit var dbreferes : DatabaseReference
    private lateinit var database: DatabaseReference
    val storage = FirebaseStorage.getInstance()
    private lateinit var auth: FirebaseAuth
    private var total3 = ""
    private var cantidad3 = "1"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getStringExtra("ID_EVENTO").toString()
        auth = FirebaseAuth.getInstance()
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
        val emailUSer = auth.currentUser!!.email.toString()
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
                        total3 = evento.price!!
                        binding.total.text = total3
                        binding.amount.setText("1")

                        binding.amount.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                            }

                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                val cantidad = s?.toString()?.toIntOrNull() ?: 1
                                val precioInt = evento.price!!.toInt()

                                if (cantidad == 0) {
                                    binding.amount.setText("1")
                                    binding.amount.setSelection(binding.amount.text.length)
                                    return
                                }

                                val total2 = cantidad * precioInt

                                total3 = total2.toString()
                                cantidad3 = cantidad.toString()
                                binding.total.text = total3
                            }

                            override fun afterTextChanged(s: Editable?) {
                            }
                        })

                        binding.Buy.setOnClickListener {
                            if(checkAllField()){
                                insertar(id, emailUSer, total3, cantidad3)
                            }
                        }
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

    private fun insertar(idEvento: String, emailUser: String, total: String, cantidadBoletos: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("bought")
        database = FirebaseDatabase.getInstance().getReference("bought")

        val qrCode = generateQRCode(idEvento, emailUser, total, cantidadBoletos)
        val id = databaseReference.push().key
        val evento = BoughtModelo(
            id!!,
            idEvento,
            emailUser,
            total,
            cantidadBoletos,
            qrCode
        )
        val nuevaReferencia = database.child(id)
        nuevaReferencia.setValue(evento).addOnSuccessListener {
            Toast.makeText(
                this@PaymentActivity,
                "Successfully Bought",
                Toast.LENGTH_SHORT
            )
                .show()
            startActivity(
                Intent(
                    this@PaymentActivity,
                    BottomNavigationActivity::class.java
                )
            )
        }.addOnFailureListener {
            Toast.makeText(
                this@PaymentActivity,
                "Failed while Bought",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun generateQRCode(idEvento: String, emailUser: String, total: String, cantidadBoletos: String): String {
        // Crear los datos para el código QR
        val data = "ID Evento: $idEvento, Usuario: $emailUser, Total: $total, Boletos: $cantidadBoletos"

        // Generar la matriz de bits del código QR
        val multiFormatWriter = MultiFormatWriter()
        val bitMatrix = multiFormatWriter.encode(data, BarcodeFormat.QR_CODE, 400, 400)

        // Crear un bitmap a partir de la matriz de bits
        val bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888)
        for (x in 0 until 400) {
            for (y in 0 until 400) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }

        // Convertir el bitmap a base64
        return bitmapToBase64(bitmap)
    }

    // Función para convertir un bitmap a una cadena base64
    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }


}