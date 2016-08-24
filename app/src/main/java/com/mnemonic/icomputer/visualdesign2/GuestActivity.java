package com.mnemonic.icomputer.visualdesign2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestActivity extends AppCompatActivity implements Callback<List<Guest>> {

    public final static String EXTRA_CHOOSED_GUEST_NAME = "com.mnemonic.icomputer.screeningtest.CHOOSED_GUEST_NAME";

    private GridView gridView;

    private List<Guest> guests;
    private GuestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        //GuestAsyncTask guestAsyncTask = new GuestAsyncTask(this);
        //guestAsyncTask.execute();

        guests = new ArrayList<>();

        gridView = (GridView) findViewById(R.id.guestGrid);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = guests.get(position).getName();
                int day = Integer.parseInt((String) android.text.format.DateFormat.format("d", guests.get(position).getBirthdate()));
                int month = Integer.parseInt((String) android.text.format.DateFormat.format("MM", guests.get(position).getBirthdate()));

                String msg = "";
                if (day%2 == 0 && day%3 == 0)
                    msg = "iOS";
                else if (day%2 == 0)
                    msg = "Blackberry";
                else if (day%3 == 0)
                    msg = "Android";
                else
                    msg = "Feature phone";

                msg = day + " : " + msg;

                String msgPrime = month + " : ";
                if (isPrime(month))
                    msgPrime += "is prime";
                else
                    msgPrime += "not prime";

                msg = msg + "\n" + msgPrime;

                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.putExtra(EXTRA_CHOOSED_GUEST_NAME, selectedItem);
                setResult(RESULT_OK, intent);
                finish();

                //Toast.makeText(getApplicationContext(), selectedItem, Toast.LENGTH_SHORT).show();

            }
        });

        GuestAPIService api = RetroClient.getGuestAPIService();

        Call<List<Guest>> call = api.getMyJSON();
        call.enqueue(this);
    }

    private boolean isPrime(int num) {
        if (num <= 1) return false;

        int mid = num/2;
        for (int i=2;i<=mid;i++) {
            if (num % i == 0) return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onResponse(Call<List<Guest>> call, Response<List<Guest>> response) {
        Log.d("RESPONSE", response.message());
        if(response.isSuccessful()) {
            guests = response.body();
            adapter = new GuestAdapter(GuestActivity.this, guests);
            gridView.setAdapter(adapter);
        }
    }

    @Override
    public void onFailure(Call<List<Guest>> call, Throwable t) {
        Log.d("FAILURE", t.getMessage());
    }
}