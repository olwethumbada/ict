package com.example.onward.ict;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class getTopics extends AsyncTask<Void, Void, String> {
    Context context;
    ProgressDialog alertDialog;
    private String NAME = "";


    public getTopics(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        int tmp;
        String data = "";

        try {
            URL url = new URL("http://10.102.139.130/Android/getTopics");
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
            NAME = user_data.getString("Name");
        } catch (JSONException e) {
            e.printStackTrace();
            err = "Exception: " + e.getMessage();
        }

    }
}

