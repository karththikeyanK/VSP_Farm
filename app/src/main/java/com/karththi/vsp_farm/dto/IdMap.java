package com.karththi.vsp_farm.dto;

public class IdMap {
    int itemId;
    int subItemId;
    String itemName;
    String subItemName;

    public IdMap() {
    }

    public IdMap(int itemId, int subItemId, String itemName, String subItemName) {
        this.itemId = itemId;
        this.subItemId = subItemId;
        this.itemName = itemName;
        this.subItemName = subItemName;
    }


    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getSubItemId() {
        return subItemId;
    }

    public void setSubItemId(int subItemId) {
        this.subItemId = subItemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSubItemName() {
        return subItemName;
    }

    public void setSubItemName(String subItemName) {
        this.subItemName = subItemName;
    }
}
