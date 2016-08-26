package com.mnemonic.icomputer.visualdesign2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.List;

/**
 * Created by iComputer on 25-08-2016.
 */
public class EventMapViewFragment extends Fragment {

    public static final String TAG = "EventMapViewFragment";

    private OnEventMapViewListener callback;

    private ViewPager viewPagerEventSlide;
    private EventSlideAdapter pagerAdapterEventSlide;

    private int currentPosition;


    public interface OnEventMapViewListener {
        List<Event> getEvents();

        void replaceMapFragment(int startPosition);

        void onSlidePageChanged(int currentItem);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_mapview_fragment, container, false);
        rootView.setTag(TAG);

        currentPosition = getArguments().getInt(EventMapFragment.ARG_START_POSITION);

        // Instantiate a ViewPager and a PagerAdapter.
        viewPagerEventSlide = (ViewPager) rootView.findViewById(R.id.pager);
        pagerAdapterEventSlide = new EventSlideAdapter(getActivity().getSupportFragmentManager(), callback.getEvents());
        viewPagerEventSlide.setAdapter(pagerAdapterEventSlide);

        int marginPx = getResources().getDimensionPixelOffset(R.dimen.viewpager_margin);
        viewPagerEventSlide.setPageMargin(marginPx);
        viewPagerEventSlide.setOffscreenPageLimit(2);
        viewPagerEventSlide.setCurrentItem(currentPosition, true);

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
                    setCurrentPositionArgs(viewPagerEventSlide.getCurrentItem());
                    callback.onSlidePageChanged(viewPagerEventSlide.getCurrentItem());
                }
            }
        });

        return rootView;
    }

    public void setCurrentPositionArgs(int currentPosition) {
        this.currentPosition = currentPosition;
        this.getArguments().putInt(EventMapFragment.ARG_START_POSITION, currentPosition);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {

        if (nextAnim == 0) {
            callback.replaceMapFragment(currentPosition);
            return super.onCreateAnimation(transit, enter, nextAnim);
        }

        Animation anim = AnimationUtils.loadAnimation(getActivity(), nextAnim);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                callback.replaceMapFragment(currentPosition);
            }
        });
        return anim;
    }

    public void setCurrentSlidePage(int position) {
        setCurrentPositionArgs(position);
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
