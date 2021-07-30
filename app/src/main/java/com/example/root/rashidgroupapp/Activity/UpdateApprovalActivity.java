package com.example.root.rashidgroupapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.rashidgroupapp.Adapter.ApprovalProductsListRecyclerViewAdapter;
import com.example.root.rashidgroupapp.Modal.ApprovalProductDetailsModal;
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
import java.util.Vector;

import static com.example.root.rashidgroupapp.Activity.MainActivity.ad_user_id;

public class UpdateApprovalActivity extends AppCompatActivity {

    String bpCode,bpName,phone,dateOrdered,warehouse,total_openbalance,bp_location,description,promisedAmt;
    String uomsymbol, old_kg,documentNo,des,promised_date,undelivered_amt,s_name,s_phone,c_order_id;
    String wordsAmtBags;
    Toolbar toolbar;
    TextView partyCodeTextView,partyNameTextView,addressTextView,mobileNoTextView,soNumberTextView,soIssueDateTextView,srNameTextView,srMobileTextView;
    TextView total_mtTextView,total_bagsTextView,total_moneyTextView,promisedDateTextView,qty_wordsTextView;
    TextView mtAmtTextView,deliveryFromTextView,partyBalanceTextView,undeliveredAmtTextView,desTextView,proAmtTextView;
    EditText msg_text;
    Button yes_button,no_button,edit_order_button;
    RecyclerView recyclerViewList;
    double totalMetricTon = 0,totalBagQty = 0,totalTakaAmt = 0;
    ArrayList<ApprovalProductDetailsModal> productDetailsModals = new ArrayList<>();
    String msg = "",isApproved = "",documentNumber = "",erpDocumentNumber = "";
    DatabaseHelper dbHelper;
    String ip="***********";
    final String apiUrl = "http://"+ip+":8080/RashidWEBAPI";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_approval);

        partyCodeTextView = findViewById(R.id.party_code_approval);
        partyNameTextView = findViewById(R.id.party_name_approval);
        addressTextView = findViewById(R.id.address_approval);
        mobileNoTextView = findViewById(R.id.mobile_approval);
        soNumberTextView = findViewById(R.id.so_number);
        soIssueDateTextView = findViewById(R.id.so_issue_date);
        srNameTextView      = findViewById(R.id.sr_name);
        srMobileTextView    = findViewById(R.id.sr_mobile);

        recyclerViewList = findViewById(R.id.order_product_list_recyclerview);
        total_mtTextView      = findViewById(R.id.total_mt);
        total_bagsTextView    = findViewById(R.id.total_bags);
        total_moneyTextView   = findViewById(R.id.total_price_amt);
        promisedDateTextView  = findViewById(R.id.promised_date);
        qty_wordsTextView      = findViewById(R.id.quantity_words_bags);
        mtAmtTextView          = findViewById(R.id.mt_amount);
        deliveryFromTextView   = findViewById(R.id.delivery_from);
        partyBalanceTextView   = findViewById(R.id.party_balance_approval);
        undeliveredAmtTextView = findViewById(R.id.undelivered_so_amt);
        desTextView            = findViewById(R.id.description_id);
        proAmtTextView         = findViewById(R.id.promAmt_id);

        msg_text = findViewById(R.id.msg_id);
        yes_button = findViewById(R.id.yes_btn);
        no_button = findViewById(R.id.no_btn);
        edit_order_button = findViewById(R.id.edit_order_btn);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null)
        {
              documentNumber   = getIntent().getStringExtra("order_doc");
              erpDocumentNumber= getIntent().getStringExtra("erp_doc");
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent1 = new Intent(UpdateApprovalActivity.this,OrderListForApprovalActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);

            }
        });

        dbHelper = new DatabaseHelper(this);
        buttonClick();
        AsyncLoadData asyncLoadData = new AsyncLoadData();
        asyncLoadData.execute();
    }

    private void buttonClick(){
        yes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = msg_text.getText().toString();
                isApproved = "Y";
               // documentNumber = order_document.getText().toString();
               // erpDocumentNumber = erp_document.getText().toString();
                if(msg.equals("") || msg.equals(null)){
                    //toastMessage("Please Leave a message");
                    msg_text.setError("Please Leave a message");
                    return;
                }
                AsyncSender task = new AsyncSender();
                task.execute();
            }
        });

        no_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = msg_text.getText().toString();
                isApproved = "N";
                //documentNumber = order_document.getText().toString();
               // erpDocumentNumber = erp_document.getText().toString();
                if(msg.equals("") || msg.equals(null)){
                   // toastMessage("Please Leave a message");
                    msg_text.setError("Please Leave a message");
                    return;
                }

                AsyncSender task = new AsyncSender();
                task.execute();
            }
        });

        edit_order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(UpdateApprovalActivity.this,UpdateOrderActivity.class);
                intent.putExtra("erp_doc",erpDocumentNumber);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }


    private final class AsyncLoadData extends AsyncTask<String, String, String>{

        ProgressDialog pd;
        String result = "";
        @Override
        protected String doInBackground(String... strings) {
            result =  FetchDataFromServer();
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(UpdateApprovalActivity.this);
            pd.setTitle("Please Wait ");
            pd.setMessage("Loading....");
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

                wordsAmtBags = NumberToWords1.convert((int)totalBagQty);
                setTextField();
                initRecyclerView();
            }
            else {
                toastMessage("Something Went Wrong!!!!");
            }
        }

    }

    private void setTextField(){
        partyCodeTextView.setText(bpCode);
        partyNameTextView.setText(bpName);
        addressTextView.setText(bp_location);
        mobileNoTextView.setText(phone);
        soNumberTextView.setText(documentNo);
        dateOrdered = dateOrdered.replaceAll("\\d\\d:\\d\\d:\\d\\d.\\d","");
        soIssueDateTextView.setText(dateOrdered);
        srNameTextView.setText(s_name);
        srMobileTextView.setText(s_phone);
        total_mtTextView.setText(String.valueOf(totalMetricTon));
        total_bagsTextView.setText(String.valueOf(totalBagQty));
        total_moneyTextView.setText(String.valueOf(totalTakaAmt));
        promised_date = promised_date.replaceAll("\\d\\d:\\d\\d:\\d\\d.\\d","");
        promisedDateTextView.setText(promised_date);
        mtAmtTextView.setText(String.valueOf(totalMetricTon));
        deliveryFromTextView.setText(warehouse);
        partyBalanceTextView.setText(total_openbalance);
        undeliveredAmtTextView.setText(undelivered_amt);
        qty_wordsTextView.setText(wordsAmtBags + " - BAGS");
        desTextView.setText(description);
        proAmtTextView.setText(promisedAmt);
    }
    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewList.setLayoutManager(linearLayoutManager);
        ApprovalProductsListRecyclerViewAdapter adapter = new ApprovalProductsListRecyclerViewAdapter(productDetailsModals,UpdateApprovalActivity.this);
        recyclerViewList.setAdapter(adapter);
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
                list.add(new BasicNameValuePair("type", "updateApproval"));
                list.add(new BasicNameValuePair("userID", ad_user_id));
                list.add(new BasicNameValuePair("isApproved",isApproved));
                list.add(new BasicNameValuePair("msg",msg));
                list.add(new BasicNameValuePair("docNo",documentNumber));
                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse=	httpClient.execute(httpPost);



                HttpEntity httpEntity=httpResponse.getEntity();
                String s= readResponse(httpResponse);
                int y = s.length();
                if (s.isEmpty() || !s.equals("ApprovalUpdated")){
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

            pd = new ProgressDialog(UpdateApprovalActivity.this);
            pd.setTitle("Please Wait ");
            pd.setMessage("Updating....");
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
                toastMessage("Updated");
                dbHelper.deleteOrderApprovalLine(erpDocumentNumber);
                finish();
                Intent intent  = new Intent(UpdateApprovalActivity.this,OrderListForApprovalActivity.class);
                startActivity(intent);
            }
            else {
                toastMessage("Something Went Wrong!!!!");
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

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
        Intent intent=new Intent(UpdateApprovalActivity.this, OrderListForApprovalActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }


    public String FetchDataFromServer() {
        int catagoryID;
        boolean result = false;
        String  res = "";

        try {

            HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/SyncOrderDetails")).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(""+apiUrl+"/SyncOrderDetails");

            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("erpdoc", erpDocumentNumber));
            //list.add(new BasicNameValuePair("m_warehouse_id",String.valueOf(branchId)));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);

            HttpEntity httpEntity=httpResponse.getEntity();
            String s= readResponse(httpResponse);
            if (s.isEmpty() || !s.startsWith("[")){
                res="No Data Found";
                return res;
            }

            ArrayList list4=new ArrayList();

            String[] parts = s.split("_");
            for(int i=0;i<parts.length;i++){
                Vector line=new Vector();
                String test = parts[i];
                String[] parts1 = parts[i].split(",",23);
                for(int j=0;j<parts1.length;j++){

                    String value=parts1[j].replaceFirst("[\\[\\]\\\" \"\\^:,]","");
                    value=value.replaceAll("[\\[\\]]","");
                    line.add(value);

                }
                list4.add(line);
            }

            for(int i1=0;i1<list4.size();i1++){
                Vector line= (Vector) list4.get(i1);
                 bpCode =((String) line.get(0));
                 bpName =(String) line.get(1);
                 phone  =((String) line.get(2));
                 dateOrdered =((String) line.get(3));
                 warehouse   = ((String)line.get(4));
                 total_openbalance   = ((String)line.get(5));
                 String rate  = ((String)line.get(6));
                 uomsymbol  = ((String)line.get(7));
                 old_kg  = ((String)line.get(8));
                 String productName  = ((String)line.get(9));
                 documentNo  = ((String)line.get(10));
                 des  = ((String)line.get(11));
                 promised_date  = ((String)line.get(12));
                 undelivered_amt  = ((String)line.get(13));
                 s_name  = ((String)line.get(14));
                 s_phone  = ((String)line.get(15));
                 c_order_id  = ((String)line.get(16));
                 String kg  = ((String)line.get(17));
                 String qty  = ((String)line.get(18));
                 String mt_qty  = ((String)line.get(19));
                 description  = ((String)line.get(20));
                 promisedAmt  = ((String)line.get(21));
                 bp_location  = ((String)line.get(22));
                double takaAmt = 0;
                if(!qty.equals("")){
                    totalBagQty = totalBagQty + Double.parseDouble(qty);
                    if(!rate.equals("")){
                        takaAmt = Double.parseDouble(qty) * Double.parseDouble(rate);
                        totalTakaAmt = totalTakaAmt + takaAmt;
                    }
                }
                if(!mt_qty.equals("")){
                    totalMetricTon = totalMetricTon + Double.parseDouble(mt_qty);
                }

                ApprovalProductDetailsModal modal = new ApprovalProductDetailsModal(productName,kg,qty,rate,String.valueOf(takaAmt));
                productDetailsModals.add(modal);
                result = true;

            }

            if (result) {
                res = "Success";
            }


        } catch (Exception e) {
            res = e.getMessage();
        }
        return res;

    }


}
