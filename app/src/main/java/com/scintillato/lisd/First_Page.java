package com.scintillato.lisd;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class First_Page extends AppCompatActivity {

    EditText pass, uname;
    Button submit, regi;
    Context ctx;
    ValidateDataJSON g;
    private InputStream IS;
    ProgressDialog loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        ctx = this;
        pass = (EditText) findViewById(R.id.et_password);
        uname = (EditText) findViewById(R.id.et_username);

        submit = (Button) findViewById(R.id.btn_submit);
        regi = (Button) findViewById(R.id.btn_register);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uname.getText().toString().length() > 0 && pass.getText().toString().length() > 0) {
                    g = new ValidateDataJSON();
                    g.execute(uname.getText().toString(), pass.getText().toString());
                } else {
                    Toast.makeText(ctx, "Enter User name or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        regi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, LaunchActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    class ValidateDataJSON extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String register_url = "http://lidsmysqldb.cloudapp.net/sih2017/lids-api/login.php";

            String username, password;

            String result = null;

            username = params[0];
            password = params[1];

            try {

                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

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
            loading = ProgressDialog.show(ctx, "Status", "Validating...", true, true);
            loading.setCancelable(false);
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
            loading.cancel();

            if (s.equals("authenication failed !")) {
                Toast.makeText(ctx, "Wrong Credentials", Toast.LENGTH_SHORT).show();
            } else if (s.equals("success")) {
                Intent intent = new Intent(ctx, searchActivity.class);
                startActivity(intent);
            }
        }
    }
}
