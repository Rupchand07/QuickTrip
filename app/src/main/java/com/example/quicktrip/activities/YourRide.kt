package com.example.quicktrip.activities

data class YourRide(
    val userId: String = "",
    var rideId: String = "",
    val seatsBooked: Int = 0,
    val rideRoute: String = "",
    val rideDateTime: String = "",
    val paymentId: String = "",
    val timestamp: Any? = null,
    val passengerName: String = "",
    val passengerEmail: String = "",
    val passengerPhone: String = ""
)

