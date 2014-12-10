package com.example.julianalouback.supnyc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.julianalouback.supnyc.Models.Event;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by julianalouback on 12/10/14.
 */
public class EventMapActivity extends Activity implements GoogleMap.OnMarkerClickListener {

    private ArrayList<Event> events;
    private GoogleMap map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_map);

        //Get list of events to mark on map
        Bundle bundle = getIntent().getExtras();
        events = bundle.getParcelableArrayList("events");

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        LatLng nyc = new LatLng(40.7127837, -74.00594130000002);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(nyc, 12));

        for(int i=0; i<events.size(); i++){
            LatLng latlng = new LatLng(events.get(i).getLatitude(),events.get(i).getLongitude());
            //set marker with latitude, longitude and the array index as title for future id
            Marker marker = map.addMarker(new MarkerOptions().position(latlng).title(Integer.toString(i)));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.event_map_menu_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.list_display) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        //get event corresponding to marker
        int index = Integer.parseInt(marker.getTitle());
        Event event = events.get(index);
        LatLng latlng = new LatLng(event.getLatitude(), event.getLongitude());
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 13));
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.event_map_card, null);

        // fill in any details dynamically here
        TextView title = (TextView) v.findViewById(R.id.map_event_title);
        title.setText(event.getTitle());
        TextView description = (TextView) v.findViewById(R.id.map_txtDescription);
        description.setText(event.getDescription());
        TextView info = (TextView) v.findViewById(R.id.map_txtAddressAndTime);
        String addressTime = event.getAddress()+"\n"+ event.getFormattedStart()+"\n"+event.getFormattedEnd();
        info.setText(addressTime);

        // insert into main view
        ViewGroup insertPoint = (ViewGroup) findViewById(R.id.map);
        insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
            //handle click here
        return true;
    }
}
