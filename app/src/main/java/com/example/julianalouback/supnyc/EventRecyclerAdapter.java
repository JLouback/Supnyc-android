package com.example.julianalouback.supnyc;

import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.julianalouback.supnyc.Models.Event;

import java.util.ArrayList;


/**
 * Created by moldy530 on 11/22/14.
 */
public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.ViewHolder> {

    private ArrayList<Event> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        protected TextView vTitleView;
        protected TextView vDescriptionView;
        protected TextView vTimeAddressView;
        protected NetworkImageView vPictureLayout; //TODO: switch this to NetworkImage

        public ViewHolder(View v) {
            super(v);
            vTitleView = (TextView) v.findViewById(R.id.event_title);
            vDescriptionView = (TextView) v.findViewById(R.id.txtDescription);
            vTimeAddressView = (TextView) v.findViewById(R.id.txtAddressAndTime);
            vPictureLayout = (NetworkImageView) v.findViewById(R.id.event_image);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EventRecyclerAdapter(ArrayList<Event> myDataset) {
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
        if(event.getUserLiked()){
            holder.vTitleView.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_action_favorite_toggled, 0);
        }
        String url = event.getImageUrl();
        loadImageBackground(url, holder);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(mDataset == null){
            return 0;
        }
        else
            return mDataset.size();
    }

    private void loadImageBackground(String url, final ViewHolder holder){
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        holder.vPictureLayout.setImageUrl(url, imageLoader);

    }

    public void setItemList(ArrayList<Event> events){
        this.mDataset = events;
    }
    public void removeItem(int position) {this.mDataset.remove(position);}
    public void setItemInList(int position, Event event) {this.mDataset.set(position, event);}
    public Event getItem(int position) {return this.mDataset.get(position);}

    public ArrayList<Event> getDataset() {return this.mDataset;}
}
