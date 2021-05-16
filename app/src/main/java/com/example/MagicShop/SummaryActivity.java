package com.example.MagicShop;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SummaryActivity extends AppCompatActivity {

    private User mUser;
    private TextView mUsername;
    private TextView mEmail;
    private TextView mBirthDate;
    private TextView mLocation;

    private static final String TAG_LOG = SummaryActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        Intent data = getIntent();
        this.mUser = (User) data.getParcelableExtra(User.USER_DATA_EXTRA);

        this.mUsername = (TextView)findViewById(R.id.show_username_data);
        mUsername.setText(mUser.getUsername());

        this.mEmail = (TextView)findViewById(R.id.show_email_data);
        mEmail.setText(mUser.getEmail());

        this.mBirthDate = (TextView)findViewById(R.id.show_birthdate_data);
        Date date=new Date(mUser.getBirthDate());
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
        String dateText = df2.format(date);
        mBirthDate.setText(dateText);

        this.mLocation = (TextView)findViewById(R.id.show_location_data);
        mLocation.setText(mUser.getLocation());


        Log.d(TAG_LOG,mUsername.getText().toString());

    }

    public void doConfirm(View confirmButton)
    {
        final Intent mainIntent = new Intent(SummaryActivity.this,MenuActivity.class);
        mainIntent.putExtra(User.USER_DATA_EXTRA,this.mUser);
        startActivity(mainIntent);
    }
}
