package com.example.onward.ict;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class home extends AppCompatActivity implements View.OnClickListener {

    EditText name, password;
    String Name, Password;
    Context context = this;
    String NAME = null, ROLE = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        name = (EditText) findViewById(R.id.main_name);
        password = (EditText) findViewById(R.id.main_password);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.main_login:
                    Name = name.getText().toString();
                    Password = password.getText().toString();
                    BackGround b = new BackGround();
                    b.execute(Name, Password);
                break;
        }
    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://10.102.139.130/Android/login.php");
                String urlParams = "username=" + username + "&" + "password=" + password;

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
            try {
                JSONObject root = new JSONObject(s);
                JSONObject user_data = root.getJSONObject("user_data");
                NAME = user_data.getString("StudentNo");
                ROLE = user_data.getString("Role");
            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: " + e.getMessage();
            }

            Intent user = new Intent(context, personalised.class);
            Intent admin = new Intent(context, Admin_home.class);

            user.putExtra("name", NAME);
            user.putExtra("err", err);

            if (ROLE.contains("Admin")) {
                startActivity(admin);
            } else {
                startActivity(user);
            }
        }
    }
}
