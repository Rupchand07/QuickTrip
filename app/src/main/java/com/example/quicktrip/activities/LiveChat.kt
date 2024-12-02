package com.example.quicktrip.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quicktrip.adapters.ChatAdapter
import com.example.quicktrip.databinding.ActivityLiveChatBinding

class LiveChat : AppCompatActivity() {
    private lateinit var binding: ActivityLiveChatBinding
    private val chatList = mutableListOf<ChatMessage>()
    private lateinit var chatAdapter: ChatAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiveChatBinding.inflate(layoutInflater)
        setContentView(binding.root)


        chatAdapter = ChatAdapter(chatList)
        binding.recyclerViewChat.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewChat.adapter = chatAdapter

        binding.btnSend.setOnClickListener {
            val userQuestion = binding.etQuestion.text.toString().trim()
            if (userQuestion.isNotEmpty()) {

                chatList.add(ChatMessage(userQuestion, isUser = true))
                chatAdapter.notifyDataSetChanged()
                binding.etQuestion.text.clear()

                getBotResponse(userQuestion)
            } else {
                Toast.makeText(this, "Please enter a question", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getBotResponse(userQuestion: String) {

        val botResponse = when {
            userQuestion.contains("offer ride", ignoreCase = true) -> "To offer a ride, go to the Offer Ride section, add vehicle details, and enter your destination and available seats."
            userQuestion.contains("book ride", ignoreCase = true) -> "To book a ride, search for rides matching your route and select one to proceed with the booking."
            userQuestion.contains("payment", ignoreCase = true) -> "We accept payments through Razorpay. Complete payment to secure your booking, and download the ticket if needed."
            userQuestion.contains("profile", ignoreCase = true) -> "You can update your profile by navigating to the Profile section where you can edit details like travel preferences and bio."
            userQuestion.contains("cancel ride", ignoreCase = true) -> "To cancel a ride, go to 'Your Rides', select the ride you wish to cancel, and tap on the Cancel button. A cancellation message will be sent to your Inbox."
            userQuestion.contains("help", ignoreCase = true) -> "For assistance, go to the Help and Support section or chat with support directly through this page."
            userQuestion.contains("contact driver", ignoreCase = true) -> "To contact the driver of a booked ride, open the ride details in 'Your Rides' and select 'Contact Driver'."
            else -> "Sorry, I don't understand the question. Please ask something else or reach out to our support team for further assistance."
        }


        chatList.add(ChatMessage(botResponse, isUser = false))
        chatAdapter.notifyDataSetChanged()
    }
}
