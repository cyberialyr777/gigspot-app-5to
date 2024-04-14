package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Modelos.SaveModelo

class CustomAdapterSaveUser(private val eventoList:ArrayList<SaveModelo>, private val onClickListener:(SaveModelo)->Unit): RecyclerView.Adapter<SaveUserViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaveUserViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_eventos,parent,false)
        return SaveUserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SaveUserViewHolder, position: Int) {
        val curretItem = eventoList[position]
        holder.render(curretItem,onClickListener)
    }

    override fun getItemCount(): Int {
        return eventoList.size
    }
}