package com.example.MagicShop;


import android.app.Activity;
import android.app.Dialog;
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
import androidx.fragment.app.DialogFragment;

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;




public class ShowProductOnSaleSeller extends AppCompatActivity {

    private DatabaseAccess dbA;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private TextView nameText;
    private TextView expansionText;
    private TextView rarityText;
    private TextView ruleText;
    private ImageView imageCard;
    private ListView mListView;
    private List<ProductOnSale> listProductOnSale = new LinkedList<>();
    private ListAdapter mAdapter;
    private User userLogged;
    private Product product;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product_on_sale_seller);
        if (getResources().getBoolean(R.bool.dual_pane)){
            mListView = (ListView)findViewById(R.id.fragment_seller_area).findViewById(R.id.list_seller_final);
            nameText = (TextView)findViewById(R.id.fragment_detail_card).findViewById(R.id.name_view);
            expansionText = (TextView)findViewById(R.id.fragment_detail_card).findViewById(R.id.expansion_view);
            rarityText = (TextView)findViewById(R.id.fragment_detail_card).findViewById(R.id.rarity_view);
            ruleText = (TextView)findViewById(R.id.fragment_detail_card).findViewById(R.id.rule_view);
            imageCard = (ImageView) findViewById(R.id.fragment_detail_card).findViewById(R.id.image_view);
        }else{
            nameText = (TextView)findViewById(R.id.fragment_detail_card).findViewById(R.id.name_view);
            expansionText = (TextView)findViewById(R.id.fragment_detail_card).findViewById(R.id.expansion_view);
            rarityText = (TextView)findViewById(R.id.fragment_detail_card).findViewById(R.id.rarity_view);
            ruleText = (TextView)findViewById(R.id.fragment_detail_card).findViewById(R.id.rule_view);
            imageCard = (ImageView) findViewById(R.id.fragment_detail_card).findViewById(R.id.image_view);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
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
                final Button addPhoto = (Button) view.findViewById(R.id.add_photo);
                final Button showPhoto = (Button) view.findViewById(R.id.show_photo);
                final Button shopProductOnSale = (Button) view.findViewById(R.id.shop_product);

                addPhoto.setVisibility(View.INVISIBLE);
                showPhoto.setVisibility(View.INVISIBLE);
                shopProductOnSale.setVisibility(View.INVISIBLE);


                ProductOnSale productOnSale = (ProductOnSale) getItem(position);

                User seller = dbA.getUsersFromProductOnSale(productOnSale);

                nameToView.setText(seller.getUsername());
                priceToView.setText(""+productOnSale.getPrice());

                if(productOnSale.getPhoto().equals("null")){
                } else {
                    showPhoto.setVisibility(View.VISIBLE);
                    final ImagePopup imagePopup = new ImagePopup(ShowProductOnSaleSeller.this);
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
                        addPhoto.setVisibility(View.VISIBLE);
                        addPhoto.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                    File photoFile = null;
                                    try {
                                        photoFile = createImageFile(productOnSale.getId());
                                    } catch (IOException ex) {
                                    }
                                    if (photoFile != null) {
                                        Uri photoURI = FileProvider.getUriForFile(ShowProductOnSaleSeller.this,
                                                ShowProductOnSaleSeller.this.getApplicationContext().getPackageName() + ".provider",
                                                photoFile);
                                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

                                    }
                                }
                            }
                        });
                    } else {
                        //NON SEI IL VENDITORE PUO COMPRARE
                        shopProductOnSale.setVisibility(View.VISIBLE);
                        shopProductOnSale.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // QUANDO COMPRI
                                showShopDialog(ShowProductOnSaleSeller.this, productOnSale, userLogged);
                            }
                        });

                    }
                }
                return view;
            }
        };

        final Button sellButton = (Button)findViewById(R.id.fragment_detail_card).findViewById(R.id.sell_product);

        dbA = DatabaseAccess.getDb();
        product = dbA.getProductFromId((Long)getIntent().getSerializableExtra(Product.PRODUCT_LIST_EXTRA));

        if(PreferenceUtils.getId(this)!=null){
            userLogged = dbA.getUserFromId(PreferenceUtils.getId(this));
            sellButton.setVisibility(View.VISIBLE);
            sellButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSellDialog(ShowProductOnSaleSeller.this, product, userLogged);
                }
            });
        }

        List<ProductOnSale> products = dbA.getAllProductOnSaleFromProduct(product);
        listProductOnSale.clear();
        listProductOnSale.addAll(products);
        if(getResources().getBoolean(R.bool.dual_pane)) {
            mListView.setAdapter(mAdapter);
        }
        nameText.setText(product.getName());
        expansionText.setText(product.getExpansion());
        rarityText.setText(product.getRarity());
        ruleText.setText(product.getRule());
        Picasso.get().load(""+product.getImg()).into(imageCard);
    }


    public void showShopDialog(Context c, ProductOnSale p, User u) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setMessage(R.string.confirm_shop_product)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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

    private void showSellDialog(Context c, Product p, User u) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle(getString(R.string.title_price)+"\n"+p.getName())
                .setMessage((getString(R.string.message_price)))
                .setView(taskEditText)
                .setPositiveButton(getString(R.string.sell_price), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Long price = null;
                        try{
                            price = Long.valueOf(String.valueOf(taskEditText.getText()));
                            if(price != 0){
                                dbA.sellProductFromUser(p, u, price);
                                onStart();
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.error_price) , Toast.LENGTH_SHORT).show();
                                showSellDialog(c,p,u);
                            }

                        } catch (Exception ex){
                            Toast.makeText(getApplicationContext(), getString(R.string.error_price), Toast.LENGTH_SHORT).show();
                            showSellDialog(c,p,u);
                        }
                    }
                })
                .setNegativeButton(getString(R.string.undo_price), null)
                .create();
        dialog.show();
    }


    private File createImageFile(String product) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_id=[" + product+"]";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap, String name, final ProductOnSale p){
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap = resizeImageForImageView(bitmap);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        StorageReference filepath = storageReference.child("product_on_sale").child(name);
        byte[] data = baos.toByteArray();

        filepath.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        dbA.addPhotoToProductOnSale(p, uri.toString());
                        onStart();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

    }

    public Bitmap resizeImageForImageView(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
          /*
        int scaleSize =1024;
        Bitmap resizedBitmap = null;
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int newWidth = -1;
        int newHeight = -1;
        float multFactor = -1.0F;
        if(originalHeight > originalWidth) {
            newHeight = scaleSize ;
            multFactor = (float) originalWidth/(float) originalHeight;
            newWidth = (int) (newHeight*multFactor);
        } else if(originalWidth > originalHeight) {
            newWidth = scaleSize ;
            multFactor = (float) originalHeight/ (float)originalWidth;
            newHeight = (int) (newWidth*multFactor);
        } else if(originalHeight == originalWidth) {
            newHeight = scaleSize ;
            newWidth = scaleSize ;
        }
        resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);*/
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final ProductOnSale prodOnSale;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                File file = new File(currentPhotoPath);
                String productOnSaleID = file.getName().substring(file.getName().indexOf("[") + 1, file.getName().indexOf("]"));
                prodOnSale = dbA.getProductOnSaleFromId(productOnSaleID);
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(ShowProductOnSaleSeller.this.getContentResolver(), Uri.fromFile(file));
                    encodeBitmapAndSaveToFirebase(bitmap, ""+file.getName(), prodOnSale);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}