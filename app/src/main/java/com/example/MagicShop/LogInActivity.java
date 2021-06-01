package com.example.MagicShop;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.MagicShop.model.DatabaseAccess;
import com.example.MagicShop.model.User;
import com.example.MagicShop.utils.PreferenceUtils;


public class LogInActivity extends AppCompatActivity {
    DatabaseAccess dbA;
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
        if(TextUtils.isEmpty(passwordEdit))
        {
            final String passwordMandatory = getResources().getString(R.string.mandatory_field_error,"password");
            this.mErrorTextView.setText(passwordMandatory);
            this.mErrorTextView.setVisibility(View.VISIBLE);
            return;
        }


        dbA = DatabaseAccess.getDb();
        User userFromDb = dbA.logInUser(usernameEdit, passwordEdit);


        if(userFromDb != null){
            Log.e("debug",""+usernameEdit+" is logged "+passwordEdit);

            PreferenceUtils.logging(true, this);
            PreferenceUtils.saveId(userFromDb.getId(), this);

            Intent resultIntent = new Intent();
            //resultIntent.putExtra(User.USER_DATA_EXTRA, userFromDb); i put extra non dovrebbero più servire nel log in
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
