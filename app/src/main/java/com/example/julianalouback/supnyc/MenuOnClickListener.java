package com.example.julianalouback.supnyc;

import android.content.Intent;
import android.view.View;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by julianalouback on 11/22/14.
 */
public class MenuOnClickListener implements View.OnClickListener {

    String type;

    public MenuOnClickListener(String eventType) {
        this.type = eventType;
    }

    @Override
    public void onClick(View v)
    {
        Intent intent = new Intent(v.getContext(), EventListActivity.class);
        intent.putExtra("type",type);
        v.getContext().startActivity(intent);
    }

}
