package com.example.quicktrip.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.quicktrip.fragments.AboutYou
import com.example.quicktrip.fragments.Account

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AboutYou()
            1 -> Account()
            else -> throw IllegalStateException("Unexpected position: $position")
        }
    }
}