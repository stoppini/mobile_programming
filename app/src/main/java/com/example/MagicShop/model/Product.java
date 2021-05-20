package com.example.MagicShop.model;

import java.io.Serializable;
//TODO bisonga farla parcializable
public class Product implements Serializable {
    private final long id;
    private final String name;
    private final String expansion;
    private final String rarity;
    private final String type;
    private final String rule;
    private final String img;


    public static final String PRODUCT_LIST_EXTRA = "com.example.cardmarket.model.PRODUCT_LIST_EXTRA";

    public Product(final long id, final String name, final String expansion, final String rarity,
                   final String type, final String rule, final String img) {
        this.id = id;
        this.name = name;
        this.expansion = expansion;
        this.rarity = rarity;
        this.type = type;
        this.rule = rule;
        this.img = img;
    }

    public static Product create (final long id, final String name, final String expansion,
                                  final String rarity, final String type, final String rule, final String img){
        return new Product(id, name, expansion, rarity, type, rule, img);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getExpansion() {
        return expansion;
    }

    public String getRarity() {
        return rarity;
    }

    public String getType() {
        return type;
    }

    public String getRule() {
        return rule;
    }

    public String getImg() {
        return img;
    }
}
