package com.example.MagicShop;

import androidx.appcompat.app.AppCompatActivity;

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

    private TextView nameToView;
    private TextView expansionToView;
    private TextView rarityToView;
    private TextView ruleToView;
    private ImageView imgToView;

    private DatabaseAccess dbA;
    private List<ProductOnSale> mProduct = new LinkedList<>();
    private ListAdapter mAdapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void onStart(){
        super.onStart();
        setContentView(R.layout.activity_show_product_on_sale_seller);

        nameToView = (TextView)findViewById(R.id.nameView);
        expansionToView = (TextView)findViewById(R.id.expansionView);
        rarityToView = (TextView)findViewById(R.id.rarityView);
        ruleToView = (TextView)findViewById(R.id.ruleView);
        imgToView = (ImageView) findViewById(R.id.imgView);

        try {
            dbA = DatabaseAccess.getInstance(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mListView = (ListView)findViewById(R.id.listView_seller);

        mAdapter = new BaseAdapter() {
            @Override
            public int getCount() { return mProduct.size(); }

            @Override
            public Object getItem(int position) {
                return mProduct.get(position);
            }

            @Override
            public long getItemId(int position) {
                ProductOnSale product = (ProductOnSale) getItem(position);
                return product.getId();
            }

            @Override
            public View getView(int position, View view, ViewGroup parent) {
                if(view == null){
                    view = getLayoutInflater().inflate(R.layout.custom_product_on_sale, null);
                }
                final TextView nameToView = (TextView) view.findViewById(R.id.name_vendor);
                final TextView priceToView = (TextView) view.findViewById(R.id.price_vendor);

                final ProductOnSale product = (ProductOnSale) getItem(position);


                priceToView.setText(""+product.getPrice());
                Log.e("TOTALE","sadasdasds" + product.getProduct_id());

                nameToView.setText((dbA.getUserFromProductOnSale(product.getUser_id())).getUsername());

                return view;
            }
        };


        Product p = dbA.getProductFromId((Long)getIntent().getSerializableExtra(Product.PRODUCT_LIST_EXTRA));

        nameToView.setText(p.getName());
        expansionToView.setText(p.getExpansion());
        rarityToView.setText(p.getRarity());
        ruleToView.setText(p.getRule());

        Picasso.get().load(""+p.getImg()).into(imgToView);


        mListView.setAdapter(mAdapter);
        DatabaseAccess dbA = null;
        try {
            dbA = DatabaseAccess.getInstance(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<ProductOnSale> products = dbA.getProductOnSaleFromProductId((Long)getIntent().getSerializableExtra(Product.PRODUCT_LIST_EXTRA));
        mProduct.clear();
        mProduct.addAll(products);
        mListView.setAdapter(mAdapter);


    }
}