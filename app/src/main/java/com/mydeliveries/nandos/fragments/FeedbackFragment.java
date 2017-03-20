package com.mydeliveries.nandos.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;


import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mydeliveries.nandos.R;
import com.mydeliveries.nandos.activity.HomeActivity;
import com.mydeliveries.nandos.app.AppController;
import com.mydeliveries.nandos.model.NavItem;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class FeedbackFragment extends Fragment {

    String version = "";

    TextView name;
    TextView surname;
    TextView Telephone;
    TextView Email;

    TextView name1;
    TextView surname1;
    TextView Telephone1;
    TextView Email1;
    TextView comments1;

    EditText comments;


    private ProgressDialog pDialog;


    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_feedback, container, false);

        // Get tracker.
        Tracker t = ((AppController) getActivity().getApplication()).getTracker(
                AppController.TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Feedback Page");

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());

        ((HomeActivity) getActivity()).loadDrawsHide();


        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((HomeActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        NavItem nav = new NavItem();
        nav.setPage("Home");

        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/webfont.ttf");
        final TextView heading = (TextView) rootView
                .findViewById(R.id.heading);
        heading.setTypeface(face);

        new UpdateApp().execute();


        name = (TextView) rootView.findViewById(R.id.fb_name);
        surname = (TextView) rootView.findViewById(R.id.fb_surname);
        Telephone = (TextView) rootView.findViewById(R.id.fb_tel_number);
        Email = (TextView) rootView.findViewById(R.id.fb_email);
        comments = (EditText) rootView.findViewById(R.id.fb_comments);

        name1 = (TextView) rootView.findViewById(R.id.textView5);
        surname1 = (TextView) rootView.findViewById(R.id.textView1);
        Telephone1 = (TextView) rootView.findViewById(R.id.textView2);
        Email1 = (TextView) rootView.findViewById(R.id.textView3);
        comments1 = (TextView) rootView.findViewById(R.id.textView4);

        name1.setTypeface(face);
        surname1.setTypeface(face);
        Telephone1.setTypeface(face);
        Email1.setTypeface(face);
        comments1.setTypeface(face);

        Button submit = (Button) rootView.findViewById(R.id.fb_send);
        submit.setTypeface(face);

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
                    errMsg = errMsg + "\nPlease enter your Name";
                } else {
                    name.setBackgroundResource(R.drawable.rect_text_edit_border);
                }
                if (surname.getText().toString().equals("")) {
                    surname.setBackgroundResource(R.drawable.rect_text_edit_border_red);
                    errMsg = errMsg + "\nPlease enter your Surname";
                } else {
                    surname.setBackgroundResource(R.drawable.rect_text_edit_border);
                }
                if (Telephone.getText().toString().equals("")) {
                    Telephone.setBackgroundResource(R.drawable.rect_text_edit_border_red);
                    errMsg = errMsg + "\nPlease enter your Number";
                } else {
                    Telephone.setBackgroundResource(R.drawable.rect_text_edit_border);
                }
                if (Email.getText().toString().equals("")) {
                    Email.setBackgroundResource(R.drawable.rect_text_edit_border_red);
                    errMsg = errMsg + "\nPlease enter your Email Address";
                } else {
                    Email.setBackgroundResource(R.drawable.rect_text_edit_border);
                }
                if (comments.getText().toString().equals("")) {
                    comments.setBackgroundResource(R.drawable.rect_text_edit_border_red);
                    errMsg = errMsg + "\nPlease enter your Comment";
                } else {
                    comments.setBackgroundResource(R.drawable.rect_text_edit_border);
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString()).matches()) {
                    Email.setBackgroundResource(R.drawable.rect_text_edit_border_red);
                    errMsg = errMsg + "\nInvalid Email Address";
                }
                if (!android.util.Patterns.PHONE.matcher(Telephone.getText().toString()).matches() || Telephone.length() < 10) {
                    Telephone.setBackgroundResource(R.drawable.rect_text_edit_border_red);
                    errMsg = errMsg + "\nInvalid Telephone Number";
                }

                if (!errMsg.equalsIgnoreCase("")) {
                    Toast toast = Toast.makeText(getActivity(), errMsg + "\n", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {

                    new sendfeedback().execute();
                }


            }
        });


        return rootView;
    }

    private class sendfeedback extends AsyncTask<String, String, String> {

        int res = 0;


        @Override
        protected void onPreExecute() {
            // Showing progress dialog before sending http request
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Sending...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String jsonString = "";
            String baseURL = "";


            try {

                JSONObject jsonObject = new JSONObject();


                jsonObject.put("name", name.getText().toString());
                jsonObject.put("surname", surname.getText().toString());
                jsonObject.put("number", Telephone.getText().toString());

                jsonObject.put("email", Email.getText().toString());
                jsonObject.put("comments", "Version: " + version + " ,Android feedback: " + comments.getText().toString());

                jsonString = jsonObject.toString();

                System.out.println("Serialized JSON Data ::" + jsonString);

                // JSON content to post
                String contentToPost = jsonString;
                // Create a URLConnection

                baseURL = "http://www.mydeliveries.co.za/ordermanager/nandos/feedback";

                URL url = null;
                HttpURLConnection urlConnection = null;

                url = new URL(baseURL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Authorization", "057f774e10cd69ce1ad7ea2e3ac708d5");

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

            name.setText("");
            surname.setText("");
            Telephone.setText("");
            Email.setText("");
            comments.setText("");

            if (res == 201) {


                Toast toast = Toast.makeText(getActivity(), "Feedback sent successfully", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();


            } else {


                Toast toast = Toast.makeText(getActivity(), "Error Sending , please try again!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();


            }

            super.onPostExecute(file);
        }


    }

    class UpdateApp extends AsyncTask<String, String, String> {

        String str = "";
        double versionvalue = 0;

        StringBuilder text = new StringBuilder();
        boolean update = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        /**
         * getting Places JSON
         */
        protected String doInBackground(String... args) {
            // creating Places class object


            try {

                URL url = new URL("http://www.mydeliveries.co.za/apk.txt");

                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

                while ((str = in.readLine()) != null) {
                    text.append(str);
                }
                in.close();
            } catch (MalformedURLException e1) {
            } catch (IOException e) {
            } catch (Exception e) {

            }
            try {
                PackageManager manager = getActivity().getPackageManager();
                PackageInfo info;

                info = manager.getPackageInfo(getActivity().getPackageName(), 0);
                versionvalue = Double.parseDouble(info.versionName);
                System.out.println("version - " + versionvalue);
                double appVersion = Double.parseDouble(text.toString());
                System.out.println("appVersion - " + appVersion);
                if (versionvalue == appVersion) {
                    update = false;
                } else {
                    update = true;
                }

            } catch (NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {

            }


            return null;
        }


        protected void onPostExecute(String file_url) {

            version = String.valueOf(versionvalue);
            super.onPostExecute(file_url);

        }


    }

}
