package com.mydeliveries.nandos.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mydeliveries.nandos.R;
import com.mydeliveries.nandos.util.AlertDialogManager;
import com.mydeliveries.nandos.activity.HomeActivity;
import com.mydeliveries.nandos.app.AppController;
import com.mydeliveries.nandos.model.NavItem;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddressFragment extends Fragment {


    EditText name;
    EditText street;
    EditText unitno;
    EditText complex;
    EditText suburb;
    EditText city;
    Spinner province;
    String url = "";

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();
    private List<String> Profile = new ArrayList<String>();

    private ProgressDialog pDialog;

    String pos = "";

    String addDropdown = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_address, container, false);

        // Get tracker.
        Tracker t = ((AppController) getActivity().getApplication()).getTracker(
                AppController.TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Address Page");

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());


        NavItem nav = new NavItem();

        nav.setPage("AddressBook");
        Typeface face1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/webfont.ttf");


        final TextView heading = (TextView) rootView
                .findViewById(R.id.heading);
        heading.setTypeface(face1);

        ((HomeActivity) getActivity()).loadDrawsHide();


        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((HomeActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);


        TextView name1 = (TextView) rootView.findViewById(R.id.textView1);
        name1.setTypeface(face1);
        name1.setTextColor(getActivity().getResources().getColor(R.color.brown));
        TextView street1 = (TextView) rootView.findViewById(R.id.textView2);
        street1.setTypeface(face1);
        street1.setTextColor(getActivity().getResources().getColor(R.color.brown));
        TextView unitno1 = (TextView) rootView.findViewById(R.id.textView3);
        unitno1.setTypeface(face1);
        unitno1.setTextColor(getActivity().getResources().getColor(R.color.brown));
        TextView complex1 = (TextView) rootView.findViewById(R.id.textView4);
        complex1.setTypeface(face1);
        complex1.setTextColor(getActivity().getResources().getColor(R.color.brown));
        TextView suburb1 = (TextView) rootView.findViewById(R.id.textView5);
        suburb1.setTypeface(face1);
        suburb1.setTextColor(getActivity().getResources().getColor(R.color.brown));
        TextView city1 = (TextView) rootView.findViewById(R.id.textView6);
        city1.setTypeface(face1);
        city1.setTextColor(getActivity().getResources().getColor(R.color.brown));

        TextView province1 = (TextView) rootView.findViewById(R.id.textView7);
        province1.setTypeface(face1);
        province1.setTextColor(getActivity().getResources().getColor(R.color.brown));

        name = (EditText) rootView.findViewById(R.id.rg_name);

        street = (EditText) rootView.findViewById(R.id.rg_street);

        unitno = (EditText) rootView.findViewById(R.id.rg_unitno);

        complex = (EditText) rootView.findViewById(R.id.rg_complex);

        suburb = (EditText) rootView.findViewById(R.id.rg_suburb);

        city = (EditText) rootView.findViewById(R.id.rg_city);

        province = (Spinner) rootView.findViewById(R.id.addspin);

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
        Editor editor = pref.edit();

        if (pref.getString("TempAddress", null) != null && (!pref.getString("TempAddress", null).equalsIgnoreCase(""))) {
            String TempAddress = pref.getString("TempAddress", null);
            try {
                JSONObject jsonObjorder = new JSONObject(TempAddress);

                name.setText(jsonObjorder.getString("name"));
                street.setText(jsonObjorder.getString("street"));
                unitno.setText(jsonObjorder.getString("unitno"));
                complex.setText(jsonObjorder.getString("complex"));
                suburb.setText(jsonObjorder.getString("suburb"));
                city.setText(jsonObjorder.getString("city"));
                pos = jsonObjorder.getString("pos");
                addDropdown = jsonObjorder.getString("province");


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        province = (Spinner) rootView.findViewById(R.id.addspin);
        String[] strings = null;
        if (addDropdown.equalsIgnoreCase("") || addDropdown == null) {
            strings = new String[]{"Gauteng", "Western Cape", "Eastern Cape", "Free State", "KwaZulu-Natal", "Limpopo", "Mpumalanga", "Northern Cape", "North West"};

        } else {
            strings = new String[]{addDropdown, "Gauteng", "Western Cape", "Eastern Cape", "Free State", "KwaZulu-Natal", "Limpopo", "Mpumalanga", "Northern Cape", "North West"};

        }


        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_dropdown_item, strings);
        province.setAdapter(spinnerArrayAdapter);

        Button submit = (Button) rootView.findViewById(R.id.rg_save);
        submit.setTypeface(face1);

        submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                String errMsg = "";
                if (name.getText().toString().equals("")) {
                    name.setBackgroundResource(R.drawable.rect_text_edit_border_red);
                    errMsg = errMsg + "\nPlease enter the name to save";
                } else {
                    name.setBackgroundResource(R.drawable.rect_text_edit_border);
                }
                if (street.getText().toString().equals("")) {
                    street.setBackgroundResource(R.drawable.rect_text_edit_border_red);
                    errMsg = errMsg + "\nPlease enter your Street";
                } else {
                    street.setBackgroundResource(R.drawable.rect_text_edit_border);
                }

                if (suburb.getText().toString().equals("")) {
                    suburb.setBackgroundResource(R.drawable.rect_text_edit_border_red);
                    errMsg = errMsg + "\nPlease enter your Suburb";
                } else {
                    suburb.setBackgroundResource(R.drawable.rect_text_edit_border);
                }
                if (city.getText().toString().equals("")) {
                    city.setBackgroundResource(R.drawable.rect_text_edit_border_red);
                    errMsg = errMsg + "\nPlease enter your city";
                } else {
                    city.setBackgroundResource(R.drawable.rect_text_edit_border);
                }


                if (!errMsg.equalsIgnoreCase("")) {
                    Toast toast = Toast.makeText(getActivity(), errMsg + "\n", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {

                    new reg().execute();
                }


            }
        });


        return rootView;
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


                jsonObject.put("name", name.getText().toString());
                jsonObject.put("street", street.getText().toString());

                jsonObject.put("unitno", unitno.getText().toString());
                jsonObject.put("complex", complex.getText().toString());

                jsonObject.put("suburb", suburb.getText().toString());
                jsonObject.put("city", city.getText().toString());

                jsonObject.put("province", province.getSelectedItem().toString());


                jsonString = jsonObject.toString();

                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                Editor editor = pref.edit();
                String address = "";
                String newAddress = "";

                if (!pos.equalsIgnoreCase("")) {
                    address = "[" + pref.getString("Address", null) + "]";
                    JSONArray oparray = null;
                    try {
                        oparray = new JSONArray(address);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    JSONObject update = oparray.getJSONObject(Integer.parseInt(pos));


                    update.put("name", name.getText().toString());
                    update.put("street", street.getText().toString());
                    update.put("unitno", unitno.getText().toString());
                    update.put("complex", complex.getText().toString());
                    update.put("suburb", suburb.getText().toString());
                    update.put("city", city.getText().toString());
                    update.put("province", province.getSelectedItem().toString());


                    if (oparray.length() == 1) {
                        JSONObject object = oparray.getJSONObject(0);

                        newAddress = object.toString();
                    } else {
                        for (int n = 0; n < oparray.length(); n++) {
                            JSONObject object = oparray.getJSONObject(n);
                            System.out.println("for" + object);
                            if (newAddress != null && (!newAddress.equalsIgnoreCase(""))) {
                                newAddress = newAddress + "," + object.toString();
                            } else {
                                newAddress = object.toString();
                            }


                        }
                    }
                    System.out.println("for" + newAddress);

                    editor.putString("Address", newAddress);

                    editor.commit(); // commit changes

                } else {


                    if (pref.getString("Address", null) != null && (!pref.getString("Address", null).equalsIgnoreCase(""))) {
                        address = pref.getString("Address", null) + "," + jsonString;
                    } else {
                        address = jsonString;
                    }


                    editor.putString("Address", address);

                    editor.commit(); // commit changes

                }


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

            super.onPostExecute(file);
        }


    }


}
