package com.example.MagicShop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.MagicShop.model.DatabaseAccess;
import com.example.MagicShop.model.User;
import com.example.MagicShop.utils.PreferenceUtils;

import java.util.HashMap;
import java.util.Map;

public class ModifyInformationsActivity extends AppCompatActivity {

    DatabaseAccess dbA;
    private TextView mErrorTextView;
    private UserOutOfModel mUser;
    private EditText mUsername;
    private EditText mEmail;
    private EditText mLocation;
    private EditText mAddress;
    private EditText mCap;
    private User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_informations);

        this.mErrorTextView = (TextView)findViewById(R.id.error_message_label);
        this.mUsername = (EditText)findViewById(R.id.username_edit_text);
        this.mEmail = (EditText)findViewById(R.id.email_edit_text);
        this.mAddress = (EditText)findViewById(R.id.address_edit_text);
        this.mLocation = (EditText)findViewById(R.id.location_edit_text);
        this.mCap = (EditText)findViewById(R.id.cap_edit_text);
    }

    public void doConfirm(View confirmButton)
    {

        //modifica dati utente e aggiornamento db
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
            if(dbA.existingUser(mUsername.getText().toString())){
                final String userAlreadyExists = getResources().getString(R.string.existing_user_error);
                this.mErrorTextView.setText(userAlreadyExists);
                this.mErrorTextView.setVisibility(View.VISIBLE);
                return;
            }
            toModify.put("username", un);
        }
        if(!TextUtils.isEmpty(email)){
            if(!Patterns.EMAIL_ADDRESS.matcher(mEmail.getText().toString()).matches()){
                final String mailError = getResources().getString(R.string.mail_error);
                this.mErrorTextView.setText(mailError);
                this.mErrorTextView.setVisibility(View.VISIBLE);
                return;
            }
            if(dbA.existingEmail(mEmail.getText().toString())){
                final String mailAlreadyExists = getResources().getString(R.string.existing_email_error);
                this.mErrorTextView.setText(mailAlreadyExists);
                this.mErrorTextView.setVisibility(View.VISIBLE);
                return;
            }
            toModify.put("email", email);
        }
        if(!TextUtils.isEmpty(address)){
            toModify.put("address", address);
        }
        if(!TextUtils.isEmpty(location)){
            toModify.put("location", location);
        }
        if(!TextUtils.isEmpty(cap)){
            if(mCap.getText().toString().length()==5){
                if(!isNumeric(mCap.getText().toString())){
                    final String numericCap = getResources().getString(R.string.cap_error);
                    this.mErrorTextView.setText(numericCap);
                    this.mErrorTextView.setVisibility(View.VISIBLE);
                    return;
                }
            }else{
                final String lengthCap = getResources().getString(R.string.cap_error);
                this.mErrorTextView.setText(lengthCap);
                this.mErrorTextView.setVisibility(View.VISIBLE);
                return;
            }
            toModify.put("cap", cap);
        }

        dbA.modifyUser(toModify);

        finish();
    }


    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

}