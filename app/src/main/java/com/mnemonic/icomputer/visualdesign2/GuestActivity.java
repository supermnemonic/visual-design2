package com.mnemonic.icomputer.visualdesign2;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestActivity extends AppCompatActivity implements Callback<List<Guest>>, View.OnClickListener {

    public final static String EXTRA_CHOOSED_GUEST_NAME = "com.mnemonic.icomputer.screeningtest.CHOOSED_GUEST_NAME";

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    //private GridView gridView;

    private List<Guest> guests;
    //private GuestAdapter adapter;

    private Realm realm;
    private RealmAsyncTask realmAsyncTask;
    private RealmConfiguration realmConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GuestAPIService api = RetroClient.getGuestAPIService();
                Call<List<Guest>> call = api.getMyJSON();
                call.enqueue(GuestActivity.this);
            }
        });

        recyclerViewLayoutManager = new GridLayoutManager(this, 2);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView = (RecyclerView) findViewById(R.id.grid_recycler_view);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerView.setHasFixedSize(true);

        // Create the Realm configuration
        realmConfig = new RealmConfiguration.Builder(this).build();
        // Open the Realm for the UI thread.
        realm = Realm.getInstance(realmConfig);

        if (realm.isEmpty()) {
            GuestAPIService api = RetroClient.getGuestAPIService();
            Call<List<Guest>> call = api.getMyJSON();
            call.enqueue(this);
        } else {
            RealmResults<Guest> storedGuests = realm.where(Guest.class).findAll();
            guests = storedGuests;
            recyclerViewAdapter = new GuestAdapter(this, guests);
            recyclerView.setAdapter(recyclerViewAdapter);
        }
    }

    public static boolean isPrime(int num) {
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
    protected void onStop() {
        super.onStop();
        if (realmAsyncTask != null && !realmAsyncTask.isCancelled()) {
            realmAsyncTask.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onResponse(Call<List<Guest>> call, Response<List<Guest>> response) {
        Log.d("RESPONSE", response.message());
        if(response.isSuccessful()) {
            guests = response.body();

            recyclerViewAdapter = new GuestAdapter(this, guests);
            recyclerView.setAdapter(recyclerViewAdapter);
            //gridView.setAdapter(adapter);

            if (realm.isClosed())
                realm = Realm.getInstance(realmConfig);

            realmAsyncTask = realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    bgRealm.copyToRealmOrUpdate(guests);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Toast.makeText(GuestActivity.this, "saved", Toast.LENGTH_LONG).show();
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    Log.d("REALM_ERROR", error.getMessage());
                }
            });

            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onFailure(Call<List<Guest>> call, Throwable t) {
        Log.d("FAILURE", t.getMessage());
    }

    @Override
    public void onClick(View view) {
        int position = recyclerView.getChildLayoutPosition(view);
        String selectedItem = guests.get(position).getName();
        int day = Integer.parseInt((String) android.text.format.DateFormat.format("d", guests.get(position).getBirthdate()));
        int month = Integer.parseInt((String) android.text.format.DateFormat.format("MM", guests.get(position).getBirthdate()));

        String msg = "";
        if (day % 2 == 0 && day % 3 == 0)
            msg = "iOS";
        else if (day % 2 == 0)
            msg = "Blackberry";
        else if (day % 3 == 0)
            msg = "Android";
        else
            msg = "Feature phone";

        msg = day + " : " + msg;

        String msgPrime = month + " : ";
        if (GuestActivity.isPrime(month))
            msgPrime += "is prime";
        else
            msgPrime += "not prime";

        msg = msg + "\n" + msgPrime;

        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();
        intent.putExtra(GuestActivity.EXTRA_CHOOSED_GUEST_NAME, selectedItem);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
