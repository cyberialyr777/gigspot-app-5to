package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class RegisterUserBandActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_user_band)

        val registerUserButton = findViewById<Button>(R.id.button2)

        registerUserButton.setOnClickListener(){
            val intent = Intent(this, RegisterUserActivity::class.java)
            startActivity(intent)
        }
    }
}
