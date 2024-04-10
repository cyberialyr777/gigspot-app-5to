package com.example.myapplication

import android.content.DialogInterface.OnClickListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapterEventsBand (private val eventoList:ArrayList<Event>, private val onClickListener:(Event)->Unit): RecyclerView.Adapter<EventViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_eventos,parent,false)
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val curretItem = eventoList[position]
        holder.render(curretItem,onClickListener)
    }

    override fun getItemCount(): Int {
        return eventoList.size
    }
}