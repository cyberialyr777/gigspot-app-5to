package com.example.myapplication.Profiles

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.myapplication.BandMenuActivity
import com.example.myapplication.Modelos.BandaModelo
import com.example.myapplication.databinding.ActivityEditProfileBandBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class EditProfileBandActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityEditProfileBandBinding
    val storage = FirebaseStorage.getInstance()

    private var selectedImageBUri: Uri? = Uri.parse("")
    private var selectedImageBUrl: String? = ""
    private var selectedImagePUri: Uri? = Uri.parse("")
    private var selectedImagePUrl: String? = ""

    companion object {
        const val IMAGE_REQUEST_CODE_1 = 1
        const val IMAGE_REQUEST_CODE_2 = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        binding = ActivityEditProfileBandBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.button4.setOnClickListener {
                actualizar()
        }
        datosUsuario()
    }



    private fun datosUsuario(){
        val databaseReference = FirebaseDatabase.getInstance().getReference("usuario")
        databaseReference.orderByChild("email").equalTo(auth.currentUser?.email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(snapshot in dataSnapshot.children){
                    val usuario = snapshot.getValue(BandaModelo::class.java)
                    binding.firstName.setText(usuario?.bandName)
                    binding.descripcion.setText(usuario?.description)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun actualizar() {
        val contexto = applicationContext
        val databaseReference = FirebaseDatabase.getInstance().getReference("usuario")

        databaseReference.orderByChild("email").equalTo(auth.currentUser?.email).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(BandaModelo::class.java)

                    if (user != null && user.email == auth.currentUser?.email) {
                        val actualizacionMap = hashMapOf<String, Any?>(
                            "bandName" to binding.firstName.text.toString().trim(),
                            "description" to binding.descripcion.text.toString().trim(),
                        )
                        snapshot.ref.updateChildren(actualizacionMap)
                            .addOnSuccessListener {
                                Log.d("UriImage1", "URI de la imagen 1: $selectedImageBUri")
                                Log.d("UriImage1", "URI de la imagen 1: $selectedImagePUri")

                                finish()
                                val intent = Intent(this@EditProfileBandActivity, BandMenuActivity::class.java)
                                intent.putExtra("targetFragment2", "ProfileBandFragment") // Identificador del fragmento a cargar
                                startActivity(intent)
                            }
                            .addOnFailureListener {
                                Toast.makeText(contexto, "Error", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}