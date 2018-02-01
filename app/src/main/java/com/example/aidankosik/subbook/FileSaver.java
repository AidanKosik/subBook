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

public class FileSaver {


    // Save the subList array to internal storage, allowing for persistent data.
    // Parameters
    // Context - this is a context type object
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

    // Load the subscription array from the save file.
    // Parameters
    // context -> this is a context type
    public void load(Context context, ArrayList<Subscription> subList) {
        Log.i("LOAD", "Loading...");

        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    String words[] = receiveString.split(",");
                    subList.add(new Subscription(words[0], words[1], Double.parseDouble(words[2]), words[3]));
                    Log.i("REC", words[0] + words[1] + words[2] + words[3]);
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
