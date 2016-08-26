package com.mnemonic.icomputer.visualdesign2;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class EventActivity extends AppCompatActivity implements EventListFragment.OnEventListListener, EventMapFragment.OnEventMapListener, EventMapViewFragment.OnEventMapViewListener {

    public final static String EXTRA_CHOOSED_EVENT_NAME = "com.mnemonic.icomputer.screeningtest.CHOOSED_EVENT_NAME";

    private String dum1 = "Lorem ipsum dolor sit amet, usu in vitae ornatus fabellas. Eu perfecto sententiae reprimique eum. Ferri complectitur at pro.";

    private List<Event> events;

    private Realm realm;
    private RealmAsyncTask realmAsyncTask;
    private RealmConfiguration realmConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        getSupportActionBar().setTitle("MESSAGE FROM CODI");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        events = new ArrayList<>();
        // what in here ..................

        // Create the Realm configuration
        realmConfig = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        // Open the Realm for the UI thread.
        realm = Realm.getInstance(realmConfig);

//        realm.beginTransaction();
//        realm.where(Event.class).findAll().deleteAllFromRealm();
//        realm.commitTransaction();

        if (realm.where(Event.class).findAll().isEmpty()) {
            events.addAll(createDummyEvents());
            //recyclerViewAdapter.notifyDataSetChanged();

            realmAsyncTask = realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    bgRealm.copyToRealmOrUpdate(events);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Toast.makeText(EventActivity.this, "events created and saved", Toast.LENGTH_SHORT).show();
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    Log.d("REALM_ERROR", error.getMessage());
                }
            });
        } else {
            RealmResults<Event> storedEvents = realm.where(Event.class).findAll();
            events.addAll(storedEvents);
            //recyclerViewAdapter.notifyDataSetChanged();
        }

        if (savedInstanceState == null) {
            //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            EventListFragment fragment = new EventListFragment();
            fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment, EventListFragment.TAG).commit();

        }
    }

    @Override
    public void onRefreshList() {
        events.add(createDummyEvent());

        if (realm.isClosed())
            realm = Realm.getInstance(realmConfig);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(events);
        realm.commitTransaction();
    }

    @Override
    public void onMarkerClick(int position) {
        EventMapViewFragment eventMapViewFragment = (EventMapViewFragment) getSupportFragmentManager().findFragmentByTag(EventMapViewFragment.TAG);
        if (eventMapViewFragment != null && eventMapViewFragment.isVisible()) {
            eventMapViewFragment.setCurrentSlidePage(position);
        }
    }

    @Override
    public List<Event> getEvents() {
        return events;
    }

    @Override
    public int getSlideCurrentPage() {
        EventMapViewFragment eventMapViewFragment = (EventMapViewFragment) getSupportFragmentManager().findFragmentByTag(EventMapViewFragment.TAG);
        if (eventMapViewFragment != null && eventMapViewFragment.isVisible()) {
            return eventMapViewFragment.getCurrentSlidePage();
        } else {
            return 0;
        }
    }

    @Override
    public void onSlidePageChanged(int currentItem) {
        EventMapFragment map = (EventMapFragment) getSupportFragmentManager().findFragmentByTag(EventMapFragment.TAG);
        if (map != null && map.isVisible()) {
            map.animateCameraToMarker(currentItem);
        }
    }

    @Override
    public void onListSelected(int position) {
//        String selectedItem = events.get(position).getName();
//
//        Intent intent = new Intent();
//        intent.putExtra(EXTRA_CHOOSED_EVENT_NAME, selectedItem);
//        setResult(RESULT_OK, intent);
//        finish();
        replaceToEventMapFragment(position);
    }

    private double[][] pos = {
            {-6.888739, 107.615672},
            {-6.8876847, 107.6116776},
            {-6.890339, 107.612103},
            {-6.893796, 107.612373},
            {-6.894745, 107.613907},
            {-6.888923, 107.618454},
            {-6.895390, 107.619501},
            {-6.892148, 107.610154},
            {-6.895945, 107.604254},
            {-6.897898, 107.607664},
            {-6.901373, 107.613600}
    };

    private List<Event> createDummyEvents() {
        List<Event> events = new ArrayList<Event>();
        for (int i = 1; i <= 10; i++) {
            events.add(new Event(i, "Event-" + i, Event.createDate("2016-1-" + i), R.drawable.r1, "tag1", dum1, pos[i][0], pos[i][1]));
        }
        return events;
    }

    private Event createDummyEvent() {
        long i = realm.where(Event.class).count() + 1;
        return new Event(i, "Event Baru-" + i, Event.createDate("2016-1-" + i), R.drawable.r1, "tagX", dum1, pos[0][0], pos[0][1]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.event_menu, menu);

        MenuItem item = menu.findItem(R.id.action_new_media);
        if (item.getActionView() != null) {
            item.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //toggleFragment();
                }
            });
        }

        Drawable drawable = menu.findItem(R.id.action_search).getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(Color.parseColor("#c5b458"), PorterDuff.Mode.SRC_ATOP);
        }

        return true;
    }

    public void toggleFragment() {
        EventListFragment eventListFragment = (EventListFragment) getSupportFragmentManager().findFragmentByTag(EventListFragment.TAG);
        EventMapViewFragment eventMapViewFragment = (EventMapViewFragment) getSupportFragmentManager().findFragmentByTag(EventMapViewFragment.TAG);

        if (eventMapViewFragment == null) {
            replaceToEventMapFragment();
        } else if (eventListFragment == null) {
            replaceToEventListFragment();
        }
    }

    private void replaceToEventListFragment() {
        EventListFragment listFragment = new EventListFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, listFragment, EventListFragment.TAG);
        transaction.commit();
    }

    private void replaceToEventMapFragment() {
        replaceToEventMapFragment(0);
    }

    private void replaceToEventMapFragment(int startPosition) {
        EventMapViewFragment mapViewFragment = new EventMapViewFragment();

        EventMapFragment mapFragment = new EventMapFragment();
        Bundle args = new Bundle();
        args.putInt(EventMapFragment.ARG_START_POSITION, startPosition);
        mapFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, mapViewFragment, EventMapViewFragment.TAG);
        transaction.replace(R.id.fragment_map_container, mapFragment, EventMapFragment.TAG);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_new_media:
                //Intent intent = new Intent(this, MapsActivity.class);
                //startActivity(intent);

                toggleFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

}
