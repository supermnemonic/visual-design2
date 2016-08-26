package com.mnemonic.icomputer.visualdesign2;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by iComputer on 15-07-2016.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    private final Fragment fragment;
    private final List<Event> events;
    private final LayoutInflater layoutInflater;

    public EventListAdapter(Context context, Fragment fragment, List<Event> events) {
        this.events = events;
        this.layoutInflater = LayoutInflater.from(context);
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_list_event, parent, false);
        view.setOnClickListener((View.OnClickListener) fragment);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //holder.imageViewIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        holder.textViewName.setText(events.get(position).getName());
        holder.imageViewIcon.setImageResource(events.get(position).getImage());
        holder.textViewDate.setText(new SimpleDateFormat("MM dd yyyy", Locale.ENGLISH).format(events.get(position).getDate()));
        holder.textViewTags.setText(events.get(position).getTags());
        holder.textViewResume.setText(events.get(position).getResume());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        public final CardView cardViewEvent;
        public final TextView textViewName;
        public final ImageView imageViewIcon;
        public final TextView textViewDate;
        public final TextView textViewTags;
        public final TextView textViewResume;

        public ViewHolder(View view) {
            super(view);

            this.cardViewEvent = (CardView) view.findViewById(R.id.event_cardview);
            this.textViewName = (TextView) view.findViewById(R.id.eventName);
            this.imageViewIcon = (ImageView) view.findViewById(R.id.roundedImageView);
            this.textViewDate = (TextView) view.findViewById(R.id.eventDate);
            this.textViewTags = (TextView) view.findViewById(R.id.eventTag);
            this.textViewResume = (TextView) view.findViewById(R.id.eventResume);
        }
    }
}
