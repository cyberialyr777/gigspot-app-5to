package com.example.myapplication.Profiles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.myapplication.BandMenuActivity
import com.example.myapplication.BottomNavigationActivity
import com.example.myapplication.Modelos.BandaModelo
import com.example.myapplication.Modelos.UsuariosModelos
import com.example.myapplication.databinding.ActivityEditProfileUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditProfileUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileUserBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        datosUsuario()

        binding.button4.setOnClickListener {
            actualizar()
            startActivity(Intent(this, BottomNavigationActivity::class.java))
        }
    }

    private fun datosUsuario(){
        val databaseReference = FirebaseDatabase.getInstance().getReference("usuario")
        databaseReference.orderByChild("email").equalTo(auth.currentUser?.email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(snapshot in dataSnapshot.children){
                    val usuario = snapshot.getValue(UsuariosModelos::class.java)
                    binding.firstName.setText(usuario?.firstName)
                    binding.userlastName.setText(usuario?.userlastName)
                    binding.userName.setText(usuario?.userName)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun actualizar2(){
        val contexto = applicationContext
        val databaseReference = FirebaseDatabase.getInstance().getReference("usuario")

        databaseReference.orderByChild("email").equalTo(auth.currentUser?.email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(snapshot in dataSnapshot.children){
                    val usuario = snapshot.getValue(UsuariosModelos::class.java)
                    val user = usuario?.userName
                    val databaseReference2 = FirebaseDatabase.getInstance().getReference("usuario/$user")
                    val actualizacionMap = hashMapOf<String, Any>(
                        "firstName" to binding.firstName.text.toString().trim(),
                        "userName" to binding.userName.text.toString().trim(),
                        "userlastName" to binding.userlastName.toString().trim()
                    )
                    databaseReference.updateChildren(actualizacionMap)
                        .addOnSuccessListener {
                            databaseReference.child("nombre").setValue(binding.userName.text.toString().trim())
                                .addOnSuccessListener {
                                    Toast.makeText(contexto, "Successfully update", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(contexto, "failure while updating", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener {
                            Toast.makeText(contexto, "failure while updating", Toast.LENGTH_SHORT).show()
                        }
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
                    val user = snapshot.getValue(UsuariosModelos::class.java)

                    if (user != null && user.email == auth.currentUser?.email) {
                        val actualizacionMap = hashMapOf<String, Any?>(
                            "firstName" to binding.firstName.text.toString().trim(),
                            "userLastName" to binding.userlastName.text.toString().trim(),
                            "userName" to binding.userName.text.toString().trim(),
                        )
                        snapshot.ref.updateChildren(actualizacionMap)
                            .addOnSuccessListener {
                                finish()
                                val intent = Intent(this@EditProfileUserActivity, BottomNavigationActivity::class.java)
                                intent.putExtra("targetFragment", "ProfileFragment") // Identificador del fragmento a cargar
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