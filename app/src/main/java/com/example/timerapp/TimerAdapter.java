package com.example.timerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TimerAdapter  extends ArrayAdapter<ItemSet> {

    private LayoutInflater inflater;
    private int layout;
    private ArrayList<ItemSet> itemsList;
    private Context context;

    TimerAdapter(Context context, int resource, ArrayList<ItemSet> items) {
        super(context, resource, items);
        this.context = context;
        this.itemsList = items;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        final ItemSet product = itemsList.get(position);

        if(convertView==null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.text.setText(product.name + " " + product.length);

        return convertView;
    }

    private static class ViewHolder {
        final TextView text;
        ViewHolder(View view){
            text = (TextView) view.findViewById(R.id.text_show);
        }
    }

}
