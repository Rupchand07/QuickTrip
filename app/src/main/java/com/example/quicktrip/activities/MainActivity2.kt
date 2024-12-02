package com.example.quicktrip.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quicktrip.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpBtn.setOnClickListener {

            startActivity(Intent(this, SignUp::class.java))
            finish()
        }
        binding.logInBtn.setOnClickListener {
            startActivity( Intent(this, LogIn::class.java))
            finish()
        }
    }
}