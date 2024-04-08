package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityLoginScreenBinding
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginScreenBinding
    private lateinit var auth: FirebaseAuth
    val TAG = "LoginActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
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
            if(checkAllField()){
                typeACount()
            }
        }
    }
    private fun typeACount(){
        val intent = Intent(this, BandMenuActivity::class.java)
        val intent2 = Intent(this, BottomNavigationActivity::class.java)
        val contexto = applicationContext
        val email = binding.email?.text.toString().trim()
        val pass = binding.passR?.text.toString()
        val databaseReference = FirebaseDatabase.getInstance().getReference("usuario")

        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(snapshot in dataSnapshot.children){
                    val usuario = snapshot.getValue(UsuariosModelos::class.java)

                    if(usuario != null){
                        if(usuario.userType == 0){
                            auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(){task ->
                                if(task.isSuccessful){
                                    finish()
                                    startActivity(intent2)
                                    Toast.makeText(contexto, "Successfully login", Toast.LENGTH_SHORT).show()
                                }else{
                                    Log.w(TAG, "singInUserWithEmail:failure", task.exception)
                                    Toast.makeText(contexto, "either email or password si wrong", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }else if(usuario.userType == 1){
                            auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(){task ->
                                if(task.isSuccessful){
                                    finish()
                                    startActivity(intent)
                                    Toast.makeText(contexto, "Successfully login", Toast.LENGTH_SHORT).show()
                                }else{
                                    Log.w(TAG, "singInUserWithEmail:failure", task.exception)
                                    Toast.makeText(contexto, "either email or password si wrong", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }else{
                        Toast.makeText(contexto, "Couldn't find the user", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "Error al consultar la base de datos")
            }
        })
    }
    private fun checkAllField(): Boolean{
        if(binding.email!!.text.toString() == ""){
            binding.email!!.error = "this field is required"
            return false
        }
        if(binding.passR!!.text.toString() == ""){
            binding.passR!!.error = "this field is required"
            return false
        }
        return true
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

