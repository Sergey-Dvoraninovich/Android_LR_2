package com.example.timerapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class EditSetAdapter  extends ArrayAdapter<ItemSet> {

    private LayoutInflater inflater;
    private int layout;
    private ArrayList<ItemSet> itemsList;
    private Context context;

    EditSetAdapter(Context context, int resource, ArrayList<ItemSet> items) {
        super(context, resource, items);
        this.context = context;
        this.itemsList = items;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        final ItemSet product = itemsList.get(position);
        final Integer length = product.length;

        if(convertView==null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(product.name);

        viewHolder.number.setText(length.toString());

        viewHolder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Integer num = Integer.parseInt(viewHolder.number.getText().toString());
                    if (num > 0)
                        num--;
                    ItemSet local_item = itemsList.get(position);
                    local_item.length = num;
                    itemsList.set(position, local_item);
                    viewHolder.number.setText(num.toString());
                }
                catch (Exception e) { }
            }
        });

        viewHolder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Integer num = Integer.parseInt(viewHolder.number.getText().toString());
                    num++;
                    ItemSet local_item = itemsList.get(position);
                    local_item.length = num;
                    itemsList.set(position, local_item);
                    viewHolder.number.setText(num.toString());
                }
                catch (Exception e)
                {

                }
            }
        });



        String name = null;
        if (product.name.equals("warm_up"))
            name = "warm_up_foreground";
        if (product.name.equals("workout"))
            name = "workout_foreground";
        if (product.name.equals("rest"))
            name = "rest_foreground";
        if (product.name.equals("cooldown"))
            name = "cooldown_foreground";
        if (product.name.equals("cycle count"))
            name = "cycle_foreground";
        if (product.name.equals("set count"))
            name = "set_foreground";
        String pkgName = context.getPackageName();
        if (name != null) {
            viewHolder.name.setText(product.name);
            int resID = context.getResources().getIdentifier(name, "mipmap", pkgName);
            viewHolder.icon.setImageResource(resID);
        }

        return convertView;
    }

    private static class ViewHolder {
        final TextView name, number;
        final ImageView icon, plus, minus;
        ViewHolder(View view){
            icon = (ImageView) view.findViewById(R.id.setting_icon);
            name = (TextView) view.findViewById(R.id.name_section);
            number = (TextView) view.findViewById(R.id.num_section);
            plus = (ImageView) view.findViewById(R.id.image_plus);
            minus = (ImageView) view.findViewById(R.id.image_minus);
        }
    }

}

