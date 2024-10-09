package org.example.javaapplicationproject;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private int Cart_ID;
    private String startDate;
    private String endDate;
    private String ISBN;
    private String title;

    public Cart(int cart_ID, String startDate, String endDate, String ISBN) {
        Cart_ID = cart_ID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ISBN = ISBN;
    }

    public int getCart_ID() {
        return Cart_ID;
    }

    public void setCart_ID(int cart_ID) {
        Cart_ID = cart_ID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

}

