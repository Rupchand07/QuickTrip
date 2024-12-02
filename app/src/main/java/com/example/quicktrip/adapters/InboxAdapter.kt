package com.example.quicktrip.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quicktrip.activities.InboxMessage
import com.example.quicktrip.databinding.ItemInboxBinding

class InboxAdapter(
    private val messages: MutableList<InboxMessage>,
    private val onDeleteClick: (InboxMessage) -> Unit,
    private val onItemClick: (InboxMessage) -> Unit
) : RecyclerView.Adapter<InboxAdapter.InboxViewHolder>() {

    inner class InboxViewHolder(private val binding: ItemInboxBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: InboxMessage) {
            binding.tvTitle.text = message.title
            binding.tvMessage.text = message.message
            binding.tvTimestamp.text = message.timestamp

            binding.root.setOnClickListener {
                onItemClick(message)
            }

            binding.btnDelete.setOnClickListener {
                onDeleteClick(message)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InboxViewHolder {
        val binding = ItemInboxBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InboxViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InboxViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount(): Int = messages.size

    fun removeItem(message: InboxMessage) {
        val position = messages.indexOf(message)
        if (position != -1) {
            messages.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}
