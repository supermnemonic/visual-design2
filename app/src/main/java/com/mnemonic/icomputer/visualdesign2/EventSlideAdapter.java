package com.mnemonic.icomputer.visualdesign2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by iComputer on 25-08-2016.
 */
public class EventSlideAdapter extends FragmentStatePagerAdapter {

    public static final String ARGS_EVENT_ID = "args_event_id";
    public static final String ARGS_EVENT_TITLE = "args_event_title";
    public static final String ARGS_EVENT_IMAGE = "args_event_image";

    private List<Event> events;

    public EventSlideAdapter(FragmentManager fm) {
        super(fm);
    }

    public EventSlideAdapter(FragmentManager fm, List<Event> events) {
        super(fm);

        this.events = events;
    }

    @Override
    public Fragment getItem(int position) {
        EventSlideItemFragment eventSlideItemFragment = new EventSlideItemFragment();
        Bundle args = new Bundle();
        args.putLong(ARGS_EVENT_ID, events.get(position).getId());
        args.putInt(ARGS_EVENT_IMAGE, events.get(position).getImage());
        args.putString(ARGS_EVENT_TITLE, events.get(position).getName());
        eventSlideItemFragment.setArguments(args);
        return eventSlideItemFragment;
    }

    @Override
    public int getCount() {
        return events.size();
    }

}
