package com.example.aidankosik.subbook;

import android.annotation.SuppressLint;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aidankosik on 2018-01-18.
 */

public class Totals {

    // Gets the total price of all of the subscriptions and returns it
    public double getTotal(ArrayList<Subscription> subList) {
        double total = 0;
        for (Subscription item : subList) {
            total += item.getPrice();
        }
        return total;
    }

    // Sets the TextView that displays the total price
    public void setTotal(TextView textView, ArrayList<Subscription> subList) {
        double tp = getTotal(subList);
        textView.setText("$" + String.format("%.2f", tp));
    }
}
