package com.example.MagicShop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.MagicShop.model.DatabaseAccess;
import com.example.MagicShop.model.Product;
import com.example.MagicShop.utils.PreferenceUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyProductsActivity extends AppCompatActivity {


    private DatabaseAccess dbA;
    private ListView mListView;
    private List<Product> mProduct = new LinkedList<>();
    private ListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_products);
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
                return -1;
            }

            @Override
            public View getView(int position, View view, ViewGroup parent) {
                if(view == null){
                    view = getLayoutInflater().inflate(R.layout.list_item_img_and_text, null);
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
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Product product = (Product) mAdapter.getItem(position);
                Log.e("Products list debug", "product id: " + product.getId() + " product name: " + product.getName());

                final Intent showProductOnSaleIntent = new Intent(
                        MyProductsActivity.this,ProductAreaActivity.class);
                showProductOnSaleIntent.putExtra(Product.PRODUCT_LIST_EXTRA, product.getId());
                showProductOnSaleIntent.putExtra("card_Id", product.getId());
                showProductOnSaleIntent.putExtra("user_id", getIntent().getExtras().getString("user_id"));
                showProductOnSaleIntent.putExtra("card_name", product.getName());
                startActivity(showProductOnSaleIntent);
            }
        });

    }



    @Override
    protected void onStart(){

        super.onStart();

        List<Product> products = new ArrayList<>();
        // List<Long> ids = (List<Long>) getIntent().getSerializableExtra(Product.PRODUCT_LIST_EXTRA);
        dbA = DatabaseAccess.getDb();
        String userId = PreferenceUtils.getId(this);
        List<Long> ids = (List<Long>) dbA.getProductsSellingIdsFromUserId(userId);
        //Log.e("debug", String.valueOf(ids.isEmpty()));
        if(!ids.isEmpty()){
            for (long id : ids){
                products.add(dbA.getProductFromId(id));
            }
            mProduct.clear();
            mProduct.addAll(products);
            mListView.setAdapter(mAdapter);

        }
        else{
            finish();
        }
    }
}