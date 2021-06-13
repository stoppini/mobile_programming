package com.example.MagicShop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MagicShop.model.DatabaseAccess;
import com.example.MagicShop.model.Product;
import com.example.MagicShop.model.ProductOnSale;
import com.example.MagicShop.model.User;
import com.example.MagicShop.utils.PreferenceUtils;

public class ProductAreaActivity extends AppCompatActivity {

    private TextView price;
    private TextView cardName;
    private DatabaseAccess dbA;
    private Long cardId;
    private String userId;
    private ProductOnSale productOnSale;
    private Button modifyPrice;
    private Button deleteProduct;
    private final int INFORMATION_MODIFIED = 1;
    private static final String TAG_LOG = UserAreaActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_area);

        dbA = DatabaseAccess.getDb();
        cardId = getIntent().getExtras().getLong("card_Id");
        userId = getIntent().getExtras().getString("user_id");


        this.cardName = (TextView)findViewById(R.id.card_name);
        this.price = (TextView)findViewById(R.id.card_price);
        this.modifyPrice = (Button)findViewById(R.id.modify_button);
        this.deleteProduct = (Button)findViewById(R.id.eliminate_button);


    }

    @Override
    protected void onStart() {
        super.onStart();
        productOnSale = dbA.getProductOnSaleFromProductId(cardId, userId);
        dbA.getProductFromId(productOnSale.getProduct_id()).getName();
        String nameProduct = dbA.getProductFromId(productOnSale.getProduct_id()).getName();
        cardName.setText(nameProduct);

        price.setText(String.valueOf(productOnSale.getPrice()));
        modifyPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyPrice(ProductAreaActivity.this, productOnSale, nameProduct);
            }
        });
        deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct(ProductAreaActivity.this, productOnSale, nameProduct);
            }
        });
    }


    private void modifyPrice(Context c, ProductOnSale p, String name) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle(getString(R.string.title_modify_price) + name +".")
                .setMessage((getString(R.string.message_price)))
                .setView(taskEditText)
                .setPositiveButton(getString(R.string.modify_price_toast), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Long price = null;
                        try{
                            price = Long.valueOf(String.valueOf(taskEditText.getText()));
                            if(price != 0){
                                dbA.modifyPriceFromId(p.getId(), price);
                                onStart();
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.error_price) , Toast.LENGTH_SHORT).show();
                                modifyPrice(c,p,name);
                            }

                        } catch (Exception ex){
                            Toast.makeText(getApplicationContext(), getString(R.string.error_price), Toast.LENGTH_SHORT).show();
                            modifyPrice(c,p,name);
                        }
                    }
                })
                .setNegativeButton(getString(R.string.undo_price), null)
                .create();
        dialog.show();
    }

    private void deleteProduct(Context c, ProductOnSale p, String name) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle(getString(R.string.remove_from_on_sale) + name +"?")
                .setPositiveButton(getString(R.string.remove_price), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbA.eliminateProductOnSaleFromDb(productOnSale.getId());
                        final Intent mainIntent = new Intent(ProductAreaActivity.this,
                                UserAreaActivity.class);
                        startActivity(mainIntent);
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.undo_price), null)
                .create();
        dialog.show();
    }



/*
    public void modifyPrice(View modifyButton){
        final Intent mainIntent = new Intent(ProductAreaActivity.this,
                ModifyPriceActivity.class);
        mainIntent.putExtra("prod_on_sale_id", productOnSale.getId());
        mainIntent.putExtra("user_id", productOnSale.getUser_id());
        startActivity(mainIntent);
        finish();
    }*/


    public void eliminateProductOnSale(View eliminateButton){
        final Intent mainIntent = new Intent(ProductAreaActivity.this,
                UserAreaActivity.class);
        dbA.eliminateProductOnSaleFromDb(productOnSale.getId());
        startActivity(mainIntent);
        finish();
    }

}