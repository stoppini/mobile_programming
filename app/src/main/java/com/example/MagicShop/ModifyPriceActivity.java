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

public class ModifyPriceActivity extends AppCompatActivity {


    private EditText price;
    private DatabaseAccess dbA;
    private User user;

    private static final String TAG_LOG = ModifyInformationsActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_price);

        this.price = (EditText)findViewById(R.id.modify_card_price);

    }

    public void doConfirm(View confirmButton)
    {
        final Intent userAreaIntent = new Intent(this,UserAreaActivity.class);

        //modifica dati utente e aggiornamento db
        dbA = DatabaseAccess.getDb();
        String productOnSaleId = getIntent().getExtras().getString("prod_on_sale_id");
        dbA.modifyPriceFromId(productOnSaleId, Long.parseLong(price.getText().toString()));

        startActivity(userAreaIntent);
        finish();
    }
}