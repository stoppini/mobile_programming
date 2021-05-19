package com.example.MagicShop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

    private static final String TAG_LOG = ModifyInformationsActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_informations);

        this.mUsername = (EditText)findViewById(R.id.username_edit_text);

    }

    public void doConfirm(View confirmButton)
    {
        final Intent userAreaIntent = new Intent(ModifyInformationsActivity.this,UserAreaActivity.class);

        //modifica dati utente e aggiornamento db
        final String un = this.mUsername.getText().toString();
        PreferenceUtils.saveUsername(un, this);

        Log.d(TAG_LOG,"username " + un + " saved in preferences");

        startActivity(userAreaIntent);
    }
}