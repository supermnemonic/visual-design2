package com.mnemonic.icomputer.visualdesign;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EventActivity extends AppCompatActivity {

    public final static String EXTRA_CHOOSED_EVENT_NAME = "com.mnemonic.icomputer.screeningtest.CHOOSED_EVENT_NAME";

    String dum1 = "Lorem ipsum dolor sit amet, usu in vitae ornatus fabellas. Eu perfecto sententiae reprimique eum. Ferri complectitur at pro.";
    ListView list;
    List<Event> events;

    private SwipeRefreshLayout swipeContainer;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        getSupportActionBar().setTitle("MESSAGE FROM CODI");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        events = new ArrayList<>();
        createDummyEvents(events);

        list = (ListView) findViewById(R.id.eventList);
        eventAdapter = new EventAdapter(EventActivity.this, events);
        list.setAdapter(eventAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                String selectedItem = events.get(position).name;
                //Toast.makeText(getApplicationContext(), selectedItem, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.putExtra(EXTRA_CHOOSED_EVENT_NAME, selectedItem);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                eventAdapter.clear();
                createDummyEvents(events);
                events.add(createNewDummyEvent());
                eventAdapter = new EventAdapter(EventActivity.this, events);
                //list.setAdapter(eventAdapter);
                swipeContainer.setRefreshing(false);
            }
        });

    }

    private void createDummyEvents(List<Event> events) {
        events.add(new Event(R.drawable.r1, "Event Satu", "2016-1-4", "tag1", dum1));
        events.add(new Event(R.drawable.r2, "Event Dua", "2016-5-8", "tag1", dum1));
        events.add(new Event(R.drawable.r3, "Event Tiga", "2016-8-9", "tag1", dum1));
        //events.add(new Event(R.drawable.r1, "Event Empat", "2016-10-15", "tag1", dum1));
        //events.add(new Event(R.drawable.r2, "Event Lima", "2016-12-24", "tag1", dum1));
    }

    private Event createNewDummyEvent() {
        return new Event(R.drawable.r1, "Event Baru", "2011-11-11", "tagX", dum1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.event_menu, menu);

        Drawable drawable = menu.findItem(R.id.action_search).getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(Color.parseColor("#c5b458"), PorterDuff.Mode.SRC_ATOP);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
