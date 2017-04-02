package com.scintillato.lisd;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class searchActivity extends AppCompatActivity {

    Button search;
    EditText veh_id;
    Context ctx;
    private String myJSON;
    locationDataJSON g;
    private ArrayList<Postion> test;
    private InputStream IS;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        test = new ArrayList<Postion>();
        ctx = this;
        veh_id = (EditText) findViewById(R.id.et_vehicle_id);
        search = (Button) findViewById(R.id.btn_search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (veh_id.getText().length() > 0) {
                    g = new locationDataJSON();
                    g.execute(veh_id.getText().toString());
                } else if (veh_id.getText().length() == 0) {
                    Toast.makeText(ctx, "Enter a Vehicle id ", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    class locationDataJSON extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String register_url = "http://lidsmysqldb.cloudapp.net/sih2017/lids-api/fetchTripInfo.php";
            String uuid = params[0];

            String result = null;
            try {

                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("uuid", "UTF-8") + "=" + URLEncoder.encode(uuid, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                StringBuilder sb = new StringBuilder();

                IS = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS, "iso-8859-1"));
                String response = "";
                String line = "";
                line = bufferedReader.readLine();

                result = line;
                bufferedReader.close();
                IS.close();
                httpURLConnection.disconnect();
                System.out.println(response);
            } catch (Exception e) {
                // Oops
            } finally {
                try {
                    if (IS != null) IS.close();
                } catch (Exception squish) {
                }
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            loading = ProgressDialog.show(ctx, "Status", "Fetching...", true, true);
            loading.setCancelable(false);
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
            loading.cancel();
            myJSON = s;
            makeList();
        }

        private JSONObject jsonObject;
        private JSONArray jsonArray;

        public void makeList() {

            Log.d("json", myJSON);
            String finalJSON = "{ \"result\" : " + myJSON.toString() + "}";
            try {
                jsonObject = new JSONObject(finalJSON);
                jsonArray = jsonObject.getJSONArray("result");
                int count = 0;
                String latitude, longitude, timeMs;
                long tmp_timeMs;
                double tmp_lati, tmp_long;
                Log.d("length", jsonArray.length() + "");
                while (count < jsonArray.length()) {
                    Intent intent = null, chooser = null;
                    JSONObject JO = jsonArray.getJSONObject(count);
                    longitude = JO.getString("longitude7E");
                    latitude = JO.getString("latitude7E");
                    timeMs = JO.getString("timestampMs");
                    tmp_timeMs = Long.parseLong(timeMs);
                    tmp_lati = Double.parseDouble(latitude);
                    tmp_long = Double.parseDouble(longitude);
                    Postion Pos = new Postion(tmp_lati, tmp_long, tmp_timeMs);
                    test.add(Pos);
                    count++;
                    Log.d("count", count + "");
                }
                for(int i=0;i<test.size();i++){
                    Log.d("list",test.get(i).getLatitude().toString()+"   "+test.get(i).getLongitude().toString());
                }
                Intent intent = new Intent(ctx,MapsActivity.class);
                intent.putExtra("test", test);
                startActivity(intent);

            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }
    }
    @Override
    protected void onPause() {
        if(g!=null)
        {
            g.cancel(true);
            loading.cancel();
        }

        super.onPause();
    }
    @Override
    protected void onStop() {
        if(g!=null)
        {
            g.cancel(true);
            loading.cancel();
        }
        super.onStop();
    }
}
