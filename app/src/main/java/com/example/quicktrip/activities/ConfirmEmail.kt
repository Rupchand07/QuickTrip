package com.example.quicktrip.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quicktrip.databinding.ActivityConfirmEmailBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ConfirmEmail : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmEmailBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConfirmEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val currentUser = auth.currentUser
        if (currentUser != null) {
            binding.textViewCurrentEmail.text = "Current Email: ${currentUser.email}"
        }

        binding.buttonVerifyEmail.setOnClickListener {
            sendEmailVerification()
        }


        binding.buttonUpdateEmail.setOnClickListener {
            val newEmail = binding.editTextNewEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if (newEmail.isNotEmpty() && password.isNotEmpty()) {
                reauthenticateAndUpdateEmail(newEmail, password)
            } else {
                Toast.makeText(this, "Please enter a new email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendEmailVerification() {
        val user = auth.currentUser
        user?.let {
            it.sendEmailVerification()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Verification email sent to ${it.email}", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Failed to send verification email", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun reauthenticateAndUpdateEmail(newEmail: String, password: String) {
        val user = auth.currentUser
        user?.let {

            val credential = EmailAuthProvider.getCredential(user.email!!, password)

            user.reauthenticate(credential)
                .addOnCompleteListener { reAuthTask ->
                    if (reAuthTask.isSuccessful) {

                        user.updateEmail(newEmail)
                            .addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
                                    sendEmailVerificationToNewEmail(newEmail)
                                    Toast.makeText(this, "Email updated successfully.", Toast.LENGTH_SHORT).show()
                                } else {

                                    Toast.makeText(this, "Failed to update email.", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {

                        Toast.makeText(this, "Re-authentication failed. Please check your password.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun sendEmailVerificationToNewEmail(newEmail: String) {
        auth.currentUser?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    binding.textViewVerificationMessage.text = "Verification email sent to $newEmail. Please check your inbox to verify."
                } else {
                    Log.e("ConfirmEmailActivity", "Error sending verification email", task.exception)
                    Toast.makeText(this, "Failed to send verification email", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
