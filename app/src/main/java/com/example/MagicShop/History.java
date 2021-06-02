package com.example.MagicShop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.MagicShop.model.DatabaseAccess;
import com.example.MagicShop.model.HistoryRecord;
import com.example.MagicShop.model.Product;
import com.example.MagicShop.model.User;
import com.example.MagicShop.utils.PreferenceUtils;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

public class History extends AppCompatActivity {

    private ListView mListView_sell;
    private ListView mListView_shop;
    private DatabaseAccess dbA;
    private List<HistoryRecord> mProduct_sell = new LinkedList<>();
    private List<HistoryRecord> mProduct_shop = new LinkedList<>();
    private ListAdapter mAdapter_sell;
    private ListAdapter mAdapter_shop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart(){
        super.onStart();
        setContentView(R.layout.activity_history);
        mListView_sell = findViewById(R.id.listView_history_sell);

        mAdapter_sell = new BaseAdapter() {
            @Override
            public int getCount() { return mProduct_sell.size(); }

            @Override
            public Object getItem(int position) {
                return mProduct_sell.get(position);
            }

            public long getItemId(int position) {
                return -1;
            }

            @Override
            public View getView(int position, View view, ViewGroup parent) {
                view = getLayoutInflater().inflate(R.layout.list_history_shop, null);
                TextView nameSeller = (TextView)view.findViewById(R.id.name_seller);
                TextView priceProduct = (TextView)view.findViewById(R.id.price_product);
                TextView nameProduct = (TextView)view.findViewById(R.id.name_product);
                TextView timeSell = (TextView)view.findViewById(R.id.time_sell);

                HistoryRecord hR = (HistoryRecord) getItem(position);
                Product product = dbA.getProductFromId(hR.getProduct_id());

                nameSeller.setText(hR.getUser().getmUserName());
                priceProduct.setText(""+hR.getPrice());
                nameProduct.setText(product.getName());

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                timeSell.setText(simpleDateFormat.format(hR.getDate()));

                return view;
            }
        };

        mListView_shop = findViewById(R.id.listView_history_shop);
        mAdapter_shop = new BaseAdapter() {
            @Override
            public int getCount() { return mProduct_shop.size(); }

            @Override
            public Object getItem(int position) {
                return mProduct_shop.get(position);
            }

            public long getItemId(int position) {
                return -1;
            }

            @Override
            public View getView(int position, View view, ViewGroup parent) {
                view = getLayoutInflater().inflate(R.layout.list_history_shop, null);
                TextView nameSeller = (TextView)view.findViewById(R.id.name_seller);
                TextView priceProduct = (TextView)view.findViewById(R.id.price_product);
                TextView nameProduct = (TextView)view.findViewById(R.id.name_product);
                TextView timeSell = (TextView)view.findViewById(R.id.time_sell);

                HistoryRecord hR = (HistoryRecord) getItem(position);
                Product product = dbA.getProductFromId(hR.getProduct_id());

                nameSeller.setText(hR.getUser().getmUserName());
                priceProduct.setText(""+hR.getPrice());
                nameProduct.setText(product.getName());

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                timeSell.setText(simpleDateFormat.format(hR.getDate()));

                return view;
            }
        };

        dbA = DatabaseAccess.getDb();
        User userLogged = dbA.getUserFromId(PreferenceUtils.getId(this));

        List<HistoryRecord> sell = dbA.getHistorySell(userLogged);
        mProduct_sell.clear();
        mProduct_sell.addAll(sell);

        List<HistoryRecord> shop = dbA.getHistoryShop(userLogged);
        mProduct_shop.clear();
        mProduct_shop.addAll(shop);


        mListView_sell.setAdapter(mAdapter_sell);
        mListView_shop.setAdapter(mAdapter_shop);

    }



}