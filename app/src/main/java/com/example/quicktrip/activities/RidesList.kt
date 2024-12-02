package com.example.quicktrip.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quicktrip.adapters.RidesAdapter
import com.example.quicktrip.databinding.ActivityRidesListBinding
import com.google.firebase.firestore.FirebaseFirestore

class RidesList : AppCompatActivity() {

    private lateinit var binding: ActivityRidesListBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var ridesAdapter: RidesAdapter
    private val ridesList = mutableListOf<Ride>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRidesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        ridesAdapter = RidesAdapter(ridesList) { ride ->
            val intent = Intent(this, RideDetail::class.java).apply {
                putExtra("rideId", ride.id)
                putExtra("availableSeats", ride.availableSeats)
            }
            startActivity(intent)
        }

        binding.recyclerViewRides.apply {
            layoutManager = LinearLayoutManager(this@RidesList)
            adapter = ridesAdapter
        }

        val pickup = intent.getStringExtra("pickup") ?: ""
        val destination = intent.getStringExtra("destination") ?: ""

        loadRides(pickup, destination)
    }

    private fun loadRides(pickup: String, destination: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerViewRides.visibility = View.GONE

        firestore.collection("rides")
            .whereEqualTo("pickup", pickup)
            .whereEqualTo("destination", destination)
            .get()
            .addOnSuccessListener { documents ->
                ridesList.clear()
                for (document in documents) {
                    val ride = document.toObject(Ride::class.java).copy(id = document.id)
                    ridesList.add(ride)
                }
                ridesAdapter.notifyDataSetChanged()
                binding.progressBar.visibility = View.GONE
                binding.recyclerViewRides.visibility = View.VISIBLE
            }
            .addOnFailureListener { exception ->
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Failed to load rides: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
