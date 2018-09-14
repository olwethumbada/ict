package com.example.onward.ict;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class forum extends AppCompatActivity implements View.OnClickListener {
    String Name, Description[];
    EditText etQuest;
    String student_no, catId, catName;
    private Spinner spinner;
    private static final String[] paths = {"item 1", "item 2", "item 3"};
    ListView listView;
    TextView textForum;


    String NO, TPID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        Info();
    }
    private void Info(){
        student_no = getIntent().getStringExtra("student");
        catId = getIntent().getStringExtra("catId");
        catName = getIntent().getStringExtra("catName");
        NO = student_no;
        etQuest = (EditText) findViewById(R.id.message_text);
        textForum = (TextView) findViewById(R.id.tvForum);

        textForum.setText(catName);

        etQuest.setOnClickListener(this);

        GetTopics gt = new GetTopics();
        gt.execute(catId);

        Name = getIntent().getStringExtra("ID");
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.bBackQues) {
            startActivity(new Intent(this, topic.class));
        } else {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
            String time_now = mdformat.format(calendar.getTime());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String mydate = dateFormat.format(new Date());

            String quest = etQuest.getText().toString().trim();
            String check[] = null;

            check = quest.split("#");

            if (check.length > 1) {
                Toast.makeText(getApplicationContext(), "Do not put in this (#) special character.", Toast.LENGTH_LONG).show();
            } else if (quest.equals("")) {
                Toast.makeText(getApplicationContext(), "Type in a Text.", Toast.LENGTH_LONG).show();
            }else if(quest.equals("#")){
                Toast.makeText(getApplicationContext(), "Type in a Text and dont put in this special character.", Toast.LENGTH_LONG).show();
            } else{


                switch (v.getId()) {
                    case R.id.send_button:
                        AddQuestion aq = new AddQuestion();
                        aq.execute(NO, mydate, time_now, quest, catId);

                        GetTopics gt = new GetTopics();
                        gt.execute(catId);
                        etQuest.setText("");
                        break;
                }
            }
        }
    }

    class GetTopics extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String categoryId = params[0];
            String data = "";

            int tmp;

            try {
                URL url = new URL("http://sict-iis.nmmu.ac.za/sos/Android/getQuestions.php");
                String urlParams = "categoryId=" + categoryId;

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
                        qID[i] = user_data.getString("ForumId");
                        question[i] = user_data.getString("Question");
                        studentNo[i] = "By "+user_data.getString("StudentNo");
                        date[i] = user_data.getString("Date");
                        time[i] = user_data.getString("Time");

                        studentNo[i] += "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + date[i] + "\t" + "\t" + "\t" + time[i];
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    err = "Exception: " + e.getMessage();
                }

                listView = (ListView) findViewById(R.id.forumMain);

                final HashMap<String, String> listMap = new HashMap<>();

                for (int i = 0; i < question.length; i++) {
                    listMap.put(question[i], studentNo[i]);
                }

                List<HashMap<String, String>> listItems = new ArrayList<>();

                final SimpleAdapter ad = new SimpleAdapter(getApplicationContext(), listItems, R.layout.list_item, new String[]{"First Line", "Second Line"}, new int[]{R.id.Item, R.id.subItem});
                Iterator it = listMap.entrySet().iterator();

                while (it.hasNext()) {
                    HashMap<String, String> results = new HashMap<>();
                    Map.Entry pair = (Map.Entry) it.next();
                    results.put("First Line", pair.getKey().toString());
                    results.put("Second Line", pair.getValue().toString());
                    listItems.add(results);
                }

                listView.setAdapter(ad);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String text = ad.getItem(position).toString();
                        String name = text.substring(67, text.length()-1);
                        String result = text.substring(13, 54);

                        String ta = (String)view.getTag();

                        Intent comm = new Intent(getApplicationContext(), comments.class);

                        comm.putExtra("studentNo", student_no);
                        comm.putExtra("question", name);
                        comm.putExtra("composer", result);
                        startActivity(comm);
                    }
                });

            }
        }
    }
    class AddQuestion extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String studenId = strings[0];
            String date = strings[1];
            String time = strings[2];
            String question = strings[3];
            String categoryId = strings[4];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://sict-iis.nmmu.ac.za/sos/Android/addQuestion.php");
                String urlParams = "StudentNo=" + studenId + "&Date=" + date + "&Time=" + time + "&Question=" + question + "&CategoryId=" + categoryId;

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
