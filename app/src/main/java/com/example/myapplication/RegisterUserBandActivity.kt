package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityLoginScreenBinding
import com.example.myapplication.databinding.ActivityRegisterUserBandBinding

class RegisterUserBandActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterUserBandBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterUserBandBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button2.setOnClickListener{
            startActivity(Intent(this, RegisterUserActivity::class.java))
        }

        binding.button3.setOnClickListener{
            startActivity(Intent(this, RegisterBandActivity::class.java))
        }


        //super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_register_user_band)


       // val registerUserButton = findViewById<Button>(R.id.button2)

        //registerUserButton.setOnClickListener(){
           // val intent = Intent(this, RegisterUserActivity::class.java)
       //     startActivity(intent)
      //  }

        //val registerBandButton = findViewById<Button>(R.id.button3)

     //   registerBandButton.setOnClickListener(){
       //     val intent = Intent(this, RegisterBandActivity::class.java)
         //   startActivity(intent)
        //}
    }
}
