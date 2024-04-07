package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class EditBandActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_band)

        val button = findViewById<Button>(R.id.button5)

        // Establecer OnClickListener para el botón
        button.setOnClickListener {
            // Crear una intención para abrir la actividad TicketActivity
            val intent = Intent(this, EventBandActivity::class.java)
            // Iniciar la actividad TicketActivity
            startActivity(intent)
        }
    }
}