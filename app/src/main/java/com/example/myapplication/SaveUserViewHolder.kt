package com.example.myapplication

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Modelos.BandaModelo
import com.example.myapplication.Modelos.Event
import com.example.myapplication.Modelos.SaveModelo
import com.example.myapplication.Profiles.ProfileBandUserActivity
import com.example.myapplication.databinding.CardEventosBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class SaveUserViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
    private lateinit var auth: FirebaseAuth
    val binding = CardEventosBinding.bind(itemView)

    fun render(item: SaveModelo, onClickListener: (SaveModelo)->Unit){
        auth = FirebaseAuth.getInstance()
        val dbreferes2 = FirebaseDatabase.getInstance().getReference("eventos")
        dbreferes2.orderByChild("id").equalTo(item.idEvento).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(dataSnapshot2 in snapshot.children){
                    val event = dataSnapshot2.getValue(Event::class.java)
                    if(event != null) {
                        Picasso.get().load(event.imagen).into(binding.imagenSelecionada)
                        binding.fechaEvento.text = event.date
                        binding.entrarEvento.text = event.titulo
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        itemView.setOnClickListener {
            onClickListener(item)
        }
    }
}