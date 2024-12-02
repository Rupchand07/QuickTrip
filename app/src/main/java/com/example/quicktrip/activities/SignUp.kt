package com.example.quicktrip.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quicktrip.databinding.ActivitySignUpBinding

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.crossBtn1.setOnClickListener {
            val i = Intent(this, MainActivity2::class.java)
            startActivity(i)
            finish()
        }
        binding.arrowRightBtn1.setOnClickListener {
            val i = Intent(this, SignUpMail::class.java)
            startActivity(i)
            finish()
        }
        binding.logIn.setOnClickListener {
            startActivity(Intent(this, LogIn::class.java))
        }


    }
}