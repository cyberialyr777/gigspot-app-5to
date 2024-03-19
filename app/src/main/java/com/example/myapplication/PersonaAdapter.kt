package com.example.myapplication

import android.icu.text.Transliterator.Position
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PersonaAdapter(private val data: List<PersonaModel>):RecyclerView.Adapter<PersonaAdapter.PersonaViewHolder>() {
    class PersonaViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: PersonaModel) {
            val id = view.findViewById<TextView>(R.id.tvId)
            val userName = view.findViewById<TextView>(R.id.tvUserName)
            val email = view.findViewById<TextView>(R.id.tvEmail)
            val name = view.findViewById<TextView>(R.id.tvName)
            val lastName = view.findViewById<TextView>(R.id.tvLastName)
            val age = view.findViewById<TextView>(R.id.tvAge)

            id.text = data.id.toString()
            userName.text = data.userName
            email.text = data.email
            name.text = data.name
            lastName.text = data.lastName
            age.text = data.age.toString()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonaViewHolder {
        val v = LayoutInflater.from(parent.context)
        return PersonaViewHolder(v.inflate(R.layout.item_persona,parent,false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: PersonaViewHolder, position: Int) {
        holder.bind(data[position])
    }
}
