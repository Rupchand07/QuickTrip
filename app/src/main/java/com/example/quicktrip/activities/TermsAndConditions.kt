package com.example.quicktrip.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quicktrip.databinding.ActivityTermsAndConditionsBinding

class TermsAndConditions : AppCompatActivity() {

    private lateinit var binding: ActivityTermsAndConditionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsAndConditionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAcceptTerms.setOnClickListener {
            Toast.makeText(this, "Terms accepted", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
