package com.karththi.vsp_farm.model;

public class Permission {
    private int id;
    private String permission;

    private int isGranted;

    public Permission() {
    }

    public Permission(int id, String permission, int isGranted) {
        this.id = id;
        this.permission = permission;
        this.isGranted = isGranted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public int getIsGranted() {
        return isGranted;
    }

    public void setIsGranted(int isGranted) {
        this.isGranted = isGranted;
    }
}
