package com.daruratindonesianurgentresponse.ui.map

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daruratindonesianurgentresponse.data.model.MapResult
import com.daruratindonesianurgentresponse.databinding.ItemLocationBinding

class TestAdapter(private val context: Context?) : RecyclerView.Adapter<TestAdapter.SearchViewHolder>() {

    private val modelResults = ArrayList<MapResult>()

    fun setResultAdapter(items: ArrayList<MapResult>) {
        modelResults.clear()
        modelResults.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = modelResults[position]

        val strPlaceID = modelResults[position].placeId


        holder.bind(item)

    }

    override fun getItemCount(): Int {
        return modelResults.size
    }

    class SearchViewHolder(private val binding: ItemLocationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MapResult){
            binding.apply {
                itemTvName.text = data.name
                itemTvAddress.text = data.vicinity
            }
        }
    }

}