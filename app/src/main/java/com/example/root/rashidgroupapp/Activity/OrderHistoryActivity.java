package com.example.root.rashidgroupapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.root.rashidgroupapp.Adapter.OrderSummaryRecyclerViewAdapter;
import com.example.root.rashidgroupapp.Modal.OrderSummaryModal;
import com.example.root.rashidgroupapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class OrderHistoryActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView totalOrderedPriceTextView,priceInWordsTextView,dateFromTextView,dateToTextView;
    ImageButton datePickerButtonFrom,getDatePickerButtonTo;
    RecyclerView summaryRecyclerView;
    DatabaseHelper mDatabaseHelper;

    SimpleDateFormat df;
    String dateStr1,dateStr2,dateFrom,dateTo;
    ArrayList<OrderSummaryModal> orderSummaryModalsArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        toolbar = findViewById(R.id.toolbar);
        summaryRecyclerView = findViewById(R.id.order_summary_list_recyclerview);

        totalOrderedPriceTextView = findViewById(R.id.tPrice_summary_textView);
        priceInWordsTextView      = findViewById(R.id.tPrice_summary_textView_inwords);
        dateFromTextView          = findViewById(R.id.dateFrom_textView);
        dateToTextView            = findViewById(R.id.dateTo_textView);

        datePickerButtonFrom      = findViewById(R.id.datePicker_imagebutton_from);
        getDatePickerButtonTo     = findViewById(R.id.datePicker_imagebutton_to);

        mDatabaseHelper = new DatabaseHelper(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderHistoryActivity.this, Home.class);
                startActivity(intent);
                finish();
            }
        });
        Calendar cal = Calendar.getInstance();
        df = new SimpleDateFormat("yyyy-MM-dd");
        dateStr1 = df.format(cal.getTime());
        dateStr2 = df.format(cal.getTime());

        dateFromTextView.setText(dateStr1);
        dateToTextView.setText(dateStr2);

        cal.add(Calendar.DATE, +1);
        dateStr2=  df.format(cal.getTime());



        loadOrderSummary();

    }

    private void loadOrderSummary(){
        double grandTotal = 0.00;
        orderSummaryModalsArrayList.clear();
        String userID = mDatabaseHelper.getUserIDFromEmployee();
      //  String dateFrom = dateFromTextView.getText().toString();
       // String dateTo   = dateToTextView.getText().toString();

       // Cursor data = mDatabaseHelper.getSalesOrderByDate(dateFrom,dateTo);
        Cursor data = mDatabaseHelper.getSalesOrderByDate(dateStr1,dateStr2);
        while(data.moveToNext()){
            String docNo = (data.getString(0));
            String date = data.getString(1);
           // String customerID = data.getString(2);
            String customerName = data.getString(2);
            //String amount = data.getString(3);

//            String customerName = mDatabaseHelper.getChemistName(customerID);
            String amount = ((mDatabaseHelper.getTotalPrice(docNo)));

            double total = Double.parseDouble(amount);
            grandTotal = grandTotal + total;

            OrderSummaryModal orderSummaryModal = new OrderSummaryModal(docNo,date,customerName,amount);
            orderSummaryModalsArrayList.add(orderSummaryModal);
        }

        initRecyclerViw();
        totalOrderedPriceTextView.setText(String.valueOf(grandTotal));
        int intTotalPrice = (int) grandTotal;
        String return_val_in_words =   NumberToWords1.convert(intTotalPrice);
        priceInWordsTextView.setText(return_val_in_words +" Taka");
    }

    private void initRecyclerViw(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        summaryRecyclerView.setLayoutManager(linearLayoutManager);
        OrderSummaryRecyclerViewAdapter orderSummaryRecyclerViewAdapter = new OrderSummaryRecyclerViewAdapter(OrderHistoryActivity.this,orderSummaryModalsArrayList);
        summaryRecyclerView.setAdapter(orderSummaryRecyclerViewAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(OrderHistoryActivity.this, Home.class);
        startActivity(intent);
        finish();
    }

    public void onClickDatePickerFrom(View v) {

       final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        @SuppressLint("WrongViewCast") DatePickerDialog datePicker = new DatePickerDialog(findViewById(R.id.dateFrom_textView).getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                //   daynum = (TextView) getActivity().findViewById(R.id.daynum);
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, (month));
                c.set(Calendar.DAY_OF_MONTH, day);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
               // dateFromTextView.setText(view.getYear() + "-" + (view.getMonth() + 1) + "-" + view.getDayOfMonth());
                dateStr1 = sdf.format(c.getTime());
                dateFromTextView.setText(sdf.format(c.getTime()));
                loadOrderSummary();
            }
            }, day, month, year);
        //subtract -3000 to select date before current date
        c.add(Calendar.YEAR, -1);
        datePicker.getDatePicker().setSpinnersShown(true);
        //datePicker.getDatePicker().setCalendarViewShown(false);
        datePicker.getDatePicker().setMinDate(c.getTimeInMillis());
        datePicker.updateDate(year,month,day);
       // c.add(Calendar.YEAR, 4);
       // datePicker.getDatePicker().setMaxDate(c.getTimeInMillis());
        //datePicker.setTitle("Select Date");
        datePicker.show();
    }

    public void onClickDatePickerTo(View v) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        @SuppressLint("WrongViewCast") DatePickerDialog datePicker = new DatePickerDialog(findViewById(R.id.dateTo_textView).getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                //   daynum = (TextView) getActivity().findViewById(R.id.daynum);
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, (month));
                c.set(Calendar.DAY_OF_MONTH, day);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
               // dateToTextView.setText(view.getYear() + "-" + (view.getMonth() + 1) + "-" + view.getDayOfMonth());
                dateToTextView.setText(sdf.format(c.getTime()));
                c.add(Calendar.DATE, +1);
                dateStr2 = df.format(c.getTime());
                loadOrderSummary();
            }
            }, day, month, year);
        //datePicker.show();
        //subtract -3000 to select date before current date
        c.add(Calendar.YEAR, -1);
      //  c.add(Calendar.MONTH, -12);
        datePicker.getDatePicker().setSpinnersShown(true);
       // datePicker.getDatePicker().setCalendarViewShown(true);
        datePicker.getDatePicker().setMinDate(c.getTimeInMillis());
        datePicker.updateDate(year,month,day);
        //c.add(Calendar.YEAR, 4);
       // datePicker.getDatePicker().setMaxDate(c.getTimeInMillis());
       // datePicker.setTitle("Select Date");
        datePicker.show();



    }
}
