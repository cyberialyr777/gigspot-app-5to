package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Modelos.BandaModelo
import com.example.myapplication.Modelos.FollowModelo
import com.example.myapplication.Modelos.UsuariosModelos
import com.example.myapplication.Profiles.ProfileBandUserActivity
import com.example.myapplication.databinding.CardFollowsBandBinding
import com.example.myapplication.databinding.CardFollowsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class FollowBandViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
    val binding = CardFollowsBandBinding.bind(itemView)


    fun render(item: FollowModelo, onClickListener: (FollowModelo) -> Unit){

        binding.button14.setOnClickListener {
            deleteFollow(item.id!!)
        }

        val dbreferes = FirebaseDatabase.getInstance().getReference("usuario")
        dbreferes.orderByChild("email").equalTo(item.emailUser).addListenerForSingleValueEvent(object :
            ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.w("Consulta", "snapshto")
                for(dataSnapshot in snapshot.children){
                    Log.i("Consulta", "${dataSnapshot}")
                    val user = dataSnapshot.getValue(UsuariosModelos::class.java)
                    Log.w("Consulta", "$user")
                    if(user != null){
                        Log.w("Consulta", "if")

                        binding.textView16.text = user.firstName +" "+ user.userlastName +": "+ user.userName

                        val imageProfileUrl = user.imageProfile
                        if (!imageProfileUrl.isNullOrEmpty()) {
                            Picasso.get().load(imageProfileUrl).into(binding.imageView3)
                        }else{
                            Picasso.get().load(item.imageBand).into(binding.imageView3)
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun deleteFollow(id: String) {
        val dbReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("follows")
        val usuarioRef: DatabaseReference = dbReference.child(id)

        usuarioRef.removeValue()
            .addOnSuccessListener { Log.i("EliminarUsuario", "Usuario eliminado exitosamente")
                Toast.makeText(itemView.context, "Unfollow", Toast.LENGTH_SHORT).show()

                itemView.context.startActivity(Intent(itemView.context, FollowUserActivity::class.java))
                (itemView.context as AppCompatActivity).finish()
            }
            .addOnFailureListener { error ->
                Log.e("EliminarUsuario", "Error al eliminar usuario: ${error.message}")
                Toast.makeText(itemView.context, "Error al eliminar usuario", Toast.LENGTH_SHORT).show()
            }
    }
}