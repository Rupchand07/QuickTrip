package com.example.quicktrip.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quicktrip.databinding.ActivityAddReviewBinding

class AddReview : AppCompatActivity() {

    private lateinit var binding: ActivityAddReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtnReview.setOnClickListener {
            finish()
        }

        binding.btnSubmitReview.setOnClickListener {
            val rating = binding.ratingBar.rating
            val reviewText = binding.etReviewText.text.toString()

            if (rating == 0f) {
                Toast.makeText(this, "Please provide a rating", Toast.LENGTH_SHORT).show()
            } else if (reviewText.isEmpty()) {
                Toast.makeText(this, "Please write your review", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Review submitted", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
