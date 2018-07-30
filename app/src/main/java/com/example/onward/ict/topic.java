package com.example.onward.ict;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;

public class topic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        getTopics topics = new getTopics(this);
        topics.doInBackground();

        Button myButton = new Button(this);
        myButton.setText("Add Me");

        RelativeLayout ll = (RelativeLayout) findViewById(R.id.rlTopics);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ll.addView(myButton, lp);
    }
}
