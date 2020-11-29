package com.example.timerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TimerActivity extends AppCompatActivity {


    public final static String PARAM_TIMER = TimerSet.class.getSimpleName();
    public final static String PARAM_POS = "position";
    public final static String PARAM_VAL = "value";
    public final static String PARAM_WARM_UP = "warm_up";
    public final static String PARAM_ID_TIMER_SET = "id_timer_set";
    public final static String BROADCAST_ACTION = "com.example.timerapp.s34578servicebackbroadcast";

    TimerReceiver myReceiver;

    private boolean is_active;
    private String status;

    ArrayList<ItemSet> settings = new ArrayList();
    ArrayList<ItemSet> prev_settings = new ArrayList();
    ListView settingsList;
    TimerAdapter new_adapter;
    ItemSet cur_set;
    int cur_pos;
    String set_cur_pos;
    int cur_val;
    String set_cur_val;
    TimerSet timerSet;

    ImageView play_stop;
    ImageView next;
    ImageView prev;
    public EditText cur_time;
    EditText title;
    TextView service_text;

    MediaPlayer mediaPlayer;

    private Timer mTimer;
    private UpdateTimerTask mUpdateTimerTask;
    private UpdatePrevTimerTask mUpdatePrevTimerTask;
    private SetTimerTask mSetTimerTask;
    BroadcastReceiver br;


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
        status = null;
        setContentView(R.layout.activity_timer);

        timerSet = new TimerSet(20, 30, 10, 40, 3, 2);
        Bundle arguments = getIntent().getExtras();
        if(arguments!=null) {
            timerSet = (TimerSet) arguments.getSerializable(TimerSet.class.getSimpleName());
        }
        settings = timerSet.getList();
        cur_set = settings.get(0);
        cur_pos = 0;
        cur_val = cur_set.length;
        prev_settings.add(settings.get(0));
        settings.remove(0);


        cur_time  = (EditText) findViewById(R.id.cur_time);
        Log.i("###########################################", "OK");
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

        myReceiver = new TimerReceiver(new Handler());
        registerReceiver(myReceiver, new IntentFilter(BROADCAST_ACTION));

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
                    Log.i("TimerActivity_TimerActivity_TimerActivity_TimerActivity_TimerActivity",
                            cur_set.name + "___" + Integer.toString(cur_set.length));
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
                        cur_val = num;
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
                    cur_pos++;
                    cur_val = cur_set.length;
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
                        cur_pos--;
                        cur_val = cur_set.length;
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

    @Override
    public void onPause(){
        super.onPause();
        Intent serviceIntent = new Intent(this, TimerService.class);
        ArrayList<ItemSet> local_items;
        serviceIntent.putExtra(PARAM_TIMER, timerSet);
        serviceIntent.putExtra(PARAM_POS, cur_pos);
        serviceIntent.putExtra(PARAM_VAL, cur_val);
        serviceIntent.putExtra(PARAM_WARM_UP, new ItemSet("warm_up", timerSet.warm_up_time));
        startService(serviceIntent);
        if (mTimer != null) {
            mTimer.purge();
            mTimer.cancel();
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        status = "not used";
        this.finish();
    }

    private void mediaStart()
    {
        mediaPlayer = MediaPlayer.create(this, R.raw.short_sound);
    }

    private class TimerReceiver extends BroadcastReceiver {

        private final Handler handler;

        public TimerReceiver(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void onReceive(final Context context, Intent intent) {
            if (status == null) {
                final int id = intent.getIntExtra(PARAM_ID_TIMER_SET, 0);
                if (id == timerSet.getId()) {
                    final int pos = intent.getIntExtra(PARAM_POS, 0);
                    final int val = intent.getIntExtra(PARAM_VAL, 0);
                    Toast.makeText(context, "pos " + Integer.toString(pos) + "\nval" + Integer.toString(val), Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < pos; i++) {
                        prev_settings.add(0, cur_set);
                        cur_set = settings.get(0);
                        cur_pos++;
                        cur_val = cur_set.length;
                        settings.remove(0);
                    }
                    cur_val = val;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (cur_time != null)
                                cur_time.setText(Integer.toString(val));
                            if (title != null)
                                title.setText(cur_set.name);
                            if (new_adapter != null)
                                new_adapter.notifyDataSetChanged();
                        }
                    });
                }
                status = "recieved";
                stopService(new Intent(TimerActivity.this, TimerService.class));
            }
        }
    }
}

