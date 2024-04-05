package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.myapplication.databinding.ActivityRegisterBandBinding
import com.example.myapplication.databinding.ActivityRegisterUserBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterBandActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBandBinding
    private lateinit var database : DatabaseReference
    val TAG = "RegisterBandActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBandBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG,"onCreate: ")

        binding.sendButton!!.setOnClickListener(){
            val nombreBanda = binding.bandName.toString()
            val email = binding.email.toString()
            val pass = binding.password.toString()

            if(checkAllField()){
                database = FirebaseDatabase.getInstance().getReference("usuario")
                val band = BandaModelo(nombreBanda, email, pass)
                database.child(nombreBanda).setValue(band).addOnSuccessListener {
                    Toast.makeText(this, "Successfuly Saved", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed while Saved", Toast.LENGTH_SHORT).show()
                }
            }
        }


        binding.password!!.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding.passwordConf!!.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
    }
    private fun checkAllField(): Boolean{
        if(binding.bandName!!.text.toString() == ""){
            binding.bandName!!.error = "this field is required"
            return false
        }
        if(binding.email!!.text.toString() == ""){
            binding.email!!.error = "this field is required"
            return false
        }
        if(binding.password!!.text.toString() == ""){
            binding.password!!.error = "this field is required"
            return false
        }
        if(binding.passwordConf!!.text.toString() != binding.password!!.text.toString()){
            binding.passwordConf!!.error = "password do not match"
            return false
        }
        return true
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