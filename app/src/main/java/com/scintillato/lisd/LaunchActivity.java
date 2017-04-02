package com.scintillato.lisd;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class LaunchActivity extends AppCompatActivity {

    EditText lic_number,veh_number,passwor;
    TextView uid,first_name,last_name,middle_name;
    String uname,uuid,v_id,l_number,pass;
    sendDataJSON g;
    private InputStream IS;
    ProgressDialog loading;
    Context ctx;

    Button btn_anv_submit,btn_anv_reset,btn_start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        // Show the Up button in the action bar.
        ctx=this;
        first_name=(TextView) findViewById(R.id.et_anv_first_name);
        last_name=(TextView) findViewById(R.id.et_anv_last_name);
        middle_name=(TextView) findViewById(R.id.et_anv_middle_name);
        lic_number=(EditText) findViewById(R.id.et_license_number);
        veh_number=(EditText) findViewById(R.id.et_vehicle_id);
        uid=(TextView)findViewById(R.id.tv_uid);
        passwor=(EditText)findViewById(R.id.et_password);
        uuid=null;
        uname=null;
        v_id=null;
        l_number=null;
        pass=null;
        init();
       // setupActionBar();
    }

    private void init() {
        // TODO Auto-generated method stub

        btn_anv_submit = (Button) findViewById(R.id.et_anv_submit);
        btn_anv_reset = (Button) findViewById(R.id.et_anv_reset);
        btn_start = (Button) findViewById(R.id.button1);

        btn_start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {

                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes

                    startActivityForResult(intent, 0);

                } catch (Exception e) {

                    Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
                    startActivity(marketIntent);

                }

            }
        });

        btn_anv_submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0) {

                int flag=1;
                if(lic_number.getText().equals("")||lic_number.getText().length()!=10){
                    flag=0;
                }
                if(veh_number.getText().equals("")){
                    flag=0;
                }
                if(passwor.getText().equals("")){
                    flag=0;
                }
                if(flag!=0){
                    uuid= uid.getText().toString();
                     v_id=veh_number.getText().toString();
                     l_number=lic_number.getText().toString();
                    pass=passwor.getText().toString();
                    g=new sendDataJSON();
                    g.execute(uuid,v_id,l_number,uname,pass);
                }
            }
        });
        btn_anv_reset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
            }


        });
    }
    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 0) {

                if (resultCode == RESULT_OK) {
                    String contents = data.getStringExtra("SCAN_RESULT");
                    Log.e("scan result"," "+contents);
                    try {


                        AadhaarCard newCard = new AadhaarXMLParser().parse(contents);


                        //et_anv_city.setText(""+newCard.co);
                        //et_anv_age.setText(""+newCard.dob);
                        try {

                            String[] name=newCard.name.split(" ");
                            uid.setText(newCard.uid);
                            uuid=uid.getText().toString();
                            uid.setText(newCard.getFormattedUID());
                            first_name.setText(""+name[0]);
                            if (name.length>1)
                            {
                                middle_name.setText(""+name[1]);
                                last_name.setText(""+name[2]);
                            }

                            uname=first_name.getText().toString()+" "+middle_name.getText().toString()+" "+last_name.getText().toString();

                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    } catch (XmlPullParserException e) {
                        // TODO Auto-generated catch block
                        Toast.makeText(ctx, " Card Not Supported",2000).show();

                        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                                LaunchActivity.this);

                        // Setting Dialog Title
                        alertDialog2.setTitle("Card Not Supported With full information.");

                        // Setting Dialog Message
                        alertDialog2.setMessage("Your Scan Detail:"+contents);


                        // Setting Positive "Yes" Btn
                        alertDialog2.setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to execute after dialog

                                        dialog.cancel();
                                    }
                                });
                        // Setting Negative "NO" Btn
                        alertDialog2.setNegativeButton("NO",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to execute after dialog

                                        dialog.cancel();
                                    }
                                });

                        // Showing Alert Dialog
                        alertDialog2.show();

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    // Persistence.storeAadhaarCard(this, newCard);
                }
                if(resultCode == RESULT_CANCELED){
                    //handle cancel
                    Log.e("scan result"," cancle");
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }
    class sendDataJSON extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            String register_url="http://lidsmysqldb.cloudapp.net/sih2017/lids-api/register.php";

            String uuid,licenseno,vehicleid,username,password;

            String result = null;

            uuid=params[0];
            vehicleid=params[1];
            licenseno=params[2];
            username=params[3];
            password=params[4];


            try {

                URL url=new URL(register_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream OS=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                String data=URLEncoder.encode("uuid","UTF-8")+"="+URLEncoder.encode(uuid,"UTF-8")+"&"+
                        URLEncoder.encode("vehicleid","UTF-8")+"="+URLEncoder.encode(vehicleid,"UTF-8")+"&"+
                        URLEncoder.encode("licenseno","UTF-8")+"="+URLEncoder.encode(licenseno,"UTF-8")+"&"+
                        URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                StringBuilder sb = new StringBuilder();

                IS=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(IS,"iso-8859-1"));
                String response="";
                String line="";
                line=bufferedReader.readLine();

                result=line;
                bufferedReader.close();
                IS.close();
                httpURLConnection.disconnect();
                System.out.println(response);
            } catch (Exception e) {
                // Oops
            }
            finally {
                try{if(IS != null)IS.close();}catch(Exception squish){}
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(ctx,s,Toast.LENGTH_SHORT).show();
            loading.cancel();

            if(s!=null){
            Intent intent =new Intent(ctx,searchActivity.class);
            startActivity(intent);
            }
        }

        @Override
        protected void onPreExecute() {
            loading = ProgressDialog.show(ctx, "Status", "Fetching...",true,true);
            loading.setCancelable(false);
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
