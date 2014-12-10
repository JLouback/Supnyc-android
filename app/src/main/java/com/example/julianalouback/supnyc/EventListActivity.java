package com.example.julianalouback.supnyc;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.julianalouback.supnyc.Models.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class EventListActivity extends Activity {
    private RecyclerView mRecyclerView;
    private EventRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.event_list_recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //TODO:get the dataset here
        getEvents("party");

        mAdapter = new EventRecyclerAdapter(null);
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

    public void getEvents(String type){
        String url = "http://supnyc.elasticbeanstalk.com/events_api?type=party&start=1418016151000&end=1420435351000";
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mAdapter.setItemList(jsonToEvent(response));
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener()  {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req,
                tag_json_arry);
    }

    public List<Event> jsonToEvent(JSONArray jsonEvents){
        List<Event> events = new ArrayList<Event>();
        Event event;
        if (jsonEvents != null) {
            for (int i=0;i<jsonEvents.length();i++){
                JSONObject jsonObject = null;
                try {
                    jsonObject = jsonEvents.getJSONObject(i);
                    //wtf to do add if remove this later
                    String description = "No description available";
                    if(jsonObject.has("description")) {
                        description = jsonObject.getString("description");
                    }
                    String end = "1420099200000";
                    if(jsonObject.has("end")) {
                        end = jsonObject.getString("end");
                    }
                    event = new Event(jsonObject.getString("title"),description,
                            jsonObject.getString("address"),jsonObject.getString("host_username"),
                            Long.parseLong(jsonObject.getString("start")),Long.parseLong(end),
                            jsonObject.getString("type"),"www.blah.com");
                    events.add(event);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return events;
    }

}
