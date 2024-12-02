package com.example.quicktrip.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quicktrip.databinding.ActivityBookingConfirmationBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

class BookingConfirmation : AppCompatActivity() {
    private lateinit var binding: ActivityBookingConfirmationBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rideRoute = intent.getStringExtra("rideRoute") ?: "Unknown Route"
        val rideDateTime = intent.getStringExtra("rideDateTime") ?: "Unknown Time"
        val seatsBooked = intent.getIntExtra("seatsBooked", 0)

        binding.tvConfirmedRideRoute.text = rideRoute
        binding.tvConfirmedRideDateTime.text = rideDateTime
        binding.tvConfirmedSeatsBooked.text = "Seats Booked: $seatsBooked"

        binding.btnReturnToMain.setOnClickListener {
            val confirmationMessage = "Your booking for $rideRoute is confirmed."
            saveConfirmationMessage(confirmationMessage)
            val resultIntent = Intent().apply {
                putExtra("confirmationMessage", confirmationMessage)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun saveConfirmationMessage(message: String) {
        val firestore = FirebaseFirestore.getInstance()
        val currentTime = System.currentTimeMillis()

        val formattedTimestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(currentTime)

        val confirmationData = hashMapOf(
            "title" to "Booking Confirmation",
            "message" to message,
            "timestamp" to formattedTimestamp
        )

        firestore.collection("messages")
            .add(confirmationData)
            .addOnSuccessListener {
                println("Message saved successfully: $message")
            }
            .addOnFailureListener { e ->
                println("Failed to save message: ${e.message}")
            }
    }
}
