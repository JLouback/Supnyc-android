package com.example.julianalouback.supnyc;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;


public class EventDetails extends Activity {
    private NetworkImageView vImage;
    private TextView vTitle;
    private TextView vHours;
    private TextView vDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        vTitle = (TextView) findViewById(R.id.details_title);
        vHours =  (TextView) findViewById(R.id.details_hours);
        vDescription = (TextView) findViewById(R.id.details_description);
        vImage = (NetworkImageView) findViewById(R.id.details_image);

        Bundle bundle = getIntent().getExtras();
        vTitle.setText(bundle.getString("title"));
        vHours.setText(bundle.getString("hours"));
        vDescription.setText(bundle.getString("description"));
        //TODO: switch to unhardcoded one
        String url = "http://cdn0.cosmosmagazine.com/wp-content/uploads/2013/05/Non-stop-party-COSMOS-Science-Fiction.jpg";
        vImage.setImageUrl(url, AppController.getInstance().getImageLoader());
        //vImage.setImageUrl(bundle.getString("url"), AppController.getInstance().getImageLoader());

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
}
