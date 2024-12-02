package com.example.quicktrip.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quicktrip.databinding.ActivitySignUpMailBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpMail : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivitySignUpMailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpMailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn1.setOnClickListener {
            startActivity( Intent(this, SignUp::class.java))
        }
        firebaseAuth = FirebaseAuth.getInstance()


        binding.signUpBtn2.setOnClickListener {
            val email = binding.emailUp.text.toString().trim()
            val password = binding.createPass.text.toString().trim()
            val confirmPassword = binding.confirmPass.text.toString().trim()

            if (email.isEmpty()) {
                binding.emailUp.error = "Email is required"
                binding.emailUp.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailUp.error = "Please enter a valid email"
                binding.emailUp.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.createPass.error = "Password is required"
                binding.createPass.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {
                binding.createPass.error = "Password must be at least 6 characters"
                binding.createPass.requestFocus()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                binding.confirmPass.error = "Passwords do not match"
                binding.confirmPass.requestFocus()
                return@setOnClickListener
            }

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, HomePage::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        task.exception?.message?.let {
                            Toast.makeText(this, "Error: $it", Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }
    }
}