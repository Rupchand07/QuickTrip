package com.example.quicktrip.activities

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quicktrip.databinding.ActivityAboutYouBinding

class AboutYou : AppCompatActivity() {

    private lateinit var binding: ActivityAboutYouBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutYouBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn10.setOnClickListener {
            finish()
        }

        binding.saveButton.setOnClickListener {
            saveUserData()
        }

        loadUserData()
    }

    private fun saveUserData() {
        val bio = binding.bio.text.toString().trim()
        val interests = binding.interests.text.toString().trim()
        val languages = binding.languages.text.toString().trim()

        if (bio.isEmpty() || interests.isEmpty() || languages.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
        } else {
            val sharedPreferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.putString("bio", bio)
            editor.putString("interests", interests)
            editor.putString("languages", languages)

            editor.apply()

            Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadUserData() {
        val sharedPreferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE)

        val bio = sharedPreferences.getString("bio", "")
        val interests = sharedPreferences.getString("interests", "")
        val languages = sharedPreferences.getString("languages", "")

        binding.bio.setText(bio)
        binding.interests.setText(interests)
        binding.languages.setText(languages)
    }
}
