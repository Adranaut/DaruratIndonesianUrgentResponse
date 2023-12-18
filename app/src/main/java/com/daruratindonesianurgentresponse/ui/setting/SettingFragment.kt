package com.daruratindonesianurgentresponse.ui.setting

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.daruratindonesianurgentresponse.R
import com.daruratindonesianurgentresponse.utils.DarkMode

class SettingFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        //Untuk mengganti bahasa
        val languagePreference = findPreference<Preference>(getString(R.string.pref_key_language))
        languagePreference?.setOnPreferenceClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            true
        }

        //Untuk memilih tema
        val themePreference = findPreference<ListPreference>(getString(R.string.pref_key_dark))
        themePreference?.setOnPreferenceChangeListener { _, newValue ->
            when(newValue){
                "follow_system" -> updateTheme(DarkMode.FOLLOW_SYSTEM.value)
                "on" -> updateTheme(DarkMode.ON.value)
                "off" -> updateTheme(DarkMode.OFF.value)
            }
            true
        }
    }

    private fun updateTheme(mode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(mode)
        requireActivity().recreate()
        return true
    }
}