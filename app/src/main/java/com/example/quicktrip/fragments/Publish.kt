package com.example.quicktrip.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.quicktrip.activities.AddLicense
import com.example.quicktrip.activities.AddVehicle
import com.example.quicktrip.activities.AddressProof
import com.example.quicktrip.activities.OfferRideDetails
import com.example.quicktrip.databinding.FragmentPublishBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class Publish : Fragment() {

    private var _binding: FragmentPublishBinding? = null
    private val binding get() = _binding!!

    private val db = FirebaseFirestore.getInstance()

    private val availableSeatsOptions = arrayOf("1", "2", "3", "4", "5")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPublishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAvailableSeatsSpinner()

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
        binding.pickupLocation.setAdapter(adapter)
        binding.destination.setAdapter(adapter)

        binding.submitOfferButton.setOnClickListener {
            offerRide()
        }
        binding.rideDate.setOnClickListener {
            showDatePicker()
        }
        binding.rideTime.setOnClickListener {
            showTimePickerDialog()
        }

        binding.vehicleDetailsButton.setOnClickListener {
            val i = Intent(requireContext(),AddVehicle::class.java)
            startActivity(i)
        }
        binding.drivingLicenseButton.setOnClickListener {
            val i = Intent(requireContext(),AddLicense::class.java)
            startActivity(i)
        }
        binding.addressProofButton.setOnClickListener {
            val i = Intent(requireContext(),AddressProof::class.java)
            startActivity(i)
        }

    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            binding.rideTime.setText(formattedTime)
        }, hour, minute, true)
        timePickerDialog.show()
    }

    private fun setupAvailableSeatsSpinner() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, availableSeatsOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.availableSeats.adapter = adapter
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
                binding.rideDate.text = selectedDate
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun offerRide() {
        val pickupLocation = binding.pickupLocation.text.toString().trim()
        val destination = binding.destination.text.toString().trim()
        val rideDate = binding.rideDate.text.toString().trim()
        val rideTime = binding.rideTime.text.toString().trim()
        val pricePerSeat = binding.pricePerSeat.text.toString().trim()
        val availableSeats = binding.availableSeats.selectedItem.toString()
        val driverName = binding.driverName.text.toString().trim()
        val driverPhone = binding.driverPhone.text.toString().trim()
        val driverEmail = binding.driverEmail.text.toString().trim()

        if (validateInputs(pickupLocation, destination, rideDate, rideTime, pricePerSeat, availableSeats, driverName, driverPhone, driverEmail)) {
            val rideOffer = hashMapOf(
                "pickupLocation" to pickupLocation,
                "destination" to destination,
                "rideDate" to rideDate,
                "rideTime" to rideTime,
                "pricePerSeat" to pricePerSeat,
                "availableSeats" to availableSeats,
                "driverName" to driverName,
                "driverPhone" to driverPhone,
                "driverEmail" to driverEmail
            )

            db.collection("rides")
                .add(rideOffer)
                .addOnSuccessListener { documentReference ->
                    val intent = Intent(requireContext(), OfferRideDetails::class.java)
                    intent.putExtra("pickupLocation", pickupLocation)
                    intent.putExtra("destination", destination)
                    intent.putExtra("rideDate", rideDate)
                    intent.putExtra("rideTime", rideTime)
                    intent.putExtra("pricePerSeat", pricePerSeat)
                    intent.putExtra("availableSeats", availableSeats)
                    intent.putExtra("driverName", driverName)
                    intent.putExtra("driverPhone", driverPhone)
                    intent.putExtra("driverEmail", driverEmail)

                    startActivity(intent)

                    Toast.makeText(requireContext(), "Ride offered successfully!", Toast.LENGTH_SHORT).show()
                    clearInputs()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to offer ride: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun validateInputs(
        pickupLocation: String,
        destination: String,
        rideDate: String,
        rideTime: String,
        pricePerSeat: String,
        availableSeats: String,
        driverName: String,
        driverPhone: String,
        driverEmail: String
    ): Boolean {
        if (pickupLocation.isEmpty() || destination.isEmpty() || rideDate.isEmpty() || rideTime.isEmpty() ||
            pricePerSeat.isEmpty() || availableSeats.isEmpty() || driverName.isEmpty() || driverPhone.isEmpty() || driverEmail.isEmpty()
        ) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun clearInputs() {
        binding.pickupLocation.text.clear()
        binding.destination.text.clear()
        binding.rideDate.text = "Select date"
        binding.rideTime.text.clear()
        binding.pricePerSeat.text.clear()
        binding.availableSeats.setSelection(0)
        binding.driverName.text.clear()
        binding.driverPhone.text.clear()
        binding.driverEmail.text.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
