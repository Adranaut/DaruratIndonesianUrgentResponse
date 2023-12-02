package com.daruratindonesianurgentresponse.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.daruratindonesianurgentresponse.data.response.CallCenter
import com.daruratindonesianurgentresponse.databinding.ItemCallcenterBinding

class CallCenterAdapter: ListAdapter<CallCenter, CallCenterAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: CallCenter)
    }
    class MyViewHolder(private val binding: ItemCallcenterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CallCenter){
            binding.apply {
                tvServiceName.text = "${data.serviceName}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCallcenterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val location = getItem(position)
        holder.bind(location)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(location)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CallCenter>() {
            override fun areItemsTheSame(oldItem: CallCenter, newItem: CallCenter): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: CallCenter, newItem: CallCenter): Boolean {
                return oldItem == newItem
            }
        }
    }
}