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

//        Intent data = getIntent();
//        this.mUser = (User) data.getParcelableExtra(User.USER_DATA_EXTRA);
//        this.mUsername = (TextView)findViewById(R.id.show_username_data);
//        mUsername.setText(mUser.getUsername());
//        this.mEmail = (TextView)findViewById(R.id.show_email_data);
//        mEmail.setText(mUser.getEmail());
//        this.mLocation = (TextView)findViewById(R.id.show_location_data);
//        mLocation.setText(mUser.getLocation());


        dbA = DatabaseAccess.getDb();
        user = dbA.getUserFromId(PreferenceUtils.getId(this));

        this.mUsername = (TextView)findViewById(R.id.show_username_data);
        mUsername.setText(user.getUsername());
        this.mEmail = (TextView)findViewById(R.id.show_email_data);
        mEmail.setText(user.getEmail());
        this.mAddress = (TextView)findViewById(R.id.show_address_data);
        mAddress.setText(user.getAddress());
        this.mCap = (TextView)findViewById(R.id.show_cap_data);
        mCap.setText(Long.toString(user.getCap()));
        this.mLocation = (TextView)findViewById(R.id.show_location_data);
        mLocation.setText(user.getLocation());


    }

    // tentativo di rimandare l'utente in "user area" se torna indietro da "modify information"
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        Log.e("DEBUG DIEGO", "entering on activity result method");
        if(requestCode == MODIFY_INFO_ID)
        {
            switch (resultCode)
            {
                case 1:
                    User user = data.getParcelableExtra("user");
                    mUsername.setText(user.getUsername());
                    mEmail.setText(user.getEmail());
                    mCap.setText(String.valueOf(user.getCap()));
                    mLocation.setText(user.getLocation());
                    mAddress.setText(user.getAddress());
                    break;
                case 0:
                    break;
            }
        }


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