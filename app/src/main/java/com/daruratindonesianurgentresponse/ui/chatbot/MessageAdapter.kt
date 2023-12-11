package com.daruratindonesianurgentresponse.ui.chatbot

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.daruratindonesianurgentresponse.R
import com.daruratindonesianurgentresponse.data.local.MessageEntity
import com.daruratindonesianurgentresponse.databinding.ItemMessageBinding

class MessageAdapter : ListAdapter<MessageEntity, MessageAdapter.MessageViewHolder>(MessageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = getItem(position)
        holder.bind(message)
    }

    class MessageViewHolder(private val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: MessageEntity) {
            binding.apply {
                tvMessage.text = message.content
                setTextColor(message.sender, tvMessage, layoutMessage)
            }

        }

        private fun setTextColor(name: String?, textView: TextView, layout: LinearLayout) {
            if (name == "User") {
                textView.setBackgroundResource(R.drawable.rounded_message_user)
                layout.gravity = Gravity.END
            } else {
                textView.setBackgroundResource(R.drawable.rounded_message_bot)
                layout.gravity = Gravity.START
            }
        }
    }

    private class MessageDiffCallback : DiffUtil.ItemCallback<MessageEntity>() {
        override fun areItemsTheSame(oldItem: MessageEntity, newItem: MessageEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MessageEntity, newItem: MessageEntity): Boolean {
            return oldItem == newItem
        }
    }
}