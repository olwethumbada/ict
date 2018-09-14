package com.example.onward.ict;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
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
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;

public class topic extends AppCompatActivity implements View.OnClickListener {
    String NAME[], ID[];
    String getID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        getID = getIntent().getStringExtra("student");

        BackGround b = new BackGround();
        b.execute();
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.bBackDisc) {
            Intent back = new Intent(this, personalised.class);
            back.putExtra("name", getID);
            startActivity(back);
        } else {
            String id = view.getTag().toString();
            if (id.equals("")) {

            } else {
                Intent user = new Intent(this, forum.class);
                user.putExtra("Name", id);
                startActivity(user);
            }
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

        @SuppressLint("ResourceType")
        @Override
        protected void onPostExecute(String s) {
            String err = null;
            String G[] = null;
            try {
                G = s.split("#");
                NAME = new String[G.length];
                ID = new String[G.length];

                for (int i = 0; i < G.length; i++) {
                    JSONObject root = new JSONObject(G[i]);
                    JSONObject user_data = root.getJSONObject("user_data");
                    ID[i] = user_data.getString("CategoryID");
                    NAME[i] = user_data.getString("Name");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: " + e.getMessage();
            }

            for (int i = 0; i < G.length; i++) {
                Button b = new Button(getApplicationContext());
                b.setText(NAME[i]);
                b.setTag(ID[i]);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView txt = (TextView) v;
                        String catId = txt.getTag().toString();
                        String catName = txt.getText().toString();

                        Intent next = new Intent(getApplicationContext(), forum.class);
                        next.putExtra("catId", catId);
                        next.putExtra("catName", catName);
                        next.putExtra("student", getID);
                        startActivity(next);
                    }
                });

                LinearLayout ll = (LinearLayout) findViewById(R.id.llTopicMain);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                ll.addView(b, lp);
            }
        }

        class GetTopics extends AsyncTask<String, String, String> {
            String err = null;
            String scope = "";
            Category ct = null;

            @Override
            protected String doInBackground(String... params) {
                String Name = params[0];
                String data = "";

                int tmp;

                try {
                    URL url = new URL("http://sict-iis.nmmu.ac.za/sos/Android/getTopics.php");
                    String urlParams = "name=" + Name;

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoOutput(true);
                    OutputStream os = httpURLConnection.getOutputStream();
                    os.write(urlParams.getBytes());
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

            @SuppressLint("ResourceType")
            @Override
            protected void onPostExecute(String s) {
                String err = null;
                String G[] = null;
                String topic_name[] = null;
                String topic_descriprion[] = null;

                if (s.equals("error")) {
                    Toast.makeText(topic.this, "The selected category does not have Topics", Toast.LENGTH_LONG).show();
                } else {


                    try {

                        G = s.split("#");
                        topic_name = new String[G.length];
                        topic_descriprion = new String[G.length];

                        for (int i = 0; i < G.length; i++) {
                            JSONObject root = new JSONObject(G[i]);
                            JSONObject user_data = root.getJSONObject("user_data");
                            topic_name[i] = user_data.getString("Name");
                            topic_descriprion[i] = user_data.getString("Description");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        err = "Exception: " + e.getMessage();
                    }

                /*for (int i = 0; i < G.length; i++) {
                    Button b = new Button(getApplicationContext());
                    b.setText(topic_name[i]);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TextView txt = (TextView) v;

                            String text = txt.getText().toString();

                            if (text.equals("")) {
                                Toast.makeText(getApplicationContext(), "No questions on this topic", Toast.LENGTH_LONG).show();
                            } else {
                                Intent next = new Intent(getApplicationContext(), forum.class);
                                next.putExtra("name", text);
                                next.putExtra("student", id);
                                startActivity(next);
                            }
                        }
                    });

                    LinearLayout ll = (LinearLayout) findViewById(R.id.llTopicMain);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    ll.addView(b, lp);*/
                }
            }
        }
    }
}
