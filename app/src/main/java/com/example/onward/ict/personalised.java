package com.example.onward.ict;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class personalised extends AppCompatActivity {

    String userID;
    TextView tvUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalised);

        tvUserID = (TextView) findViewById(R.id.tvID);

        userID = getIntent().getStringExtra("name");

        tvUserID.setText(userID);
    }

    public void Forum(View v){
        startActivity(new Intent(personalised.this, topic.class));
    }
}
