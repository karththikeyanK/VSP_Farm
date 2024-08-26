package com.karththi.vsp_farm.model;

public class SubItem {
    private int id;
    private String SubItemName;
    private Double price;

    private int ItemId;

    public SubItem() {
    }

    public SubItem(int id, String subItemName, Double price, int itemId) {
        this.id = id;
        SubItemName = subItemName;
        this.price = price;
        ItemId = itemId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubItemName() {
        return SubItemName;
    }

    public void setSubItemName(String subItemName) {
        SubItemName = subItemName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }
}
