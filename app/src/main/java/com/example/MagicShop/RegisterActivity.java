package com.example.MagicShop;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.MagicShop.model.User;
import com.example.MagicShop.utils.PreferenceUtils;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

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

        //checking passwords are the same
        if(!(passwordEdit.equals(confirmPasswordEdit)))
        {
            final String differentPasswords = getResources().getString(R.string.different_passwords_error);
            this.mErrorTextView.setText(differentPasswords);
            this.mErrorTextView.setVisibility(View.VISIBLE);
            return;
        }

//        Calendar cal = Calendar.getInstance();
//        cal.set(this.mBirthDateEditText.getYear(),this.mBirthDateEditText.getMonth(),this.mBirthDateEditText.getDayOfMonth());
//        final long birthDateEdit = cal.getTimeInMillis();
        final User user = User.create().withUsername(usernameEdit).withPassword(passwordEdit).withEmail(emailEdit).
                withLocation(locationEdit).withAddress(addressEdit).withCap(Integer.parseInt(capEdit));

        Intent resultIntent = new Intent();

        if(user != null) {
            Log.d(TAG_LOG,"Send registration!");
            resultIntent.putExtra(User.USER_DATA_EXTRA,user);
            // saving preferences for logged user
            PreferenceUtils.saveUsername(usernameEdit, this);
            setResult(RESULT_OK,resultIntent);
            finish();
        }
        else
        {
            setResult(RESULT_CANCELED,resultIntent);
            finish();
        }
    }
}