package com.example.myapplication



import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import com.example.myapplication.databinding.FragmentAddBandBinding
import com.google.firebase.auth.FirebaseAuth
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.net.URL
import java.util.Base64
import java.util.Calendar
import java.util.UUID

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddBandFragment : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private lateinit var auth: FirebaseAuth
    private lateinit var _binding: FragmentAddBandBinding
    private lateinit var database: DatabaseReference
    val storage = FirebaseStorage.getInstance()
    private var selectedImageUri: Uri? = Uri.parse("")
    private var selectedImageUrl: String? = ""


    private val binding get() = _binding
    val TAG = "AddEventFragment"

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddBandBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding.Fecha.setOnClickListener {
            showDatePickerDialog()
        }
        binding.time.setOnClickListener {
            showTimePickerDialog()
        }
        binding.guardarImagen.setOnClickListener {
            pickImageGallery()
        }
        binding.button4.setOnClickListener {
            if (checkAllField()) {
                binding.progressBar.visibility = View.VISIBLE
                binding.blockingView.visibility = View.VISIBLE
                addEventWithImage()

            }
        }
    }




    private fun pickImageGallery() {
      val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
            selectedImageUri = data?.data
            binding.imagenSeleccionada.setImageURI(selectedImageUri)
            Log.d("UriImage", "URI: "+"$selectedImageUri")
        }
    }
/*
    private fun uploadImageToFirebaseStorage() {
            val storageRef = storage.reference.child("images").child("${UUID.randomUUID()}")
            Log.d("UriImage", "paso el val")
            storageRef.putFile(selectedImageUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        // Éxito al obtener la URL de descarga de la imagen
                        selectedImageUrl = uri.toString()
                        Log.d("UriImage", "URL: " + "$selectedImageUrl")
                    }.addOnFailureListener {
                        Log.e("FirebaseStorage", "Error getting download URL: $it")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("FirebaseStorage", "Error uploading image: $e")
                }
    }

 */

    private fun addEventWithImage() {

        val date = binding.Fecha.text.toString().trim()
        val time = binding.time.text.toString().trim()
        val place = binding.spinner8.toString()
        val price = binding.precio.text.toString().trim()
        val description = binding.descripcion.text.toString().trim()
        val titulo = binding.titulo.text.toString().trim()
        val idBand = auth.currentUser?.uid.toString()
        val emailBand = auth.currentUser?.email.toString()
        val URL = String

        val databaseReference = FirebaseDatabase.getInstance().getReference("usuario")
        databaseReference.orderByChild("email").equalTo(emailBand)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val usuario = snapshot.getValue(BandaModelo::class.java)
                        val userName = usuario?.bandName
                        if (usuario != null) {
                            val storageRef = storage.reference.child("images").child("${UUID.randomUUID()}")
                            Log.d("UriImage", "paso el val")
                            storageRef.putFile(selectedImageUri!!)
                                .addOnSuccessListener { taskSnapshot ->
                                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                                        // Éxito al obtener la URL de descarga de la imagen
                                        selectedImageUrl = uri.toString()
                                        Log.d("UriImage", "URL: " + "$selectedImageUrl")
                                        database = FirebaseDatabase.getInstance().getReference("eventos")
                                        val evento = EventModelo(
                                            date,
                                            time,
                                            place,
                                            price,
                                            description,
                                            idBand,
                                            emailBand,
                                            titulo,
                                            selectedImageUrl!!
                                        )
                                        val id = databaseReference.push().key
                                        val nuevaReferencia = database.child(id!!)
                                        nuevaReferencia.setValue(evento).addOnSuccessListener {
                                            Toast.makeText(
                                                requireContext(),
                                                "Successfully added",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                            binding.progressBar.visibility = View.GONE
                                            binding.blockingView.visibility = View.GONE
                                            startActivity(
                                                Intent(
                                                    requireContext(),
                                                    BandMenuActivity::class.java
                                                )
                                            )
                                        }.addOnFailureListener {
                                            Toast.makeText(
                                                requireContext(),
                                                "Failed while adding",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                    }.addOnFailureListener {
                                        Log.e("FirebaseStorage", "Error getting download URL: $it")
                                    }
                                }
                                .addOnFailureListener { e ->
                                    Log.e("FirebaseStorage", "Error uploading image: $e")
                                }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun checkAllField(): Boolean {
        if (binding.Fecha.text.toString() == "") {
            binding.Fecha.error = "this field is required"
            return false
        }
        if (binding.time.text.toString() == "") {
            binding.time.error = "this field is required"
            return false
        }
        if(binding.spinner8.text.toString() == ""){
            binding.spinner8.error = "this field is required"
            return false
        }
        if (binding.precio.text.toString() == "") {
            binding.precio.error = "this field is required"
            return false
        }
        if (binding.descripcion.text.toString() == "") {
            binding.descripcion.error = "this field is required"
            return false
        }
        if(selectedImageUri == Uri.parse("")){
            Toast.makeText(requireContext(), "It needs an image", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }


    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        // Crear un DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            this, // Esta clase implementa DatePickerDialog.OnDateSetListener
            year, month, dayOfMonth
        )

        // Mostrar el DatePickerDialog
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        // Mostrar la fecha seleccionada en el EditText
        val selectedDate = "$dayOfMonth/${month + 1}/$year"
        binding.Fecha.setText(selectedDate)
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // Crear un TimePickerDialog
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            this, // Esta clase implementa TimePickerDialog.OnTimeSetListener
            hour, minute, true
        )

        // Mostrar el TimePickerDialog
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        // Mostrar la hora seleccionada en el EditText
        val selectedTime = "$hourOfDay:$minute"
        binding.time.setText(selectedTime)
    }

    companion object {
        val IMAGE_REQUEST_CODE = 100

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddBandFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
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
