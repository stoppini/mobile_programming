package com.example.MagicShop.model;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.MagicShop.History;
import com.example.MagicShop.MenuActivity;
import com.example.MagicShop.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;


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

    private final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    /*public void addNotificationEvent(User u, Context context){
        DatabaseReference reference = database.child("user").child(u.getId()).child("history_sell");
        Log.e("user id", ""+u.getId());


        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                notification(context);
                Log.e("user id", "notificaaaa£");

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.e("user id", "noticambiato");
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void notification(Context context){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"My notification");
        builder.setContentTitle("My Title");
        builder.setContentText("Hello from Easy");
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setAutoCancel(true);
        NotificationManagerCompat compat = NotificationManagerCompat.from(context);
        compat.notify(1,builder.build());
    }*/


    public void addPhotoToProductOnSale (ProductOnSale p, String uri){
        database.child("product_on_sale").child(p.getId()).child("photo").setValue(uri.toString());
    }

    public void shopProduct(ProductOnSale productOnSale, User user){

        Task<DataSnapshot> productTask = database.child("product_on_sale").child(productOnSale.getId()).get();
        while (!productTask.isComplete()) {}
        DataSnapshot productRef = productTask.getResult();
        User seller = getUserFromId(productOnSale.getUser_id());
        Date time = new Date(System.currentTimeMillis());
        Long times = time.getTime();

        HistoryRecord.UserHistory hSeller = HistoryRecord.UserHistory.create(
                seller.getUsername(), seller.getEmail(), seller.getLocation(), seller.getAddress(),seller.getCap());

        HistoryRecord hRecordSeller = HistoryRecord.create((Long)productRef.child("product_id").getValue(),
                (Long)productRef.child("price").getValue(),times , hSeller);


        HistoryRecord.UserHistory hBuyer = HistoryRecord.UserHistory.create(
                user.getUsername(), user.getEmail(), user.getLocation(), user.getAddress(),user.getCap());

        HistoryRecord hRecordBuyer = HistoryRecord.create((Long)productRef.child("product_id").getValue(),
                (Long)productRef.child("price").getValue(), times, hBuyer);


        database.child("user").child(user.getId()).child("history_shop").push().setValue(hRecordSeller);

        database.child("user").child(productOnSale.getUser_id()).child("history_sell").push().setValue(hRecordBuyer);

        database.child("product_on_sale").child(productOnSale.getId()).removeValue();

    }

    public List<HistoryRecord> getHistorySell(User user){
        List<HistoryRecord> history = new ArrayList<>();
        Task<DataSnapshot> productTask = database.child("user").child(user.getId()).child("history_sell").get();
        while (!productTask.isComplete()) {}
        for (DataSnapshot s : productTask.getResult().getChildren()) {
            HistoryRecord.UserHistory userH = new HistoryRecord.UserHistory(
                    ""+s.child("user").child("mUserName").getValue(),
                    ""+s.child("user").child("mEmail").getValue(),
                    ""+s.child("user").child("mLocation").getValue(),
                    ""+s.child("user").child("mAddress").getValue(),
                    (long)s.child("user").child("mCap").getValue());
            HistoryRecord record = new HistoryRecord((long)s.child("product_id").getValue(),
                    Float.parseFloat(""+s.child("price").getValue()), (long)s.child("date").getValue(),
                    userH);
            history.add(record);
        }
        return history;
    }

    public List<HistoryRecord> getHistoryShop(User user){
        List<HistoryRecord> history = new ArrayList<>();

        Task<DataSnapshot> productTask = database.child("user").child(user.getId()).child("history_shop").get();
        while (!productTask.isComplete()) {}

        for (DataSnapshot s : productTask.getResult().getChildren()) {
            HistoryRecord.UserHistory userH = new HistoryRecord.UserHistory(
                    ""+s.child("user").child("mUserName").getValue(),
                    ""+s.child("user").child("mEmail").getValue(),
                    ""+s.child("user").child("mLocation").getValue(),
                    ""+s.child("user").child("mAddress").getValue(),
                    (long)s.child("user").child("mCap").getValue());
            HistoryRecord record = new HistoryRecord((long)s.child("product_id").getValue(),
                    Float.parseFloat(""+s.child("price").getValue()), (long)s.child("date").getValue(),
                    userH);
            history.add(record);
        }
        return history;
    }



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
                    Float.parseFloat(""+s.child("price").getValue()), ""+s.child("photo"));
            products_on_sale.add(p);

        }
        return products_on_sale;
    }


    public List<Product> getAllProducts() {
        DatabaseReference product = database.child("product");
        List<Product> prod = new ArrayList<>();
        Task<DataSnapshot> snap = product.get();
        while (!snap.isComplete()) {}

        DatabaseReference language = database.child("language_" + Locale.getDefault().getLanguage());
        Task<DataSnapshot> lan = language.get();
        while (!lan.isComplete()) {}

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
        while (!ref_prod.isComplete()) {}

        Task<DataSnapshot> ref_prod_on_sale = database.child("product_on_sale").get();
        while (!ref_prod_on_sale.isComplete()) {}

        for (DataSnapshot s : ref_prod_on_sale.getResult().getChildren()) {
            if (("" + s.child("product_id").getValue()).equals("" + product.getId())) {
                String id = s.getKey();
                float price = Float.parseFloat(""+s.child("price").getValue());
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
                String expansion = "" + s.child("expansion").getValue();
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
                float price = Float.parseFloat(""+s.child("price").getValue());
                String photo = ""+s.child("photo").getValue();
                return ProductOnSale.create(idd, product_id, user_id, price,photo);
            }
        }
        return null;
    }

    public ProductOnSale getProductOnSaleFromProductId(Long id, String userId) {
        DatabaseReference product = database.child("product_on_sale");
        List<Product> prod = new ArrayList<>();
        Task<DataSnapshot> snap = product.get();
        while (!snap.isComplete()) {
        }
        Log.e("diego debug", "user id: " + userId);

        for (DataSnapshot s : snap.getResult().getChildren()) {
            Log.e("diego debug", "user id: " + s.child("user_id"));
            if (Long.parseLong("" + s.child("product_id").getValue()) == (id) && s.child("user_id").getValue().equals(userId)) {
                String idd = s.getKey();
                Long product_id = (Long) s.child("product_id").getValue();
                String user_id = "" + s.child("user_id").getValue();
                float price = Float.parseFloat(""+s.child("price").getValue());
                Log.e("my product debug", "product id: " + product_id + " product price: "+ price);
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
        if (mExpansion.equals("All") || (mExpansion.equals("Tutto")) ) {
            mExpansion = "";
        }
        if (mType.equals("All") || (mType.equals("Tutto"))) {
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
            if (mRarity.equals("All") || mRarity.equals("Tutto")) {

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


    public List<String> getAllExpansion(String a) {
        HashSet<String> expansions = new HashSet<String>();
        expansions.add(a);
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


    public List<String> getAllTypes(String a) {
        HashSet<String> types = new HashSet<String>();
        types.add(a);
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


    public List<String> getAllRaritys(String a) {
        HashSet<String> raritys = new HashSet<String>();
        raritys.add(a);
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


    public void sellProductFromUser(Product product, User user, float price) {
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
        while (!snap.isComplete()) {};
        for (DataSnapshot s : snap.getResult().getChildren()) {
            if (("" + s.getKey()).equals(mId)) {

                String address = "" + s.child("address").getValue();
                Long cap = (Long)s.child("cap").getValue();
                String location = "" + s.child("location").getValue();
                String email = "" + s.child("email").getValue();
                String username = "" + s.child("username").getValue();
                String password = "" + s.child("password").getValue();
                String id = s.getKey();
                return User.create().withAddress(address).withUsername(username).withCap(cap).withEmail(email).withPassword(password)
                        .withLocation(location).withId(id);
            }
        }
        return null;
    }

    public void modifyUser(Map<String, String> toModify) {
        String id = toModify.get("id");
        String username = toModify.get("username");
        String email = toModify.get("email");
        String address = toModify.get("address");
        String location = toModify.get("location");
        String cap = toModify.get("cap");

        DatabaseReference refToUser = database.child("user").child(id);

        if(username != null){
            refToUser.child("username").setValue(username);
        }
        if(email != null){
            refToUser.child("email").setValue(username);
        }
        if(address != null){
            refToUser.child("address").setValue(username);
        }
        if(location != null){
            refToUser.child("location").setValue(username);
        }
        if(cap != null){
            refToUser.child("cap").setValue(username);
        }
    }

    public void addPhotoToProductOnSale(ProductOnSale p) {
        String id = p.getId();
        database.child("product_on_sale").child(id).setValue(p.withId(null));
        p.withId(id);
    }

    public List<Long> getProductsSellingIdsFromUserId(String id) {
            List<Long> ids = new ArrayList<Long>();
        DatabaseReference product = database.child("product_on_sale");
        List<Product> prod = new ArrayList<>();
        Task<DataSnapshot> snap = product.get();
        while (!snap.isComplete()) {
        }

        for (DataSnapshot s : snap.getResult().getChildren()) {
            //Log.e("debug", "DEBUGGGG" + String.valueOf(s.child("user_id").getValue()));
            if (s.child("user_id").getValue().equals(id)) {
                Long product_id = (Long) s.child("product_id").getValue();
                ids.add(product_id);
            }
        }
        Log.e("DIEGO DEBUG", String.valueOf(ids));
        return ids;
    }

    public List<String> getAllProductsOnSaleFromUserId(String userId){
        List<String> ids = new ArrayList<String>();
        DatabaseReference product = database.child("product_on_sale");
        Task<DataSnapshot> snap = product.get();
        while (!snap.isComplete()) {
        }
        for (DataSnapshot s : snap.getResult().getChildren()) {
            if (s.child("user_id").getValue().equals(userId)) {
                String product_on_sale_id = (String) s.getKey();
                ids.add(product_on_sale_id);
            }
        }
        return ids;
    }



    public void modifyPriceFromId(String id, float price) {
//        Log.e("DIEGO DEBUG", "product id:" + id + " user id: " + userId);
//        boolean found = false;
//        DatabaseReference users = database.child("product_on_sale");
//        Task<DataSnapshot> snap = users.get();
//        while (!snap.isComplete()) {
//        }
//        ;
//        for (DataSnapshot s : snap.getResult().getChildren()) {
//            if(s.child(userId).equals(userId) && s.getKey().equals(id)){
//                found=true;
//            }
//        }
//        if(found){
//            users.child(id).child("price").setValue(price);
//        }
//
        //naive implementation
        database.child("product_on_sale").child(id).child("price").setValue(price);
    }

    public void eliminateProductOnSaleFromDb(String id){
//        Log.e("DIEGO DEBUG", "product id:" + id + "user id: " + userId);
//
//        DatabaseReference users = database.child("product_on_sale");
//        Task<DataSnapshot> snap = users.get();
//        while (!snap.isComplete()) {
//        }
//        ;
//        for (DataSnapshot s : snap.getResult().getChildren()) {
//            if(s.child(userId).equals(userId) && s.getKey().equals(id)){
//                users.child(id).removeValue();
//            }
//        }

        //naive implementation
        database.child("product_on_sale").child(id).removeValue();
    }

    public boolean existingUser(String un){
        DatabaseReference users = database.child("user");
        Task<DataSnapshot> snap = users.get();
        while (!snap.isComplete()) {
        }
        for (DataSnapshot s : snap.getResult().getChildren()) {
            if (s.child("username").getValue().equals(un)) {
                return true;
            }
        }
        return false;
    }

    public boolean existingEmail(String email){
        DatabaseReference users = database.child("user");
        Task<DataSnapshot> snap = users.get();
        while (!snap.isComplete()) {
        }
        for (DataSnapshot s : snap.getResult().getChildren()) {
            if (s.child("email").getValue().equals(email)) {
                return true;
            }
        }
        return false;
    }
}





