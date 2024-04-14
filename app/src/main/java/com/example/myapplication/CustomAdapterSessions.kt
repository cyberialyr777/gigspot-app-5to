package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Modelos.UsuariosModelos
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CustomAdapterSessions(var sesionList: ArrayList<SessionsModelo>): RecyclerView.Adapter<CustomAdapterSessions.MyViewHolderSession>(){
    lateinit var sesionesDBHelper: mySQliteHelper
    private lateinit var auth: FirebaseAuth
    val TAG = "AdapterSesiones"

    class MyViewHolderSession(itemView: View): RecyclerView.ViewHolder(itemView){
        val email: TextView = itemView.findViewById(R.id.sesionEmail)
        val delete: Button = itemView.findViewById(R.id.deleteSesion)
        val login: Button = itemView.findViewById(R.id.loginSesion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderSession {
        auth = FirebaseAuth.getInstance()
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_sesiones_view, parent,false)
        return MyViewHolderSession(itemView)
    }

    override fun getItemCount(): Int {
        return sesionList.size
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyViewHolderSession, position: Int) {
        val currentItem = sesionList[position]
        holder.email.text = "Email: " + currentItem.email
        holder.delete.setOnClickListener {
            sesionList.removeAt(position)
            notifyDataSetChanged()
            val sesionesDBHelper = mySQliteHelper(holder.itemView.context)
            val exito = sesionesDBHelper.deleteSesion(currentItem._id)
            if (exito) {
                Toast.makeText(holder.itemView.context, "Session eliminated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(holder.itemView.context, "Error while eliminating the session", Toast.LENGTH_SHORT).show()
            }
        }
        holder.login.setOnClickListener {
            val contexto = holder.itemView.context
            Log.w(TAG, "before the function")
            typeACount(currentItem.email,currentItem.pass, contexto)
        }
    }

    private fun requireContext(): Context {
        TODO("Not yet implemented")
    }

    private fun typeACount(email: String, pass: String, context: Context){

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
                            auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(){task ->
                                Log.w(TAG, "after auth")
                                if(task.isSuccessful){
                                    Log.w(TAG, "succesful")
                                    val intent = Intent(context, BottomNavigationActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    context.startActivity(intent)
                                    Toast.makeText(context, "Successfully login", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }else if(usuario.userType == 1){
                            Log.w(TAG, "during first second if")
                            auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(){task ->
                                Log.w(TAG, "after auth")
                                if(task.isSuccessful){
                                    Log.w(TAG, "succesful 2")
                                    val intent = Intent(context, BandMenuActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    context.startActivity(intent)
                                    Toast.makeText(context, "Successfully login", Toast.LENGTH_SHORT).show()
                                }else{
                                    Log.w(TAG, "singInUserWithEmail:failure", task.exception)
                                    Toast.makeText(null, "either email or password si wrong", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }else{
                        Toast.makeText(null, "Couldn't find the user", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "Error al consultar la base de datos")
            }
        })
    }
}