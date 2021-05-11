package com.example.myapplication_test_db.model;

public class Product {
    public final long id;
    public final String name;
    public final String expansion;
    public final String rarity;
    public final String type;
    public final String img;


    public Product(final long id, final String name, final String expansion, final String rarity,
                   final String type, final String img) {
        this.id = id;
        this.name = name;
        this.expansion = expansion;
        this.rarity = rarity;
        this.type = type;
        this.img = img;
    }

    public static Product create (final long id, final String name, final String expansion,
                                  final String rarity, final String type, final String img){
        return new Product(id, name, expansion, rarity, type, img);
    }
}
