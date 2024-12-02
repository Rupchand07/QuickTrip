package com.example.quicktrip.activities

data class Ride(
        val id: String = "",
        val pickup: String = "",
        val destination: String = "",
        val price: String = "",
        val time: String = "",
        val driverName: String = "",
        val rating: String = "",
        val driverImageUrl: String = "",
        val availableSeats: String = "",
        val departureTime : String = "",
        val driverPhoneNumber: String = "",
        val isOffered: Boolean = false,
        val isSearched: Boolean = false
)
