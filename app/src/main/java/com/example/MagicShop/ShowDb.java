 package com.example.MagicShop;

import androidx.appcompat.app.AppCompatActivity;

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

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

 public class ShowDb extends AppCompatActivity {
     private ListView mListView;
    private List<Product> mProduct = new LinkedList<>();
    private ListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_first_local_data);
        mListView = (ListView)findViewById(R.id.listViewT);

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
                    view = getLayoutInflater().inflate(R.layout.custom_list_item, null);
                }
                final TextView nameToView = (TextView) view.findViewById(R.id.nameT);
                final ImageView imageToView = (ImageView)view.findViewById(R.id.imgT);

                final Product product = (Product) getItem(position);

                nameToView.setText(product.getName());
                Picasso.get().load(""+product.getImg()).into(imageToView);
                return view;
            }
        };

        mListView.setAdapter(mAdapter);

    }

    @Override
     protected void onStart(){
        super.onStart();
        DatabaseAccess dbA = null;
        try {
            dbA = DatabaseAccess.getInstance(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Product> products = dbA.getAllProducts();
        mProduct.clear();
        mProduct.addAll(products);
        mListView.setAdapter(mAdapter);
        List<ProductOnSale> prodss  = dbA.getAllProductOnSaleFromProduct(products.get(0));

        Log.e("PRODUCT_ON_SALE_0", ""+ dbA.getProdutFromProductOnSale(prodss.get(0)).getName());

    }
}