package com.example.quicktrip.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quicktrip.activities.RecentSearchData
import com.example.quicktrip.databinding.ItemRecentSearchBinding

class RecentSearchesAdapter(
    private var searches: List<RecentSearchData>,
    private val deleteSearch: (RecentSearchData) -> Unit,
    private val onSearchClick: (RecentSearchData) -> Unit
) : RecyclerView.Adapter<RecentSearchesAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemRecentSearchBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecentSearchBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val search = searches[position]
        holder.binding.textViewPickup.text = search.pickup
        holder.binding.textViewDestination.text = search.destination
        holder.binding.textViewDate.text = search.date
        holder.binding.textViewPassengers.text = search.passengers

        holder.binding.buttonDelete.setOnClickListener {
            deleteSearch(search)
        }
        holder.itemView.setOnClickListener {
            onSearchClick(search)
        }
    }

    override fun getItemCount(): Int = searches.size
}
