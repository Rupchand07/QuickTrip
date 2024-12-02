package com.example.quicktrip.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quicktrip.databinding.ActivityPaymentCompletedBinding
import com.example.quicktrip.fragments.Search
import com.example.quicktrip.fragments.YourRides

class PaymentCompleted : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentCompletedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentCompletedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rideId = intent.getStringExtra("rideId")
        binding.tvConfirmationMessage.text = "Your booking for Ride ID: $rideId is confirmed!"

        binding.btnBackToHome.setOnClickListener {
            finish()
        }
    }
}
