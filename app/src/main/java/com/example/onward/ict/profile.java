package com.example.onward.ict;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class profile extends AppCompatActivity implements View.OnClickListener {
    String studentNo;
    TextView tvName, tvQuali, tvFac, tvCamp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = (TextView) findViewById(R.id.tvName);
        tvQuali = (TextView) findViewById(R.id.tvQualification);
        tvFac = (TextView) findViewById(R.id.tvFacultyPro);
        tvCamp = (TextView) findViewById(R.id.tvCompany);

         studentNo = getIntent().getStringExtra("studentNo");

        BackGround bg = new BackGround();
        bg.execute(studentNo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bProfBack:
                Intent back = new Intent(this,personalised.class);
                back.putExtra("name", studentNo);
                startActivity(back);
                break;
        }
    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://sict-iis.nmmu.ac.za/sos/Android/getProfiles.php");
                String urlParams = "studentNo=" + username;

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

        @Override
        protected void onPostExecute(String s) {
            String err = null;
            String G[] = null;

            if(s.equals("error")){
                Toast.makeText(getApplicationContext(), "No information on this yet.", Toast.LENGTH_SHORT).show();
            }else {
                try {
                    JSONObject root = new JSONObject(s);
                    JSONObject user_data = root.getJSONObject("user_data");
                    tvName.setText( user_data.getString("Default"));
                    tvQuali.setText( user_data.getString("Stream"));
                    tvFac.setText( user_data.getString("Faculty"));
                    tvCamp.setText( user_data.getString("Campus"));

                }catch (JSONException e) {
                    e.printStackTrace();
                    err = "Exception: " + e.getMessage();
                }
            }
        }
    }
}
