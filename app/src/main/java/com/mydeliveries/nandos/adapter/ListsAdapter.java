package com.mydeliveries.nandos.adapter;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mydeliveries.nandos.R;
import com.mydeliveries.nandos.fragments.CheckOutFragment;
import com.mydeliveries.nandos.activity.HomeActivity;
import com.mydeliveries.nandos.model.Price;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


public class ListsAdapter extends ArrayAdapter<String> {

    private FragmentActivity context;
    private final String[] meal;
    private final String[] price;
    private final String[] quantity;
    private final String[] side;
    private final String[] drink;
    private final String[] veggie;
    private final String[] hotlevel;
    private final String[] extra;
    private final String[] notes;


    Integer count = 1;
    String name = "";
    String price1 = "";
    String side1 = "";
    String drink1 = "";
    String hotlevelmenu = "";
    String diatary = "";
    String vegoption = "";
    String extra1 = "";
    String notes1 = "";
    double newprice = 0;
    Double priceExtra = 6.90;

    String quantity1 = "";


    List<Spinner> allspinhot = new ArrayList<Spinner>();
    List<Spinner> allspindrink = new ArrayList<Spinner>();
    List<Spinner> allspinside = new ArrayList<Spinner>();
    List<Spinner> allspinextra = new ArrayList<Spinner>();
    List<CheckBox> checkboxveggie = new ArrayList<CheckBox>();

    List<CheckBox> checkboxcheese = new ArrayList<CheckBox>();
    List<CheckBox> checkboxpine = new ArrayList<CheckBox>();

    List<EditText> editNotes = new ArrayList<EditText>();


    List<String> hotlevel1 = new ArrayList<String>();
    List<String> drinklist = new ArrayList<String>();
    List<String> sidelist = new ArrayList<String>();
    List<String> extralist = new ArrayList<String>();
    List<String> veggielist = new ArrayList<String>();
    List<String> noteslist = new ArrayList<String>();

    List<String> cheeselist = new ArrayList<String>();
    List<String> pinelist = new ArrayList<String>();


    private ProgressDialog pDialog;
    JSONArray jarray = null;

    Integer pos = -1;
    Double counter = 0.0;

    AlertDialog alertDialog;

    public ListsAdapter(FragmentActivity context,
                        String[] meal, String[] price, String[] quantity, String[] side, String[] drink, String[] veggie, String[] hotlevel, String[] extra, String[] notes) {
        super(context, R.layout.list_lists, meal);
        this.context = context;
        this.meal = meal;
        this.price = price;
        this.quantity = quantity;
        this.side = side;
        this.drink = drink;
        this.veggie = veggie;
        this.hotlevel = hotlevel;
        this.extra = extra;
        this.notes = notes;
    }

    @Override
    public View getView(final int position, final View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_lists, null, true);
        final TextView txtMeal = (TextView) rowView.findViewById(R.id.meal);
        final TextView txtPrice = (TextView) rowView.findViewById(R.id.price);
        final TextView txtSide = (TextView) rowView.findViewById(R.id.side);
        final TextView txtDrink = (TextView) rowView.findViewById(R.id.drink);
        final TextView txtVeggie = (TextView) rowView.findViewById(R.id.veggie);
        final TextView txtHotlevel = (TextView) rowView.findViewById(R.id.hotlevel);
        final TextView txtExtra = (TextView) rowView.findViewById(R.id.extra);
        final TextView txtNotes = (TextView) rowView.findViewById(R.id.notes);


        Button delete = (Button) rowView.findViewById(R.id.delete_list);


        final Price p = new Price();

        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set title
                alertDialogBuilder.setTitle("Remove - " + meal[position]);

                // Use an EditText view to get user input.

                // set dialog message
                alertDialogBuilder
                        .setCancelable(true)
                        .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity

                                pos = position;

                                new deleteorder().execute();

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing


                                dialog.cancel();
                            }
                        });

                // create alert dialog
                alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();


            }

        });


        Button update = (Button) rowView.findViewById(R.id.update_list);


        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View formElementsView = inflater.inflate(R.layout.form_elements,
                        null, false);

                final ArrayList<LinearLayout> rows = new ArrayList<LinearLayout>();

                SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                String fullOrder = "";

                ArrayList<String> arrside = new ArrayList<String>();
                ArrayList<String> arrdrink = new ArrayList<String>();
                ArrayList<String> arrextra = new ArrayList<String>();
                ArrayList<String> arrveggie = new ArrayList<String>();
                ArrayList<String> arrtype = new ArrayList<String>();


                pos = position;


                fullOrder = "[" + pref.getString("Order", null) + "]";
                JSONArray oparray = null;
                try {
                    oparray = new JSONArray(fullOrder);


                    JSONObject update = oparray.getJSONObject(pos);
                    quantity1 = update.getString("quantity");

                    name = update.getString("meal");
                    side1 = update.getString("side");
                    drink1 = update.getString("drink");
                    extra1 = update.getString("extra");
                    notes1 = update.getString("notes");

                    price1 = update.getString("price");
                    vegoption = update.getString("veggie");

                    hotlevelmenu = update.getString("type");
                    //diatary = ((TextView) v.findViewById(R.id.diatary)).getText().toString();


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                p.setPrice(Double.parseDouble(price1));


                System.out.println("&*&*&*&*&*&*&*&" + arrtype.toString());


                count = 1;

                final TextView foodname = (TextView) formElementsView
                        .findViewById(R.id.foodName);

                final TextView priceform = (TextView) formElementsView
                        .findViewById(R.id.priceform);

                final Button increase = (Button) formElementsView
                        .findViewById(R.id.quantity);

                final Button decrease = (Button) formElementsView
                        .findViewById(R.id.decrease);

                final TextView totalprice = (TextView) formElementsView
                        .findViewById(R.id.totalprice);
                final TextView counter = (TextView) formElementsView
                        .findViewById(R.id.counter);

                final TextView total = (TextView) formElementsView
                        .findViewById(R.id.total);


                Typeface face1 = Typeface.createFromAsset(context.getAssets(), "fonts/webfont.ttf");

                foodname.setTypeface(face1);
                foodname.setTextSize(20);
                totalprice.setTypeface(face1);
                priceform.setTypeface(face1);
                total.setTypeface(face1);


                System.out.println("*******************" + quantity1);

                count = Integer.parseInt(quantity1);

                Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/webfont.ttf");


                LinearLayout mainLayout = (LinearLayout) formElementsView.findViewById(R.id.lay4);
                LinearLayout linearlayout1 = new LinearLayout(context);
                linearlayout1.setOrientation(LinearLayout.VERTICAL);

                ImageView im = new ImageView(context);
                im.setImageResource(R.drawable.linedivide);
                linearlayout1.addView(im);

                TextView tv = new TextView(context);
                tv.setText("Meal " + count);
                tv.setTypeface(face);
                tv.setTextColor(Color.parseColor("#5c4a3d"));
                tv.setTextSize(20);
                linearlayout1.addView(tv);


                if (!(vegoption.equalsIgnoreCase("None"))) {

                    if (vegoption.equalsIgnoreCase("No")) {
                        vegoption = "No";
                    } else {
                        vegoption = "Yes";
                    }
                    CheckBox cb = new CheckBox(context);
                    if (vegoption.equalsIgnoreCase("Yes")) {
                        cb.setChecked(true);
                    }
                    cb.setText("Veggie option");
                    linearlayout1.addView(cb);
                    checkboxveggie.add(cb);
                }


                System.out.println(vegoption + vegoption.length());

                if (!(hotlevelmenu.equalsIgnoreCase("None"))) {

                    TextView hot = new TextView(context);
                    hot.setText("HotLevel:");
                    hot.setTypeface(face);
                    hot.setTextColor(Color.parseColor("#5c4a3d"));
                    hot.setTextSize(14);
                    linearlayout1.addView(hot);

                    String[] strings = {hotlevelmenu, "Lemon & Herb", "Mild", "Hot", "Extra Hot", "Tangy Tomato"};

                    Spinner spinner = new Spinner(context);
                    spinner.setBackgroundResource(R.drawable.dropdown);
                    spinner.setTag(count);
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, R.layout.spinner_dropdown_item, strings);
                    spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    spinner.setAdapter(spinnerArrayAdapter);

                    linearlayout1.addView(spinner);
                    allspinhot.add(spinner);


                }
                if (!(side1.equalsIgnoreCase("None"))) {

                    TextView sides = new TextView(context);
                    sides.setText("Side:");
                    sides.setTypeface(face);
                    sides.setTextColor(Color.parseColor("#5c4a3d"));
                    sides.setTextSize(14);
                    linearlayout1.addView(sides);


                    String[] strings = {side1, "Chips", "PERi-PERi Chips", "Wedges", "PERi-PERi Wedges", "Spicy Rice", "Coleslaw ", "PERi Spinach", "Roast Veg"};

                    Spinner spinner = new Spinner(context);
                    spinner.setBackgroundResource(R.drawable.dropdown);
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, R.layout.spinner_dropdown_item, strings);
                    spinner.setAdapter(spinnerArrayAdapter);

                    linearlayout1.addView(spinner);
                    allspinside.add(spinner);


                }
                if (!(drink1.equalsIgnoreCase("None"))) {

                    TextView drinks = new TextView(context);
                    drinks.setText("Drink:");
                    drinks.setTypeface(face);
                    drinks.setTextColor(Color.parseColor("#5c4a3d"));
                    drinks.setTextSize(14);
                    linearlayout1.addView(drinks);


                    String[] strings = {drink1, "coke", "sprite", "fanta", "oros"};

                    Spinner spinner = new Spinner(context);
                    spinner.setBackgroundResource(R.drawable.dropdown);
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, R.layout.spinner_dropdown_item, strings);
                    spinner.setAdapter(spinnerArrayAdapter);

                    linearlayout1.addView(spinner);
                    allspindrink.add(spinner);


                }

                if (!(extra1.equalsIgnoreCase("None,None"))) {

                    TextView extra = new TextView(context);
                    extra.setText("Extra:" + "(R" + String.valueOf(String.format("%.2f", priceExtra)) + " each)");
                    extra.setTypeface(face);
                    extra.setTextColor(Color.parseColor("#5c4a3d"));
                    extra.setTextSize(14);
                    linearlayout1.addView(extra);

                    String[] parts = extra1.split(",");
                    String cheese = parts[0];
                    String pine = parts[1];

                    if (cheese.equalsIgnoreCase("No")) {
                        cheese = "No";
                    } else {
                        cheese = "Yes";
                    }

                    if (pine.equalsIgnoreCase("No")) {
                        pine = "No";
                    } else {
                        pine = "Yes";
                    }

                    CheckBox cb1 = new CheckBox(context);
                    if (cheese.equalsIgnoreCase("Yes")) {
                        cb1.setChecked(true);
                    }
                    cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                       @Override
                                                       public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                           if (isChecked == true) {
                                                               Double price = p.getPrice();
                                                               price = price + priceExtra;
                                                               p.setPrice(price);
                                                               totalprice.setText(" R" + String.valueOf(String.format("%.2f", p.getPrice())));
                                                           }
                                                           if (isChecked == false) {
                                                               Double price = p.getPrice();
                                                               price = price - priceExtra;
                                                               p.setPrice(price);
                                                               totalprice.setText(" R" + String.valueOf(String.format("%.2f", p.getPrice())));
                                                           }
                                                       }
                                                   }
                    );
                    cb1.setText("Cheese");
                    linearlayout1.addView(cb1);
                    checkboxcheese.add(cb1);

                    CheckBox cb2 = new CheckBox(context);
                    if (pine.equalsIgnoreCase("Yes")) {
                        cb2.setChecked(true);
                    }
                    cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                       @Override
                                                       public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                           if (isChecked == true) {
                                                               Double price = p.getPrice();
                                                               price = price + priceExtra;
                                                               p.setPrice(price);
                                                               totalprice.setText(" R" + String.valueOf(String.format("%.2f", p.getPrice())));
                                                           }
                                                           if (isChecked == false) {
                                                               Double price = p.getPrice();
                                                               price = price - priceExtra;
                                                               p.setPrice(price);
                                                               totalprice.setText(" R" + String.valueOf(String.format("%.2f", p.getPrice())));
                                                           }
                                                       }
                                                   }
                    );
                    cb2.setText("Pine");
                    linearlayout1.addView(cb2);
                    checkboxpine.add(cb2);


                }
                TextView note = new TextView(context);
                note.setText("Notes:");
                note.setTypeface(face);
                note.setTextColor(Color.parseColor("#5c4a3d"));
                note.setTextSize(14);
                linearlayout1.addView(note);

                EditText notes = new EditText(context);
                notes.setHint("Extra notes for our chef");
                notes.setTextColor(Color.parseColor("#5c4a3d"));
                notes.setTextSize(14);
                linearlayout1.addView(notes);
                editNotes.add(notes);

                notes.setText(notes1);

                mainLayout.addView(linearlayout1);
                rows.add(linearlayout1);


                foodname.setText(name);


                final double tempprice = Double.parseDouble(price1) / Double.parseDouble(quantity1);
                priceform.setText("R" + String.format("%.2f", tempprice));
                counter.setVisibility(view.GONE);

                newprice = Double.parseDouble(price1);
                totalprice.setText(" R" + String.format("%.2f", Double.parseDouble(price1)));


                decrease.setVisibility(view.GONE);
                increase.setVisibility(view.GONE);


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context).setView(formElementsView);

                LayoutInflater inflater1 = context.getLayoutInflater();
                View view = inflater1.inflate(R.layout.tool_bar_order, null);

                TextView header = (TextView) view
                        .findViewById(R.id.toolbartitle);
                header.setTypeface(face);
                header.setText("Update Order");
                alertDialogBuilder.setCustomTitle(view);


                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Update Order", new DialogInterface.OnClickListener() {


                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity
                                try {

                                    for (EditText s : editNotes) {

                                        noteslist.add(s.getText().toString());
                                    }

                                    for (Spinner s : allspinhot) {

                                        hotlevel1.add(s.getSelectedItem().toString());
                                    }

                                    for (Spinner s : allspindrink) {

                                        drinklist.add(s.getSelectedItem().toString());
                                    }


                                    for (CheckBox s : checkboxcheese) {

                                        if (Boolean.toString(s.isChecked()).equalsIgnoreCase("true")) {
                                            cheeselist.add("Yes");
                                        } else {
                                            cheeselist.add("No");
                                        }


                                    }

                                    for (CheckBox s : checkboxpine) {

                                        if (Boolean.toString(s.isChecked()).equalsIgnoreCase("true")) {
                                            pinelist.add("Yes");
                                        } else {
                                            pinelist.add("No");
                                        }


                                    }

                                    for (Spinner s : allspinside) {

                                        sidelist.add(s.getSelectedItem().toString());
                                    }

                                    for (CheckBox s : checkboxveggie) {

                                        if (Boolean.toString(s.isChecked()).equalsIgnoreCase("true")) {
                                            veggielist.add("Yes");
                                        } else {
                                            veggielist.add("No");
                                        }


                                    }

                                    double extracheese = 0.0;
                                    if (!cheeselist.isEmpty()) {

                                        if (cheeselist.get(0).equalsIgnoreCase("No")) {
                                            extracheese = 0.0;
                                        } else {
                                            extracheese = priceExtra;
                                        }
                                    }
                                    double extrapine = 0.0;
                                    if (!pinelist.isEmpty()) {
                                        if (pinelist.get(0).equalsIgnoreCase("No")) {
                                            extrapine = 0.0;
                                        } else {
                                            extrapine = priceExtra;
                                        }
                                    }
                                    double finalprice = Double.parseDouble(price1) + extracheese + extrapine;


                                    SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                                    SharedPreferences.Editor editor = pref.edit();
                                    String fullOrder = "";
                                    String newOrder = "";

                                    fullOrder = "[" + pref.getString("Order", null) + "]";
                                    JSONArray oparray = null;
                                    try {
                                        oparray = new JSONArray(fullOrder);

                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                    JSONObject update = oparray.getJSONObject(pos);

                                    update.put("meal", name);
                                    update.put("quantity", quantity1);
                                    update.put("type", ((hotlevel1.isEmpty()) ? "None" : hotlevel1.get(0)));
                                    update.put("price", String.format("%.2f", p.getPrice()).replace(",", "."));
                                    update.put("side", ((sidelist.isEmpty()) ? "None" : sidelist.get(0)));
                                    update.put("drink", ((drinklist.isEmpty()) ? "None" : drinklist.get(0)));
                                    update.put("veggie", ((veggielist.isEmpty()) ? "None" : veggielist.get(0)));
                                    update.put("extra", ((cheeselist.isEmpty()) ? "None" : cheeselist.get(0)) + "," + ((pinelist.isEmpty()) ? "None" : pinelist.get(0)));
                                    update.put("notes", ((noteslist.isEmpty()) ? "None" : noteslist.get(0)));

                                    if (oparray.length() == 1) {
                                        JSONObject object = oparray.getJSONObject(0);

                                        newOrder = object.toString();
                                    } else {
                                        for (int n = 0; n < oparray.length(); n++) {
                                            JSONObject object = oparray.getJSONObject(n);
                                            System.out.println("for" + object);
                                            if (newOrder != null && (!newOrder.equalsIgnoreCase(""))) {
                                                newOrder = newOrder + "," + object.toString();
                                            } else {
                                                newOrder = object.toString();
                                            }


                                        }
                                    }
                                    System.out.println("for" + newOrder);

                                    editor.putString("Order", newOrder);

                                    editor.commit(); //


                                    System.out.println("menu page" + pref.getString("Order", null));

                                    hotlevel1.clear();
                                    drinklist.clear();
                                    sidelist.clear();
                                    veggielist.clear();
                                    noteslist.clear();
                                    cheeselist.clear();
                                    pinelist.clear();
                                    allspinhot.clear();
                                    allspinside.clear();
                                    allspindrink.clear();
                                    allspinextra.clear();
                                    checkboxveggie.clear();
                                    checkboxcheese.clear();
                                    checkboxpine.clear();


                                    ((HomeActivity) context).displayView(2);


                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }


                            }

                        })
                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                hotlevel1.clear();
                                drinklist.clear();
                                sidelist.clear();
                                veggielist.clear();
                                noteslist.clear();
                                cheeselist.clear();
                                pinelist.clear();
                                allspinhot.clear();
                                allspinside.clear();
                                allspindrink.clear();
                                checkboxveggie.clear();
                                allspinextra.clear();
                                checkboxcheese.clear();
                                checkboxpine.clear();
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }


        });


        String extrastr = extra[position].toString();


        String[] parts = extrastr.split(",");
        String cheese = parts[0];
        String pine = parts[1];

        if (cheese.equalsIgnoreCase("None")) {
            cheese = "No";
        } else {
            cheese = "Yes";
        }

        if (pine.equalsIgnoreCase("None")) {
            pine = "No";
        } else {
            pine = "Yes";
        }

        Typeface face1 = Typeface.createFromAsset(context.getAssets(), "fonts/webfont.ttf");
        txtMeal.setText(quantity[position] + " x " + meal[position]);
        txtMeal.setTypeface(face1);
        txtPrice.setText("R " + price[position]);
        txtPrice.setTypeface(face1);
        txtSide.setText("Side: " + side[position].toString());
        txtDrink.setText("Drink: " + drink[position].toString());
        txtHotlevel.setText("Hotlevel: " + hotlevel[position].toString());
        txtExtra.setText("Extras: " + "Cheese: " + cheese + " Pine: " + pine);
        txtVeggie.setText("Veggie option: " + veggie[position].toString());
        txtNotes.setText("Notes: " + notes[position].toString());


        return rowView;
    }


    private class deleteorder extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            // Showing progress dialog before sending http request

        }

        @Override
        protected String doInBackground(String... params) {


            SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            String fullOrder = "";

            fullOrder = "[" + pref.getString("Order", null) + "]";
            JSONArray oparray = null;
            try {
                oparray = new JSONArray(fullOrder);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            remove(pos, oparray);


            String newOrder = jarray.toString();
            String order = newOrder.substring(1);
            String finalorder = order.substring(0, order.length() - 1);
            editor.putString("Order", finalorder);

            editor.commit(); // commit changes
            System.out.println("(deleted order change" + pref.getString("Order", null));


            CheckOutFragment CheckOutFragment = new CheckOutFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction;
            fragmentTransaction = context.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, CheckOutFragment);
            fragmentTransaction.commit();

            return null;
        }


        @Override
        protected void onPostExecute(String file) {

            notifyDataSetChanged();
            ((HomeActivity) context).displayView(2);

            super.onPostExecute(file);


        }


    }


    public JSONArray remove(final int index, final JSONArray from) {
        final List<JSONObject> objs = getList(from);
        objs.remove(index);

        jarray = new JSONArray();
        for (final JSONObject obj : objs) {
            jarray.put(obj);
        }

        return jarray;
    }

    public static List<JSONObject> getList(final JSONArray jarray) {
        final int len = jarray.length();
        final ArrayList<JSONObject> result = new ArrayList<JSONObject>(len);
        for (int i = 0; i < len; i++) {
            final JSONObject obj = jarray.optJSONObject(i);
            if (obj != null) {
                result.add(obj);
            }
        }
        return result;
    }


}

