package com.example.julianalouback.supnyc;

import java.io.BufferedInputStream;
import java.io.InputStream;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.julianalouback.supnyc.Models.Event;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.HttpClient;

import java.util.List;

/**
 * Created by moldy530 on 11/22/14.
 */
public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.ViewHolder> {

    private List<Event> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        protected TextView vTitleView;
        protected TextView vDescriptionView;
        protected TextView vTimeAddressView;
        protected LinearLayout vPictureLayout;

        public ViewHolder(View v) {
            super(v);
            vTitleView = (TextView) v.findViewById(R.id.event_title);
            vDescriptionView = (TextView) v.findViewById(R.id.txtDescription);
            vTimeAddressView = (TextView) v.findViewById(R.id.txtAddressAndTime);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EventRecyclerAdapter(List<Event> myDataset) {
        mDataset = myDataset; //this will be passed the dataset
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EventRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
       // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_card, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Event event = mDataset.get(position);
        String time = event.getFormattedStart() + " - " + event.getFormattedEnd();
        holder.vTimeAddressView.setText(event.getAddress() + "\n" + time);
        holder.vDescriptionView.setText(event.getDescription());
        holder.vTitleView.setText(event.getTitle());
        //TODO: get and set the background of the card
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public InputStream getRequest(String url){
        HttpGet httpGet = new HttpGet(url);
        BufferedInputStream bis = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(httpGet);
            bis = new BufferedInputStream(response.getEntity().getContent());
        }catch(Exception e){
            e.printStackTrace();
        }
        return bis;
    }

    public void setItemList(List<Event> events){
        this.mDataset = events;
    }

}
