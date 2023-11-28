package com.daruratindonesianurgentresponse.ui.map

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.daruratindonesianurgentresponse.data.model.MapResult
import com.daruratindonesianurgentresponse.databinding.ItemLocationBinding

class MapAdapter : ListAdapter<MapResult, MapAdapter.MyViewHolder>(DIFF_CALLBACK) {

//    private val mapList = ArrayList<MapList>()
//    lateinit var simpleLocation: SimpleLocation
//    var strLatitude = 0.0
//    var strLongitude = 0.0


    class MyViewHolder(private val binding: ItemLocationBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: MapResult){
            binding.apply {
                itemTvName.text = data.name
                itemTvAddress.text = data.vicinity
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val map = getItem(position)
        holder.bind(map)
//        val item = mapList[position]

//        simpleLocation = SimpleLocation(context)
//
//        //current location
//        strLatitude = simpleLocation.latitude
//        strLongitude = simpleLocation.longitude
//        val strPlaceID = mapList[position].placeId

        //location destination
//        val strLat = modelResults[position].modelGeometry.modelLocation.lat
//        val strLong = modelResults[position].modelGeometry.modelLocation.lng
//        val strJarak = getDistance(strLat, strLong, strLatitude, strLongitude)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MapResult>() {
            override fun areItemsTheSame(oldItem: MapResult, newItem: MapResult): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: MapResult, newItem: MapResult): Boolean {
                return oldItem == newItem
            }
        }
    }
}