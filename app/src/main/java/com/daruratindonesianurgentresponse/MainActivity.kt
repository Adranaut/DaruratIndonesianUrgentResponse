package com.daruratindonesianurgentresponse

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.daruratindonesianurgentresponse.databinding.ActivityMainBinding
import com.daruratindonesianurgentresponse.ui.chatbot.ChatBotFragment
import com.daruratindonesianurgentresponse.ui.home.HomeFragment
import com.daruratindonesianurgentresponse.ui.map.MapFragment
import com.daruratindonesianurgentresponse.ui.setting.SettingFragment
import com.daruratindonesianurgentresponse.utils.GpsStatusListener
import com.daruratindonesianurgentresponse.utils.TurnOnGps
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient : FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        replaceFragment(HomeFragment())
        binding.bottomNavBar.setItemSelected(R.id.bottom_home)
        bottomNavSetup()
        getMyLocation()

        val gpsStatusListener = GpsStatusListener(this)
        val turnOnGps = TurnOnGps(this)
        var isGpsStatusChanged: Boolean? = null
        gpsStatusListener.observe(this) { isGpsOn ->
            if (isGpsStatusChanged == null) {
                if (!isGpsOn) {
                    //turn on the gps
                    getMyLocation()
                    turnOnGps.startGps(resultLauncher)
                }
                isGpsStatusChanged = isGpsOn
            } else {
                if (isGpsStatusChanged != isGpsOn) {
                    if (!isGpsOn) {
                        //turn on gps
                        getMyLocation()
                        turnOnGps.startGps(resultLauncher)
                    }
                    isGpsStatusChanged = isGpsOn
                }
            }
        }
    }

    private fun bottomNavSetup() {
        //Untuk Menu Selected Listener atau untuk Navigasi Fragment
        binding.bottomNavBar.setOnItemSelectedListener {
            when (it) {
                R.id.bottom_home -> replaceFragment(HomeFragment())
                R.id.bottom_map  -> replaceFragment(MapFragment())
                R.id.bottom_chatbot  -> replaceFragment(ChatBotFragment())
                R.id.bottom_setting  -> replaceFragment(SettingFragment())
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        //Untuk Replace Fragment Ke Layout
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            snackBar("GPS is on")
        } else if (activityResult.resultCode == RESULT_CANCELED) {
            snackBar("Request is canceled")
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

    @Suppress("DEPRECATION")
    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val locationRequest = LocationRequest.create()
            locationRequest.interval = 100
            locationRequest.fastestInterval = 50
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

            val locationCallback: LocationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    if (locationResult != null) {
                        if (locationResult == null) {
                            return
                        }
//                        LATITUDE = locationResult.lastLocation!!.latitude
//                        LONGITUDE = locationResult.lastLocation!!.longitude
                    }
                }
            }
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun snackBar(message: String) {
        Snackbar.make(binding.frameLayout, message, Snackbar.LENGTH_LONG).show()
    }
}