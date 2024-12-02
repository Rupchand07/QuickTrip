package com.example.quicktrip.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quicktrip.databinding.ActivityLicensesBinding

class Licenses : AppCompatActivity() {
    private lateinit var binding: ActivityLicensesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLicensesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}