package com.example.myapplication

import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CustomAdapter: RecyclerView.Adapter<CustomAdapter.ViewHolder>(){
    private lateinit var auth: FirebaseAuth
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_layout,viewGroup,false)
        auth = FirebaseAuth.getInstance()
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        /*
        viewHolder.itemTitle.text = titles[i]
        viewHolder.itemImage.setImageResource(images[i])
        viewHolder.itemButton.setOnClickListener {
            Intent(requireActivity, EventsActivity::class.java)

        }
        */

    }

    override fun getItemCount(): Int {
        var titles = emptyArray<String>()
        var images = emptyArray<Drawable>()

        val databaseReference = FirebaseDatabase.getInstance().getReference("eventos")
        databaseReference.orderByChild("idBand").equalTo(auth.currentUser?.uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(snapshot in dataSnapshot.children){
                    val evento = snapshot.getValue(EventModelo::class.java)
                    if(evento != null){
                        titles += evento.titulo
                        //images += evento.imagen
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        return titles.size
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemImage: ImageView
        var itemTitle: TextView
        var itemButton: Button
        init {
            itemImage = itemView.findViewById(R.id.image)
            itemTitle = itemView.findViewById(R.id.bandName)
            itemButton = itemView.findViewById(R.id.checkConcert)
        }
    }
}