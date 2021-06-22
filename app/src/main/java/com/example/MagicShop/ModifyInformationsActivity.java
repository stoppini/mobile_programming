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

import java.util.HashMap;
import java.util.Map;

public class ModifyInformationsActivity extends AppCompatActivity {

    private UserOutOfModel mUser;
    private EditText mUsername;
    private EditText mEmail;
    private EditText mLocation;
    private EditText mAddress;
    private EditText mCap;
    private DatabaseAccess dbA;
    private User user;



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
        dbA = DatabaseAccess.getDb();
        user = dbA.getUserFromId(PreferenceUtils.getId(this));

        final String un = this.mUsername.getText().toString();
        final String email = this.mEmail.getText().toString();
        final String location= this.mLocation.getText().toString();
        final String address = this.mAddress.getText().toString();
        final String cap = this.mCap.getText().toString();

        Map<String, String> toModify = new HashMap<String, String>();

        toModify.put("id",PreferenceUtils.getId(this));

        if(!TextUtils.isEmpty(un)){
            toModify.put("username", un);
        }
        if(!TextUtils.isEmpty(email)){
            toModify.put("email", email);
        }
        if(!TextUtils.isEmpty(address)){
            toModify.put("address", address);
        }
        if(!TextUtils.isEmpty(location)){
            toModify.put("location", location);
        }

        if(!TextUtils.isEmpty(cap)){
            toModify.put("cap", cap);
        }

        dbA.modifyUser(toModify);

        finish();
    }

}