package com.example.root.rashidgroupapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.root.rashidgroupapp.Adapter.OrderListRecyclerViewAdapter;
import com.example.root.rashidgroupapp.Modal.OrderListModal;
import com.example.root.rashidgroupapp.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static com.example.root.rashidgroupapp.Activity.OrgBranchSelection.branchId;
import static com.example.root.rashidgroupapp.Activity.OrgBranchSelection.orgId;

public class OrderListActivity extends AppCompatActivity {

    String ip = "*********";

    final String apiUrl = "http://"+ip+":8080/**********";
    ArrayList<String> intentList;

    static String docNo,chemName,chemId;
    String customerName;
    double total_price = 0.00;
    static TextView totalPriceTextView,totalPriceInWordsTextView;
    Button sendToServerButton;
    Toolbar toolbar;
    RecyclerView orderListRecyclerView;
    DatabaseHelper mDatabaseHelper;
    ArrayList<OrderListModal> orderListModalArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list_activty);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null)
        {
            intentList = getIntent().getStringArrayListExtra("Listintent");
            docNo = intentList.get(1);
            customerName = intentList.get(0);
        }

        orderListRecyclerView = findViewById(R.id.order_list_recyclerview);
        totalPriceTextView = findViewById(R.id.tPrice_textView);
        totalPriceInWordsTextView = findViewById(R.id.tPrice_textView_inwords);
        sendToServerButton = findViewById(R.id.send_order_btn);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderListActivity.this,Order.class);
                intent.putExtra("Listintent",intentList);
                startActivity(intent);
                finish();
            }
        });

        mDatabaseHelper = new DatabaseHelper(this);
        loadOrderList();
        loadOrderPrice(this);
        sendToServer();
    }

    public void loadOrderPrice(Context mContext){
        DatabaseHelper mDatabaseHelper = new DatabaseHelper(mContext);
        Cursor totalPrice = mDatabaseHelper.getTotalOrderPrice(docNo);
        if(totalPrice.moveToFirst()){
            do{
                total_price = totalPrice.getDouble(0);
            }while(totalPrice.moveToNext());
        }

        totalPriceTextView.setText(String.valueOf(total_price));
        int intTotalPrice = (int) total_price;
        String return_val_in_words =   NumberToWords1.convert(intTotalPrice);
        totalPriceInWordsTextView.setText(return_val_in_words +" Taka");

//        Cursor check = mDatabaseHelper.getSalesOrderLineDataTemp(docNo);
//        if(!check.moveToNext()) {
//            sendToServerButton.setEnabled(false);
//        }
    }


    private void loadOrderList(){
        Cursor data = mDatabaseHelper.getSalesOrderLineDataTemp(docNo);
        if(data.moveToFirst()){
            do{
               // int p_key = data.getInt(0);
                String productID=data.getString(2);
                String quantity =String.valueOf(data.getString(3));
                double price= data.getDouble(4);
                //price =Double.parseDouble(new DecimalFormat("##.####").format(price));
                String productName = data.getString(5);
                String unitPrice = data.getString(6);
                String uom       = data.getString(7);

               // total_price = total_price + price;
                OrderListModal orderListModal = new OrderListModal(docNo,productID,productName,unitPrice,quantity,uom,price,customerName);
                orderListModalArrayList.add(orderListModal);
            }while(data.moveToNext());
        }
        initRecyclerView();

    }

    public void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        orderListRecyclerView.setLayoutManager(linearLayoutManager);
        OrderListRecyclerViewAdapter adapter = new OrderListRecyclerViewAdapter(orderListModalArrayList,OrderListActivity.this);
        orderListRecyclerView.setAdapter(adapter);
    }

    private void sendToServer(){
            sendToServerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendToServerButton.setEnabled(false);
                    AsyncSendToServer task = new AsyncSendToServer();
                    task.execute();
                }
            });
    }


    private final class AsyncSendToServer extends AsyncTask<String,String,String> {

        // String result = "";
        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            //String docNo= docNo;
            String userID=mDatabaseHelper.getUserIDFromEmployee();

            boolean isActive=true;

            Cursor check = mDatabaseHelper.getSalesOrderLineDataTemp(docNo);
            if(check.moveToNext()) {

            } else {
                // toastMessage("No OrderLine Exists ");
                 return "noOrderLine";
            }
            try {
                //conn.setAutoCommit(false);
                Cursor data = mDatabaseHelper.getSalesOrder(docNo);
                ArrayList salesHeader=new ArrayList();
                if (data.moveToNext()) {

                    String chemistID = data.getString(0);
                    String date = data.getString(1);
                    String delivaryDate = data.getString(2);
                    String description  = data.getString(3);
                    String promisedAmount = data.getString(4);
                    String salesType      = data.getString(5);
                    String billingLocation = data.getString(6);

                    Vector headerLines=new Vector();
                    headerLines.add(date);
                    headerLines.add(docNo);
                    headerLines.add(chemistID);
                    headerLines.add(userID);
                    headerLines.add(delivaryDate);
                    headerLines.add(description);
                    headerLines.add(promisedAmount);
                    headerLines.add(salesType);
                    headerLines.add(billingLocation);
                    headerLines.add(orgId);
                    headerLines.add(branchId);
                    salesHeader.add(headerLines);



                }

                String headerString = "";
                for (Object s : salesHeader) {
                    if (headerString.isEmpty()) {
                        headerString =headerString+s;
                    } else {
                        headerString =headerString+"_" + s;

                    }
                }

                Cursor dataorderLine = mDatabaseHelper.getSalesOrderLineDataTemp(docNo);

                ArrayList salesOrder=new ArrayList();
                while (dataorderLine.moveToNext()) {

                    String productID = dataorderLine.getString(2);
                    String quantity = dataorderLine.getString(3);
                    double unit_price  = dataorderLine.getDouble(6);
                    Vector orderLines=new Vector();
                    orderLines.add(docNo);
                    orderLines.add(productID);
                    orderLines.add(quantity);
                    orderLines.add(unit_price);

                    salesOrder.add(orderLines);


                }


                String lineString = "";
                for (Object s : salesOrder) {
                    if (lineString.isEmpty()) {
                        lineString =lineString+s;
                    } else {
                        lineString =lineString+"_" + s;

                    }
                }

                HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/SalesOrder")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(""+apiUrl+"/SalesOrder");

                List<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("type", "salesorder"));
                list.add(new BasicNameValuePair("header", headerString));
                list.add(new BasicNameValuePair("lines",lineString));
                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse=	httpClient.execute(httpPost);



                HttpEntity httpEntity=httpResponse.getEntity();
                String s= readResponse(httpResponse);

                if (s.isEmpty() || !s.equals("Success")){
                    result=s;
                    return result;
                } else if(s.equals("Success")){
                    return "Success";
                }

            }
            catch (Exception e){
                result = e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
                if(s.equals("Success")){
                    mDatabaseHelper.deleteSalesOrder();
                    mDatabaseHelper.deleteSalesOrderLine();
                    toastMessage("Order Placed Successfully");
                    sendToServerButton.setBackgroundColor(getColor(R.color.colorGreen));
                    Intent intent = new Intent(OrderListActivity.this,ChemistSelection.class);
                    startActivity(intent);
                    finish();
                }else if(s.equals("noOrderLine")){
                    toastMessage("No order line exits");
                } else{
                    sendToServerButton.setEnabled(true);
                    //toastMessage("Server Connection lost Please Send Again");
                    ShowAlert("Error","Server Connection Failed Please Send Again");
                }
            }catch (NullPointerException ex){

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
            }
        return a;
    }


    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    public void ShowAlert(String title,String msg){


        androidx.appcompat.app.AlertDialog.Builder dlgAlert  = new androidx.appcompat.app.AlertDialog.Builder(OrderListActivity.this);

        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.drawable.logo_11);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(OrderListActivity.this,Order.class);
        intent.putExtra("Listintent",intentList);
        startActivity(intent);
        finish();
    }
}
