package com.example.MagicShop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.MagicShop.model.DatabaseAccess;
import com.example.MagicShop.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ShowProducts extends AppCompatActivity {
    private DatabaseAccess dbA;
    private ListView mListView;
    private List<Product> mProduct = new LinkedList<>();
    private ListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_first_local_data_show);
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

                final TextView rarityToView = (TextView) view.findViewById(R.id.rarityT);
                final TextView priceToView = (TextView) view.findViewById(R.id.price_product_2);
                final TextView expansionToView = (TextView) view.findViewById(R.id.expansionT);

                final Product product = (Product) getItem(position);


                nameToView.setText(getString(R.string.card_name)+": "+product.getName());
                expansionToView.setText(getString(R.string.expansion)+": "+product.getExpansion());
                rarityToView.setText(getString(R.string.rarity)+": "+product.getRarity());
                priceToView.setText("");
                Picasso.get().load(""+product.getImg()).into(imageToView);
                return view;
            }
        };

        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Product product = (Product) mAdapter.getItem(position);

                final Intent showProductOnSaleIntent = new Intent(
                        ShowProducts.this,ShowProductOnSaleSeller.class);
                showProductOnSaleIntent.putExtra(Product.PRODUCT_LIST_EXTRA, product.getId());
                startActivity(showProductOnSaleIntent);
            }
        });

    }



    @Override
    protected void onStart(){

        super.onStart();

        List<Product> products = new ArrayList<>();
        List<Long> ids = (List<Long>) getIntent().getSerializableExtra(Product.PRODUCT_LIST_EXTRA);
        dbA = DatabaseAccess.getDb();
        for (long id : ids){
            products.add(dbA.getProductFromId(id));
        }
        mProduct.clear();
        mProduct.addAll(products);
        mListView.setAdapter(mAdapter);
    }
}