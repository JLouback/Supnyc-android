package com.example.julianalouback.supnyc;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;


public class EventDetails extends Activity {
    private NetworkImageView vImage;
    private TextView vTitle;
    private TextView vHours;
    private TextView vDescription;
    private TextView vAddress;
    private View mActionBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.app_transparent));
//displaying custom ActionBar
        View mActionBarView = getLayoutInflater().inflate(R.layout.details_action_bar, null);
        actionBar.setCustomView(mActionBarView);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        vTitle = (TextView) findViewById(R.id.details_title);
        vHours =  (TextView) findViewById(R.id.details_hours);
        vDescription = (TextView) findViewById(R.id.details_description);
        vImage = (NetworkImageView) findViewById(R.id.details_image);
        vAddress = (TextView) findViewById(R.id.details_address);

        Bundle bundle = getIntent().getExtras();
        vTitle.setText(bundle.getString("title"));
        vHours.setText(bundle.getString("hours"));
        vDescription.setText(bundle.getString("description"));
        vAddress.setText(bundle.getString("address"));
        String url = bundle.getString("url");
        vImage.setImageUrl(url, AppController.getInstance().getImageLoader());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_details, menu);
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

    public void goBack(View v){
        super.onBackPressed();
    }
}
