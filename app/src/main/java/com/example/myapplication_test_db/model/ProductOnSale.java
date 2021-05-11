package com.example.myapplication_test_db.model;

import android.media.midi.MidiManager;

public class ProductOnSale {
    public final long id;
    public final long product_id;
    public final long user_id;
    public final long price;


    public ProductOnSale(final long id, final long product_id,
                         final long user_id, final long price) {
        this.id = id;
        this.product_id = product_id;
        this.user_id = user_id;
        this.price = price;
    }

    public static ProductOnSale create(final long id, final long product_id,
                                       final long user_id, final long price) {
        return new ProductOnSale(id, product_id, user_id, price);
    }


}
