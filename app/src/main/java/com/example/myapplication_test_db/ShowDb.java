 package com.example.myapplication_test_db;

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

import com.example.myapplication_test_db.model.DatabaseAccess;
import com.example.myapplication_test_db.model.Product;
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
                return product.id;
            }

            @Override
            public View getView(int position, View view, ViewGroup parent) {
                if(view == null){
                    view = getLayoutInflater().inflate(R.layout.custom_list_item, null);
                }
                final TextView nameToView = (TextView) view.findViewById(R.id.nameT);
                final ImageView imageToView = (ImageView)view.findViewById(R.id.imgT);

                final Product product = (Product) getItem(position);

                nameToView.setText(product.name);
                Picasso.get().load(""+product.img).into(imageToView);
                Log.e("TEST",""+nameToView.toString());
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
        List<Product> products = dbA.getAllProduct();
        mProduct.clear();
        mProduct.addAll(products);
        mListView.setAdapter(mAdapter);
    }
}