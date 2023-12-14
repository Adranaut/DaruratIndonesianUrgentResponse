package com.daruratindonesianurgentresponse.ui.map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.daruratindonesianurgentresponse.R
import com.daruratindonesianurgentresponse.data.response.DataItem
import com.daruratindonesianurgentresponse.databinding.FragmentMapBinding
import com.daruratindonesianurgentresponse.ui.ViewModelFactory
import com.daruratindonesianurgentresponse.utils.DarkMode
import com.daruratindonesianurgentresponse.utils.LATITUDE
import com.daruratindonesianurgentresponse.utils.LOCATION
import com.daruratindonesianurgentresponse.utils.LONGITUDE
import com.daruratindonesianurgentresponse.utils.PRIMARYRADIUS
import com.daruratindonesianurgentresponse.utils.SECONDARYRYRADIUS
import com.daruratindonesianurgentresponse.utils.SPINNERINDEX
import com.daruratindonesianurgentresponse.utils.STATUSGPS
import com.daruratindonesianurgentresponse.utils.STATUSMAP
import com.daruratindonesianurgentresponse.utils.TYPE
import com.daruratindonesianurgentresponse.utils.TYPEAMBULANCE
import com.daruratindonesianurgentresponse.utils.TYPEFIREFIGHTER
import com.daruratindonesianurgentresponse.utils.TYPEMEDICE
import com.daruratindonesianurgentresponse.utils.TYPEPOLICE
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MapFragment : Fragment(), OnMapReadyCallback {

    private val viewModel by viewModels<MapViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    private lateinit var alamat: String
    private val boundsBuilder = LatLngBounds.Builder()

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding?.apply {
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

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMapBinding.bind(view)

        if (AppCompatDelegate.getDefaultNightMode() == DarkMode.ON.value) {
            binding?.spinnerType?.background = ColorDrawable(resources.getColor(R.color.dark_gray))
        } else {
            binding?.spinnerType?.background = ColorDrawable(resources.getColor(R.color.white))
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding?.rvMap?.layoutManager = LinearLayoutManager(requireContext())

        binding?.spinnerType?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        binding?.apply {
                            rvMap.visibility = View.GONE
                            tvStatusData.visibility = View.VISIBLE
                            mMap.clear()
                            getMyLocation()
                        }
                        SPINNERINDEX = 0
                        STATUSMAP = false
                    }
                    1 -> {
                        getMyLocation()
                        servicesLocation(TYPEPOLICE)
                        TYPE = TYPEPOLICE
                        SPINNERINDEX = 1
                    }
                    2 -> {
                        getMyLocation()
                        servicesLocation(TYPEAMBULANCE)
                        TYPE = TYPEAMBULANCE
                        SPINNERINDEX = 2
                    }
                    3 -> {
                        getMyLocation()
                        servicesLocation(TYPEMEDICE)
                        TYPE = TYPEMEDICE
                        SPINNERINDEX = 3
                    }
                    4 -> {
                        getMyLocation()
                        servicesLocation(TYPEFIREFIGHTER)
                        TYPE = TYPEFIREFIGHTER
                        SPINNERINDEX = 4
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                snackBar(getString(R.string.select_category))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        showLoading(true)
        setMapStyle()

        //Cek lokasi terkini apakah sudah pernah tampil
        if (STATUSMAP) {
            binding?.apply {
                tvStatusData.visibility = View.GONE
                rvMap.visibility = View.VISIBLE
                getMyLocation()
                spinnerType.setSelection(SPINNERINDEX)
                servicesLocation(TYPE)
            }
        } else {
            binding?.apply {
                tvStatusData.visibility = View.VISIBLE
                rvMap.visibility = View.GONE
                getMyLocation()
                spinnerType.setSelection(SPINNERINDEX)
            }
        }
    }

    private fun setMapStyle() {
        if (!isAdded || view == null) {
            // Fragment is not added or the view is null
            return
        }
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val context = context
                if (context != null) {
                    val success =
                        if (AppCompatDelegate.getDefaultNightMode() == DarkMode.ON.value) {
                            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.dark_map_style))
                        } else {
                            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.light_map_style))
                        }
                    if (!success) {
                        snackBar(getString(R.string.style_parsing_failed))
                    }
                } else {
                    // Handle the case when the context is null
                    snackBar(getString(R.string.context_is_null))
                }
            } catch (exception: Resources.NotFoundException) {
                snackBar(getString(R.string.cant_find_style))
            }
        }
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
        viewLifecycleOwner.lifecycleScope.launch {
            // Delay for 3 seconds
            delay(3000)

            val currentView = view
            if (currentView != null) {
                val context = context ?: return@launch // Check if the context is available
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        if (location != null) {
                            LATITUDE = location.latitude
                            LONGITUDE = location.longitude
                            LOCATION = LatLng(LATITUDE, LONGITUDE)
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LOCATION, 12f))
                            mMap.addMarker(
                                MarkerOptions()
                                    .position(LOCATION)
                                    .title(getString(R.string.current_location))
                                    .icon(
                                        BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_GREEN
                                        )
                                    )
                            )
                        }
                    }
                    showLoading(false)
                } else {
                    showLoading(false)
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            } else {
                // Handle the case when the view is not available (null)
                showLoading(false)
            }
        }
    }

    private fun servicesLocation(type: String) {
        showLoading(true)
        if (STATUSGPS) {
            mMap.clear()
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.getNearbyPlaces(type, "$LATITUDE", "$LONGITUDE", PRIMARYRADIUS).collect { result ->
                    result.onSuccess { credentials ->
                        credentials.data?.let { items ->
                            STATUSMAP = true
                            binding?.apply {
                                tvStatusData.visibility = View.GONE
                                rvMap.visibility = View.VISIBLE
                            }
                            showRecycleView(items)
                            boundsBuilder.include(LOCATION)
                            mMap.addMarker(
                                MarkerOptions()
                                    .position(LOCATION)
                                    .title(getString(R.string.current_location))
                                    .icon(
                                        BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_GREEN
                                        )
                                    )
                            )
                            items.forEach { data ->
                                val latLng = LatLng(
                                    data?.latitude!!.toDouble(),
                                    data.longitude!!.toDouble()
                                )
                                alamat = data.alamat ?: ""
                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(latLng)
                                        .title(data.nama)
                                        .snippet(alamat)
                                )
                                boundsBuilder.include(latLng)
                            }
                            val bounds: LatLngBounds = boundsBuilder.build()
                            mMap.animateCamera(
                                CameraUpdateFactory.newLatLngBounds(
                                    bounds,
                                    resources.displayMetrics.widthPixels,
                                    resources.displayMetrics.heightPixels,
                                    200
                                )
                            )
                            showLoading(false)
                        }
                    }

                    result.onFailure {
                        viewModel.getNearbyPlaces(type, "$LATITUDE", "$LONGITUDE", SECONDARYRYRADIUS).collect { result ->
                            result.onSuccess { credentials ->
                                credentials.data?.let { items ->
                                    STATUSMAP = true
                                    binding?.apply {
                                        tvStatusData.visibility = View.GONE
                                        rvMap.visibility = View.VISIBLE
                                    }
                                    showRecycleView(items)
                                    boundsBuilder.include(LOCATION)
                                    mMap.addMarker(
                                        MarkerOptions()
                                            .position(LOCATION)
                                            .title(getString(R.string.current_location))
                                            .icon(
                                                BitmapDescriptorFactory.defaultMarker(
                                                    BitmapDescriptorFactory.HUE_GREEN
                                                )
                                            )
                                    )
                                    items.forEach { data ->
                                        val latLng = LatLng(
                                            data?.latitude!!.toDouble(),
                                            data.longitude!!.toDouble()
                                        )
                                        alamat = data.alamat ?: ""
                                        mMap.addMarker(
                                            MarkerOptions()
                                                .position(latLng)
                                                .title(data.nama)
                                                .snippet(alamat)
                                        )
                                        boundsBuilder.include(latLng)
                                    }
                                    val bounds: LatLngBounds = boundsBuilder.build()
                                    mMap.animateCamera(
                                        CameraUpdateFactory.newLatLngBounds(
                                            bounds,
                                            resources.displayMetrics.widthPixels,
                                            resources.displayMetrics.heightPixels,
                                            200
                                        )
                                    )
                                    showLoading(false)
                                }
                            }

                            result.onFailure {
                                STATUSMAP = false
                                binding?.apply {
                                    tvStatusData.visibility = View.VISIBLE
                                    rvMap.visibility = View.GONE
                                }
                                snackBar(getString(R.string.failed_to_get_data))
                                showLoading(false)
                            }
                        }
                    }
                }
            }
        } else {
            showLoading(false)
            snackBar(getString(R.string.message_gps))
        }
    }

    //Variable permission panggilan
    private val requestPermissionCall =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                snackBar(getString(R.string.message_granted))
            } else {
                snackBar(getString(R.string.message_denied))
            }
        }

    private fun setupCall(tel: String) {
        //Untuk meminta permission dan untuk menelepon
        if (ContextCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            intentCall(tel)
        } else {
            requestPermissionCall.launch(Manifest.permission.CALL_PHONE)
        }
    }

    private fun intentCall(tel: String) {
        //Membuat alert dialog untuk konfirmasi
        val prefManager =
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(requireContext())
        val shouldConfirm =
            prefManager.getBoolean(requireContext().getString(R.string.pref_key_confirm), false)
        if (shouldConfirm) {
            AlertDialog.Builder(requireContext()).apply {
                setTitle(getString(R.string.alert_title))
                setMessage(getString(R.string.alert_message))
                setNegativeButton(getString(R.string.no), null)
                setPositiveButton(getString(R.string.yes)) { _, _ ->
                    val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$tel"))
                    startActivity(intent)
                }
                show()
            }
        } else {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$tel"))
            startActivity(intent)
        }
    }

    private fun showRecycleView(mapResults: List<DataItem?>) {
        val adapter = NearbyAdapter()
        adapter.submitList(mapResults)
        binding?.rvMap?.adapter = adapter

        adapter.setOnItemClickCallback(object : NearbyAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataItem) {
                setupCall(data.telepon!!)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.progressBar?.visibility = View.VISIBLE
            binding?.darkOverlayView?.visibility = View.VISIBLE
        } else {
            binding?.progressBar?.visibility = View.GONE
            binding?.darkOverlayView?.visibility = View.GONE
        }
    }

    private fun snackBar(message: String) {
        Snackbar.make(binding!!.loMain, message, Snackbar.LENGTH_LONG).show()
    }
}