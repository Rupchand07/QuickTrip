package com.example.quicktrip.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quicktrip.databinding.ActivityLogInBinding

class LogIn : AppCompatActivity() {
    private lateinit var binding: ActivityLogInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.crossBtn2.setOnClickListener {
            startActivity(Intent(this, MainActivity2::class.java))
        }
        binding.signUp.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }
        binding.arrowRightBtn2.setOnClickListener{
            startActivity(Intent(this, SignInMail::class.java))
        }
    }
}