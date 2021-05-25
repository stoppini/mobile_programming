package com.example.MagicShop;


import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.MagicShop.model.DatabaseAccess;
import com.example.MagicShop.model.Product;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShowProductOnSaleSeller extends AppCompatActivity {
    DatabaseAccess dbA;
    TextView nameToView;
    TextView expansionToView;
    TextView rarityToView;
    TextView ruleToView;
    ImageView imgToView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product_on_sale_seller);
    }

    @Override
    protected void onStart(){
        super.onStart();
        setContentView(R.layout.activity_show_product_on_sale_seller);
        nameToView = (TextView)findViewById(R.id.nameView);
        expansionToView = (TextView)findViewById(R.id.expansionView);
        rarityToView = (TextView)findViewById(R.id.rarityView);
        ruleToView = (TextView)findViewById(R.id.rule);
        imgToView = (ImageView) findViewById(R.id.imgView);

        try {
            dbA = DatabaseAccess.getInstance(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Product p = dbA.getProductFromId((Long)getIntent().getSerializableExtra(Product.PRODUCT_LIST_EXTRA));
        /*
        nameToView.setText(p.getName());
        expansionToView.setText(p.getExpansion());
        rarityToView.setText(p.getRarity());
        ruleToView.setText(p.getRule());
        Picasso.get().load(""+p.getImg()).into(imgToView);

         */
        Log.e("",""+p.toString());
    }
}