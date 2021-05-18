package com.example.MagicShop.model;

public class ProductOnSale {

    //TODO bisonga farla parcializable

    private final long id;
    private final long product_id;
    private final long user_id;
    private final long price;


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

    public long getId() {return id;}

    public long getProduct_id() {return product_id;}

    public long getUser_id() {return user_id; }

    public long getPrice() {return price; }
}
