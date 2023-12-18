package com.daruratindonesianurgentresponse.ui.chatbot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.daruratindonesianurgentresponse.R
import com.daruratindonesianurgentresponse.data.local.MessageEntity
import com.daruratindonesianurgentresponse.databinding.FragmentChatBotBinding
import com.daruratindonesianurgentresponse.ui.ViewModelFactory
import kotlinx.coroutines.launch

class ChatBotFragment : Fragment() {

    private val viewModel by viewModels<ChatBotViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private var _binding: FragmentChatBotBinding? = null
    private val binding get() = _binding
    private lateinit var adapter: MessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_bot, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentChatBotBinding.bind(view)

        setupRecyclerView()

        viewModel.allMessages.observe(requireActivity()) { messages ->
            adapter.submitList(messages)
            scrollToBottom()
        }

        binding?.buttonSend?.setOnClickListener {
            sendMessage()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupRecyclerView() {
        adapter = MessageAdapter()
        binding?.recyclerView?.adapter = adapter
        binding?.recyclerView?.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun sendMessage() {
        val sender = "User" // Replace with actual sender (user authentication)
        val content = binding?.editTextMessage?.text.toString().trim()

        if (content.isNotEmpty()) {
            val message = MessageEntity(sender = sender, content = content)
            lifecycleScope.launch {
                viewModel.sendMessage(message)
                viewModel.inputChatBot(content).collect { result ->
                    result.onSuccess { credentials ->
                        credentials.response?.let { response ->
                            val messageBot = MessageEntity(sender = "Bot", content = response)
                            viewModel.sendMessage(messageBot)
                        }
                    }

                    result.onFailure {
                        val messageBot = MessageEntity(sender = "Bot", content = "Sorry process message failure")
                        viewModel.sendMessage(messageBot)
                    }
                }
            }

            // Clear the input field after sending the message
            binding?.editTextMessage?.text?.clear()
        }
    }

    private fun scrollToBottom() {
        if (adapter.itemCount > 0) {
            binding?.recyclerView?.viewTreeObserver?.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    binding?.recyclerView?.viewTreeObserver?.removeOnPreDrawListener(this)
                    binding?.recyclerView?.smoothScrollToPosition(adapter.itemCount - 1)
                    return true
                }
            })
        }
    }
}