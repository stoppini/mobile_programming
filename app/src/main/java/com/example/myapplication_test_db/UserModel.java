package com.example.myapplication_test_db;


import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class UserModel implements Parcelable {
    private String mUserName;
    private String mPassword;
    private String mEmail;
    private String mLocation;
    private long mBirthDate;
    public static final String USER_DATA_EXTRA = "com.example.cardmarket.model.USER_DATA_EXTRA";

    private static final byte PRESENT = 1;
    private static final byte NOT_PRESENT = 0;


    public static final Parcelable.Creator<UserModel> CREATOR = new Parcelable.Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel parcel) {
            return new UserModel(parcel);
        }

        @Override
        public UserModel[] newArray(int i) {
            return new UserModel[0];
        }
    };

    public UserModel(Parcel in) {
        this.mBirthDate = in.readLong();
        if(in.readByte() == PRESENT)
        {
            this.mUserName = in.readString();
        }
        if(in.readByte() == PRESENT)
        {
            this.mPassword = in.readString();
        }

        if(in.readByte() == PRESENT)
        {
            this.mEmail = in.readString();
        }

        if(in.readByte() == PRESENT)
        {
            this.mLocation = in.readString();
        }
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flag)
    {
        dest.writeLong(this.mBirthDate);
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
    }

    private UserModel(final long birthDate) {
        this.mBirthDate = birthDate;
    }

    public static UserModel create(final long birthDate) {
        final UserModel userModel = new UserModel(birthDate);
        return userModel;
    }

    public UserModel withUsername(String newUserUsername) {
        this.mUserName = newUserUsername;
        return this;
    }

    public UserModel withPassword(String newPassword) {
        this.mPassword = newPassword;
        return this;
    }

    public UserModel withEmail(String newEmail) {
        this.mEmail = newEmail;
        return this;
    }

    public UserModel withLocation(String newLocation) {
        this.mLocation = newLocation;
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

    public long getBirthDate() {
        return this.mBirthDate;
    }
}
