package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityRegisterUserBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterUserActivity : AppCompatActivity(){
    private lateinit var binding: ActivityRegisterUserBinding
    private lateinit var database : DatabaseReference
    private lateinit var Auth: FirebaseAuth
    val TAG = "RegisterUserActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG,"onCreate: ")
        Auth = FirebaseAuth.getInstance()

        binding.button4.setOnClickListener(){
            if(checkAllField()) {
                crateAcount()
            }
        }
    }

    private fun crateAcount(){
        val email = binding.email?.text.toString().trim()
        val pass = binding.password?.text.toString().trim()
        val nombre = binding.firstName?.text.toString().trim()
        val apellido = binding.userlastName?.text.toString().trim()
        val userName = binding.userName?.text.toString().trim()

        Auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(RegisterUserActivity()) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Successfully Register", Toast.LENGTH_SHORT).show()
                database = FirebaseDatabase.getInstance().getReference("usuario")
                val user = UsuariosModelos(userName, nombre, apellido, pass, email)
                database.child(userName).setValue(user).addOnSuccessListener {
                    finish()
                    startActivity(Intent(this, LoginActivity::class.java))
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed while Saved", Toast.LENGTH_SHORT).show()
                }
            }else{
                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                Toast.makeText(this, "Failed while Saved with Auth", Toast.LENGTH_SHORT).show()
                updateUI(null)
            }
        }
    }
    private fun updateUI(usuario: FirebaseUser?){
    }

    private fun checkAllField(): Boolean{
        val e_mail = binding.email!!.text.toString()
        if(binding.firstName!!.text.toString() == ""){
            binding.firstName!!.error = "this field is required"
            return false
        }
        if(binding.userlastName!!.text.toString() == ""){
            binding.userlastName!!.error = "this field is required"
            return false
        }
        if(binding.userName!!.text.toString() == ""){
            binding.userName!!.error = "this field is required"
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