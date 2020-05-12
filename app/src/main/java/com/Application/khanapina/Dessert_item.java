package com.Application.khanapina;

public class Dessert_item {

    String item_name;
    String item_url;

    public Dessert_item() {
    }

    public Dessert_item(String item_name, String item_url) {
        this.item_name = item_name;
        this.item_url = item_url;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_url() {
        return item_url;
    }

    public void setItem_url(String item_url) {
        this.item_url = item_url;
    }
}
