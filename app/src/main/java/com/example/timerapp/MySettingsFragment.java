package com.example.timerapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;
import androidx.fragment.app.Fragment;

public class MySettingsFragment extends PreferenceFragmentCompat {

    private IDialogUpdate update_UI;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        update_UI = (IDialogUpdate) context;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.main_preference, rootKey);

        SwitchPreferenceCompat set_dark_mode = (SwitchPreferenceCompat)findPreference("set_night_mode");
        set_dark_mode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ((boolean)newValue)
                {
                    AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_YES);
                    //Toast.makeText(requireActivity().getBaseContext(), "dark mode", Toast.LENGTH_LONG).show();
                }
                else
                {
                    //Toast.makeText(requireActivity().getBaseContext(), "classic mode", Toast.LENGTH_LONG).show();
                    AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_NO);
                }
                return true;
            }
        });

        ListPreference set_language = (ListPreference)findPreference("set_language");
        set_language.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String local_val = (String)newValue;
                if (local_val.equals("1"))
                {
                    Toast.makeText(requireActivity().getBaseContext(), "English", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(requireActivity().getBaseContext(), "Russian", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        ListPreference set_font_size = (ListPreference)findPreference("set_font_size");
        set_font_size.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String local_val = (String)newValue;
                if (local_val.equals("1"))
                {
                    update_UI.font_size_update("Small");
                    Toast.makeText(requireActivity().getBaseContext(), "Small", Toast.LENGTH_LONG).show();
                }
                else
                {
                    update_UI.font_size_update("Large");
                    Toast.makeText(requireActivity().getBaseContext(), "Large", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

    }
}
