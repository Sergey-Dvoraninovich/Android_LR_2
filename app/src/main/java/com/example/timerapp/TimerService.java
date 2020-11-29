package com.example.timerapp;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TimerService extends Service {

    private Timer mTimer;
    private CountTimerTask mCountTimerTask;
    private ArrayList<ItemSet> items;

    private TimerSet timer;
    private int count;
    private int num;
    Service s_this;

    public TimerService() {
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        s_this = this;
        Bundle arguments = intent.getExtras();
        if(arguments!=null) {
            timer = (TimerSet) arguments.getSerializable(TimerActivity.PARAM_TIMER);
            count = (int) arguments.getSerializable(TimerActivity.PARAM_POS);
            num = (int) arguments.getSerializable(TimerActivity.PARAM_VAL);
            ItemSet item = (ItemSet) arguments.getSerializable(TimerActivity.PARAM_WARM_UP);
            items = timer.getList();
            items.add(0, item);
        }
        mTimer = new Timer();
        mCountTimerTask = new CountTimerTask();
        mTimer.schedule(mCountTimerTask, 1000, 1000);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (mTimer != null)
        {
            mTimer.purge();
            mTimer.cancel();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    class CountTimerTask extends TimerTask
    {
        @Override
        public void run() {
            if (num != 0)
            {
                num--;
                Intent intent = new Intent(TimerActivity.BROADCAST_ACTION);
                intent.putExtra(TimerActivity.PARAM_ID_TIMER_SET, timer.getId());
                intent.putExtra(TimerActivity.PARAM_POS, count);
                intent.putExtra(TimerActivity.PARAM_VAL, num);
                sendBroadcast(intent);
            }
            else
            {
                if (count < timer.getList().size() - 1)
                {
                    count++;
                    num = items.get(count).length;
                    mTimer.cancel();
                    mTimer.purge();
                    mTimer =  new Timer();
                    mCountTimerTask = new CountTimerTask();
                    mTimer.schedule(mCountTimerTask, 1000, 1000);

                }
                else
                {
                    mTimer.cancel();
                    mTimer.purge();
                    stopSelf();
                }
            }

        }

    }

}
