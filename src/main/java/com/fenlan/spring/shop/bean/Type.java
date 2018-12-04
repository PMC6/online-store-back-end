package com.fenlan.spring.shop.bean;

public enum Type {
    SELLER(0, "卖家"),
    SHOP(1, "店铺"),
    CUSTOMER(2, "买家"),
    PRODUCT(3, "商品"),
    ADMIN(4, "管理员");

    Type(int number, String description) {
        this.code = number;
        this.description = description;
    }

    private final int code;
    private String description;

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static Type getByCode(int code) {
        switch (code) {
            case 0 : return SELLER;
            case 1 : return SHOP;
            case 2 : return CUSTOMER;
            case 3 : return PRODUCT;
            case 4 : return ADMIN;
            default : return null;
        }
    }
}