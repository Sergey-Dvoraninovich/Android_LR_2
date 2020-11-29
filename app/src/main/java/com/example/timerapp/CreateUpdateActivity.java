package com.example.timerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class CreateUpdateActivity extends AppCompatActivity {

    ArrayList<ItemSet> settings = new ArrayList();
    EditSetAdapter adapter;
    boolean isEdit;

    ListView settingsList;
    TimerSet timerSet;
    Button button_save;
    EditText get_title;
    TextView string_title;

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_update);

        button_save = (Button) findViewById(R.id.button_save);
        get_title = (EditText) findViewById(R.id.text_title);
        string_title = (TextView) findViewById(R.id.set_title);

        timerSet = new TimerSet(20, 30, 10, 40, 3, 2);
        isEdit = false;
        Bundle arguments = getIntent().getExtras();
        if(arguments!=null) {
            timerSet = (TimerSet) arguments.getSerializable(TimerSet.class.getSimpleName());
            isEdit = true;
        }

        get_title.setText(timerSet.getTitle());
        settings = timerSet.getsimpleList();
        settingsList = (ListView) findViewById(R.id.list_update);
        adapter = new EditSetAdapter(this, R.layout.item_add, settings);
        settingsList.setAdapter(adapter);

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
                db.execSQL("CREATE TABLE IF NOT EXISTS timers (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                           "title TEXT, warm_up INTEGER, workout INTEGER , rest INTEGER, cooldown INTEGER, cycle INTEGER, " +
                           "set_t INTEGER, r_t INTEGER, g_t INTEGER, b_t INTEGER)");
                String v0 = get_title.getText().toString();
                String v1 = settings.get(0).length.toString();
                String v2 = settings.get(1).length.toString();
                String v3 = settings.get(2).length.toString();
                String v4 = settings.get(3).length.toString();
                String v5 = settings.get(4).length.toString();
                String v6 = settings.get(5).length.toString();
                String v7, v8, v9;
                if (!isEdit) {
                    Random random = new Random();
                    v7 = Integer.toString(random.nextInt(256) - 1);
                    v8 = Integer.toString(random.nextInt(256) - 1);
                    v9 = Integer.toString(random.nextInt(256) - 1);
                    db.execSQL("INSERT INTO timers (title, warm_up, workout, rest, cooldown, cycle, set_t, r_t, g_t, b_t) " +
                               "VALUES ('" + v0 + "', " + v1 + ", " + v2 + ", " + v3 + ", " + v4 + ", " + v5 + ", " + v6 + ", " +
                               v7 + ", " + v8 + ", " + v9 +" );");
                }
                else
                {
                    String id = Integer.toString(timerSet.getId());
                    v7 = Integer.toString(timerSet.getColor()[0]);
                    v8 = Integer.toString(timerSet.getColor()[1]);
                    v9 = Integer.toString(timerSet.getColor()[2]);
                    db.execSQL("UPDATE timers SET " + "title = '" + v0 + "'," +
                               "warm_up = " + v1 + ", workout = " + v2 + ", rest = " + v3 + ", cooldown = " + v4 + ", " +
                               "cycle = " + v5 + ", set_t = " + v6 + ", " +
                               "r_t = " + v7 + ", g_t = " + v8 + ", b_t = " + v9 +"  WHERE id = " + id + " ;");
                }
                db.close();
                Toast.makeText(CreateUpdateActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            }

        });
    }
}