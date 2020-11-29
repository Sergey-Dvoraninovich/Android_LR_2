package com.example.timerapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TimerSet implements Serializable {

    private int id;
    private String timerSet_text;

    public int warm_up_time;
    private int workout_time;
    private int rest_time;
    private int cooldown_time;
    private int cycle_count; //work and rest repeat
    private int set_count; //3 types repeat

    private int[] colors;

    private ArrayList<ItemSet> item_list = new ArrayList();
    private ArrayList<ItemSet> simple_item_list = new ArrayList();



    public TimerSet(int warm_up_time, int workout_time, int rest_time, int cooldown_time, int cycle_count, int set_count)
    {
        this.warm_up_time = warm_up_time;
        this.workout_time = workout_time;
        this.rest_time = rest_time;
        this.cooldown_time = cooldown_time;
        this.cycle_count = cycle_count;
        this.set_count = set_count;
        updateList();
        simpleupdateList();
        this.colors = new int[] {0, 0, 0};
    }

    public TimerSet(int id, int warm_up_time, int workout_time, int rest_time, int cooldown_time, int cycle_count, int set_count)
    {
        this.id = id;
        this.warm_up_time = warm_up_time;
        this.workout_time = workout_time;
        this.rest_time = rest_time;
        this.cooldown_time = cooldown_time;
        this.cycle_count = cycle_count;
        this.set_count = set_count;
        updateList();
        simpleupdateList();
    }

    private void updateList()
    {
        item_list.add(new ItemSet("warm_up", this.warm_up_time));
        for (int i = 0; i < set_count; i++)
        {
            for (int j = 0; j < cycle_count; j++)
            {
                item_list.add(new ItemSet("workout", this.workout_time));
                item_list.add(new ItemSet("rest", this.rest_time));
            }
            item_list.add(new ItemSet("cooldown", this.cooldown_time));
        }
    }

    public int getTotalTime()
    {
        int time = 0;
        time += this.warm_up_time;
        for (int i = 0; i < set_count; i++)
        {
            for (int j = 0; j < cycle_count; j++)
            {
                time += workout_time;
                time += rest_time;
            }
            time += cooldown_time;
        }
        return time;
    }

    private void simpleupdateList()
    {
        simple_item_list.add(new ItemSet("warm_up", this.warm_up_time));
        simple_item_list.add(new ItemSet("workout", this.workout_time));
        simple_item_list.add(new ItemSet("rest", this.rest_time));
        simple_item_list.add(new ItemSet("cooldown", this.cooldown_time));
        simple_item_list.add(new ItemSet("cycle count", this.cycle_count));
        simple_item_list.add(new ItemSet("set count", this.set_count));
    }

    public ArrayList<ItemSet> getList()
    {
        return this.item_list;
    }

    public ArrayList<ItemSet> getsimpleList()
    {
        return this.simple_item_list;
    }

    public void setTitle(String timerSet_text)
    {
        this.timerSet_text = timerSet_text;
    }

    public String getTitle()
    {
        return this.timerSet_text;
    }

    public void setColor(int[] colors)
    {
        this.colors = colors;
    }

    public int[] getColor()
    {
        return this.colors;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return this.id;
    }
}
