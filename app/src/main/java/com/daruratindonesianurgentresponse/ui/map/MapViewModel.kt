package com.daruratindonesianurgentresponse.ui.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.daruratindonesianurgentresponse.BuildConfig
import com.daruratindonesianurgentresponse.data.Repository
import com.daruratindonesianurgentresponse.data.api.ApiConfig
import com.daruratindonesianurgentresponse.data.model.MapResult
import com.daruratindonesianurgentresponse.data.response.NearbyResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapViewModel(private val repository: Repository) : ViewModel() {

    private val mapResultsMutableLiveData = MutableLiveData<ArrayList<MapResult>>()

    fun setHospitalLocation(strLocation: String) {
        val apiService = ApiConfig.getApiService()
        val call = apiService.getNearbyResult(BuildConfig.API_KEY, "hospital", strLocation, "distance")
        call.enqueue(object : Callback<NearbyResult> {
            override fun onResponse(call: Call<NearbyResult>, response: Response<NearbyResult>) {
                if (!response.isSuccessful) {
                    Log.d("response", response.toString())
                } else if (response.body() != null) {
                    val items = ArrayList(response.body()!!.mapResults)
                    mapResultsMutableLiveData.postValue(items)
                }
            }

            override fun onFailure(call: Call<NearbyResult>, t: Throwable) {
                Log.d("failure", t.toString())
            }
        })
    }

    fun getHospitalLocation(): LiveData<ArrayList<MapResult>> = mapResultsMutableLiveData
}