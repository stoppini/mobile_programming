package com.example.MagicShop.model;


import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

public class User implements Parcelable {
    // togliere: data,
    // aggiungere: address, cap
    // private int mID;
    private String mUserName;
    private String mPassword;
    private String mEmail;
    private String mLocation;
    private String mAddress;
    private long mCap;

    //private long mBirthDate;

    public static final String USER_DATA_EXTRA = "com.example.cardmarket.model.USER_DATA_EXTRA";

    private static final byte PRESENT = 1;
    private static final byte NOT_PRESENT = 0;


    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel parcel) {
            return new User(parcel);
        }

        @Override
        public User[] newArray(int i) {
            return new User[0];
        }
    };

    public User(Parcel in) {

        Log.d("debug","cap: " + this.mCap);

        if(in.readByte() == PRESENT)
        {
            this.mUserName = in.readString();
            Log.d("debug","username: " + this.mUserName);
        }
        if(in.readByte() == PRESENT)
        {
            this.mPassword = in.readString();
            Log.d("debug","password: " + this.mPassword);
        }

        if(in.readByte() == PRESENT)
        {
            this.mEmail = in.readString();
            Log.d("debug","email: " + this.mEmail);
        }

        if(in.readByte() == PRESENT)
        {
            this.mLocation = in.readString();
            Log.d("debug","location: " + this.mLocation);
        }

        if(in.readByte() == PRESENT)
        {
            this.mAddress = in.readString();
            Log.d("debug","address: " + this.mAddress);
        }

        this.mCap = in.readLong();
//        if(in.readByte() == PRESENT)
//        {
//            this.mCap = in.readLong();
//            Log.d("debug","cap: " + this.mCap);
//        }
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flag)
    {
        //dest.writeLong(this.mBirthDate);
        if(!TextUtils.isEmpty(this.mUserName))
        {
            dest.writeByte(PRESENT);
            dest.writeString(this.mUserName);
        } else {
            dest.writeByte(NOT_PRESENT);
        }

        if(!TextUtils.isEmpty(this.mPassword))
        {
            dest.writeByte(PRESENT);
            dest.writeString(this.mPassword);
        } else {
            dest.writeByte(NOT_PRESENT);
        }

        if(!TextUtils.isEmpty(this.mEmail))
        {
            dest.writeByte(PRESENT);
            dest.writeString(this.mEmail);
        } else {
            dest.writeByte(NOT_PRESENT);
        }

        if(!TextUtils.isEmpty(this.mLocation))
        {
            dest.writeByte(PRESENT);
            dest.writeString(this.mLocation);
        } else {
            dest.writeByte(NOT_PRESENT);
        }

        if(!TextUtils.isEmpty(this.mAddress))
        {
            dest.writeByte(PRESENT);
            dest.writeString(this.mAddress);
        } else {
            dest.writeByte(NOT_PRESENT);
        }

        if(!TextUtils.isEmpty(Long.toString(this.mCap)))
        {
            dest.writeByte(PRESENT);
            //scrivo il long
            dest.writeLong(this.mCap);
        } else {
            dest.writeByte(NOT_PRESENT);
        }

    }

    private User() {

    }

    public static User create() {
        final User user = new User();
        return user;
    }


    public User withUsername(String newUserUsername) {
        this.mUserName = newUserUsername;
        return this;
    }

    public User withPassword(String newPassword) {
        this.mPassword = newPassword;
        return this;
    }

    public User withEmail(String newEmail) {
        this.mEmail = newEmail;
        return this;
    }

    public User withLocation(String newLocation) {
        this.mLocation = newLocation;
        return this;
    }

    public User withAddress(String newAddress) {
        this.mAddress = newAddress;
        return this;
    }

    public User withCap(long newCap) {
        this.mCap = newCap;
        return this;
    }

    public String getUsername() {
        return this.mUserName;
    }

    public String getPassword() {
        return this.mPassword;
    }

    public String getEmail() {
        return this.mEmail;
    }

    public String getLocation() {
        return this.mLocation;
    }

    public String getAddress() {
        return this.mAddress;
    }

    public long getCap() {
        return this.mCap;
    }


//    public long getBirthDate() {
//        return this.mBirthDate;
//    }

}

