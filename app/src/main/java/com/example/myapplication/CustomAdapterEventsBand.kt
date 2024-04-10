package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapterEventsBand (private val eventoList: ArrayList<EventModelo>): RecyclerView.Adapter<CustomAdapterEventsBand.MyViewHolderSession>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderSession {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_eventos,parent,false)
        return MyViewHolderSession(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolderSession, position: Int) {
        val curretItem = eventoList[position]
        holder.fecha.text = curretItem.date
        holder.seleccion.text = curretItem.titulo
    }

    override fun getItemCount(): Int {
        return eventoList.size
    }

    class MyViewHolderSession (itemView: View): RecyclerView.ViewHolder(itemView){
        var fecha : Button = itemView.findViewById(R.id.fechaEvento)
        var seleccion : Button = itemView.findViewById(R.id.entrarEvento)
        val imagen : ImageView = itemView.findViewById(R.id.imagenSeleccionada)
    }
}