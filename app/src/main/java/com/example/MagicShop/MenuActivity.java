package com.example.MagicShop;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.MagicShop.model.DatabaseAccess;
import com.example.MagicShop.model.User;
import com.example.MagicShop.utils.PreferenceUtils;

public class MenuActivity extends AppCompatActivity {
    private DatabaseAccess dbA;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        dbA = DatabaseAccess.getDb();


        final Button userArea = (Button)findViewById(R.id.user_area_button);
        userArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { enterUserArea(); }
        });

        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ){
            NotificationChannel channel = new NotificationChannel("My notification","My notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }*/


        final Button logOut = (Button)findViewById(R.id.log_out_button);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { doLogOut(); }
        });

/*        final Button anonymousBtn = (Button)findViewById(R.id.view_remote_test_db);
        anonymousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { enterTestDatabase(); }
        });*/

        final Button findProductsBtn = (Button)findViewById(R.id.find_products);
        findProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { findProducts(); }
        });
    }


/*    private void enterTestDatabase(){
        Log.d("DEBUG", "DB test access");
        final Intent showDatabaseIntent = new Intent(MenuActivity.this, ShowDb.class);
        startActivity(showDatabaseIntent);
    }*/

    private void findProducts(){
        Log.d("DEBUG", "Find Products");
        final Intent findProducts = new Intent(MenuActivity.this, FindProducts.class);
        startActivity(findProducts);
    }

    private void enterUserArea(){
//        if(PreferenceUtils.isLogged(this)){
            Log.e("Menu Activity", "entering user area");
            final Intent userArea = new Intent(MenuActivity.this, UserAreaActivity.class);
            startActivity(userArea);
//        }
//        else{
//            Log.e("Toast", "toast should appear on screen");
//            Context context = getApplicationContext();
//            CharSequence text = getResources().getString(R.string.user_not_logged);
//            int duration = Toast.LENGTH_SHORT;
//            Toast toast = Toast.makeText(context, text, duration);
//            toast.show();
//        }
    }

    private void doLogOut(){
//        if(PreferenceUtils.isLogged(this)) {
            Log.d("DEBUG", "Logging in");
            final Intent userArea = new Intent(MenuActivity.this, FirstAccessActivity.class);
            PreferenceUtils.logOut(this);
            startActivity(userArea);
            finish();
//        }
//        else{
//            Log.e("Toast", "toast should appear on screen");
//            Context context = getApplicationContext();
//            CharSequence text = getResources().getString(R.string.user_not_logged);
//            int duration = Toast.LENGTH_SHORT;
//            Toast toast = Toast.makeText(context, text, duration);
//            toast.show();
//        }
    }
}
