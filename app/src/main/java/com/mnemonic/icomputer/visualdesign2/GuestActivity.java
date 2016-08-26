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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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

    private List<Guest> guests;

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

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.guest_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                TextView textStatus = (TextView) findViewById(R.id.text_status);
                textStatus.setVisibility(View.GONE);

                GuestAPIService api = RetroClient.getGuestAPIService();
                Call<List<Guest>> call = api.getMyJSON();
                call.enqueue(GuestActivity.this);
            }
        });

        RecyclerView.LayoutManager recyclerViewLayoutManager = new GridLayoutManager(this, 2);

        recyclerView = (RecyclerView) findViewById(R.id.guest_recycler_view);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerView.setHasFixedSize(true);

        guests = new ArrayList<>();
        recyclerViewAdapter = new GuestAdapter(this, guests);
        recyclerView.setAdapter(recyclerViewAdapter);

        // Create the Realm configuration
        realmConfig = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        // Open the Realm for the UI thread.
        realm = Realm.getInstance(realmConfig);

        if (realm.where(Guest.class).findAll().isEmpty()) {
            GuestAPIService api = RetroClient.getGuestAPIService();
            Call<List<Guest>> call = api.getMyJSON();
            call.enqueue(this);
        } else {
            RealmResults<Guest> storedGuests = realm.where(Guest.class).findAll();
            guests.addAll(storedGuests);
            recyclerViewAdapter.notifyDataSetChanged();
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

        TextView textStatus = (TextView) findViewById(R.id.text_status);
        textStatus.setVisibility(View.GONE);

        if(response.isSuccessful()) {
            guests.clear();
            guests.addAll(response.body());
            //guests.add(createDummyGuest());

            if (realm.isClosed())
                realm = Realm.getInstance(realmConfig);
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(guests);
            realm.commitTransaction();

            recyclerViewAdapter.notifyDataSetChanged();
        }

        swipeRefreshLayout.setRefreshing(false);
    }

    private Guest createDummyGuest() {
        long i = realm.where(Guest.class).count()+1;
        return new Guest(i,"Guest-"+i,Guest.createDate("2016-8-"+i));
    }

    @Override
    public void onFailure(Call<List<Guest>> call, Throwable t) {
        Log.d("FAILURE", t.getMessage());

        TextView textStatus = (TextView) findViewById(R.id.text_status);
        textStatus.setText(t.getMessage());
        textStatus.setVisibility(View.VISIBLE);

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View view) {
        int position = recyclerView.getChildLayoutPosition(view);
        String selectedItem = guests.get(position).getName();
        int day = Integer.parseInt((String) android.text.format.DateFormat.format("d", guests.get(position).getBirthdate()));
        int month = Integer.parseInt((String) android.text.format.DateFormat.format("MM", guests.get(position).getBirthdate()));

        String msg;
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
