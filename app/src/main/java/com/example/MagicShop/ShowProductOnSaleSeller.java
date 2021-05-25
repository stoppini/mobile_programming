package com.example.MagicShop;


import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.MagicShop.model.DatabaseAccess;
import com.example.MagicShop.model.Product;
import com.example.MagicShop.model.ProductOnSale;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ShowProductOnSaleSeller extends AppCompatActivity {
    DatabaseAccess dbA;
    TextView nameToView;
    TextView expansionToView;
    TextView rarityToView;
    TextView ruleToView;
    ImageView imgToView;
    private ListView mListView;
    private List<ProductOnSale> mProduct = new LinkedList<>();
    private ListAdapter mAdapter;

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

        try {
            dbA = DatabaseAccess.getInstance(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                return product.getId();
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
        try {
            dbA = DatabaseAccess.getInstance(this);
        } catch (IOException e) {
            e.printStackTrace();
        }



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


        Log.e("",""+p.toString());
    }
}