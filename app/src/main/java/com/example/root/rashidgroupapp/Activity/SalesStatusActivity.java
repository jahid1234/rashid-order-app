package com.example.root.rashidgroupapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.root.rashidgroupapp.R;

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
import java.util.Vector;

public class SalesStatusActivity extends AppCompatActivity {

    String partner_id,levelno;
    String ip = "**********";
    final String apiUrl = "http://"+ip+":8080/NaafcoWEBAPI";
    ProgressDialog pd;
    ArrayList<String> finalList = new ArrayList<>();
    private static SalesStatusAdapter adapter;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_status);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null)
        {
            partner_id =(String) bundle.get("partner_id");
            levelno = (String) bundle.get("levelno");
        }

        Toolbar toolbar = findViewById(R.id.sales_status_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent=new Intent(SalesStatusActivity.this, Home.class);
                startActivity(intent);
            }
        });

        AsyncSender task = new AsyncSender();
        task.execute();

    }

    @Override
    public void onBackPressed(){

        finish();
        Intent intent=new Intent(this,Home.class);
        startActivity(intent);

    }

    private void showProgressDialog(){
        if(pd == null){
            pd = new ProgressDialog(SalesStatusActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Sales Status Loading.....");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        }
        pd.show();
    }

    private void dismissProgressDialog(){
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    private final class AsyncSender extends AsyncTask<String,String,String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressDialog();
            if(s.equals("sales sync successfull")){
                toastMessage("sales sync done!!");
                initListView(finalList);
            }else{
                ShowAlert(s);
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            String result = "",error="";
            try{
                result = syncSalesStatus();

            }catch (Exception e){
                error = e.getMessage();
                e.printStackTrace();
                return error;
            }
            return result;
        }
    }

    private String syncSalesStatus(){
        String result = "";
        String error = " ";
        String territoryName ="" ,territoryCode= "",tp="",target="";

        try{
            HttpURLConnection httpURLConnection = (HttpURLConnection) (new URL(""+apiUrl+"/SalesStatusSync")).openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(""+apiUrl+"/SalesStatusSync");

            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("partner_id",partner_id));
            list.add(new BasicNameValuePair("levelno",levelno));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse = httpClient.execute(httpPost);

            String s = readResponse(httpResponse);
           // System.out.println(s);
            if(s.isEmpty() || !s.startsWith("[")){
                result = "Product not found";
                return result;
            }

            result = "sales sync successfull";
            final ArrayList salesList = new ArrayList();
            String[] parts = s.split("_");

            for(int i=0;i<parts.length;i++){
                Vector line = new Vector();
                String[] parts1 = parts[i].split(",");
                for(int j=0;j<parts1.length;j++){
                    String value = parts1[j].replaceFirst("[\\[\\]\\\"\"\\^:,]","");
                    value=value.replaceAll("[\\[\\]]","");
                    line.add(value);
                }


                salesList.add(line);
               // System.out.println(salesList);
            }

            for(int i1=0;i1<salesList.size();i1++){
                Vector line1= (Vector)salesList.get(i1);
                 territoryName =(String) line1.get(0);
                 territoryCode =(String) line1.get(1);
                 tp=(String) line1.get(2);
                 target=(String) line1.get(3);

                 finalList.add(territoryName+" "+territoryCode+" "+tp+" "+target);
            }



            //initRecyclerView(finalList);



        }catch (ClientProtocolException e){
            error = e.getMessage();
            return error;
        }catch (UnsupportedEncodingException e){
            error = e.getMessage();
            return error;
        }catch (IOException e){
            error = e.getMessage();
            return error;
        }


        return result;
    }

    private void toastMessage(String message ){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    private void ShowAlert(String msg){
        androidx.appcompat.app.AlertDialog.Builder dlgAlert = new androidx.appcompat.app.AlertDialog.Builder(SalesStatusActivity.this);

        dlgAlert.setMessage(msg);

        dlgAlert.setIcon(R.drawable.naafcologo);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }


    private void initListView(ArrayList<String> list){


       // SalesStatusAdapter adapter = new SalesStatusAdapter(this, R.layout.sales_status_recycler_layout, R.id.territory, list);
        //System.out.println(items.length);
       // listView.setAdapter(adapter);
        listView = findViewById(R.id.sales_status_list);
        ViewGroup headerView = (ViewGroup) getLayoutInflater().inflate(R.layout.sales_status_header, listView, false);
        // Add header view to the ListView
        listView.addHeaderView(headerView);
        adapter= new SalesStatusAdapter(getApplicationContext(),list);

        listView.setAdapter(adapter);


    }

    private String readResponse(HttpResponse response){
        InputStream inputStream = null;
        String a = "";

        try{
            inputStream = response.getEntity().getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";
            StringBuffer stringBuffer = new StringBuffer();
            while((line=bufferedReader.readLine())!=null){
                a=line;
            }
        }catch (Exception e){
            a = e.getMessage();
        }
        return a;
    }
}
