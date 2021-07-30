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

import com.example.root.rashidgroupapp.Adapter.ApprovalOrderListRecyclerViewAdapter;
import com.example.root.rashidgroupapp.Modal.ApprovalOrderModal;
import com.example.root.rashidgroupapp.R;

import java.util.ArrayList;

public class OrderListForApprovalActivity extends AppCompatActivity {

    TextView emptyListText;
    Toolbar toolbar;
    RecyclerView orderListRecyclerView;
    DatabaseHelper mDatabaseHelper;
    ArrayList<ApprovalOrderModal> approvalOrderModals = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list_for_approval);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        emptyListText = findViewById(R.id.no_list_id);
        orderListRecyclerView = findViewById(R.id.approve_order_list_recyclerview);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(OrderListForApprovalActivity.this, Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });
        mDatabaseHelper = new DatabaseHelper(this);
        loadOrderForApproval();
    }

    private void loadOrderForApproval(){
        Cursor data = mDatabaseHelper.getSalesOrderListForApproval();
        if(data.moveToFirst()){
            do{
                // int p_key = data.getInt(0);
                String orderDoc=data.getString(1);
                String erpDoc =data.getString(2);
                String grandTotal= data.getString(3);
                String preparedBy = data.getString(4);
                String salesType = data.getString(5);
                String sent_date = data.getString(6);
                ApprovalOrderModal orderListModal = new ApprovalOrderModal(orderDoc,erpDoc,grandTotal,preparedBy,salesType,sent_date);
                approvalOrderModals.add(orderListModal);
            }while(data.moveToNext());
        }
        if(approvalOrderModals == null || approvalOrderModals.isEmpty()){
            emptyListText.setText("No OrderList TO Approve For This User");
        }else{
            initRecyclerView();
        }

    }

    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        orderListRecyclerView.setLayoutManager(linearLayoutManager);
        ApprovalOrderListRecyclerViewAdapter adapter = new ApprovalOrderListRecyclerViewAdapter(approvalOrderModals,OrderListForApprovalActivity.this);
        orderListRecyclerView.setAdapter(adapter);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(OrderListForApprovalActivity.this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
}
