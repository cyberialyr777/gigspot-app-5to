package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityLoginScreenBinding
import android.util.Log
import android.widget.EditText
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginScreenBinding
    val TAG = "LoginActivity"
    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        Thread.sleep(500)
        setContentView(binding.root)
        Log.d(TAG, "onCreate: ")

        binding.textView4.setOnClickListener {
            startActivity(Intent(this, RegisterUserBandActivity::class.java))
        }

        binding.button.setOnClickListener {
            startActivity(Intent(this, BottomNavigationActivity::class.java))
        }

        binding.Prueba1?.setOnClickListener {
            startActivity(Intent(this, BandMenuActivity::class.java))
        }

        val pasword = findViewById<EditText>(R.id.pasword)

        pasword.inputType =
            android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    //LifeCycle

    override fun onStart() {
        super.onStart()
        Log.d(TAG,"onStart: ")
    }

    override fun onResume(){
        super.onResume()
        Log.d(TAG,"onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG,"onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG,"onStop: ")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG,"onRestart: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy :")
    }
}

