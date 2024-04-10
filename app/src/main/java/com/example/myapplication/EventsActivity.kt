package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityEventsBinding
import android.widget.Button
import android.widget.TextView



class EventsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEventsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Encontrar la referencia al botón

        val BuyTicket = binding.button

        // Establecer OnClickListener para el botón
        BuyTicket.setOnClickListener {
            // Iniciar la actividad TicketActivity
            startActivity(Intent(this, TicketActivity::class.java))
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