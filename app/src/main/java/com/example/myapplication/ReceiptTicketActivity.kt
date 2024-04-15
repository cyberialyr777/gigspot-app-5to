package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityReceiptTicketBinding

class ReceiptTicketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReceiptTicketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceiptTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
