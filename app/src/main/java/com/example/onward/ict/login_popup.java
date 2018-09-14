package com.example.onward.ict;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class login_popup extends Activity implements View.OnClickListener{
    RadioGroup radioGroup;
    RadioButton radioButton;
    String studentNo;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        radioGroup = (RadioGroup) findViewById(R.id.rbGroup);
        studentNo = getIntent().getStringExtra("name");
        password = getIntent().getStringExtra("password");

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.3));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);
    }

    @Override
    public void onClick(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        Intent admin = new Intent(this, Admin_home.class);
        Intent user = new Intent(this, personalised.class);
        user.putExtra("name", studentNo);
        user.putExtra("password",password);
        admin.putExtra("name", studentNo);

        radioButton = findViewById(radioId);

        if (radioButton.getText().equals("Admin")){
            startActivity(admin);
        }else  if(radioButton.getText().equals("User")){
            startActivity(user);
        }
    }
}
