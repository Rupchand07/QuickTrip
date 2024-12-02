package com.example.quicktrip.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quicktrip.databinding.ActivityTakeActionBinding
import com.google.firebase.firestore.FirebaseFirestore

class TakeAction : AppCompatActivity() {

    private lateinit var binding: ActivityTakeActionBinding
    private lateinit var firestore: FirebaseFirestore
    private var messageId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTakeActionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        messageId = intent.getStringExtra("messageId")

        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        binding.btnRespond.setOnClickListener {
            respondToMessage()
        }
        binding.btnBackActionPage.setOnClickListener {
            finish()
        }
        binding.btnContactSupport.setOnClickListener {
            contactSupport()
        }
    }

    private fun respondToMessage() {
        val i = Intent(this,LiveChat::class.java)
        startActivity(i)
        Toast.makeText(this, "Responding to message...", Toast.LENGTH_SHORT).show()
    }


    private fun contactSupport() {
        val i = Intent(this,Help::class.java)
        startActivity(i)
        Toast.makeText(this, "Contacting support...", Toast.LENGTH_SHORT).show()
    }
}
