package com.example.onward.ict;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Admin_category extends AppCompatActivity implements View.OnClickListener {

    TextView tvList;
    LinearLayout parent;
    Button myButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        ReadCategory();

    }
    private void ReadCategory(){
        parent = (LinearLayout)findViewById(R.id.llMain);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        for(int i = 0; i < 3; i++){
            myButton = new Button(this);
            parent.addView(myButton, lp);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.fbAddCategory:
                startActivity(new Intent(this, add_category.class));
                break;
            case R.id.clBack:
                startActivity(new Intent(this, Admin_home.class));
                break;
        }
    }
}
