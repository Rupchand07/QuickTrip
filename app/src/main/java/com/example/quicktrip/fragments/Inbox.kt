package com.example.quicktrip.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quicktrip.activities.InboxMessage
import com.example.quicktrip.activities.MessageDetails
import com.example.quicktrip.adapters.InboxAdapter
import com.example.quicktrip.databinding.FragmentInboxBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class Inbox : Fragment() {
    private lateinit var binding: FragmentInboxBinding
    private val inboxMessages = mutableListOf<InboxMessage>()
    private lateinit var inboxAdapter: InboxAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInboxBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadInboxMessages()

        val confirmationMessage = arguments?.getString("confirmationMessage")
        confirmationMessage?.let {
            showConfirmationSnackbar(it)
            addMessageToInbox(it)
        }
    }

    private fun setupRecyclerView() {
        inboxAdapter = InboxAdapter(
            inboxMessages,
            onDeleteClick = { message -> deleteInboxMessage(message) },
            onItemClick = { message -> openMessageDetails(message) }
        )

        binding.recyclerViewInbox.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewInbox.adapter = inboxAdapter
    }

    private fun loadInboxMessages() {
        FirebaseFirestore.getInstance().collection("messages")
            .get()
            .addOnSuccessListener { result ->
                inboxMessages.clear()
                for (document in result) {
                    val message = InboxMessage(
                        id = document.id,
                        title = document.getString("title") ?: "",
                        message = document.getString("message") ?: "",
                        timestamp = document.getString("timestamp") ?: ""
                    )
                    inboxMessages.add(message)
                }
                inboxAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Snackbar.make(binding.root, "Failed to load messages: ${e.message}", Snackbar.LENGTH_LONG).show()
            }
    }

    private fun deleteInboxMessage(message: InboxMessage) {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("messages").document(message.id)
            .delete()
            .addOnSuccessListener {
                inboxAdapter.removeItem(message)
                Snackbar.make(binding.root, "Message deleted", Snackbar.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Snackbar.make(binding.root, "Failed to delete message: ${e.message}", Snackbar.LENGTH_LONG).show()
            }
    }

    private fun showConfirmationSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun addMessageToInbox(message: String) {
        inboxMessages.add(
            InboxMessage(
                id = "temp_${System.currentTimeMillis()}",
                title = "Booking Confirmation",
                message = message,
                timestamp = System.currentTimeMillis().toString()
            )
        )
        inboxAdapter.notifyItemInserted(inboxMessages.size - 1)
    }

    private fun openMessageDetails(message: InboxMessage) {
        val intent = Intent(requireContext(), MessageDetails::class.java).apply {
            putExtra("messageId", message.id)
            putExtra("title", message.title)
            putExtra("message", message.message)
            putExtra("timestamp", message.timestamp)
        }
        startActivity(intent)
    }
}
