package com.example.quicktrip.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.quicktrip.R
import com.example.quicktrip.databinding.ActivityRideDetailBinding
import com.google.firebase.firestore.FirebaseFirestore

class RideDetail : AppCompatActivity() {

    private lateinit var binding: ActivityRideDetailBinding
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRideDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        val rideId = intent.getStringExtra("rideId") ?: return

        loadRideDetails(rideId)

        binding.backBtn3.setOnClickListener {
            finish()
        }
    }

    private fun loadRideDetails(rideId: String) {
        firestore.collection("rides").document(rideId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val ride = document.toObject(Ride::class.java)
                    if (ride != null) {
                        binding.tvPickup.text = "Pickup:  ${ride.pickup}"
                        binding.tvDestination.text = "Destination:  ${ride.destination}"
                        binding.tvPrice.text = "Price:  ₹${ride.price}"
                        binding.tvDepartureTime.text = "Time: ${ride.time}"
                        binding.tvDriverName.text = ride.driverName
                        binding.tvSeatsAvailable.text = "Seats: ${ride.availableSeats}"
                        binding.tvDriverRating.text = ride.rating?.split("*")?.get(0) ?: "No rating"

                        Glide.with(this)
                            .load(ride.driverImageUrl)
                            .error(R.drawable.baseline_person_24)
                            .into(binding.ivDriverImage)

                        val driverPhoneNumber = ride.driverPhoneNumber

                        binding.btnBook.setOnClickListener {

                            val bookIntent = Intent(this, BookRide::class.java).apply {
                                putExtra("rideId", rideId)
                                putExtra(
                                    "DEPARTURE_DETAILS",
                                    "${ride.pickup} ➝ ${ride.destination}"
                                )
                                putExtra("DEPARTURE_DATE_TIME", ride.time)
                                putExtra("AVAILABLE_SEATS", ride.availableSeats.toInt())
                                putExtra("PRICE", ride.price)
                                putExtra("RATING",ride.rating)
                            }
                            startActivity(bookIntent)
                        }
                        binding.btnContactDriver.setOnClickListener {
                            if (driverPhoneNumber != null) {
                                val contactIntent = Intent(this, ContactDriver::class.java).apply {
                                    putExtra("DRIVER_PHONE", driverPhoneNumber)
                                }
                                startActivity(contactIntent)
                            } else {
                                Toast.makeText(
                                    this,
                                    "Driver's phone number not available",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "Ride not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Failed to load ride details: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}