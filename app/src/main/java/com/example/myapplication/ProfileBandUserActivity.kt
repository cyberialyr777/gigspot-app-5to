package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class ProfileBandUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_band_user)

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