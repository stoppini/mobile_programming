package com.example.MagicShop;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.MagicShop.model.DatabaseAccess;
import com.example.MagicShop.model.Product;
import com.example.MagicShop.model.ProductOnSale;
import com.example.MagicShop.model.User;
import com.squareup.picasso.Picasso;


import java.io.IOException;
import java.util.LinkedList;
import java.util.List;




public class ShowProductOnSaleSeller extends AppCompatActivity {
    DatabaseAccess dbA;

    private TextView nameToView;
    private TextView expansionToView;
    private TextView rarityToView;
    private TextView ruleToView;
    private ImageView imgToView;
    private ListView mListView;
    private List<ProductOnSale> mProduct = new LinkedList<>();
    private ListAdapter mAdapter;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product_on_sale_seller);
        mListView = (ListView)findViewById(R.id.listViewShowUsers);
        nameToView = (TextView)findViewById(R.id.nameView);
        expansionToView = (TextView)findViewById(R.id.expansionView);
        rarityToView = (TextView)findViewById(R.id.rarityView);
        ruleToView = (TextView)findViewById(R.id.ruleView);
        imgToView = (ImageView) findViewById(R.id.imgView);

    }

    @Override
    protected void onStart(){

        super.onStart();

        mAdapter = new BaseAdapter() {
            @Override
            public int getCount() { return mProduct.size(); }

            @Override
            public Object getItem(int position) {
                return mProduct.get(position);
            }

            @Override
            public long getItemId(int position) {
                Product product = (Product) getItem(position);
                return -1;
            }

            @Override
            public View getView(int position, View view, ViewGroup parent) {
                if(view == null){
                    view = getLayoutInflater().inflate(R.layout.custom_seller, null);
                }

                final TextView nameToView = (TextView) view.findViewById(R.id.name_seller);
                final TextView priceToView = (TextView) view.findViewById(R.id.price_seller);

                final ProductOnSale product = (ProductOnSale) getItem(position);

                nameToView.setText(dbA.getUsersFromProductOnSale(product).getUsername());
                priceToView.setText(""+product.getPrice());
                return view;
            }
        };


        mListView.setAdapter(mAdapter);

        dbA = DatabaseAccess.getDb();

        Product p = dbA.getProductFromId((Long)getIntent().getSerializableExtra(Product.PRODUCT_LIST_EXTRA));
        List<ProductOnSale> products = dbA.getAllProductOnSaleFromProduct(p);

        mProduct.clear();
        mProduct.addAll(products);
        mListView.setAdapter(mAdapter);


        nameToView.setText(p.getName());
        expansionToView.setText(p.getExpansion());
        rarityToView.setText(p.getRarity());
        ruleToView.setText(p.getRule());
        Picasso.get().load(""+p.getImg()).into(imgToView);


        final Button sellButton = (Button)findViewById(R.id.sell_product);
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotDialog(context, p, User.create());
            }
        });

    }


    private void showForgotDialog(Context c, Product p, User u) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Sell the product "+p.getName())
                .setMessage("Enter the amount in Euro")
                .setView(taskEditText)
                .setPositiveButton("Sell", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Long price = Long.valueOf(String.valueOf(taskEditText.getText()));
                        dbA.sellProductFromUser(p, u, price);
                        onStart();
                    }
                })
                .setNegativeButton("Undo", null)
                .create();
        dialog.show();
    }
}