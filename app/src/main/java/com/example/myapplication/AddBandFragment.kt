package com.example.myapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import java.util.Calendar

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddBandFragment : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var editTextFecha: EditText
    private lateinit var editTextHora: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_band, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextFecha = view.findViewById(R.id.Fecha)
        editTextHora = view.findViewById(R.id.time)

        // Agregar un click listener al EditText para la fecha
        editTextFecha.setOnClickListener {
            showDatePickerDialog()
        }

        // Agregar un click listener al EditText para la hora
        editTextHora.setOnClickListener {
            showTimePickerDialog()
        }
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
        editTextFecha.setText(selectedDate)
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
        editTextHora.setText(selectedTime)
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
