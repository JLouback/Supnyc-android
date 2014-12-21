package com.example.julianalouback.supnyc;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.julianalouback.supnyc.Models.Event;
import com.example.julianalouback.supnyc.util.LikesDataSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class EventListActivity extends Activity {
    private RecyclerView mRecyclerView;
    private EventRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String mType;
    private View mActionBarView;
    private LikesDataSource mDataSource;

    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        mDataSource = new LikesDataSource(this);
        mDataSource.open();

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);

        View mActionBarView = getLayoutInflater().inflate(R.layout.view_as_map_action_bar, null);
        actionBar.setCustomView(mActionBarView);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        mRecyclerView = (RecyclerView) findViewById(R.id.event_list_recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Intent mIntent = getIntent();
        this.mType = mIntent.getStringExtra("type");
        getEvents(mType);

        mAdapter = new EventRecyclerAdapter(null);
        mRecyclerView.setAdapter(mAdapter);

        SwipeDismissRecyclerViewTouchListener touchListener =
                new SwipeDismissRecyclerViewTouchListener(
                        mRecyclerView,
                        new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(RecyclerView recyclerView, int[] reverseSortedPositions, boolean dismissRight) {
                                for (int position : reverseSortedPositions) {
                                    //TODO: update the server for the item at this location
                                    if(dismissRight){
                                        Event event = mAdapter.getItem(position);
                                        event.setUserLiked(true);
                                        mDataSource.createLike(event);
                                        mAdapter.setItemInList(position, event);
                                        mAdapter.notifyItemChanged(position);
                                    }
                                    else {
                                        Event event = mAdapter.getItem(position);
                                        mDataSource.createDislike(event);
                                        mAdapter.removeItem(position);
                                        mAdapter.notifyItemRemoved(position);
                                    }
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });
        mRecyclerView.setOnTouchListener(touchListener);
        mRecyclerView.setOnScrollListener(touchListener.makeScrollListener());
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Bundle bundle = new Bundle();
                        bundle.putString("range_key", mAdapter.getItem(position).getRangeKey());
                        bundle.putString("title", mAdapter.getItem(position).getTitle());
                        bundle.putString("description", mAdapter.getItem(position).getDescription());
                        bundle.putString("address", mAdapter.getItem(position).getAddress());
                        bundle.putString("hours", mAdapter.getItem(position).getFormattedStart() + " - " + mAdapter.getItem(position).getFormattedEnd());
                        bundle.putString("url", mAdapter.getItem(position).getImageUrl());
                        Intent intent = new Intent(getBaseContext(), EventDetails.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        //Toast.makeText(EventListActivity.this, "Clicked " + mAdapter.getItem(position).getTitle(), Toast.LENGTH_SHORT).show();
                    }
                }));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.event_list_menu_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    public void getEvents(String type){
        String url;
        if(type == "recommended") {
            url = "http://supnyc.elasticbeanstalk.com/recommended_events_api";
        }
        else {
            url = "http://supnyc.elasticbeanstalk.com/events_api?type=" + type + "&start=1418016151000&end=1420435351000";
        }
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

    public ArrayList<Event> jsonToEvent(JSONArray jsonEvents){
        ArrayList<Event> events = new ArrayList<Event>();
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
                    Geocoder coder = new Geocoder(this);
                    List<Address> address = coder.getFromLocationName(jsonObject.getString("address"),5);
                    if (address != null) {
                        Address location = address.get(0);
                    }

                    Address location = address.get(0);
                    location.getLatitude();
                    location.getLongitude();

                    String image_url = "http://www.cootharababeefgenes.com.au/assets/placeholder-81127a71a07cd5c12cde6fc9ac9b1b6e.png";
                    if(jsonObject.has("image_url")){
                        image_url = jsonObject.getString("image_url");
                    }
                    event = new Event(jsonObject.getString("title"),description,
                            jsonObject.getString("address"),
                            location.getLatitude(),location.getLongitude(),
                            jsonObject.getString("host_username"),
                            Long.parseLong(jsonObject.getString("start")),Long.parseLong(end),
                            jsonObject.getString("type"),image_url,
                            Long.parseLong(jsonObject.getString("like_count")),
                            Long.parseLong(jsonObject.getString("going_count")),
                            jsonObject.getString("range_key"));
                    events.add(event);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return events;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildPosition(childView));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }
    }

    public void viewAsMap(View v){
        Intent intent = new Intent(getBaseContext(), EventMapActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("events", mAdapter.getDataset());
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void goBack(View v){
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        mDataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mDataSource.close();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mDataSource.close();
        super.onDestroy();
    }

}
