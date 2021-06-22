package com.example.MagicShop;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
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

public class FragmentSellerCards extends Fragment {
    private DatabaseAccess dbA;
    private ListView mListView;
    private List<ProductOnSale> listProductOnSale = new LinkedList<>();
    private ListAdapter mAdapter;
    private User userLogged;
    private Product product;
    private OnNavigationListener listener;

    @Override
    public void onStart() {
        super.onStart();
        View view =getView();
        Log.i("DEBUG","Sei nel Fragments dei Seller");
        mListView = (ListView) view.findViewById(R.id.list_seller_final);

        view.findViewById(R.id.text_username).setVisibility(View.VISIBLE);
        view.findViewById(R.id.text_price).setVisibility(View.VISIBLE);

        dbA = DatabaseAccess.getDb();
        product = dbA.getProductFromId((Long)getActivity().getIntent().getSerializableExtra(Product.PRODUCT_LIST_EXTRA));

        mAdapter = new BaseAdapter() {
            @Override
            public int getCount() { return listProductOnSale.size(); }

            @Override
            public Object getItem(int position) {
                return listProductOnSale.get(position);
            }

            @Override
            public long getItemId(int position) {
                ProductOnSale product = (ProductOnSale) getItem(position);
                return -1;
            }

            @Override
            public View getView(int position, View view, ViewGroup parent) {
                if(view == null){
                    view = getLayoutInflater().inflate(R.layout.custom_seller, null);
                }

                final TextView nameToView = (TextView) view.findViewById(R.id.name_seller);
                final TextView priceToView = (TextView) view.findViewById(R.id.price_seller);
                final Button editCard = (Button) view.findViewById(R.id.edit_card_details);
                final Button showPhoto = (Button) view.findViewById(R.id.show_photo);
                final Button shopProductOnSale = (Button) view.findViewById(R.id.shop_product);

                editCard.setVisibility(View.INVISIBLE);
                showPhoto.setVisibility(View.INVISIBLE);
                shopProductOnSale.setVisibility(View.INVISIBLE);


                ProductOnSale productOnSale = (ProductOnSale) getItem(position);

                User seller = dbA.getUsersFromProductOnSale(productOnSale);

                nameToView.setText(seller.getUsername());
                priceToView.setText(""+productOnSale.getPrice());

                if(productOnSale.getPhoto().equals("null")){
                } else {
                    showPhoto.setVisibility(View.VISIBLE);
                    final ImagePopup imagePopup = new ImagePopup(getActivity());
                    imagePopup.initiatePopupWithPicasso(productOnSale.getPhoto());

                    imagePopup.setBackgroundColor(Color.BLACK);  // Optional
                    imagePopup.setFullScreen(false); // Optional
                    imagePopup.setHideCloseIcon(true);  // Optional
                    imagePopup.setImageOnClickClose(false);  // Optional
                    imagePopup.setHideCloseIcon(true);
                    imagePopup.setLayoutOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(productOnSale.getPhoto()));
                            startActivity(browserIntent);
                            return false;
                        }
                    });
                    showPhoto.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            imagePopup.viewPopup();
                        }
                    });
                }

                if(userLogged != null){
                    //SEI LOGGATO
                    if (userLogged.getId().equals(seller.getId())){
                        //SEI IL VENDITORE
                        editCard.setVisibility(View.VISIBLE);
                        editCard.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("DEBUG", "Edit card");
                                final Intent editcard = new Intent(getActivity(), ProductAreaActivity.class);
                                editcard.putExtra("product_on_sale_id",""+productOnSale.getId());
                                startActivity(editcard);
                                getActivity().finish();
                            }
                        });

                    } else {
                        //NON SEI IL VENDITORE PUO COMPRARE
                        shopProductOnSale.setVisibility(View.VISIBLE);
                        shopProductOnSale.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // QUANDO COMPRI
                                showShopDialog(getActivity(), productOnSale, userLogged);
                            }
                        });

                    }
                }
                return view;
            }
        };


        if(PreferenceUtils.getId(getActivity().getApplicationContext())!=null) {
            userLogged = dbA.getUserFromId(PreferenceUtils.getId(getActivity().getApplicationContext()));
        }

        List<ProductOnSale> products = dbA.getAllProductOnSaleFromProduct(product);
        listProductOnSale.clear();
        listProductOnSale.addAll(products);
        mListView.setAdapter(mAdapter);

        if (!getResources().getBoolean(R.bool.dual_pane)) {
            //Abilito il bottonne con l'evento per passare all'activity con il singolo fragment
            final Button gotoDetail = (Button) view.findViewById(R.id.goto_detail_card);
            gotoDetail.setVisibility(View.VISIBLE);
            gotoDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //crea tramirte l'interfaccia la notifica di cambiare nuovamente fragmente nell'activity principale
                    listener = (OnNavigationListener) getActivity();
                    listener.onButtonDetails();
                }
            });
        }else{
            final Button gotoDetails = (Button) view.findViewById(R.id.goto_detail_card);
            gotoDetails.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_seller_cards, container, false);
        Log.i("DEBUG","Sei nel Fragments dei Seller");
        mListView = (ListView) view.findViewById(R.id.list_seller_final);

        view.findViewById(R.id.text_username).setVisibility(View.VISIBLE);
        view.findViewById(R.id.text_price).setVisibility(View.VISIBLE);

        dbA = DatabaseAccess.getDb();
        product = dbA.getProductFromId((Long)getActivity().getIntent().getSerializableExtra(Product.PRODUCT_LIST_EXTRA));

        mAdapter = new BaseAdapter() {
            @Override
            public int getCount() { return listProductOnSale.size(); }

            @Override
            public Object getItem(int position) {
                return listProductOnSale.get(position);
            }

            @Override
            public long getItemId(int position) {
                ProductOnSale product = (ProductOnSale) getItem(position);
                return -1;
            }

            @Override
            public View getView(int position, View view, ViewGroup parent) {
                if(view == null){
                    view = getLayoutInflater().inflate(R.layout.custom_seller, null);
                }

                final TextView nameToView = (TextView) view.findViewById(R.id.name_seller);
                final TextView priceToView = (TextView) view.findViewById(R.id.price_seller);
                final Button editCard = (Button) view.findViewById(R.id.edit_card_details);
                final Button showPhoto = (Button) view.findViewById(R.id.show_photo);
                final Button shopProductOnSale = (Button) view.findViewById(R.id.shop_product);

                editCard.setVisibility(View.INVISIBLE);
                showPhoto.setVisibility(View.INVISIBLE);
                shopProductOnSale.setVisibility(View.INVISIBLE);


                ProductOnSale productOnSale = (ProductOnSale) getItem(position);

                User seller = dbA.getUsersFromProductOnSale(productOnSale);

                nameToView.setText(seller.getUsername());
                priceToView.setText(""+productOnSale.getPrice());

                if(productOnSale.getPhoto().equals("null")){
                } else {
                    showPhoto.setVisibility(View.VISIBLE);
                    final ImagePopup imagePopup = new ImagePopup(getActivity());
                    imagePopup.initiatePopupWithPicasso(productOnSale.getPhoto());

                    imagePopup.setBackgroundColor(Color.BLACK);  // Optional
                    imagePopup.setFullScreen(false); // Optional
                    imagePopup.setHideCloseIcon(true);  // Optional
                    imagePopup.setImageOnClickClose(false);  // Optional
                    imagePopup.setHideCloseIcon(true);
                    imagePopup.setLayoutOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(productOnSale.getPhoto()));
                            startActivity(browserIntent);
                            return false;
                        }
                    });
                    showPhoto.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            imagePopup.viewPopup();
                        }
                    });
                }

                if(userLogged != null){
                    //SEI LOGGATO
                    if (userLogged.getId().equals(seller.getId())){
                        //SEI IL VENDITORE
                        editCard.setVisibility(View.VISIBLE);
                        editCard.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("DEBUG", "Edit card");
                                final Intent editcard = new Intent(getActivity(), ProductAreaActivity.class);
                                editcard.putExtra("product_on_sale_id",""+productOnSale.getId());
                                startActivity(editcard);
                                getActivity().finish();
                            }
                        });

                    } else {
                        //NON SEI IL VENDITORE PUO COMPRARE
                        shopProductOnSale.setVisibility(View.VISIBLE);
                        shopProductOnSale.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // QUANDO COMPRI
                                showShopDialog(getActivity(), productOnSale, userLogged);
                            }
                        });

                    }
                }
                return view;
            }
        };


        if(PreferenceUtils.getId(getActivity().getApplicationContext())!=null) {
            userLogged = dbA.getUserFromId(PreferenceUtils.getId(getActivity().getApplicationContext()));
        }

        List<ProductOnSale> products = dbA.getAllProductOnSaleFromProduct(product);
        listProductOnSale.clear();
        listProductOnSale.addAll(products);
        mListView.setAdapter(mAdapter);

        if (!getResources().getBoolean(R.bool.dual_pane)) {
            //Abilito il bottonne con l'evento per passare all'activity con il singolo fragment
            final Button gotoDetail = (Button) view.findViewById(R.id.goto_detail_card);
            gotoDetail.setVisibility(View.VISIBLE);
            gotoDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //crea tramirte l'interfaccia la notifica di cambiare nuovamente fragmente nell'activity principale
                    listener = (OnNavigationListener) getActivity();
                    listener.onButtonDetails();
                }
            });
        }else{
            final Button gotoDetails = (Button) view.findViewById(R.id.goto_detail_card);
            gotoDetails.setVisibility(View.GONE);
        }

        return view;
    }

    public void showShopDialog(Context c, ProductOnSale p, User u) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setMessage(R.string.confirm_shop_product)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), R.string.success_product_buy,
                                Toast.LENGTH_SHORT).show();
                        dbA.shopProduct(p,u);
                        // FIRE ZE MISSILES!
                        onStart();
                    }
                })
                .setNegativeButton(R.string.abort, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }
}