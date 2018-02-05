package com.example.aidankosik.subbook;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by aidankosik on 2018-01-18.
 */

/**
 * This is the file saving class. This class to an array of subscriptions
 * and saves them to a text file in internal storage. This allows
 * persistence for the app so subscriptions are saved.
 */
public class FileSaver {


    /**
     * This function saves the Array of subscriptions to the text file.
     * @param context the context of the activity
     * @param subList the array of subscriptions
     */
    public void save(Context context, ArrayList<Subscription> subList) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            for (Subscription item : subList ) {
                outputStreamWriter.write(item.toWrite());
            }
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("WRITE", "File write failed: " + e.toString());
        }
    }

    /**
     * Loads the saved text file and inserts the subscription
     * items into the subList Array.
     * @param context the context of the activity
     * @param subList the Array of subscriptions
     */
    public void load(Context context, ArrayList<Subscription> subList) {

        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    String words[] = receiveString.split(",");
                    subList.add(new Subscription(words[0], words[1], Double.parseDouble(words[2]), words[3]));
                }

                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("LOAD", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("LOAD", "Can not read file: " + e.toString());
        }


    }
}
