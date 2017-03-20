package com.mydeliveries.nandos.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import android.widget.Spinner;
import android.widget.TextView;

import com.mydeliveries.nandos.R;
import com.mydeliveries.nandos.model.Price;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Tab4 extends ListFragment {

    Integer count = 1;
    String name = "";
    String price = "";
    Double priceExtra = 6.90;
    String side = "";
    String drink = "";
    String extra = "";
    String hotlevelmenu = "";
    String diatary = "";
    String vegoption = "";
    double newprice = 0.0;
    TextView totalprice;


    List<Spinner> allspinhot = new ArrayList<Spinner>();
    List<Spinner> allspindrink = new ArrayList<Spinner>();
    List<Spinner> allspinside = new ArrayList<Spinner>();
    List<Spinner> allspinextra = new ArrayList<Spinner>();

    List<CheckBox> checkboxveggie = new ArrayList<CheckBox>();
    List<CheckBox> checkboxcheese = new ArrayList<CheckBox>();
    List<CheckBox> checkboxpine = new ArrayList<CheckBox>();

    List<EditText> editNotes = new ArrayList<EditText>();


    List<String> hotlevel = new ArrayList<String>();
    List<String> drinklist = new ArrayList<String>();
    List<String> sidelist = new ArrayList<String>();
    List<String> veggielist = new ArrayList<String>();
    List<String> noteslist = new ArrayList<String>();
    // List<String> extralist = new ArrayList<String>();

    List<String> cheeselist = new ArrayList<String>();
    List<String> pinelist = new ArrayList<String>();

    ArrayList<Integer> cheesechecked = new ArrayList<Integer>();
    ArrayList<Integer> pinechecked = new ArrayList<Integer>();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_4, container, false);
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        String Menu = pref.getString("Menu", null); // getting String
        String newMenu = Menu.replaceAll("=", ":");


        // Hashmap for ListView
        ArrayList<HashMap<String, String>> menu = new ArrayList<HashMap<String, String>>();


        cheesechecked.add(1);
        pinechecked.add(1);

        // getting JSON string from URL
        JSONArray contacts = null;
        try {
            contacts = new JSONArray(newMenu);

            for (int i = 0; i < contacts.length(); i++) {
                JSONObject json_data = contacts.getJSONObject(i);

                // Storing each json item in variable
                String cat = json_data.getString("row(0)");
                String name = json_data.getString("row(1)");
                String desc = json_data.getString("row(2)");
                String price = json_data.getString("row(3)");
                String diatary = json_data.getString("row(4)");
                String side = json_data.getString("row(5)");
                String drink = json_data.getString("row(6)");
                String hotlevel = json_data.getString("row(7)");
                String vegoption = json_data.getString("row(8)");
                String extra = json_data.getString("row(9)");

                HashMap<String, String> map = new HashMap<String, String>();
                if (cat.equalsIgnoreCase("appeteasers")) {
                    map.put("row(1)", name);
                    map.put("row(2)", desc);
                    map.put("row(3)", price);
                    map.put("row(4)", diatary);
                    map.put("row(5)", side);
                    map.put("row(6)", drink);
                    map.put("row(7)", hotlevel);
                    map.put("row(8)", vegoption);
                    map.put("row(9)", extra);

                    menu.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //  System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" + menu);

        // keys of hashmap
        String[] from = {"row(1)", "row(2)", "row(3)", "row(4)", "row(5)", "row(6)", "row(7)", "row(8)", "row(9)"};

        // view id's to which data to be binded
        int[] to = {R.id.name, R.id.description, R.id.price, R.id.diatary, R.id.side, R.id.drink, R.id.hotlevel, R.id.vegoption, R.id.extra};

        // Creating Adapter
        ListAdapter adapter = new SimpleAdapter(getActivity(), menu,
                R.layout.list_items, from, to);

        // Setting Adapter to ListView
        setListAdapter(adapter);
        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.form_elements,
                null, false);

        count = 1;
        final Price p = new Price();
        final TextView foodname = (TextView) formElementsView
                .findViewById(R.id.foodName);

        final TextView priceform = (TextView) formElementsView
                .findViewById(R.id.priceform);

        final Button increase = (Button) formElementsView
                .findViewById(R.id.quantity);

        final Button decrease = (Button) formElementsView
                .findViewById(R.id.decrease);

        totalprice = (TextView) formElementsView
                .findViewById(R.id.totalprice);

        final TextView counter = (TextView) formElementsView
                .findViewById(R.id.counter);

        final TextView total = (TextView) formElementsView
                .findViewById(R.id.total);


        Typeface face1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/webfont.ttf");

        foodname.setTypeface(face1);
        foodname.setTextSize(20);
        totalprice.setTypeface(face1);
        priceform.setTypeface(face1);
        priceform.setTextSize(20);
        total.setTypeface(face1);


        name = ((TextView) v.findViewById(R.id.name)).getText().toString();
        side = ((TextView) v.findViewById(R.id.side)).getText().toString();
        drink = ((TextView) v.findViewById(R.id.drink)).getText().toString();

        hotlevelmenu = ((TextView) v.findViewById(R.id.hotlevel)).getText().toString();
        diatary = ((TextView) v.findViewById(R.id.diatary)).getText().toString();
        vegoption = ((TextView) v.findViewById(R.id.vegoption)).getText().toString();
        extra = ((TextView) v.findViewById(R.id.extra)).getText().toString();

        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/webfont.ttf");


        LinearLayout mainLayout = (LinearLayout) formElementsView.findViewById(R.id.lay4);


        ImageView im = new ImageView(getActivity());
        im.setImageResource(R.drawable.linedivide);
        mainLayout.addView(im);

        TextView tv = new TextView(getActivity());
        tv.setText("Meal " + count);
        tv.setTypeface(face);
        tv.setTextColor(Color.parseColor("#5c4a3d"));
        tv.setTextSize(20);
        mainLayout.addView(tv);

        if (vegoption.equalsIgnoreCase("1")) {
            CheckBox cb = new CheckBox(getActivity());

            cb.setText("Veggie option");
            mainLayout.addView(cb);
            checkboxveggie.add(cb);
        }

        if (hotlevelmenu.equalsIgnoreCase("1")) {
            TextView hot = new TextView(getActivity());
            hot.setText("HotLevel:");
            hot.setTypeface(face);
            hot.setTextColor(Color.parseColor("#5c4a3d"));
            hot.setTextSize(14);
            mainLayout.addView(hot);

            String[] strings = {"Lemon & Herb", "Mild", "Hot", "Extra Hot", "Tangy Tomato"};

            Spinner spinner = new Spinner(getActivity());
            spinner.setBackgroundResource(R.drawable.dropdown);
            spinner.setTag(count);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_dropdown_item, strings);
            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinner.setAdapter(spinnerArrayAdapter);

            mainLayout.addView(spinner);
            allspinhot.add(spinner);
        }
        if (side.equalsIgnoreCase("1")) {
            TextView sides = new TextView(getActivity());
            sides.setText("Side:");
            sides.setTypeface(face);
            sides.setTextColor(Color.parseColor("#5c4a3d"));
            sides.setTextSize(14);
            mainLayout.addView(sides);


            String[] strings = {"Chips", "PERi-PERi Chips", "Wedges", "PERi-PERi Wedges", "Spicy Rice", "Coleslaw ", "PERi Spinach", "Roast Veg"};

            Spinner spinner = new Spinner(getActivity());
            spinner.setBackgroundResource(R.drawable.dropdown);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_dropdown_item, strings);
            spinner.setAdapter(spinnerArrayAdapter);

            mainLayout.addView(spinner);
            allspinside.add(spinner);
        }
        if (drink.equalsIgnoreCase("1")) {
            TextView drinks = new TextView(getActivity());
            drinks.setText("Drink:");
            drinks.setTypeface(face);
            drinks.setTextColor(Color.parseColor("#5c4a3d"));
            drinks.setTextSize(14);
            mainLayout.addView(drinks);


            String[] strings = {"coke", "sprite", "fanta", "oros"};

            Spinner spinner = new Spinner(getActivity());
            spinner.setBackgroundResource(R.drawable.dropdown);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_dropdown_item, strings);
            spinner.setAdapter(spinnerArrayAdapter);

            mainLayout.addView(spinner);
            allspindrink.add(spinner);
        }

        if (extra.equalsIgnoreCase("1")) {
            TextView extra = new TextView(getActivity());
            extra.setText("Extra:" + "(R" + String.valueOf(String.format("%.2f", priceExtra)) + " each)");
            extra.setTypeface(face);
            extra.setTextColor(Color.parseColor("#5c4a3d"));
            extra.setTextSize(14);
            mainLayout.addView(extra);

            CheckBox cb1 = new CheckBox(getActivity());
            cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                               @Override
                                               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                   if (isChecked == true) {
                                                       Double price = p.getPrice();
                                                       price = price + priceExtra;
                                                       p.setPrice(price);
                                                       totalprice.setText("R" + String.valueOf(String.format("%.2f", p.getPrice())));
                                                   }
                                                   if (isChecked == false) {
                                                       Double price = p.getPrice();
                                                       price = price - priceExtra;
                                                       p.setPrice(price);
                                                       totalprice.setText("R" + String.valueOf(String.format("%.2f", p.getPrice())));
                                                   }
                                               }
                                           }
            );
            cb1.setText("Cheese");
            mainLayout.addView(cb1);
            checkboxcheese.add(cb1);

            CheckBox cb2 = new CheckBox(getActivity());
            cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                               @Override
                                               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                   if (isChecked == true) {
                                                       Double price = p.getPrice();
                                                       price = price + priceExtra;
                                                       p.setPrice(price);
                                                       totalprice.setText("R" + String.valueOf(String.format("%.2f", p.getPrice())));
                                                   }
                                                   if (isChecked == false) {
                                                       Double price = p.getPrice();
                                                       price = price - priceExtra;
                                                       p.setPrice(price);
                                                       totalprice.setText("R" + String.valueOf(String.format("%.2f", p.getPrice())));
                                                   }
                                               }
                                           }
            );
            cb2.setText("Pine");
            mainLayout.addView(cb2);
            checkboxpine.add(cb2);


        }

        TextView note = new TextView(getActivity());
        note.setText("Notes:");
        note.setTypeface(face);
        note.setTextColor(Color.parseColor("#5c4a3d"));
        note.setTextSize(14);
        mainLayout.addView(note);

        EditText notes = new EditText(getActivity());
        notes.setHint("Extra notes for our chef");
        notes.setTextColor(Color.parseColor("#5c4a3d"));
        notes.setTextSize(14);
        notes.setFocusable(false);
        mainLayout.addView(notes);
        editNotes.add(notes);

        notes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });


        foodname.setText(name);


        final String pricet = ((TextView) v.findViewById(R.id.price)).getText().toString();
        price = pricet.replace("R", "");
        priceform.setText("R" + String.format("%.2f", Double.parseDouble(price)));
        counter.setText("1");
        counter.setTypeface(face);
        counter.setTextColor(Color.parseColor("#5c4a3d"));
        newprice = Double.parseDouble(price);
        p.setPrice(newprice);
        totalprice.setText(" R" + String.format("%.2f", p.getPrice()));


        if (count <= 1) {
            decrease.setEnabled(false);
        }
        final ArrayList<LinearLayout> rows = new ArrayList<LinearLayout>();

        System.out.println("cheese" + checkboxcheese.size());
        System.out.println("pine" + checkboxpine.size());
        System.out.println("drink" + allspindrink.size());
        System.out.println("hot" + allspinhot.size());
        System.out.println("veggie" + checkboxveggie.size());

        increase.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if (count >= 1) {
                    decrease.setEnabled(true);
                }
                Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/webfont.ttf");

                Double price = newprice + p.getPrice();
                p.setPrice(price);

                count++;

                counter.setText(count.toString());
                counter.setTypeface(face);
                counter.setTextColor(Color.parseColor("#5c4a3d"));

                totalprice.setText(" R" + String.valueOf(String.format("%.2f", p.getPrice())));
                totalprice.setTypeface(face);
                totalprice.setTextColor(Color.parseColor("#5c4a3d"));


                LinearLayout mainLayout = (LinearLayout) formElementsView.findViewById(R.id.lay4);
                LinearLayout linearlayout1 = new LinearLayout(getActivity());
                linearlayout1.setOrientation(LinearLayout.VERTICAL);

                ImageView im = new ImageView(getActivity());
                im.setImageResource(R.drawable.linedivide);
                linearlayout1.addView(im);

                TextView tv = new TextView(getActivity());

                tv.setText("Meal " + count);
                tv.setTypeface(face);
                tv.setTextColor(Color.parseColor("#5c4a3d"));
                tv.setTextSize(20);
                linearlayout1.addView(tv);

                if (vegoption.equalsIgnoreCase("1")) {
                    CheckBox cb = new CheckBox(getActivity());
                    cb.setText("Veggie option");
                    linearlayout1.addView(cb);
                    checkboxveggie.add(cb);
                }

                if (hotlevelmenu.equalsIgnoreCase("1")) {

                    TextView hot = new TextView(getActivity());
                    hot.setText("HotLevel:");
                    hot.setTypeface(face);
                    hot.setTextColor(Color.parseColor("#5c4a3d"));
                    hot.setTextSize(14);
                    linearlayout1.addView(hot);

                    String[] strings = {"Lemon & Herb", "Mild", "Hot", "Extra Hot", "Tangy Tomato"};

                    Spinner spinner = new Spinner(getActivity());
                    spinner.setBackgroundResource(R.drawable.dropdown);
                    spinner.setTag(count);
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_dropdown_item, strings);
                    spinner.setAdapter(spinnerArrayAdapter);

                    linearlayout1.addView(spinner);
                    allspinhot.add(spinner);


                }

                if (side.equalsIgnoreCase("1")) {
                    TextView sides = new TextView(getActivity());
                    sides.setText("Side:");
                    sides.setTypeface(face);
                    sides.setTextColor(Color.parseColor("#5c4a3d"));
                    sides.setTextSize(14);
                    linearlayout1.addView(sides);


                    String[] strings = {"Chips", "PERi-PERi Chips", "Wedges", "PERi-PERi Wedges", "Spicy Rice", "Coleslaw ", "PERi Spinach", "Roast Veg"};

                    Spinner spinner = new Spinner(getActivity());
                    spinner.setBackgroundResource(R.drawable.dropdown);

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_dropdown_item, strings);
                    spinner.setAdapter(spinnerArrayAdapter);

                    linearlayout1.addView(spinner);
                    allspinside.add(spinner);


                }
                if (drink.equalsIgnoreCase("1")) {

                    TextView drinks = new TextView(getActivity());
                    drinks.setText("Drink:");
                    drinks.setTypeface(face);
                    drinks.setTextColor(Color.parseColor("#5c4a3d"));
                    drinks.setTextSize(14);
                    linearlayout1.addView(drinks);


                    String[] strings = {"coke", "sprite", "fanta", "oros"};

                    Spinner spinner = new Spinner(getActivity());
                    spinner.setBackgroundResource(R.drawable.dropdown);

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_dropdown_item, strings);
                    spinner.setAdapter(spinnerArrayAdapter);

                    linearlayout1.addView(spinner);
                    allspindrink.add(spinner);


                }

                if (extra.equalsIgnoreCase("1")) {
                    TextView extra = new TextView(getActivity());
                    extra.setText("Extra:" + "(R" + String.valueOf(String.format("%.2f", priceExtra)) + " each)");
                    extra.setTypeface(face);
                    extra.setTextColor(Color.parseColor("#5c4a3d"));
                    extra.setTextSize(14);
                    linearlayout1.addView(extra);

                    CheckBox cb1 = new CheckBox(getActivity());
                    cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                       @Override
                                                       public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                           if (isChecked == true) {
                                                               Double price = p.getPrice();
                                                               price = price + priceExtra;
                                                               p.setPrice(price);
                                                               cheesechecked.add(1);
                                                               System.out.println("cheese size" + cheesechecked.size());
                                                               totalprice.setText(" R" + String.valueOf(String.format("%.2f", p.getPrice())));
                                                           }
                                                           if (isChecked == false) {
                                                               Double price = p.getPrice();
                                                               price = price - priceExtra;
                                                               p.setPrice(price);
                                                               cheesechecked.remove(1);
                                                               System.out.println("cheese size" + cheesechecked.size());
                                                               totalprice.setText(" R" + String.valueOf(String.format("%.2f", p.getPrice())));
                                                           }
                                                       }
                                                   }
                    );
                    cb1.setText("Cheese");
                    linearlayout1.addView(cb1);
                    checkboxcheese.add(cb1);

                    CheckBox cb2 = new CheckBox(getActivity());
                    cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                       @Override
                                                       public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                           if (isChecked == true) {
                                                               Double price = p.getPrice();
                                                               price = price + priceExtra;
                                                               p.setPrice(price);
                                                               pinechecked.add(1);
                                                               System.out.println("pine size" + pinechecked.size());
                                                               totalprice.setText(" R" + String.valueOf(String.format("%.2f", p.getPrice())));
                                                           }
                                                           if (isChecked == false) {
                                                               Double price = p.getPrice();
                                                               price = price - priceExtra;
                                                               p.setPrice(price);
                                                               pinechecked.remove(1);
                                                               System.out.println("pine size" + pinechecked.size());
                                                               totalprice.setText(" R" + String.valueOf(String.format("%.2f", p.getPrice())));
                                                           }
                                                       }
                                                   }
                    );
                    cb2.setText("Pine");
                    linearlayout1.addView(cb2);
                    checkboxpine.add(cb2);

                }

                TextView note = new TextView(getActivity());
                note.setText("Notes:");
                note.setTypeface(face);
                note.setTextColor(Color.parseColor("#5c4a3d"));
                note.setTextSize(14);
                linearlayout1.addView(note);

                EditText notes = new EditText(getActivity());
                notes.setHint("Extra notes for our chef");
                notes.setTextColor(Color.parseColor("#5c4a3d"));
                notes.setTextSize(14);
                linearlayout1.addView(notes);
                editNotes.add(notes);

                mainLayout.addView(linearlayout1);
                rows.add(linearlayout1);

                System.out.println("cheese" + checkboxcheese.size());
                System.out.println("pine" + checkboxpine.size());
                System.out.println("drink" + allspindrink.size());
                System.out.println("hot" + allspinhot.size());
                System.out.println("veggie" + checkboxveggie.size());

            }

        });

        decrease.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                LinearLayout mainLayout = (LinearLayout) formElementsView.findViewById(R.id.lay4);
                Double extracheese = 0.0;
                if (cheesechecked.size() > 1) {
                    cheesechecked.remove(1);
                    extracheese = priceExtra;
                }

                Double extrapine = 0.0;

                if (pinechecked.size() > 1) {
                    pinechecked.remove(1);
                    extrapine = priceExtra;
                }


                System.out.println("price extraxs" + extrapine);

                System.out.println("price extracheese" + extracheese);


                try {
                    // get the last item
                    LinearLayout layoutToRemove = rows.get(rows.size() - 1);
                    mainLayout.removeView(layoutToRemove); // remove it
                    rows.remove(layoutToRemove); // update the list
                    mainLayout.invalidate(); // may need this (optional)

                    allspinhot.remove(allspinhot.size() - 1);
                    allspinside.remove(allspinside.size() - 1);
                    allspindrink.remove(allspindrink.size() - 1);
                    allspinextra.remove(allspinextra.size() - 1);
                    editNotes.remove(editNotes.size() - 1);
                    checkboxveggie.remove(checkboxveggie.size() - 1);
                    checkboxcheese.remove(checkboxcheese.size() - 1);
                    checkboxpine.remove(checkboxpine.size() - 1);


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    System.out.println("Exception " + e.toString());
                }

                Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/webfont.ttf");

                Double price = p.getPrice() - newprice - extracheese - extrapine;

                System.out.println("price&*&*&*&*&*&*&*&*& " + price);
                p.setPrice(price);

                System.out.println("cheese" + checkboxcheese.size());
                System.out.println("pine" + checkboxpine.size());
                System.out.println("drink" + allspindrink.size());
                System.out.println("hot" + allspinhot.size());
                System.out.println("veggie" + checkboxveggie.size());

                count--;
                counter.setText(count.toString());
                counter.setTypeface(face);
                counter.setTextColor(Color.parseColor("#5c4a3d"));

                totalprice.setText(" R" + String.format("%.2f", p.getPrice()));
                totalprice.setTypeface(face);
                totalprice.setTextColor(Color.parseColor("#5c4a3d"));


                if (count <= 1) {
                    decrease.setEnabled(false);
                }

            }

        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity()).setView(formElementsView);
        LayoutInflater inflater1 = getActivity().getLayoutInflater();
        View view = inflater1.inflate(R.layout.tool_bar_order, null);

        TextView header = (TextView) view
                .findViewById(R.id.toolbartitle);
        header.setTypeface(face);
        header.setText("Add to order");
        alertDialogBuilder.setCustomTitle(view);


        alertDialogBuilder

                .setCancelable(false)
                .setPositiveButton("Add To Order", new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        try {

                            for (EditText s : editNotes) {

                                noteslist.add(s.getText().toString());
                            }

                            for (Spinner s : allspinhot) {

                                hotlevel.add(s.getSelectedItem().toString());
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


                            for (int i = 0; i < count; i++) {


                                String jsonString = "";

                                JSONObject jsonObject = new JSONObject();
                                double extracheese = 0.0;
                                if (!cheeselist.isEmpty()) {

                                    if (cheeselist.get(i).equalsIgnoreCase("No")) {
                                        extracheese = 0.0;
                                    } else {
                                        extracheese = priceExtra;
                                    }
                                }
                                double extrapine = 0.0;
                                if (!pinelist.isEmpty()) {
                                    if (pinelist.get(i).equalsIgnoreCase("No")) {
                                        extrapine = 0.0;
                                    } else {
                                        extrapine = priceExtra;
                                    }
                                }
                                double finalprice = newprice + extracheese + extrapine;

                                jsonObject.put("meal", name);
                                jsonObject.put("quantity", "1");
                                jsonObject.put("type", ((hotlevel.isEmpty()) ? "None" : hotlevel.get(i)));
                                jsonObject.put("price", String.format("%.2f", finalprice).replace(",", "."));
                                jsonObject.put("side", ((sidelist.isEmpty()) ? "None" : sidelist.get(i)));
                                jsonObject.put("drink", ((drinklist.isEmpty()) ? "None" : drinklist.get(i)));
                                jsonObject.put("veggie", ((veggielist.isEmpty()) ? "None" : veggielist.get(i)));
                                jsonObject.put("extra", ((cheeselist.isEmpty()) ? "None" : cheeselist.get(i)) + "," + ((pinelist.isEmpty()) ? "None" : pinelist.get(i)));
                                jsonObject.put("notes", ((noteslist.isEmpty()) ? "None" : noteslist.get(i)));
                                jsonString = jsonObject.toString();

                                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                                SharedPreferences.Editor editor = pref.edit();
                                String fullOrder = "";
                                if (pref.getString("Order", null) != null && (!pref.getString("Order", null).equalsIgnoreCase(""))) {
                                    fullOrder = pref.getString("Order", null) + "," + jsonString;
                                } else {
                                    fullOrder = jsonString;
                                }

                                try {

                                    editor.putString("Order", fullOrder);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                editor.commit(); // commit changes
                                System.out.println("menu page" + pref.getString("Order", null));
                            }


                            hotlevel.clear();
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
                        hotlevel.clear();
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
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


}