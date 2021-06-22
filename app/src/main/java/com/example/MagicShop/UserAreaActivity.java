package com.example.MagicShop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MagicShop.model.DatabaseAccess;
import com.example.MagicShop.model.User;
import com.example.MagicShop.utils.PreferenceUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserAreaActivity extends AppCompatActivity {

    private UserOutOfModel mUser;
    private TextView mUsername;
    private TextView mEmail;
    private TextView mLocation;
    private TextView mAddress;
    private TextView mCap;
    private DatabaseAccess dbA;
    private User user;
    private final int INFORMATION_MODIFIED = 1;
    private static final int MODIFY_INFO_ID = 2;
    private static final String TAG_LOG = UserAreaActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);
        this.mUsername = (TextView)findViewById(R.id.show_username_data);
        this.mEmail = (TextView)findViewById(R.id.show_email_data);
        this.mAddress = (TextView)findViewById(R.id.show_address_data);
        this.mCap = (TextView)findViewById(R.id.show_cap_data);
        this.mLocation = (TextView)findViewById(R.id.show_location_data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        dbA = DatabaseAccess.getDb();
        user = dbA.getUserFromId(PreferenceUtils.getId(this));
        mUsername.setText(user.getUsername());
        mEmail.setText(user.getEmail());
        mAddress.setText(user.getAddress());
        mCap.setText(Long.toString(user.getCap()));
        mLocation.setText(user.getLocation());
    }


    public void modifyInformations(View modifyButton){
        // gestire la modifica nel db
        // creare activity per modifica informazioni utente, simile
        final Intent mainIntent = new Intent(UserAreaActivity.this,
                                                ModifyInformationsActivity.class);
        startActivityForResult(mainIntent, MODIFY_INFO_ID);
    }


    public void showHistory(View modifyButton){
        final Intent mainIntent = new Intent(UserAreaActivity.this,
                History.class);
        startActivity(mainIntent);
    }

    // accedo ai prodotti che sto vendendo
    public void seeMyProducts(View modifyButton){
        //List<Long> ids = dbA.getProductsSellingIdsFromUserId(user.getId());
        List<String> ids = dbA.getAllProductsOnSaleFromUserId(user.getId());
        if (ids.size() > 0){
            final Intent productsIntent = new Intent(UserAreaActivity.this,
                    MyProductsActivity.class);
            productsIntent.putExtra("ids", (Serializable) ids);
            startActivity(productsIntent);
            //finish();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.no_products), Toast.LENGTH_SHORT).show();
        }

    }

}