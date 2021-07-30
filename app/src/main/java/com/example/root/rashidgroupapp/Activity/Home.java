package com.example.root.rashidgroupapp.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.root.rashidgroupapp.Other.DCR;
import com.example.root.rashidgroupapp.Other.DoctorsInfo;
import com.example.root.rashidgroupapp.Other.NewChemist;
import com.example.root.rashidgroupapp.Other.Promo;
import com.example.root.rashidgroupapp.R;
import com.example.root.rashidgroupapp.Other.RxCapturing;
import com.example.root.rashidgroupapp.Other.SendSms;

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
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static com.example.root.rashidgroupapp.Activity.MainActivity.employeeid;
import static com.example.root.rashidgroupapp.Activity.OrgBranchSelection.branchId;

public class Home extends AppCompatActivity {


    DatabaseHelper mDatabaseHelper;


    Button loginButton,salesStatusbtn;

    String userId;

    Connection conn = null;
    ResultSet rs = null;

    boolean doubleBackToExitPressedOnce = false;
     String ip = "***************";

    final String apiUrl = "http://"+ip+":8080/***************";

    public static String partner_id,levelno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

       // salesStatusbtn = findViewById(R.id.sales_status_btn);
        mDatabaseHelper = new DatabaseHelper(this);

//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        if(bundle!=null)
//        {
//            partner_id =(String) bundle.get("partner_id");
//            //levelno = (String) bundle.get("levelno");
//        }

//        if(levelno.equals("3")){
//            salesStatusbtn.setText("RSM Sales Status");
//        }else if(levelno.equals("4")){
//            salesStatusbtn.setText("ABM Sales Status");
//        }else if(levelno.equals("5")){
//            salesStatusbtn.setText("Level 5 sales Status");
//        }
    }

    public void loadSalesOrder(View view) {
        AsyncLoadRecord asyncLoadRecord = new AsyncLoadRecord();
        asyncLoadRecord.execute();
    }

    public void orderActivity(View view) {

        Intent activityintent = new Intent(this, ChemistSelection.class);
        startActivity(activityintent);
        finish();
    }

    public void newChemistActivity(View view) {
        //finish();
        Intent activityintent = new Intent(this, NewChemist.class);

        startActivity(activityintent);
        finish();
    }


    public void sendSMSActivity(View view) {
        //finish();
        Intent activityintent = new Intent(this, SendSms.class);

        startActivity(activityintent);
        finish();
    }

    public void newDrActivity(View view) {

        Intent activityintent = new Intent(this, DoctorsInfo.class);

        startActivity(activityintent);
        finish();
    }

    public void dcrActivity(View view) {

        Intent activityintent = new Intent(this, DCR.class);

        startActivity(activityintent);
        finish();
    }

    public void RxActivity(View view) {

        Intent activityintent = new Intent(this, RxCapturing.class);

        startActivity(activityintent);
        finish();
    }

    public void DBsyncActivity(View view) {
//        Intent activityintent = new Intent(this, DBSync.class);
//
//        startActivity(activityintent);

        Home.AsyncSender task = new Home.AsyncSender();
        task.execute();


    }

    public void summaryLayoutActivity(View view) {
        finish();
        Intent activityintent = new Intent(this, OrderHistoryActivity.class);
        startActivity(activityintent);
    }

    public void promoActivity(View view) {
        finish();
        Intent activityintent = new Intent(this, Promo.class);

        startActivity(activityintent);
    }

    public void salesStatusActivity(View view){
        finish();
        Intent intent = new Intent(this, SalesStatusActivity.class);
        intent.putExtra("partner_id",partner_id);
        intent.putExtra("levelno",levelno);
        startActivity(intent);
    }

    public void stockActivity(View view){
        finish();
        Intent intent = new Intent(this, StatementOfStock.class);
        startActivity(intent);
    }

    public void  partyBalanceActivity(View view){
        finish();
        Intent intent = new Intent(this, PartyBalanceActivity.class);
        startActivity(intent);
    }

    public void collectionStalementActivity(View view){
        finish();
        Intent intent = new Intent(Home.this,CollectionStalementActivity.class);
        startActivity(intent);
    }

    public void  updatePasswordActivity(View view){
        finish();
        Intent intent = new Intent(this, UpdateActivity.class);
        startActivity(intent);
    }

    public void  OrderListForApprovalActivity(View view){
        finish();
        Intent intent = new Intent(this, OrderListForApprovalActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Log Out?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        toastMessage("Logging Out...........");
                        finish();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void toastMessage(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    private String DBSYNC() {

        String res = "";

        try {
            mDatabaseHelper.deleteSalesType();
            //toastMessage("Delete Products Successfull");
        } catch (Exception e) {

            //Toast.makeText(getApplicationContext(),""+ e.getMessage(),Toast.LENGTH_LONG).show();
            res = e.getMessage();
            return res;
        }


        try {
            mDatabaseHelper.deleteProducts();
            //toastMessage("Delete Products Successfull");
        } catch (Exception e) {

            //Toast.makeText(getApplicationContext(),""+ e.getMessage(),Toast.LENGTH_LONG).show();
            res = e.getMessage();
            return res;
        }

        try {
            mDatabaseHelper.deleteProductCatagory();
            // toastMessage("Delete Product Catagory Successfull");
        } catch (Exception e) {

            //Toast.makeText(getApplicationContext(),""+ e.getMessage(),Toast.LENGTH_LONG).show();
            res = e.getMessage();
            return res;
        }


        try {
            mDatabaseHelper.deletechemist();
            // toastMessage("Delete chemist Successfull");
        } catch (Exception e) {

            //Toast.makeText(getApplicationContext(),""+ e.getMessage(),Toast.LENGTH_LONG).show();
            res = e.getMessage();
            return res;
        }

        try {
            mDatabaseHelper.deletedr();
            // toastMessage("Delete Dr Successfull");
        } catch (Exception e) {

            //Toast.makeText(getApplicationContext(),""+ e.getMessage(),Toast.LENGTH_LONG).show();
            res = e.getMessage();
            return res;
        }

        try {
            mDatabaseHelper.deletePromoTools();
            // toastMessage("Delete Promo Tools Successfull");
        } catch (Exception e) {

            //Toast.makeText(getApplicationContext(),""+ e.getMessage(),Toast.LENGTH_LONG).show();
            res = e.getMessage();
            return res;
        }

        try {
            mDatabaseHelper.deletePromoTable();
            // toastMessage("Delete Promo Tools Successfull");
        } catch (Exception e) {

            //Toast.makeText(getApplicationContext(),""+ e.getMessage(),Toast.LENGTH_LONG).show();
            res = e.getMessage();
            return res;
        }

        try {
            mDatabaseHelper.deleteOrderApprovalTable();
            // toastMessage("Delete Promo Tools Successfull");
        } catch (Exception e) {

            //Toast.makeText(getApplicationContext(),""+ e.getMessage(),Toast.LENGTH_LONG).show();
            res = e.getMessage();
            return res;
        }

        userId = mDatabaseHelper.getUserIDFromEmployee();
        List<String> labels = new ArrayList<String>();
        labels= mDatabaseHelper.getUserDetailsFromEmployee();

        String ad_user_id=labels.get(0);
        String m_warehouse_id=labels.get(1);

        res = FetchProductCatagoryFromServer();

//        if (!res.equals("Product Catagory Sync Successful")){
//            return res;
//
//        }

        res = FetchProductsFromServer();
//        if (!res.equals("Product Sync Successful")){
//            return res;
//
//        }

        res = FetchAllChemistDetailsFromServer(userId);
//        if (!res.equals("Chemist Sync Successful")){
//            return res;
//
//        }

        res = FetchAllOrderForApproval(ad_user_id);

        res = FetchSalesTypeFromServer();



      //  res = FetchAllDrDetailsFromServer(userId);
//        if (!res.equals("Doctor Sync Successful")){
//            return res;
//
//        }

       // res = FetchPromoFromServer(ad_user_id,m_warehouse_id);

      //  res = FetchAllPromoToolsDetailsFromServer();

        try {


            return res;
            //finish();
//            Intent intent = new Intent(DBSync.this, Home.class);
//            startActivity(intent);
            //toastMessage("connection Closed");
        } catch (Exception e){
            res=e.getMessage();
        }
        return res;

    }



    private String FetchAllOrderForApproval(String user_id) {

        String order_doc, erp_orderNo, grand_total,prepared_by,sales_type,sent_date, res = "";

        boolean result = false;

        try {

            HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

            ArrayList list13=new ArrayList();
            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("name", "orderApproval"));
            list.add(new BasicNameValuePair("userId", user_id));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);



            HttpEntity httpEntity=httpResponse.getEntity();
            String s= readResponse(httpResponse);
            if (s.isEmpty() || !s.startsWith("[")){
                res="Order Not Found";
                return res;
            }


            ArrayList list4=new ArrayList();


            String[] parts = s.split("_");
            for(int i=0;i<parts.length;i++){
                Vector line=new Vector();
                String[] parts1 = parts[i].split(",");
                for(int j=0;j<parts1.length;j++){

                    String value=parts1[j].replaceFirst("[\\[\\]\\\" \"\\^:,]","");
                    value=value.replaceAll("[\\[\\]]","");
                    line.add(value);

                }
                list4.add(line);
            }



            for(int i1=0;i1<list4.size();i1++){
                Vector line= (Vector) list4.get(i1);
                order_doc =((String) line.get(0));
                erp_orderNo=(String) line.get(1);
                grand_total=((String) line.get(2));
                prepared_by = ((String) line.get(3));
                sales_type  = ((String) line.get(4));
                sent_date   = (String) line.get(5);
                sent_date = sent_date.replaceAll("\\d\\d:\\d\\d:\\d\\d.\\d","");
                result = mDatabaseHelper.insertIntoOrderApproval(order_doc, erp_orderNo,grand_total,prepared_by,sales_type,sent_date);
            }


            if (result) {

                //toastMessage("Insert  Successful into Doctor Table : ");
                res = "Order For Approval Sync Successful";
            }


        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(getApplicationContext(),"Failed to Data fetch",Toast.LENGTH_SHORT).show();
            res = e.getMessage();

        }
        return res;
    }



    private String FetchSalesTypeFromServer() {


        String name, description, res = "";

        boolean result = false;
        try {


            // HttpURLConnection con = (HttpURLConnection) ( new URL("http://"+ip+":8080/NaafcoWEBAPI/DBSync")).openConnection();
            HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            HttpClient httpClient=new DefaultHttpClient();
            //HttpPost httpPost=new HttpPost("http://"+ip+":8080/NaafcoWEBAPI/DBSync");
            HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("name", "salesType"));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);



            HttpEntity httpEntity=httpResponse.getEntity();
            String s= readResponse(httpResponse);
            if (s.isEmpty() || !s.startsWith("[")){
                res="salesType Not Foundd";
                return res;
            }


            ArrayList list4=new ArrayList();


            String[] parts = s.split("_");
            for(int i=0;i<parts.length;i++){
                Vector line=new Vector();
                String[] parts1 = parts[i].split(",");
                for(int j=0;j<parts1.length;j++){

                    String value=parts1[j].replaceFirst("[\\[\\]\\\" \"\\^:,]","");
                    value=value.replaceAll("[\\[\\]]","");
                    line.add(value);

                }
                list4.add(line);
            }



            for(int i1=0;i1<list4.size();i1++){
                Vector line= (Vector) list4.get(i1);
                name=((String) line.get(0));

                result = mDatabaseHelper.insertIntoSalesType(name);

            }



            if (result) {

                //toastMessage("Insert  Successful into PrmoTools Table : ");
                res = "Sales Type Sync Successful";


            }


        }  catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(getApplicationContext(),"Failed to Data fetch",Toast.LENGTH_SHORT).show();
            res = e.getMessage();

        }
        return res;

    }


    private String FetchAllPromoToolsDetailsFromServer() {


        String name, description, res = "";

        boolean result = false;
        try {


           // HttpURLConnection con = (HttpURLConnection) ( new URL("http://"+ip+":8080/NaafcoWEBAPI/DBSync")).openConnection();
            HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            HttpClient httpClient=new DefaultHttpClient();
            //HttpPost httpPost=new HttpPost("http://"+ip+":8080/NaafcoWEBAPI/DBSync");
            HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("name", "promotools"));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);



            HttpEntity httpEntity=httpResponse.getEntity();
            String s= readResponse(httpResponse);
            if (s.isEmpty() || !s.startsWith("[")){
                res="PromoTools Not Foundd";
                return res;
            }


            ArrayList list4=new ArrayList();


            String[] parts = s.split("_");
            for(int i=0;i<parts.length;i++){
                Vector line=new Vector();
                String[] parts1 = parts[i].split(",");
                for(int j=0;j<parts1.length;j++){

                    String value=parts1[j].replaceFirst("[\\[\\]\\\" \"\\^:,]","");
                    value=value.replaceAll("[\\[\\]]","");
                    line.add(value);

                }
                list4.add(line);
            }



            for(int i1=0;i1<list4.size();i1++){
                Vector line= (Vector) list4.get(i1);
                name=((String) line.get(0));
                description=(String) line.get(1);

                result = mDatabaseHelper.insertIntoPromoTools(name, description);

            }



            if (result) {

                //toastMessage("Insert  Successful into PrmoTools Table : ");
                res = "Promo Tools Sync Successful";
            }


        }  catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(getApplicationContext(),"Failed to Data fetch",Toast.LENGTH_SHORT).show();
            res = e.getMessage();

        }
        return res;

    }

    private String FetchAllDrDetailsFromServer(String userName) {

        String dr_code, dr_name, address, res = "";

        boolean result = false;

        try {

            HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

            ArrayList list13=new ArrayList();
            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("name", "doctor"));
            list.add(new BasicNameValuePair("userId", userName));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);



            HttpEntity httpEntity=httpResponse.getEntity();
            String s= readResponse(httpResponse);
            if (s.isEmpty() || !s.startsWith("[")){
                res="Doctor Not Foundd";
                return res;
            }


            ArrayList list4=new ArrayList();


            String[] parts = s.split("_");
            for(int i=0;i<parts.length;i++){
                Vector line=new Vector();
                String[] parts1 = parts[i].split(",");
                for(int j=0;j<parts1.length;j++){

                    String value=parts1[j].replaceFirst("[\\[\\]\\\" \"\\^:,]","");
                    value=value.replaceAll("[\\[\\]]","");
                    line.add(value);

                }
                list4.add(line);
            }



            for(int i1=0;i1<list4.size();i1++){
                Vector line= (Vector) list4.get(i1);
                dr_code=((String) line.get(0));
                dr_name=(String) line.get(1);
                address=((String) line.get(2));

                result = mDatabaseHelper.insertIntodrInfo(dr_code, dr_name, address);

            }


            if (result) {

                //toastMessage("Insert  Successful into Doctor Table : ");
                res = "Doctor Sync Successful";

            }


        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(getApplicationContext(),"Failed to Data fetch",Toast.LENGTH_SHORT).show();
            res = e.getMessage();

        }
        return res;
    }

    private String FetchPromoFromServer(String ad_user_id,String m_warehouseid) {

        String bp_code, bp_name, pr_code,pr_name,m_locator_id,m_warehouse_id,ad_userid,movementqty, res = "";

        boolean result = false;

        try {


            HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("name", "promo"));
            list.add(new BasicNameValuePair("ad_user_id", ad_user_id));
            list.add(new BasicNameValuePair("m_warehouse_id", m_warehouseid));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);



            HttpEntity httpEntity=httpResponse.getEntity();
            String s= readResponse(httpResponse);
            if (s.isEmpty() || !s.startsWith("[")){
                res="Promo Not Foundd";
                return res;
            }


            ArrayList list4=new ArrayList();


            String[] parts = s.split("_");
            for(int i=0;i<parts.length;i++){
                Vector line=new Vector();
                String[] parts1 = parts[i].split(",");
                for(int j=0;j<parts1.length;j++){

                    String value=parts1[j].replaceFirst("[\\[\\]\\\" \"\\^:,]","");
                    value=value.replaceAll("[\\[\\]]","");
                    line.add(value);

                }
                list4.add(line);
            }



            for(int i1=0;i1<list4.size();i1++){
                Vector line= (Vector) list4.get(i1);
                bp_code=((String) line.get(0));
                bp_name=(String) line.get(1);
                pr_code=((String) line.get(2));
                pr_name=((String) line.get(3));
                m_locator_id=((String) line.get(4));
                m_warehouse_id=((String) line.get(5));
                ad_userid=((String) line.get(6));
                movementqty=((String) line.get(7));

                result = mDatabaseHelper.insertIntoPromo(bp_code, bp_name, pr_code,pr_name,m_locator_id,m_warehouse_id,ad_userid,movementqty);

            }


            if (result) {

                //toastMessage("Insert  Successful into Doctor Table : ");
                res = "Promo Sync Successful";

            }


        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(getApplicationContext(),"Failed to Data fetch",Toast.LENGTH_SHORT).show();
            res = e.getMessage();

        }
        return res;
    }

    private String FetchAllChemistDetailsFromServer(String userName) {

        String chemist_code, chemist_name, address,balance,phoneNo, res = "";

        boolean result = false;

        try {


            HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("name", "customer"));
            list.add(new BasicNameValuePair("userId", userName));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);



            HttpEntity httpEntity=httpResponse.getEntity();
            String s= readResponse(httpResponse);
            if (s.isEmpty() || !s.startsWith("[")){
                res="Customer Not Foundd";
                return res;
            }


            ArrayList list4=new ArrayList();


            String[] parts = s.split("_");
            for(int i=0;i<parts.length;i++){
                Vector line=new Vector();
                String[] parts1 = parts[i].split(",",5);
                for(int j=0;j<parts1.length;j++){

                    String value=parts1[j].replaceFirst("[\\[\\]\\\" \"\\^:,]","");
                    value=value.replaceAll("[\\[\\]]","");
                    line.add(value);

                }
                list4.add(line);
            }



            for(int i1=0;i1<list4.size();i1++){
                Vector line= (Vector) list4.get(i1);
                chemist_code=((String) line.get(0));
                chemist_name=(String) line.get(1);
                balance = ((String) line.get(2));
                phoneNo = ((String) line.get(3));
                address=((String) line.get(4));
               // address = address.replaceAll(",","");

                result = mDatabaseHelper.insertIntoChemist(chemist_code, chemist_name, address,balance,phoneNo);

            }

            if (result) {

                //toastMessage("Insert  Successful into Chemist Table : ");
                res = "Chemist Sync Successful";


            }


        }catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(getApplicationContext(),"Failed to Data fetch",Toast.LENGTH_SHORT).show();
            res = e.getMessage();

        }
        return res;
    }

    private String  FetchProductCatagoryFromServer() {


        String catagoryname, res = "";

        int id;
        boolean result = false;
        try {


            HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("name", "product_catagory"));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);



            HttpEntity httpEntity=httpResponse.getEntity();
            String s= readResponse(httpResponse);
            if (s.isEmpty() || !s.startsWith("[")){
                res="Product Catagory Not Foundd";
                return res;
            }


            ArrayList list4=new ArrayList();

            String[] parts = s.split("_");
            for(int i=0;i<parts.length;i++){
                Vector line=new Vector();
                String[] parts1 = parts[i].split(",");
                for(int j=0;j<parts1.length;j++){

                    String value=parts1[j].replaceFirst("[\\[\\]\\\" \"\\^:,]","");
                    value=value.replaceAll("[\\[\\]]","");
                    line.add(value);

                }
                list4.add(line);
            }


            String ad_user_id="",m_warehouse_id="";
            for(int i1=0;i1<list4.size();i1++){
                Vector line= (Vector) list4.get(i1);
                int catagoryId=(Integer.parseInt((String) line.get(0)));
                String catagoryName=(String) line.get(1);

                result = mDatabaseHelper.insertIntoProductCatagory(catagoryId, catagoryName);

            }

            if (result) {

                //toastMessage("Insert  Successful into Product Catagory Table : ");
                res = "Product Catagory Sync Successful";


            }


        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(getApplicationContext(),"Failed to Data fetch",Toast.LENGTH_SHORT).show();
            res = e.getMessage();


        }
        return res;
    }

    public String FetchProductsFromServer() {

        int catagoryID;
        boolean result = false;

        String name, productID, price, res = "",unitName = "",avlQty = "";

        try {

            HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("name", "product"));
            list.add(new BasicNameValuePair("m_warehouse_id",String.valueOf(branchId)));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);



            HttpEntity httpEntity=httpResponse.getEntity();
            String s= readResponse(httpResponse);
            if (s.isEmpty() || !s.startsWith("[")){
                res="Product  Not Foundd";
                return res;
            }


            ArrayList list4=new ArrayList();


            String[] parts = s.split("_");
            for(int i=0;i<parts.length;i++){
                Vector line=new Vector();
                String[] parts1 = parts[i].split(",");
                for(int j=0;j<parts1.length;j++){

                    String value=parts1[j].replaceFirst("[\\[\\]\\\" \"\\^:,]","");
                    value=value.replaceAll("[\\[\\]]","");
                    line.add(value);

                }
                list4.add(line);
            }



            for(int i1=0;i1<list4.size();i1++){
                Vector line= (Vector) list4.get(i1);
                productID=((String) line.get(0));
                name=(String) line.get(1);
                catagoryID=Integer.parseInt((String) line.get(2));
                price=((String) line.get(3));
                unitName = ((String)line.get(4));
                avlQty   = ((String)line.get(5));
                result = mDatabaseHelper.insertIntoProduct(productID, name, catagoryID, price,unitName,avlQty);

            }




            if (result) {

                //toastMessage("Insert  Successful into Product Table : ");
                res = "Product Sync Successful";

            }


        } catch (Exception e) {
            res = e.getMessage();
        }
        return res;

    }

    public void ShowAlert(String title, String msg) {


        androidx.appcompat.app.AlertDialog.Builder dlgAlert = new androidx.appcompat.app.AlertDialog.Builder(Home.this);

        dlgAlert.setMessage(msg);
        // dlgAlert.setTitle(Html.fromHtml("<font color='#FF7F27'>"+title+"</font>"));
        dlgAlert.setIcon(R.drawable.naafcologo);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    private final class AsyncSender extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        @Override
        protected String doInBackground(String... params) {
            String result = "", error = "";
            try {
                result = DBSYNC();
            } catch (Exception e) {
                error = e.getMessage();
                e.printStackTrace();
                return error;
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(Home.this);
            pd.setTitle("Please Wait ");
            pd.setMessage("DBSync On Progress ........");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();

            if (result.equals("Sales Type Sync Successful")) {
                toastMessage("DBSYNC Successful");
            } else {
                toastMessage(result);
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

        }
        return a;
    }

    private final class AsyncLoadRecord extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        @Override
        protected String doInBackground(String... params) {
            String result = "", error = "";
            try {
                mDatabaseHelper.deleteSalesOrderRecord();
                result = salesRecordSync();
            } catch (Exception e) {
                error = e.getMessage();
                e.printStackTrace();
                return error;
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(Home.this);
            pd.setTitle("Please Wait ");
            pd.setMessage("Record Sync On Progress ........");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            try{
                if (result.equals("Order Record Sync Successful")) {
                    toastMessage("Order Sync Successful");
                } else {
                    toastMessage(result);
                }
            }catch (NullPointerException e){

            }
        }
    }

    public String salesRecordSync() {
        boolean result = false;
        String  res = "";
        try {

            HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("name", "salesorderlist"));
            list.add(new BasicNameValuePair("userid",employeeid));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);

            HttpEntity httpEntity=httpResponse.getEntity();
            String s= readResponse(httpResponse);
            if (s.isEmpty() || !s.startsWith("[")){
                res="Sales Record Not Found";
                return res;
            }


            ArrayList list4=new ArrayList();


            String[] parts = s.split("_");
            for(int i=0;i<parts.length;i++){
                Vector line=new Vector();
                String[] parts1 = parts[i].split(",");
                for(int j=0;j<parts1.length;j++){

                    String value=parts1[j].replaceFirst("[\\[\\]\\\" \"\\^:,]","");
                    value=value.replaceAll("[\\[\\]]","");
                    line.add(value);

                }
                list4.add(line);
            }

            for(int i1=0;i1<list4.size();i1++){
                Vector line= (Vector) list4.get(i1);
                String docNo=((String) line.get(0));
                String orderDate=(String) line.get(1);
                String grandTotal=(String) line.get(2);
                String productName=((String) line.get(3));
                String  uom = ((String)line.get(4));
                String  qty_ordered   = ((String)line.get(5));
                String pricePerProduct   = ((String)line.get(6));
                String customerName   = ((String)line.get(7));
                orderDate = orderDate.replaceAll("\\d\\d:\\d\\d:\\d\\d.\\d","");
                result = mDatabaseHelper.insertIntoOrderRecord(docNo, orderDate, customerName,grandTotal,productName,uom,qty_ordered,pricePerProduct);

            }

            if (result) {
                res = "Order Record Sync Successful";
            }


        } catch (Exception e) {
            res = e.getMessage();
        }
        return res;
    }
}
