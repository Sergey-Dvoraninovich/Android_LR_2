package com.example.timerapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class TimersListAdapter  extends ArrayAdapter<TimerSet> {

    private LayoutInflater inflater;
    private int layout;
    private ArrayList<TimerSet> timerList;
    private Context context;
    private TimerSet cur_timer;

    TimersListAdapter(Context context, int resource, ArrayList<TimerSet> products) {
        super(context, resource, products);
        this.context = context;
        this.timerList = products;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {

        final ViewHolder viewHolder;
        cur_timer = timerList.get(position);
        final View set_convView = convertView;

        if(convertView==null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
            convertView.setBackgroundResource(R.drawable.round_corners);
            GradientDrawable drawable = (GradientDrawable) convertView.getBackground();
            int[] colors = cur_timer.getColor();
            drawable.setColor(Color.rgb(colors[0], colors[1], colors[2]));
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(cur_timer.getTitle());
        ArrayList<ItemSet> values = cur_timer.getsimpleList();
        ItemSet item = values.get(0);
        viewHolder.text_1.setText(item.name + "   " + item.length.toString());
        item = values.get(1);
        viewHolder.text_2.setText(item.name + "   " + item.length.toString());
        item = values.get(2);
        viewHolder.text_3.setText(item.name + "   " + item.length.toString());
        item = values.get(3);
        viewHolder.text_4.setText(item.name + "   " + item.length.toString());
        item = values.get(4);
        viewHolder.text_5.setText(item.name + "   " + item.length.toString());
        item = values.get(5);
        viewHolder.text_6.setText(item.name + "   " + item.length.toString());
        viewHolder.text_7.setText(context.getString(R.string.TotalTime) + "   " + Integer.toString(cur_timer.getTotalTime()));



        viewHolder.image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimerSet local_timer = timerList.get(position);
                String id = Integer.toString(local_timer.getId());
                timerList.remove(position);
                notifyDataSetChanged();
                SQLiteDatabase db = context.openOrCreateDatabase("app.db", MODE_PRIVATE, null);
                db.execSQL("DELETE FROM timers WHERE id = " + id + ";");
                db.close();
            }
        });

        viewHolder.image_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TimersListAdapter.this.getContext(), CreateUpdateActivity.class);
                TimerSet local_timerSet = timerList.get(position);
                i.putExtra(TimerSet.class.getSimpleName(), local_timerSet);
                v.getContext().startActivity(i);
            }
        });

        return convertView;
    }

    private class ViewHolder {
        final TextView title, text_1, text_2, text_3, text_4, text_5, text_6, text_7;
        final ImageView image_delete, image_edit;
        ViewHolder(View view){
            image_delete = (ImageView) view.findViewById(R.id.timer_set_delege_image);
            image_edit = (ImageView) view.findViewById(R.id.timer_set_update_image);
            title = (TextView) view.findViewById(R.id.timer_title);
            text_1 = (TextView) view.findViewById(R.id.timer_text_1);
            text_2 = (TextView) view.findViewById(R.id.timer_text_2);
            text_3 = (TextView) view.findViewById(R.id.timer_text_3);
            text_4 = (TextView) view.findViewById(R.id.timer_text_4);
            text_5 = (TextView) view.findViewById(R.id.timer_text_5);
            text_6 = (TextView) view.findViewById(R.id.timer_text_6);
            text_7 = (TextView) view.findViewById(R.id.timer_text_7);
        }
    }
}
