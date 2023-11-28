package com.daruratindonesianurgentresponse.ui.setting

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.daruratindonesianurgentresponse.R

class SettingFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}