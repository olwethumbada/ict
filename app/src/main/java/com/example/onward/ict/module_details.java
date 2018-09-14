package com.example.onward.ict;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
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

public class module_details extends AppCompatActivity implements View.OnClickListener {
    String mod_code, mode_name, studentNo;
    TextView tvHead, tvLecturer, tvOffice, tvOfficeNo, tvEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_details);
        getInfo();


    }
    private void getInfo(){
        studentNo = getIntent().getStringExtra("studentNo");
        mod_code = getIntent().getStringExtra("code");
        mode_name = getIntent().getStringExtra("module");

        tvHead = (TextView) findViewById(R.id.tvModDetHead);
        tvLecturer = (TextView) findViewById(R.id.tvModDetLecName);
        tvOffice = (TextView) findViewById(R.id.tvModOffice);
        tvOfficeNo = (TextView) findViewById(R.id.tvModOfficeNo);
        tvEmail = (TextView) findViewById(R.id.tvModEmail);

        tvHead.setText(mode_name+"\n("+mod_code+")");

        BackGround bg = new BackGround();
        bg.execute(mod_code);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bModDetBack:
                Intent back = new Intent(this, module.class);
                back.putExtra("studentNo", studentNo);
                startActivity(back);
                break;
        }
    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String module_code = params[0];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://sict-iis.nmmu.ac.za/sos/Android/getStaffbyCode.php");
                String urlParams = "code=" + module_code;

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
                    tvLecturer.setText( user_data.getString("Name"));
                    tvOffice.setText( user_data.getString("Office"));
                    tvOfficeNo.setText( user_data.getString("Phone"));
                    tvEmail.setText( user_data.getString("Email"));

                }catch (JSONException e) {
                    e.printStackTrace();
                    err = "Exception: " + e.getMessage();
                }
            }
        }
    }
}
