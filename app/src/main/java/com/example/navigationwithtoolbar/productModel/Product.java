package com.example.navigationwithtoolbar.productModel;

public class Product {
    private String companyName,price,status;

    public String getCompanyName() {
        return companyName;
    }

    public String getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public Product(String companyName, String price, String status) {
        this.companyName = companyName;
        this.price = price;
        this.status = status;
    }
}
