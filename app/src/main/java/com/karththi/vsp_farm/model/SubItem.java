package com.karththi.vsp_farm.model;

public class SubItem {
    private int id;
    private String SubItemName;
    private Double price;
    private int ItemId;
    private String status;

    public SubItem() {
    }

    public SubItem(int id, String subItemName, Double price, int itemId, String status) {
        this.id = id;
        this.SubItemName = subItemName;
        this.price = price;
        this.ItemId = itemId;
        this.status = status;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
