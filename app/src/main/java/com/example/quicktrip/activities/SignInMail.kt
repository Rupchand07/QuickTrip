package com.example.quicktrip.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quicktrip.databinding.ActivitySignInMailBinding
import com.google.firebase.auth.FirebaseAuth

class SignInMail : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignInMailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInMailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.logInBtn2.setOnClickListener {
            val email = binding.emailIn.text.toString().trim()
            val password = binding.passIn.text.toString().trim()


            if (email.isEmpty()) {
                binding.emailIn.error = "Email is required"
                binding.emailIn.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.passIn.error = "Password is required"
                binding.passIn.requestFocus()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, HomePage::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        task.exception?.message?.let {
                            Toast.makeText(this, "Error: Please enter your correct Mail and Password", Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }
        binding.forgotPass.setOnClickListener {
            val email = binding.emailIn.text.toString().trim()

            if (email.isEmpty()) {
                binding.emailIn.error = "Email is required"
                binding.emailIn.requestFocus()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Password reset email sent", Toast.LENGTH_LONG).show()
                    } else {
                        task.exception?.message?.let {
                            Toast.makeText(this, "Error: $it", Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }

        binding.backBtn2.setOnClickListener {
            finish()
        }
    }
}