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
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Base64;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.example.MagicShop.model.DatabaseAccess;
import com.example.MagicShop.model.Product;
import com.example.MagicShop.model.ProductOnSale;
import com.example.MagicShop.model.User;
import com.example.MagicShop.utils.PreferenceUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
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
                    final Button showPhoto = (Button) view.findViewById(R.id.show_photo);
                    if(product.getPhoto().equals("null")){
                        showPhoto.setVisibility(View.INVISIBLE);
                    } else {
                        showPhoto.setVisibility(View.VISIBLE);
                        final ImagePopup imagePopup = new ImagePopup(ShowProductOnSaleSeller.this);
                        imagePopup.initiatePopupWithPicasso(product.getPhoto());
                        imagePopup.setWindowHeight(800); // Optional
                        imagePopup.setWindowWidth(800); // Optional
                        imagePopup.setBackgroundColor(Color.BLACK);  // Optional
                        imagePopup.setFullScreen(true); // Optional
                        imagePopup.setHideCloseIcon(true);  // Optional
                        imagePopup.setImageOnClickClose(true);  // Optional
                        showPhoto.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                imagePopup.viewPopup();
                            }
                        });
                    }

                    addPhoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                File photoFile = null;
                                try {
                                    photoFile = createImageFile(product.getId());
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
        int scaleSize =1024;
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
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
        resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
        return Bitmap.createBitmap(resizedBitmap, 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight(), matrix, true);
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