package com.example.aidankosik.a301a1;

import java.io.Serializable;

/**
 * Created by aidankosik on 2018-01-17.
 */

public class Subscription implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private String date;
    private double price;
    private String comment;

    Subscription(String title, String date, double price, String comment) {
        this.title = title;
        this.date = date;
        this.price = price;
        this.comment = comment;
    }


    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public double getPrice() {
        return price;
    }

    public String getPriceAsString() {
        return Double.toString(price);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title + ": $" + price + " pays on " + date;
    }

    public String showComment() {
        if (comment == "") {
            return "No comment entered.";
        }

        return comment;
    }

    public String toWrite() {
        return title + "," + date + "," + Double.toString(price) + "," + comment + ",\n";
    }
}
