package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView

class TicketActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket)

        val button = findViewById<Button>(R.id.buttonCancel)

        // Establecer OnClickListener para el botón
        button.setOnClickListener {
            // Crear una intención para abrir la actividad TicketActivity
            val intent = Intent(this, EventsActivity::class.java)
            // Iniciar la actividad TicketActivity
            startActivity(intent)
        }

        val imagebutton = findViewById<ImageButton>(R.id.imageButton4)

        // Establecer OnClickListener para el botón
        imagebutton.setOnClickListener {
            // Crear una intención para abrir la actividad TicketActivity
            val intent = Intent(this, EventsActivity::class.java)
            // Iniciar la actividad TicketActivity
            startActivity(intent)
        }
    }

}