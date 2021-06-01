package com.example.MagicShop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.MagicShop.model.DatabaseAccess;
import com.example.MagicShop.model.User;
import com.example.MagicShop.utils.PreferenceUtils;

public class ModifyInformationsActivity extends AppCompatActivity {

    private UserOutOfModel mUser;
    private EditText mUsername;
    private EditText mEmail;
    private EditText mLocation;
    private EditText mAddress;
    private EditText mCap;
    private DatabaseAccess dbA;
    private User user;

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
        dbA = DatabaseAccess.getDb();
        user = dbA.getUserFromId(PreferenceUtils.getId(this));

        final String un = this.mUsername.getText().toString();
        final String email = this.mEmail.getText().toString();
        final String location= this.mLocation.getText().toString();
        final String address = this.mAddress.getText().toString();
        final String cap = this.mCap.getText().toString();

        if(!TextUtils.isEmpty(un)){
            user.withUsername(un);
        }
        if(!TextUtils.isEmpty(email)){
            user.withEmail(email);
        }
        if(!TextUtils.isEmpty(address)){
            user.withAddress(address);
        }
        if(!TextUtils.isEmpty(location)){
            user.withLocation(location);
        }

        if(!TextUtils.isEmpty(cap)){
            user.withCap(Long.parseLong(cap));
        }

        dbA.modifyUser(user);

        startActivity(userAreaIntent);
        finish();
    }

}