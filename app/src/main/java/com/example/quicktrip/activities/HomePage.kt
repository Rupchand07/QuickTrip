package com.example.quicktrip.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.quicktrip.R
import com.example.quicktrip.adapters.ViewPagerAdapter
import com.example.quicktrip.databinding.ActivityHomePageBinding
import com.example.quicktrip.fragments.Inbox
import com.example.quicktrip.fragments.Profile
import com.example.quicktrip.fragments.Publish
import com.example.quicktrip.fragments.Search
import com.example.quicktrip.fragments.YourRides
import com.google.android.material.tabs.TabLayoutMediator

class HomePage : AppCompatActivity() {
    private lateinit var binding: ActivityHomePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(Search())

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_search -> {
                    loadFragment(Search())
                    hideTabLayout()
                    true
                }
                R.id.navigation_publish -> {
                    loadFragment(Publish())
                    hideTabLayout()
                    true
                }
                R.id.navigation_your_rides -> {
                    loadFragment(YourRides())
                    hideTabLayout()
                    true
                }
                R.id.navigation_inbox -> {
                    loadFragment(Inbox())
                    hideTabLayout()
                    true
                }
                R.id.navigation_profile -> {
                    profile()
                    loadFragment(Profile())
                    true
                }
                else -> false
            }
        }
    }
    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commit()
    }
    private fun profile(){
        binding.tabLayout.visibility = View.VISIBLE
        binding.viewPager.visibility = View.VISIBLE

        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        val adapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "About You"
                1 -> tab.text = "Account"
            }
        }.attach()
    }
    private fun hideTabLayout() {
        binding.tabLayout.visibility = View.GONE
        binding.viewPager.visibility = View.GONE
    }
}