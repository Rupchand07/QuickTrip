package com.example.quicktrip.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quicktrip.databinding.ActivityMessageDetailsBinding

class MessageDetails : AppCompatActivity() {

    private lateinit var binding: ActivityMessageDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title")
        val message = intent.getStringExtra("message")
        val timestamp = intent.getStringExtra("timestamp")

        binding.tvTitle.text = title
        binding.tvMessage.text = message
        binding.tvTimestamp.text = timestamp

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnAction.setOnClickListener {
            val actionIntent = Intent(this, TakeAction::class.java)
            actionIntent.putExtra("messageId", title)
            startActivity(actionIntent)
        }

    }
}
