package com.example.MagicShop.model;

import java.util.Date;

public class HistoryRecord {

    private long product_id;
    private float price;
    private UserHistory user;
    private long date;

    public HistoryRecord(long product_id, float price, long date, UserHistory user){
        this.product_id = product_id;
        this.price = price;
        this.user = user;
        this.date = date;
    }

    public static HistoryRecord create(long product_id, float price, long date, UserHistory user){
        return new HistoryRecord(product_id, price,date, user);
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public UserHistory getUser() {
        return user;
    }

    public void setUser(UserHistory user) {
        this.user = user;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public static class UserHistory{
        private String mUserName;
        private String mEmail;
        private String mLocation;
        private String mAddress;
        private long mCap;


        public UserHistory(String mUserName, String mEmail, String mLocation, String mAddress, long mCap){
            this.mUserName = mUserName;
            this.mEmail = mEmail;
            this.mLocation = mLocation;
            this.mAddress = mAddress;
            this.mCap = mCap;
        }

        public static UserHistory create(String mUserName, String mEmail, String mLocation, String mAddress, long mCap){
            return new UserHistory(mUserName, mEmail, mLocation, mAddress, mCap);
        }

        public String getmUserName() {
            return mUserName;
        }

        public void setmUserName(String mUserName) {
            this.mUserName = mUserName;
        }

        public String getmEmail() {
            return mEmail;
        }

        public void setmEmail(String mEmail) {
            this.mEmail = mEmail;
        }

        public String getmLocation() {
            return mLocation;
        }

        public void setmLocation(String mLocation) {
            this.mLocation = mLocation;
        }

        public String getmAddress() {
            return mAddress;
        }

        public void setmAddress(String mAddress) {
            this.mAddress = mAddress;
        }

        public long getmCap() {
            return mCap;
        }

        public void setmCap(long mCap) {
            this.mCap = mCap;
        }
    }


}
