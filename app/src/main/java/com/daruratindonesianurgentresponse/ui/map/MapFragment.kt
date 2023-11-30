package com.daruratindonesianurgentresponse.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.daruratindonesianurgentresponse.R
import com.daruratindonesianurgentresponse.data.response.ResultsItem
import com.daruratindonesianurgentresponse.databinding.FragmentMapBinding
import com.daruratindonesianurgentresponse.ui.ViewModelFactory
import com.daruratindonesianurgentresponse.utils.LATITUDE
import com.daruratindonesianurgentresponse.utils.LONGITUDE
import com.daruratindonesianurgentresponse.utils.RADIUS
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MapFragment : Fragment(), OnMapReadyCallback {

    private val viewModel by viewModels<MapViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private lateinit var mLocation: LatLng
    private lateinit var mMap : GoogleMap
    private val boundsBuilder = LatLngBounds.Builder()

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.apply {
            mapView.onCreate(savedInstanceState)
            mapView.onResume()
            mapView.getMapAsync(this@MapFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMapBinding.bind(view)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.rvMap.layoutManager = LinearLayoutManager(requireContext())

        binding.spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        binding.apply {
                            rvMap.visibility = View.GONE
                            tvStatusData.visibility = View.VISIBLE
                            mMap.clear()
                            getMyLocation()
                        }
                        snackBar("Please select the category!")
                    }
                    1 -> servicesLocation("12072")
                    2 -> servicesLocation("15008")
                    3 -> servicesLocation("12071")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                snackBar("Please select the category!")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(
//                    requireContext(),
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ) ==PackageManager.PERMISSION_GRANTED) {
//                getMyLocation()
//                mMap!!.isMyLocationEnabled = true
//            }
//        } else {
//            getMyLocation()
//            mMap!!.isMyLocationEnabled = true
//        }

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        getMyLocation()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
                snackBar(getString(R.string.message_granted))
            } else {
                snackBar(getString(R.string.message_denied))
            }
        }

    private fun getMyLocation() {
        showLoading(true)
        if (ContextCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    LATITUDE = location.latitude
                    LONGITUDE = location.longitude
                    mLocation = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLocation, 12f))
                    mMap.addMarker(
                        MarkerOptions()
                            .position(mLocation)
                            .title(getString(R.string.current_location))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    )
                }
                showLoading(false)
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            showLoading(false)
        }
    }

    private fun servicesLocation(code: String) {
        showLoading(true)
        binding.apply {
            tvStatusData.visibility = View.GONE
            binding.rvMap.visibility = View.VISIBLE
        }
        mMap.clear()
        lifecycleScope.launch {
            viewModel.getNearbyPlaces("$LATITUDE,$LONGITUDE", RADIUS, code).collect { result ->
                result.onSuccess { credentials ->
                    credentials.results?.let { items ->
                        showRecycleView(items)
                        boundsBuilder.include(mLocation)
                        mMap.addMarker(
                            MarkerOptions()
                                .position(mLocation)
                                .title(getString(R.string.current_location))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        )
                        items.forEach { data ->
                            val latLng = LatLng(data?.geocodes?.main?.latitude as Double, data.geocodes.main.longitude as Double)
                            mMap.addMarker(
                                MarkerOptions()
                                    .position(latLng)
                                    .title(data.name)
                                    .snippet(data.location?.formattedAddress)
                            )
                            boundsBuilder.include(latLng)
                        }

                        val bounds: LatLngBounds = boundsBuilder.build()
                        mMap.animateCamera(
                            CameraUpdateFactory.newLatLngBounds(
                                bounds,
                                resources.displayMetrics.widthPixels,
                                resources.displayMetrics.heightPixels,
                                180
                            )
                        )
                        showLoading(false)
                    }
                }

                result.onFailure {
                    snackBar("Gagal mendapatkan data")
                    showLoading(false)
                }
            }
        }
    }

    private fun showRecycleView(mapResults: List<ResultsItem?>) {
        val adapter = NearbyAdapter()
        adapter.submitList(mapResults)
        binding.rvMap.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun snackBar(message: String) {
        Snackbar.make(binding.loMain, message, Snackbar.LENGTH_LONG).show()
    }
}