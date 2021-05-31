package com.example.MagicShop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.MagicShop.model.DatabaseAccess;
import com.example.MagicShop.model.User;
import com.example.MagicShop.utils.PreferenceUtils;

import java.io.IOException;

public class MenuActivity extends AppCompatActivity {
    private DatabaseAccess dba;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);




//        Log.d("CHEK USERS IN DB","users: " + dbA.getAllUsers().get(0).getUsername() + "\n"
//                                                    + dbA.getAllUsers().get(1).getUsername() + "\n"
//                                                    + dbA.getAllUsers().get(2).getUsername() + "\n"
//                                                    + dbA.getAllUsers().get(3).getUsername());

        //Log.d("CHEK USERS IN DB","users: " + dbA.getAllUsers());

        final Button userArea = (Button)findViewById(R.id.user_area_button);
        userArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { enterUserArea(); }
        });

        final Button logOut = (Button)findViewById(R.id.log_out_button);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { doLogOut(); }
        });

        final Button anonymousBtn = (Button)findViewById(R.id.view_remote_test_db);
        anonymousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { enterTestDatabase(); }
        });

        final Button findProductsBtn = (Button)findViewById(R.id.find_products);
        findProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { findProducts(); }
        });
    }


    private void enterTestDatabase(){


        Log.d("DEBUG", "DB test access");
        final Intent showDatabaseIntent = new Intent(MenuActivity.this, ShowDb.class);
        startActivity(showDatabaseIntent);
    }

    private void findProducts(){
        Log.d("DEBUG", "Find Products");
        final Intent findProducts = new Intent(MenuActivity.this, FindProducts.class);
        startActivity(findProducts);
    }

    private void enterUserArea(){
        Log.d("Menu Activity", "entering user area");
        final Intent userArea = new Intent(MenuActivity.this, UserAreaActivity.class);
        startActivity(userArea);
    }

    private void doLogOut(){
        Log.d("Menu Activity", "logging out");
        final Intent userArea = new Intent(MenuActivity.this, FirstAccessActivity.class);
        PreferenceUtils.logOut(this);
        startActivity(userArea);
    }
}
