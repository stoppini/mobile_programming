package com.example.myapplication_test_db.model;

import android.content.Context;
import android.content.PeriodicSync;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.example.myapplication_test_db.utils.MagicDB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private MagicDBHelper dbHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    private String[] allProductCol = {MagicDB.Pruduct_db.COLUMN_ID, MagicDB.Pruduct_db.TABLE_NAME,
            MagicDB.Pruduct_db.COLUMN_EXPANSION, MagicDB.Pruduct_db.COLUMN_RARITY,
            MagicDB.Pruduct_db.COLUMN_TYPE, MagicDB.Pruduct_db.COLUMN_IMG};

    private DatabaseAccess (Context context) throws IOException {
        this.dbHelper = new MagicDBHelper(context);
        dbHelper.importDataBaseFromAssets();
    }

    public static DatabaseAccess getInstance(Context context) throws IOException {
        if(instance == null){
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void open(){
        this.db = dbHelper.getWritableDatabase();
    }

    public void close(){
        if(db != null){
            this.db.close();
        }
    }

    public List<Product> getAllProduct() {
        open();
        List<Product> products = new ArrayList<>();
        Log.println(Log.DEBUG,"DB",db.toString());
        Cursor c = db.rawQuery("SELECT * from product",null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            Product p = cursorToProdut(c);
            Log.e("BOOOOOOOOOOOO","PIANGO "+p.name);
            products.add(p);
            c.moveToNext();
        }
        c.close();
        close();
        return products;
    }

    private Product cursorToProdut(Cursor c) {
        long id = c.getLong(0);
        String name = c.getString(1);
        String expansion = c.getString(2);
        String rarity = c.getString(3);
        String type = c.getString(4);
        String img = c.getString(5);
        return Product.create(id, name, expansion, rarity, type,img);
    }


}
