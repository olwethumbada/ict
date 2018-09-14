package com.example.onward.ict;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class add_category extends AppCompatActivity implements View.OnClickListener {

    EditText name, description;
    String Name, Description;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        name = (EditText) findViewById(R.id.etName);
        description = (EditText) findViewById(R.id.etDescription);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bSubmit:
                if(name.getText().equals("")) {
                    Toast.makeText(this,"Type in the category name", Toast.LENGTH_SHORT).show();
                } else if(description.getText().equals("")){
                    Toast.makeText(this,"Type in the description of the category", Toast.LENGTH_SHORT).show();
                } else{

                    Name = name.getText().toString();
                    Description = description.getText().toString();

                    BackGround b = new BackGround();
                    b.execute(Name, Description);

                    name.setText("");
                    description.setText("");

                    startActivity(new Intent(context, Admin_category.class));
                }
                break;
            case R.id.acBack:
                startActivity(new Intent(context, Admin_category.class));
                break;
        }
    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String name = strings[0];
            String description = strings[1];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://sict-iis.nmmu.ac.za/sos/Android/addCategory.php");
                String urlParams = "name=" + name + "&description=" + description;

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
            if(s.equals("")){
                s="Data saved successfully.";
            }
            Toast.makeText(context, s, Toast.LENGTH_LONG).show();
        }
    }
}
