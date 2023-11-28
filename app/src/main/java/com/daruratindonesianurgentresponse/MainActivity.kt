package com.daruratindonesianurgentresponse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.daruratindonesianurgentresponse.databinding.ActivityMainBinding
import com.daruratindonesianurgentresponse.ui.chatbot.ChatBotFragment
import com.daruratindonesianurgentresponse.ui.home.HomeFragment
import com.daruratindonesianurgentresponse.ui.map.MapFragment
import com.daruratindonesianurgentresponse.ui.setting.SettingFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())
        binding.bottomNavBar.setItemSelected(R.id.bottom_home)
        bottomNavSetup()
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
}