package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Modelos.BoughtModelo

class CustomAdapterBougth(private val eventoList:ArrayList<BoughtModelo>, private val onClickListener:(BoughtModelo)->Unit): RecyclerView.Adapter<BougthViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BougthViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_eventos,parent,false)
        return BougthViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BougthViewHolder, position: Int) {
        val curretItem = eventoList[position]
        holder.render(curretItem,onClickListener)
    }

    override fun getItemCount(): Int {
        return eventoList.size
    }
}