package com.example.myapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentAddBandBinding
import java.util.Calendar

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddBandFragment : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentAddBandBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBandBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Agregar un click listener al EditText para la fecha
        binding.Fecha.setOnClickListener {
            showDatePickerDialog()
        }

        // Agregar un click listener al EditText para la hora
        binding.time.setOnClickListener {
            showTimePickerDialog()
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
                                        val id = databaseReference.push().key
                                        val evento = EventModelo(
                                            id!!,
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
                                        val nuevaReferencia = database.child(id)
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
        if (binding.titulo.text.toString() == "") {
            binding.titulo.error = "this field is required"
            return false
        }
        if (binding.titulo.length() > 20) {
            binding.titulo.error = "title must be less than 20 caracters"
            return false
        }
        if (binding.descripcion.text.toString() == "") {
            binding.descripcion.error = "this field is required"
            return false
        }
        if (binding.descripcion.length() > 500) {
            binding.descripcion.error = "title must be less than 100 caracters"
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
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddBandFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
