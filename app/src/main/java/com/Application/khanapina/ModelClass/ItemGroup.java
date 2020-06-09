package com.Application.khanapina.ModelClass;

import java.util.ArrayList;
import java.util.List;

public class ItemGroup {
    private String Key;
    private ArrayList<Menu_item> menu;

    public ItemGroup() {
    }

    public ItemGroup(String key, ArrayList<Menu_item> menu) {
        Key = key;
        this.menu = menu;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public List<Menu_item> getMenu() {
        return menu;
    }

    public void setMenu(ArrayList<Menu_item> menu) {
        this.menu = menu;
    }
}
