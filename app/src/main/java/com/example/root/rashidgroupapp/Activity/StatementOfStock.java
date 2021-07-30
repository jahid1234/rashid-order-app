package com.example.root.rashidgroupapp.Activity;

import android.content.Intent;
import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.root.rashidgroupapp.Adapter.SkuStatementRecyclerViewAdapter;
import com.example.root.rashidgroupapp.Modal.SkuStatementModal;
import com.example.root.rashidgroupapp.R;

import java.util.ArrayList;

public class StatementOfStock extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView statementRecyclerView;
    DatabaseHelper databaseHelper;
    ArrayList<SkuStatementModal> statementOfStockArrayList = new ArrayList<>();
    SkuStatementRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement_of_stock);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatementOfStock.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

        databaseHelper = new DatabaseHelper(this);
        statementRecyclerView = findViewById(R.id.statemrnt_sku_recyclerview);
        loadStatement();
    }

    private void loadStatement(){
        Cursor cursor = databaseHelper.getSkuWiseStock();
        if(cursor.moveToFirst()){
            do{

                String productName = cursor.getString(0);
                String quantity    = cursor.getString(1);
                String unit        = cursor.getString(2);

                SkuStatementModal skuStatementModal = new SkuStatementModal(productName,quantity,unit);
                statementOfStockArrayList.add(skuStatementModal);
            }while(cursor.moveToNext());
        }
        initRecyclerView();
    }

    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        statementRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new SkuStatementRecyclerViewAdapter(statementOfStockArrayList,StatementOfStock.this);
        statementRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu,menu);
        MenuItem mySearch = menu.findItem(R.id.search_view);

        SearchView searchView = (SearchView) mySearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(TextUtils.isEmpty(newText)){
                    adapter.searchFilter("");
                    statementRecyclerView.removeAllViews();
                    initRecyclerView();
                }else{
                    adapter.searchFilter(newText);
                }
                return true;
            }
        });

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(StatementOfStock.this,Home.class);
        startActivity(intent);
    }
}
