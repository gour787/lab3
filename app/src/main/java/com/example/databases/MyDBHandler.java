package com.example.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "products";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PRODUCT_NAME = "name";
    private static final String COLUMN_PRODUCT_PRICE = "price";
    private static final String DATABASE_NAME = "products.db";
    private static final int DATABASE_VERSION = 1;

    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table_cmd = "CREATE TABLE " + TABLE_NAME +
                "(" + COLUMN_ID + "INTEGER PRIMARY KEY, " +
                COLUMN_PRODUCT_NAME + " TEXT, " +
                COLUMN_PRODUCT_PRICE + " DOUBLE " + ")";

        db.execSQL(create_table_cmd);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        return db.rawQuery(query, null); // returns "cursor" all products from the table
    }


    public void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_PRODUCT_NAME, product.getProductName());
        values.put(COLUMN_PRODUCT_PRICE, product.getProductPrice());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void deleteProduct(String product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, product);
        db.delete(TABLE_NAME, "name=?", new String[]{product});
        db.close();

    }

    public ArrayList<String> findList(String names) {
        ArrayList<String> findList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE name LIKE ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{names});
        if (cursor.getCount() == 0) {
            // do nothing
        } else {
            while (cursor.moveToNext()) {
                findList.add(cursor.getString(1) + " (" + cursor.getString(2) + ")");
            }

        }
        cursor.close();
        db.close();
        return findList;
    }

    public ArrayList<String> findList(double prices) {
        String priced=Double.toString(prices);
        ArrayList<String> findList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE price LIKE ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{priced});
        if (cursor.getCount() == 0) {
            // do nothing
        } else {
            while (cursor.moveToNext()) {
                findList.add(cursor.getString(1) + " (" + cursor.getString(2) + ")");
            }

        }
        cursor.close();
        db.close();
        return findList;
    }

    public ArrayList<String> findList(String names, double prices) {
        String priced=Double.toString(prices);
        ArrayList<String> findList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE price=? AND name LIKE ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{priced,names});
        if (cursor.getCount() == 0) {
            // do nothing
        } else {
            while (cursor.moveToNext()) {
                findList.add(cursor.getString(1) + " (" + cursor.getString(2) + ")");
            }

        }
        cursor.close();
        db.close();
        return findList;
    }


}