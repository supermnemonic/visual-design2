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
public class Guest extends RealmObject {
    @PrimaryKey
    private long id;
    private String name;
    private Date birthdate;

    public Guest(){}

    public Guest (long id, String name, Date birthdate) {
        this.id = id;
        this.name = name;
        this.birthdate = birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getBirthdate() {
        return birthdate;
    }

}
