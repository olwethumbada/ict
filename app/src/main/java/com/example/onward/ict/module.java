package com.example.onward.ict;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

public class module extends AppCompatActivity implements View.OnClickListener {

    String studentNo, password, module[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);

        studentNo = getIntent().getStringExtra("studentNo");

        BackGround bg = new BackGround();
        bg.execute(studentNo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bModBack:
                Intent back = new Intent(this, personalised.class);
                back.putExtra("name",studentNo);
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
                URL url = new URL("http://sict-iis.nmmu.ac.za/sos/Android/getModule.php");
                String urlParams = "studentNo=" + username ;

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

            if(s.equals("Empty")){
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }else {
                    G = s.split("#");
                    module = new String[G.length];
                    try{
                for (int i = 0; i < G.length; i++) {
                    JSONObject root = new JSONObject(G[i]);
                    JSONObject user_data = root.getJSONObject("user_data");
                    module[i] = user_data.getString("code");
                    GetModuleName mn = new GetModuleName();
                    mn.execute(module[i]);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: " + e.getMessage();
            }


            }
        }
    }
    class GetModuleName extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String mod = params[0];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://sict-iis.nmmu.ac.za/sos/Android/getModuleName.php");
                String urlParams = "module=" + mod;

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
            String mod = null, code=null;

            if(s.equals("error")){
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }else {

                try {
                    JSONObject root = new JSONObject(s);
                    JSONObject user_data = root.getJSONObject("user_data");
                    code = user_data.getString("Code");
                    mod = user_data.getString("Module");

                }catch (JSONException e) {
                    e.printStackTrace();
                    err = "Exception: " + e.getMessage();
                }

                Button b = new Button(getApplicationContext());
                b.setText(mod);
                b.setTag(code);
                b.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.right_arrow,0);
                b.setGravity(Gravity.LEFT);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView text = (TextView) v;
                        String mod_code = text.getTag().toString();
                        String mod_name = text.getText().toString();

                        Intent next = new Intent(getApplicationContext(), module_details.class);
                        next.putExtra("code", mod_code);
                        next.putExtra("module", mod_name);
                        next.putExtra("studentNo",studentNo);
                        startActivity(next);
                    }
                });

                LinearLayout ll = (LinearLayout) findViewById(R.id.llModules);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                ll.addView(b, lp);
            }
        }
    }
}
