package com.example.timerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    ArrayList<TimerSet> timers = new ArrayList();
    TimersListAdapter adapter;
    ListView timersList;

    TextView text;
    ImageView image_add;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView) findViewById(R.id.test_text);
        timersList = (ListView) findViewById(R.id.list_timer_sets);
        image_add = (ImageView) findViewById(R.id.image_add_timer);

        if(timers.size()==0){
            setTimers();
        }

        adapter = new TimersListAdapter(this, R.layout.item_timer, timers);
        timersList.setAdapter(adapter);
        timersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Intent i = new Intent(MainActivity.this, TimerActivity.class);
                TimerSet local_timerSet = timers.get(position);
                i.putExtra(TimerSet.class.getSimpleName(), local_timerSet);
                startActivity(i);
            }
        });

        image_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CreateUpdateActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        timers.removeAll(timers);
        setTimers();
        TimersListAdapter new_adapter = new TimersListAdapter(this, R.layout.item_timer, timers);
        timersList.setAdapter(new_adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.app_settings) {
            Intent i = new Intent(MainActivity.this, ActivitySettings.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setTimers()
    {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS timers (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                   "title TEXT, warm_up INTEGER, workout INTEGER , rest INTEGER, cooldown INTEGER, cycle INTEGER, set_t INTEGER, " +
                   "r_t INTEGER, g_t INTEGER, b_t INTEGER)");

        Cursor query = db.rawQuery("SELECT * FROM timers;", null);
        TextView textView = (TextView) findViewById(R.id.test_text);
        textView.setText("");
        if(query.moveToFirst()){
            do{
                int id = query.getInt(0);
                String name = query.getString(1);
                int warm_up = query.getInt(2);
                int workout = query.getInt(3);
                int rest = query.getInt(4);
                int cooldown = query.getInt(5);
                int cycle = query.getInt(6);
                int set_t = query.getInt(7);
                int r_t = query.getInt(8);
                int g_t = query.getInt(9);
                int b_t = query.getInt(10);
                TimerSet timer = new TimerSet(id, warm_up, workout, rest, cooldown, cycle, set_t);
                timer.setColor(new int[] {r_t, g_t, b_t});
                timer.setTitle(name);
                timers.add(timer);
            }
            while(query.moveToNext());
        }
        query.close();
        db.close();
    }

    private void dropTimers()
    {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        db.execSQL("DROP TABLE IF EXISTS timers");
        db.close();
    }

}