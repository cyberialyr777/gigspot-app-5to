package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.Modelos.UsuariosModelos
import com.example.myapplication.databinding.ActivityCheckingSessionBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CheckingSessionActivity: AppCompatActivity() {
    private lateinit var binding: ActivityCheckingSessionBinding
    private lateinit var auth: FirebaseAuth
    val TAG = "SessionActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding = ActivityCheckingSessionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        typeACount2()
    }


    private fun typeACount2(){
        val intent = Intent(this, BandMenuActivity::class.java)
        val intent2 = Intent(this, BottomNavigationActivity::class.java)
        val context = applicationContext
        val email = auth.currentUser?.email
        val databaseReference = FirebaseDatabase.getInstance().getReference("usuario")

        Log.w(TAG, "during the function")
        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot){
                Log.w(TAG, "$email")
                for(snapshot in dataSnapshot.children){
                    Log.w(TAG, "during for")
                    val usuario = snapshot.getValue(UsuariosModelos::class.java)
                    if(usuario != null){
                        Log.w(TAG, "during first if")
                        if(usuario.userType == 0){
                            Log.w(TAG, "during first second if")
                            auth.signInWithEmailAndPassword(email!!,usuario.password!!).addOnCompleteListener(){task ->
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
                            auth.signInWithEmailAndPassword(email!!,usuario.password!!).addOnCompleteListener(){task ->
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