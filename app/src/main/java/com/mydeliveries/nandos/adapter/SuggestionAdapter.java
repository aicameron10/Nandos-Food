package com.mydeliveries.nandos.adapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mydeliveries.nandos.R;
import com.mydeliveries.nandos.util.JsonParse;

import com.mydeliveries.nandos.model.SuggestNandos;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Filter;

public class SuggestionAdapter extends ArrayAdapter<String> {

    String what = "";
    List<SuggestNandos> ListData = new ArrayList<SuggestNandos>();

    protected static final String TAG = "SuggestionAdapter";
    private List<String> suggestions;

    public SuggestionAdapter(Activity context, String nameFilter) {
        super(context, R.layout.simple_dropdown_item_1line);
        try {
            suggestions = new ArrayList<String>();
        } catch (Exception e) {

        }
    }

    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Override
    public String getItem(int index) {
        try {
            return suggestions.get(index);
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                what = constraint.toString();
                try {
                    if (constraint != null) {
                        // A class that queries a web API, parses the data and
                        // returns an ArrayList<GoEuroGetSet>
                        new getApiWhat().execute();

                        suggestions.clear();
                        for (int i = 0; i < ListData.size(); i++) {
                            suggestions.add(ListData.get(i).getW());
                        }

                        // Now assign the values and count to the FilterResults
                        // object
                        filterResults.values = suggestions;
                        filterResults.count = suggestions.size();
                    }
                } catch (Exception e) {

                }

                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence contraint,
                                          FilterResults results) {
                try {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                } catch (Exception e) {

                }
            }
        };
        return myFilter;
    }


    class getApiWhat extends AsyncTask<String, String, String> {


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

                String baseURL = "http://www.mydeliveries.co.za/ordermanager/nandos/stores/";

                String searchURL = baseURL + what.trim().replaceAll(" ", "%20");

                URL url = new URL(searchURL);

                URLConnection urlConnection = url.openConnection();
                urlConnection.setRequestProperty("Authorization", "057f774e10cd69ce1ad7ea2e3ac708d5");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(false);

                InputStream is = urlConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int numCharsRead;
                char[] charArray = new char[1024];
                StringBuffer sb = new StringBuffer();
                while ((numCharsRead = isr.read(charArray)) > 0) {
                    sb.append(charArray, 0, numCharsRead);
                }
                String json = sb.toString();
                JSONObject jsonRes = new JSONObject(json);
                String value = jsonRes.getString("stores");
                JSONArray jsonResponse = new JSONArray(value);
                ListData.clear();
                for (int i = 0; i < 5; i++) {
                    JSONObject r = jsonResponse.getJSONObject(i);
                    ListData.add(new SuggestNandos(r.getString("name").toLowerCase()));
                }
            } catch (Exception e1) {
                // TODO Auto-generated catch block

            }


            return null;
        }


        protected void onPostExecute(String result) {

            super.onPostExecute(result);
        }


    }


}