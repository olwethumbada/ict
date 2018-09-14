package com.example.onward.ict;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.EventLogTags;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Admin_category extends AppCompatActivity implements View.OnClickListener {
    String error = "";
    String NAME[];
    String ID[];
    String DESCRIPTION[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        BackGround b = new BackGround();
        b.execute();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fbAddCategory:
                startActivity(new Intent(this, add_category.class));
                break;
            case R.id.clBack:
                startActivity(new Intent(this, Admin_home.class));
                break;
        }
    }
    class BackGround extends AsyncTask<String, String, String> {
        String err = null;
        String scope = "";
        Category ct = null;

        @Override
        protected String doInBackground(String... params) {
            String data = "";

            int tmp;

            try {
                URL url = new URL("http://sict-iis.nmmu.ac.za/sos/Android/getCategory.php");

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            String err = null;
            String G[] = null;
            try {
                G = s.split("#");
                NAME = new String[G.length];
                ID = new String[G.length];
                DESCRIPTION = new String[G.length];

                for(int i = 0; i < G.length; i++){
                    JSONObject root = new JSONObject(G[i]);
                    JSONObject user_data = root.getJSONObject("user_data");
                    ID[i]= user_data.getString("CategoryID");
                    NAME[i] = user_data.getString("Name");
                   DESCRIPTION[i] = user_data.getString("Description");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: " + e.getMessage();
            }

            for (int i = 0; i < G.length; i++){
                Button b = new Button(getApplicationContext());
                b.setText(NAME[i]);

                LinearLayout ll = (LinearLayout)findViewById(R.id.llMain);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                ll.addView(b, lp);
            }
        }
    }
}

