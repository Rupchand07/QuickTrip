package com.example.quicktrip.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quicktrip.databinding.ActivityHelpBinding

class Help : AppCompatActivity() {

    private lateinit var binding: ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonContactSupport.setOnClickListener {
            sendEmailToSupport()
        }
    }

    private fun sendEmailToSupport() {

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:rupchand905@gmail.com")
            putExtra(Intent.EXTRA_SUBJECT, "Help Request")
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}
