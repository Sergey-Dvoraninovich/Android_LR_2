package com.example.timerapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ContentView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.SwitchPreferenceCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;




public class ActivitySettings extends AppCompatActivity implements IDialogInteraction, IDialogUpdate{

    Button delete_all_button;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_frame, new MySettingsFragment())
                .commit();


        delete_all_button = (Button) findViewById(R.id.button);

        delete_all_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DeleteAllTimersDialog dialog = new DeleteAllTimersDialog();
                Bundle args = new Bundle();
                args.putString("dialog", getResources().getString(R.string.DeleteAllTimersDialog));
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "custom");
            }
        });
    }

    @Override
    public void remove(String name) {
        Toast.makeText(this, "Will be deleted", Toast.LENGTH_SHORT).show();
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        db.execSQL("DELETE FROM timers");
        db.close();
    }

    @Override
    public void font_size_update(String name) {
        if (name.equals("Small")){
            delete_all_button.setTextSize(14);
        }
        else{
            delete_all_button.setTextSize(25);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String local_font_size = sp.getString("set_font_size", "1");
        if (local_font_size.equals("1")) {
            delete_all_button.setTextSize(14);
        }
        else {
            delete_all_button.setTextSize(25);
        }
    }
}