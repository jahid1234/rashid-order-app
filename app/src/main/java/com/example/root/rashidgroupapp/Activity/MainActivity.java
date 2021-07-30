package com.example.root.rashidgroupapp.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
//import android.support.v4.app.ActivityCompat;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.root.rashidgroupapp.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {


    DatabaseHelper mDatabaseHelper;

    EditText employeeIdEditText, passwordEditText;

    Button loginButton;
    ImageView logo;
    CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    Boolean saveLogin=false;

    String deviceId = null;
    String c_bpartner_id = "";
    String levelno;
   // static String branch_id= "";
    boolean doubleBackToExitPressedOnce=false;
    Connection conn = null;
    ResultSet rs = null, rs2 = null;
    String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static String employeeid="",ad_user_id = "";String password="";

    String ip = "***********";
    final String apiUrl = "http://"+ip+":8080/*********";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDatabaseHelper = new DatabaseHelper(this);

        employeeIdEditText = (EditText) findViewById(R.id.employeeID_editText);
        passwordEditText = (EditText) findViewById(R.id.password_editText);

        saveLoginCheckBox = (CheckBox)findViewById(R.id.remember_checkbox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            employeeIdEditText.setText(loginPreferences.getString("username", ""));
            passwordEditText.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }

        logo = (ImageView) findViewById(R.id.logo);

        loginButton = (Button) findViewById(R.id.login_button);
        OnLoginButtonClick();

      //  homeActivity();

          getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
       // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    private void homeActivity() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent activityintent = new Intent(MainActivity.this, Home.class);
                startActivity(activityintent);
            }
        });

    }


    private void OnLoginButtonClick() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                employeeid = employeeIdEditText.getText().toString();
                password = passwordEditText.getText().toString();

                if (Build.VERSION.SDK_INT >= 23) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling

                        ActivityCompat.requestPermissions(MainActivity.this, permissionsRequired, 1);
                        return;

                    }
                }

                String id, pass;



                TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

                //deviceId = tm.getDeviceId();



                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
                    StrictMode.setThreadPolicy(policy);

                    if (TextUtils.isEmpty(employeeIdEditText.getText().toString()))

                    {
                        employeeIdEditText.setError("Please Enter ID ");
                        return;
                    }

                if (saveLoginCheckBox.isChecked()) {
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("username", employeeid);
                    loginPrefsEditor.putString("password", password);
                    loginPrefsEditor.commit();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }


                AsyncSender task = new AsyncSender();
                task.execute();



            }


        });
    }

    private final class AsyncSender extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected String doInBackground(String... params) {

            String result="",error="";

            try {

                HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/Login")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(""+apiUrl+"/Login");

                List<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("name", employeeid));
                list.add(new BasicNameValuePair("pass",password));
                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse=	httpClient.execute(httpPost);



                HttpEntity httpEntity=httpResponse.getEntity();
                String s= readResponse(httpResponse);
                int y = s.length();
                if (s.isEmpty() || !s.startsWith("[")){
                    result=s;
                    return result;
                }
                ArrayList list4=new ArrayList();
                Vector line5 = new Vector();
                String[] parts = s.split("_");
                for(int i=0;i<parts.length;i++){
                    Vector line1=new Vector();
                    line5 = new Vector();
                    String[] parts1 = parts[i].split(",");
                    for(int j=0;j<parts1.length;j++){

                       // line1.add(parts1[j].replaceAll("[\\[\\]\\\" \"\\^:,]",""));
                        String value = parts1[j].replaceFirst("[\\[\\]\\\" \"\\^:,]","");
                        value=value.replaceAll("[\\[\\]]","");
                        line1.add(value);
                        line5.add(parts1[j].replaceAll("[\\[\\]\\\" \"\\^:,]",""));
                    }
                    list4.add(line1);
                }


                String org_name = "",org_id = "",branch_name = "", branch_id = "",m_warehouse_id = "";
                if(line5.size()<2){
                    error="Employee ID and Password Did not Matched  Or Not Authorised for this User";
                    return error;
                }
                mDatabaseHelper.deleteOrganization();
                for(int i1=0;i1<list4.size();i1++){
                    Vector line= (Vector) list4.get(i1);
                    ad_user_id=(String) line.get(0);
                    m_warehouse_id=(String) line.get(1);
                    org_name = (String) line.get(2);
                    org_id = (String) line.get(3);
                    branch_name = (String) line.get(4);
                    branch_id   = (String) line.get(5);
                   // c_bpartner_id = (String) line.get(2);
                   // levelno = (String) line.get(3);
                    mDatabaseHelper.insertIntoOrganization(org_name,Integer.parseInt(org_id),branch_name,Integer.parseInt(branch_id));
                }


                    mDatabaseHelper.deleteEmployee();

//
                    boolean res = mDatabaseHelper.insertIntoEmployee(employeeid,ad_user_id,m_warehouse_id);
                    if (res) {


                        //loginButton.setEnabled(false);
                        //toastMessage("Welcome " + employeeid);
                        result="Success";

                    } else {

                        error="Failed to save Employee";
                        return error;
                    }


            }
//            catch (SQLException e) {
//                //Toast.makeText(getApplicationContext(),""+ e.getMessage(),Toast.LENGTH_LONG).show();
//                e.printStackTrace();
//                error=e.getMessage();
//                return  error;
//            }
          catch (ClientProtocolException e) {
                error=e.getMessage();
                return  error;
            } catch (UnsupportedEncodingException e) {
                error=e.getMessage();
                return  error;
            } catch (IOException e) {
                error="Failed To Connect";
                return  error;
            }


            return result;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setTitle("Please Wait ");
            pd.setMessage("Logging In ........");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }



        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();

            if (result.equals("Success")) {
              //  toastMessage("WELCOME " + employeeid +" levelno "+ levelno +"Patner_Id"+c_bpartner_id);
                finish();
                Intent activityintent = new Intent(MainActivity.this, OrgBranchSelection.class);
               // activityintent.putExtra("partner_id",c_bpartner_id);
               // activityintent.putExtra("levelno",levelno);
                startActivity(activityintent);
            }
            else {
                ShowAlert("ERROR",result);
                //toastMessage(result);
            }
        }
    }

    public String readResponse(HttpResponse res) {
        InputStream is=null;
        String return_text="",a="";
        try {
            is=res.getEntity().getContent();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
            String line="";
            StringBuffer sb=new StringBuffer();
            while ((line=bufferedReader.readLine())!=null)
            {

                //sb.append(line);
                 a=line;
            }
            return_text=sb.toString();
        } catch (Exception e)
        {
            a = "Error" + e.getMessage();
            return a;
        }
        return a;

    }



    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        toastMessage("Please click BACK again to EXIT");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void ShowAlert(String title,String msg){


        androidx.appcompat.app.AlertDialog.Builder dlgAlert  = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);

        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.drawable.logo_11);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

}
