package com.example.quicktrip.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quicktrip.R
import com.example.quicktrip.activities.YourRide
import com.example.quicktrip.adapters.YourRidesAdapter
import com.example.quicktrip.databinding.FragmentYourRidesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class YourRides : Fragment(R.layout.fragment_your_rides) {
    private var _binding: FragmentYourRidesBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore
    private lateinit var ridesList: MutableList<YourRide>
    private lateinit var ridesAdapter: YourRidesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYourRidesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FirebaseFirestore.getInstance()
        ridesList = mutableListOf()

        ridesAdapter = YourRidesAdapter(ridesList, requireContext()) {
            fetchYourRides()
        }
        binding.recyclerViewYourRides.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewYourRides.adapter = ridesAdapter

        fetchYourRides()
    }

    private fun fetchYourRides() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: return

        db.collection("yourRides")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                ridesList.clear()
                for (document in documents) {
                    val ride = document.toObject(YourRide::class.java)
                    ride.rideId = document.id
                    ridesList.add(ride)
                }
                ridesAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("YourRidesFragment", "Error fetching rides: ${e.message}", e)
                Toast.makeText(context, "Error loading rides.", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
