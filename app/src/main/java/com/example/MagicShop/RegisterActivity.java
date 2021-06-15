package com.example.MagicShop;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.MagicShop.model.DatabaseAccess;
import com.example.MagicShop.model.User;

public class RegisterActivity extends AppCompatActivity {

    DatabaseAccess dbA;
    private TextView mErrorTextView;
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private EditText mEmailEditText;
    //private DatePicker mBirthDateEditText;
    private EditText mLocationEditText;
    private EditText mAddressEditText;
    private EditText mCapEditText;

    private static final String TAG_LOG = RegisterActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.mErrorTextView = (TextView)findViewById(R.id.error_message_label);
        this.mUsernameEditText = (EditText)findViewById(R.id.username_edit_to_register);
        this.mPasswordEditText = (EditText)findViewById(R.id.password_edit_to_register);
        this.mConfirmPasswordEditText = (EditText)findViewById(R.id.confirm_password);
        this.mEmailEditText = (EditText)findViewById(R.id.email_edit_to_register);
        //this.mBirthDateEditText = (DatePicker)findViewById(R.id.birthDate_edit_to_register);
        this.mLocationEditText = (EditText)findViewById(R.id.location_edit_to_register);
        this.mAddressEditText = (EditText)findViewById(R.id.address_edit_to_register);
        this.mCapEditText = (EditText)findViewById(R.id.cap_edit_to_register);
    }

    public void doRegistration(View confirmButton){
        final String usernameEdit = this.mUsernameEditText.getText().toString().toLowerCase();
        final String passwordEdit = this.mPasswordEditText.getText().toString();
        final String confirmPasswordEdit = this.mConfirmPasswordEditText.getText().toString();
        final String emailEdit = this.mEmailEditText.getText().toString();
        final String locationEdit = this.mLocationEditText.getText().toString().toLowerCase();
        final String addressEdit = this.mAddressEditText.getText().toString().toLowerCase();
        final String capEdit = this.mCapEditText.getText().toString();

        //checking all fields are not null
        if(TextUtils.isEmpty(usernameEdit) || TextUtils.isEmpty(passwordEdit) || TextUtils.isEmpty(confirmPasswordEdit)
                || TextUtils.isEmpty(emailEdit) || TextUtils.isEmpty(locationEdit) || TextUtils.isEmpty(addressEdit)
                || TextUtils.isEmpty(capEdit))
        {
            final String usernameMandatory = getResources().getString(R.string.all_mandatory_fields_error);
            this.mErrorTextView.setText(usernameMandatory);
            this.mErrorTextView.setVisibility(View.VISIBLE);
            return;
        }

        //checking passwords
        if(!(passwordEdit.equals(confirmPasswordEdit)))
        {
            final String differentPasswords = getResources().getString(R.string.different_passwords_error);
            this.mErrorTextView.setText(differentPasswords);
            this.mErrorTextView.setVisibility(View.VISIBLE);
            return;
        }

        //checking cap
        if(capEdit.length()==5){
            if(!isNumeric(capEdit)){
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

        //checking email
        if(!Patterns.EMAIL_ADDRESS.matcher(emailEdit).matches()){
            final String mailError = getResources().getString(R.string.mail_error);
            this.mErrorTextView.setText(mailError);
            this.mErrorTextView.setVisibility(View.VISIBLE);
            return;
        }


        dbA = DatabaseAccess.getDb();
        //checking user already exists
        if(dbA.existingUser(usernameEdit)){
            final String userAlreadyExists = getResources().getString(R.string.existing_user_error);
            this.mErrorTextView.setText(userAlreadyExists);
            this.mErrorTextView.setVisibility(View.VISIBLE);
            return;
        }

        if(dbA.existingEmail(emailEdit)){
            final String mailAlreadyExists = getResources().getString(R.string.existing_email_error);
            this.mErrorTextView.setText(mailAlreadyExists);
            this.mErrorTextView.setVisibility(View.VISIBLE);
            return;
        }


        final User user = User.create().withUsername(usernameEdit).withPassword(passwordEdit).withEmail(emailEdit).
                withLocation(locationEdit).withAddress(addressEdit).withCap(Long.parseLong(capEdit));


        Intent resultIntent = new Intent();

        if(user != null) {

            Log.d(TAG_LOG,"Send registration!");
            resultIntent.putExtra(User.USER_DATA_EXTRA,user);


            // saving preferences for logged user
//            PreferenceUtils.saveUsername(usernameEdit, this);
//            PreferenceUtils.saveAddress(addressEdit, this);
//            PreferenceUtils.saveEmail(emailEdit, this);
//            PreferenceUtils.saveLocation(locationEdit, this);
//            PreferenceUtils.saveCap(capEdit, this);


            setResult(RESULT_OK,resultIntent);
            finish();
        }
        else
        {
            setResult(RESULT_CANCELED,resultIntent);
            finish();
        }
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