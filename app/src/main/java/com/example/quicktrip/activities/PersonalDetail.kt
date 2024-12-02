package com.example.quicktrip.activities

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quicktrip.databinding.ActivityPersonalDetailBinding

class PersonalDetail : AppCompatActivity() {

    private lateinit var binding: ActivityPersonalDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.backBtn7.setOnClickListener {
            finish()
        }
        loadUserDetails()

        binding.buttonSaveDetails.setOnClickListener {
            val name = binding.editTextFullName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val phone = binding.editTextPhoneNumber.text.toString()

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                saveUserDetails(name, email, phone)
            }
        }
    }

    private fun loadUserDetails() {
        val sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        val name = sharedPref.getString("name", "")
        val email = sharedPref.getString("email", "")
        val phone = sharedPref.getString("phone", "")

        binding.editTextFullName.setText(name)
        binding.editTextEmail.setText(email)
        binding.editTextPhoneNumber.setText(phone)
    }

    private fun saveUserDetails(name: String, email: String, phone: String) {
        val sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        sharedPref.edit().apply {
            putString("name", name)
            putString("email", email)
            putString("phone", phone)
            apply()
        }
        binding.editTextFullName.text.clear()
        binding.editTextEmail.text.clear()
        binding.editTextPhoneNumber.text.clear()
        Toast.makeText(this, "Details saved: $name, $email, $phone", Toast.LENGTH_LONG).show()
    }
}
