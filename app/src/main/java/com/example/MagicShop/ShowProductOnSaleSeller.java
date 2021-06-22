package com.example.MagicShop;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.example.MagicShop.model.DatabaseAccess;
import com.example.MagicShop.model.Product;
import com.example.MagicShop.model.ProductOnSale;
import com.example.MagicShop.model.User;
import com.example.MagicShop.utils.PreferenceUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;




public class ShowProductOnSaleSeller extends AppCompatActivity implements OnNavigationListener {

    private DatabaseAccess dbA;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product_on_sale_seller);
        dbA = DatabaseAccess.getDb();
        if (getResources().getBoolean(R.bool.dual_pane)){
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            Fragment fragment = new FragmentDetailcard();
            transaction.replace(R.id.fragment_detail_card, fragment);

            Fragment seller = new FragmentSellerCards();
            transaction.replace(R.id.fragment_seller_area, seller);

            transaction.commit();
        }else{
            Fragment fragment = new FragmentDetailcard();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.fragment_main, fragment);
            transaction.commit();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        dbA = DatabaseAccess.getDb();
        if (getResources().getBoolean(R.bool.dual_pane)){
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            Fragment fragment = new FragmentDetailcard();
            transaction.replace(R.id.fragment_detail_card, fragment);

            Fragment seller = new FragmentSellerCards();
            transaction.replace(R.id.fragment_seller_area, seller);

            transaction.commit();
        }else{
            Fragment fragment = new FragmentDetailcard();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.fragment_main, fragment);
            transaction.commit();
        }
    }

    @Override
    public void newSell() {
        //update the data in case of data update
        onStart();
    }

    @Override
    public void onButtonDetails() {
        Fragment fragment = new FragmentDetailcard();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_main, fragment);
        transaction.commit();
    }

    @Override
    public void onButtonSeller() {
        Fragment fragment = new FragmentSellerCards();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_main, fragment);
        transaction.commit();
    }
}