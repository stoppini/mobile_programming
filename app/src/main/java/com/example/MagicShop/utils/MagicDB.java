package com.example.MagicShop.utils;

import android.provider.BaseColumns;

public class MagicDB {
    public static final String DATABASE_NAME = "magic_db.db";
    public static final int DATABASE_VERSION = 1;

    public static class Pruduct_db implements BaseColumns{
        public static final String TABLE_NAME = "product";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_EXPANSION = "expansion";
        public static final String COLUMN_RARITY = "rarity";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_IMG = "img";
    }

    public static class Pruduct_On_Sale_db implements BaseColumns{
        public static final String TABLE_NAME = "product_on_sale";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_PRODUCT_ID = "product_id";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_PRICE = "price";
    }


}
