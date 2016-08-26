package com.mnemonic.icomputer.visualdesign2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by iComputer on 25-08-2016.
 */
public class EventMapViewFragment extends Fragment {

    public static final String TAG = "EventMapViewFragment";
    private static final float RATIO_SCALE = 0.3f;

    private OnEventMapViewListener callback;

    private ViewPager viewPagerEventSlide;
    private EventSlideAdapter pagerAdapterEventSlide;


    public interface OnEventMapViewListener {
        public List<Event> getEvents();

        public void onSlidePageChanged(int currentItem);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_mapview_fragment, container, false);
        rootView.setTag(TAG);

        // Instantiate a ViewPager and a PagerAdapter.
        viewPagerEventSlide = (ViewPager) rootView.findViewById(R.id.pager);
        pagerAdapterEventSlide = new EventSlideAdapter(getActivity().getSupportFragmentManager(), callback.getEvents());
        viewPagerEventSlide.setAdapter(pagerAdapterEventSlide);

        int marginPx = getResources().getDimensionPixelOffset(R.dimen.viewpager_margin);
        viewPagerEventSlide.setPageMargin(marginPx);
        viewPagerEventSlide.setOffscreenPageLimit(2);

        viewPagerEventSlide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    callback.onSlidePageChanged(viewPagerEventSlide.getCurrentItem());
                }
            }
        });

        return rootView;
    }

    public int getCurrentSlidePage() {
        return viewPagerEventSlide.getCurrentItem();
    }

    public void setCurrentSlidePage(int position) {
        viewPagerEventSlide.setCurrentItem(position, true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callback = (OnEventMapViewListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnEventListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        callback = null;
    }
}
