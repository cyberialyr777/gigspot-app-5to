package com.example.myapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.Modelos.BandaModelo
import com.example.myapplication.Modelos.Event
import com.example.myapplication.databinding.ActivityEditBandBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.*

class EditBandActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    private lateinit var binding: ActivityEditBandBinding
    private lateinit var dbreferes : DatabaseReference
    val storage = FirebaseStorage.getInstance()
    private var selectedImageUri: Uri? = Uri.parse("")
    private var selectedImageUrl: String? = ""
    private val MAP_REQUEST_CODE = 1001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBandBinding.inflate(layoutInflater)
        val id = intent.getStringExtra("ID_EVENTO").toString()
        val selectedAddress = intent.getStringExtra("selectedAddress").toString()
        Log.i("address", selectedAddress)
        val view = binding.root
        setContentView(view)

        // Establecer OnClickListener para el botón
        binding.button4.setOnClickListener {
            // Crear una intención para abrir la actividad EventBandActivity
            val intent = Intent(this, EventBandActivity::class.java)
            // Iniciar la actividad EventBandActivity
            startActivity(intent)
        }

        // Agregar OnClickListener al EditText para la fecha
        binding.Fecha.setOnClickListener {
            showDatePickerDialog()
        }

        // Agregar OnClickListener al EditText para la hora
        binding.time.setOnClickListener {
            showTimePickerDialog()
        }
        binding.guardarImagen.setOnClickListener {
            pickImageGallery()
        }

        binding.button4.setOnClickListener {
            actualizar(id)
        }

        binding.cancel.setOnClickListener {
            val intent = Intent(this,EventBandActivity::class.java)
            intent.putExtra("ID_EVENTO", id)
            finish()
            startActivity(intent)
            Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show()
        }

        val mapButton: Button = binding.selectUbicacion
        mapButton.setOnClickListener {
            val intent = Intent(this, Map2Acrtivity::class.java)
            startActivityForResult(intent, MAP_REQUEST_CODE)
        }

        cosultaEvento(id, selectedAddress)
    }

    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, AddBandFragment.IMAGE_REQUEST_CODE)
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == AddBandFragment.IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
            selectedImageUri = data?.data
            binding.imagenSeleccionada?.setImageURI(selectedImageUri)
            Log.d("UriImage", "URI: "+"$selectedImageUri")
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        // Crear un DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            this,
            this, // Esta clase implementa DatePickerDialog.OnDateSetListener
            year, month, dayOfMonth
        )

        // Mostrar el DatePickerDialog
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val selectedDate = "$dayOfMonth/${month + 1}/$year"
        binding.Fecha.setText(selectedDate)
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // Crear un TimePickerDialog
        val timePickerDialog = TimePickerDialog(
            this,
            this, // Esta clase implementa TimePickerDialog.OnTimeSetListener
            hour, minute, true
        )

        // Mostrar el TimePickerDialog
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val selectedTime = "$hourOfDay:$minute"
        binding.time.setText(selectedTime)
    }

    private fun cosultaEvento(id: String, address: String) {
        Log.w("Consulta", id)
        Log.w("Consulta", "inicio")
        dbreferes = FirebaseDatabase.getInstance().getReference("eventos")
        dbreferes.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.w("Consulta", "snapshto")
                for (dataSnapshot in snapshot.children) {
                    Log.i("Consulta", "${dataSnapshot}")
                    val evento = dataSnapshot.getValue(Event::class.java)
                    Log.w("Consulta", "$evento")
                    if (evento != null) {
                        Log.w("Consulta", "if")
                        Picasso.get().load(evento.imagen).into(binding.imagenSeleccionada)
                        binding.titulo.setText(evento.titulo);
                        binding.Fecha.setText(evento.date);
                        binding.time.setText(evento.time);
                        if(address == "null"){
                            binding.spinner8.text = evento.place
                        }else if(address != "null"){
                            binding.spinner8.text = address
                        }
                        binding.precio.setText(evento.price);
                        binding.descripcion.setText(evento.description);
                        Log.w("Consulta", "${evento.titulo}")

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun actualizar(id: String) {
        val contexto = applicationContext
        val databaseReference = FirebaseDatabase.getInstance().getReference("eventos")

        // Encuentra el evento con el ID especificado.
        databaseReference.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {



                val storageRef = storage.reference.child("images").child("${UUID.randomUUID()}")
                storageRef.putFile(selectedImageUri!!).addOnSuccessListener { taskSnapshot ->
                    storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        val downloadUrl = downloadUri.toString()
                        selectedImageUrl = downloadUrl
                        Log.d("UriImage1", "URI de la imagen 1: $selectedImageUri")

                        databaseReference.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                for (snapshot in dataSnapshot.children) {
                                    val user = snapshot.getValue(Event::class.java)
                                    if (user != null && user.id == id) {
                                        val actualizacionMap = hashMapOf<String, Any?>(
                                            "imagen" to selectedImageUrl,
                                        )
                                        snapshot.ref.updateChildren(actualizacionMap)
                                            .addOnSuccessListener {

                                                Log.d("UriImage1", "URI de la imagen 1: $selectedImageUrl")
                                                for (snapshot2 in dataSnapshot.children) {
                                                    // Obtén el objeto `Event` del `DataSnapshot`.
                                                    val evento = snapshot2.getValue(Event::class.java)
                                                    val eventId = evento?.id

                                                    // Solo actualiza si el evento con el ID especificado existe.
                                                    if (eventId != null && eventId == id) {
                                                        // Crea un `HashMap` para almacenar las actualizaciones.
                                                        val actualizacionMap2 = hashMapOf<String, Any?>(
                                                            "date" to binding.Fecha.text.toString().trim(),
                                                            "time" to binding.time.text.toString().trim(),
                                                            "place" to binding.spinner8.text.toString().trim(),
                                                            "price" to binding.precio.text.toString().trim(),
                                                            "description" to binding.descripcion.text.toString().trim(),
                                                            "titulo" to binding.titulo.text.toString().trim()
                                                        )
                                                        snapshot.ref.updateChildren(actualizacionMap2)
                                                            .addOnSuccessListener {
                                                                val intent = Intent(this@EditBandActivity, EventBandActivity::class.java)
                                                                intent.putExtra("ID_EVENTO", id)
                                                                finish()
                                                                startActivity(intent)
                                                                Toast.makeText(contexto, "Successfully update", Toast.LENGTH_SHORT).show()
                                                            }
                                                            .addOnFailureListener {
                                                                Toast.makeText(contexto, "Error", Toast.LENGTH_SHORT).show()
                                                            }
                                                    }
                                                }
                                            }
                                            .addOnFailureListener {
                                                Toast.makeText(this@EditBandActivity, "Error", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Maneja el error de cancelación si es necesario.
                Toast.makeText(contexto, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy :")
    }


}
