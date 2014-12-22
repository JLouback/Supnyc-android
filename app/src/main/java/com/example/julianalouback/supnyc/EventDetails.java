package com.example.julianalouback.supnyc;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.example.julianalouback.supnyc.Models.StoredEvent;
import com.example.julianalouback.supnyc.util.APIHelper;
import com.example.julianalouback.supnyc.util.LikesDataSource;


public class EventDetails extends Activity {
    private NetworkImageView vImage;
    private TextView vTitle;
    private TextView vHours;
    private TextView vDescription;
    private TextView vAddress;
    private View mActionBarView;
    private ImageButton vDislikeButton, vLikeButton;
    private Bundle bundle;
    private LikesDataSource mDataSource;
    private boolean userLiked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.app_transparent));
        actionBar.setDisplayShowTitleEnabled(true);

        //displaying custom ActionBar
        mActionBarView = getLayoutInflater().inflate(R.layout.details_action_bar, null);
        actionBar.setCustomView(mActionBarView);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        vTitle = (TextView) findViewById(R.id.details_title);
        vHours =  (TextView) findViewById(R.id.details_hours);
        vDescription = (TextView) findViewById(R.id.details_description);
        vImage = (NetworkImageView) findViewById(R.id.details_image);
        vAddress = (TextView) findViewById(R.id.details_address);
        vDislikeButton = (ImageButton) findViewById(R.id.details_dislike);
        vLikeButton = (ImageButton) findViewById(R.id.details_like);
        mDataSource = new LikesDataSource(this);
        mDataSource.open();

        bundle = getIntent().getExtras();
        userLiked = bundle.getBoolean("userLiked");
        if(userLiked){
            vLikeButton.setImageResource(R.drawable.ic_action_favorite_toggled);
        }
        else{
            vLikeButton.setImageResource(R.drawable.ic_action_favorite);
        }

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

    public void dislikeEvent(View v){
        String range = bundle.getString("range_key");
        String type = bundle.getString("type");
        APIHelper.updateLike(range, type, "dislike", false);
        mDataSource.createDislike(range, type);
        super.onBackPressed();
        Toast.makeText(EventDetails.this, "Event disliked", Toast.LENGTH_SHORT).show();
    }

    public void likeEvent(View v){
        String range = bundle.getString("range_key");
        String type = bundle.getString("type");
        System.out.println(range + " " + type);
        if(userLiked){
            userLiked = !userLiked;
            mDataSource.removeLike(new StoredEvent(range, type));
            APIHelper.updateLike(range, type, "like", true);
            vLikeButton.setImageResource(R.drawable.ic_action_favorite);
        }
        else{
            userLiked = !userLiked;
            mDataSource.createLike(range, type);
            APIHelper.updateLike(range, type, "like", false);
            vLikeButton.setImageResource(R.drawable.ic_action_favorite_toggled);
        }

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
