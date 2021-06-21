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
import com.example.MagicShop.model.ProductOnSale;
import com.example.MagicShop.utils.PreferenceUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyProductsActivity extends AppCompatActivity {


    private DatabaseAccess dbA;
    private ListView mListView;
    private List<String> mProduct = new LinkedList<>();
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
                String productOnSaleId = (String) getItem(position);
                return -1;
            }

            @Override
            public View getView(int position, View view, ViewGroup parent) {
                if(view == null){
                    view = getLayoutInflater().inflate(R.layout.list_item_img_and_text, null);
                }
                final TextView nameToView = (TextView) view.findViewById(R.id.nameT);
                final TextView rarityToView = (TextView) view.findViewById(R.id.rarityT);
                final TextView priceToView = (TextView) view.findViewById(R.id.price_product_2);
                final TextView expansionToView = (TextView) view.findViewById(R.id.expansionT);
                final ImageView imageToView = (ImageView)view.findViewById(R.id.imgT);
                final ProductOnSale productOnSale = (ProductOnSale) dbA.getProductOnSaleFromId(""+getItem(position));
                final Product product = (Product) dbA.getProductFromId(productOnSale.getProduct_id());
                nameToView.setText(getString(R.string.card_name)+": "+product.getName());
                expansionToView.setText(getString(R.string.expansion)+": "+product.getExpansion());
                rarityToView.setText(getString(R.string.rarity)+": "+product.getRarity());
                priceToView.setText(getString(R.string.card_price)+": "+productOnSale.getPrice()+ " €");
                Picasso.get().load(""+product.getImg()).into(imageToView);
                return view;
            }
        };

        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String productOnSaleId = ""+mAdapter.getItem(position);
                final Intent showProductOnSaleIntent = new Intent(
                        MyProductsActivity.this,ProductAreaActivity.class);
                //showProductOnSaleIntent.putExtra(Product.PRODUCT_LIST_EXTRA, product.getId());
                showProductOnSaleIntent.putExtra("product_on_sale_id",productOnSaleId);
                //showProductOnSaleIntent.putExtra("user_id", getIntent().getExtras().getString("user_id"));
                //showProductOnSaleIntent.putExtra("card_name", product.getName());
                startActivity(showProductOnSaleIntent);
                finish();
            }
        });
    }



    @Override
    protected void onStart(){
        super.onStart();
        List<Product> products = new ArrayList<>();
        List<String> ids = (List<String>) getIntent().getSerializableExtra("ids");
        dbA = DatabaseAccess.getDb();
        String userId = PreferenceUtils.getId(this);
        //List<Long> ids = (List<Long>) dbA.getProductsSellingIdsFromUserId(userId);
        //Log.e("debug", String.valueOf(ids.isEmpty()));
        mProduct.clear();
        mProduct.addAll(ids);
        mListView.setAdapter(mAdapter);
        /*
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
        }*/
    }
}