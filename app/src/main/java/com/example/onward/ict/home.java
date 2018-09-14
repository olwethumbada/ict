package com.example.onward.ict;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class home extends AppCompatActivity implements View.OnClickListener {

    EditText name, password;
    TextView error;
    String Name, Password;
    Context context = this;

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

                if (name.getText().toString().matches(""))
                    Toast.makeText(this, "You did not enter a Username", Toast.LENGTH_SHORT).show();
                else if (password.getText().toString().matches(""))
                    Toast.makeText(this, "You did not enter a Password", Toast.LENGTH_SHORT).show();
                else {
                    Name = name.getText().toString();
                    Password = password.getText().toString();
                    BackGround b = new BackGround();
                    b.execute(Name.trim(), Password.trim());
                    break;
                }
                break;
            case R.id.bBack:
                startActivity(new Intent(context, main.class));
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
                URL url = new URL("http://sict-iis.nmmu.ac.za/sos/Android/login.php");
                String urlParams = "studentNo=" + username + "&" + "password=" + password;

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
            String pass = password.getText().toString();

            if(s.equals("Bind successful"))
            {
                Intent user = new Intent(context, personalised.class);
                Intent pop = new Intent(context, login_popup.class);

                pop.putExtra("password", pass);
                pop.putExtra("name", Name.substring(0,10));

                user.putExtra("password", pass);
                user.putExtra("name", Name.substring(0,10));

                if (Name.substring(1,10).contains("214221431")) {
                    startActivity(pop);
                } else {
                    startActivity(user);
                }

            }else {
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
