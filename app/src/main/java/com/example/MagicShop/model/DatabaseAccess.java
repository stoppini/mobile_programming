package com.example.MagicShop.model;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Base64;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;


public class DatabaseAccess {

    private static DatabaseAccess db = null;

    private DatabaseAccess() {
    }

    public static synchronized DatabaseAccess getDb() {
        if (db == null) {
            db = new DatabaseAccess();
        }
        return db;
    }

    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();


    public List<ProductOnSale> getAllProductsOnSale() {
        DatabaseReference ref = database.child("product_on_sale");
        List<ProductOnSale> products_on_sale = new ArrayList<>();
        Task<DataSnapshot> snap = ref.get();
        while (!snap.isComplete()) {
        }

        for (DataSnapshot s : snap.getResult().getChildren()) {
            ProductOnSale p = new ProductOnSale("" + s.getKey(),
                    (Long) s.child("product_id").getValue(),
                    "" + s.child("user_id").getValue(),
                    (Long) s.child("price").getValue(), ""+s.child("photo"));
            products_on_sale.add(p);

        }
        return products_on_sale;
    }


    public List<Product> getAllProducts() {
        DatabaseReference product = database.child("product");
        List<Product> prod = new ArrayList<>();
        Task<DataSnapshot> snap = product.get();
        while (!snap.isComplete()) {
        }

        DatabaseReference language = database.child("language_" + Locale.getDefault().getLanguage());
        Task<DataSnapshot> lan = language.get();
        while (!lan.isComplete()) {
        }
        ;
        for (DataSnapshot s : lan.getResult().getChildren()) {
            long id = (Long) s.child("product_id").getValue();
            String name = "" + s.child("name").getValue();
            String expansion = "" + s.child("name").getValue();
            String rarity = "" + s.child("rarity").getValue();
            String type = "" + s.child("type").getValue();
            String rule = "" + s.child("rule").getValue();
            String img = "" + snap.getResult().child("" + id).child("img").getValue();
            Product p = Product.create(id, name, expansion, rarity, type, rule, img);
            prod.add(p);
        }
        return prod;
    }


    public List<ProductOnSale> getAllProductOnSaleFromProduct(Product product) {
        List<ProductOnSale> products_on_sale = new ArrayList<>();
        Task<DataSnapshot> ref_prod = database.child("product").get();
        while (!ref_prod.isComplete()) {
        }

        Task<DataSnapshot> ref_prod_on_sale = database.child("product_on_sale").get();
        while (!ref_prod_on_sale.isComplete()) {
        }
        ;
        for (DataSnapshot s : ref_prod_on_sale.getResult().getChildren()) {

            if (("" + s.child("product_id").getValue()).equals("" + product.getId())) {
                String id = s.getKey();
                Long price = (Long) s.child("price").getValue();
                long prod_id = (Long) s.child("product_id").getValue();
                String user_id = "" + s.child("user_id").getValue();
                String photo = "" + s.child("photo").getValue();

                ProductOnSale p = ProductOnSale.create(id, prod_id, user_id, price,photo);
                products_on_sale.add(p);

            }
        }
        return products_on_sale;
    }


    public User getUsersFromProductOnSale(ProductOnSale p) {
        DatabaseReference ref = database.child("user");
        Task<DataSnapshot> snap = ref.get();
        while (!snap.isComplete()) {
        }
        for (DataSnapshot s : snap.getResult().getChildren()) {
            if (s.getKey().equals(p.getUser_id())) {
                User u = User.create().withUsername("" + s.child("username").getValue()).withId(s.getKey());
                return u;
            }
        }
        return null;
    }


    public Product getProductFromId(Long id) {
        DatabaseReference product = database.child("product");
        List<Product> prod = new ArrayList<>();
        Task<DataSnapshot> snap = product.get();
        while (!snap.isComplete()) {
        }

        DatabaseReference language = database.child("language_" + Locale.getDefault().getLanguage());
        Task<DataSnapshot> lan = language.get();
        while (!lan.isComplete()) {
        }
        ;
        for (DataSnapshot s : lan.getResult().getChildren()) {
            if (Long.parseLong("" + s.child("product_id").getValue()) == (id)) {
                long idd = (Long) s.child("product_id").getValue();
                String name = "" + s.child("name").getValue();
                String expansion = "" + s.child("name").getValue();
                String rarity = "" + s.child("rarity").getValue();
                String type = "" + s.child("type").getValue();
                String rule = "" + s.child("rule").getValue();
                String img = "" + snap.getResult().child("" + id).child("img").getValue();
                return Product.create(idd, name, expansion, rarity, type, rule, img);
            }
        }
        return null;
    }


    public ProductOnSale getProductOnSaleFromId(String id) {
        DatabaseReference product = database.child("product_on_sale");
        List<Product> prod = new ArrayList<>();
        Task<DataSnapshot> snap = product.get();
        while (!snap.isComplete()) {
        }

        for (DataSnapshot s : snap.getResult().getChildren()) {
            if (s.getKey().equals(id)) {
                String idd = s.getKey();
                Long product_id = (Long) s.child("product_id").getValue();
                String user_id = "" + s.child("user_id").getValue();
                Long price = (Long) s.child("price").getValue();
                String photo = ""+s.child("photo").getValue();
                return ProductOnSale.create(idd, product_id, user_id, price,photo);
            }
        }
        return null;
    }


    public List<Product> getSearchProducts(final String n, final String e,
                                           final String r, final String t) {
        String mExpansion = e;
        String mRarity = r;
        String mType = t;
        List<Product> products = new ArrayList<>();
        if (mExpansion.equals("All")) {
            mExpansion = "";
        }
        if (mType.equals("All")) {
            mType = "";
        }
        DatabaseReference product = database.child("product");
        List<Product> prod = new ArrayList<>();
        Task<DataSnapshot> snap = product.get();
        while (!snap.isComplete()) {
        }
        DatabaseReference language = database.child("language_" + Locale.getDefault().getLanguage());
        Task<DataSnapshot> lan = language.get();
        while (!lan.isComplete()) {
        }
        ;
        for (DataSnapshot s : lan.getResult().getChildren()) {
            if (mRarity.equals("All")) {

                if (("" + s.child("name").getValue()).toLowerCase().contains(n.toLowerCase()) &&
                        ("" + s.child("expansion").getValue()).toLowerCase().contains(mExpansion.toLowerCase()) &&
                        ("" + s.child("type").getValue()).toLowerCase().contains(mType.toLowerCase())) {

                    long idd = (Long) s.child("product_id").getValue();
                    String name = "" + s.child("name").getValue();
                    String expansion = "" + s.child("name").getValue();
                    String rarity = "" + s.child("rarity").getValue();
                    String type = "" + s.child("type").getValue();
                    String rule = "" + s.child("rule").getValue();
                    String img = "" + snap.getResult().child("" + s.child("product_id").getValue()).child("img").getValue();
                    Product p = Product.create(idd, name, expansion, rarity, type, rule, img);
                    products.add(p);
                }
            } else {
                if (("" + s.child("name").getValue()).toLowerCase().contains(n.toLowerCase()) &&
                        ("" + s.child("expansion").getValue()).toLowerCase().contains(mExpansion.toLowerCase()) &&
                        ("" + s.child("rarity").getValue()).toLowerCase().equals(mRarity.toLowerCase()) &&
                        ("" + s.child("type").getValue()).toLowerCase().contains(mType.toLowerCase())) {

                    long idd = (Long) s.child("product_id").getValue();
                    String name = "" + s.child("name").getValue();
                    String expansion = "" + s.child("name").getValue();
                    String rarity = "" + s.child("rarity").getValue();
                    String type = "" + s.child("type").getValue();
                    String rule = "" + s.child("rule").getValue();
                    String img = "" + snap.getResult().child("" + s.child("product_id").getValue()).child("img").getValue();
                    Product p = Product.create(idd, name, expansion, rarity, type, rule, img);
                    products.add(p);
                }
            }

        }
        return products;
    }


    public List<String> getAllExpansion() {
        HashSet<String> expansions = new HashSet<String>();
        expansions.add("All");
        DatabaseReference language = database.child("language_" + Locale.getDefault().getLanguage());
        Task<DataSnapshot> lan = language.get();
        while (!lan.isComplete()) {
        }
        ;
        for (DataSnapshot s : lan.getResult().getChildren()) {
            expansions.add("" + s.child("expansion").getValue());
        }
        return new ArrayList<String>(expansions);
    }


    public List<String> getAllTypes() {
        HashSet<String> types = new HashSet<String>();
        types.add("All");
        DatabaseReference language = database.child("language_" + Locale.getDefault().getLanguage());
        Task<DataSnapshot> lan = language.get();
        while (!lan.isComplete()) {
        }
        ;
        for (DataSnapshot s : lan.getResult().getChildren()) {
            types.add("" + s.child("type").getValue());
        }
        return new ArrayList<String>(types);
    }


    public List<String> getAllRaritys() {
        HashSet<String> raritys = new HashSet<String>();
        raritys.add("All");
        DatabaseReference language = database.child("language_" + Locale.getDefault().getLanguage());
        Task<DataSnapshot> lan = language.get();
        while (!lan.isComplete()) {
        }
        ;
        for (DataSnapshot s : lan.getResult().getChildren()) {
            raritys.add("" + s.child("rarity").getValue());
        }
        return new ArrayList<String>(raritys);
    }


    public void sellProductFromUser(Product product, User user, Long price) {
        ProductOnSale p = ProductOnSale.create(null, product.getId(), user.getId(), price, null);
        database.child("product_on_sale").push().setValue(p);
    }

    //TODO rifare con i dati
    public void registerUser(User user) {
        database.child("user").push().setValue(user);
    }


    public User logInUser(String userna, String passwo) {
        DatabaseReference users = database.child("user");
        Task<DataSnapshot> snap = users.get();
        while (!snap.isComplete()) {
        }
        ;
        for (DataSnapshot s : snap.getResult().getChildren()) {
            if (("" + s.child("username").getValue()).equals(userna) &&
                    ("" + s.child("password").getValue()).equals(passwo)) {
                String address = "" + s.child("address").getValue();
                Long cap = (Long) s.child("cap").getValue();
                String location = "" + s.child("location").getValue();
                String email = "" + s.child("email").getValue();
                String username = "" + s.child("username").getValue();
                String password = "" + s.child("password").getValue();
                String id = s.getKey();
                User u = User.create().withAddress(address).withUsername(username).withCap(cap).withEmail(email).withPassword(password)
                        .withLocation(location).withId(id);
                return u;
            }
        }
        return null;
    }

    public User getUserFromId(String mId) {
        DatabaseReference users = database.child("user");
        Task<DataSnapshot> snap = users.get();
        while (!snap.isComplete()) {
        }
        ;
        for (DataSnapshot s : snap.getResult().getChildren()) {
            if (("" + s.getKey()).equals(mId)) {
                String address = "" + s.child("address").getValue();
                Long cap = (Long) s.child("cap").getValue();
                String location = "" + s.child("location").getValue();
                String email = "" + s.child("email").getValue();
                String username = "" + s.child("username").getValue();
                String password = "" + s.child("password").getValue();
                String id = s.getKey();
                User u = User.create().withAddress(address).withUsername(username).withCap(cap).withEmail(email).withPassword(password)
                        .withLocation(location).withId(id);
                return u;
            }
        }
        return null;
    }

    public void modifyUser(User user) {
        String id = user.getId();
        database.child("user").child(id).setValue(user.withId(null));
        user.withId(id);
    }

    public void addPhotoToProductOnSale(ProductOnSale p) {
        String id = p.getId();
        database.child("product_on_sale").child(id).setValue(p.withId(null));
        p.withId(id);
    }

    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap, String name, final ProductOnSale p){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap = resizeImageForImageView(bitmap);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        StorageReference filepath = storageReference.child("product_on_sale").child(name);
        byte[] data = baos.toByteArray();

        filepath.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        p.withPhoto(uri.toString());
                        database.child("product_on_sale").child(p.getId()).child("photo").setValue(uri.toString());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

    }


    public Bitmap resizeImageForImageView(Bitmap bitmap) {
        int scaleSize =1024;
        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        Bitmap resizedBitmap = null;
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int newWidth = -1;
        int newHeight = -1;
        float multFactor = -1.0F;
        if(originalHeight > originalWidth) {
            newHeight = scaleSize ;
            multFactor = (float) originalWidth/(float) originalHeight;
            newWidth = (int) (newHeight*multFactor);
        } else if(originalWidth > originalHeight) {
            newWidth = scaleSize ;
            multFactor = (float) originalHeight/ (float)originalWidth;
            newHeight = (int) (newWidth*multFactor);
        } else if(originalHeight == originalWidth) {
            newHeight = scaleSize ;
            newWidth = scaleSize ;
        }
        resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
        return Bitmap.createBitmap(resizedBitmap, 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight(), matrix, true);
    }
}

/*
    public boolean logInUser(String username, String password) {
        open();
        boolean login=true;
        String query = String.format("SELECT EXISTS (SELECT * FROM user WHERE username = ? AND password = ?)");
        Cursor c = db.rawQuery(query, null);
        Log.d("DEBUG", "query result: " + c.getCount());
        if(c.getCount() <= 0){
            login=false;
        }
        c.close();
        close();
        return login;
    }


    public User getUser(String username, String password){
        open();
        String query = String.format("SELECT user.username, user.email, user.city, user.address, user.cap FROM user WHERE username = ? AND password = ?");
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        Log.println(Log.DEBUG,"username ", String.valueOf(c.isNull(0)));
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

    //TODO necessiterà un fix
    private User cursorToUser(Cursor c){
        //long id = c.getLong(0);
        String username = c.getString(0);
        String email = c.getString(1);
        String location = c.getString(2);
        String address = c.getString(3);
        long cap = c.getLong(4);
        return User.create().withUsername(username).
                withEmail(email).withLocation(location).withAddress(address).withCap(cap);
    }


    /*
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



       public List<ProductOnSale> getAllProductOnSaleFromProduct(Product product){
        open();
        List<ProductOnSale> products_on_sale = new ArrayList<>();
        Log.println(Log.DEBUG,"DB",db.toString());
        String query = String.format("SELECT product_on_sale.id, product_on_sale.product_id, " +
                "product_on_sale.user_id, product_on_sale.price FROM product INNER JOIN " +
                "product_on_sale ON product_on_sale.product_id = product.id INNER JOIN " +
                "language_%s ON language_%s.product_id = product.id WHERE product.id = %s",
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


    public Product getProductFromId(String id) {
        open();
        Log.println(Log.DEBUG,"DB",db.toString());
        String query = String.format("SELECT product.id, language_%1$s.name, " +
                "language_%1$s.expansion, language_%1$s.rarity, language_%1$s.type, " +
                "language_%1$s.rule, product.img FROM product INNER JOIN language_%1$s ON " +
                "language_%1$s.product_id = product.id WHERE product.id = %2$s",
                Locale.getDefault().getLanguage(), id);
        Log.e("QUERY", ""+query);

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        Product p = cursorToProduct(c);
        c.close();
        close();
        return p;
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
     */




