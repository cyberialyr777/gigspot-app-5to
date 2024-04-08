package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityEditProfileUserBinding

class EditProfileUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}