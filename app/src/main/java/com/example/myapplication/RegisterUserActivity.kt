package com.example.myapplication

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class RegisterUserActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)
        val password = findViewById<EditText>(R.id.apellido4)

        password.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD

        val passwordapellido5 = findViewById<EditText>(R.id.apellido5)

        passwordapellido5.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
    }
}