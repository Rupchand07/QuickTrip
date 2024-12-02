package com.example.quicktrip.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quicktrip.activities.RecentSearchData
import com.example.quicktrip.activities.Ride
import com.example.quicktrip.activities.RidesList
import com.example.quicktrip.adapters.RecentSearchesAdapter
import com.example.quicktrip.databinding.FragmentSearchBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class Search : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var firestore: FirebaseFirestore
    private lateinit var recentSearchesAdapter: RecentSearchesAdapter
    private val recentSearchesList = mutableListOf<RecentSearchData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestore = FirebaseFirestore.getInstance()


        recentSearchesAdapter = RecentSearchesAdapter(recentSearchesList, ::deleteSearch, ::populateSearchFields)
        binding.recyclerViewRecentSearches.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recentSearchesAdapter
        }

        loadRecentSearches()

        val cities = listOf(
            "Mumbai", "Delhi", "Bengaluru", "Chennai", "Kolkata",
            "Hyderabad", "Ahmedabad", "Pune", "Jaipur", "Lucknow",
            "Kanpur", "Nagpur", "Indore", "Thane", "Bhopal",
            "Visakhapatnam", "Vadodara", "Patna", "Ludhiana", "Agra",
            "Vijayawada", "Guntur", "Nellore", "Tirupati", "Kakinada",
            "Rajahmundry", "Anantapur", "Kadapa", "Eluru", "Ongole",
            "Srikakulam", "Vizianagaram", "Chittoor", "Machilipatnam", "Amalapuram",
            "Hyderabad", "Warangal", "Nizamabad", "Khammam", "Karimnagar",
            "Nalgonda", "Mahbubnagar", "Adilabad", "Suryapet", "Siddipet",
            "Mancherial", "Jangaon", "Bhongir", "Miryalaguda", "Medak", "Mysore"
        )

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, cities)
        binding.autoCompletePickup.setAdapter(adapter)
        binding.autoCompleteDestination.setAdapter(adapter)

        binding.buttonSelectDate.setOnClickListener {
            showDatePicker()
        }

        binding.buttonSearch.setOnClickListener {
            val pickup = binding.autoCompletePickup.text.toString().trim()
            val destination = binding.autoCompleteDestination.text.toString().trim()
            val date = binding.textViewDate.text.toString().trim()
            val passengersInput = binding.editTextPassengers.text.toString().trim()

            if (pickup.isEmpty() || destination.isEmpty() || date == "Select Date" || passengersInput.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_LONG).show()
            } else {
                val passengers = passengersInput.toIntOrNull() ?: 1
                val totalPrice = calculatePrice(pickup, destination, passengers)
                val search = RecentSearchData(pickup, destination, date, passengersInput)
                saveSearchToFirestore(search)

                val ride = Ride(
                    pickup = pickup,
                    destination = destination,
                    price = totalPrice.toString(),
                    time = date,
                    driverName = "Driver Name",
                    rating = "4.5",
                    driverImageUrl = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png",
                    id = "id1",
                    availableSeats = "1",
                    departureTime = "2024-10-06T15:00:00Z",
                    driverPhoneNumber = "7661880568",
                    isSearched = false,
                    isOffered = true
                )
                saveRideToFirestore(ride)

                val intent = Intent(requireActivity(), RidesList::class.java).apply {
                    putExtra("pickup", pickup)
                    putExtra("destination", destination)
                    putExtra("date", date)
                    putExtra("passengers", passengersInput)
                    putExtra("totalPrice", totalPrice)
                }
                startActivity(intent)
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.textViewDate.text = selectedDate
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun saveSearchToFirestore(search: RecentSearchData) {
        firestore.collection("recentSearches").add(search)
            .addOnSuccessListener {
                loadRecentSearches()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to save search", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveRideToFirestore(ride: Ride) {
        firestore.collection("rides").add(ride)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Ride added successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to add ride", Toast.LENGTH_SHORT).show()
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadRecentSearches() {
        firestore.collection("recentSearches")
            .limit(2)
            .get()
            .addOnSuccessListener { result ->
                recentSearchesList.clear()
                for (document in result) {
                    val search = document.toObject(RecentSearchData::class.java)
                    Log.d("RecentSearchData", "Pickup: ${search.pickup}, Destination: ${search.destination}")
                    recentSearchesList.add(search)
                }
                recentSearchesAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load recent searches", Toast.LENGTH_SHORT).show()
            }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteSearch(search: RecentSearchData) {
        firestore.collection("recentSearches")
            .whereEqualTo("pickup", search.pickup)
            .whereEqualTo("destination", search.destination)
            .whereEqualTo("date", search.date)
            .whereEqualTo("passengers", search.passengers)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    firestore.collection("recentSearches").document(document.id).delete()
                }
                recentSearchesList.remove(search)
                recentSearchesAdapter.notifyDataSetChanged()
                Toast.makeText(requireContext(), "Search deleted successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to delete search", Toast.LENGTH_SHORT).show()
            }
    }

    private fun populateSearchFields(search: RecentSearchData) {
        binding.autoCompletePickup.setText(search.pickup)
        binding.autoCompleteDestination.setText(search.destination)
        binding.textViewDate.text = search.date
        binding.editTextPassengers.setText(search.passengers)
    }

    private fun calculatePrice(pickup: String, destination: String, passengers: Int): Int {
        val basePrice = when {
            pickup.equals("LocationA", true) && destination.equals("LocationB", true) -> 100
            pickup.equals("LocationA", true) && destination.equals("LocationC", true) -> 150
            else -> 200
        }
        return basePrice * passengers
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
