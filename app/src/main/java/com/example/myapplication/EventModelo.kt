package com.example.myapplication

import android.graphics.drawable.Drawable
import android.widget.Spinner
import java.util.SimpleTimeZone

class EventModelo(
    val date: String,
    val time: String,
    val place: String,
    val municip: String,
    val state: String,
    val price: String,
    val description: String,
    val idBand: String,
    val emailBand: String,
    val titulo: String,
    val imagen: Drawable
)
