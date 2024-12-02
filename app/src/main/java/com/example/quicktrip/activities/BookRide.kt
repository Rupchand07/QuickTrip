package com.example.quicktrip.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quicktrip.databinding.ActivityBookRideBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

class BookRide : AppCompatActivity(), PaymentResultListener {
    private lateinit var binding: ActivityBookRideBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var rideId: String
    private lateinit var userName: String
    private lateinit var userEmail: String
    private lateinit var userPhone: String
    private lateinit var rideRoute: String
    private lateinit var rideDateTime: String
    private var availableSeats: Int = 0
    private var pricePerSeat: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookRideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        rideId = intent.getStringExtra("rideId") ?: ""
        rideRoute = intent.getStringExtra("DEPARTURE_DETAILS") ?: "N/A"
        rideDateTime = intent.getStringExtra("DEPARTURE_DATE_TIME") ?: "N/A"
        availableSeats = intent.getIntExtra("AVAILABLE_SEATS", 0)
        pricePerSeat = intent.getStringExtra("PRICE")?.toIntOrNull() ?: 0

        binding.tvRideDetails.text = rideRoute
        binding.tvRideDateTime.text = "Departure: $rideDateTime"
        binding.tvAvailableSeats.text = "Seats Available: $availableSeats"


        loadUserDetails()


        setupSeatSpinner()

        binding.backBtnBookRide.setOnClickListener {
            finish()
        }

        binding.btnProceedToPayment.setOnClickListener {
            val selectedSeats = binding.spinnerSeats.selectedItem.toString().toInt()
            if (selectedSeats <= availableSeats) {
                confirmBooking(selectedSeats)
                startPayment(selectedSeats)
            } else {
                Toast.makeText(this, "Not enough seats available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadUserDetails() {
        val sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        userName = sharedPref.getString("name", "") ?: "N/A"
        userEmail = sharedPref.getString("email", "") ?: "N/A"
        userPhone = sharedPref.getString("phone", "") ?: "N/A"

        binding.tvPassengerName.text = "Name: $userName"
        binding.tvPassengerEmail.text = "Email: $userEmail"
        binding.tvPassengerPhone.text = "Phone: $userPhone"
    }

    private fun setupSeatSpinner() {
        val seatOptions = (1..availableSeats).toList()
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, seatOptions)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSeats.adapter = spinnerAdapter

        binding.spinnerSeats.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedSeats = seatOptions[position]
                binding.tvTotalAmount.text = "Total Amount: Rs.${selectedSeats * pricePerSeat}"
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun confirmBooking(selectedSeats: Int) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: return

        if (rideId.isNotEmpty()) {
            val rideRef = db.collection("rides").document(rideId)

            db.runTransaction { transaction ->
                val rideSnapshot = transaction.get(rideRef)
                val availableSeats = rideSnapshot.getLong("availableSeats")?.toInt() ?: 0

                if (selectedSeats <= availableSeats) {
                    val newAvailableSeats = availableSeats - selectedSeats
                    transaction.update(rideRef, "availableSeats", newAvailableSeats)

                    val bookingData = hashMapOf(
                        "userId" to userId,
                        "rideId" to rideId,
                        "seatsBooked" to selectedSeats,
                        "passengerName" to userName,
                        "passengerEmail" to userEmail,
                        "passengerPhone" to userPhone,
                        "timestamp" to FieldValue.serverTimestamp()
                    )
                    db.collection("bookings").add(bookingData)
                } else {
                    throw Exception("Not enough seats available")
                }
            }.addOnSuccessListener {
                Toast.makeText(this, "Booking confirmed", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { e ->
                Toast.makeText(this, e.message ?: "Error occurred", Toast.LENGTH_SHORT).show()
                Log.e("BookRide", "Transaction failed", e)
            }
        }
    }

    private fun startPayment(selectedSeats: Int) {
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_wVeMnsfkvm2sOO")

        try {
            val options = JSONObject()
            options.put("name", "QuickTrip Ride Booking")
            options.put("description", "Booking ride seats")
            options.put("currency", "INR")
            val totalAmount = selectedSeats * pricePerSeat * 100
            options.put("amount", totalAmount)
            options.put("prefill.email", userEmail)
            options.put("prefill.contact", userPhone)
            checkout.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(this, "Error in payment: ${e.message}", Toast.LENGTH_LONG).show()
            Log.e("BookRide", "Payment error: ${e.message}", e)
        }
    }

    override fun onPaymentSuccess(razorpayPaymentID: String?) {
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show()
        Log.d("BookRide", "Payment successful with ID: $razorpayPaymentID")

        binding.btnProceedToPayment.isEnabled = false

        val seatsBooked = binding.spinnerSeats.selectedItem.toString().toInt()

        generateBookingTicket(seatsBooked)

        val intent = Intent(this, BookingConfirmation::class.java).apply {
            putExtra("rideRoute", rideRoute)
            putExtra("rideDateTime", rideDateTime)
            putExtra("seatsBooked", seatsBooked)
        }
        startActivity(intent)
    }

    override fun onPaymentError(code: Int, response: String?) {
        Toast.makeText(this, "Payment Failed: $response", Toast.LENGTH_SHORT).show()
        Log.e("BookRide", "Payment failed: Code $code, Response: $response")
    }

    private fun generateBookingTicket(seatsBooked: Int) {
        val pdfDocument = PdfDocument()
        val paint = Paint()

        val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        val canvas = page.canvas
        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 14f
        paint.color = Color.BLACK

        canvas.drawText("Booking Ticket", 80f, 50f, paint)
        canvas.drawText("Ride Route: $rideRoute", 20f, 100f, paint)
        canvas.drawText("Departure: $rideDateTime", 20f, 140f, paint)
        canvas.drawText("Seats Booked: $seatsBooked", 20f, 180f, paint)

        pdfDocument.finishPage(page)

        val filePath = File(getExternalFilesDir(null), "booking_ticket.pdf")
        pdfDocument.writeTo(FileOutputStream(filePath))
        pdfDocument.close()

        Toast.makeText(this, "Ticket downloaded at ${filePath.absolutePath}", Toast.LENGTH_LONG)
            .show()
    }
}
