package com.example.onward.ict;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Array;
import java.util.jar.Attributes;

public class topic extends AppCompatActivity {

    String NAME[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);


BackGround b = new BackGround();
b.execute();
        /*Button myButton = new Button(this);
        myButton.setText("Add Me");

        RelativeLayout ll = (RelativeLayout) findViewById(R.id.rlTopics);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ll.addView(myButton, lp);*/
    }
    class BackGround extends AsyncTask<String, String, String> {
        String err = null;

        @Override
        protected String doInBackground(String... params) {
            //String username = params[0];
            //String password = params[1];
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

                for(int i = 0; i < G.length; i++){

                JSONObject root = new JSONObject(G[i]);
                JSONObject user_data = root.getJSONObject("user_data");


                    NAME = new String[G.length];
                    NAME[i] = user_data.getString("Name");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: " + e.getMessage();
            }

            return NAME[0];
        }

        @Override
        protected void onPostExecute(String s) {
            String err = null;
            try {
                JSONObject root = new JSONObject(s);
                JSONObject user_data = root.getJSONObject("user_data");
int o = user_data.length();
                for(int i = 0; i < user_data.length(); i++){
                    NAME[i] = user_data.getString("Name");
                }

            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: " + e.getMessage();
            }

            //Intent i = new Intent(ctx, personalised.class);
            //i.putExtra("name", NAME);
            //i.putExtra("err", err);
            //startActivity(i);

        }
    }
}
