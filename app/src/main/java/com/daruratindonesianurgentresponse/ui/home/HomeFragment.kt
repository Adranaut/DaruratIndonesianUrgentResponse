package com.daruratindonesianurgentresponse.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.daruratindonesianurgentresponse.R
import com.daruratindonesianurgentresponse.data.response.CallCenter
import com.daruratindonesianurgentresponse.databinding.FragmentHomeBinding
import com.daruratindonesianurgentresponse.utils.ADDRESS
import com.daruratindonesianurgentresponse.utils.STATUS
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val list = ArrayList<CallCenter>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        showLoading(false)

        //Inisialisasi lokasi terkini
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        //Cek lokasi terkini apakah sudah pernah tampil
        if (STATUS) {
            binding.apply {
                loStatus.visibility = View.GONE
                loAddress.visibility = View.VISIBLE
                tvAddress.text = ADDRESS
            }
        }

        //Membuat list untuk call center
        list.addAll(getList())
        binding.rvCallCenter.layoutManager = LinearLayoutManager(requireContext())
        showRecyclerView()

        //Button status
        binding.btnStatus.setOnClickListener {
            getMyLocation()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getList(): ArrayList<CallCenter> {
        val serviceName = resources.getStringArray(R.array.service_name)
        val serviceNumber = resources.getStringArray(R.array.service_number)
        val list = ArrayList<CallCenter>()
        for (i in serviceName.indices) {
            val callCenter = CallCenter(serviceName[i], serviceNumber[i])
            list.add(callCenter)
        }
        return list
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

    //Variable permission lokasi
    private val requestPermissionLocation =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val locationPermissionGranted =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false ||
                        permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            if (locationPermissionGranted) {
                //Akses lokasi yang tepat atau perkiraan diberikan
                getMyLocation()
                snackBar(getString(R.string.message_granted))
            } else {
                //Tidak ada akses lokasi yang diberikan.
                snackBar(getString(R.string.message_denied))
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    @Suppress("DEPRECATION")
    private fun fusedLocation(location: Location) {
        //Jika lokasi terkini ditemukan
        val geoCode = Geocoder(requireContext(), Locale.getDefault())
        val address = geoCode.getFromLocation(location.latitude, location.longitude, 3)
        if (address != null) {
            ADDRESS = address[2].getAddressLine(0)
            STATUS = true
            binding.apply {
                loStatus.visibility = View.GONE
                loAddress.visibility = View.VISIBLE
                btnStatus.text = getString(R.string.get)
                tvStatus.text = getString(R.string.get_current_location)
                tvAddress.text = ADDRESS
            }
        } else {
            //Jika lokasi terkini tidak ditemukan
            ADDRESS = ""
            STATUS = false
            binding.apply {
                loStatus.visibility = View.VISIBLE
                loAddress.visibility = View.GONE
                btnStatus.text = getString(R.string.try_again)
                tvStatus.text = getString(R.string.something_wrong)
            }
        }
        showLoading(false)
    }

    private fun getMyLocation() {
        showLoading(true)
        viewLifecycleOwner.lifecycleScope.launch {
            // Delay untuk 3 detik
            delay(3000)

            val activity = requireActivity()
            if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
                checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            ) {
                fusedLocationClient.lastLocation.addOnSuccessListener(activity) { location ->
                    if (location != null) {
                        fusedLocation(location)
                    } else {
                        showLoading(false)
                        snackBar(getString(R.string.message_gps))
                    }
                }
            } else {
                requestPermissionLocation.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
                showLoading(false)
            }
        }
    }

    //Membuat snack bar
    private fun snackBar(message: String) {
        Snackbar.make(binding.loMain, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showRecyclerView() {
        val adapter = CallCenterAdapter()
        adapter.submitList(list)
        binding.rvCallCenter.adapter = adapter

        adapter.setOnItemClickCallback(object : CallCenterAdapter.OnItemClickCallback {
            override fun onItemClicked(data: CallCenter) {
                setupCall(data.serviceNumber!!)
            }
        })
    }

    //Mengatur keadaan ketika terjadi loading data
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.darkOverlayView.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.darkOverlayView.visibility = View.GONE
        }
    }
}