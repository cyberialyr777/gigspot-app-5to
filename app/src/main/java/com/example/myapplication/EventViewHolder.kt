package com.example.myapplication

import android.content.DialogInterface.OnClickListener
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.CardEventosBinding
import com.squareup.picasso.Picasso

class EventViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
    val binding = CardEventosBinding.bind(itemView)

    fun render(item: Event,onClickListener: (Event)->Unit){
        binding.fechaEvento.text=item.date
        binding.entrarEvento.text=item.titulo
        Picasso.get().load(item.imagen).into(binding.imagenSelecionada)
        itemView.setOnClickListener{onClickListener(item)}
    }
}