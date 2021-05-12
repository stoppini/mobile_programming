package com.example.myapplication_test_db;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class LogInActivity extends AppCompatActivity {

    private TextView mErrorTextView;
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        this.mErrorTextView = (TextView)findViewById(R.id.error_message_label);
        this.mUsernameEditText = (EditText)findViewById(R.id.username_edit_text);
        this.mPasswordEditText = (EditText)findViewById(R.id.password_edit_text);
    }

    public void doLogin(View loginButton) {
        final String usernameEdit = this.mUsernameEditText.getText().toString();
        if(TextUtils.isEmpty(usernameEdit))
        {
            final String usernameMandatory = getResources().getString(R.string.mandatory_field_error,"username");
            this.mErrorTextView.setText(usernameMandatory);
            this.mErrorTextView.setVisibility(View.VISIBLE);
            return;
        }

        final String passwordEdit = this.mPasswordEditText.getText().toString();
        if(TextUtils.isEmpty(usernameEdit))
        {
            final String passwordMandatory = getResources().getString(R.string.mandatory_field_error,"password");
            this.mErrorTextView.setText(passwordMandatory);
            this.mErrorTextView.setVisibility(View.VISIBLE);
            return;
        }


        final String DUMMY_USERNAME = "diego";
        final String DUMMY_PASSWORD = "password";

        UserModel userModel = null;

        if(DUMMY_USERNAME.equals(usernameEdit) && DUMMY_PASSWORD.equals(passwordEdit))
        {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH,17);
            cal.set(Calendar.MONTH,6);
            cal.set(Calendar.YEAR,1977);
            final long birthDate = cal.getTimeInMillis();
            userModel = UserModel.create(birthDate).withUsername(usernameEdit).withPassword(passwordEdit).withEmail("diego.berardi@unibs.it").withLocation("Brescia");
        }

        if(userModel != null)
        {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(UserModel.USER_DATA_EXTRA,userModel);
            setResult(RESULT_OK,resultIntent);
            finish();
        }
        else
        {
            this.mErrorTextView.setText(getResources().getString(R.string.wrong_credentials_error));
            this.mErrorTextView.setVisibility(View.VISIBLE);
        }
    }
}
