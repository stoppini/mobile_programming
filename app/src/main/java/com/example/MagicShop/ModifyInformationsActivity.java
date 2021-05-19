package com.example.MagicShop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.MagicShop.utils.PreferenceUtils;

public class ModifyInformationsActivity extends AppCompatActivity {

    private User mUser;
    private EditText mUsername;
    private EditText mEmail;
    private EditText mLocation;
    private EditText mAddress;
    private EditText mCap;

    private static final String TAG_LOG = ModifyInformationsActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_informations);

        this.mUsername = (EditText)findViewById(R.id.username_edit_text);
        this.mEmail = (EditText)findViewById(R.id.email_edit_text);
        this.mAddress = (EditText)findViewById(R.id.address_edit_text);
        this.mLocation = (EditText)findViewById(R.id.location_edit_text);
        this.mCap = (EditText)findViewById(R.id.cap_edit_text);

    }

    public void doConfirm(View confirmButton)
    {
        final Intent userAreaIntent = new Intent(ModifyInformationsActivity.this,UserAreaActivity.class);

        //modifica dati utente e aggiornamento db


        final String un = this.mUsername.getText().toString();
        final String email = this.mEmail.getText().toString();
        final String location= this.mLocation.getText().toString();
        final String address = this.mAddress.getText().toString();
        final String cap = this.mCap.getText().toString();

        if(!TextUtils.isEmpty(un)){
            PreferenceUtils.saveUsername(un, this);
            Log.d(TAG_LOG,"username " + un + " saved in preferences");
        }
        if(!TextUtils.isEmpty(email)){
            PreferenceUtils.saveEmail(email, this);
            Log.d(TAG_LOG,"email " + email + " saved in preferences");
        }
        if(!TextUtils.isEmpty(address)){
            PreferenceUtils.saveAddress(address, this);
            Log.d(TAG_LOG,"address " + address + " saved in preferences");
        }
        if(!TextUtils.isEmpty(location)){
            PreferenceUtils.saveLocation(location, this);
            Log.d(TAG_LOG,"location " + location+ " saved in preferences");
        }
        if(!TextUtils.isEmpty(cap)){
            PreferenceUtils.saveCap(cap, this);
            Log.d(TAG_LOG,"cap " + cap + " saved in preferences");
        }

        startActivity(userAreaIntent);
        finish();
    }
}