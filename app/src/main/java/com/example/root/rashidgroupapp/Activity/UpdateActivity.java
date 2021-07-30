package com.example.root.rashidgroupapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.ArrayList;
import java.util.List;

import static com.example.root.rashidgroupapp.Activity.MainActivity.employeeid;

public class UpdateActivity extends AppCompatActivity {

    EditText confirm_password_editText,enterNew_password_editText,confirmNew_password_editText;
    Button goButton,confirmButton;
    LinearLayout linearLayoutPanel;


    String currentPassword = "",newPassword = "";
    String ip="**********";
    final String apiUrl = "http://"+ip+":8080/RashidWEBAPI";

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UpdateActivity.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

        linearLayoutPanel =findViewById(R.id.change_pasword_layout);
        confirm_password_editText = findViewById(R.id.check_password_editText);
        enterNew_password_editText = findViewById(R.id.new_password_editText);
        confirmNew_password_editText = findViewById(R.id.confirm_new_password_editText);

        goButton = findViewById(R.id.go_button);
        confirmButton = findViewById(R.id.confirm_password_button);

        buttonFunction();
    }

    private void buttonFunction(){
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPassword = confirm_password_editText.getText().toString();
                AsyncSender task = new AsyncSender();
                task.execute();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pass = enterNew_password_editText.getText().toString();
                if(pass.equals("")){
                    toastMessage("enter new password");
                    return;
                }
                newPassword = confirmNew_password_editText.getText().toString();
                if(!newPassword.equals(enterNew_password_editText.getText().toString())){
                    toastMessage("password did not match");
                    return;
                }

                AsyncSender1 task = new AsyncSender1();
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

                HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/UpdateOperation")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(""+apiUrl+"/UpdateOperation");

                List<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("type", "checkPassword"));
                list.add(new BasicNameValuePair("name", employeeid));
                list.add(new BasicNameValuePair("pass",currentPassword));
                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse=	httpClient.execute(httpPost);



                HttpEntity httpEntity=httpResponse.getEntity();
                String s= readResponse(httpResponse);
                int y = s.length();
                if (s.isEmpty() || !s.equals("Success")){
                    result=s;
                    return result;
                }else{
                    return "Success";
                }

            }

            catch (ClientProtocolException e) {
                error=e.getMessage();
                return  error;
            } catch (UnsupportedEncodingException e) {
                error=e.getMessage();
                return  error;
            } catch (IOException e) {
                error=e.getMessage();
                return  error;
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(UpdateActivity.this);
            pd.setTitle("Please Wait ");
            pd.setMessage("Checking");
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
                toastMessage("password matched");
                linearLayoutPanel.setVisibility(View.VISIBLE);
            }
            else {
                toastMessage("Something Went Wrong!!");
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

    public void onBackPressed(){

        Intent intent=new Intent(UpdateActivity.this,Home.class);
        startActivity(intent);
        finish();
    }


    private final class AsyncSender1 extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected String doInBackground(String... params) {

            String result="",error="";

            try {

                HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/UpdateOperation")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(""+apiUrl+"/UpdateOperation");

                List<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("type", "newPassword"));
                list.add(new BasicNameValuePair("name", employeeid));
                list.add(new BasicNameValuePair("pass",currentPassword));
                list.add(new BasicNameValuePair("newpass",newPassword));
                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse=	httpClient.execute(httpPost);



                HttpEntity httpEntity=httpResponse.getEntity();
                String s= readResponse(httpResponse);
                int y = s.length();
                if (s.isEmpty() || !s.equals("PasswordUpdated")){
                    result=s;
                    return result;
                }else{
                    return "Success";
                }

            }

            catch (ClientProtocolException e) {
                error=e.getMessage();
                return  error;
            } catch (UnsupportedEncodingException e) {
                error=e.getMessage();
                return  error;
            } catch (IOException e) {
                error=e.getMessage();
                return  error;
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(UpdateActivity.this);
            pd.setTitle("Please Wait ");
            pd.setMessage("Updating");
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
                toastMessage("password Changed");
                enterNew_password_editText.setText("");
                confirmNew_password_editText.setText("");
                linearLayoutPanel.setVisibility(View.INVISIBLE);

            }
            else {
                toastMessage("Something went wrong");
            }
        }
    }

}
