package com.mydeliveries.nandos.fragments;


import com.mydeliveries.nandos.R;
import com.mydeliveries.nandos.activity.HomeActivity;
import com.mydeliveries.nandos.model.NavItem;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import android.app.ProgressDialog;

import android.os.Bundle;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class WebViewFragment extends Fragment {
    // Log tag

    private ProgressDialog pDialog;

    String option = "";
    String URL = "";
    private WebView webView;

    String name = "";
    String surname = "";
    String email = "";
    String number = "";

    String price = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            option = bundle.getString("option");


        }


        View rootView = inflater.inflate(R.layout.activity_webview, container, false);

        ((HomeActivity) getActivity()).loadDrawsHide();


        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((HomeActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);


        NavItem nav = new NavItem();
        nav.setPage("Order");


        final ProgressBar Pbar;
        final TextView txtview = (TextView) rootView.findViewById(R.id.tV1);
        Pbar = (ProgressBar) rootView.findViewById(R.id.pB1);

        webView = (WebView) rootView.findViewById(R.id.webViewInfo);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; U; Android 2.0; en-us; Droid Build/ESD20) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17");
        webView.getSettings().setDomStorageEnabled(true);


        webView.setWebViewClient(new MyWebViewClient());

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        String User = "";

        User = pref.getString("User", null);

        price = pref.getString("PayUprice", null);
        try {
            JSONObject jsonObjorder = new JSONObject(User);

            name = jsonObjorder.getString("name");
            surname = jsonObjorder.getString("surname");
            email = jsonObjorder.getString("email");
            number = jsonObjorder.getString("number");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Double tempprice = Double.parseDouble(price) * 100;

        int x = tempprice.intValue();

        price = Integer.toString(x);


        URL = "http://www.mydeliveries.co.za/payu/payu-rpp-setTransaction-using-soap.php?firstName=" + name + "&lastName=" + surname + "&email=" + email + "&mobile=" + number + "&amountInCents=" + price + "&description=Nando's Order";


        webView.loadUrl(URL);


        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100 && Pbar.getVisibility() == ProgressBar.GONE) {
                    Pbar.setVisibility(ProgressBar.VISIBLE);
                    txtview.setVisibility(View.VISIBLE);
                }
                Pbar.setProgress(progress);
                if (progress == 100) {
                    Pbar.setVisibility(ProgressBar.GONE);
                    txtview.setVisibility(View.GONE);
                }
            }
        });

        return rootView;
    }


    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            String success = webView.getUrl();
            if (success.equalsIgnoreCase("http://www.mydeliveries.co.za/payu/success_payment.php")) {
                new placeOrder().execute();

            }
            return false;
        }
    }

    class placeOrder extends AsyncTask<String, String, String> {
        int res = 0;
        final SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        @Override
        protected void onPreExecute() {


            super.onPreExecute();

        }

        /**
         * getting Places JSON
         */
        protected String doInBackground(String... args) {
            // creating Places class object


            String order = pref.getString("PayUcomplete", null);
            try {
                JSONObject oparray = new JSONObject(order);
                oparray.put("paid", "1");


                String jsonString = "";
                String baseURL = "";

                jsonString = oparray.toString();

                System.out.println("Serialized JSON Data ::" + jsonString);

                // JSON content to post
                String contentToPost = jsonString;
                // Create a URLConnection

                baseURL = "http://www.mydeliveries.co.za/ordermanager/nandos/orders";

                java.net.URL url1 = null;
                HttpURLConnection urlConnection = null;

                url1 = new URL(baseURL);
                urlConnection = (HttpURLConnection) url1.openConnection();
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


        protected void onPostExecute(String file_url) {

            if (res == 200 || res == 201) {


                Toast toast = Toast.makeText(getActivity(), "Order Placed successfully", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();


                editor.remove("Order");

                editor.commit();

                ((HomeActivity) getActivity()).displayView(0);


            } else {


                Toast toast = Toast.makeText(getActivity(), "Error, please try again!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();


            }

            super.onPostExecute(file_url);
        }


    }


}
