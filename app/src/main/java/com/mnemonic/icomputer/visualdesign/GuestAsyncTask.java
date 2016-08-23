package com.mnemonic.icomputer.visualdesign;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by iComputer on 21-07-2016.
 */
public class GuestAsyncTask extends AsyncTask<String, String, String> {

    private GuestActivity guestActivity;
    private HttpURLConnection urlConnection;

    public GuestAsyncTask (GuestActivity guestActivity) {
        this.guestActivity = guestActivity;
    }

    @Override
    protected void onPreExecute() {
        ((TextView) guestActivity.findViewById(R.id.loadingText)).setText("Loading...");
    }

    @Override
    protected String doInBackground(String... params) {
        StringBuilder result = new StringBuilder();
        String urlString = "http://dry-sierra-6832.herokuapp.com/api/people";

        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

        return result.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        ((TextView) guestActivity.findViewById(R.id.loadingText)).setVisibility(View.GONE);
        guestActivity.onGuestDataRetrieved(result);
    }
}
