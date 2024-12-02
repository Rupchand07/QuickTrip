package com.example.quicktrip.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quicktrip.databinding.ActivityOfferRideDetailsBinding
import com.google.firebase.firestore.FirebaseFirestore

class OfferRideDetails : AppCompatActivity() {

    private lateinit var binding: ActivityOfferRideDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOfferRideDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pickupLocation = intent.getStringExtra("pickupLocation")
        val destination = intent.getStringExtra("destination")
        val rideDate = intent.getStringExtra("rideDate")
        val rideTime = intent.getStringExtra("rideTime")
        val pricePerSeat = intent.getStringExtra("pricePerSeat")
        val availableSeats = intent.getStringExtra("availableSeats")
        val driverName = intent.getStringExtra("driverName")
        val driverPhone = intent.getStringExtra("driverPhone")
        val driverEmail = intent.getStringExtra("driverEmail")

        binding.textViewPickupLocationValue.text = pickupLocation
        binding.textViewDestinationValue.text = destination
        binding.textViewRideDateValue.text = rideDate
        binding.textViewRideTimeValue.text = rideTime
        binding.textViewPricePerSeatValue.text = pricePerSeat
        binding.textViewAvailableSeatsValue.text = availableSeats
        binding.textViewDriverNameValue.text = driverName
        binding.textViewDriverPhoneValue.text = driverPhone
        binding.textViewDriverEmailValue.text = driverEmail

        binding.buttonConfirm.setOnClickListener {
            saveRideToFirestore(pickupLocation, destination, rideDate, rideTime, pricePerSeat, availableSeats, driverName, driverPhone)
            Toast.makeText(this, "Ride details added", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun saveRideToFirestore(pickup: String?, destination: String?, date: String?, time: String?, price: String?, seats: String?, driverName: String?, driverPhone: String?) {
        val ride = Ride(
            pickup = pickup ?: "",
            destination = destination ?: "",
            price = price ?: "",
            time = time ?: "",
            driverName = driverName ?: "",
            rating = "4.5",
            driverImageUrl = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png",
            id = "",
            availableSeats = seats ?: "",
            departureTime = "",
            driverPhoneNumber = driverPhone ?: "",
            isSearched = false,
            isOffered = true
        )

        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("rides").add(ride)
            .addOnSuccessListener {
                Toast.makeText(this, "Ride added successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to add ride", Toast.LENGTH_SHORT).show()
            }
    }
}
