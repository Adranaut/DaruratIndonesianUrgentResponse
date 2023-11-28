package com.daruratindonesianurgentresponse.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.daruratindonesianurgentresponse.R
import com.daruratindonesianurgentresponse.databinding.FragmentMapBinding
import com.daruratindonesianurgentresponse.ui.ViewModelFactory
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

class MapFragment : Fragment(), OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private val viewModel by viewModels<MapViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
//    private lateinit var mMap : GoogleMap
//    private lateinit var myLocation : Location
//    private lateinit var fusedLocationClient : FusedLocationProviderClient
//
//    private lateinit var mLocation: String
//    private var latitude : Double? = null
//    private var longitude : Double? = null

//    private lateinit var placesClient : PlacesClient
//    private lateinit var sessionToken : AutocompleteSessionToken

    var mMap : GoogleMap? = null
    lateinit var mLastLocation : Location
    var mCurrLocationMarker: Marker? = null
    var mGoogleApiClient: GoogleApiClient? = null
    lateinit var mLocationRequest: LocationRequest

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

//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
//
//        binding.rvMap.layoutManager = LinearLayoutManager(requireContext())


        //set location lat long
//        mLocation = "$latitude,$longitude"

        binding.btnTest.setOnClickListener {
            searchLocation()
        }

//        viewModel.setHospitalLocation(mLocation)
//
//        viewModel.getHospitalLocation().observe(viewLifecycleOwner) { mapResults: ArrayList<MapResult>? ->
//            showRecycleView(mapResults)
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) ==PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient()
                mMap!!.isMyLocationEnabled = true
            }
        } else {
            buildGoogleApiClient()
            mMap!!.isMyLocationEnabled = true
        }

        mMap!!.uiSettings.isZoomControlsEnabled = true
        mMap!!.uiSettings.isIndoorLevelPickerEnabled = true
        mMap!!.uiSettings.isCompassEnabled = true
        mMap!!.uiSettings.isMapToolbarEnabled = true

//        getMyLocation()
    }

    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(requireContext())
            .addConnectionCallbacks(this@MapFragment)
            .addOnConnectionFailedListener(this@MapFragment)
            .addApi(LocationServices.API).build()
        mGoogleApiClient!!.connect()
    }

    override fun onLocationChanged(location: Location) {
        mLastLocation = location
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker!!.remove()
        }

        val latLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("Current Position")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        mCurrLocationMarker = mMap!!.addMarker(markerOptions)

        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap!!.moveCamera(CameraUpdateFactory.zoomTo(12f))

        if (mGoogleApiClient != null) {
            LocationServices.getFusedLocationProviderClient(requireActivity())
        }
    }

    override fun onConnected(p0: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 1000
        mLocationRequest.fastestInterval = 1000
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
        ) ==PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient(requireActivity())
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("Not yet implemented")
    }

    fun searchLocation() {
        val locationSearch = "Bandung"
        var location : String
        location = locationSearch.trim()
        var addressList: List<Address>? = null

        if (location == null || location == "") {
            Toast.makeText(requireContext(), "Provide location", Toast.LENGTH_LONG).show()
        } else {
            val geoCoder = Geocoder(requireContext())
            try {
                addressList = geoCoder.getFromLocationName(location, 1)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val address = addressList!![0]
            val latLng = LatLng(address.latitude, address.longitude)
            mMap!!.addMarker(MarkerOptions().position(latLng).title(location))
            mMap!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        }
    }

//    private val requestPermissionLauncher =
//        registerForActivityResult(
//            ActivityResultContracts.RequestPermission()
//        ) { isGranted: Boolean ->
//            if (isGranted) {
//                getMyLocation()
//            }
//        }
//
//    private fun getMyLocation() {
//        if (ContextCompat.checkSelfPermission(
//                this.requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
////            mMap.isMyLocationEnabled = true
//            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//                if (location != null) {
//                    latitude = location.latitude
//                    longitude = location.longitude
////                    val myLocation = location
//                    val currentLocation = LatLng(location.latitude, location.longitude)
//                    mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12f))
//
//                    mLocation = "$latitude,$longitude"
//                    viewModel.setHospitalLocation(mLocation)
//
//                    viewModel.getHospitalLocation().observe(requireActivity()) { mapResults: ArrayList<MapResult>? ->
//                        if (mapResults?.size != 0) {
//                            showRecycleView(mapResults)
//                            Toast.makeText(requireContext(), "ditemukan!", Toast.LENGTH_SHORT).show()
//                        }else {
//                            Toast.makeText(requireContext(), "Oops, lokasi Rumah Sakit tidak ditemukan!", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                }
//            }
//
//        } else {
//            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//        }
//    }

//    private fun showRecycleView(mapResults: ArrayList<MapResult>?) {
//        val adapter = MapAdapter()
//        if (mapResults != null) {
//            adapter.submitList(mapResults)
//        }
////        adapter.submitList(mapResults)
//        binding.rvMap.adapter = adapter
//    }
}