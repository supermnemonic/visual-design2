package com.mnemonic.icomputer.visualdesign2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by iComputer on 15-07-2016.
 */
public class GuestAdapter extends RecyclerView.Adapter<GuestAdapter.ViewHolder> {

    private final Context context;
    private List<Guest> guests;
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
        int edge = context.getResources().getDimensionPixelSize(R.dimen.edge);
        int width = displayMetrics.widthPixels - 2 * edge;
        int layParamWidth = (int) (0.5f * width);

        View view = layoutInflater.inflate(R.layout.item_list_guest, parent, false);
        view.setLayoutParams(new GridLayoutManager.LayoutParams(layParamWidth, layParamWidth));
        view.setPadding(edge, edge, edge, edge);
        view.setOnClickListener((View.OnClickListener) context);
        //relativeLayoutView.setLayoutParams(new GridView.LayoutParams(layParamWidth, layParamWidth));
        //relativeLayoutView.setPadding(60,60,60,60);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.textViewName.setText(guests.get(position).getName());
        holder.imageViewIcon.setImageResource(R.drawable.icon_person);
        //holder.imageViewIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }

    @Override
    public int getItemCount() {
        return guests.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView textViewName;
        public final ImageView imageViewIcon;

        public ViewHolder(View view) {
            super(view);

            this.textViewName = (TextView) view.findViewById(R.id.guestName);
            this.imageViewIcon = (ImageView) view.findViewById(R.id.guestIcon);

        }
    }
}
