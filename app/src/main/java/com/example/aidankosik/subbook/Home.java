package com.example.aidankosik.subbook;


import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * The main activity of the app. This is where the user can look
 * at and select their subscription. It displays the total
 * cost of the subscriptions at the top of the screen.
 * It also houses all of the alert dialogs used when adding
 * and deleting subscriptions.
 */
public class Home extends AppCompatActivity {
    private ArrayList<Subscription> subList = new ArrayList<>();
    private ArrayAdapter adapter;
    private FileSaver saver = new FileSaver();
    private Totals totaler = new Totals();
    private Toolbar toolbar;
    private boolean deleteMode = false;

    /**
     * This is the setup for when the app is created.
     * The ListView, Toolbar, ArrayAdapter and the ListView's
     * onClickListener are all setup in this function.
     * @param savedInstanceState a saved instance if the app was running
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Find and set the toolbar
        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Delete Mode: Off");

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

                if (deleteMode) {
                    final Subscription item = (Subscription) parent.getItemAtPosition(position);
                    subList.remove(item);adapter.notifyDataSetChanged();
                    totaler.setTotal((TextView) findViewById(R.id.totalPrice), subList);
                    saver.save(getBaseContext(), subList);
                    return;
                }

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
                    eComment.setText("");
                } else {
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
                        .setNegativeButton("Cancel Changes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();

                            }
                        });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    /**
     * This function sets up what to do when the add button is clicked.
     * The alert dialog that is opened when add is clicked is created
     * and set up in this function as well.
     */
    public void onClickAdd() {
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialog_view = inflater.inflate(R.layout.dialog_layout, null);

        // Create the alert dialog to enter information
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this)
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
                            double price = Double.parseDouble(tPrice);

                            // Do all the checks for the fields.
                            // Make sure the date is in the right format
                            if (!parseDate(date)) {
                                Toast.makeText(getBaseContext(), "The date format should be yyyy-MM-dd.", Toast.LENGTH_LONG).show();
                                return;
                            }
                            // Make sure the price is non-negative, make a toast popup if it is negative and return without adding.
                            if (price < 0) {
                                Toast.makeText(getBaseContext(), "The price cannot be negative.", Toast.LENGTH_LONG).show();
                                return;
                            }
                            // Make sure the title is <= 20 characters
                            if (title.length() > 20) {
                                Toast.makeText(getBaseContext(), "Title can be max 20 characters.", Toast.LENGTH_LONG).show();
                                return;
                            }

                            // If the comment is empty, give it the specified empty comment string
                            if (comment.isEmpty()) {
                                comment = "%$CommentEmpty$%";
                            } else {
                                if (comment.length() > 30) {
                                    Toast.makeText(getBaseContext(), "Comment can be max 30 characters.", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }

                            Subscription e = new Subscription(title, date, price, comment);
                            addSub(e);
                        } else {
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
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * This function takes a string that is the date for
     * a subscription. It then parses this date and ensures
     * that it is in the right format.
     * Credit to: MadProgrammer -
     * https://stackoverflow.com/questions/20231539/
     * java-check-the-date-format-of-current-string-is-according-to-required-format-or
     * @param string_date the date entered
     * @return boolean whether the date is formatted right
     */
    private boolean parseDate(String string_date) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            date = format.parse(string_date);
            if (!string_date.equals(format.format(date))) {
                date = null;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date != null;
    }


    /**
     * Adds a subscription to the subscription array and updates
     * the adapter and total price of the subscriptions.
      * @param s the subscription to be added
     */
    public void addSub(Subscription s) {
        subList.add(s);
        adapter.notifyDataSetChanged();
        totaler.setTotal((TextView) findViewById(R.id.totalPrice), subList);
    }


    /**
     * When the app is closed, this makes sure that
     * the subscriptions are saved to the text file.
     */
    @Override
    protected void onStop() {
        super.onStop();
        saver.save(this, subList);
    }

    /**
     * When the menu bar is selected this inflates the
     * correct layout for the menu.
     * @param menu the menu being selected
     * @return the super classes constructor for this.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Coordinates what to do when an item on the Toolbar
     * is selected. The options are delete, add, and clear.
     * @param item the menu item that was selected
     * @return boolean true if a valid item was selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.add_item) {
            // If the add item button is selected call onClickAdd
            onClickAdd();
            return true;
        }
        else if (item.getItemId() == R.id.delete) {
            // When the delete button is selected create a warning
            // to see if the user wants to turn delete mode on or off.
            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this)
                    .setPositiveButton("On", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteMode = true;
                            toolbar.setTitle("Delete Mode: On");
                        }
                    })
                    .setNegativeButton("Off", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteMode = false;
                            toolbar.setTitle("Delete Mode: Off");
                        }
                    })
                    .setTitle("Delete mode")
                    .setMessage("Would you like to turn delete mode on or off?");
            AlertDialog dialog = builder.create();
            dialog.show();

            return true;
        }
        else if (item.getItemId() == R.id.reset_app) {
            // When the reset button is chosen, create a warning to ensure this is
            // what the user wants to do.
            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this)
                    .setMessage("Would you like to clear all submissions?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            subList.clear();
                            saver.save(getBaseContext(), subList);
                            adapter.notifyDataSetChanged();
                            totaler.setTotal((TextView) findViewById(R.id.totalPrice), subList);

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    })
                    .setTitle("Clear All");
            AlertDialog dialog = builder.create();
            dialog.show();

            return true;
        } else {
            return false;
        }
    }



}
