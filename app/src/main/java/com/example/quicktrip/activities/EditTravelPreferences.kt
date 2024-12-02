package com.example.quicktrip.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quicktrip.databinding.ActivityEditTravelPreferencesBinding
import com.google.firebase.firestore.FirebaseFirestore

class EditTravelPreferences : AppCompatActivity() {


    private lateinit var binding: ActivityEditTravelPreferencesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTravelPreferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn9.setOnClickListener {
            finish()
        }

        binding.savePreferences.setOnClickListener {
            val musicPreference = when {
                binding.musicLoud.isChecked -> "Loud Music"
                binding.musicSoft.isChecked -> "Soft Music"
                else -> "No Music"
            }

            val conversationPreference = when {
                binding.conversationTalkative.isChecked -> "Talkative"
                binding.conversationQuiet.isChecked -> "Quiet"
                else -> "Silent"
            }

            val petsAllowed = binding.petsAllowedSwitch.isChecked
            val smokingAllowed = binding.smokingAllowedSwitch.isChecked

            savePreferencesToDatabase(musicPreference, conversationPreference, petsAllowed, smokingAllowed)
        }
    }


    private fun savePreferencesToDatabase(music: String, conversation: String, pets: Boolean, smoking: Boolean) {

        val preferences = hashMapOf(
            "musicPreference" to music,
            "conversationPreference" to conversation,
            "petsAllowed" to pets,
            "smokingAllowed" to smoking
        )
        val currentUser = "userId"
        FirebaseFirestore.getInstance().collection("userPreferences")
            .document(currentUser)
            .set(preferences)
            .addOnSuccessListener {
                Toast.makeText(this, "Preferences saved successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save preferences: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
