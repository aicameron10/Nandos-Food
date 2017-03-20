package com.mydeliveries.nandos.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.mydeliveries.nandos.R;
import com.mydeliveries.nandos.activity.HomeActivity;
import com.mydeliveries.nandos.adapter.OrderListAdapter;
import com.mydeliveries.nandos.app.AppController;
import com.mydeliveries.nandos.model.Orders;
import com.mydeliveries.nandos.model.NavItem;
import com.mydeliveries.nandos.util.GPSTracker;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OrderFragment extends ListFragment {
    // Log tag
    private static final String TAG = OrderFragment.class.getSimpleName();


    private ProgressDialog pDialog;
    private List<Orders> listingList = new ArrayList<Orders>();
    private ListView listView;
    private OrderListAdapter adapter;

    private ArrayList<String> SearchMap = new ArrayList<String>();


    private Button listButton = null;
    private Button mapButton = null;

    String userEmail;

    // GPSTracker class
    GPSTracker gps;

    String url = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_listvieworder, container, false);

        // Get tracker.
        Tracker t = ((AppController) getActivity().getApplication()).getTracker(
                AppController.TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Order History List");

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());

        NavItem nav = new NavItem();

        nav.setPage("Home");

        listView = (ListView) rootView.findViewById(R.id.list);
        ((HomeActivity) getActivity()).loadDrawsHide();


        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((HomeActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/webfont.ttf");
        final TextView heading = (TextView) rootView
                .findViewById(R.id.heading);
        heading.setTypeface(face);

        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        doSearch();

    }


    public void doSearch() {

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        String User = "";
        if (pref.getString("User", null) != null && (!pref.getString("User", null).equalsIgnoreCase(""))) {
            User = pref.getString("User", null);
            try {
                JSONObject jsonObjorder = new JSONObject(User);


                userEmail = jsonObjorder.getString("email");


            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {


            userEmail = "false";

            Toast.makeText(getActivity(), "Please enter an email address for a profile search", Toast.LENGTH_LONG).show();
            ((HomeActivity) getActivity()).displayView(7);

        }

        //userEmail = "aicameron10@gmail.com";

        url = "http://www.mydeliveries.co.za/ordermanager/nandos/userorders/" + userEmail;


        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading Orders...");
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

        // Creating volley request obj
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {

                        pDialog.dismiss();

                        try {

                            JSONArray results = (JSONArray) response.get("orders");

                            System.out.println(results);

                            SearchMap.add(results.toString());


                            //Log.d(" results array", SearchMap.toString());

                            //Log.d("url results", results.toString());

                            // Parsing json
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject obj = results.getJSONObject(i);
                                Orders order = new Orders();
                                order.setId(obj.getString("id"));
                                order.setOrder(obj.getString("order"));
                                order.setStatus(obj.getString("status"));
                                order.setName(obj.getString("name"));
                                order.setSurname(obj.getString("surname"));
                                order.setNumber(obj.getString("number"));
                                order.setEmail(obj.getString("email"));
                                order.setAddress(obj.getString("address"));
                                order.setCollection(obj.getString("collection"));
                                order.setDelivery(obj.getString("delivery"));
                                order.setPreorder(obj.getString("preorder"));
                                order.setOrderdate(obj.getString("orderdate"));
                                order.setCapture(obj.getString("capture"));
                                order.setReference(obj.getString("reference"));
                                order.setStoredelivery(obj.getString("storedelivery"));
                                order.setStorecollect(obj.getString("storecollect"));
                                order.setStorehours(obj.getString("storehours"));
                                order.setNowactive(obj.getString("nowactive"));
                                order.setStorename(obj.getString("storename"));
                                order.setStoreapi(obj.getString("storeapi"));


                                //JSONObject phone = (JSONObject) obj.get("tel");
                                //	listing.setNumber(phone.getString("main"));


                                // adding listing to listings array
                                listingList.add(order);


                            }

                            System.out.println("results widget list array*************" + listingList.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                try {
                    Log.d("error results", error.toString());

                    HomeFragment ErrorFragment = new HomeFragment();

                    FragmentTransaction fragmentTransaction =
                            getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, ErrorFragment);
                    fragmentTransaction.commit();
                    Toast toast = Toast.makeText(getActivity(), "No Order History Found", Toast.LENGTH_LONG);
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
        adapter = new OrderListAdapter(getActivity(), listingList);
        setListAdapter(adapter);
        listView.setAdapter(adapter);


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


