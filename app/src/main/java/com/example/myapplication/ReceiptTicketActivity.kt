package com.example.myapplication

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import android.widget.Toast
import com.example.myapplication.Modelos.BandaModelo
import com.example.myapplication.Modelos.BoughtModelo
import com.example.myapplication.Modelos.Event
import com.example.myapplication.Modelos.UsuariosModelos
import com.example.myapplication.databinding.ActivityReceiptTicketBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class ReceiptTicketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReceiptTicketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceiptTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("ID_EVENTO").toString()

        datosCompra(id)
    }

    private fun datosCompra(id: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("bought")
        databaseReference.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val evento = snapshot.getValue(BoughtModelo::class.java)
                    if (evento != null) {
                        // Convertir la cadena base64 a un Bitmap
                        val qrBitmap = base64ToBitmap(evento.qrCode!!)
                        binding.qrCode.setImageBitmap(qrBitmap)
                        binding.sectorDb.setText(evento.total)
                        binding.seatDb.setText(evento.cantidadBoletos)

                        val databaseReference2 = FirebaseDatabase.getInstance().getReference("eventos")
                        databaseReference2.orderByChild("id").equalTo(evento.idEvento).addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (snapshot2 in snapshot.children){
                                    val evento2 = snapshot2.getValue(Event::class.java)
                                    if(evento2 != null){
                                        binding.textView2.setText(evento2.date)
                                        binding.textView9.setText(evento2.time)
                                        binding.textView3.setText(evento2.place)

                                        val databaseReference3 = FirebaseDatabase.getInstance().getReference("usuario")
                                        databaseReference3.orderByChild("email").equalTo(evento2.emailBand).addListenerForSingleValueEvent(object : ValueEventListener{
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                for(snapshot3 in snapshot.children){
                                                    val banda = snapshot3.getValue(BandaModelo::class.java)
                                                    binding.nombreBanda.setText(banda!!.bandName + ": " + evento2.titulo)
                                                }
                                            }

                                            override fun onCancelled(error: DatabaseError) {
                                                TODO("Not yet implemented")
                                            }

                                        })
                                    }

                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar el caso donde la consulta a la base de datos es cancelada o falla
                Toast.makeText(this@ReceiptTicketActivity, "Error al cargar los datos", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Función para convertir una cadena base64 a un Bitmap
    private fun base64ToBitmap(base64String: String): Bitmap? {
        val decodedString = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

}
