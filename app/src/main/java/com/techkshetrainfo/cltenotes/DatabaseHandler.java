package com.techkshetrainfo.cltenotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by savsoft-3 on 22/12/16.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "emagazine";
    private static final String TABLE_MAGAZINE = "contacts";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_Magazine_TABLE = "CREATE TABLE `magazine` (\n" +
                "\t`mid`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`title`\tTEXT,\n" +
                "\t`description`\tTEXT,\n" +
                "\t`cover_photo`\tTEXT,\n" +
                "\t`month`\tTEXT,\n" +
                "\t`year`\tTEXT,\n" +
                "\t`views`\tINTEGER DEFAULT 0,\n" +
                "\t`date`\tINTEGER,\n" +
                "\t`published`\tINTEGER,\n" +
                "\t`magazine_type`\tTEXT,\n" +
                "\t`amount`\tTEXT,\n" +
                "\t`avg_rating`\tTEXT,\n" +
                "\t`five_rating`\tINTEGER,\n" +
                "\t`four_rating`\tINTEGER,\n" +
                "\t`three_rating`\tINTEGER,\n" +
                "\t`two_rating`\tINTEGER,\n" +
                "\t`one_rating`\tINTEGER\n" +
                ")";

        String CRWATE_MAGAZINE_PAGES_TABLE= "CREATE TABLE `magazine_pages` (\n" +
                "\t`pid`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`mid`\tINTEGER,\n" +
                "\t`page_number`\tINTEGER,\n" +
                "\t`page_description`\tTEXT,\n" +
                "\t`page_image`\tTEXT\n" +
                ");";


        String PAYMENT_TABLE="CREATE TABLE `payment` (\n" +
                "\t`pid`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`mid`\tint(11) ,\n" +
                "\t`amount`\tfloat ,\n" +
                "\t`paid_date`\tint(11),\n" +
                "\t`payment_gateway`\tvarchar(100)DEFAULT 'Payumoney',\n" +
                "\t`payment_status`\tvarchar(100) DEFAULT 'Pending',\n" +
                "\t`transaction_id`\tvarchar(1000) ,\n" +
                "\t`name`\tvarchar(1000) NOT NULL,\n" +
                "\t`device_id`\tvarchar(100),\n" +
                "\t`email`\tvarchar(100)\n" +
                ");";





        db.execSQL(CREATE_Magazine_TABLE);
        db.execSQL(CRWATE_MAGAZINE_PAGES_TABLE);
        db.execSQL(PAYMENT_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

   // code to add the new contact
   public void addMagazineCover(int mid,String title,String description,String cover_photo,String month,String year,int views,int date,int publish,String mag_type,String amount,String avg_rating,int fivestar,int fourstar,int threestar,int twostar,int onestar ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("mid", mid);
        values.put("title", title);
        values.put("description", description);
        values.put("cover_photo", cover_photo);
        values.put("month", month);
        values.put("year", year);
        values.put("views", views);
        values.put("date", date);
        values.put("published", publish);
        values.put("magazine_type", mag_type);
        values.put("amount", amount);
        values.put("avg_rating", avg_rating);
        values.put("five_rating", fivestar);
        values.put("four_rating", fourstar);
        values.put("three_rating", threestar);
        values.put("two_rating", twostar);
        values.put("one_rating", onestar);



        // Inserting Row


        try{
             db.insert("magazine", null, values);
        }
        catch (SQLException ex){
            Log.e("error",ex.toString()) ;
        }

        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public void clear_data(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("magazine",null,null);
        db.delete("magazine_pages",null,null);
        db.execSQL("VACUUM");
        db.close();
    }
    public void clear_paymet(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("payment",null,null);
        db.execSQL("VACUUM");
        db.close();
    }

    public void addMagazinepages(int pid,int mid,int page_number,String description,String page_image ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("pid", pid);
        values.put("mid", mid);
        values.put("page_number", page_number);
        values.put("page_description", description);
        values.put("page_image", page_image);

        // Inserting Row


        try{
            db.insert("magazine_pages", null, values);
        }
        catch (SQLException ex){
            Log.e("error",ex.toString()) ;
        }

        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public void payment_data(int pid,int mid,float amount,int paid_date,String name,String payment_gateway,String payment_status,String transaction_id,String device_id,String email ) {
        SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
        values.put("pid", pid);
        values.put("mid", mid);
        values.put("amount", amount);
        values.put("paid_date", paid_date);
        values.put("payment_gateway", payment_gateway);
        values.put("name", name);
        values.put("payment_status", payment_status);
        values.put("transaction_id", transaction_id);
        values.put("device_id", device_id);
        values.put("email", email);

        // Inserting Row


        try{
            db.insert("payment", null, values);
        }
        catch (SQLException ex){
            Log.e("error",ex.toString()) ;
        }

        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + "magazine";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);


        // return count
        return cursor.getCount();
    }


    public Cursor getmagazine(int year){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] search_year = new String[]{String.valueOf(year)};
       // db.close();
        return db.rawQuery("select * from  magazine where year = ? order by month  DESC ",search_year);
        //return myDataBase.execSQL("dict",column, null ,null , null , null ,null);

    }
    public Cursor getmagazinepages(String mid){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] search_magazine = new String[]{String.valueOf(mid)};
        // db.close();
        return db.rawQuery("select * from  magazine_pages where mid = ?  order by page_number  ASC ",search_magazine);
        //return myDataBase.execSQL("dict",column, null ,null , null , null ,null);

    }

    public Cursor getpaymnetdetail_loggedin(String user_email, String mid, String device_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] search_magazine = new String[]{user_email,String.valueOf(mid)};
        // db.close();
        return db.rawQuery("select * from  payment where email = ? and mid = ?",search_magazine);

    }

    public Cursor getmagazine_years() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select Distinct year from magazine order by year asc",null);

    }


}

