package com.mnemonic.icomputer.visualdesign2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.util.HashMap;
import java.util.List;

import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.ITALIC;
import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

/**
 * Created by iComputer on 25-08-2016.
 */
public class EventMapFragment extends Fragment implements OnMapReadyCallback {

    public static final String ARG_START_POSITION = "arg_start_position";

    public static final String TAG = "EventMapFragment";
    private static final float DEF_ZOOM = 16.4f;
    private GoogleMap map;
    private MapView mapView;
    private HashMap<Marker, Integer> markerIdHash;
    private HashMap<Integer, Marker> markerHash;

    private List<Event> events;

    private int currentPosition;

    private OnEventMapListener callback;
    IconGenerator iconFactory;

    public interface OnEventMapListener {
        public void onMarkerClick(int position);

        public List<Event> getEvents();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_map_fragment, container, false);
        rootView.setTag(TAG);

        currentPosition = getArguments().getInt(ARG_START_POSITION);

        mapView = (MapView) rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        events = callback.getEvents();

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (!isVisible()) return;

        //Toast.makeText(getActivity(), "onMapReady", Toast.LENGTH_SHORT).show();

        map = googleMap;

        //RealmResults<Event> events = realm.where(Event.class).findAll();
        markerIdHash = new HashMap<>();
        markerHash = new HashMap();

        iconFactory = new IconGenerator(getActivity());
        //iconFactory.setColor(Color.CYAN);
        iconFactory.setStyle(IconGenerator.STYLE_ORANGE);
        for (int i = 0; i < events.size(); i++) {
            LatLng pos = new LatLng(events.get(i).getLat(), events.get(i).getLng());
            Marker marker = addIcon(iconFactory, events.get(i).getName(), pos);
            markerIdHash.put(marker, i);
            markerHash.put(i, marker);
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                animateCameraToMarker(marker);
                callback.onMarkerClick((Integer) markerIdHash.get(marker));
                return false;
            }
        });

        //int i = callback.getSlideCurrentPage();
        //LatLng pos = new LatLng(events.get(i).getLat(), events.get(i).getLng());
        moveCameraToMarkerId(currentPosition, false);
    }

    private void resetMarkerStyle() {
        iconFactory.setStyle(IconGenerator.STYLE_ORANGE);
        for (Integer position : markerHash.keySet()) {
            Marker marker = markerHash.get(position);
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(events.get(position).getName())));
        }
    }

    private Marker addIcon(IconGenerator iconFactory, CharSequence text, LatLng position) {
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).
                position(position).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        return map.addMarker(markerOptions);
    }

    public void animateCameraToMarkerId(int markerId) {
        moveCameraToMarkerId(markerId, true);
    }

    public void animateCameraToMarker(Marker marker) {
        moveCameraToMarker(marker, true);
    }

    public void moveCameraToMarkerId(int markerId, boolean animate) {
        Marker marker = markerHash.get(markerId);
        moveCameraToPos(marker.getPosition(), animate);

        resetMarkerStyle();
        setMarkerSelected(marker, markerIdHash.get(marker));
    }

    public void moveCameraToMarker(Marker marker, boolean animate) {
        moveCameraToPos(marker.getPosition(), animate);

        resetMarkerStyle();
        setMarkerSelected(marker, markerIdHash.get(marker));
    }

    public void moveCameraToPos(LatLng pos, boolean animate) {
        if (animate)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, DEF_ZOOM));
        else
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, DEF_ZOOM));

    }

    private void setMarkerSelected(Marker marker, int markerId) {
        iconFactory.setStyle(IconGenerator.STYLE_GREEN);
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(events.get(markerId).getName())));
    }

    private CharSequence makeCharSequence() {
        String prefix = "Mixing ";
        String suffix = "different fonts";
        String sequence = prefix + suffix;
        SpannableStringBuilder ssb = new SpannableStringBuilder(sequence);
        ssb.setSpan(new StyleSpan(ITALIC), 0, prefix.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new StyleSpan(BOLD), prefix.length(), sequence.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callback = (OnEventMapListener) context;
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
