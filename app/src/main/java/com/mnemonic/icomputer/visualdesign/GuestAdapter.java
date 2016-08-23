package com.mnemonic.icomputer.visualdesign;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by iComputer on 15-07-2016.
 */
public class GuestAdapter extends ArrayAdapter<Guest> {

    private final Activity context;
    private final Guest[] guests;

    public GuestAdapter(Activity context, Guest[] guests) {
        super(context, R.layout.item_list_event, guests);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.guests = guests;
    }

    public View getView(int position, View view, ViewGroup parent) {
        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //view.setLayoutParams(new GridView.LayoutParams(params));

        //view.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, rowHigh));

        LayoutInflater inflater = context.getLayoutInflater();
        View itemView = inflater.inflate(R.layout.item_list_guest, null, false);

        //TextView txtName = (TextView) itemView.findViewById(R.id.guestName);
        //ImageView imageView = (ImageView) rowView.findViewById(R.id.eventIcon);
        //TextView txtDate = (TextView) rowView.findViewById(R.id.eventDate);

        //txtName.setText(guests[position].name);
        //imageView.setImageResource(events[position].image);
        //txtDate.setText(events[position].date.toString());
        //return rowView;

        RelativeLayout rView;
        if (view == null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) getContext()).getWindowManager()
                    .getDefaultDisplay()
                    .getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            int layParamWidth = (int) (0.3f * width);

            // if it's not recycled, initialize some attributes
            rView = (RelativeLayout) inflater.inflate(R.layout.item_list_guest, null, false);
            rView.setLayoutParams(new GridView.LayoutParams(layParamWidth, layParamWidth));
            TextView txtName1 = (TextView) rView.findViewById(R.id.guestName);
            ImageView imageView1 = (ImageView) rView.findViewById(R.id.guestIcon);
            txtName1.setText(guests[position].getName());
            imageView1.setImageResource(R.drawable.icon_person);
            imageView1.setScaleType(ImageView.ScaleType.FIT_CENTER);
            rView.setPadding(8, 8, 8, 8);
        } else {
            rView = (RelativeLayout) view;
        }

        //imageView.setImageResource(R.drawable.icon_person);
        return rView;
    };
}
