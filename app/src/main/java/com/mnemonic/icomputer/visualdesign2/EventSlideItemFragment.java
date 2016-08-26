package com.mnemonic.icomputer.visualdesign2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by iComputer on 25-08-2016.
 */
public class EventSlideItemFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.event_slide_item, container, false);

        long id = getArguments().getLong(EventSlideAdapter.ARGS_EVENT_ID);
        int image = getArguments().getInt(EventSlideAdapter.ARGS_EVENT_IMAGE);
        String name = getArguments().getString(EventSlideAdapter.ARGS_EVENT_TITLE);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.event_item_slide_image);
        TextView textView = (TextView) rootView.findViewById(R.id.event_item_slide_title);

        imageView.setImageResource(image);
        textView.setText(name);

        return rootView;
    }

}
