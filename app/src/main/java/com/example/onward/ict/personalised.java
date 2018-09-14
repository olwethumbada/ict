package com.example.onward.ict;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class personalised extends AppCompatActivity implements View.OnClickListener {

    String getUserID, getUserPass;
    TextView tvUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalised);

        tvUserID = (TextView) findViewById(R.id.tvID);

        final  String userID = getIntent().getStringExtra("name");
        final String password = getIntent().getStringExtra("password");

        getUserID = userID;
        getUserPass = password;
        tvUserID.setText(userID);
    }

    public void Forum(View v){
        Intent user = new Intent(this, topic.class);
        user.putExtra("student", getUserID);
        startActivity(user);
    }
    public void Profile(View v){
        Intent user = new Intent(this, profile.class);
        user.putExtra("studentNo", getUserID);
        startActivity(user);
    }
    public void Module(View v){
        Intent user = new Intent(this, module.class);
        user.putExtra("studentNo", getUserID);
        startActivity(user);
    }

    @Override
    public void onClick(View v) {
        Intent in = new Intent(this,main.class);
        in.putExtra("studentNo", getUserID);
        in.putExtra("password", getUserPass);
        switch (v.getId()){
           case R.id.bHome:
               startActivity(in);
               break;
            case R.id.bLogout:
                in.replaceExtras(new Bundle());
                in.setAction("");
                in.setData(null);
                in.setFlags(0);
                startActivity(in);
        }
    }
}
