package com.example.myapplication.Profiles

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.myapplication.Modelos.FollowModelo
import com.example.myapplication.Modelos.BandaModelo
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityProfileBandUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

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
        consultafollow2(Auth.currentUser?.email!!, email)
    }

    private fun consultaBanda(email: String){
        Log.w("Consulta", "inicio")
        dbreferes = FirebaseDatabase.getInstance().getReference("usuario")
        dbreferes.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.w("Consulta", "snapshto")
                for(dataSnapshot in snapshot.children){
                    Log.i("Consulta", "${dataSnapshot}")
                    val user = dataSnapshot.getValue(BandaModelo::class.java)
                    Log.w("Consulta", "$user")
                    if(user != null){
                        Log.w("Consulta", "if")
                        binding.textView.setText(user?.bandName)
                        binding.textView4.setText(user?.description)
                        binding.textView10.setText("Genre: " + user?.genre)

                        val imageBackUrl = user?.imageBack
                        if (!imageBackUrl.isNullOrEmpty()) {
                            Picasso.get().load(imageBackUrl).into(binding.imageView2)
                        }

                        val imageProfileUrl = user?.imageProfile
                        if (!imageProfileUrl.isNullOrEmpty()) {
                            Picasso.get().load(imageProfileUrl).into(binding.imageButton3)
                        }

                        binding.button5.setOnClickListener {
                            consultafollow(user.email!!,Auth.currentUser?.email!!)
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
            binding.button5.setBackgroundResource(R.drawable.custom_for_buttons)
            Toast.makeText(this, "Following", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "fallo al seguir", Toast.LENGTH_SHORT).show()
        }
    }


    private fun consultafollow2(emailUser: String, emailBand: String){
        Log.w("Consulta", "inicio")
        dbreferes = FirebaseDatabase.getInstance().getReference("follows")
        dbreferes.orderByChild("emailUser").equalTo(emailUser).ref.orderByChild("emailBand").equalTo(emailBand).addListenerForSingleValueEvent(object : ValueEventListener {
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

    private fun consultafollow(emailUser: String, emailBand: String){
        Log.w("Consulta", "inicio")
        dbreferes = FirebaseDatabase.getInstance().getReference("follows")
        dbreferes.orderByChild("emailUser").equalTo(emailUser).ref.orderByChild("emailBand").equalTo(emailBand).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.w("Consulta", "snapshto")
                val user = snapshot.getValue(FollowModelo::class.java)
                Log.w("Consulta", "$user")
                if(user != null){
                    binding.button5.setBackgroundResource(R.drawable.custom_for_buttons)
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