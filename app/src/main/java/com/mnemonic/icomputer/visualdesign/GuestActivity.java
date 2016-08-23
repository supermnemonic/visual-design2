package com.mnemonic.icomputer.visualdesign;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GuestActivity extends AppCompatActivity {

    public final static String EXTRA_CHOOSED_GUEST_NAME = "com.mnemonic.icomputer.screeningtest.CHOOSED_GUEST_NAME";

    public Guest[] guests;
    public GridView grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        GuestAsyncTask guestAsyncTask = new GuestAsyncTask(this);
        guestAsyncTask.execute();
    }

    public void onGuestDataRetrieved(String jsonStringGuests) {
        try {
            JSONArray jsonArray = new JSONArray(jsonStringGuests);
            guests = new Guest[jsonArray.length()];
            for(int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int id = Integer.parseInt(jsonObject.optString("id").toString());
                String name = jsonObject.optString("name").toString();
                String birthdateStr = jsonObject.optString("birthdate").toString();

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date birthdate = new Date();
                try {
                    birthdate =  df.parse(birthdateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                guests[i] = new Guest(id, name, birthdate);
            }

            GuestAdapter adapter = new GuestAdapter(this, guests);
            grid = (GridView)findViewById(R.id.guestGrid);
            grid.setAdapter(adapter);

            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub
                    String selectedItem = guests[+position].getName();
                    int day = Integer.parseInt((String) android.text.format.DateFormat.format("d", guests[+position].getBirthdate()));
                    int month = Integer.parseInt((String) android.text.format.DateFormat.format("MM", guests[+position].getBirthdate()));

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
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
}
