package com.example.onward.ict;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Admin_home extends AppCompatActivity implements View.OnClickListener {

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
    }
    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.bDiscussions:

                break;
        }
    }
    class BackGround extends AsyncTask<String, String, String> {
        String err = null;
        String NAME[];

        @Override
        protected String doInBackground(String... params) {
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://10.102.139.130/Android/getCategory.php");
                //String urlParams = "username=" + username +"&"+ "password=" + password;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                //os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                //return data;

                String G[];

                G = data.split("#");
                NAME = new String[G.length];

                for (int i = 0; i < G.length; i++) {

                    JSONObject root = new JSONObject(G[i]);
                    JSONObject user_data = root.getJSONObject("user_data");
                    NAME[i] = user_data.getString("Name");
                }
                return NAME[G.length];
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (JSONException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            String err = null;


            Intent i = new Intent(context, personalised.class);

            
                startActivity(i);
        }
    }
}
