package com.example.onward.ict;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class north_map extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_north_map);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvLink:
                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://findme-sc.mandela.ac.za/MapBuddy/mapBuddyNavigation.do"));
                startActivity(browser);
        }
    }
}
