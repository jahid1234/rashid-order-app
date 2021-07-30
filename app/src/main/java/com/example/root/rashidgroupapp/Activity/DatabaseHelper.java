package com.example.root.rashidgroupapp.Activity;

/**
 * Created by root on 4/19/17.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2/28/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "product";
    private static final String COL1 = "productID";
    private static final String COL2 = "productName";
    private static final String COL3 = "catagoryID";
    private static final String COL4 = "price";
    private static final String COL5 = "unit";
    private static final String COL6 = "availableQty";


    private static final String DATABASE_NAME = "rashidgroup";




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 4);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createProductTable = "CREATE TABLE " + TABLE_NAME + " (productID TEXT Unique, " +
                COL2 +" TEXT, " +
                COL3 +" INTEGER, " +
                COL4 +" TEXT,"+COL5+" TEXT,"+COL6+" TEXT)";
        String createProductCatagoryTable="CREATE TABLE product_catagory(catagoryID INTEGER PRIMARY KEY," +
                "catagoryName TEXT)";

        String createSalesOrderTable="CREATE TABLE sales_order(docNo TEXT not null unique," +
                "date TEXT,chemist_code TEXT,status TEXT default 'draft',userid TEXT,delivary_date Text,description Text,promised_amount Text default '0',sales_type Text,billing_location Text)";

        String createSalesOrderLineTable="CREATE TABLE sales_orderline(orderline INTEGER PRIMARY KEY AUTOINCREMENT,docNo TEXT ," +
                "productID TEXT,quantity INTEGER,price Integer,product_name Text,unit_price Text,uom Text)";

        String createChemistProposalTable="CREATE TABLE chemist_proposal(chemistID INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "shopName TEXT,shopAddress TEXT,tradeLicenseNo TEXT,drugLicenseNo TEXT,contactPerson TEXT," +
                "mobile TEXT,email TEXT,nearbyHospital TEXT,estimatedDailySales TEXT,ownerName TEXT,ownerAddress TEXT)";

        String createDrTable="CREATE TABLE dr_information(dr_code TEXT ," +
                "drName TEXT,dr_address TEXT)";

        String createDrInfoTable="CREATE TABLE dr_proposal(ID INTEGER PRIMARY KEY AUTOINCREMENT,Name TEXT,qualification TEXT" +
                ",institution TEXT,specialisation TEXT,designation TEXT,Phone TEXT,EMail TEXT," +
                "mailingAddress TEXT,DOB TEXT,homeDistrict TEXT,spouseName TEXT,marriageDay TEXT,likings TEXT,dislikings TEXT,chamberAddress TEXT,practicingDay TEXT," +
                "MobileNumber TEXT,visitingHour TEXT,visitingTimeMPO TEXT,avgPatientNo_day TEXT,call_month TEXT,dr_status TEXT,productname TEXT)";

        String createEmployeeTable="CREATE TABLE employee(emp_id INTEGER PRIMARY KEY AUTOINCREMENT,user_id TEXT,ad_user_id TEXT,m_warehouse_id TEXT)";

        String createChemistTable="CREATE TABLE chemist(chemist_code TEXT,chemist_name TEXT,chemist_address TEXT,customer_balance TEXT,phone Text)";

        String createPrescriptionTable="CREATE TABLE prescription(dr_code TEXT,product_name TEXT,image_name TEXT)";

        String createDCRTable="CREATE TABLE dcr(code TEXT,promo_tools TEXT,work_station TEXT)";

        String createPromoToolsTable="CREATE TABLE promo_tools(name TEXT,description TEXT)";

        String CreatePromoDetailsTable="CREATE TABLE promo(promo_id INTEGER PRIMARY KEY AUTOINCREMENT,bpcode TEXT,bpname TEXT,product_code TEXT," +
                "product_name TEXT,m_locatorto_id TEXT,m_warehouse_id TEXT,ad_user_id TEXT,movementqty INTEGER)";

        String createPromoOrderTable="CREATE TABLE promo_order(docNo TEXT not null unique," +
                "date TEXT,dr_code TEXT,status TEXT default 'draft',userid TEXT)";

        String createPromoOrderLineTable="CREATE TABLE promo_orderline(promo_orderline INTEGER PRIMARY KEY AUTOINCREMENT,docNo TEXT ," +
                "productID TEXT,quantity INTEGER)";

        String createOrganizationTable = "Create Table organization (org_name Text,org_id Integer,branch_name Text,branch_id Integer)";

        String createSalesTypeTable = "Create Table salesType(salesType_id INTEGER PRIMARY KEY AUTOINCREMENT,salesType_name Text)";

        String createOrderApprovalTable = "Create Table orderApproval(orderApproval_id INTEGER PRIMARY KEY AUTOINCREMENT,order_doc Text,erp_orderNo Text,grand_total Text,prepared_by text,sales_type Text,sent_date Text)";

        String createSalesOrderRecord="CREATE TABLE sales_order_record(docNo TEXT," +
                "date TEXT,customer_name TEXT,grand_total Text,product_name Text,uom Text,ordered_qty Text default '0',pricePerProduct Text default '0')";


        db.execSQL(createProductTable);
        db.execSQL(createProductCatagoryTable);
        db.execSQL(createSalesOrderTable);
        db.execSQL(createSalesOrderLineTable);
        db.execSQL(createChemistProposalTable);
        db.execSQL(createDrTable);
        db.execSQL(createDrInfoTable);
        db.execSQL(createEmployeeTable);
        db.execSQL(createChemistTable);
        db.execSQL(createPrescriptionTable);
        db.execSQL(createDCRTable);
        db.execSQL(createPromoToolsTable);
        db.execSQL(CreatePromoDetailsTable);
        db.execSQL(createPromoOrderTable);
        db.execSQL(createPromoOrderLineTable);
        db.execSQL(createOrganizationTable);
        db.execSQL(createSalesTypeTable);
        db.execSQL(createOrderApprovalTable);
        db.execSQL(createSalesOrderRecord);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP  TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP  TABLE IF EXISTS  product_catagory");
        db.execSQL("DROP  TABLE IF EXISTS sales_order");
        db.execSQL("DROP  TABLE IF EXISTS sales_orderline");
        db.execSQL("DROP  TABLE IF EXISTS chemist_proposal");
        db.execSQL("DROP  TABLE IF EXISTS dr_information");
        db.execSQL("DROP  TABLE IF EXISTS dr_proposal");
        db.execSQL("DROP  TABLE IF EXISTS employee");
        db.execSQL("DROP  TABLE IF EXISTS chemist");
        db.execSQL("DROP  TABLE IF EXISTS prescription");
        db.execSQL("DROP  TABLE IF EXISTS dcr");
        db.execSQL("DROP  TABLE IF EXISTS promo_tools");
        db.execSQL("DROP  TABLE IF EXISTS promo");
        db.execSQL("DROP  TABLE IF EXISTS promo_order");
        db.execSQL("DROP  TABLE IF EXISTS promo_orderline");
        db.execSQL("DROP  TABLE IF EXISTS organization");
        db.execSQL("DROP  TABLE IF EXISTS salesType");
        db.execSQL("DROP  TABLE IF EXISTS orderApproval");
        db.execSQL("DROP  TABLE IF EXISTS sales_order_record");
        onCreate(db);
    }


    public boolean insertIntoOrderRecord(String docNo,String date,String customer_name,String grand_total,String product_name,String uom,String ordered_qty,String pricePerProduct) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("docNo", docNo);
        contentValues.put("date", date);
        contentValues.put("customer_name", customer_name);
        contentValues.put("grand_total", grand_total);
        contentValues.put("product_name", product_name);
        contentValues.put("uom", uom);
        contentValues.put("ordered_qty", ordered_qty);
        contentValues.put("pricePerProduct", pricePerProduct);

        long result = db.insert("sales_order_record",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntoOrderApproval(String order_doc,String erp_orderNo,String grand_total,String prepared_by,String sales_type,String sent_date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("order_doc", order_doc);
        contentValues.put("erp_orderNo", erp_orderNo);
        contentValues.put("grand_total", grand_total);
        contentValues.put("prepared_by", prepared_by);
        contentValues.put("sales_type", sales_type);
        contentValues.put("sent_date", sent_date);

        long result = db.insert("orderApproval",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean insertIntoSalesType(String typename) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("salesType_name", typename);

        long result = db.insert("salesType", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }



    public boolean insertIntoOrganization(String orgName,int orgId,String branchName,int branchId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("org_name", orgName);
        contentValues.put("org_id", orgId);
        contentValues.put("branch_name", branchName);
        contentValues.put("branch_id", branchId);

        long result = db.insert("organization", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }



    public boolean insertIntoEmployee(String user,String ad_user_id,String m_warehouse_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", user);
        contentValues.put("ad_user_id", ad_user_id);
        contentValues.put("m_warehouse_id", m_warehouse_id);



        long result = db.insert("employee", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntodrInfo(String dr_code,String name,String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("dr_code", dr_code);
        contentValues.put("drName", name);
        contentValues.put("dr_address", address);



        long result = db.insert("dr_information",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntoPromoTools(String name,String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("description", desc);



        long result = db.insert("promo_tools",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean insertIntoSalesOrderLine(String docNo,String productID,String quantity,double price,String productName,String unitPrice,String uom) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("docNo", docNo);
        contentValues.put("productID", productID);
        contentValues.put("quantity", quantity);
        contentValues.put("price", price);
        contentValues.put("product_name",productName);
        contentValues.put("unit_price",unitPrice);
        contentValues.put("uom",uom);
        long result = db.insert("sales_orderline", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }





    public boolean insertIntoSalesOrder(String docNo,String date,String chemID,String userID,String delivaryDate,String des,String amount,String salesType,String billingLocation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("docNo", docNo);
        contentValues.put("date", date);
        contentValues.put("chemist_code", chemID);
        contentValues.put("userid", userID);
        contentValues.put("delivary_date", delivaryDate);
        contentValues.put("description", des);
        contentValues.put("promised_amount", amount);
        contentValues.put("sales_type", salesType);
        contentValues.put("billing_location", billingLocation);

        long result = db.insert("sales_order", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntoPromoOrder(String docNo,String date,String drID,String userID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("docNo", docNo);
        contentValues.put("date", date);
        contentValues.put("dr_code", drID);
        contentValues.put("userid", userID);



        long result = db.insert("promo_order", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntoPromoOrderLine(String docNo,String productID,String quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("docNo", docNo);
        contentValues.put("productID", productID);
        contentValues.put("quantity", quantity);


        long result = db.insert("promo_orderline", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean insertIntodrProposal(List<String> stringList){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put("Name", stringList.get(0));
        contentValues.put("qualification", stringList.get(1));
        contentValues.put("institution", stringList.get(2));
        contentValues.put("specialisation", stringList.get(3));
        contentValues.put("designation", stringList.get(4));
        contentValues.put("Phone", stringList.get(5));
        contentValues.put("EMail", stringList.get(6));
        contentValues.put("mailingAddress", stringList.get(7));
        contentValues.put("DOB", stringList.get(8));
        contentValues.put("homeDistrict", stringList.get(9));
        contentValues.put("spouseName", stringList.get(10));
        contentValues.put("marriageDay", stringList.get(11));
        contentValues.put("likings", stringList.get(12));
        contentValues.put("dislikings", stringList.get(13));
        contentValues.put("chamberAddress", stringList.get(14));
        contentValues.put("practicingDay", stringList.get(15));
        contentValues.put("MobileNumber", stringList.get(16));
        contentValues.put("visitingHour", stringList.get(17));
        contentValues.put("visitingTimeMPO", stringList.get(18));
        contentValues.put("avgPatientNo_day", stringList.get(19));
        contentValues.put("call_month", stringList.get(20));
        contentValues.put("dr_status", stringList.get(21));
        contentValues.put("productname", stringList.get(22));


        long result = db.insert("dr_proposal", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean insertIntoProduct(String productID,String productName,int catagoryID,String price,String unit,String avlQty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("productID", productID);
        contentValues.put("productName", productName);
        contentValues.put("catagoryID", catagoryID);
        contentValues.put("price", price);
        contentValues.put("unit", unit);
        contentValues.put("availableQty", avlQty);
        long result = db.insert("product", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }




    public boolean insertIntoProductCatagory(int catagoryID,String catagoryName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("catagoryID", catagoryID);
        contentValues.put("catagoryName", catagoryName);



        long result = db.insert("product_catagory", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntoDCR(String code,String promotools,String workstation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("code", code);
        contentValues.put("promo_tools", promotools);
        contentValues.put("work_station", workstation);



        long result = db.insert("dcr", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean insertIntoPrescription(String dr_code,String product,String image_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("dr_code", dr_code);
        contentValues.put("product_name", product);
        contentValues.put("image_name", image_name);



        long result = db.insert("prescription", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean insertIntoChemist(String chemist_code,String chemist_name,String address,String balance,String phoneNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("chemist_code", chemist_code);
        contentValues.put("chemist_name", chemist_name);
        contentValues.put("chemist_address", address);
        contentValues.put("customer_balance", balance);
        contentValues.put("phone", phoneNo);

        long result = db.insert("chemist", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntoPromo(String bp_code,String bp_name,String pr_code,String pr_name,String m_locator_id,String m_warehouse_id,String ad_userid,String movementqty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("bpcode", bp_code);
        contentValues.put("bpname", bp_name);
        contentValues.put("product_code", pr_code);
        contentValues.put("product_name", pr_name);
        contentValues.put("m_locatorto_id", m_locator_id);
        contentValues.put("m_warehouse_id", m_warehouse_id);
        contentValues.put("ad_user_id", ad_userid);
        contentValues.put("movementqty", movementqty);



        long result = db.insert("promo", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }



    public boolean insertIntoChemistProposal(String chemistName,String shopAddress,String tradeLicenseNo,String drugLicenseNo,
                                     String contactPerson,String mobile,String email,String nearbyHospital,String estimatedDailySales,String ownerName,String ownerAddress)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("shopName", chemistName);
        contentValues.put("shopAddress", shopAddress);
        contentValues.put("tradeLicenseNo", tradeLicenseNo);
        contentValues.put("drugLicenseNo", drugLicenseNo);
        contentValues.put("contactPerson", contactPerson);
        contentValues.put("mobile", mobile);
        contentValues.put("email", email);
        contentValues.put("nearbyHospital", nearbyHospital);
        contentValues.put("estimatedDailySales", estimatedDailySales);
        contentValues.put("ownerName", ownerName);
        contentValues.put("ownerAddress", ownerAddress);



        long result = db.insert("chemist_proposal", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns all the data from database
     * @return
     */


    public List<String> searchProduct(String name){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT productName FROM product inner join product_catagory ON product.catagoryID=product_catagory.catagoryID where upper(product_catagory.catagoryName) = 'FINISHED GOODS' and  product.productName LIKE '"+name+"%' Order by productName";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public List<String> searchProductByCode(String code){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT productName FROM product  inner join product_catagory ON product.catagoryID=product_catagory.catagoryID where upper(product_catagory.catagoryName) = 'FINISHED GOODS' and product.productID LIKE '%"+code+"%'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }



    public List<String> searchCustomerByName(String name){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT chemist_name FROM chemist where chemist_name LIKE '"+name+"%' Order by chemist_name";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public Cursor searchCustomerByNameNew(String name) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM chemist  where chemist_name LIKE '%"+name+"%' Order by chemist_name";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor searchCustomerByCodeNew(String code) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM chemist  where chemist_code LIKE '%"+code+"%' Order by chemist_name";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public String getCustomerAddressByCode(String code) {

        SQLiteDatabase db = this.getWritableDatabase();
        String address="";
        String selectQuery = "SELECT chemist_address FROM chemist where chemist_code='"+code+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                address=(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return address;
    }

    public String getCustomerBalanceByCode(String code) {

        SQLiteDatabase db = this.getWritableDatabase();
        String balance="";
        String selectQuery = "SELECT customer_balance FROM chemist where chemist_code='"+code+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                balance=(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return balance;
    }

    public List<String> searchCustomerByCode(String code){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT chemist_name FROM chemist where chemist_code LIKE '%"+code+"%' Order by chemist_name";



        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public List<String> getAllProduct(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT productName FROM product inner join product_catagory ON product.catagoryID=product_catagory.catagoryID where upper(product_catagory.catagoryName) = 'FINISHED GOODS'  Order by productName";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public List<String> getAllProductCode(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT productID FROM product Order by productName";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }


    public List<String> getAllPromoTools(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT name FROM promo_tools Order by name";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public Cursor getSalesOrderLineData(String docNo){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT product_name,uom,ordered_qty,pricePerProduct,customer_name FROM sales_order_record where docNo='"+docNo+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getSalesOrderLineDataTemp(String docNo){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM sales_orderline where docNo='"+docNo+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getSalesOrderListForApproval(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM orderApproval";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getPromoOrderLineData(String docNo){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT productID,quantity FROM promo_orderline where docNo='"+docNo+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getSalesType(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT salesType_name from salesType where salesType_name != 'Customer Return Material'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public String getUserIDFromEmployee(){


        String name = null;

        // Select All Query
        String selectQuery = "select user_id from employee order by emp_id desc limit 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                name=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return name;
    }

    public List<String> getUserDetailsFromEmployee(){


        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "select ad_user_id,m_warehouse_id from employee order by emp_id desc limit 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public String getDrCodeByName(String name){


        String code = null;

        // Select All Query
        String selectQuery = "SELECT dr_code FROM dr_information where drName='"+name+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                code=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return code;
    }

    public String getProductName(String productID){


        String name = null;

        // Select All Query
        String selectQuery = "SELECT productName FROM product where productID='"+productID+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                name=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return name;
    }
    public String getProductNameFromPromo(String productID){


        String name = null;

        // Select All Query
        String selectQuery = "SELECT product_name FROM promo where product_code='"+productID+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                name=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return name;
    }


    public String getChemistName(String chemId){


        String name = null;

        // Select All Query
        String selectQuery = "SELECT chemist_name FROM chemist where chemist_code='"+chemId+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                name=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return name;
    }

    public String getDrNameByCode(String drID){


        String name = null;

        // Select All Query
        String selectQuery = "SELECT drName FROM dr_information where dr_code='"+drID+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                name=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return name;
    }

    public String getdrCodeByDocNo(String docNo){


        String name = null;

        // Select All Query
        String selectQuery = "SELECT dr_code FROM promo_order where docNo='"+docNo+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                name=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return name;
    }



    public List<String> getAllDrNames(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT drName FROM dr_information";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }



    public String getChemistID(String docNo){


        String id =null;

        // Select All Query
        String selectQuery = "SELECT chemist_code FROM sales_order where docNo='"+docNo+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                id=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return id;
    }

    public String getTotalPrice(String docNo){


        String total = null;
        Double total_price = 0.00 ;
        // Select All Query
        String selectQuery = "select ordered_qty,pricePerProduct from sales_order_record where docno='"+docNo+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String qty = cursor.getString(0);
                String price = cursor.getString(1);
                total_price = total_price +  Double.parseDouble(qty) * Double.parseDouble(price);
            } while (cursor.moveToNext());
            total= String.valueOf(total_price);
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return total;
    }

    public String getOrderedProduct(String docNo){


        String name1 = "";

        // Select All Query
        String selectQuery = "select product_name from sales_orderline where docno='"+docNo+"' order by product_name";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
               String name=(cursor.getString(0));
                name1 += name + "\n";
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return name1;
    }

    public String getOrderedProductQty(String docNo){


        String qty = "";

        // Select All Query
        String selectQuery = "select quantity from sales_orderline where docno='"+docNo+"' order by product_name";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
              String qty1=(cursor.getString(0));
                qty += qty1 + "\n";
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return qty;
    }


    public int getCatagoryID(String catagoryName){


        int id = 0;

        // Select All Query
        String selectQuery = "SELECT catagoryID FROM product_catagory where catagoryName='"+catagoryName+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                id=(cursor.getInt(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return id;
    }

    public String getProductIDByProductName(String productName){


        String id = null;

        // Select All Query
        String selectQuery = "SELECT productID FROM product where productName='"+productName+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                id=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return id;
    }


    public String getProductIDByProductNameFromPromo(String productName){


        String id = null;

        // Select All Query
        String selectQuery = "SELECT product_code FROM promo where product_name='"+productName+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                id=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return id;
    }

    public String getDrCodeByNameAddress(String name,String Address){


        String id = null;

        // Select All Query
        String selectQuery = "SELECT dr_code FROM dr_information where drName='"+name+"' and dr_address='"+Address+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                id=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return id;
    }
    public String getChemistCodeByName(String name,String Address){


        String id = null;

        // Select All Query
        String selectQuery = "SELECT chemist_code FROM chemist where chemist_name='"+name+"' and chemist_address='"+Address+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                id=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return id;
    }

    public String getChemistIDByName(String name){


        String id = null;

        // Select All Query
        String selectQuery = "SELECT chemist_code FROM chemist where chemist_name='"+name+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                id=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return id;
    }


    public String getProductQty(String productName){


        String avlQty=null;

        // Select All Query
        String selectQuery = "SELECT availableQty FROM product where productName='"+productName+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                avlQty=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return avlQty;
    }



    public String getProductPrice(String productName){


        String Price=null;

        // Select All Query
        String selectQuery = "SELECT price FROM product where productName='"+productName+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Price=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return Price;
    }


    public String getProductUnit(String productName){
        String unit=null;
        String selectQuery = "SELECT unit FROM product where productName='"+productName+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                unit=(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return unit;
    }

    public int getAvailableQty(String productCode){


        int qty=0;

        // Select All Query
        String selectQuery = "SELECT movementqty FROM promo where product_code='"+productCode+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                qty=(cursor.getInt(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return qty;
    }




    public List<String> getAllProductByCatagory(int catagoryID){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT productName FROM product where catagoryID="+catagoryID+" Order by productName";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }






    public List<String> getAllProductCatagory(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT catagoryName FROM product_catagory order by catagoryName";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }


    public boolean getSalesOrderLineByDocAndProduct(String docNo,String productID){

        boolean res=false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM sales_orderline where docNo='"+docNo+"' and productID='"+productID+"'";
        Cursor data = db.rawQuery(query, null);
        if (data.moveToNext()){

            res=true;
        }
        else{
            res=false;
        }
        return res;

    }

    public boolean getPromoOrderLineByDocAndProduct(String docNo,String productID){

        boolean res=false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM promo_orderline where docNo='"+docNo+"' and productID='"+productID+"'";
        Cursor data = db.rawQuery(query, null);
        if (data.moveToNext()){

            res=true;
        }
        else{
            res=false;
        }
        return res;

    }

    public Cursor getSalesOrderByDate(String date1,String date2){
        SQLiteDatabase db = this.getWritableDatabase();

       // String query = "SELECT * FROM sales_order where date='"+date+"' and status!='draft' and userid='"+userID+"'";

       // String query = "SELECT DISTINCT docNo,date,customer_name FROM sales_order_record where date between '"+date1+"' and '"+date+"'";
        String query = "SELECT DISTINCT docNo,date,customer_name FROM sales_order_record where date between '"+date1+"' and '"+date2+"' ";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public List<String> getAllChemists(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT chemist_name FROM chemist ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    /**
     * Returns only the ID that matches the name passed in
     * @param name
     * @return
     */



    public Cursor getItemID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL3 + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }





    public void updateName(String newProduct,String newItem,String newQuantity, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 + " = '" + newProduct + "',"+COL3+ "= '"+newItem+"', "+COL4+" = '"+newQuantity+ "' WHERE " + COL1 + " = " + id+"" ;

        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + newItem);
        db.execSQL(query);


    }

    public boolean updateOrderStatus(String docno){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues args = new ContentValues();
        args.put("status", "complete");

        int i = db.update("sales_order", args, "docNo = ?", new String[]{docno});
        if( i > 0){
            return true;
        }
        return false;

    }

    public boolean updatePromoQty(String product_code,int qty){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues args = new ContentValues();
        args.put("movementqty", qty);

        int i = db.update("promo", args, "product_code = ?", new String[]{product_code});
        if( i > 0){
            return true;
        }
        return false;

    }






    public void deleteName(int id, String item){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = " + id + "";


        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + item + " from database.");
        db.execSQL(query);
    }

    public void deleteSalesOrderLine(String id, String docNo){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  sales_orderline WHERE docNo='"+docNo+"' AND productID='"+id+"'";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deletePromoOrderLine(String id, String docNo){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  promo_orderline WHERE docNo='"+docNo+"' AND productID='"+id+"'";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deleteProducts(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  product";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deleteSalesType(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  salesType";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
    public void deleteProductCatagory(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  product_catagory";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
    public Cursor getSalesOrder(String docNo) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT chemist_code,date,delivary_date,description,promised_amount,sales_type,billing_location FROM sales_order where docNo='"+docNo+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getPromoOrder(String docNo) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT dr_code,date FROM promo_order where docNo='"+docNo+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }



    public Cursor getDrDetails() {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM dr_information order by drName ";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getPromoProduct(String ad_user_id,String ad_warehouse_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT product_code,product_name,movementqty FROM promo order by product_name ";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public Cursor getChemistDetails() {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM chemist order by chemist_name";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getOrganization() {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT DISTINCT org_name,org_id FROM organization";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getBranch() {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT branch_name,branch_id FROM organization where branch_name != 'TRANSIT WH' and branch_name != 'RAW MATERIALS WH'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public Cursor getSkuWiseStock(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select productName,availableQty,unit from product \n" +
                "inner join product_catagory ON product.catagoryID=product_catagory.catagoryID where upper(product_catagory.catagoryName) = 'FINISHED GOODS'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getTotalOrderPrice(String docNo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select sum(price) from sales_orderline where docNo = '" + docNo + "' ", null);
        return res;
    }

    public void deletechemist() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  chemist";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deletedr() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  dr_information";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deletePromoTools() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  promo_tools";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deletePromoTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  promo";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deleteOrganization() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  organization";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deleteEmployee() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  employee";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deleteOrderApprovalTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  orderApproval";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deleteOrderApprovalLine(String erp_doc) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  orderApproval where erp_orderNo = '"+erp_doc+"'  ";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deleteSalesOrder() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  sales_order ";
        db.execSQL(query);
    }

    public void deleteSalesOrderLine() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  sales_orderline";
        db.execSQL(query);
    }

    public void deleteSalesOrderRecord() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  sales_order_record";
        db.execSQL(query);
    }

    public void exec(String s) {

    }
}























