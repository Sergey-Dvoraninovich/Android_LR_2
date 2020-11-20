package com.example.timerapp;

import java.io.Serializable;

public class ItemSet implements Serializable {
    public String name;
    public String image_name;
    public Integer length;
    public ItemSet(String name, int length)
    {
        this.name = name;
        this.length = length;
        this.image_name = null;
    }
    public ItemSet(String name, String image_name, Integer length)
    {
        this.name = name;
        this.length = length;
        this.image_name = image_name;
    }
}
