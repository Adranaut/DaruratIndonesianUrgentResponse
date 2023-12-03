package com.daruratindonesianurgentresponse.ui.map

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.daruratindonesianurgentresponse.data.response.ResultsItem
import com.daruratindonesianurgentresponse.databinding.ItemLocationBinding
import com.daruratindonesianurgentresponse.utils.CalculateDistance
import java.text.DecimalFormat

class NearbyAdapter: ListAdapter<ResultsItem, NearbyAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(private val binding: ItemLocationBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data: ResultsItem){
            val distance = CalculateDistance.getDistance(
                data.geometry?.location?.lat as Double,
                data.geometry.location.lng as Double
            )
            binding.apply {
                itemTvName.text = "${data.name}"
                itemTvAddress.text = "${data.vicinity}"
                itemTvDistance.text = DecimalFormat("#.##").format(distance) + " KM"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val location = getItem(position)
        holder.bind(location)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ResultsItem>() {
            override fun areItemsTheSame(oldItem: ResultsItem, newItem: ResultsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ResultsItem, newItem: ResultsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}