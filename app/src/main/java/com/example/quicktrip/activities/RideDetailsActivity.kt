package com.example.quicktrip.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quicktrip.databinding.ActivityRideDetailsBinding
import com.google.firebase.firestore.FirebaseFirestore

class RideDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRideDetailsBinding
    private lateinit var db: FirebaseFirestore
    private var rideId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRideDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        loadUserDetails()

        rideId = intent.getStringExtra("RIDE_ID")
        val rideRoute = intent.getStringExtra("rideRoute")
        val departureTime = intent.getStringExtra("departureTime")
        val seatsBooked = intent.getIntExtra("seatsBooked", 0)

        binding.tvRideRoute.text = "Route: $rideRoute"
        binding.tvDepartureTime.text = "Departure: $departureTime"
        binding.tvSeatsBooked.text = "Seats Booked: $seatsBooked"

        binding.btnContactDriver.setOnClickListener {
            val i = Intent(this,ContactDriver::class.java)
            startActivity(i)
        }
        binding.backBtn11.setOnClickListener {
            finish()
        }

    }

    private fun loadUserDetails() {
        val sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        val name = sharedPref.getString("name", "")
        val email = sharedPref.getString("email", "")
        val phone = sharedPref.getString("phone", "")

        binding.tvPassengerName.text = "Name: $name"
        binding.tvPassengerEmail.text = "Email: $email"
        binding.tvPassengerPhone.text = "Phone: $phone"
    }
}