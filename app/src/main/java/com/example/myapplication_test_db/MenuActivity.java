package com.example.myapplication_test_db;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    private UserModel mUserModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        final Button anonymousBtn = (Button)findViewById(R.id.view_remote_test_db);
        anonymousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { enterTestDatabase(); }
        });
    }


    private void enterTestDatabase(){
        Log.d("DEBUG", "DB test access");
        final Intent showDatabaseIntent = new Intent(MenuActivity.this, ShowDb.class);
        startActivity(showDatabaseIntent);
    }
}
