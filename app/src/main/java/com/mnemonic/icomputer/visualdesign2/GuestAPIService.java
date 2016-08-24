package com.mnemonic.icomputer.visualdesign2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by iComputer on 23-08-2016.
 */
public interface GuestAPIService {
    @GET("/api/people")
    Call<List<Guest>> getMyJSON();
}
