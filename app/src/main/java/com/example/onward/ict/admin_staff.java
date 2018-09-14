package com.example.onward.ict;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class admin_staff extends AppCompatActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_staff);
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
                URL url = new URL("http://sict-iis.nmmu.ac.za/sos/Android/getStaff.php");
                String urlParams = "topic_name=" + Name;

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
            String staffId[] = null;
            String name[] = null;
            String email[] = null;
            String phoneNo[] = null;
            String officeNo[] = null;
            String faxNo[] = null;
            String string[] = null;

            if (s.equals("error")) {

            } else {
                try {

                    G = s.split("#");
                    staffId = new String[G.length];
                    name = new String[G.length];
                    email = new String[G.length];
                    phoneNo = new String[G.length];
                    officeNo = new String[G.length];
                    faxNo = new String[G.length];
                    string = new String[G.length];

                    for (int i = 0; i < G.length; i++) {
                        JSONObject root = new JSONObject(G[i]);
                        JSONObject user_data = root.getJSONObject("user_data");
                        staffId[i] = user_data.getString("StaffId");
                        name[i] = user_data.getString("Name");
                        email[i] = user_data.getString("Email");
                        phoneNo[i] = user_data.getString("PhoneNo");
                        officeNo[i] = user_data.getString("OfficeNo");
                        faxNo[i] = user_data.getString("FaxNo");

                        string[i] += "Email: \t"+ email[i]+"\nPhone No.: \t" +phoneNo[i]+
                                "\nOffice No.: \t" +officeNo[i]+"\nFax No.: \t" +faxNo[i];
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    err = "Exception: " + e.getMessage();
                }

                listView = (ListView) findViewById(R.id.forumMain);

                HashMap<String, String> listMap = new HashMap<>();

                for (int i = 0; i < name.length; i++) {
                    listMap.put(name[i], string[i]);
                }

                List<HashMap<String, String>> listItems = new ArrayList<>();
                SimpleAdapter ad = new SimpleAdapter(getApplicationContext(), listItems, R.layout.list_item, new String[]{"First Line", "Second Line"}, new int[]{R.id.Item, R.id.subItem});
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
                        //String text = parent.getTag().toString();

                        //String name = text.get ;

                        LinearLayout linearLayoutParent = (LinearLayout) view;
                        LinearLayout linearLayoutChild = (LinearLayout ) linearLayoutParent.getChildAt(position);
                        TextView tvCountry = (TextView) linearLayoutChild.getChildAt(position);

                        String text = tvCountry.getText().toString();
                    }
                });

            }
        }
    }
}
