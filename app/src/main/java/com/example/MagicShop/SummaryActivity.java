package com.example.MagicShop;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.MagicShop.model.DatabaseAccess;
import com.example.MagicShop.model.User;
import com.example.MagicShop.utils.PreferenceUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SummaryActivity extends AppCompatActivity {

    private DatabaseAccess dbA;
    private User mUser;
    private TextView mUsername;
    private TextView mPassword;
    private TextView mEmail;
    //private TextView mBirthDate;
    private TextView mLocation;
    private TextView mAddress;
    private TextView mCap;

    private static final String TAG_LOG = SummaryActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        Intent data = getIntent();
        this.mUser = (User) data.getParcelableExtra(User.USER_DATA_EXTRA);
        Log.e("debug",  "" + mUser.getUsername() + mUser.getAddress() + mUser.getEmail() + mUser.getLocation()
        + mUser.getCap());

        this.mUsername = (TextView)findViewById(R.id.show_username_data);
        mUsername.setText(mUser.getUsername());

        this.mPassword = (TextView)findViewById(R.id.show_password_data);
        mPassword.setText(mUser.getPassword());

        this.mEmail = (TextView)findViewById(R.id.show_email_data);
        mEmail.setText(mUser.getEmail());

//        this.mBirthDate = (TextView)findViewById(R.id.show_birthdate_data);
//        Date date=new Date(mUser.getBirthDate());
//        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
//        String dateText = df2.format(date);
//        mBirthDate.setText(dateText);

        this.mLocation = (TextView)findViewById(R.id.show_location_data);
        mLocation.setText(mUser.getLocation());

        this.mAddress = (TextView)findViewById(R.id.show_address_data);
        mAddress.setText(mUser.getAddress());

        this.mCap = (TextView)findViewById(R.id.show_cap_data);
        mCap.setText(Long.toString(mUser.getCap()));

        Log.d(TAG_LOG,mUsername.getText().toString());

    }

    public void doConfirm(View confirmButton)
    {
        // saving in db
        dbA = DatabaseAccess.getDb();
        dbA.registerUser(mUser);

        // re-loading user from db because we need his ID do be saved in preferenceUtils
        User userFromDb = dbA.logInUser(mUser.getUsername(), mUser.getPassword());
        PreferenceUtils.logging(true, this);
        PreferenceUtils.saveId(userFromDb.getId(), this);
        Log.e("debug",  "id utente appena registrato: " + PreferenceUtils.getId(this));

        final Intent mainIntent = new Intent(SummaryActivity.this,MenuActivity.class);
        mainIntent.putExtra(User.USER_DATA_EXTRA,this.mUser);
        startActivity(mainIntent);
        finish();
    }
}
