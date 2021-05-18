package com.example.MagicShop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.MagicShop.model.User;

public class MenuActivity extends AppCompatActivity {

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

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
}
