package com.example.quicktrip.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.quicktrip.activities.AddReview
import com.example.quicktrip.activities.ChangePassword
import com.example.quicktrip.activities.CloseAccount
import com.example.quicktrip.activities.Help
import com.example.quicktrip.activities.Licenses
import com.example.quicktrip.activities.LiveChat
import com.example.quicktrip.activities.MainActivity2
import com.example.quicktrip.activities.TermsAndConditions
import com.example.quicktrip.databinding.FragmentAccountBinding

class Account : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogout.setOnClickListener {
            val intent = Intent(requireContext(),MainActivity2::class.java)
            startActivity(intent)
        }
        binding.buttonHelp.setOnClickListener {
            val i = Intent(requireContext(),Help::class.java)
            startActivity(i)
        }
        binding.buttonChat.setOnClickListener {
            val i = Intent(requireContext(),LiveChat::class.java)
            startActivity(i)
        }
        binding.buttonChangePassword.setOnClickListener {
            val i = Intent(requireContext(), ChangePassword::class.java)
            startActivity(i)
        }
        binding.buttonTermsConditions.setOnClickListener {
            val i = Intent(requireContext(),TermsAndConditions::class.java)
            startActivity(i)
        }
        binding.buttonLicenses.setOnClickListener {
            val i = Intent(requireContext(),Licenses::class.java)
            startActivity(i)
        }
        binding.buttonRatings.setOnClickListener {
            val i = Intent(requireContext(),AddReview::class.java)
            startActivity(i)
        }
        binding.buttonCloseAccount.setOnClickListener {
            val i = Intent(requireContext(),CloseAccount::class.java)
            startActivity(i)
        }
    }
}