package com.example.quicktrip.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quicktrip.databinding.ActivityContactDriverBinding

class ContactDriver : AppCompatActivity() {
    private lateinit var binding: ActivityContactDriverBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactDriverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtnContact.setOnClickListener {
            finish()
        }

        val driverPhoneNumber = intent.getStringExtra("DRIVER_PHONE") ?: ""

        binding.btnCallDriver.setOnClickListener {
            callDriver(driverPhoneNumber)
        }

        binding.btnMessageDriver.setOnClickListener {
            messageDriver(driverPhoneNumber)
        }
    }
    private fun callDriver(phoneNumber: String) {
        if (phoneNumber.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }
            startActivity(intent)
        } else {
            Toast.makeText(this, "No phone number available", Toast.LENGTH_SHORT).show()
        }
    }
    private fun messageDriver(phoneNumber: String) {
        if (phoneNumber.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("sms:$phoneNumber")
            }
            startActivity(intent)
        } else {
            Toast.makeText(this, "No phone number available", Toast.LENGTH_SHORT).show()
        }
    }
}