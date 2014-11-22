package com.example.julianalouback.supnyc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;


public class MenuActivity extends Activity {

    ImageButton party;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);
        party = (ImageButton) findViewById(R.id.party_button);
        party.setOnClickListener(new MenuOnClickListener("party"));
    }

}
