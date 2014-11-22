package com.example.julianalouback.supnyc;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;


public class LoadingPageActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_page);

        /* New Handler to start the Menu-Activity*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(LoadingPageActivity.this, MenuActivity.class);
                LoadingPageActivity.this.startActivity(mainIntent);
                LoadingPageActivity.this.finish();
            }
        }, 5000);
    }

}
