package com.example.MagicShop;


import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class User implements Parcelable {
    private String mUserName;
    private String mPassword;
    private String mEmail;
    private String mLocation;
    private long mBirthDate;
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

    private User(final long birthDate) {
        this.mBirthDate = birthDate;
    }

    public static User create(final long birthDate) {
        final User user = new User(birthDate);
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
