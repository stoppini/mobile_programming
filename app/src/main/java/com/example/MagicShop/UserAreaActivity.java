package com.example.MagicShop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.MagicShop.utils.PreferenceUtils;

public class UserAreaActivity extends AppCompatActivity {

    private User mUser;
    private TextView mUsername;
    private TextView mEmail;
    private TextView mLocation;
    private TextView mAddress;
    private TextView mCap;

    private static final String TAG_LOG = UserAreaActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

//        Intent data = getIntent();
//        this.mUser = (User) data.getParcelableExtra(User.USER_DATA_EXTRA);
//        this.mUsername = (TextView)findViewById(R.id.show_username_data);
//        mUsername.setText(mUser.getUsername());
//        this.mEmail = (TextView)findViewById(R.id.show_email_data);
//        mEmail.setText(mUser.getEmail());
//        this.mLocation = (TextView)findViewById(R.id.show_location_data);
//        mLocation.setText(mUser.getLocation());
        this.mUsername = (TextView)findViewById(R.id.show_username_data);
        String un = PreferenceUtils.getUsername(this); //per checkare nel log
        mUsername.setText(un);
        this.mEmail = (TextView)findViewById(R.id.show_email_data);
        mEmail.setText(PreferenceUtils.getEmail(this));
        this.mAddress = (TextView)findViewById(R.id.show_address_data);
        mAddress.setText(PreferenceUtils.getAddress(this));
        this.mCap = (TextView)findViewById(R.id.show_cap_data);
        mCap.setText(PreferenceUtils.getCap(this));
        this.mLocation = (TextView)findViewById(R.id.show_location_data);
        mLocation.setText(PreferenceUtils.getLocation(this));

        Log.d(TAG_LOG,"username " + un + " retrived from preferences");


    }

    public void modifyInformations(View modifyButton){
        // gestire la modifica nel db
        // creare activity per modifica informazioni utente, simile
        final Intent mainIntent = new Intent(UserAreaActivity.this,
                                                ModifyInformationsActivity.class);
        startActivity(mainIntent);
        finish();
    }

}