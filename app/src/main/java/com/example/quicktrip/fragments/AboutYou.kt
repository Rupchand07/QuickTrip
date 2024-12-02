package com.example.quicktrip.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.quicktrip.activities.*
import com.example.quicktrip.activities.AboutYou
import com.example.quicktrip.databinding.FragmentAboutYouBinding

class AboutYou : Fragment() {

    private var _binding: FragmentAboutYouBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutYouBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileImage.setOnClickListener {
            val i = Intent(requireContext(), EditProfilePicture::class.java)
            startActivity(i)
        }

        binding.username.setOnClickListener {
            val i = Intent(requireContext(), PersonalDetail::class.java)
            startActivity(i)
        }

        binding.addVehicle.setOnClickListener {
            val intent = Intent(requireContext(), AddVehicle::class.java)
            startActivity(intent)
        }

        binding.editProfilePicture.setOnClickListener {
            val intent = Intent(requireContext(), EditProfilePicture::class.java)
            startActivity(intent)
        }

        binding.editProfileDetails.setOnClickListener {
            val intent = Intent(requireContext(), PersonalDetail::class.java)
            startActivity(intent)
        }

        binding.addDrivingLicense.setOnClickListener {
            val intent = Intent(requireContext(), AddLicense::class.java)
            startActivity(intent)
        }

        binding.myId.setOnClickListener {
            val intent = Intent(requireContext(), VerifyId::class.java)
            startActivity(intent)
        }

        binding.confirmEmail.setOnClickListener {
            val intent = Intent(requireContext(), ConfirmEmail::class.java)
            startActivity(intent)
        }

        binding.confirmPhone.setOnClickListener {
            val intent = Intent(requireContext(), ConfirmPhone::class.java)
            startActivity(intent)
        }

        binding.editTravelPreferences.setOnClickListener {
            val intent = Intent(requireContext(), EditTravelPreferences::class.java)
            startActivity(intent)
        }
        binding.editAboutYou.setOnClickListener {
            val intent = Intent(requireContext(), AboutYou::class.java)
            startActivity(intent)
        }

        loadUserData()
    }

    private fun loadUserData() {
        val sharedPreferences = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)

        val bio = sharedPreferences.getString("bio", "No bio available")
        val interests = sharedPreferences.getString("interests", "No interests available")
        val languages = sharedPreferences.getString("languages", "No languages available")

        binding.bioText.text = bio
        binding.interestsText.text = interests
        binding.languagesText.text = languages
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
