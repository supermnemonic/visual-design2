package com.mnemonic.icomputer.visualdesign;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by iComputer on 15-07-2016.
 */
public class EventAdapter extends ArrayAdapter<Event> {

    private final Activity context;
    private final List<Event> events;

    public EventAdapter(Activity context, List<Event> events) {
        super(context, R.layout.item_list_event, events);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.events = events;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.item_list_event, null,true);

        TextView txtName = (TextView) rowView.findViewById(R.id.eventName);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.eventImg);
        TextView txtDate = (TextView) rowView.findViewById(R.id.eventDate);
        TextView txtTag = (TextView) rowView.findViewById(R.id.eventTag);
        TextView txtResume = (TextView) rowView.findViewById(R.id.eventResume);

        txtName.setText(events.get(position).name);
        imageView.setImageResource(events.get(position).image);
        txtDate.setText(new SimpleDateFormat("MM dd yyyy").format(events.get(position).date));
        txtTag.setText(events.get(position).tags);
        txtResume.setText(events.get(position).resume);
        return rowView;
    };
}
