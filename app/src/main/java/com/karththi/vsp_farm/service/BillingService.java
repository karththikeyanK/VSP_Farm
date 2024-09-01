package com.karththi.vsp_farm.service;

public class BillingService {

    public String getQuantity(Double price, Double total) {
        return String.valueOf(total / price);
    }
}
