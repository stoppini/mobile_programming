package com.example.MagicShop;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.MagicShop.model.DatabaseAccess;
import com.example.MagicShop.model.Product;
import com.example.MagicShop.model.ProductOnSale;
import com.example.MagicShop.model.User;
import com.example.MagicShop.utils.PreferenceUtils;

import com.squareup.picasso.Picasso;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;




public class ShowProductOnSaleSeller extends AppCompatActivity {
    private DatabaseAccess dbA;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private TextView nameToView;
    private TextView expansionToView;
    private TextView rarityToView;
    private TextView ruleToView;
    private ImageView imgToView;
    private ListView mListView;
    private List<ProductOnSale> mProduct = new LinkedList<>();
    private ListAdapter mAdapter;
    private Context context = this;
    private User u;
    private Product p;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product_on_sale_seller);
        mListView = (ListView)findViewById(R.id.listViewShowUsers);
        nameToView = (TextView)findViewById(R.id.nameView);
        expansionToView = (TextView)findViewById(R.id.expansionView);
        rarityToView = (TextView)findViewById(R.id.rarityView);
        ruleToView = (TextView)findViewById(R.id.ruleView);
        imgToView = (ImageView) findViewById(R.id.imgView);

    }

    @Override
    protected void onStart(){

        super.onStart();
        mAdapter = new BaseAdapter() {
            @Override
            public int getCount() { return mProduct.size(); }

            @Override
            public Object getItem(int position) {
                return mProduct.get(position);
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

                final ProductOnSale product = (ProductOnSale) getItem(position);
                User seller = dbA.getUsersFromProductOnSale(product);

                nameToView.setText(seller.getUsername());
                if(seller.getId().equals(u.getId())){
                    final Button addPhoto = (Button) view.findViewById(R.id.adding_photo);
                    final ImageView img = (ImageView) view.findViewById(R.id.photo);


                    if(product.getPhoto()!=null){
                        Log.e("asdasd",""+product.getPhoto());
                        Picasso.get().load(""+product.getPhoto()).into(img);
                    }
                    addPhoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            // Ensure that there's a camera activity to handle the intent
                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                // Create the File where the photo should go
                                File photoFile = null;
                                try {
                                    photoFile = createImageFile(product.getId());
                                } catch (IOException ex) {
                                    // Error occurred while creating the File
                                }
                                // Continue only if the File was successfully created
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
                }
                priceToView.setText(""+product.getPrice());
                return view;
            }
        };


        dbA = DatabaseAccess.getDb();
        p = dbA.getProductFromId((Long)getIntent().getSerializableExtra(Product.PRODUCT_LIST_EXTRA));

        if(PreferenceUtils.isLogged(this)){
            u = dbA.getUserFromId(PreferenceUtils.getId(this));
            final Button sellButton = (Button)findViewById(R.id.sell_product);
            sellButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showForgotDialog(context, p, u);
                }
            });

        }

        List<ProductOnSale> products = dbA.getAllProductOnSaleFromProduct(p);
        mProduct.clear();
        mProduct.addAll(products);
        mListView.setAdapter(mAdapter);
        nameToView.setText(p.getName());
        expansionToView.setText(p.getExpansion());
        rarityToView.setText(p.getRarity());
        ruleToView.setText(p.getRule());
        Picasso.get().load(""+p.getImg()).into(imgToView);
    }


    private void showForgotDialog(Context c, Product p, User u) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Sell the product "+p.getName())
                .setMessage("Enter the amount in Euro")
                .setView(taskEditText)
                .setPositiveButton("Sell", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Long price = Long.valueOf(String.valueOf(taskEditText.getText()));
                        dbA.sellProductFromUser(p, u, price);
                        onStart();
                    }
                })
                .setNegativeButton("Undo", null)
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
                    dbA.encodeBitmapAndSaveToFirebase(bitmap, ""+file.getName(), prodOnSale);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}