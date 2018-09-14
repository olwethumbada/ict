package com.example.onward.ict;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class comments extends AppCompatActivity implements View.OnClickListener {
    TextView tvQuestComment, tvByWho, tvNumber;
    String forumId, forum, question, composer, studentNo;
    int number;
    ListView listView;
    EditText etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        tvQuestComment = findViewById(R.id.tvQuestComment);
        tvByWho = findViewById(R.id.tvBy);
        tvNumber = findViewById(R.id.tvNumberOfComments);
        etMessage = findViewById(R.id.etComment);


        info();
    }

    private void info() {
        studentNo = getIntent().getStringExtra("studentNo");
        question = getIntent().getStringExtra("question");
        composer = getIntent().getStringExtra("composer");
        GetForumId fi = new GetForumId();
        fi.execute(question);

        tvQuestComment.setText(question);
        tvByWho.setText(composer);
    }

    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
        String time_now = mdformat.format(calendar.getTime());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String mydate = dateFormat.format(new Date());

        String comment = etMessage.getText().toString().trim();
        String check[] = null;

        check = comment.split("#");

        switch (v.getId()) {
            case R.id.bAddComment:
                if (check.length > 1) {
                    Toast.makeText(getApplicationContext(), "Do not put in this (#) special character.", Toast.LENGTH_LONG).show();
                } else if (comment.equals("")) {
                    Toast.makeText(getApplicationContext(), "Type in a Text.", Toast.LENGTH_LONG).show();
                } else if (comment.equals("#")) {
                    Toast.makeText(getApplicationContext(), "Type in a Text and dont put in this special character.", Toast.LENGTH_LONG).show();
                }
                AddComment ac = new AddComment();
                ac.execute(studentNo, forumId, comment, mydate, time_now);

                info();
                etMessage.setText("");
                break;
            case R.id.bBackForum:
                startActivity(new Intent(this, forum.class));
        }
    }

    class GetForumId extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String question = strings[0];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://sict-iis.nmmu.ac.za/sos/Android/getForumId.php");
                String urlParams = "question=" + question;

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

            if (s.equals("error")) {

            } else {
                try {
                    JSONObject root = new JSONObject(s);
                    JSONObject user_data = root.getJSONObject("user_data");
                    forumId = user_data.getString("ForumId");

                } catch (JSONException e) {
                    e.printStackTrace();
                    err = "Exception: " + e.getMessage();
                }
            }

            GetNoOfComments nc = new GetNoOfComments();
            nc.execute(forumId);
            GetComments gc = new GetComments();
            gc.execute(forumId);
        }
    }

    class GetNoOfComments extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String forumId = strings[0];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://sict-iis.nmmu.ac.za/sos/Android/getNumberOfComments.php");
                String urlParams = "forumId=" + forumId;

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
            String num = null;

            if (s.equals("error")) {

            } else {
                try {
                    JSONObject root = new JSONObject(s);
                    JSONObject user_data = root.getJSONObject("user_data");
                    num = user_data.getString("Number");

                } catch (JSONException e) {
                    e.printStackTrace();
                    err = "Exception: " + e.getMessage();
                }
            }

            number = Integer.parseInt(num);

            if (number == 0) {
                tvNumber.setText("No Comments");
            } else if (number == 1) {
                tvNumber.setText(num + " Comment");
            } else {
                tvNumber.setText(num + " Comments");
            }

        }
    }

    class GetComments extends AsyncTask<String, String, String> {
        String err = null;

        @Override
        protected String doInBackground(String... params) {
            String forum = params[0];
            String data = "";

            int tmp;

            try {
                URL url = new URL("http://sict-iis.nmmu.ac.za/sos/Android/getComments.php");
                String urlParams = "forumId=" + forum;

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
            String question[] = null;
            String studentNo[] = null;
            String date[] = null;
            String time[] = null;
            String qID[] = null;

            if (s.equals("error")) {

            } else {
                try {

                    G = s.split("#");
                    question = new String[G.length];
                    studentNo = new String[G.length];
                    date = new String[G.length];
                    time = new String[G.length];
                    qID = new String[G.length];

                    for (int i = 0; i < G.length; i++) {

                        JSONObject root = new JSONObject(G[i]);
                        JSONObject user_data = root.getJSONObject("user_data");
                        studentNo[i] = user_data.getString("StudentNo");
                        question[i] = user_data.getString("Comment");
                        date[i] = user_data.getString("Date");
                        time[i] = user_data.getString("Time");

                        studentNo[i] += "\n" + date[i] + " - " + time[i];
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    err = "Exception: " + e.getMessage();
                }

                listView = (ListView) findViewById(R.id.lvComments);

                final HashMap<String, String> listMap = new HashMap<>();

                for (int i = 0; i < question.length; i++) {
                    listMap.put(question[i], studentNo[i]);
                }

                List<HashMap<String, String>> listItems = new ArrayList<>();
                final SimpleAdapter ad = new SimpleAdapter(getApplicationContext(), listItems, R.layout.list_comment, new String[]{"First Line", "Second Line"}, new int[]{R.id.ItemComment, R.id.subItemComment});
                Iterator it = listMap.entrySet().iterator();

                while (it.hasNext()) {
                    HashMap<String, String> results = new HashMap<>();
                    Map.Entry pair = (Map.Entry) it.next();
                    results.put("First Line", pair.getKey().toString());
                    results.put("Second Line", pair.getValue().toString());
                    listItems.add(results);
                }

                listView.setAdapter(ad);
            }
        }
    }

    class AddComment extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String studentId = strings[0];
            String fid = strings[1];
            String comment = strings[2];
            String date = strings[3];
            String time = strings[4];

            String data = "";
            int tmp;

            try {
                URL url = new URL("http://sict-iis.nmmu.ac.za/sos/Android/addComment.php");
                String urlParams = "StudentNo=" + studentId + "&ForumId=" + fid + "&Comment=" + comment + "&Date=" + date + "&Time=" + time;

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
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

        }
    }
}
