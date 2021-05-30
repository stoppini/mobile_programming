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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

 public class ShowDb extends AppCompatActivity { private ListView mListView;
    private DatabaseAccess dbA;
    private List<Product> mProduct = new LinkedList<>();
    private ListAdapter mAdapter;


     DatabaseReference database = FirebaseDatabase.getInstance().getReference();



     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
     protected void onStart(){
        super.onStart();
        setContentView(R.layout.simple_first_local_data);
        mListView = (ListView)findViewById(R.id.listViewT);

        mAdapter = new BaseAdapter() {
            @Override
            public int getCount() { return mProduct.size(); }

            @Override
            public Object getItem(int position) {
                return mProduct.get(position);
            }

            public long getItemId(int position) {
                return -1;
            }

            @Override
            public View getView(int position, View view, ViewGroup parent) {
                if(view == null){
                    view = getLayoutInflater().inflate(R.layout.custom_list_item, null);
                }
                final TextView nameToView = (TextView) view.findViewById(R.id.nameT);
                final ImageView imageToView = (ImageView)view.findViewById(R.id.imgT);
                final TextView ruleToView = (TextView) view.findViewById(R.id.rule);

                final Product product = (Product) getItem(position);

                nameToView.setText(product.getName());
                Picasso.get().load(""+product.getImg()).into(imageToView);
                ruleToView.setText(product.getRule());
                return view;
            }
        };

        mListView.setAdapter(mAdapter);
        dbA = DatabaseAccess.getDb();

        List<Product> prodss  = dbA.getAllProducts();
        mProduct.clear();
        mProduct.addAll(prodss);
        mListView.setAdapter(mAdapter);


        List<ProductOnSale> proasdfdss  = dbA.getAllProductsOnSale();
        for (ProductOnSale p : proasdfdss){
            Log.e("TEST", ""+p.getId());
        }



    }


}