package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import com.example.myapplication.databinding.ActivityEventsBinding

class EventsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Establecer OnClickListener para el botón
        binding.button.setOnClickListener {
            // Crear una intención para abrir la actividad TicketActivity
            val intent = Intent(this, TicketActivity::class.java)
            // Iniciar la actividad TicketActivity
            startActivity(intent)
        }

        // Establecer OnClickListener para el textView
        binding.textView.setOnClickListener {
            // Crear una intención para abrir la actividad ProfileBandUserActivity
            val intent = Intent(this, ProfileBandUserActivity::class.java)
            // Iniciar la actividad ProfileBandUserActivity
            startActivity(intent)
        }

        binding.imageButton4.setOnClickListener {
            // Crear una intención para abrir la actividad ProfileBandUserActivity
            val intent = Intent(this, EventsActivity::class.java)
            // Iniciar la actividad ProfileBandUserActivity
            startActivity(intent)
        }
    }
}
