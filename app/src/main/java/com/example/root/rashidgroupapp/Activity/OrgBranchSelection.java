package com.example.root.rashidgroupapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.root.rashidgroupapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrgBranchSelection extends AppCompatActivity {

    Button selectButton,cancelButton;
    Spinner orgSpinner,branchSpinner;
    DatabaseHelper databaseHelper;

    List<String> orgNameArrayList = new ArrayList<String>();
    List<String> branchNameArrayList = new ArrayList<String>();

    HashMap<String,Integer> hashOrgId  = new HashMap<>();
    HashMap<String,Integer> hashBranchId  = new HashMap<>();

    ProgressDialog pd;

    public static int orgId = 0, branchId = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_branch_selection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrgBranchSelection.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        selectButton  = findViewById(R.id.orgSelect_button);
        cancelButton  = findViewById(R.id.cancel_button);
        orgSpinner    = findViewById(R.id.org_spinner);
        branchSpinner = findViewById(R.id.branch_spinner);

        databaseHelper = new DatabaseHelper(this);

        AsyncLoadSpinner task = new AsyncLoadSpinner();
        task.execute();
        selectButtonClicked();
    }




    private final class AsyncLoadSpinner extends AsyncTask<String,String,String> {



        String result;
        @Override
        protected String doInBackground(String... strings) {

            // getDealerCode();
            loadOrganization();
            // getOutletFromServer();
            result = "Success";
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(OrgBranchSelection.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading ....");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressDialog();
            if(s.equals("Success")){
                loadOrgSpinner();
                //loadBranchSpinner();
                loadBranchSpinner();
               // selectBranchSpinner();
            }else{
                toastMessage(s);
            }
        }
    }

    private void loadOrgSpinner(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,orgNameArrayList);


        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        orgSpinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
    }

    private void selectOrgSpinner(){
        orgSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String selectedOrg = parent.getSelectedItem().toString();
                    orgId = hashOrgId.get(selectedOrg);

                }catch (NullPointerException ex){

                }catch (ArrayIndexOutOfBoundsException ex){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void loadBranchSpinner(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, branchNameArrayList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        branchSpinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();

    }

    private void selectBranchSpinner(){
        branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String selectedBranch = parent.getSelectedItem().toString();
                    branchId = hashBranchId.get(selectedBranch);
                }catch (NullPointerException ex){

                }catch (ArrayIndexOutOfBoundsException ex){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void toastMessage(String msg){
        Toast.makeText(OrgBranchSelection.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void dismissProgressDialog(){
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    private void loadOrganization(){
        Cursor cursor = databaseHelper.getOrganization();
        Cursor cursor1 = databaseHelper.getBranch();
        orgNameArrayList.add("Organization");
        //branchNameArrayList.add("Branch");
        if(cursor.moveToFirst()){
            do{
                String orgName     = cursor.getString(0);
                int orgId          = cursor.getInt(1);
                orgNameArrayList.add(orgName);
                hashOrgId.put(orgName,orgId);
            }while(cursor.moveToNext());
        }

        if(cursor1.moveToFirst()){
            do{
                String branchName  = cursor1.getString(0);
                int branchId       = cursor1.getInt(1);
              //  if(!branchName.equals("TRANSIT WH") && !branchName.equals("RAW MATERIALS WH")) {
                if(branchName.equals("FINISHED GOODS WH")) {
                    branchNameArrayList.add(branchName);
                    hashBranchId.put(branchName, branchId);
                }
              //  }

            }while(cursor1.moveToNext());
        }

    }

    private void selectButtonClicked(){
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  selectOrgSpinner();
              //  selectBranchSpinner();
                String selectedOrg = orgSpinner.getSelectedItem().toString();
                if(!selectedOrg.equals("Organization")) {
                    orgId = hashOrgId.get(selectedOrg);
                }
                String selectedBranch = branchSpinner.getSelectedItem().toString();
                if(!selectedBranch.equals("Branch")) {
                    branchId = hashBranchId.get(selectedBranch);
                }

                if(selectedOrg.equals("Organization")){
                    toastMessage("Please select Organization");
                    return;
                }
//                if(selectedBranch.equals("Branch")){
//                    toastMessage("Please select Branch");
//                    return;
//                }

                Intent intent = new Intent(OrgBranchSelection.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrgBranchSelection.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
