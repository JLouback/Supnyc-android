package com.example.julianalouback.supnyc;

import android.app.Activity;
import android.os.Bundle;

import com.example.julianalouback.supnyc.Models.Event;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by julianalouback on 12/10/14.
 */
public class EventMapActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_map);

        //Get list of events to mark on map
        Bundle bundle = getIntent().getExtras();
        ArrayList<Event> events = bundle.getParcelableArrayList("events");

        GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        for(int i=0; i<events.size(); i++){
            LatLng latlng = new LatLng(events.get(i).getLatitude(),events.get(i).getLongitude());
            Marker marker = map.addMarker(new MarkerOptions().position(latlng));
        }
    }
}
