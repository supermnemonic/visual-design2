package com.mnemonic.icomputer.visualdesign2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmConfiguration;

/**
 * Created by iComputer on 25-08-2016.
 */
public class EventMapFragment extends Fragment implements OnMapReadyCallback {

    public static final String ARG_START_POSITION = "arg_start_position";

    public static final String TAG = "EventMapFragment";
    private static final float DEF_ZOOM = 17.0f;
    private GoogleMap map;
    private MapView mapView;
    private HashMap markerIdHash;
    private HashMap markerHash;

    private int startPosition;

    private Realm realm;
    private RealmAsyncTask realmAsyncTask;
    private RealmConfiguration realmConfig;

    private OnEventMapListener callback;

    public interface OnEventMapListener {
        public void onMarkerClick(int position);
        public List<Event> getEvents();
        public int getSlideCurrentPage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_map_fragment, container, false);
        rootView.setTag(TAG);

        startPosition = getArguments().getInt(ARG_START_POSITION);

        mapView = (MapView) rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        realmConfig = new RealmConfiguration.Builder(getActivity())
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(realmConfig);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (!isVisible()) return;

        //Toast.makeText(getActivity(), "onMapReady", Toast.LENGTH_SHORT).show();

        map = googleMap;

        //RealmResults<Event> events = realm.where(Event.class).findAll();
        List<Event> events = callback.getEvents();
        markerIdHash = new HashMap();
        markerHash = new HashMap();

        for (int i=0;i<events.size();i++) {
            LatLng pos = new LatLng(events.get(i).getLat(), events.get(i).getLng());
            Marker marker = map.addMarker(new MarkerOptions().position(pos).title(events.get(i).getName()));
            markerIdHash.put(marker,i);
            markerHash.put(i,marker);
        }

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                animateCameraTo(marker.getPosition());
                callback.onMarkerClick((Integer) markerIdHash.get(marker));
            }
        });
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                animateCameraTo(marker.getPosition());
                callback.onMarkerClick((Integer) markerIdHash.get(marker));
                return false;
            }
        });

        //int i = callback.getSlideCurrentPage();
        //LatLng pos = new LatLng(events.get(i).getLat(), events.get(i).getLng());
        moveCameraToMarker(startPosition);
    }

    private void animateCameraTo(LatLng pos) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, DEF_ZOOM));
    }

    public void animateCameraToMarker(int markerId) {
        Marker marker = (Marker) markerHash.get(markerId);
        if (marker != null) {
            LatLng pos = marker.getPosition();
            animateCameraTo(pos);

            marker.showInfoWindow();
        }
    }

    public void moveCameraToMarker(int markerId) {
        Marker marker = (Marker) markerHash.get(markerId);
        if (marker != null) {
            LatLng pos = marker.getPosition();
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, DEF_ZOOM));

            marker.showInfoWindow();
        }
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
        realm.close();
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
