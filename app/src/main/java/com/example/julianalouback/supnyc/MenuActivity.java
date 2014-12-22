package com.example.julianalouback.supnyc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class MenuActivity extends Activity {

    ImageButton party;
    ImageButton culture;
    ImageButton bars;
    ImageButton dining;
    Button recommended;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);
        party = (ImageButton) findViewById(R.id.party_button);
        party.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), EventListActivity.class);
                intent.putExtra("type", "party");
                startActivity(intent);
            }
        });

        culture = (ImageButton) findViewById(R.id.culture_button);
        culture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), EventListActivity.class);
                intent.putExtra("type", "culture");
                startActivity(intent);
            }
        });


        bars = (ImageButton) findViewById(R.id.bars_button);
        bars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), EventListActivity.class);
                intent.putExtra("type", "bar");
                startActivity(intent);
            }
        });

        dining = (ImageButton) findViewById(R.id.dining_button);
        dining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), EventListActivity.class);
                intent.putExtra("type", "dining");
                startActivity(intent);
            }
        });

        recommended = (Button) findViewById(R.id.recommended_button);
        recommended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), EventListActivity.class);
                intent.putExtra("type", "recommended");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up_info, R.anim.no_change);
            }
        });
    }

}
