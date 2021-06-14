package com.example.MagicShop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

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
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProductAreaActivity extends AppCompatActivity {

    private TextView price;
    private TextView cardName;
    private DatabaseAccess dbA;
    private String productOnSaleId;
    private String userId;
    private ProductOnSale productOnSale;
    private Button modifyPrice;
    private Button deleteProduct;
    private Button addPhoto;
    private Button showPhoto;
    private String currentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    //private final int INFORMATION_MODIFIED = 1;
    private static final String TAG_LOG = UserAreaActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_area);

        dbA = DatabaseAccess.getDb();
        productOnSaleId = getIntent().getExtras().getString("product_on_sale_id");
        userId = PreferenceUtils.getId(this);

        this.cardName = (TextView)findViewById(R.id.card_name);
        this.price = (TextView)findViewById(R.id.card_price);
        this.modifyPrice = (Button)findViewById(R.id.modify_button);
        this.deleteProduct = (Button)findViewById(R.id.eliminate_button);
        this.addPhoto = (Button)findViewById(R.id.add_photo);
        this.showPhoto = (Button)findViewById(R.id.show_photo);
        this.showPhoto.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        productOnSale = dbA.getProductOnSaleFromId(productOnSaleId);
        String nameProduct = dbA.getProductFromId(productOnSale.getProduct_id()).getName();
        cardName.setText(nameProduct);
        if (productOnSale.getPhoto().equals("null")) {
        } else {
            showPhoto.setVisibility(View.VISIBLE);
            final ImagePopup imagePopup = new ImagePopup(ProductAreaActivity.this);
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
        price.setText(String.valueOf(productOnSale.getPrice())+" €");
        modifyPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyPrice(ProductAreaActivity.this, productOnSale, nameProduct);
            }
        });
        deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct(ProductAreaActivity.this, productOnSale, nameProduct);
            }
        });
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
                        Uri photoURI = FileProvider.getUriForFile(ProductAreaActivity.this,
                                ProductAreaActivity.this.getApplicationContext().getPackageName() + ".provider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

                    }
                }
            }
        });
    }


    private void modifyPrice(Context c, ProductOnSale p, String name) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle(getString(R.string.title_modify_price) + name +".")
                .setMessage((getString(R.string.message_price)))
                .setView(taskEditText)
                .setPositiveButton(getString(R.string.modify_price_toast), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Float price = null;
                        try{
                            price = Float.parseFloat(String.valueOf(taskEditText.getText()));
                            DecimalFormat df = new DecimalFormat("#.##");
                            price = Float.parseFloat(df.format(price));
                            if(price >= 0.01){
                                dbA.modifyPriceFromId(p.getId(), price);
                                onStart();
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.error_price) , Toast.LENGTH_SHORT).show();
                                modifyPrice(c,p,name);
                            }

                        } catch (Exception ex){
                            Toast.makeText(getApplicationContext(), getString(R.string.error_price), Toast.LENGTH_SHORT).show();
                            modifyPrice(c,p,name);
                        }
                    }
                })
                .setNegativeButton(getString(R.string.undo_price), null)
                .create();
        dialog.show();
    }

    private void deleteProduct(Context c, ProductOnSale p, String name) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle(getString(R.string.remove_from_on_sale) + name +"?")
                .setPositiveButton(getString(R.string.remove_price), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbA.eliminateProductOnSaleFromDb(productOnSale.getId());
                        final Intent mainIntent = new Intent(ProductAreaActivity.this,
                                UserAreaActivity.class);
                        startActivity(mainIntent);
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.undo_price), null)
                .create();
        dialog.show();
    }




    public void eliminateProductOnSale(View eliminateButton){
        final Intent mainIntent = new Intent(ProductAreaActivity.this,
                UserAreaActivity.class);
        dbA.eliminateProductOnSaleFromDb(productOnSale.getId());
        startActivity(mainIntent);
        finish();
    }


    private File createImageFile(String product) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_id=[" + product+"]";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg",storageDir);
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
                    bitmap = MediaStore.Images.Media.getBitmap(ProductAreaActivity.this.getContentResolver(), Uri.fromFile(file));
                    encodeBitmapAndSaveToFirebase(bitmap, ""+file.getName(), prodOnSale);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        onStart();
    }


}