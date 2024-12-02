package com.example.quicktrip.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.quicktrip.activities.Ride
import com.example.quicktrip.databinding.RidesItemBinding

class RidesAdapter(private val rideList: List<Ride>,private val onRideClick: (Ride) -> Unit) : RecyclerView.Adapter<RidesAdapter.RideViewHolder>() {

    inner class RideViewHolder(val binding: RidesItemBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                onRideClick(rideList[bindingAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RideViewHolder {
        val binding = RidesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RideViewHolder(binding)
    }

    override fun getItemCount(): Int = rideList.size

    override fun onBindViewHolder(holder: RideViewHolder, position: Int) {
        val ride = rideList[position]
        holder.binding.apply {
            tvDepartureTime.text = ride.time
            tvStartLocation.text = ride.pickup
            tvEndLocation.text = ride.destination
            tvPrice.text = "₹${ride.price}"
            tvDriverName.text = ride.driverName
            tvDriverRating.text = ride.rating
            Glide.with(holder.itemView.context).load(ride.driverImageUrl).into(ivDriverImage)
        }
    }
}
