package com.mnemonic.icomputer.visualdesign2;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by iComputer on 15-07-2016.
 */
public class Event {
    public int image;
    public String name;
    public Date date;
    public String tags;
    public String resume;

    public Event (int image, String name, String date) {
        this.image = image;
        this.name = name;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date result = new Date();
        try {
            result =  df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.date = result;
    }

    public Event (int image, String name, String date, String tags, String resume) {
        this.image = image;
        this.name = name;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date result = new Date();
        try {
            result =  df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.date = result;
        this.tags = tags;
        this.resume = resume;
    }
}
