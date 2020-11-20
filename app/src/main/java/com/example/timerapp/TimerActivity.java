package com.example.timerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TimerActivity extends AppCompatActivity {

    private boolean is_active;

    ArrayList<ItemSet> settings = new ArrayList();
    ArrayList<ItemSet> prev_settings = new ArrayList();
    ListView settingsList;
    TimerAdapter new_adapter;
    ItemSet cur_set;

    ImageView play_stop;
    ImageView next;
    ImageView prev;
    EditText cur_time;
    EditText title;
    TextView service_text;

    MediaPlayer mediaPlayer;

    private Timer mTimer;
    private UpdateTimerTask mUpdateTimerTask;
    private UpdatePrevTimerTask mUpdatePrevTimerTask;
    private SetTimerTask mSetTimerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaPlayer = MediaPlayer.create(this, R.raw.short_sound);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.stop();
            }
        });
        setContentView(R.layout.activity_timer);

        TimerSet timerSet = new TimerSet(20, 30, 10, 40, 3, 2);
        Bundle arguments = getIntent().getExtras();
        if(arguments!=null) {
            timerSet = (TimerSet) arguments.getSerializable(TimerSet.class.getSimpleName());
        }
        settings = timerSet.getList();
        cur_set = settings.get(0);
        prev_settings.add(settings.get(0));
        settings.remove(0);


        cur_time  = (EditText) findViewById(R.id.cur_time);
        cur_time.setText(cur_set.length.toString());
        title = (EditText) findViewById(R.id.text_title);
        title.setText(cur_set.name);
        service_text = (TextView) findViewById(R.id.service_text);
        is_active = false;


        play_stop = (ImageView) findViewById(R.id.stop_play_image);
        play_stop.setOnClickListener(mImageClickListener);
        next = (ImageView) findViewById(R.id.next_image);
        next.setOnClickListener(mNextClickListener);
        prev = (ImageView) findViewById(R.id.prev_image);
        prev.setOnClickListener(mPrevClickListener);


        settingsList = (ListView) findViewById(R.id.list_create_update);
        new_adapter = new TimerAdapter(this, R.layout.item_show, settings);
        settingsList.setAdapter(new_adapter);

        cur_time.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                if (cur_time.getText().toString().equals("0")){
                    if (!settings.isEmpty())
                    {
                        mTimer.cancel();
                        mTimer.purge();
                        mTimer = new Timer();
                        mSetTimerTask = new SetTimerTask();
                        mUpdateTimerTask = new UpdateTimerTask();
                        mTimer.schedule(mUpdateTimerTask, 1000);
                        mTimer.schedule(mSetTimerTask, 2000, 1000);
                    }
                    else
                    {
                        setFinish();
                    }
                    mediaStart();
                    mediaPlayer.start();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }

    private View.OnClickListener mImageClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (service_text.getText().toString().equals(getString(R.string.Paused))) {
                play_stop.setImageResource(R.mipmap.stop_foreground);
                if (!cur_time.getText().toString().equals(getString(R.string.Finish)))
                    service_text.setText(getString(R.string.Runnig));


                mTimer = new Timer();
                mSetTimerTask = new SetTimerTask();
                mTimer.schedule(mSetTimerTask, 1000, 1000);
            }
            else {
                play_stop.setImageResource(R.mipmap.start_foreground);
                if (!cur_time.getText().toString().equals(getString(R.string.Finish)))
                   service_text.setText(getString(R.string.Paused));

                mTimer.cancel();
                mTimer.purge();
            }
        }
    };

    private View.OnClickListener mNextClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!settings.isEmpty()) {
                mTimer.cancel();
                mTimer.purge();
                mTimer = new Timer();
                mSetTimerTask = new SetTimerTask();
                mUpdateTimerTask = new UpdateTimerTask();
                mTimer.schedule(mUpdateTimerTask, 50);
                mTimer.schedule(mSetTimerTask, 1000, 1000);
            }
            else
            {
                setFinish();
            }
        }
    };

    private View.OnClickListener mPrevClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = new Timer();
            mSetTimerTask = new SetTimerTask();
            mUpdatePrevTimerTask = new UpdatePrevTimerTask();
            mTimer.schedule(mUpdatePrevTimerTask, 50);
            mTimer.schedule(mSetTimerTask, 1000, 1000);
        }
    };

    class SetTimerTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    String local_string = cur_time.getText().toString();
                    try {
                        Integer num = Integer.parseInt(local_string);
                        if (num > 0) {
                            num--;
                            local_string = num.toString();
                        }
                    }
                    catch (Exception e)
                    {

                    }
                    cur_time.setText(local_string);
                }
            });
        }
    }

    class UpdateTimerTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    prev_settings.add(0, cur_set);
                    cur_set = settings.get(0);
                    settings.remove(0);
                    new_adapter.notifyDataSetChanged();
                    cur_time.setText(cur_set.length.toString());
                    title.setText(cur_set.name);
                }
            });
        }
    }

    class UpdatePrevTimerTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (!prev_settings.isEmpty()) {
                        if (cur_set.name != "warm_up")
                            settings.add(0, cur_set);
                        cur_set = prev_settings.get(0);
                        prev_settings.remove(0);
                    }
                    new_adapter.notifyDataSetChanged();
                    cur_time.setText(cur_set.length.toString());
                    title.setText(cur_set.name);
                }
            });
        }
    }

    private void setFinish()
    {
        cur_time.setTextSize(100);
        cur_time.setText(getString(R.string.Finish));
        title.setText("");
        service_text.setText("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    private void mediaStart()
    {
        mediaPlayer = MediaPlayer.create(this, R.raw.short_sound);
    }
}
