package com.example.myapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class EditBandActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_band)

        val button = findViewById<Button>(R.id.button5)
        val editTextFecha = findViewById<EditText>(R.id.Fecha)
        val editTextHora = findViewById<EditText>(R.id.Hora)

        // Establecer OnClickListener para el botón
        button.setOnClickListener {
            // Crear una intención para abrir la actividad EventBandActivity
            val intent = Intent(this, EventBandActivity::class.java)
            // Iniciar la actividad EventBandActivity
            startActivity(intent)
        }

        // Agregar OnClickListener al EditText para la fecha
        editTextFecha.setOnClickListener {
            showDatePickerDialog()
        }

        // Agregar OnClickListener al EditText para la hora
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
            this,
            this, // Esta clase implementa DatePickerDialog.OnDateSetListener
            year, month, dayOfMonth
        )

        // Mostrar el DatePickerDialog
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val selectedDate = "$dayOfMonth/${month + 1}/$year"
        findViewById<EditText>(R.id.Fecha).setText(selectedDate)
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
        findViewById<EditText>(R.id.Hora).setText(selectedTime)
    }
}
