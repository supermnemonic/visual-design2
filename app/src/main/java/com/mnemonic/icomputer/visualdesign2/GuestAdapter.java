package com.mnemonic.icomputer.visualdesign2;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by iComputer on 15-07-2016.
 */
public class GuestAdapter extends RecyclerView.Adapter<GuestAdapter.ViewHolder> {

    private final Context context;
    private final List<Guest> guests;
    private LayoutInflater layoutInflater;

    public GuestAdapter(Context context, List<Guest> guests) {
        this.context = context;
        this.guests = guests;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int layParamWidth = (int) (0.3f * width);

        View view = layoutInflater.inflate(R.layout.item_list_guest, parent, false);
        RelativeLayout relativeLayoutView = (RelativeLayout) view;
        relativeLayoutView.setLayoutParams(new GridView.LayoutParams(layParamWidth, layParamWidth));
        relativeLayoutView.setPadding(8, 8, 8, 8);

        return new ViewHolder(relativeLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textViewName.setText(guests.get(position).getName());
        holder.imageViewIcon.setImageResource(R.drawable.icon_person);
        holder.imageViewIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }

    @Override
    public int getItemCount() {
        return guests.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public final RelativeLayout rootView;
        public final TextView textViewName;
        public final ImageView imageViewIcon;

        public ViewHolder(RelativeLayout rootView) {
            super(rootView);

            this.rootView = (RelativeLayout) rootView;
            this.textViewName = (TextView) rootView.findViewById(R.id.guestName);
            this.imageViewIcon = (ImageView) rootView.findViewById(R.id.guestIcon);

        }
    }
}
