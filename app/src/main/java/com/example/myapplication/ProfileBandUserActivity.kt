package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import com.example.myapplication.databinding.ActivityProfileBandUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.UUID

class ProfileBandUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBandUserBinding
    private lateinit var Auth: FirebaseAuth
    private lateinit var dbreferes : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Auth = FirebaseAuth.getInstance()
        val email = intent.getStringExtra("ID_EVENTO").toString()
        binding = ActivityProfileBandUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        consultaBanda(email)

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
                        binding.textView.text = ": " + user.bandName
                        binding.button5.setOnClickListener {
                            consultafollow(Auth.currentUser?.email!!,user.email!!)
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun follow(email: String, emailBand: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("follows")
        val id = databaseReference.push().key
        val folow = FollowModelo(id ,email, emailBand)
        dbreferes.child(id!!).setValue(folow).addOnSuccessListener {
            Toast.makeText(this, "Following", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "fallo al seguir", Toast.LENGTH_SHORT).show()
        }
    }
    private fun consultafollow(emailUser: String, emailBand: String){
        Log.w("Consulta", "inicio")
        dbreferes = FirebaseDatabase.getInstance().getReference("follows")
        dbreferes.orderByChild("id").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.w("Consulta", "snapshto")
                val user = snapshot.getValue(FollowModelo::class.java)
                Log.w("Consulta", "$user")
                if(user != null){
                    Toast.makeText(this@ProfileBandUserActivity, "Already following", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@ProfileBandUserActivity, "Following", Toast.LENGTH_SHORT).show()
                    follow(emailUser,emailBand)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}