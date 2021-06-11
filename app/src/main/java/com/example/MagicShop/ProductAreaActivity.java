package com.example.MagicShop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.MagicShop.model.DatabaseAccess;
import com.example.MagicShop.model.ProductOnSale;
import com.example.MagicShop.model.User;
import com.example.MagicShop.utils.PreferenceUtils;

public class ProductAreaActivity extends AppCompatActivity {

    private TextView price;
    private TextView cardName;
    private DatabaseAccess dbA;
    private Long cardId;
    private ProductOnSale productOnSale;
    private final int INFORMATION_MODIFIED = 1;
    private static final String TAG_LOG = UserAreaActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_area);

        dbA = DatabaseAccess.getDb();
        cardId = getIntent().getExtras().getLong("card_Id");

        productOnSale = dbA.getProductOnSaleFromProductId(cardId);


        this.price = (TextView)findViewById(R.id.card_price);
        price.setText(String.valueOf(productOnSale.getPrice()));

        this.cardName = (TextView)findViewById(R.id.card_name);
        dbA.getProductFromId(productOnSale.getProduct_id()).getName();
        cardName.setText(dbA.getProductFromId(productOnSale.getProduct_id()).getName());
    }


    public void modifyPrice(View modifyButton){
        final Intent mainIntent = new Intent(ProductAreaActivity.this,
                ModifyPriceActivity.class);
        mainIntent.putExtra("prod_on_sale_id", productOnSale.getId());
        startActivity(mainIntent);
        finish();
    }


    public void eliminateProductOnSale(View eliminateButton){
        final Intent mainIntent = new Intent(ProductAreaActivity.this,
                UserAreaActivity.class);
        dbA.eliminateProductOnSaleFromDb(productOnSale.getId());
        startActivity(mainIntent);
        finish();
    }

}