package com.mnemonic.icomputer.visualdesign2;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by iComputer on 15-07-2016.
 */
public class Event extends RealmObject {
    @PrimaryKey
    private long id;
    private String name;
    private Date date;
    private int image;
    private String tags;
    private String resume;
    private int idList;
    private double lat;
    private double lng;

    public Event() {

    }

    public Event(long id, String name, Date date, int image, String tags, String resume, double lat, double lng) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.image = image;
        this.tags = tags;
        this.resume = resume;
        this.lat = lat;
        this.lng = lng;
    }

    public static Date createDate(String stringDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date result = new Date();
        try {
            result = df.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getIdList() {
        return idList;
    }

    public void setIdList(int idList) {
        this.idList = idList;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
