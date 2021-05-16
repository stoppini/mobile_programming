package com.example.MagicShop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.MagicShop.model.User;

public class FirstAccessActivity extends AppCompatActivity {

    private static final String TAG_LOG = FirstAccessActivity.class.getName();
    private static final int LOGIN_REQUEST_ID = 1;
    private static final int REGISTRATION_REQUEST_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_access);

        final Button anonymousBtn = (Button)findViewById(R.id.anonymous_button);
        anonymousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { enterAsAnonymous(); }
        });

        final Button registrationBtn = (Button)findViewById(R.id.register_button);
        registrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { doRegistration(); }
        });

        final Button loginBtn = (Button)findViewById(R.id.login_button);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { doLogin(); }
        });
    }

    private void enterAsAnonymous(){
        Log.d(TAG_LOG, "Anonymous access");
        final Intent anonymousIntent = new Intent(FirstAccessActivity.this, MenuActivity.class);
        final User user = User.create(System.currentTimeMillis());
        anonymousIntent.putExtra(User.USER_DATA_EXTRA, user);
        //userModel.save(this); // non usiamo più gli extra in questo caso, ma le SharedPreferences
        startActivity(anonymousIntent);
    }

    private void doRegistration(){
        Log.d(TAG_LOG, "Registration");
        final Intent registrationIntent = new Intent(Action.REGISTRATION_ACTION);
        startActivityForResult(registrationIntent, REGISTRATION_REQUEST_ID);
    }

    private void doLogin(){
        Log.d(TAG_LOG, "Log in");
        final Intent loginIntent = new Intent(Action.LOGIN_ACTION);
        startActivityForResult(loginIntent, LOGIN_REQUEST_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == LOGIN_REQUEST_ID)
        {
            switch (resultCode)
            {
                case RESULT_OK:
                    // now using SharePreferences, quindi commenti le prossime righe
                    final User user = data.getParcelableExtra(User.USER_DATA_EXTRA);
                    final Intent mainIntent = new Intent(FirstAccessActivity.this,MenuActivity.class);
                    mainIntent.putExtra(User.USER_DATA_EXTRA, user);
                    startActivity(mainIntent);
                    finish();
                    break;
                case RESULT_CANCELED:
                    break;
            }
        } else if(requestCode == REGISTRATION_REQUEST_ID)
        {
            switch (resultCode)
            {
                case RESULT_OK:
                    // now using SharePreferences, quindi commenti le prossime righe
                    final User user = (User)data.getParcelableExtra(User.USER_DATA_EXTRA);
                    //final Intent detailIntent = new Intent(Action.SHOW_USER_ACTION);
                    final Intent summaryIntent = new Intent(FirstAccessActivity.this, SummaryActivity.class);
                    Log.d(TAG_LOG,"Registration completed!");
                    summaryIntent.putExtra(User.USER_DATA_EXTRA, user);
                    startActivity(summaryIntent);
                    finish();
                    break;
                case RESULT_CANCELED:
                    break;
            }
        }
    }


}