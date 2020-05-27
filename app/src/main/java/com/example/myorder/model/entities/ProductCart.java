package com.example.myorder.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "products", primaryKeys = {"idProduct", "size"})
public class ProductCart {

    public String logo;
    public String name; // название (Гавайская)

    @NonNull
    public String idProduct; // id такого продукта в бд (Hawaii)

    @NonNull
    public String size; // размер (30см)
    public int cost_one; // цена за 1 ед. продукта (309р)
    public int count; // количество таких продуктов
    public int total_cost; // общая стоимость таких продуктов (927р)

    public ProductCart() {
    }

    public static class Builder {
        private ProductCart productCart;

        public Builder() {
            productCart = new ProductCart();
            productCart.count = 1;
        }

        public Builder withLogo(String logo) {
            productCart.logo = logo;
            return this;
        }

        public Builder withName(String name) {
            productCart.name = name;
            return this;
        }

        public Builder withIdProduct(String idProduct) {
            productCart.idProduct = idProduct;
            return this;
        }

        public Builder withSize(String size) {
            productCart.size = size;
            return this;
        }

        public Builder withCost_one(int cost_one) {
            productCart.cost_one = cost_one;
            productCart.total_cost = cost_one;
            return this;
        }

        public ProductCart build() {
            return productCart;
        }
    }
}