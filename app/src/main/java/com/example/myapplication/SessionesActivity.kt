package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivitySesionesConsultaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SessionesActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySesionesConsultaBinding
    lateinit var sesionesDBHelper: mySQliteHelper
    private lateinit var auth: FirebaseAuth


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySesionesConsultaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sesionesDBHelper = mySQliteHelper(this)
        auth = FirebaseAuth.getInstance()
        binding.datos.text = ""

        select()
    }

    private fun select(){
        val db : SQLiteDatabase = sesionesDBHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM sesiones", null)

        if(cursor.moveToFirst()){
            do {
                val datoTextView = TextView(this)
                datoTextView.text = "${cursor.getInt(0)}: ${cursor.getString(1)}: ${cursor.getString(2)}"

                val botonEliminar = Button(this)
                botonEliminar.text = "Eliminar"
                botonEliminar.setOnClickListener {
                    // Acción al hacer clic en el botón "Eliminar"
                    Toast.makeText(this, "Eliminado", Toast.LENGTH_SHORT).show()
                    sesionesDBHelper.deleteRecordById(cursor.getInt(0))
                    recreate()
                }

                val botonIniciarSesion = Button(this)
                botonIniciarSesion.text = "Iniciar Sesión"
                botonIniciarSesion.setOnClickListener {
                    typeACount(cursor.getString(1).toString(), cursor.getString(2).toString())
                    Toast.makeText(this, "Iniciando sesión", Toast.LENGTH_SHORT).show()
                }

                val linearLayout = binding.linearlayout
                linearLayout.addView(datoTextView)
                linearLayout.addView(botonEliminar)
                linearLayout.addView(botonIniciarSesion)

            }while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
    }
    private fun typeACount(email: String,pass: String){
        val intent = Intent(this, BandMenuActivity::class.java)
        val intent2 = Intent(this, BottomNavigationActivity::class.java)
        val contexto = applicationContext
        val databaseReference = FirebaseDatabase.getInstance().getReference("usuario")

        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
            ValueEventListener {
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
                                    Toast.makeText(contexto, "either email or password si wrong", Toast.LENGTH_SHORT).show()
                                    sesionesDBHelper.anyadirDatos(email,pass)
                                }
                            }
                        }else if(usuario.userType == 1){
                            auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(){task ->
                                if(task.isSuccessful){
                                    finish()
                                    startActivity(intent)
                                    Toast.makeText(contexto, "Successfully login", Toast.LENGTH_SHORT).show()
                                    sesionesDBHelper.anyadirDatos(email,pass)
                                }else{
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

}