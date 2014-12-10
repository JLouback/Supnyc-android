package com.example.julianalouback.supnyc;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.julianalouback.supnyc.Models.Event;

import java.util.ArrayList;
import java.util.List;


public class EventListActivity extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.event_list_recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //TODO:get the dataset here

        List<Event> events = generateTestEvents();

        mAdapter = new EventRecyclerAdapter(events);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public List<Event> generateTestEvents(){
        List<Event> events = new ArrayList<Event>();
        //String title, String desc, String address, String mHostUsername, Long mStart, Long mEnd, String mType, String mImageUrl
        for(int i = 0; i<10; i++){
            events.add(new Event("Title", "Description", "Address", "Host", (long) 100000, (long) 150000, "Type", "URL"));
        }

        return events;
    }

    public List<Event> getEvents(String type){
        return null;
    }
}
