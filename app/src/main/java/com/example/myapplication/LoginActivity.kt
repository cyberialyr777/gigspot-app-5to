package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityLoginScreenBinding
import android.util.Log
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
    lateinit var sesionesDBHelper: mySQliteHelper
    val TAG = "LoginActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        val splashScreen = installSplashScreen()
        sesionesDBHelper = mySQliteHelper(this)
        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        Thread.sleep(500)
        setContentView(binding.root)
        Log.d(TAG, "onCreate: ")

        binding.textView4.setOnClickListener {
            startActivity(Intent(this, RegisterUserBandActivity::class.java))
        }

        binding.sesiones!!.setOnClickListener {
            startActivity(Intent(this, SessionesActivity::class.java))
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
                                    select(email, pass)
                                    Toast.makeText(contexto, "Successfully login", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }else if(usuario.userType == 1){
                            auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(){task ->
                                if(task.isSuccessful){
                                    finish()
                                    startActivity(intent)
                                    select(email, pass)
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

    private fun typeACount2(email: String, context: Context){
        val intent = Intent(this, BandMenuActivity::class.java)
        val intent2 = Intent(this, BottomNavigationActivity::class.java)
        val databaseReference = FirebaseDatabase.getInstance().getReference("usuario")

        Log.w(TAG, "during the function")
        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot){
                Log.w(TAG, "$email")
                for(snapshot in dataSnapshot.children){
                    Log.w(TAG, "during for")
                    val usuario = snapshot.getValue(UsuariosModelos::class.java)
                    if(usuario != null){
                        Log.w(TAG, "during first if")
                        if(usuario.userType == 0){
                            Log.w(TAG, "during first second if")
                            auth.signInWithEmailAndPassword(email,usuario.password!!).addOnCompleteListener(){task ->
                                Log.w(TAG, "after auth")
                                if(task.isSuccessful){
                                    Log.w(TAG, "succesful")
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent2)
                                    Toast.makeText(context, "Successfully login", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }else if(usuario.userType == 1){
                            Log.w(TAG, "during first second if")
                            auth.signInWithEmailAndPassword(email,usuario.password!!).addOnCompleteListener(){task ->
                                Log.w(TAG, "after auth")
                                if(task.isSuccessful){
                                    Log.w(TAG, "succesful 2")
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                    Toast.makeText(context, "Successfully login", Toast.LENGTH_SHORT).show()
                                }else{
                                    Log.w(TAG, "singInUserWithEmail:failure", task.exception)
                                    Toast.makeText(context, "either email or password si wrong", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }else{
                        Toast.makeText(context, "Couldn't find the user", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "Error al consultar la base de datos")
            }
        })
    }

    private fun select(email: String, pass: String){
        val db : SQLiteDatabase = sesionesDBHelper.readableDatabase
        val query = "SELECT * FROM sesiones WHERE email = ?"
        val selectionArgs = arrayOf(email)

        val cursor = db.rawQuery(query, selectionArgs)
        if (cursor.count == 0) {
            sesionesDBHelper.anyadirDatos(email,pass)
        }
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
        val email = FirebaseAuth.getInstance().currentUser?.email
        super.onResume()

        FirebaseAuth.getInstance().currentUser?.apply {
            typeACount2(email!!, this@LoginActivity)
            finish()
        }

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

