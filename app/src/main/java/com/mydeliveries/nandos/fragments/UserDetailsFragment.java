package com.mydeliveries.nandos.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.mydeliveries.nandos.R;
import com.mydeliveries.nandos.util.AlertDialogManager;
import com.mydeliveries.nandos.activity.HomeActivity;
import com.mydeliveries.nandos.app.AppController;
import com.mydeliveries.nandos.model.NavItem;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserDetailsFragment extends Fragment {


    TextView uname;
    TextView usurname;
    TextView unumber;
    TextView uemail;


    String url = "";
    String authStringEnc = "";


    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();
    private List<String> Profile = new ArrayList<String>();

    private ProgressDialog pDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_user, container, false);

        // Get tracker.
        Tracker t = ((AppController) getActivity().getApplication()).getTracker(
                AppController.TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Profile Page");

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());


        NavItem nav = new NavItem();

        nav.setPage("Profile");


        ((HomeActivity) getActivity()).loadDrawsHide();


        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((HomeActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/webfont.ttf");

        TextView tx1 = (TextView) rootView.findViewById(R.id.textView1);
        TextView tx2 = (TextView) rootView.findViewById(R.id.textView2);
        TextView tx3 = (TextView) rootView.findViewById(R.id.textView3);

        TextView tx4 = (TextView) rootView.findViewById(R.id.textView4);

        tx1.setTypeface(face);
        tx2.setTypeface(face);
        tx3.setTypeface(face);
        tx4.setTypeface(face);

        final TextView heading = (TextView) rootView
                .findViewById(R.id.heading);
        heading.setTypeface(face);

        uname = (TextView) rootView.findViewById(R.id.rg_name);
        usurname = (TextView) rootView.findViewById(R.id.rg_lastname);
        unumber = (TextView) rootView.findViewById(R.id.rg_number);

        uemail = (TextView) rootView.findViewById(R.id.rg_email);

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        String User = "";
        if (pref.getString("User", null) != null && (!pref.getString("User", null).equalsIgnoreCase(""))) {
            User = pref.getString("User", null);
            try {
                JSONObject jsonObjorder = new JSONObject(User);

                uname.setText(jsonObjorder.getString("name"));
                usurname.setText(jsonObjorder.getString("surname"));
                unumber.setText(jsonObjorder.getString("number"));

                uemail.setText(jsonObjorder.getString("email"));


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        Button submit = (Button) rootView.findViewById(R.id.rg_send);

        submit.setTypeface(face);

        submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                String errMsg = "";
                if (uname.getText().toString().equals("")) {
                    uname.setBackgroundResource(R.drawable.rect_text_edit_border_red);
                    errMsg = errMsg + "\nPlease enter your Name";
                } else {
                    uname.setBackgroundResource(R.drawable.rect_text_edit_border);
                }
                if (usurname.getText().toString().equals("")) {
                    usurname.setBackgroundResource(R.drawable.rect_text_edit_border_red);
                    errMsg = errMsg + "\nPlease enter your Surname";
                } else {
                    usurname.setBackgroundResource(R.drawable.rect_text_edit_border);
                }
                if (uemail.getText().toString().equals("")) {
                    uemail.setBackgroundResource(R.drawable.rect_text_edit_border_red);
                    errMsg = errMsg + "\nPlease enter your Email Address";
                } else {
                    uemail.setBackgroundResource(R.drawable.rect_text_edit_border);
                }


                if (unumber.getText().toString().equals("") || unumber.length() < 10) {
                    unumber.setBackgroundResource(R.drawable.rect_text_edit_border_red);
                    errMsg = errMsg + "\nPlease enter a valid Telephone Number";
                } else {
                    unumber.setBackgroundResource(R.drawable.rect_text_edit_border);
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(uemail.getText().toString()).matches()) {
                    uemail.setBackgroundResource(R.drawable.rect_text_edit_border_red);
                    errMsg = errMsg + "\nInvalid Email Address";
                }
                if (!android.util.Patterns.PHONE.matcher(unumber.getText().toString()).matches()) {
                    unumber.setBackgroundResource(R.drawable.rect_text_edit_border_red);
                    errMsg = errMsg + "\nInvalid Telephone Number";
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
            // Showing progress dialog before sending http request

        }

        @Override
        protected String doInBackground(String... params) {

            String jsonString = "";

            try {

                JSONObject jsonObject = new JSONObject();


                jsonObject.put("name", uname.getText().toString());
                jsonObject.put("surname", usurname.getText().toString());

                jsonObject.put("number", unumber.getText().toString());
                jsonObject.put("email", uemail.getText().toString());


                jsonString = jsonObject.toString();

                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                String user = "";

                user = jsonString;

                editor.putString("User", user);

                editor.commit(); // commit changes


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String file) {
            // closing progress dialog
            ((HomeActivity) getActivity()).loadDraws();
            Toast toast = Toast.makeText(getActivity(), "User saved Successfully", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            super.onPostExecute(file);
        }


    }


}
