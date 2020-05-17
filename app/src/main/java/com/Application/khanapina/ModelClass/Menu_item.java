package com.Application.khanapina.ModelClass;

public class Menu_item {

    private int item_price;
    private String item_name;
    private String item_url;

    public Menu_item() {
    }

    public Menu_item(String item_name, String item_url, int item_price) {
        this.item_name = item_name;
        this.item_url = item_url;
        this.item_price = item_price;
    }

    public int getItem_price() {
        return item_price;
    }

    public void setItem_price(int item_price) {
        this.item_price = item_price;
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
