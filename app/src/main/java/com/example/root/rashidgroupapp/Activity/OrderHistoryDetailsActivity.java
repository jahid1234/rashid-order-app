package com.example.root.rashidgroupapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.root.rashidgroupapp.Adapter.OrderListHistoryRecyclerViewAdapter;
import com.example.root.rashidgroupapp.Modal.OrderListHistoryModal;
import com.example.root.rashidgroupapp.R;

import java.util.ArrayList;

public class OrderHistoryDetailsActivity extends AppCompatActivity {

    double total_price = 0.0;
    String docNo;
    Toolbar toolbar;
    RecyclerView orderListRecyclerView;
    TextView totalPriceTextView,priceInWordsTextView;
    DatabaseHelper databaseHelper;
    ArrayList<OrderListHistoryModal> orderListModalArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_details);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null)
        {
            docNo = getIntent().getStringExtra("docNo");
        }

        totalPriceTextView = findViewById(R.id.tPrice_history_textView);
        priceInWordsTextView = findViewById(R.id.tPrice_history_textView_inwords);
        orderListRecyclerView = findViewById(R.id.order_list_history_recyclerview);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadOrderHistory();
    }

    private void loadOrderHistory(){
        databaseHelper = new DatabaseHelper(this);
        Cursor data = databaseHelper.getSalesOrderLineData(docNo);
        while(data.moveToNext()) {
            String productName = data.getString(0);
            String uom = data.getString(1);
            //price =Double.parseDouble(new DecimalFormat("##.####").format(price));
            Double quantity = data.getDouble(2);
            Double unitPrice = data.getDouble(3);
            String customerName = data.getString(4);
            Double price = quantity * unitPrice;
            total_price = total_price + price;

            OrderListHistoryModal historyModal = new OrderListHistoryModal(productName,String.valueOf(unitPrice),String.valueOf(quantity),uom,price,customerName);
            orderListModalArrayList.add(historyModal);
        }

        initRecyclerView();
        totalPriceTextView.setText(String.valueOf(total_price));
        int intTotalPrice = (int) total_price;
        String return_val_in_words =   NumberToWords1.convert(intTotalPrice);
        priceInWordsTextView.setText(return_val_in_words +" Taka");
    }


    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        orderListRecyclerView.setLayoutManager(linearLayoutManager);
        OrderListHistoryRecyclerViewAdapter adapter = new OrderListHistoryRecyclerViewAdapter(orderListModalArrayList,OrderHistoryDetailsActivity.this);
        orderListRecyclerView.setAdapter(adapter);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
