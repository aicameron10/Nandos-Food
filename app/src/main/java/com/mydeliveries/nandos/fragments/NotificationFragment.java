package com.mydeliveries.nandos.fragments;

import com.mydeliveries.nandos.activity.HomeActivity;
import com.mydeliveries.nandos.adapter.CustomListAdapterNotifications;
import com.mydeliveries.nandos.app.AppController;
import com.mydeliveries.nandos.model.NavItem;
import com.mydeliveries.nandos.model.Notification;
import com.mydeliveries.nandos.util.GPSTracker;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mydeliveries.nandos.R;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import android.os.Bundle;
import android.util.Log;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;


public class NotificationFragment extends ListFragment {
    // Log tag
    private static final String TAG = ListViewFragment.class.getSimpleName();


    private ProgressDialog pDialog;
    private List<Notification> listingList = new ArrayList<Notification>();
    private ListView listView;
    private CustomListAdapterNotifications adapter;


    Typeface face;
    private Button listButton = null;
    private Button mapButton = null;
    private Button filterButton = null;

    Boolean filter = false;
    String filterSend = "";

    String searchSend = "";

    // GPSTracker class
    GPSTracker gps;

    String url = "";

    Boolean otherSearch = false;
    ImageButton fab;

    AutoCompleteTextView store;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        filter = false;


        View rootView = inflater.inflate(R.layout.activity_listview_notifications, container, false);

        // Get tracker.
        Tracker t = ((AppController) getActivity().getApplication()).getTracker(
                AppController.TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Notifications");

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());

        NavItem nav = new NavItem();

        nav.setPage("Home");


        face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/webfont.ttf");


        final TextView heading = (TextView) rootView
                .findViewById(R.id.heading);
        heading.setTypeface(face);

        listView = (ListView) rootView.findViewById(R.id.list);


        ((HomeActivity) getActivity()).loadDrawsHide();


        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((HomeActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);


        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        new doSearch().execute();

    }


    class doSearch extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressDialog(getActivity());
            // Showing progress dialog before making http request
            pDialog.setMessage("Loading Notifications...");
            pDialog.setCancelable(false);
            pDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    HomeFragment ErrorFragment = new HomeFragment();

                    FragmentTransaction fragmentTransaction =
                            getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, ErrorFragment);
                    fragmentTransaction.commit();
                }
            });

            pDialog.show();
            super.onPreExecute();

        }

        /**
         * getting Places JSON
         */
        protected String doInBackground(String... args) {
            // creating Places class object


            url = "http://www.mydeliveries.co.za/ordermanager/nandos/notifications";


            // Creating volley request obj
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject response) {

                            try {

                                JSONArray results = (JSONArray) response.get("notifications");

                                System.out.println(results);


                                // Parsing json
                                for (int i = 0; i < results.length(); i++) {
                                    JSONObject obj = results.getJSONObject(i);
                                    Notification listing = new Notification();
                                    listing.setBody(obj.getString("body"));
                                    listing.setTitle(obj.getString("title"));
                                    listing.setImage(obj.getString("image"));

                                    // adding listing to listings array
                                    listingList.add(listing);


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            // notifying list adapter about data changes
                            // so that it renders the list view with updated data
                            adapter.notifyDataSetChanged();

                            pDialog.hide();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    try {
                        Log.d("error results", error.toString());

                        HomeFragment ErrorFragment = new HomeFragment();

                        FragmentTransaction fragmentTransaction =
                                getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_container, ErrorFragment);
                        fragmentTransaction.commit();
                        Toast toast = Toast.makeText(getActivity(), "No Notifications Available", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        VolleyLog.d(TAG, "Error: " + error.getMessage());

                        hidePDialog();

                    } catch (Exception e) {

                    }

                }
            }) {

                /**
                 * Passing some request headers
                 * */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "057f774e10cd69ce1ad7ea2e3ac708d5");
                    return headers;
                }

            };

            int socketTimeout = 15000;//15 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            getRequest.setRetryPolicy(policy);

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(getRequest);
            // Getting adapter
            adapter = new CustomListAdapterNotifications(getActivity(), listingList);


            return null;
        }


        protected void onPostExecute(String file_url) {

            //setListAdapter(adapter);

            listView.setAdapter(adapter);


            super.onPostExecute(file_url);
        }


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }


}


