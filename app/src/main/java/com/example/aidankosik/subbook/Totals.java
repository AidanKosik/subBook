package com.example.aidankosik.subbook;

import android.annotation.SuppressLint;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aidankosik on 2018-01-18.
 */

/**
 * Gets and sets the total price of the subscriptions.
 */
public class Totals {

    /**
     * Gets the total price of all of the subscriptions
     * in the array of subscriptions.
     * @param subList the array of subscriptions to be totalled
     * @return the new total of the subscriptions
     */
    public double getTotal(ArrayList<Subscription> subList) {
        double total = 0;
        for (Subscription item : subList) {
            total += item.getPrice();
        }
        return total;
    }

    /**
     * Sets the total of the array of subscriptions.
     * @param textView the TextView that shows the total
     * @param subList the array of subscriptions
     */
    // Sets the TextView that displays the total price
    public void setTotal(TextView textView, ArrayList<Subscription> subList) {
        double tp = getTotal(subList);
        textView.setText("$" + String.format("%.2f", tp));
    }
}
