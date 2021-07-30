package com.example.root.rashidgroupapp.Activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.rashidgroupapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChemistSelection extends AppCompatActivity {


    TextView addressText,balanceText,nameText,numberText,billingLocationTextView;
    EditText editText_dateTime,delivaryDate_editText,description_editText,amount_editText,search_editText;
    Button selectChemistButton;
    ImageButton datepickerImageButton_1,datePickerButton_2;

    CheckBox codeCheckbox;
    String dateStr;
    String chemistCode = "";
    Spinner chemistSpinner,salesTypeSpinner;

    List<String> intentList=new ArrayList<String>() ;
    List<String> customerAddressArrayList = new ArrayList<>();

    List<String> lables = new ArrayList<String>();
   // HashMap<Integer, String> addressHash = new HashMap<>();
  //  HashMap<Integer, String> balanceHash = new HashMap<>();
  //  HashMap<Integer, String> pNumberHash = new HashMap<>();
    HashMap<String, String> codeHash = new HashMap<>();
    DatabaseHelper mDatabaseHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chemist_selection);

        delivaryDate_editText = findViewById(R.id.delivary_date_editText);
        description_editText  = findViewById(R.id.description_editText);
        amount_editText       = findViewById(R.id.amount_editText);
        editText_dateTime=(EditText)findViewById(R.id.date_editText);

        salesTypeSpinner = findViewById(R.id.salesType_spinner);
       // billingLocationSpinner = findViewById(R.id.location_spinner);
        billingLocationTextView  = findViewById(R.id.location_textView);
        chemistSpinner=(Spinner)findViewById(R.id.chemistId_spinner);

       // addressText = findViewById(R.id.address_textView);
        balanceText = findViewById(R.id.balance_textView);
       // nameText    = findViewById(R.id.name_textView);
       // numberText  = findViewById(R.id.pnumber_textView);

        search_editText = findViewById(R.id.search_customer_editText);
        codeCheckbox    = findViewById(R.id.search_code_checkbox);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Calendar cal = Calendar.getInstance();
       // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        dateStr = df.format(cal.getTime());

        Calendar cal1 = Calendar.getInstance();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        editText_dateTime.setText(df1.format(cal1.getTime()));
        delivaryDate_editText.setText(df1.format(cal1.getTime()));


//        String s = editText_dateTime.getText().toString();
        mDatabaseHelper=new DatabaseHelper(this);


        //editText_dateTime.setEnabled(false);

        selectChemistButton=(Button)findViewById(R.id.chemistSelect_button);
        datepickerImageButton_1=(ImageButton)findViewById(R.id.datePicker_imagebutton);
        datePickerButton_2  = findViewById(R.id.datePicker_imagebutton1);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(ChemistSelection.this,Home.class);
                startActivity(intent);
                finish();
            }
        });

        selectChemistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChemistSelection.this, Order.class);
                startActivity(intent);
            }
        });

        loadSalesTypeSpinner();
        AsyncLoadSpinner task = new AsyncLoadSpinner();
        task.execute();

        OnSelectChemistButtonClick();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        searchCustomer();
        //nextActivity();

    }


    private void nextActivity(){
        selectChemistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent=new Intent(ChemistSelection.this,Order.class);
                startActivity(intent);
            }
        });

    }

    private void searchCustomer(){
            search_editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if(codeCheckbox.isChecked()) {
                        String searchKey = search_editText.getText().toString();

                        List<String> customerList = (List<String>) mDatabaseHelper.searchCustomerByCode(searchKey);

                        if (!customerList.isEmpty()) {
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ChemistSelection.this, android.R.layout.simple_spinner_item, customerList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            // attaching data adapter to spinner
                            chemistSpinner.setAdapter(dataAdapter);
                            dataAdapter.notifyDataSetChanged();
                            setCustomerDetailsByCode(searchKey);
                        } else {
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ChemistSelection.this, android.R.layout.simple_spinner_item, lables);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            // attaching data adapter to spinner
                            chemistSpinner.setAdapter(dataAdapter);
                            dataAdapter.notifyDataSetChanged();

                            setCustomerDetails();
                        }
                    }else{
                        String searchKey = search_editText.getText().toString();

                        List<String> customerList = (List<String>) mDatabaseHelper.searchCustomerByName(searchKey);

                        if(!customerList.isEmpty()){
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ChemistSelection.this, android.R.layout.simple_spinner_item, customerList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            // attaching data adapter to spinner
                            chemistSpinner.setAdapter(dataAdapter);
                            dataAdapter.notifyDataSetChanged();

                            setCustomerDetailsByName(searchKey);

                        }else{
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ChemistSelection.this, android.R.layout.simple_spinner_item, lables);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            // attaching data adapter to spinner
                            chemistSpinner.setAdapter(dataAdapter);
                            dataAdapter.notifyDataSetChanged();

                            setCustomerDetails();
                        }
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

    }

    private final class AsyncLoadSpinner extends AsyncTask<String,String,String> {



        String result;
        @Override
        protected String doInBackground(String... strings) {

            //customerAddressArrayList.clear();
            LoadCustomer();
            result = "Success";
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("Success")){
//                AsyncLoadCustomerSelectSpinner task = new AsyncLoadCustomerSelectSpinner();
//                task.execute();
                LoadChemistIDSpinner();
                getSelectedCustomerSpinnerItem();

            }else{
                toastMessage(s);
            }
        }
    }



    public void getSelectedCustomerSpinnerItem(){


        chemistSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {

                    balanceText.setText("");

                    String chemistName=chemistSpinner.getSelectedItem().toString();

                    chemistCode = codeHash.get(chemistName);
                    String address = mDatabaseHelper.getCustomerAddressByCode(chemistCode);
                    billingLocationTextView.setText(address);
                    String balance = mDatabaseHelper.getCustomerBalanceByCode(chemistCode);
                    balanceText.setText(balance);
                }catch (NullPointerException ex){

                }catch (ArrayIndexOutOfBoundsException ex){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public String generateDocumentNumber(){

        String Number=null;
        String date,time;

        String[] parts = dateStr.split(" ");
        date = parts[0];
        time= (parts[1]);

        String[] partsOfDate=date.split("-");
        String datepart0=partsOfDate[0];

        String datepart1=(datepart0.substring(2,4));//17

        String datepart2=partsOfDate[1];
        String datepart3=partsOfDate[2];

        String[]  partsOfTime=time.split(":");
        String timepart1=partsOfTime[0];
        String timepart2=partsOfTime[1];
        String timepart3=partsOfTime[2];

        Number=datepart1+datepart2+datepart3+timepart1+timepart2;

        return  Number;
    }

    public void OnSelectChemistButtonClick(){
        selectChemistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (chemistSpinner.getCount()<1){
                    toastMessage("No Customer Found");
                    return;
                }
                if (salesTypeSpinner.getCount()<1){
                    toastMessage("No Type Found");
                    return;
                }
                if (billingLocationTextView.equals("")){
                    toastMessage("No location Found");
                    return;
                }

                if (salesTypeSpinner.getSelectedItem().toString().equals("")){
                    toastMessage("Please Select Sales Type");
                    return;
                }
                if (chemistSpinner.getSelectedItem().toString().equals("")){
                    toastMessage("Please Select Customer");
                    return;
                }


//                if(amount_editText.getText().toString().isEmpty()){
//                    toastMessage("No Price Found add price");
//                    return;
//                }
               // String[] parts=chemistSpinner.getSelectedItem().toString().split("-");
//                String chemID = parts[0];
//                String chemName = parts[1];
//                String address=parts[2];

                // String address="";
//                for (int i=1;i<parts.length;i++) {
//
//                    if (i>=2){
//                        address=address+"-"+parts[i];
//                    }
//                    else
//                     address=address+parts[i];
//                }


              //  String chemName=(chemistSpinner.getSelectedItem().toString());
                //String chemID=mDatabaseHelper.getChemistCodeByName(chemName,address);

                //toastMessage("Chemist Code : "+chemID);



                String userID=mDatabaseHelper.getUserIDFromEmployee();
                //String datetime=generateDocumentNumber();

                long now = System.currentTimeMillis();
                String strLong = Long.toString(now);

                String documentNo=userID+strLong;
              //  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                dateStr=editText_dateTime.getText().toString();
                String billingLocation = billingLocationTextView.getText().toString();


               // billingLocation = billingLocation.replaceAll("\\s+","");
                billingLocation = billingLocation.replaceAll(",","-");
               // billingLocation = billingLocation.replaceAll("[^a-zA-Z0-9]", "");
                billingLocation = billingLocation.trim();

                if(!amount_editText.getText().toString().equals("")){
                    boolean result=mDatabaseHelper.insertIntoSalesOrder(documentNo,dateStr,chemistCode,userID,delivaryDate_editText.getText().toString(),description_editText.getText().toString(),amount_editText.getText().toString().replaceAll("\\s+",""),salesTypeSpinner.getSelectedItem().toString(),billingLocation);

                    if (result){
                        toastMessage("Insertion Successfull  !!!");
                    }

                    else{

                        ShowAlert("Error" , "Order Creation Failed , Something went wrong");
                        toastMessage("Order Creation Failed , Something went wrong");
                        return;
                    }
                }else{
                    boolean result=mDatabaseHelper.insertIntoSalesOrder(documentNo,dateStr,chemistCode,userID,delivaryDate_editText.getText().toString(),description_editText.getText().toString(),"0",salesTypeSpinner.getSelectedItem().toString(),billingLocation);
                    if (result){
                        toastMessage("Insertion Successfull  !!!");
                    }

                    else{

                        ShowAlert("Error" , "Order Creation Failed , Something went wrong");
                        toastMessage("Order Creation Failed , Something went wrong");
                        return;
                    }
                }



                finish();

                Intent intent=new Intent(ChemistSelection.this,Order.class);


                intentList.add(chemistSpinner.getSelectedItem().toString());
                intentList.add(documentNo);
                intentList.add(dateStr);
                intentList.add(billingLocationTextView.getText().toString());
                intent.putStringArrayListExtra("Listintent", (ArrayList<String>) intentList);

                startActivity(intent);
            }
        });
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void LoadCustomer(){

        lables.add("");
        Cursor data = mDatabaseHelper.getChemistDetails();
        //int serial = 1;
        while (data.moveToNext()) {

            String chemistCode = data.getString(0);
            String chemistName = data.getString(1);
            String chemistAddress = data.getString(2);
            String balance        = data.getString(3);
            String pNumber        = data.getString(4);

           // addressHash.put(serial,chemistAddress);
           // balanceHash.put(serial,balance);
            //pNumberHash.put(serial,pNumber);
            codeHash.put(chemistName,chemistCode);
            //  customerAddressArrayList.add(chemistAddress);
            if (chemistAddress.equals("") || chemistAddress==null || chemistAddress.isEmpty()){

                toastMessage("Address Not Found");
            } else{

                lables.add(chemistName);
            }
            //serial++;
        }
    }


    public void LoadChemistIDSpinner() {

        // Spinner Drop down elements
       // List<String> lables = (List<String>) mDatabaseHelper.getAllChemists();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // attaching data adapter to spinner
        chemistSpinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();

    }


    private void loadSalesTypeSpinner(){
        List<String> salesTypeArrayList = new ArrayList<String>();
        Cursor cursor = mDatabaseHelper.getSalesType();
        salesTypeArrayList.add("");
        if(cursor.moveToFirst()){
            do{
                String typeName     = cursor.getString(0);
                //if(!typeName.equals("Customer Return Material")) {
                    salesTypeArrayList.add(typeName);
               // }

            }while(cursor.moveToNext());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, salesTypeArrayList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        salesTypeSpinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
    }

    private void loadBillingLocationSpinner(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, customerAddressArrayList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        //billingLocationSpinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
    }

    public void onClickDatePicker(View v) {


       final Calendar c = Calendar.getInstance();
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //dateStr = df.format(c.getTime());
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);



        @SuppressLint("WrongViewCast") DatePickerDialog datePicker = new DatePickerDialog(findViewById(R.id.date_editText).getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                //   daynum = (TextView) getActivity().findViewById(R.id.daynum);
               // editText_dateTime.setText(df.format(view.getYear() + "-" + (view.getMonth() + 1) + "-" + view.getDayOfMonth()));
                //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String d2 = view.getYear() + "-" + (view.getMonth() + 1) + "-" + view.getDayOfMonth();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, (month));
                c.set(Calendar.DAY_OF_MONTH, day);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                editText_dateTime.setText(sdf.format(c.getTime()));

                //dobEditText.setEnabled(false);
                //  new ReadJSON().execute("url" + "day=" + view.getDayOfMonth() + "&" + "month=" + (view.getMonth() + 1) + "&" + "year=" + view.getYear());
                //  new ReadJSON1().execute("url" + "day=" + view.getDayOfMonth() + "&" + "month=" + (view.getMonth() + 1) + "&" + "year=" + view.getYear());
            }
        }, day, month, year);
        //subtract -3000 to select date before current date
       // c.add(Calendar.YEAR, -2);
        datePicker.getDatePicker().setSpinnersShown(true);
        datePicker.getDatePicker().setCalendarViewShown(false);
        datePicker.getDatePicker().setMinDate(c.getTimeInMillis());
        c.add(Calendar.YEAR, 4);
        datePicker.getDatePicker().setMaxDate(c.getTimeInMillis());
        datePicker.setTitle("Select Date");
        datePicker.show();
    }

    public void onClickDatePickerDelivary(View v) {


       final Calendar c = Calendar.getInstance();
      //  final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);



        @SuppressLint("WrongViewCast") DatePickerDialog datePicker = new DatePickerDialog(findViewById(R.id.delivary_date_editText).getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                //   daynum = (TextView) getActivity().findViewById(R.id.daynum);
               // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String d1 = view.getYear() + "-" + (view.getMonth() + 1) + "-" + view.getDayOfMonth();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, (month));
                c.set(Calendar.DAY_OF_MONTH, day);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                //delivaryDate_editText.setText(d1);
                delivaryDate_editText.setText(sdf.format(c.getTime()));
               // delivaryDate_editText.setText(df.format(view.getYear() + "-" + (view.getMonth() + 1) + "-" + view.getDayOfMonth()));
                //dobEditText.setEnabled(false);
                //  new ReadJSON().execute("url" + "day=" + view.getDayOfMonth() + "&" + "month=" + (view.getMonth() + 1) + "&" + "year=" + view.getYear());
                //  new ReadJSON1().execute("url" + "day=" + view.getDayOfMonth() + "&" + "month=" + (view.getMonth() + 1) + "&" + "year=" + view.getYear());
            }
        }, day, month, year);
        //subtract -3000 to select date before current date
        //c.add(Calendar.YEAR, -2);
        datePicker.getDatePicker().setSpinnersShown(true);
        datePicker.getDatePicker().setCalendarViewShown(false);
        datePicker.getDatePicker().setMinDate(c.getTimeInMillis());
        c.add(Calendar.YEAR, 4);
        datePicker.getDatePicker().setMaxDate(c.getTimeInMillis());
        datePicker.setTitle("Select Date");
        datePicker.show();
    }

    public void ShowAlert(String title,String msg){


        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(ChemistSelection.this);

        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
    public void onBackPressed(){

        Intent intent=new Intent(ChemistSelection.this,Home.class);
        startActivity(intent);
        finish();

    }

    public void setCustomerDetails(){
        Cursor data = mDatabaseHelper.getChemistDetails();

       // addressHash.clear();
        //balanceHash.clear();
        //pNumberHash.clear();
        codeHash.clear();
        //int serial = 1;
        while (data.moveToNext()) {

            String chemistCode = data.getString(0);
            String chemistName = data.getString(1);
            String chemistAddress = data.getString(2);
            String balance        = data.getString(3);
            String pNumber        = data.getString(4);

           // addressHash.put(serial,chemistAddress);
           // balanceHash.put(serial,balance);
          //  pNumberHash.put(serial,pNumber);
            codeHash.put(chemistName,chemistCode);
            //  customerAddressArrayList.add(chemistAddress);
            if (chemistAddress.equals("") || chemistAddress==null || chemistAddress.isEmpty()){

                toastMessage("Address Not Found");
            }
            //serial++;
        }

       // getSelectedCustomerSpinnerItem();
    }

    public void setCustomerDetailsByCode(String searchKey){
        Cursor data = mDatabaseHelper.searchCustomerByCodeNew(searchKey);

       // addressHash.clear();
       // balanceHash.clear();
       // pNumberHash.clear();
        codeHash.clear();
        //int serial = 1;
        while (data.moveToNext()) {

            String chemistCode = data.getString(0);
            String chemistName = data.getString(1);
            String chemistAddress = data.getString(2);
            String balance        = data.getString(3);
            String pNumber        = data.getString(4);

           // addressHash.put(serial,chemistAddress);
           // balanceHash.put(serial,balance);
           // pNumberHash.put(serial,pNumber);
            codeHash.put(chemistName,chemistCode);
            //  customerAddressArrayList.add(chemistAddress);
            if (chemistAddress.equals("") || chemistAddress==null || chemistAddress.isEmpty()){

                toastMessage("Address Not Found");
            }
           // serial++;
        }

       // getSelectedCustomerSpinnerItem();
    }

    public void setCustomerDetailsByName(String searchKey){
        Cursor data = mDatabaseHelper.searchCustomerByNameNew(searchKey);

       // addressHash.clear();
       // balanceHash.clear();
       // pNumberHash.clear();
        codeHash.clear();
        //int serial = 1;
        while (data.moveToNext()) {

            String chemistCode = data.getString(0);
            String chemistName = data.getString(1);
            String chemistAddress = data.getString(2);
            String balance        = data.getString(3);
            String pNumber        = data.getString(4);

           // addressHash.put(serial,chemistAddress);
           // balanceHash.put(serial,balance);
           // pNumberHash.put(serial,pNumber);
            codeHash.put(chemistName,chemistCode);
            //  customerAddressArrayList.add(chemistAddress);
            if (chemistAddress.equals("") || chemistAddress==null || chemistAddress.isEmpty()){

                toastMessage("Address Not Found");
            }
            //serial++;
        }

        //getSelectedCustomerSpinnerItem();
    }
}
