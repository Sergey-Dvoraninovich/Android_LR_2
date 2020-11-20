package com.example.timerapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

public class MySettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.main_preference, rootKey);

        SwitchPreferenceCompat set_dark_mode = (SwitchPreferenceCompat)findPreference("set_night_mode");
        set_dark_mode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ((boolean)newValue)
                {
                    requireActivity().setTheme(R.style.DarkAppTheme);
                    Toast.makeText(requireActivity().getBaseContext(), "dark mode", Toast.LENGTH_LONG).show();
                }
                else
                {
                    requireActivity().setTheme(R.style.AppTheme);
                    Toast.makeText(requireActivity().getBaseContext(), "classic mode", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

    }
}
