package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityLoginScreenBinding
import com.example.myapplication.databinding.ActivityMainBinding
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.widget.EditText
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import kotlin.concurrent.thread

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        Thread.sleep(500)
        setContentView(binding.root)
        //splashScreen.setKeepOnScreenCondition{true}
        //startActivity(Intent(this, LoginActivity::class.java))
        //finish()

        binding.textView4.setOnClickListener {
            startActivity(Intent(this, RegisterUserBandActivity::class.java))
        }

        binding.button.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }



        val pasword = findViewById<EditText>(R.id.pasword)

        pasword.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        //val spannableString = SpannableString("Sign in")
        //val clickableSpan = object : ClickableSpan() {
        //  override fun onClick(widget: android.view.View) {
        // Handle the click event here
        //    goToSecondActivity()
        //}
        //}

        // spannableString.setSpan(clickableSpan, 6, 28, 0)

        // Set the SpannableString to the TextView using ViewBinding
        //binding.textView4.text = spannableString
        //binding.textView4.movementMethod = LinkMovementMethod.getInstance()

        // Set the click listener using ViewBinding
        //binding.textView4.setOnClickListener {
        //  goToSecondActivity()
        //}
        //}

        //private fun goToSecondActivity() {
        //  val intent = Intent(this, RegisterUserBandActivity::class.java)
        //startActivity(intent)
        //}
    }
}

