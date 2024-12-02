package com.example.quicktrip.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quicktrip.databinding.ActivityCloseAccountBinding
import retrofit2.Call
import retrofit2.Callback

class CloseAccount : AppCompatActivity() {

    private lateinit var binding: ActivityCloseAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCloseAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonConfirmCloseAccount.setOnClickListener {
            confirmCloseAccount()
        }

        binding.buttonCancel.setOnClickListener {
            finish()
        }
    }

    private fun confirmCloseAccount() {
        val feedback = binding.editTextFeedback.text.toString()
        val userId = "12345"

        val request = CloseAccountRequest(userId, feedback)

        RetrofitClient.api.closeAccount(request).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: retrofit2.Response<ApiResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CloseAccount, response.body()?.message, Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@CloseAccount, "Error: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(this@CloseAccount, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
