package com.example.root.rashidgroupapp.Activity;

import android.content.Intent;
import android.database.Cursor;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Order extends AppCompatActivity {

    Spinner productCatagorySpinner,productSpinner;
    EditText quantityEditText,priceEditText;

    Button buttonAdd,buttonView,buttonSendToServer,buttonGetErpValue;
    SearchView searchProduct;


    String Price,unit;
    int count=1;
    EditText editText_dateTime,searchCodeEditText;

    EditText searchEditText;
    CheckBox codeCheckbox;
    TextView headerTextView,chemistDetailsTextView,showErpReturnvalueTextView,unitTextView,totalPrice_textView,avilableQty_textView;
    TextView avlUnit;
    DatabaseHelper mDatabaseHelper;

    ArrayList<String> intentList;

    List<String> unitArrayList = new ArrayList<>();
    double Productprice;
    int quantityOfProduct;

    String DocumentNo;

    String ip = "**********";

    final String apiUrl = "http://"+ip+":8080/**********";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        productSpinner = (Spinner) findViewById(R.id.product_spinner);
        //productCodeSpinner = findViewById(R.id.product_code_spinner);
        searchCodeEditText = findViewById(R.id.search_product_code_editText);
       // productCatagorySpinner = (Spinner) findViewById(R.id.productCatagory_spinner);
        //searchProduct = findViewById(R.id.search_view);
        codeCheckbox = findViewById(R.id.search_code_checkbox_order);

        buttonAdd = (Button) findViewById(R.id.button_Add);
        buttonView = (Button) findViewById(R.id.button_View);
        //buttonGetErpValue = (Button) findViewById(R.id.getERPvalue_button);
     // buttonSendToServer = (Button) findViewById(R.id.sendToServer_button);
        quantityEditText = (EditText) findViewById(R.id.editText_quantity);
        editText_dateTime = (EditText) findViewById(R.id.date_editText);
        priceEditText = (EditText) findViewById(R.id.editText_price);
        searchEditText = (EditText) findViewById(R.id.search_editText);

        totalPrice_textView = findViewById(R.id.TextView_total_price);
        avilableQty_textView  = findViewById(R.id.TextView_available);
        //priceEditText.setEnabled(false);
//        buttonGetErpValue.setEnabled(false);
        avlUnit = findViewById(R.id.TextView_available_unit);
        headerTextView = (TextView) findViewById(R.id.header_textView);
        chemistDetailsTextView = (TextView) findViewById(R.id.chemistDetails_textView);
       // showErpReturnvalueTextView = (TextView) findViewById(R.id.erpReturn_textView);
        unitTextView = findViewById(R.id.TextView_unit);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            intentList = getIntent().getStringArrayListExtra("Listintent");
            chemistDetailsTextView.setText("Customer Name : " + intentList.get(0) + " \nCustomer Address : " + intentList.get(3) + " \n Document No : " + intentList.get(1));
            DocumentNo = intentList.get(1);
            editText_dateTime.setText(intentList.get(2));
        }

        mDatabaseHelper = new DatabaseHelper(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = df.format(cal.getTime());

        String s = editText_dateTime.getText().toString();


        editText_dateTime.setEnabled(false);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(Order.this, ChemistSelection.class);
                startActivity(intent);
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


       // OnProductCatagorySelectedSpinner();


        LoadProductSpinner();
       // LoadProductCodeSpinner();
        onQuantityEditTextFocusChange();
        OnAddButtonPress();


       // LoadProductCatagorySpinner();
       // OnSendToServerButtonClick();

        //OnGetERPValueButtonClick();
        //CalculateTotalOnQuantityInput();



        Search();
        quantityEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {


                if (TextUtils.isEmpty(quantityEditText.getText().toString()))

                {
                    quantityEditText.setError("Please Enter Quantity ");
                    return;
                }

//                Productprice=Double.parseDouble(Price);
//                quantityOfProduct=Integer.parseInt(quantityEditText.getText().toString());
//                priceEditText.setText(String.valueOf((int)quantityOfProduct*Productprice));


            }
        });
    }

    private void onQuantityEditTextFocusChange(){
        quantityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!priceEditText.getText().toString().equals("") && !quantityEditText.getText().toString().equals("")){
                    if(!priceEditText.getText().toString().equals(".") && !quantityEditText.getText().toString().equals(".")) {
                        double price = Double.parseDouble(priceEditText.getText().toString());
                        double quantity = Double.parseDouble(quantityEditText.getText().toString());
                        double total_price = price * quantity;
                        totalPrice_textView.setText(String.valueOf(total_price));
                    }
                }else{
                    totalPrice_textView.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        priceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!quantityEditText.getText().toString().equals("") && !priceEditText.getText().toString().equals("")){
                    if(!quantityEditText.getText().toString().equals(".") && !priceEditText.getText().toString().equals(".")) {
                        double price = Double.parseDouble(priceEditText.getText().toString());
                        double quantity = Double.parseDouble(quantityEditText.getText().toString());
                        double total_price = price * quantity;
                        totalPrice_textView.setText(String.valueOf(total_price));
                    }
                }else{
                    totalPrice_textView.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void LoadProductSpinner() {

        List<String> lables = (List<String>) mDatabaseHelper.getAllProduct();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        productSpinner.setAdapter(dataAdapter);
        OnProductSelectedSpinner();
    }

//    private void LoadProductCodeSpinner() {
//
//        List<String> lables = (List<String>) mDatabaseHelper.getAllProductCode();
//
//        // Creating adapter for spinner
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, lables);
//
//        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // attaching data adapter to spinner
//        productCodeSpinner.setAdapter(dataAdapter);
//        OnProductSelectedSpinner();
//    }

    private void OnGetERPValueButtonClick() {

        buttonGetErpValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String erp_docNo;
                Double grand_total;
                try {


                    HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/SalesOrder")).openConnection();
                    con.setRequestMethod("POST");
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.connect();

                    HttpClient httpClient=new DefaultHttpClient();
                    HttpPost httpPost=new HttpPost(""+apiUrl+"/SalesOrder");

                    List<NameValuePair> list=new ArrayList<NameValuePair>();
                    list.add(new BasicNameValuePair("type", "GetERPValue"));
                    list.add(new BasicNameValuePair("docNo", DocumentNo));
                    httpPost.setEntity(new UrlEncodedFormEntity(list));
                    HttpResponse httpResponse=	httpClient.execute(httpPost);



                    HttpEntity httpEntity=httpResponse.getEntity();
                    String s= readResponse(httpResponse);


                    String[] parts=s.split("_");

                    erp_docNo=parts[0];
                    grand_total= Double.valueOf(parts[1]);


                    if (erp_docNo==null || erp_docNo.isEmpty() || grand_total==0 ){

                        toastMessage("Server Still in Progress , Please Wait");
                        showErpReturnvalueTextView.setText("");
                    }

                    else
                    {
                        showErpReturnvalueTextView.setText("Doc No : "+erp_docNo+"\n"+"G.Total : "+String.valueOf(grand_total));
                    }


                }catch (ProtocolException e) {
                    ShowAlert("Error",e.getMessage());
                } catch (MalformedURLException e) {
                    ShowAlert("Error",e.getMessage());
                } catch (ClientProtocolException e) {
                    ShowAlert("Error",e.getMessage());
                } catch (UnsupportedEncodingException e) {
                    ShowAlert("Error",e.getMessage());
                } catch (IOException e) {
                    ShowAlert("Error",e.getMessage());
                }


            }
        });



    }




    public void Search(){

//        searchEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                String key=searchEditText.getText().toString();
//                List<String> lables = (List<String>) mDatabaseHelper.searchProduct(key);
//
//                if (lables.isEmpty()){
//                    List<String> lables1 = (List<String>) mDatabaseHelper.getAllProduct();
//
//                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Order.this,
//                            android.R.layout.simple_spinner_item, lables1);
//                    // Drop down layout style - list view with radio button
//                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    // attaching data adapter to spinner
//                    productSpinner.setAdapter(dataAdapter);
//                    dataAdapter.notifyDataSetChanged();
//                    //quantityEditText.setText("1");
//                    toastMessage("NOT Found");
//                }
//                else {
//
//                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Order.this,
//                            android.R.layout.simple_spinner_item, lables);
//                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    // attaching data adapter to spinner
//                    productSpinner.setAdapter(dataAdapter);
//                    dataAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
        searchCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (codeCheckbox.isChecked()){
                    String searchKey = searchCodeEditText.getText().toString();

                    List<String> propductList = mDatabaseHelper.searchProductByCode(searchKey);

                    if (propductList.isEmpty()) {

                        List<String> lables1 = (List<String>) mDatabaseHelper.getAllProduct();

                        // Creating adapter for spinnerBB
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Order.this,
                                android.R.layout.simple_spinner_item, lables1);
                        // Drop down layout style - list view with radio button
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // attaching data adapter to spinner
                        productSpinner.setAdapter(dataAdapter);
                        dataAdapter.notifyDataSetChanged();
                        toastMessage("NOT Found");

                    } else {
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Order.this,
                                android.R.layout.simple_spinner_item, propductList);

                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // attaching data adapter to spinner
                        productSpinner.setAdapter(dataAdapter);
                        dataAdapter.notifyDataSetChanged();

                    }
                }else{

                    String key=searchCodeEditText.getText().toString();
                    List<String> lables = (List<String>) mDatabaseHelper.searchProduct(key);

                    if (lables.isEmpty()){
                        List<String> lables1 = (List<String>) mDatabaseHelper.getAllProduct();

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Order.this,
                                android.R.layout.simple_spinner_item, lables1);
                        // Drop down layout style - list view with radio button
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // attaching data adapter to spinner
                        productSpinner.setAdapter(dataAdapter);
                        dataAdapter.notifyDataSetChanged();
                        //quantityEditText.setText("1");
                        toastMessage("NOT Found");
                    }
                    else {

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Order.this,
                                android.R.layout.simple_spinner_item, lables);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // attaching data adapter to spinner
                        productSpinner.setAdapter(dataAdapter);
                        dataAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

//    private void OnSendToServerButtonClick() {
//
//        buttonSendToServer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AsyncSendToServer task = new AsyncSendToServer();
//                task.execute();
//            }
//        });
//
//    }

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



    public void LoadProductCatagorySpinner() {


        List<String> lables = (List<String>) mDatabaseHelper.getAllProductCatagory();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        productCatagorySpinner.setAdapter(dataAdapter);

    }





    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }



    public void OnAddButtonPress() {


        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (productSpinner.getCount()==0){
                    toastMessage("No Product Selected !!");
                    return;
                }
                if (TextUtils.isEmpty(quantityEditText.getText().toString()) || quantityEditText.getText().toString().equals(""))

                {
                    quantityEditText.setError("Please Enter Quantity ");
                    return;
                }

                if(priceEditText.getText().toString().isEmpty() || priceEditText.getText().toString().equals("")){
                    toastMessage("No Price Found add price");
                    return;
                }

                if (priceEditText.getText().toString().equals("0")){
                    toastMessage("Please Enter Price !!");
                    return;
                }

                if (quantityEditText.getText().toString().equals("0")){
                    toastMessage("Please Enter Quantity !!");
                    return;
                }

                if ( quantityEditText.getText().toString().equals(".") ){
                    toastMessage("Please Enter Quantity !!");
                    return;
                }

                if ( priceEditText.getText().toString().equals(".") ){
                    toastMessage("Please Enter price !!");
                    return;
                }
                String productName;
                String  docNo=(intentList.get(1));
               // String  docNo= "1";
                //catagory=productCatagorySpinner.getSelectedItem().toString();
                productName=productSpinner.getSelectedItem().toString();

                String unitPrice = priceEditText.getText().toString();

                String uom       = unitTextView.getText().toString();

                String quantity=(quantityEditText.getText().toString());

                String totalPrice    = totalPrice_textView.getText().toString();

                String productID=mDatabaseHelper.getProductIDByProductName(productName);


                boolean checkAlreadyAdded=mDatabaseHelper.getSalesOrderLineByDocAndProduct(docNo,productID);

                if (checkAlreadyAdded==true){
                    toastMessage("Already Added ");
                    return;
                }


                boolean result=mDatabaseHelper.insertIntoSalesOrderLine(docNo,productID,quantity,Double.parseDouble(totalPrice),productName,unitPrice,uom);

                if(result){
                    toastMessage("Order Line Inserted Successfully!!!");
                    quantityEditText.setText("");
                    priceEditText.setText("");
                    totalPrice_textView.setText("");
                    searchEditText.setText("");
                    searchCodeEditText.setText("");
                }

                else {
                    toastMessage("Order Line Insertion Failed !!");
                }



            }
        });

    }


    public void OnViewButtonPress(View view) {
        //get the data and append to a list
        Cursor data = mDatabaseHelper.getSalesOrderLineDataTemp(intentList.get(1));

        //Cursor data = mDatabaseHelper.getSalesOrderLineData("1");
        if(data.moveToNext()) {

            Intent activityintent = new Intent(Order.this, OrderListActivity.class);
            activityintent.putExtra("Listintent",intentList);
           // activityintent.putExtra("docNo","1");
            startActivity(activityintent);
            finish();
        }
        else {
            toastMessage("No OrderLine Exists to Show");
            return;

        }



    }





    public void OnProductCatagorySelectedSpinner(){

        productCatagorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                String productCatagory=productCatagorySpinner.getSelectedItem().toString();

                int productCatagoryID=mDatabaseHelper.getCatagoryID(productCatagory);

                List<String> lables = (List<String>) mDatabaseHelper.getAllProductByCatagory(productCatagoryID);

                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Order.this,
                        android.R.layout.simple_spinner_item, lables);
                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                productSpinner.setAdapter(dataAdapter);
                quantityEditText.setText("1");

            }

            public void onNothingSelected(AdapterView<?> adapterView) {


                return;
            }
        });
    }


    public void OnProductSelectedSpinner(){

        productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                String selectedProduct=productSpinner.getSelectedItem().toString();

                Price=mDatabaseHelper.getProductPrice(selectedProduct);
                unit = mDatabaseHelper.getProductUnit(selectedProduct);
                String avlProductQty = mDatabaseHelper.getProductQty(selectedProduct);

                quantityEditText.setText("");
                priceEditText.setText("");
                unitTextView.setText(unit);
                avilableQty_textView.setText(avlProductQty);
                avlUnit.setText(unit);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {


                return;
            }
        });
    }


    public void CalculateTotalOnQuantityInput(){
        quantityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                //String key=quantityEditText.getText().toString();

                if (Price==null || Price.isEmpty() || Price.equals("null") ){
                    toastMessage("Price Not Available For this Product ");
                    return;
                }

                if (quantityEditText.getText().toString().isEmpty())

                {
                    //toastMessage("Please Enter Quantity");
                    priceEditText.setText("");
                    return;
                }

                Productprice= Double.parseDouble(Price);
                String q=quantityEditText.getText().toString();
                quantityOfProduct=Integer.parseInt(q);

                Double total=quantityOfProduct*Productprice;

                String value=String.valueOf(new DecimalFormat("##.##").format(total));
                //String amount= String.valueOf(Double.parseDouble(Price));
                priceEditText.setText(value);





            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }



    public void onBackPressed(){

        finish();
        Intent intent=new Intent(Order.this,ChemistSelection.class);
        startActivity(intent);

    }
    public void ShowAlert(String title,String msg){


        androidx.appcompat.app.AlertDialog.Builder dlgAlert  = new androidx.appcompat.app.AlertDialog.Builder(Order.this);

        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.drawable.logo_11);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }


//    private final class AsyncSendToServer extends AsyncTask<String,String,String> {
//
//        // String result = "";
//        @Override
//        protected String doInBackground(String... strings) {
//            String result = "";
//            String docNo=(intentList.get(1));
//            String userID=mDatabaseHelper.getUserIDFromEmployee();
//
//            boolean isActive=true;
//
//            Cursor check = mDatabaseHelper.getSalesOrderLineData(docNo);
//            if(check.moveToNext()) {
//
//            } else {
//                toastMessage("No OrderLine Exists ");
//               // return;
//            }
//            try {
//                //conn.setAutoCommit(false);
//                Cursor data = mDatabaseHelper.getSalesOrder(docNo);
//                ArrayList salesHeader=new ArrayList();
//                if (data.moveToNext()) {
//
//                    String chemistID = data.getString(0);
//                    String date = data.getString(1);
//                    String delivaryDate = data.getString(2);
//                    String description  = data.getString(3);
//                    String promisedAmount = data.getString(4);
//                    String salesType      = data.getString(5);
//                    String billingLocation = data.getString(6);
//                    Vector headerLines=new Vector();
//                    headerLines.add(date);
//                    headerLines.add(docNo);
//                    headerLines.add(chemistID);
//                    headerLines.add(userID);
//                    headerLines.add(delivaryDate);
//                    headerLines.add(description);
//                    headerLines.add(promisedAmount);
//                    headerLines.add(salesType);
//                    headerLines.add(billingLocation);
//                    headerLines.add(orgId);
//                    headerLines.add(branchId);
//                    salesHeader.add(headerLines);
//
//
//
//                }
//
//                String headerString = "";
//                for (Object s : salesHeader) {
//                    if (headerString.isEmpty()) {
//                        headerString =headerString+s;
//                    } else {
//                        headerString =headerString+"_" + s;
//
//                    }
//                }
//
//                Cursor dataorderLine = mDatabaseHelper.getSalesOrderLineData(docNo);
//
//                ArrayList salesOrder=new ArrayList();
//                while (dataorderLine.moveToNext()) {
//
//                    String productID = dataorderLine.getString(0);
//                    int quantity = dataorderLine.getInt(1);
//                    double price  = dataorderLine.getDouble(2);
//                    Vector orderLines=new Vector();
//                    orderLines.add(docNo);
//                    orderLines.add(productID);
//                    orderLines.add(quantity);
//                    orderLines.add(price);
//
//                    salesOrder.add(orderLines);
//
//
//                }
//
//
//                String lineString = "";
//                for (Object s : salesOrder) {
//                    if (lineString.isEmpty()) {
//                        lineString =lineString+s;
//                    } else {
//                        lineString =lineString+"_" + s;
//
//                    }
//                }
//
//                HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/SalesOrder")).openConnection();
//                con.setRequestMethod("POST");
//                con.setDoInput(true);
//                con.setDoOutput(true);
//                con.connect();
//
//                HttpClient httpClient=new DefaultHttpClient();
//                HttpPost httpPost=new HttpPost(""+apiUrl+"/SalesOrder");
//
//                List<NameValuePair> list=new ArrayList<NameValuePair>();
//                list.add(new BasicNameValuePair("type", "salesorder"));
//                list.add(new BasicNameValuePair("header", headerString));
//                list.add(new BasicNameValuePair("lines",lineString));
//                httpPost.setEntity(new UrlEncodedFormEntity(list));
//                HttpResponse httpResponse=	httpClient.execute(httpPost);
//
//
//
//                HttpEntity httpEntity=httpResponse.getEntity();
//                String s= readResponse(httpResponse);
//
//                if (s.isEmpty() || !s.equals("Success")){
//                    result=s;
//                    return result;
//                } else if(s.equals("Success")){
//                    boolean res=mDatabaseHelper.updateOrderStatus(docNo);
//                    return "Success";
//                }
//
//            }
//            catch (Exception e){
//                result = e.getMessage();
//            }
//            return result;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            try{
//                if(s.equals("Success")){
//
//                    toastMessage("Order Placed Successfully");
//                    buttonSendToServer.setEnabled(false);
//                   // buttonGetErpValue.setEnabled(true);
//                    buttonAdd.setEnabled(false);
//                }else {
//                    buttonSendToServer.setEnabled(true);
//                  //  buttonGetErpValue.setEnabled(false);
//                    buttonAdd.setEnabled(true);
//
//                    toastMessage(s);
//                    ShowAlert("Error",s);
//                }
//            }catch (NullPointerException ex){
//
//            }
//        }
//    }

}
