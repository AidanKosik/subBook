package com.example.aidankosik.a301a1;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class Home extends AppCompatActivity {
    private ArrayList<Subscription> subList = new ArrayList<>();
    private ArrayAdapter adapter;
    private FileSaver saver = new FileSaver();
    private Totals totaler = new Totals();
    private Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Find and set the toolbar
        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        ListView listView = findViewById(R.id.subsList);
        saver.load(this, subList);
        totaler.setTotal((TextView) findViewById(R.id.totalPrice), subList);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, subList);

        listView.setAdapter(adapter);


        // When a ListView item is clicked, create an alert dialog that allows the user to edit
        // and delete the selected subscription
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                LayoutInflater inflater = getLayoutInflater();
                final View dialog_view = inflater.inflate(R.layout.dialog_layout, null);
                final EditText eTitle = dialog_view.findViewById(R.id.d_title);
                final EditText eDate = dialog_view.findViewById(R.id.d_date);
                final EditText ePrice = dialog_view.findViewById(R.id.d_price);
                final EditText eComment = dialog_view.findViewById(R.id.d_comment);


                final Subscription item = (Subscription) parent.getItemAtPosition(position);
                eTitle.setText(item.getTitle());
                eDate.setText(item.getDate());
                ePrice.setText(item.getPriceAsString());

                // If the comment is empty, set it as "" rather than our tag
                if (item.getComment().equals("%$CommentEmpty$%")) {
                    Log.i("EQUALS", "E1");
                    eComment.setText("");
                } else {
                    Log.i("EQUALS", "E2");
                    eComment.setText(item.getComment());
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this)
                        .setView(dialog_view)
                        .setPositiveButton("Submit Changes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                item.setTitle(eTitle.getText().toString());
                                item.setDate(eDate.getText().toString());
                                item.setPrice(Double.parseDouble(ePrice.getText().toString()));
                                // If the comment is empty, give it the specified empty comment string
                                if (eComment.getText().toString().isEmpty()) {
                                    item.setComment("%$CommentEmpty$%");
                                } else {
                                    item.setComment(eComment.getText().toString());
                                }

                                adapter.notifyDataSetChanged();
                                totaler.setTotal((TextView) findViewById(R.id.totalPrice), subList);

                            }
                        })
                        .setNegativeButton("Delete Subscription", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                subList.remove(item);
                                adapter.notifyDataSetChanged();
                                totaler.setTotal((TextView) findViewById(R.id.totalPrice), subList);

                            }
                        });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    // The onClick function for the add button.
    // Opens an alert dialog using the dialog_layout.
    // The add button will submit the given information as a subscription object adding it to the list view
    public void onClickAdd() {
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialog_view = inflater.inflate(R.layout.dialog_layout, null);

        // Create the alert dialog to enter information
        AlertDialog.Builder dialog = new AlertDialog.Builder(Home.this)
                .setView(dialog_view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // get the EditText objects from the dialog_layout -----
                        EditText eTitle = dialog_view.findViewById(R.id.d_title);
                        EditText eDate = dialog_view.findViewById(R.id.d_date);
                        EditText ePrice = dialog_view.findViewById(R.id.d_price);
                        EditText eComment = dialog_view.findViewById(R.id.d_comment);
                        // -------------------------------------------------------

                        // Get the entered data for the subscription --
                        String title = eTitle.getText().toString();
                        String date = eDate.getText().toString();
                        String tPrice = ePrice.getText().toString();
                        String comment = eComment.getText().toString();
                        // -----------------------------------------------

                        if (!title.isEmpty() && !date.isEmpty() && !tPrice.isEmpty()) {
                            if(parseDate(date)) {
                                double price = Double.parseDouble(tPrice);
                                // If the comment is empty, give it the specified empty comment string
                                if (comment.isEmpty()) {
                                    comment = "%$CommentEmpty$%";
                                }

                                Subscription e = new Subscription(title, date, price, comment);
                                addSub(e);
                            }
                            else {
                                Toast.makeText(getBaseContext(), "The date format should be dd/MM/yyyy.", Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            Toast.makeText(getBaseContext(), "Title, Price and Date need entries.", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .setTitle("Add Subscription");
        dialog.show();
    }

    private boolean parseDate(String string_date) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            date = format.parse(string_date);
            if (!string_date.equals(format.format(date))) {
                date = null;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date != null;
    }


    // This function adds the given subscription object to the list of subscriptions and then
    // it updates the listView to show new subscription.
    // Parameters:
        // s - a subscription item to be added
    public void addSub(Subscription s) {
        subList.add(s);
        adapter.notifyDataSetChanged();
        totaler.setTotal((TextView) findViewById(R.id.totalPrice), subList);
    }


    @Override
    protected void onStop() {
        super.onStop();
        saver.save(this, subList);
        Log.i("SAVE", "Saving on stop!");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.add_item) {
            // If the add item button is selected
            onClickAdd();
            return true;
        }
        else if (item.getItemId() == R.id.delete) {
            return true;
        }
        else if (item.getItemId() == R.id.reset_app) {
            return true;
        } else {
            return false;
        }
    }


}
