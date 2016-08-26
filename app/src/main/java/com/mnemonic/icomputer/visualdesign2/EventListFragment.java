package com.mnemonic.icomputer.visualdesign2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by iComputer on 25-08-2016.
 */
public class EventListFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "EventListFragment";

    private OnEventListListener callback;

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;


    public interface OnEventListListener {
        void onRefreshList();
        List<Event> getEvents();
        void onListSelected(int position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_list_fragment, container, false);
        rootView.setTag(TAG);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.event_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callback.onRefreshList();
                recyclerViewAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView = (RecyclerView) rootView.findViewById(R.id.event_recycler_view);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerViewAdapter = new EventListAdapter(getActivity(), this, callback.getEvents());
        recyclerView.setAdapter(recyclerViewAdapter);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callback = (OnEventListListener) context;
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

    @Override
    public void onClick(View view) {
        int position = recyclerView.getChildLayoutPosition(view);
        callback.onListSelected(position);
    }

}
