package com.example.onward.ict;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class main extends AppCompatActivity implements View.OnClickListener {

    String getStudentNo = null;
    String getPassword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getInfo();

    }

    @Override
    public void onClick(View v) {
        Intent in = new Intent(this, personalised.class);


        switch (v.getId()) {
            case R.id.ibMaps:
                startActivity(new Intent(this, north_map.class));
                break;
            case R.id.fbFacebook:
                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/NelsonMandelaUni.IT/"));
                startActivity(browser);
                break;
            case R.id.ibLogin:
                if (getStudentNo != null && getPassword != null) {
                    in.putExtra("name", getStudentNo);
                    in.putExtra("password", getPassword);
                    startActivity(in);
                } else {
                    startActivity(new Intent(this, home.class));
                }
        }

    }

    private void getInfo() {
        getStudentNo = getIntent().getStringExtra("studentNo");
        getPassword = getIntent().getStringExtra("password");
    }
}
