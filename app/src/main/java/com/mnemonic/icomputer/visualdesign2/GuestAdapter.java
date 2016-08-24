package com.mnemonic.icomputer.visualdesign2;

import android.app.Activity;
import android.content.Context;
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
public class GuestAdapter extends ArrayAdapter<Guest> {

    private final Context context;
    private final List<Guest> guests;
    private LayoutInflater layoutInflater;

    public GuestAdapter(Context context, List<Guest> guests) {
        super(context, R.layout.item_list_guest, guests);

        this.context = context;
        this.guests = guests;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        // if it's not recycled, initialize some attributes
        if (convertView == null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) getContext()).getWindowManager()
                    .getDefaultDisplay()
                    .getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            int layParamWidth = (int) (0.3f * width);

            View view = layoutInflater.inflate(R.layout.item_list_guest, parent, false);

            RelativeLayout relativeLayoutView = (RelativeLayout) view;
            relativeLayoutView.setLayoutParams(new GridView.LayoutParams(layParamWidth, layParamWidth));
            relativeLayoutView.setPadding(8, 8, 8, 8);

            viewHolder = ViewHolder.create(relativeLayoutView);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textViewName.setText(guests.get(position).getName());
        viewHolder.imageViewIcon.setImageResource(R.drawable.icon_person);
        viewHolder.imageViewIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);

        return viewHolder.rootView;
    }

    private static class ViewHolder {
        public final RelativeLayout rootView;
        public final TextView textViewName;
        public final ImageView imageViewIcon;

        private ViewHolder(RelativeLayout rootView, TextView name, ImageView icon) {
            this.rootView = rootView;
            this.textViewName = name;
            this.imageViewIcon = icon;
        }

        public static ViewHolder create(RelativeLayout rootView) {
            return new ViewHolder(
                    rootView,
                    (TextView) rootView.findViewById(R.id.guestName),
                    (ImageView) rootView.findViewById(R.id.guestIcon)
            );
        }

    }
}
