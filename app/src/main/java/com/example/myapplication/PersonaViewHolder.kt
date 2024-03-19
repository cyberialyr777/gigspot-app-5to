package com.example.myapplication

import androidx.recyclerview.widget.RecyclerView

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val textView: TextView

        init {
            textView = view.findViewById(R.id.textView)
        }
    }
}