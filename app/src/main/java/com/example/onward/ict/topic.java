package com.example.onward.ict;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

public class topic extends AppCompatActivity implements View.OnClickListener {
    LinearLayout parent;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        parent = (LinearLayout)findViewById(R.id.llCategory);
        Button myButton;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        try{
            BackGround b = new BackGround();
            b.execute();


            for (int i = 0; i < b.NAME.length; i++){

                myButton = new Button(this);
                myButton.setText(b.NAME[i]);

                parent.addView(myButton);
                myButton.setOnClickListener(topic.this);
            }
        }catch (Exception e){
            progressDialog.setMessage(e.getMessage());
            progressDialog.show();
        }


    }

    @Override
    public void onClick(View view) {

    }
    class BackGround extends AsyncTask<String, String, String> {
        String err = null;
        String NAME[];

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
                NAME = new String[G.length];

                for(int i = 0; i < G.length; i++){

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
            }catch (JSONException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {


        }
    }
}
