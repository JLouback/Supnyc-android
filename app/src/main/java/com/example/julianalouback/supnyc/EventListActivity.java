package com.example.julianalouback.supnyc;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.julianalouback.supnyc.Models.Event;
import com.example.julianalouback.supnyc.Models.StoredEvent;
import com.example.julianalouback.supnyc.util.APIHelper;
import com.example.julianalouback.supnyc.util.LikesDataSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EventListActivity extends Activity {
    private RecyclerView mRecyclerView;
    private EventRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String mType;
    private LikesDataSource mDataSource;
    private ProgressBar vProgressBar;

    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        mDataSource = new LikesDataSource(this);
        mDataSource.open();

        vProgressBar = (ProgressBar) findViewById(R.id.event_list_progress);
        vProgressBar.getIndeterminateDrawable().setColorFilter(0xFFF2B441, PorterDuff.Mode.SRC_ATOP);
        mRecyclerView = (RecyclerView) findViewById(R.id.event_list_recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Intent mIntent = getIntent();
        this.mType = mIntent.getStringExtra("type");
        getEvents(mType);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.app_transparent));
        final View mActionBarView = getLayoutInflater().inflate(R.layout.view_as_map_action_bar, null);
        actionBar.setTitle(mType.replace(mType.charAt(0), mType.toUpperCase().charAt(0)));
        actionBar.setCustomView(mActionBarView);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        TextView mTextView = (TextView) actionBar.getCustomView().findViewById(R.id.event_map_title);
        mTextView.setText(getTypeTitle());

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
                                    if(dismissRight) {
                                        Event event = mAdapter.getItem(position);
                                        if (!mDataSource.getAllLikes().contains(event)){
                                            event.setUserLiked(true);
                                            mDataSource.createLike(event);
                                            mAdapter.setItemInList(position, event);
                                            mAdapter.notifyItemChanged(position);
                                            APIHelper.updateLike(event, "like", false);
                                        }
                                        else{
                                            event.setUserLiked(false);
                                            mDataSource.removeLike(new StoredEvent(event.getRangeKey(), event.getType()));
                                            mAdapter.setItemInList(position, event);
                                            mAdapter.notifyItemChanged(position);
                                            APIHelper.updateLike(event, "like", true);
                                        }
                                    }
                                    else {
                                        Event event = mAdapter.getItem(position);
                                        mDataSource.createDislike(event);
                                        mAdapter.removeItem(position);
                                        mAdapter.notifyItemRemoved(position);
                                        APIHelper.updateLike(event, "dislike", false);
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
                        bundle.putBoolean("userLiked", mAdapter.getItem(position).getUserLiked());
                        bundle.putString("type", mAdapter.getItem(position).getType());
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
        if(type.equals("recommended")) {
            url = "http://supnyc.elasticbeanstalk.com/recommended_events_api";
        }
        else {
            url = "http://supnyc.elasticbeanstalk.com/events_api?type=" + type + "&start=1386817441&end=1449889441";
        }
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<Event> events = jsonToEvent(response);
                        List<StoredEvent> likedEvents = mDataSource.getAllLikes();
                        List<StoredEvent> dislikedEvents = mDataSource.getAllDisLikes();
                        for(StoredEvent event : likedEvents){
                            if(events.contains(event)){
                                int ind = events.indexOf(event);
                                Event e = events.get(ind);
                                e.setUserLiked(true);
                                events.set(ind, e);
                            }
                        }
                        for(StoredEvent event : dislikedEvents){
                            events.remove(event);
                        }
                        mAdapter.setItemList(events);
                        mAdapter.notifyDataSetChanged();
                        vProgressBar.setVisibility(View.GONE);
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
                    Address location = null;
                    if (address.size() > 0) {
                        location = address.get(0);

                    }

                    String image_url = getResources().getString(R.string.default_image_url);
                    if(jsonObject.has("image_url")){
                        image_url = jsonObject.getString("image_url");
                    }
                    event = new Event(jsonObject.getString("title"),description,
                            jsonObject.getString("address"),
                            address.size() > 0 ? location.getLatitude() : 200,address.size() > 0 ? location.getLongitude() : 200,
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
        System.out.println("RESUME");
        vProgressBar.setVisibility(View.VISIBLE);
        getEvents(mType);
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

    private String getTypeTitle(){
        if(mType.equals("party")){
            return "Party";
        }
        else if(mType.equals("bar")){
            return "Bars";
        }
        else if(mType.equals("dining")){
            return "Dining";
        }
        else if(mType.equals("culture")){
            return "Culture";
        } else {
            return "Recommended Events";
        }
    }

}
