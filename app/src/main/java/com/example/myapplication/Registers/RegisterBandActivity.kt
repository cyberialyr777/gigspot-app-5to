package com.example.myapplication.Registers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.LoginActivity
import com.example.myapplication.Modelos.BandaModelo
import com.example.myapplication.databinding.ActivityRegisterBandBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterBandActivity: AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBandBinding
    private lateinit var database : DatabaseReference
    private lateinit var Auth: FirebaseAuth
    val TAG = "RegisterBandActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBandBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG,"onCreate: ")
        Auth = FirebaseAuth.getInstance()

        binding.Button?.setOnClickListener() {
            if (checkAllField()){
                createAcount()
            }
        }
    }

    private fun createAcount(){
        val nombreBanda = binding.bandName?.text.toString().trim()
        val email = binding.total?.text.toString().trim()
        val pass = binding.amount?.text.toString().trim()
        val userType = 1

        Auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(RegisterBandActivity()) { task ->
            if(task.isSuccessful){
                database = FirebaseDatabase.getInstance().getReference("usuario")
                val band = BandaModelo(nombreBanda, email, pass,"","","","",userType)
                database.child(nombreBanda).setValue(band).addOnSuccessListener {
                    finish()
                    Toast.makeText(this, "Successfully Register", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed while Saved", Toast.LENGTH_SHORT).show()
                }
            }else{
                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkAllField(): Boolean{
        if(binding.bandName!!.text.toString() == ""){
            binding.bandName!!.error = "this field is required"
            return false
        }
        if(binding.total!!.text.toString() == ""){
            binding.total!!.error = "this field is required"
            return false
        }
        if(binding.amount!!.text.toString() == ""){
            binding.amount!!.error = "this field is required"
            return false
        }
        if(binding.passwordConf!!.text.toString() != binding.amount!!.text.toString()){
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