package com.example.MagicShop.model;

public class ProductOnSale {

    private String id;
    private final long product_id;
    private final String user_id;
    private final float price;
    private String photo;


    public ProductOnSale(String id, final long product_id,
                         final String user_id, final float price, final String photo) {
        this.id = id;
        this.product_id = product_id;
        this.user_id = user_id;
        this.price = price;
        this.photo = photo;
    }

    public static ProductOnSale create(String id, final long product_id,
                                       final String user_id, final float price, final String photo) {
        return new ProductOnSale(id, product_id, user_id, price, photo);
    }

    public ProductOnSale withPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public ProductOnSale withId(String id){
        this.id = id;
        return this;
    }


    public String getPhoto() {return photo;}

    public String getId() {return id;}

    public long getProduct_id() {return product_id;}

    public String getUser_id() {return user_id; }

    public float getPrice() {return price; }
}
