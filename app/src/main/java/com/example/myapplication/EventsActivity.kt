package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.widget.TextView


class EventsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)

        // Encontrar la referencia al botón
        val button = findViewById<Button>(R.id.button)

        // Establecer OnClickListener para el botón
        button.setOnClickListener {
            // Crear una intención para abrir la actividad TicketActivity
            val intent = Intent(this, TicketActivity::class.java)
            // Iniciar la actividad TicketActivity
            startActivity(intent)
        }

        val textview = findViewById<TextView>(R.id.textView)

        // Establecer OnClickListener para el botón
        textview.setOnClickListener {
            // Crear una intención para abrir la actividad TicketActivity
            val intent = Intent(this, ProfileBandUserActivity::class.java)
            // Iniciar la actividad TicketActivity
            startActivity(intent)
        }
    }
}