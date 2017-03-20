package com.mydeliveries.nandos.fragments;

import android.app.Activity;
import android.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.mydeliveries.nandos.R;
import com.mydeliveries.nandos.activity.HomeActivity;
import com.mydeliveries.nandos.adapter.ListAdapterInvite;
import com.mydeliveries.nandos.adapter.ListsAdapter;
import com.mydeliveries.nandos.model.NavItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Andrew on 10/07/2015.
 */


public class CheckOutFragment extends Fragment {


    EditText nameadd;
    EditText street;
    EditText unitno;
    EditText complex;
    EditText suburb;
    EditText city;
    Spinner province;

    FloatingActionMenu menu1;

    private FloatingActionButton fab1;


    private List<FloatingActionMenu> menus = new ArrayList<>();


    List<String> listingmeal = new ArrayList<String>();
    List<String> listingprice = new ArrayList<String>();
    List<String> listingquantity = new ArrayList<String>();
    List<String> listingdrink = new ArrayList<String>();
    List<String> listingside = new ArrayList<String>();
    List<String> listingveggie = new ArrayList<String>();
    List<String> listingextra = new ArrayList<String>();
    List<String> listinghotlevel = new ArrayList<String>();
    List<String> listingnotes = new ArrayList<String>();
    JSONArray oparray;
    ListView list;

    Double counter = 0.0;
    Double app = 2.0;

    String cName = "";
    String cSurname = "";
    String cNumber = "";
    String cEmail = "";

    Integer cDelivery = 0;
    Integer cPreorder = 0;
    Integer cCollection = 0;
    Integer cPaid = 0;
    Double cAppfee = 0.0;
    Double cGateway = 0.0;

    Double cPrice = 0.0;
    Double cTotalPrice = 0.0;
    String cOrderDate = "";
    String cGCM = "";
    String cAddress = "";
    String cOrder = "";
    String creference = "";

    boolean useAddress = false;
    boolean payOnline = false;

    Double onlinefee = 0.0;

    EditText name = null;

    EditText surname = null;

    EditText number = null;

    EditText email = null;

    EditText address = null;
    TextView choose = null;
    Button addAddress = null;
    Spinner s = null;

    Spinner se = null;

    String dayDate = "";


    AlertDialog alertDialog;

    private ProgressDialog pDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        listingdrink.clear();
        listingprice.clear();
        listingprice.clear();
        listingquantity.clear();
        listingside.clear();
        listingextra.clear();
        listingveggie.clear();
        listinghotlevel.clear();
        listingnotes.clear();


        final SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();


        NavItem nav = new NavItem();

        nav.setPage("Home");


        ((HomeActivity) getActivity()).loadDrawsHide();


        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((HomeActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        final View rootView = inflater.inflate(R.layout.fragment_lists, container, false);

        fab1 = (FloatingActionButton) rootView.findViewById(R.id.fab1);
        menu1 = (FloatingActionMenu) rootView.findViewById(R.id.menu_down);

        fab1.setOnClickListener(clickListener);


        final Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/webfont.ttf");


        final RadioGroup choiceRadioGroup1 = (RadioGroup) rootView
                .findViewById(R.id.choiceRadioGroup1);

        final RadioGroup choiceRadioGroup2 = (RadioGroup) rootView
                .findViewById(R.id.choiceRadioGroup2);

        final RadioButton DeliveryRadioButton = (RadioButton) rootView
                .findViewById(R.id.DeliveryRadioButton);
        final RadioButton todayRadioButton = (RadioButton) rootView
                .findViewById(R.id.TodayRadioButton);

        final RadioGroup choiceRadioGroup3 = (RadioGroup) rootView
                .findViewById(R.id.choiceRadioGroup3);

        final TextView storeName = (TextView) rootView
                .findViewById(R.id.storeName);

        final TextView heading = (TextView) rootView
                .findViewById(R.id.heading);
        final TextView payment = (TextView) rootView
                .findViewById(R.id.payment);
        final TextView OrderDate = (TextView) rootView
                .findViewById(R.id.OrderDate);


        if (!pref.getString("StoreDelivery", null).equalsIgnoreCase("TRUE")) {

            DeliveryRadioButton.setEnabled(false);
        }


        Boolean closed = false;
        int dayweek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String dateNow = formatter.format(currentDate.getTime());

        if (dayweek == 1) {
            String storeclose = pref.getString("StoreHours", null);
            String timenow = dateNow.substring(0, 2);

            int store = Integer.parseInt(storeclose) - 1;
            int currenttime = Integer.parseInt(timenow);
            if (currenttime >= store) {
                closed = true;
            }

        } else if (dayweek == 2) {

            String storeclose = pref.getString("StoreHours", null);
            String timenow = dateNow.substring(0, 2);

            int store = Integer.parseInt(storeclose) - 1;
            int currenttime = Integer.parseInt(timenow);
            if (currenttime >= store) {
                closed = true;
            }

        } else if (dayweek == 3) {

            String storeclose = pref.getString("StoreHours", null);
            String timenow = dateNow.substring(0, 2);

            int store = Integer.parseInt(storeclose) - 1;
            int currenttime = Integer.parseInt(timenow);
            if (currenttime >= store) {
                closed = true;
            }

        } else if (dayweek == 4) {

            String storeclose = pref.getString("StoreHours", null);
            String timenow = dateNow.substring(0, 2);

            int store = Integer.parseInt(storeclose) - 1;
            int currenttime = Integer.parseInt(timenow);
            if (currenttime >= store) {
                closed = true;
            }

        } else if (dayweek == 5) {

            String storeclose = pref.getString("StoreHours", null);
            String timenow = dateNow.substring(0, 2);

            int store = Integer.parseInt(storeclose) - 1;
            int currenttime = Integer.parseInt(timenow);
            if (currenttime >= store) {
                closed = true;
            }

        } else if (dayweek == 6) {

            String storeclose = pref.getString("StoreHours", null);
            String timenow = dateNow.substring(0, 2);

            int store = Integer.parseInt(storeclose) - 1;
            int currenttime = Integer.parseInt(timenow);
            if (currenttime >= store) {
                closed = true;
            }
        } else if (dayweek == 7) {

            String storeclose = pref.getString("StoreHours", null);
            String timenow = dateNow.substring(0, 2);

            int store = Integer.parseInt(storeclose) - 1;
            int currenttime = Integer.parseInt(timenow);
            if (currenttime >= store) {
                closed = true;
            }

        }

        if (closed == true) {

            todayRadioButton.setEnabled(false);
        }

        System.out.println("reference" + pref.getString("Order", null));


        String orders = "[" + pref.getString("Order", null) + "]";

        String ref = Long.toHexString(Double.doubleToLongBits(Math.random()));
        String numbers = ref.substring(ref.length() - 6);

        creference = numbers;

        final List<String> listingname = new ArrayList<String>();

        String addresses = "[" + pref.getString("Address", null) + "]";

        String msg = "Nando&rsquo;s";


        storeName.setText("From " + Html.fromHtml(msg) + " " + pref.getString("StoreName", null));
        storeName.setTypeface(face);

        heading.setTypeface(face);
        payment.setTypeface(face);
        OrderDate.setTypeface(face);

        try {
            oparray = new JSONArray(addresses);


            for (int i = 0; i < oparray.length(); i++) {
                JSONObject obj = oparray.getJSONObject(i);

                String name = (String) obj.get("name");

                listingname.add(name);


            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        try {
            oparray = new JSONArray(orders);


            for (int i = 0; i < oparray.length(); i++) {
                JSONObject obj = oparray.getJSONObject(i);


                String meal = (String) obj.get("meal");
                String price = (String) obj.get("price");
                String quantity = (String) obj.get("quantity");
                String side = (String) obj.get("side");
                String drink = (String) obj.get("drink");
                String veggie = (String) obj.get("veggie");
                String hotlevel = (String) obj.get("type");
                String extra = (String) obj.get("extra");

                String notes = (String) obj.get("notes");

                listingmeal.add(meal);
                listingprice.add(price);
                listingquantity.add(quantity);
                listingside.add(side.toString());
                listingdrink.add(drink.toString());
                listingveggie.add(veggie.toString());
                listinghotlevel.add(hotlevel.toString());
                listingextra.add(extra.toString());
                listingnotes.add(notes.toString());


            }
            //listingname.add("Create New List");
            //listingList.add(Integer.toString(1));

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //final String[] web = (String[]) listingname.toArray();

        final String[] meal = listingmeal.toArray(new String[listingmeal.size()]);

        final String[] price = listingprice.toArray(new String[listingprice.size()]);
        final String[] quantity = listingquantity.toArray(new String[listingquantity.size()]);

        final String[] side = listingside.toArray(new String[listingside.size()]);
        final String[] drink = listingdrink.toArray(new String[listingdrink.size()]);

        final String[] veggie = listingveggie.toArray(new String[listingveggie.size()]);
        final String[] hotlevel = listinghotlevel.toArray(new String[listinghotlevel.size()]);
        final String[] extra = listingextra.toArray(new String[listingextra.size()]);
        final String[] notes = listingnotes.toArray(new String[listingnotes.size()]);


        ListsAdapter adapter = new
                ListsAdapter(getActivity(), meal, price, quantity, side, drink, veggie, hotlevel, extra, notes);
        list = (ListView) rootView.findViewById(R.id.list_lists);
        list.setAdapter(adapter);
        listingdrink.clear();
        listingprice.clear();
        listingprice.clear();
        listingquantity.clear();
        listingside.clear();
        listingveggie.clear();
        listinghotlevel.clear();
        listingextra.clear();
        listingnotes.clear();
        setListViewHeightBasedOnChildren(list);

        for (String s : price) {

            counter = counter + Double.parseDouble(s);
        }


        editor.putString("TotalOrderPrice", counter.toString());

        editor.commit(); // commit changes

        final ArrayList<LinearLayout> rows = new ArrayList<LinearLayout>();
        final ArrayList<LinearLayout> row = new ArrayList<LinearLayout>();
        final ArrayList<LinearLayout> ro = new ArrayList<LinearLayout>();
        final String TotalPrice = pref.getString("TotalOrderPrice", null);


        Button orderButton = (Button) rootView.findViewById(R.id.buttonOrder);
        orderButton.setTypeface(face);
        orderButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager input = (InputMethodManager) getActivity()
                            .getSystemService(Activity.INPUT_METHOD_SERVICE);
                    input.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                int selected = choiceRadioGroup1.getCheckedRadioButtonId();
                int selected1 = choiceRadioGroup2.getCheckedRadioButtonId();
                int selected2 = choiceRadioGroup3.getCheckedRadioButtonId();

                if (selected == -1) {
                    Toast toast = Toast.makeText(getActivity(), "Please select Delivery or Collection", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (selected1 == -1) {
                    Toast toast = Toast.makeText(getActivity(), "Please select a Payment Method", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (selected2 == -1) {
                    Toast toast = Toast.makeText(getActivity(), "Please select a Order Date", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (se == null && cDelivery == 1) {
                    Toast toast = Toast.makeText(getActivity(), "Please add an address for delivery", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {


                    if (payOnline == true) {

                        String jsonString = "";

                        try {

                            JSONObject jsonObject = new JSONObject();


                            jsonObject.put("name", name.getText().toString());
                            jsonObject.put("surname", surname.getText().toString());

                            jsonObject.put("number", number.getText().toString());
                            jsonObject.put("email", email.getText().toString());


                            jsonString = jsonObject.toString();

                            SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                            SharedPreferences.Editor editor = pref.edit();
                            String user = "";

                            user = jsonString;

                            editor.putString("User", user);

                            editor.commit(); // commit changes

                        } catch (Exception e) {

                        }
                        new placeOrderPayment().execute();

                        ((HomeActivity) getActivity()).displayView(9);
                    } else {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                getActivity());

                        // set title
                        alertDialogBuilder.setTitle("Order Policy");


                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Thank you for Placing an order with Nando's " + pref.getString("StoreName", null) + ", please note that Refunds will not be accepted. To continue please press 'Order'")
                                .setCancelable(false)
                                .setPositiveButton("Order", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        String jsonString = "";

                                        try {

                                            JSONObject jsonObject = new JSONObject();


                                            jsonObject.put("name", name.getText().toString());
                                            jsonObject.put("surname", surname.getText().toString());

                                            jsonObject.put("number", number.getText().toString());
                                            jsonObject.put("email", email.getText().toString());


                                            jsonString = jsonObject.toString();

                                            SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                                            SharedPreferences.Editor editor = pref.edit();
                                            String user = "";

                                            user = jsonString;

                                            editor.putString("User", user);

                                            editor.commit(); // commit changes

                                        } catch (Exception e) {

                                        }

                                        new placeOrder().execute();

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
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();


                    }


                }
            }

        });

        choiceRadioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb = (RadioButton) rootView.findViewById(checkedId);

                if (rb.getText().toString().equalsIgnoreCase("Pay Online (PayU)")) {
                    LinearLayout mainLayout = (LinearLayout) rootView.findViewById(R.id.totalBucket);
                    LinearLayout linearlayout1 = new LinearLayout(getActivity());
                    linearlayout1.setOrientation(LinearLayout.VERTICAL);

                    payOnline = true;


                    if (rows.size() >= 1) {

                        LinearLayout layoutToRemove = rows.get(rows.size() - 1);
                        mainLayout.removeView(layoutToRemove); // remove it
                        rows.remove(layoutToRemove); // update the list
                        mainLayout.invalidate(); // may need this (optional)
                    }


                    onlinefee = 2 + (Double.parseDouble(TotalPrice) * 3.2 / 100);
                    TextView online = new TextView(getActivity());
                    online.setText("Online Fee: R" + String.format("%.2f", onlinefee));
                    online.setTextSize(18);
                    online.setTypeface(face);
                    linearlayout1.addView(online);

                    Double Total = Double.parseDouble(TotalPrice) + app + onlinefee;
                    TextView grandtotal = new TextView(getActivity());
                    grandtotal.setText("Total: R" + String.format("%.2f", Total));
                    grandtotal.setTextSize(24);
                    grandtotal.setTypeface(face);
                    linearlayout1.addView(grandtotal);
                    mainLayout.addView(linearlayout1);
                    rows.add(linearlayout1);

                    cAppfee = 2.0;
                    cGateway = onlinefee;
                    cPrice = Double.parseDouble(TotalPrice);
                    cTotalPrice = Total;


                }

                if (rb.getText().toString().equalsIgnoreCase("Pay In-Store/Driver")) {
                    LinearLayout mainLayout = (LinearLayout) rootView.findViewById(R.id.totalBucket);
                    LinearLayout linearlayout1 = new LinearLayout(getActivity());
                    linearlayout1.setOrientation(LinearLayout.VERTICAL);

                    payOnline = false;

                    if (rows.size() >= 1) {
                        LinearLayout layoutToRemove = rows.get(rows.size() - 1);
                        mainLayout.removeView(layoutToRemove); // remove it
                        rows.remove(layoutToRemove); // update the list
                        mainLayout.invalidate(); // may need this (optional)
                    }

                    Double Total = Double.parseDouble(TotalPrice) + app;
                    TextView grandtotal = new TextView(getActivity());
                    grandtotal.setText("Total: R" + String.format("%.2f", Total));
                    grandtotal.setTextSize(24);
                    grandtotal.setTypeface(face);
                    linearlayout1.addView(grandtotal);
                    mainLayout.addView(linearlayout1);
                    rows.add(linearlayout1);

                    cAppfee = 2.0;
                    cGateway = 0.0;
                    cPaid = 0;
                    cPrice = Double.parseDouble(TotalPrice);
                    cTotalPrice = Total;

                }

            }
        });

        choiceRadioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb = (RadioButton) rootView.findViewById(checkedId);

                if (rb.getText().toString().equalsIgnoreCase("Delivery")) {
                    LinearLayout mainLayout = (LinearLayout) rootView.findViewById(R.id.details);
                    LinearLayout linearlayout1 = new LinearLayout(getActivity());
                    linearlayout1.setOrientation(LinearLayout.VERTICAL);

                    cDelivery = 1;
                    cCollection = 0;

                    if (row.size() >= 1) {

                        LinearLayout layoutToRemove = row.get(row.size() - 1);
                        mainLayout.removeView(layoutToRemove); // remove it
                        row.remove(layoutToRemove); // update the list
                        mainLayout.invalidate(); // may need this (optional)
                    }

                    TextView tname = new TextView(getActivity());
                    tname.setText("Name:");
                    tname.setTypeface(face);
                    tname.setTextColor(Color.parseColor("#5c4a3d"));
                    tname.setTextSize(14);
                    linearlayout1.addView(tname);

                    name = new EditText(getActivity());
                    name.setBackgroundResource(R.drawable.rect_text_edit_border);
                    cName = name.getText().toString();
                    linearlayout1.addView(name);

                    TextView tsurname = new TextView(getActivity());
                    tsurname.setText("Surname:");
                    tsurname.setTypeface(face);
                    tsurname.setTextColor(Color.parseColor("#5c4a3d"));
                    tsurname.setTextSize(14);
                    linearlayout1.addView(tsurname);


                    surname = new EditText(getActivity());
                    surname.setBackgroundResource(R.drawable.rect_text_edit_border);
                    cSurname = surname.getText().toString();
                    linearlayout1.addView(surname);

                    TextView tnumber = new TextView(getActivity());
                    tnumber.setText("Number:");
                    tnumber.setTypeface(face);
                    tnumber.setTextColor(Color.parseColor("#5c4a3d"));
                    tnumber.setTextSize(14);
                    linearlayout1.addView(tnumber);

                    number = new EditText(getActivity());
                    number.setBackgroundResource(R.drawable.rect_text_edit_border);
                    cNumber = number.getText().toString();
                    linearlayout1.addView(number);

                    TextView temail = new TextView(getActivity());
                    temail.setText("Email:");
                    temail.setTypeface(face);
                    temail.setTextColor(Color.parseColor("#5c4a3d"));
                    temail.setTextSize(14);
                    linearlayout1.addView(temail);

                    email = new EditText(getActivity());
                    email.setBackgroundResource(R.drawable.rect_text_edit_border);
                    cEmail = email.getText().toString();
                    linearlayout1.addView(email);

                    TextView taddress = new TextView(getActivity());
                    taddress.setText("My Address Book:");
                    taddress.setTypeface(face);
                    taddress.setTextColor(Color.parseColor("#5c4a3d"));
                    taddress.setTextSize(14);
                    linearlayout1.addView(taddress);

                    address = new EditText(getActivity());

                    useAddress = true;

                    final float scale = getActivity().getResources().getDisplayMetrics().density;

                    addAddress = new Button(getActivity());

                    addAddress.setText("Add New Address");


                    addAddress.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (50 * scale)));
                    addAddress.setTypeface(face);
                    addAddress.setTextSize(18);
                    addAddress.setTextColor(getResources().getColor(R.color.white));
                    addAddress.setBackgroundResource(R.drawable.buttonpress);

                    addAddress.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            String jsonString = "";

                            try {

                                JSONObject jsonObject = new JSONObject();


                                jsonObject.put("name", name.getText().toString());
                                jsonObject.put("surname", surname.getText().toString());

                                jsonObject.put("number", number.getText().toString());
                                jsonObject.put("email", email.getText().toString());


                                jsonString = jsonObject.toString();

                                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                                SharedPreferences.Editor editor = pref.edit();
                                String user = "";

                                user = jsonString;

                                editor.putString("User", user);

                                editor.commit(); // commit changes

                            } catch (Exception e) {

                            }
                            LayoutInflater inflater1 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final View formElementsView = inflater1.inflate(R.layout.fragment_address_dialog,
                                    null, false);


                            TextView name1 = (TextView) formElementsView.findViewById(R.id.textView1);
                            name1.setTypeface(face);
                            name1.setTextColor(getActivity().getResources().getColor(R.color.brown));
                            TextView street1 = (TextView) formElementsView.findViewById(R.id.textView2);
                            street1.setTypeface(face);
                            street1.setTextColor(getActivity().getResources().getColor(R.color.brown));
                            TextView unitno1 = (TextView) formElementsView.findViewById(R.id.textView3);
                            unitno1.setTypeface(face);
                            unitno1.setTextColor(getActivity().getResources().getColor(R.color.brown));
                            TextView complex1 = (TextView) formElementsView.findViewById(R.id.textView4);
                            complex1.setTypeface(face);
                            complex1.setTextColor(getActivity().getResources().getColor(R.color.brown));
                            TextView suburb1 = (TextView) formElementsView.findViewById(R.id.textView5);
                            suburb1.setTypeface(face);
                            suburb1.setTextColor(getActivity().getResources().getColor(R.color.brown));
                            TextView city1 = (TextView) formElementsView.findViewById(R.id.textView6);
                            city1.setTypeface(face);
                            city1.setTextColor(getActivity().getResources().getColor(R.color.brown));

                            TextView province1 = (TextView) formElementsView.findViewById(R.id.textView7);
                            province1.setTypeface(face);
                            province1.setTextColor(getActivity().getResources().getColor(R.color.brown));

                            nameadd = (EditText) formElementsView.findViewById(R.id.rg_name);

                            street = (EditText) formElementsView.findViewById(R.id.rg_street);

                            unitno = (EditText) formElementsView.findViewById(R.id.rg_unitno);

                            complex = (EditText) formElementsView.findViewById(R.id.rg_complex);

                            suburb = (EditText) formElementsView.findViewById(R.id.rg_suburb);

                            city = (EditText) formElementsView.findViewById(R.id.rg_city);

                            province = (Spinner) formElementsView.findViewById(R.id.addspin);
                            String[] strings = {"Gauteng", "Western Cape", "Eastern Cape", "Free State", "KwaZulu-Natal", "Limpopo", "Mpumalanga", "Northern Cape", "North West"};


                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_dropdown_item, strings);
                            province.setAdapter(spinnerArrayAdapter);

                            final TextView headingadd = (TextView) formElementsView
                                    .findViewById(R.id.heading);
                            headingadd.setTypeface(face);

                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    getActivity()).setView(formElementsView);

                            LayoutInflater inflater2 = getActivity().getLayoutInflater();
                            View view = inflater2.inflate(R.layout.tool_bar_order, null);

                            TextView header = (TextView) view
                                    .findViewById(R.id.toolbartitle);
                            header.setTypeface(face);
                            header.setText("Address Book");
                            alertDialogBuilder.setCustomTitle(view);


                            alertDialogBuilder
                                    .setCancelable(false)
                                    .setPositiveButton("Add Address", new DialogInterface.OnClickListener() {


                                        public void onClick(DialogInterface dialog, int id) {
                                            // if this button is clicked, close
                                            // current activity
                                            new reg().execute();

                                        }

                                    })
                                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // if this button is clicked, just close
                                            // the dialog box and do nothing
                                            alertDialog.dismiss();
                                            dialog.cancel();
                                        }
                                    });

                            // create alert dialog
                            alertDialog = alertDialogBuilder.create();

                            // show it
                            alertDialog.show();
                        }

                    });


                    SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    String User = "";
                    if (pref.getString("User", null) != null && (!pref.getString("User", null).equalsIgnoreCase(""))) {
                        User = pref.getString("User", null);
                        try {
                            JSONObject jsonObjorder = new JSONObject(User);

                            name.setText(jsonObjorder.getString("name"));
                            surname.setText(jsonObjorder.getString("surname"));
                            number.setText(jsonObjorder.getString("number"));

                            email.setText(jsonObjorder.getString("email"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (!listingname.isEmpty()) {
                        se = new Spinner(getActivity());
                        se.setBackgroundResource(R.drawable.dropdown);
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_dropdown_item, listingname);
                        se.setAdapter(spinnerArrayAdapter);
                        linearlayout1.addView(se);

                        TextView add = new TextView(getActivity());
                        add.setText("Add Address:");
                        add.setTypeface(face);
                        add.setTextColor(Color.parseColor("#5c4a3d"));
                        add.setTextSize(14);
                        linearlayout1.addView(add);

                        linearlayout1.addView(addAddress);

                    } else {
                        linearlayout1.addView(addAddress);
                    }

                    mainLayout.addView(linearlayout1);
                    row.add(linearlayout1);


                }

                if (rb.getText().toString().equalsIgnoreCase("Collection")) {
                    LinearLayout mainLayout = (LinearLayout) rootView.findViewById(R.id.details);
                    LinearLayout linearlayout1 = new LinearLayout(getActivity());
                    linearlayout1.setOrientation(LinearLayout.VERTICAL);

                    cDelivery = 0;
                    cCollection = 1;

                    if (row.size() >= 1) {
                        LinearLayout layoutToRemove = row.get(row.size() - 1);
                        mainLayout.removeView(layoutToRemove); // remove it
                        row.remove(layoutToRemove); // update the list
                        mainLayout.invalidate(); // may need this (optional)
                    }

                    TextView tname = new TextView(getActivity());
                    tname.setText("Name:");
                    tname.setTypeface(face);
                    tname.setTextColor(Color.parseColor("#5c4a3d"));
                    tname.setTextSize(14);
                    linearlayout1.addView(tname);

                    name = new EditText(getActivity());
                    name.setBackgroundResource(R.drawable.rect_text_edit_border);
                    cName = name.getText().toString();
                    linearlayout1.addView(name);

                    TextView tsurname = new TextView(getActivity());
                    tsurname.setText("Surname:");
                    tsurname.setTypeface(face);
                    tsurname.setTextColor(Color.parseColor("#5c4a3d"));
                    tsurname.setTextSize(14);
                    linearlayout1.addView(tsurname);

                    surname = new EditText(getActivity());
                    surname.setBackgroundResource(R.drawable.rect_text_edit_border);
                    cSurname = surname.getText().toString();
                    linearlayout1.addView(surname);

                    TextView tnumber = new TextView(getActivity());
                    tnumber.setText("Number:");
                    tnumber.setTypeface(face);
                    tnumber.setTextColor(Color.parseColor("#5c4a3d"));
                    tnumber.setTextSize(14);
                    linearlayout1.addView(tnumber);

                    number = new EditText(getActivity());
                    number.setBackgroundResource(R.drawable.rect_text_edit_border);
                    cNumber = number.getText().toString();
                    linearlayout1.addView(number);


                    TextView temail = new TextView(getActivity());
                    temail.setText("Email:");
                    temail.setTypeface(face);
                    temail.setTextColor(Color.parseColor("#5c4a3d"));
                    temail.setTextSize(14);
                    linearlayout1.addView(temail);

                    email = new EditText(getActivity());
                    email.setBackgroundResource(R.drawable.rect_text_edit_border);
                    cEmail = email.getText().toString();
                    linearlayout1.addView(email);

                    useAddress = false;

                    SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    String User = "";
                    if (pref.getString("User", null) != null && (!pref.getString("User", null).equalsIgnoreCase(""))) {
                        User = pref.getString("User", null);
                        try {
                            JSONObject jsonObjorder = new JSONObject(User);

                            name.setText(jsonObjorder.getString("name"));
                            surname.setText(jsonObjorder.getString("surname"));
                            number.setText(jsonObjorder.getString("number"));

                            email.setText(jsonObjorder.getString("email"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    mainLayout.addView(linearlayout1);
                    row.add(linearlayout1);

                }

            }
        });

        choiceRadioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb = (RadioButton) rootView.findViewById(checkedId);

                if (rb.getText().toString().equalsIgnoreCase("Today")) {
                    LinearLayout mainLayout = (LinearLayout) rootView.findViewById(R.id.orderDateSelect);
                    LinearLayout linearlayout1 = new LinearLayout(getActivity());
                    linearlayout1.setOrientation(LinearLayout.VERTICAL);

                    cPreorder = 0;
                    cOrderDate = "";

                    if (ro.size() >= 1) {
                        LinearLayout layoutToRemove = ro.get(ro.size() - 1);
                        mainLayout.removeView(layoutToRemove); // remove it
                        ro.remove(layoutToRemove); // update the list
                        mainLayout.invalidate(); // may need this (optional)
                    }

                    DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                    Date now = new Date();

                    dayDate = dateFormat1.format(now);

                    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    Date date = new Date();


                    String Date = dateFormat.format(date);

                    String[] items = {"10:30:00", "11:00:00", "11:30:00", "12:00:00", "12:30:00", "13:00:00", "13:30:00", "14:00:00", "14:30:00", "15:00:00", "15:30:00", "16:00:00", "16:30:00", "17:00:00", "17:30:00", "18:00:00", "18:30:00", "19:00:00", "19:30:00", "20:00:00", "20:30:00"};
                    List<String> newItems = new ArrayList<String>();
                    newItems.add("now");

                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    Date MyDate = null;
                    try {
                        MyDate = sdf.parse(Date);

                        for (String s : items) {
                            Date ToCheckAgainst = sdf.parse(s);

                            if (MyDate.before(ToCheckAgainst)) {
                                newItems.add(s);
                            }
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    s = new Spinner(getActivity());
                    s.setBackgroundResource(R.drawable.dropdown);
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_dropdown_item, newItems);
                    s.setAdapter(spinnerArrayAdapter);

                    choose = new TextView(getActivity());
                    choose.setText("Select Time:");
                    linearlayout1.addView(choose);

                    linearlayout1.addView(s);
                    mainLayout.addView(linearlayout1);
                    ro.add(linearlayout1);


                }

                if (rb.getText().toString().equalsIgnoreCase("Tomorrow")) {
                    LinearLayout mainLayout = (LinearLayout) rootView.findViewById(R.id.orderDateSelect);
                    LinearLayout linearlayout1 = new LinearLayout(getActivity());
                    linearlayout1.setOrientation(LinearLayout.VERTICAL);

                    cPreorder = 1;
                    cOrderDate = "";

                    if (ro.size() >= 1) {
                        LinearLayout layoutToRemove = ro.get(ro.size() - 1);
                        mainLayout.removeView(layoutToRemove); // remove it
                        ro.remove(layoutToRemove); // update the list
                        mainLayout.invalidate(); // may need this (optional)
                    }

                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date now = new Date();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(now);
                    cal.add(Calendar.DATE, 1);
                    Date date = cal.getTime();
                    System.out.println(date);


                    dayDate = dateFormat.format(date);


                    String[] strings = {"10:30:00", "11:00:00", "11:30:00", "12:00:00", "12:30:00", "13:00:00", "13:30:00", "14:00:00", "14:30:00", "15:00:00", "15:30:00", "16:00:00", "16:30:00", "17:00:00", "17:30:00", "18:00:00", "18:30:00", "19:00:00", "19:30:00", "20:00:00", "20:30:00"};

                    s = new Spinner(getActivity());
                    s.setBackgroundResource(R.drawable.dropdown);
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_dropdown_item, strings);
                    s.setAdapter(spinnerArrayAdapter);

                    choose = new TextView(getActivity());
                    choose.setText("Select Time:");
                    linearlayout1.addView(choose);

                    linearlayout1.addView(s);
                    mainLayout.addView(linearlayout1);
                    ro.add(linearlayout1);


                }

            }
        });

        //final RadioButton radiochoiceButton1;
        // final int selectedId1 = choiceRadioGroup1.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        // radiochoiceButton1 = (RadioButton) rootView .findViewById(selectedId1);
        // radiochoiceButton1.getText().toString();

        //final RadioButton radiochoiceButton2;
        //final int selectedId2 = choiceRadioGroup2.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        // radiochoiceButton2 = (RadioButton) rootView .findViewById(selectedId2);
        // radiochoiceButton2.getText().toString();


        LinearLayout mainLayout = (LinearLayout) rootView.findViewById(R.id.totalBucket);

        mainLayout.setOrientation(LinearLayout.VERTICAL);
        TextView due = new TextView(getActivity());
        due.setText("Total Due");
        due.setTypeface(face);
        due.setTextSize(24);
        mainLayout.addView(due);

        TextView tv = new TextView(getActivity());
        tv.setText("Order Amount: R" + String.format("%.2f", Double.parseDouble(TotalPrice)));
        tv.setTextSize(18);
        tv.setTypeface(face);
        mainLayout.addView(tv);


        TextView appfee = new TextView(getActivity());
        appfee.setText("App Fee: R" + String.format("%.2f", app));
        appfee.setTextSize(18);
        appfee.setTypeface(face);
        mainLayout.addView(appfee);


        return rootView;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.fab1:
                    menu1.close(true);
                    Toast.makeText(getActivity(), "Removed all orders", Toast.LENGTH_SHORT).show();
                    SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    editor.remove("Order");
                    editor.commit();
                    ((HomeActivity) getActivity()).displayView(0);
                    break;

            }

        }
    };

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
            listItem.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            //listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    private class placeOrder extends AsyncTask<String, String, String> {

        int res = 0;


        @Override
        protected void onPreExecute() {
            // Showing progress dialog before sending http request
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Placing Order...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String jsonString = "";
            String baseURL = "";
            SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();

            cOrder = pref.getString("Order", null);

            if (useAddress == true) {
                String address = "[" + pref.getString("Address", null) + "]";
                try {
                    JSONArray jsonObjorder = new JSONArray(address);

                    JSONObject ady = jsonObjorder.getJSONObject(se.getSelectedItemPosition());

                    cAddress = ady.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                cAddress = "{}";
            }


            System.out.println("cOrder" + cOrder);
            cGCM = pref.getString("GCM", null);
            cName = name.getText().toString();

            cSurname = surname.getText().toString();

            cNumber = number.getText().toString();

            cEmail = email.getText().toString();

            cOrderDate = dayDate + " " + s.getSelectedItem().toString();
            if (cOrderDate.equalsIgnoreCase(dayDate + " now")) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();

                cOrderDate = dateFormat.format(date);
            }

            try {

                String jsonObjorder = "[" + cOrder + "]";

                JSONArray orders = new JSONArray(jsonObjorder);

                JSONObject jsonObjadd = new JSONObject(cAddress);

                JSONArray addresses = new JSONArray();
                addresses.put(jsonObjadd);
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("name", cName);
                jsonObject.put("surname", cSurname);
                jsonObject.put("number", cNumber);
                jsonObject.put("email", cEmail);
                jsonObject.put("delivery", cDelivery);
                jsonObject.put("collection", cCollection);
                jsonObject.put("preorder", cPreorder);
                jsonObject.put("orderdate", cOrderDate);
                jsonObject.put("paid", cPaid);
                jsonObject.put("appfee", cAppfee);
                jsonObject.put("gateway", cGateway);
                jsonObject.put("price", cPrice);
                jsonObject.put("totalprice", cTotalPrice);
                jsonObject.put("gcm_regid", cGCM);
                jsonObject.put("reference", creference);
                jsonObject.put("address", addresses);
                jsonObject.put("order", orders);

                jsonString = jsonObject.toString();

                System.out.println("Serialized JSON Data ::" + jsonString);

                // JSON content to post
                String contentToPost = jsonString;
                // Create a URLConnection

                baseURL = "http://www.mydeliveries.co.za/ordermanager/nandos/orders";

                URL url = null;
                HttpURLConnection urlConnection = null;

                url = new URL(baseURL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");


                //pref.getString("StoreAPI", null)
                urlConnection.setRequestProperty("Authorization", pref.getString("StoreAPI", null));

                urlConnection.setRequestProperty("Content-Length", "" + contentToPost.length());
                // To post JSON content, set the content type to application/json OR application/jsonrequest
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Cache-Control", "no-cache"); // Post your content
                OutputStream stream = urlConnection.getOutputStream();
                stream.write(contentToPost.getBytes());
                stream.close();

                res = urlConnection.getResponseCode();
                System.out.println("***********" + res + "**************");


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String file) {
            // closing progress dialog
            if (pDialog != null) {
                pDialog.dismiss();
            }


            if (res == 200 || res == 201) {


                Toast toast = Toast.makeText(getActivity(), "Order Placed successfully", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();

                editor.remove("Order");

                editor.commit();

                ((HomeActivity) getActivity()).displayView(0);


            } else {


                Toast toast = Toast.makeText(getActivity(), "Error, please try again!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();


            }

            super.onPostExecute(file);
        }


    }

    private class placeOrderPayment extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            // Showing progress dialog before sending http request

        }

        @Override
        protected String doInBackground(String... params) {

            String jsonString = "";
            String baseURL = "";
            SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();

            cOrder = pref.getString("Order", null);

            if (useAddress == true) {
                String address = "[" + pref.getString("Address", null) + "]";
                try {
                    JSONArray jsonObjorder = new JSONArray(address);

                    JSONObject ady = jsonObjorder.getJSONObject(se.getSelectedItemPosition());

                    cAddress = ady.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                cAddress = "{}";
            }


            System.out.println("cOrder" + cOrder);
            cGCM = pref.getString("GCM", null);
            cName = name.getText().toString();

            cSurname = surname.getText().toString();

            cNumber = number.getText().toString();

            cEmail = email.getText().toString();

            cOrderDate = dayDate + " " + s.getSelectedItem().toString();
            if (cOrderDate.equalsIgnoreCase(dayDate + " now")) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();

                cOrderDate = dateFormat.format(date);
            }

            try {

                String jsonObjorder = "[" + cOrder + "]";

                JSONArray orders = new JSONArray(jsonObjorder);


                JSONObject jsonObjadd = new JSONObject(cAddress);

                JSONArray addresses = new JSONArray();
                addresses.put(jsonObjadd);
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("name", cName);
                jsonObject.put("surname", cSurname);
                jsonObject.put("number", cNumber);
                jsonObject.put("email", cEmail);
                jsonObject.put("delivery", cDelivery);
                jsonObject.put("collection", cCollection);
                jsonObject.put("preorder", cPreorder);
                jsonObject.put("orderdate", cOrderDate);
                jsonObject.put("paid", cPaid);
                jsonObject.put("appfee", cAppfee);
                jsonObject.put("gateway", cGateway);
                jsonObject.put("price", cPrice);
                jsonObject.put("totalprice", cTotalPrice);
                jsonObject.put("gcm_regid", cGCM);
                jsonObject.put("reference", creference);
                jsonObject.put("address", addresses);
                jsonObject.put("order", orders);

                jsonString = jsonObject.toString();

                System.out.println("Serialized JSON Data ::" + jsonString);

                editor.putString("PayUcomplete", jsonString);
                editor.putString("PayUprice", String.valueOf(cTotalPrice));

                editor.commit(); // commit changes

                // JSON content to post
                String contentToPost = jsonString;
                // Create a URLConnection


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String file) {
            // closing progress dialog


            super.onPostExecute(file);
        }


    }


    private class reg extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {


        }

        @Override
        protected String doInBackground(String... params) {

            String jsonString = "";

            try {

                JSONObject jsonObject = new JSONObject();


                jsonObject.put("name", nameadd.getText().toString());
                jsonObject.put("street", street.getText().toString());

                jsonObject.put("unitno", unitno.getText().toString());
                jsonObject.put("complex", complex.getText().toString());

                jsonObject.put("suburb", suburb.getText().toString());
                jsonObject.put("city", city.getText().toString());

                jsonObject.put("province", province.getSelectedItem().toString());


                jsonString = jsonObject.toString();

                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                String address = "";


                if (pref.getString("Address", null) != null && (!pref.getString("Address", null).equalsIgnoreCase(""))) {
                    address = pref.getString("Address", null) + "," + jsonString;
                } else {
                    address = jsonString;
                }


                editor.putString("Address", address);

                editor.commit(); // commit changes


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String file) {


            Toast toast = Toast.makeText(getActivity(), "Address Saved", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            ((HomeActivity) getActivity()).displayView(2);

            super.onPostExecute(file);
        }


    }


}
