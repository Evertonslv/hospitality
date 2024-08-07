package com.hospitality.api.domain.usecases.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChargeDetail {
    private final String description;
    private double amount;
    private int quantity;

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void addamount(double amount) {
        this.amount += amount;
    }
}