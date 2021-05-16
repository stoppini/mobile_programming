package com.example.MagicShop.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.MagicShop.utils.MagicDB;

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

    public List<ProductOnSale> getAllProductsOnSale() {
        open();
        List<ProductOnSale> products_on_sale = new ArrayList<>();
        Log.println(Log.DEBUG,"DB",db.toString());
        Cursor c = db.rawQuery("SELECT * from product_on_sale",null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            ProductOnSale p = cursorToProdutOnSale(c);
            products_on_sale.add(p);
            c.moveToNext();
        }
        c.close();
        close();
        return products_on_sale;
    }

    public List<Product> getAllProducts() {
        open();
        List<Product> products = new ArrayList<>();
        Log.println(Log.DEBUG,"DB",db.toString());
        Cursor c = db.rawQuery("SELECT * from product",null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            Product p = cursorToProdut(c);
            products.add(p);
            c.moveToNext();
        }
        c.close();
        close();
        return products;
    }

    public List<ProductOnSale> getAllProductOnSaleFromProduct(Product product){
        open();
        List<ProductOnSale> products_on_sale = new ArrayList<>();
        Log.println(Log.DEBUG,"DB",db.toString());
        String query = String.format("SELECT product_on_sale.id, product_on_sale.product_id, " +
                        "product_on_sale.user_id, product_on_sale.price FROM product_on_sale " +
                        "INNER JOIN product ON product_on_sale.product_id = product.id " +
                        "WHERE product.id = %d", product.getId());
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            ProductOnSale p = cursorToProdutOnSale(c);
            products_on_sale.add(p);
            c.moveToNext();
        }
        c.close();
        close();
        return products_on_sale;
    }

    //TODO serve fixare l'user
    /*
    public List<ProductOnSale> getAllProductOnSaleFromUser(User user){
        open();
        List<ProductOnSale> products_on_sale = new ArrayList<>();
        Log.println(Log.DEBUG,"DB",db.toString());
        String query = String.format("SELECT product_on_sale.id, product_on_sale.product_id, " +
                "product_on_sale.user_id, product_on_sale.price FROM product_on_sale " +
                "INNER JOIN user ON product_on_sale.product_id = user.id " +
                "WHERE product.id = %d", user.getId());
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            ProductOnSale p = cursorToProdutOnSale(c);
            products_on_sale.add(p);
            c.moveToNext();
        }
        c.close();
        close();
        return products_on_sale;
    }
    */

    public Product getProdutFromProductOnSale(ProductOnSale productOnSale){
        open();
        String query = String.format("SELECT product.id, product.name, product.expansion, " +
                        "product.rarity, product.type, product.img FROM product INNER JOIN " +
                        "product_on_sale ON product_on_sale.product_id = product.id WHERE " +
                        "product_on_sale.id = %d", productOnSale.getId());
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        Product product = cursorToProdut(c);
        c.close();
        close();
        return product;
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

    private ProductOnSale cursorToProdutOnSale(Cursor c) {
        long id = c.getLong(0);
        long product_id = c.getLong(1);
        long user_id = c.getLong(2);
        long price = c.getLong(3);
        return ProductOnSale.create(id, product_id, user_id, price);
    }


}
