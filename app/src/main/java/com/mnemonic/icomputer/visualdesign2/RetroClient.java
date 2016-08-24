package com.mnemonic.icomputer.visualdesign2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by iComputer on 23-08-2016.
 */
public class RetroClient {

    private static final String ROOT_URL = "http://dry-sierra-6832.herokuapp.com";

    private static Retrofit getRetrofitInstance(){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();
        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static GuestAPIService getGuestAPIService() {
        return getRetrofitInstance().create(GuestAPIService.class);
    }
}
