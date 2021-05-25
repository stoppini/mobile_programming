package com.example.MagicShop.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.MagicShop.utils.MagicDB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DatabaseAccess {
    private MagicDBHelper dbHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    private String[] allProductCol = {MagicDB.Product_db.COLUMN_ID, MagicDB.Product_db.TABLE_NAME,
            MagicDB.Product_db.COLUMN_EXPANSION, MagicDB.Product_db.COLUMN_RARITY,
            MagicDB.Product_db.COLUMN_TYPE, MagicDB.Product_db.COLUMN_IMG};

    private static String KEY_USERNAME = "username";
    private static String KEY_PASSWORD = "password";
    private static String KEY_EMAIL = "email";
    private static String KEY_LOCATION = "city";
    private static String KEY_ADDRESS= "address";
    private static String KEY_CAP= "cap";

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
            ProductOnSale p = cursorToProductOnSale(c);
            products_on_sale.add(p);
            c.moveToNext();
        }
        c.close();
        close();
        return products_on_sale;
    }


    public List<ProductOnSale> getAllProductOnSaleFromProduct(Product product){
        open();
        List<ProductOnSale> products_on_sale = new ArrayList<>();
        Log.println(Log.DEBUG,"DB",db.toString());
        String query = String.format("SELECT product_on_sale.id, product_on_sale.product_id, " +
                "product_on_sale.user_id, product_on_sale.price FROM product INNER JOIN " +
                "product_on_sale ON product_on_sale.product_id = product.id INNER JOIN " +
                "language_%s ON language_%s.product_id = product.id WHERE product.id = %d",
                Locale.getDefault().getLanguage(), Locale.getDefault().getLanguage(), product.getId());
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            ProductOnSale p = cursorToProductOnSale(c);
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
        String query = String.format("SELECT product.id, language_%1$s.name, " +
                "language_%1$s.expansion, language_%1$s.rarity, language_%1$s.type, " +
                "language_%1$s.rule, product.img FROM product INNER JOIN language_%1$s ON " +
                "language_%1$s.product_id = product.id", Locale.getDefault().getLanguage());
        Log.e("QUERY", ""+query);

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            Product p = cursorToProduct(c);
            products.add(p);
            c.moveToNext();
        }
        c.close();
        close();
        return products;
    }


    public Product getProductFromId(Long id) {
        open();
        Log.println(Log.DEBUG,"DB",db.toString());
        String query = String.format("SELECT product.id, language_%1$s.name, " +
                "language_%1$s.expansion, language_%1$s.rarity, language_%1$s.type, " +
                "language_%1$s.rule, product.img FROM product INNER JOIN language_%1$s ON " +
                "language_%1$s.product_id = product.id WHERE product.id = %2$d",
                Locale.getDefault().getLanguage(), id);
        Log.e("QUERY", ""+query);

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        Product p = cursorToProduct(c);
        c.close();
        close();
        return p;
    }


    public List<Product> getSearchProducts(final String name, final String expansion,
                                           final String rarity, final String type){
        String mExpansion = expansion;
        String mRarity = rarity;
        String mType = type;
        open();
        List<Product> products = new ArrayList<>();
        Log.println(Log.DEBUG,"DB","NEL METODO");
        if (mExpansion.equals("All")) {
            mExpansion = "";
        }
        if (mRarity.equals("All")) {
            mRarity = "";
        }
        if (mType.equals("All")) {
            mType = "";
        }

        String query = String.format("SELECT product.id, language_%1$s.name, " +
                        "language_%1$s.expansion, language_%1$s.rarity, language_%1$s.type, " +
                        "language_%1$s.rule, product.img FROM product INNER JOIN language_%1$s ON " +
                        "language_%1$s.product_id = product.id WHERE language_%1$s.name LIKE '%2$s' " +
                        "AND language_%1$s.expansion LIKE '%3$s' AND language_%1$s.rarity LIKE '%4$s' " +
                        "AND language_%1$s.type LIKE '%5$s' ", Locale.getDefault().getLanguage(),
                        name.concat("%"), mExpansion.concat("%"), mRarity.concat("%"), mType.concat("%"));
        Log.println(Log.DEBUG,"QUERY", ""+query);
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            Product p = cursorToProduct(c);
            Log.println(Log.DEBUG,"PRODUCT",p.getName());
            products.add(p);
            c.moveToNext();
        }
        c.close();
        close();
        return products;
    }


    public Product getProductFromProductOnSale(ProductOnSale productOnSale){
        open();
        String query = String.format("SELECT product.id, product.name, product.expansion, " +
                "product.rarity, product.type, product.img FROM product INNER JOIN " +
                "product_on_sale ON product_on_sale.product_id = product.id WHERE " +
                "product_on_sale.id = %d", productOnSale.getId());
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        Product product = cursorToProduct(c);
        c.close();
        close();
        return product;
    }


    public List<String> getAllExpansion(){
        open();
        List<String> expansions = new ArrayList<>();
        Log.println(Log.DEBUG,"DB",db.toString());
        String query = String.format("SELECT DISTINCT language_%1$s.expansion FROM language_%1$s",
                Locale.getDefault().getLanguage());

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        expansions.add("All");
        while (!c.isAfterLast()){
            expansions.add(c.getString(0));
            c.moveToNext();
        }
        c.close();
        close();
        return expansions;
    }


    public List<String> getAllTypes(){
        open();
        List<String> types = new ArrayList<>();
        Log.println(Log.DEBUG,"DB",db.toString());
        String query = String.format("SELECT DISTINCT language_%1$s.type FROM language_%1$s",
                Locale.getDefault().getLanguage());
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        types.add("All");
        while (!c.isAfterLast()){
            types.add(c.getString(0));
            c.moveToNext();
        }
        c.close();
        close();
        return types;
    }


    public List<String> getAllRaritys(){
        open();
        List<String> raritys = new ArrayList<>();
        Log.println(Log.DEBUG,"DB",db.toString());
        String query = String.format("SELECT DISTINCT language_%1$s.rarity FROM language_%1$s",
                Locale.getDefault().getLanguage());
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        raritys.add("All");
        while (!c.isAfterLast()){
            raritys.add(c.getString(0));
            c.moveToNext();
        }
        c.close();
        close();
        return raritys;
    }

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

    public void registerUser(User user) {
        open();
        String query = String.format("INSERT INTO user (username, password, email, city, " +
                        "address, cap) VALUES ('%s','%s','%s','%s','%s','%d')", user.getUsername(),
                user.getPassword(), user.getEmail(), user.getLocation(), user.getAddress(), user.getCap());

        // da fixare
        ContentValues contentValues=new ContentValues();
        contentValues.put(KEY_USERNAME, user.getUsername());
        contentValues.put(KEY_PASSWORD, user.getPassword());
        contentValues.put(KEY_EMAIL, user.getEmail());
        contentValues.put(KEY_LOCATION, user.getLocation());
        contentValues.put(KEY_ADDRESS, user.getAddress());
        contentValues.put(KEY_CAP, user.getCap());
        db.insert("user",null, contentValues);
        Cursor c = db.rawQuery(query, null);
        c.close();
        close();
    }

    //TODO necessiterà un fix
    public User getUsersFromProductOnSale(ProductOnSale p) {
        open();
        List<User> users = new ArrayList<>();
        Log.e("ID", ""+p.getId());
        String query = String.format("SELECT user.username FROM user INNER JOIN product_on_sale ON " +
                        "product_on_sale.user_id = user.id WHERE product_on_sale.id = %d", p.getId());
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        User u = cursorToUser(c);
        c.close();
        close();
        Log.e("nane", ""+u.getUsername());
        return u;
    }

    public boolean logInUser(String username, String password) {
        open();
        boolean login=true;
        String query = String.format("SELECT EXISTS (SELECT * FROM user WHERE username = ? AND password = ?)");
        Cursor c = db.rawQuery(query, null);
        // Log.d("DEBUG", "query result: " + c);
        if(c.isNull(0)){
            login=false;
        }
        c.close();
        close();
        return login;
    }


    public User getUser(String username, String password){
        open();
        String query = String.format("SELECT * FROM user WHERE username = ? AND password = ?");
        Cursor c = db.rawQuery(query, null);
        // c.moveToFirst();
        User user = cursorToUser(c);
        return user;
    }


    public List<User> getAllUsers() {
        open();
        List<User> users = new ArrayList<>();
        Log.println(Log.DEBUG,"DB",db.toString());
        Cursor c = db.rawQuery("SELECT * from user",null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            User p = cursorToUser(c);
            users.add(p);
            c.moveToNext();
        }
        c.close();
        close();
        return users;
    }

    private Product cursorToProduct(Cursor c) {
        long id = c.getLong(0);
        String name = c.getString(1);
        String expansion = c.getString(2);
        String rarity = c.getString(3);
        String type = c.getString(4);
        String rule = c.getString(5);
        String img = c.getString(6);
        return Product.create(id, name, expansion, rarity, type, rule, img);
    }

    private ProductOnSale cursorToProductOnSale(Cursor c) {
        long id = c.getLong(0);
        long product_id = c.getLong(1);
        long user_id = c.getLong(2);
        long price = c.getLong(3);
        return ProductOnSale.create(id, product_id, user_id, price);
    }

//    private User cursorToUser(Cursor c) {
//        String name = c.getString(0);
//        return User.create().withUsername(name);
//    }

    private User cursorToUser(Cursor c){
        long id = c.getLong(0);
        String username = c.getString(1);
        String password = c.getString(2);
        String email = c.getString(3);
        String location = c.getString(4);
        String address = c.getString(5);
        long cap = c.getLong(6);
        return User.create().withUsername(username).
                withEmail(email).withLocation(location).withAddress(address).withCap(cap);
    }

}
