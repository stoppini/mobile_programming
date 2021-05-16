package com.example.MagicShop.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.MagicShop.utils.MagicDB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MagicDBHelper extends SQLiteOpenHelper {
        Context context;
        String DB_PATH="";


        public MagicDBHelper(Context context) throws IOException {
            super(context, MagicDB.DATABASE_NAME, null, 3);
            this.context = context;
            this.DB_PATH = context.getDatabasePath(MagicDB.DATABASE_NAME).toString();
        }

        public boolean isDataBaseExists() {
            File dbFile = new File(DB_PATH + "");
            return dbFile.exists();
        }

        public void importDataBaseFromAssets() throws IOException {
            InputStream     myInput = context.getAssets().open(MagicDB.DATABASE_NAME);
            String outFileName = DB_PATH;
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            Toast.makeText(context.getApplicationContext(), "Successfully Imported",
                    Toast.LENGTH_SHORT).show();
            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();


        }

        @Override
        public void onCreate(SQLiteDatabase arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
        }

    }