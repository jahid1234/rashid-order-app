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

import com.example.root.rashidgroupapp.Adapter.PartyBalanceRecyclerViewAdapter;
import com.example.root.rashidgroupapp.Modal.PartyBalanceModal;
import com.example.root.rashidgroupapp.R;

import java.util.ArrayList;

public class PartyBalanceActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    Toolbar toolbar;
    RecyclerView partyBalanceRecyclerView;
    ArrayList<PartyBalanceModal> balanceModalArrayList = new ArrayList<>();

    PartyBalanceRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_balance);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PartyBalanceActivity.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

        databaseHelper = new DatabaseHelper(this);
        partyBalanceRecyclerView = findViewById(R.id.party_balance_recyclerview);
        LoadCustomer();
    }

    public void LoadCustomer(){

        Cursor data = databaseHelper.getChemistDetails();
        while (data.moveToNext()) {

            String customerCode = data.getString(0);
            String customerName = data.getString(1);
            String chemistAddress = data.getString(2);
            String balance        = data.getString(3);

            PartyBalanceModal partyBalanceModal = new PartyBalanceModal(customerCode,customerName,balance);
            balanceModalArrayList.add(partyBalanceModal);
        }

        initRecyclerView();
    }

    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        partyBalanceRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new PartyBalanceRecyclerViewAdapter(balanceModalArrayList,PartyBalanceActivity.this);
        partyBalanceRecyclerView.setAdapter(adapter);
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
                    partyBalanceRecyclerView.removeAllViews();
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
        Intent intent = new Intent(PartyBalanceActivity.this,Home.class);
        startActivity(intent);
    }
}
