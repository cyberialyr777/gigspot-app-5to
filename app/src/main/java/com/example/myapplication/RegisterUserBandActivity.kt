package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityLoginScreenBinding
import com.example.myapplication.databinding.ActivityRegisterUserBandBinding

class RegisterUserBandActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterUserBandBinding
    val TAG = "Register User Band Activity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterUserBandBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG,"onCreate: ")
        binding.button2.setOnClickListener{
            startActivity(Intent(this, RegisterUserActivity::class.java))
        }

        binding.button3.setOnClickListener{
            startActivity(Intent(this, RegisterBandActivity::class.java))
        }
    }


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
