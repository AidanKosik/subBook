package com.example.aidankosik.subbook;

import android.annotation.SuppressLint;

import java.io.Serializable;

/**
 * Created by aidankosik on 2018-01-17.
 */

/**
 * Contains all of the information for a subscription
 * and the functions for a subscription.
 */
public class Subscription {
    private String title;
    private String date;
    private double price;
    private String comment;

    /**
     * Constructor for the subscription class.
     * @param title the name of the subscription
     * @param date the date the subscription was started
     * @param price the monthly fee of the subscription
     * @param comment a comment for the subscription
     */
    Subscription(String title, String date, double price, String comment) {
        this.title = title;
        this.date = date;
        this.price = price;
        this.comment = comment;
    }


    /**
     * Getter for the title.
     * @return title the name of the subscription
     */
    public String getTitle() {
        return title;
    }

    /**
     * the getter for the date.
     * @return date the date the subscription was started
     */
    public String getDate() {
        return date;
    }

    /**
     * getter for the price as a double.
     * @return price the price as a double
     */
    public double getPrice() {
        return price;
    }

    /**
     * Gets the price in string format rather than double.
     * @return price returns the price as a string
     */
    public String getPriceAsString() {
        return Double.toString(price);
    }

    /**
     * The getter for the comment.
     * @return comment returns the comment of the subscription
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the comment of subscription
     * @param comment the new comment of the subscription
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Sets the price of the subscription
     * @param price the new price of the subscription
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Sets the date of the subscription
     * @param date the new date of the subscription
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Sets the title of the subscription
     * @param title the new title of the subscription
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Override the toString function so it prints in
     * the right format in the ListView.
     * @return
     */
    @Override
    public String toString() {
        return title + ": $" + String.format("%.2f", price) + " Started:  " + date;
    }

    /**
     * Function for when writing the object to a text file.
     * @return String a string of the text separated by a comma.
     */
    public String toWrite() {
        return title + "," + date + "," + Double.toString(price) + "," + comment + ",\n";
    }
}
