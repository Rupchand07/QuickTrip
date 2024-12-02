package com.example.quicktrip.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.quicktrip.activities.RideDetailsActivity
import com.example.quicktrip.activities.YourRide
import com.example.quicktrip.databinding.ItemYourRideBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

class YourRidesAdapter(
    private val ridesList: MutableList<YourRide>,
    private val context: Context,
    private val refreshList: () -> Unit
) : RecyclerView.Adapter<YourRidesAdapter.RideViewHolder>() {

    inner class RideViewHolder(private val binding: ItemYourRideBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ride: YourRide) {
            binding.tvRideRoute.text = ride.rideRoute
            binding.tvDepartureTime.text = "Departure: ${ride.rideDateTime}"
            binding.tvSeatsBooked.text = "Seats Booked: ${ride.seatsBooked}"

            binding.btnCancelRide.setOnClickListener {
                removeRide(ride.rideId)
            }


            binding.root.setOnClickListener {
                val intent = Intent(context, RideDetailsActivity::class.java).apply {
                    putExtra("RIDE_ID", ride.rideId)
                    putExtra("rideRoute", ride.rideRoute)
                    putExtra("departureTime", ride.rideDateTime)
                    putExtra("seatsBooked", ride.seatsBooked)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RideViewHolder {
        val binding = ItemYourRideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RideViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RideViewHolder, position: Int) {
        holder.bind(ridesList[position])
    }

    override fun getItemCount(): Int = ridesList.size

    private fun removeRide(rideId: String) {
        val index = ridesList.indexOfFirst { it.rideId == rideId }

        if (index != -1) {
            val db = FirebaseFirestore.getInstance()
            val rideRoute = ridesList[index].rideRoute
            val userEmail = FirebaseAuth.getInstance().currentUser?.email

            db.collection("yourRides").document(rideId).delete()
                .addOnSuccessListener {
                    ridesList.removeAt(index)
                    notifyItemRemoved(index)
                    Toast.makeText(context, "Ride canceled successfully", Toast.LENGTH_SHORT).show()

                    logCancellationMessage(rideRoute)

                    userEmail?.let { email -> sendCancellationEmail(email, rideRoute) }

                    refreshList()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error canceling ride: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun logCancellationMessage(rideRoute: String) {
        val db = FirebaseFirestore.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formattedTimestamp = dateFormat.format(System.currentTimeMillis())
        val message = mapOf(
            "title" to "Cancellation Notice",
            "message" to "Your ride with route $rideRoute has been canceled.",
            "timestamp" to formattedTimestamp
        )

        db.collection("messages").add(message)
            .addOnSuccessListener {
                Log.d("YourRidesAdapter", "Message added to inbox successfully")
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to log cancellation: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun sendCancellationEmail(userEmail: String, rideRoute: String) {
        val client = OkHttpClient()
        val formspreeUrl = "https://formspree.io/f/xvgorwbo"

        val formBody = FormBody.Builder()
            .add("email", userEmail)
            .add("subject", "Your Ride Has Been Canceled")
            .add("message", "Dear user, your ride on route $rideRoute has been successfully canceled.")
            .build()

        val request = Request.Builder()
            .url(formspreeUrl)
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                (context as? Activity)?.runOnUiThread {
                    Toast.makeText(context, "Failed to send cancellation email: ${e.message}", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                (context as? Activity)?.runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Cancellation email sent successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        val errorBody = response.body?.string()
                        Toast.makeText(context, "Failed to send cancellation email", Toast.LENGTH_SHORT).show()
                        Log.e("YourRidesAdapter", "Error response: $errorBody")
                    }
                }
            }
        })
    }
}
