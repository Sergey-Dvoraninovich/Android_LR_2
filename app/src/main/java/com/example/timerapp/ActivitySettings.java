package com.example.timerapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.ContentView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;


public class ActivitySettings extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(R.style.DarkAppTheme);
        SharedPreferences sp = getSharedPreferences("set_night_mode", Context.MODE_PRIVATE);
        if ((boolean)sp.getBoolean("set_night_mode", false))
        {
            this.setTheme(R.style.DarkAppTheme);
        }
        else {
            this.setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_settings);
        //getSupportFragmentManager()
        //        .beginTransaction()
        //        .replace(R.id.settings_fragment, new MySettingsFragment())
        //        .commit();
    }


}