package com.example.myapplication

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Modelos.Event
import com.example.myapplication.Modelos.FollowModelo
import com.squareup.picasso.Picasso

class CustomAdapterFollowUsers (private val followList:ArrayList<FollowModelo>, private val onClickListener:(FollowModelo)->Unit): RecyclerView.Adapter<FollowViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_follows,parent,false)
        return FollowViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FollowViewHolder, position: Int) {
        val curretItem = followList[position]
        holder.render(curretItem, onClickListener)
    }

    override fun getItemCount(): Int {
        return followList.size
    }
}