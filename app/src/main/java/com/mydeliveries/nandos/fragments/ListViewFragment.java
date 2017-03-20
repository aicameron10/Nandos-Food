package com.mydeliveries.nandos.fragments;

import com.github.clans.fab.FloatingActionMenu;
import com.mydeliveries.nandos.activity.HomeActivity;
import com.mydeliveries.nandos.adapter.CustomListAdapter;
import com.mydeliveries.nandos.adapter.SuggestionAdapter;
import com.mydeliveries.nandos.app.AppController;
import com.mydeliveries.nandos.model.Listing;
import com.mydeliveries.nandos.model.NavItem;
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

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import android.os.Bundle;
import android.util.Log;

import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
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


public class ListViewFragment extends ListFragment {
    // Log tag
    private static final String TAG = ListViewFragment.class.getSimpleName();


    private ProgressDialog pDialog;
    private List<Listing> listingList = new ArrayList<Listing>();
    private ListView listView;
    private CustomListAdapter adapter;

    private ArrayList<String> SearchMap = new ArrayList<String>();

    Typeface face;
    private Button listButton = null;
    private Button mapButton = null;
    private Button filterButton = null;

    Boolean filter = false;
    String filterSend = "";

    FloatingActionMenu menu1;


    private List<FloatingActionMenu> menus = new ArrayList<>();

    String coordsSend = "";

    String searchSend = "";

    // GPSTracker class
    GPSTracker gps;

    String url = "";

    Boolean otherSearch = false;


    AutoCompleteTextView store;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        filter = false;


        View rootView = inflater.inflate(R.layout.activity_listview, container, false);

        // Get tracker.
        Tracker t = ((AppController) getActivity().getApplication()).getTracker(
                AppController.TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Store List");

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());

        NavItem nav = new NavItem();

        nav.setPage("Home");


        face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/webfont.ttf");

        gps = new GPSTracker(getActivity());

        try {
            // check if GPS enabled
            if (gps.canGetLocation()) {

            } else {

                gps.showSettingsAlert();

            }
        } catch (Exception e) {

        }

        listView = (ListView) rootView.findViewById(R.id.list);


        ((HomeActivity) getActivity()).loadDrawsHide();


        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((HomeActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);


        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        listButton = (Button) view.findViewById(R.id.list_btn);

        mapButton = (Button) view.findViewById(R.id.map_btn);
        filterButton = (Button) view.findViewById(R.id.filter_btn);
        filterButton.setTypeface(face);
        listButton.setTypeface(face);
        mapButton.setTypeface(face);

        menu1 = (FloatingActionMenu) view.findViewById(R.id.fab_image_button);


        menu1.setVisibility(View.GONE);

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        String API = pref.getString("StoreAPI", null);
        if (pref.getString("StoreAPI", null) != null && (!pref.getString("StoreAPI", null).equalsIgnoreCase(""))) {
            menu1.setVisibility(View.VISIBLE);
        } else {
            menu1.setVisibility(View.GONE);
        }

        final Button search = (Button) view.findViewById(R.id.search);
        search.setTypeface(face);

        store = (AutoCompleteTextView) view.findViewById(R.id.store);
        try {
            store.setAdapter(new SuggestionAdapter(getActivity(), store.getText().toString()));
        } catch (Exception e) {

        }

        store.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

            }

        });

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;


        int newWidth = (width * 80) / 100;

        store.setDropDownWidth(newWidth);


        final Drawable img = this.getResources().getDrawable(R.drawable.ic_action_close);
        final Drawable img2 = this.getResources().getDrawable(R.drawable.ic_magnify);

        store.setCompoundDrawablesWithIntrinsicBounds(img2, null, null, null);

        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (store.getText().toString().equals("")) {

                    Toast toast = Toast.makeText(getActivity(), "Please enter a store", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                } else {
                    InputMethodManager inputManager = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                    searchSend = store.getText().toString();
                    otherSearch = true;

                    doSearchsearch();
                }
            }
        });


        store.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search.performClick();
                    return true;
                }
                return false;
            }

        });


        store.setOnFocusChangeListener(new View.OnFocusChangeListener() {


            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    store.setCompoundDrawablesWithIntrinsicBounds(img2, null, img, null);

                    store.setOnTouchListener(new View.OnTouchListener() {
                        @SuppressLint("ClickableViewAccessibility")
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {


                            if (store.getCompoundDrawables()[2] == null)
                                return false;

                            if (event.getAction() != MotionEvent.ACTION_UP)
                                return false;

                            if (event.getX() > store.getWidth() - store.getPaddingRight() - img.getIntrinsicWidth()) {
                                store.setText("");

                            }
                            return false;
                        }
                    });


                } else {

                    store.setCompoundDrawablesWithIntrinsicBounds(img2, null, null, null);
                }
            }
        });


        menu1.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode

                if (pref.getString("Order", null) != null && (!pref.getString("Order", null).equalsIgnoreCase(""))) {
                    if (pref.getString("StoreAPI", null) != null && (!pref.getString("StoreAPI", null).equalsIgnoreCase(""))) {

                        ((HomeActivity) getActivity()).displayView(2);

                    } else {
                        ((HomeActivity) getActivity()).displayView(1);

                    }
                } else {
                    Toast.makeText(
                            getActivity(),
                            "Please Add Order to Cart",
                            Toast.LENGTH_LONG).show();

                    ((HomeActivity) getActivity()).displayView(0);
                }
            }
        });


        mapButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //mapButton.setBackgroundColor(Color.parseColor("#eae7e7"));
                //listButton.setBackgroundColor(Color.parseColor("#ffffff"));
                MapLocatorFragment MapFragment = new MapLocatorFragment();
                Bundle bundle = new Bundle();
                bundle.putString("searchmap", SearchMap.toString());
                MapFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction =
                        getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, MapFragment);
                fragmentTransaction.commit();
            }

        });


        filterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (filterButton.getText().toString().equalsIgnoreCase("Filter")) {


                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View formElementsView = inflater.inflate(R.layout.form_elements_filter,
                            null, false);


                    final CheckBox chkOnline = (CheckBox) formElementsView.findViewById(R.id.chkOnline);
                    final CheckBox chkalcohol = (CheckBox) formElementsView.findViewById(R.id.chkalcohol);
                    final CheckBox chkbreakfast = (CheckBox) formElementsView.findViewById(R.id.chkbreakfast);
                    final CheckBox chkcoffee = (CheckBox) formElementsView.findViewById(R.id.chkcoffee);
                    final CheckBox chkdrive = (CheckBox) formElementsView.findViewById(R.id.chkdrive);
                    final CheckBox chkfullservice = (CheckBox) formElementsView.findViewById(R.id.chkfullservice);
                    final CheckBox chkhalaal = (CheckBox) formElementsView.findViewById(R.id.chkhalaal);
                    final CheckBox chkKosher = (CheckBox) formElementsView.findViewById(R.id.chkKosher);
                    final CheckBox chkload = (CheckBox) formElementsView.findViewById(R.id.chkload);
                    final CheckBox chkdel = (CheckBox) formElementsView.findViewById(R.id.chkdel);
                    final CheckBox chkcollect = (CheckBox) formElementsView.findViewById(R.id.chkcollect);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            getActivity()).setView(formElementsView);

                    LayoutInflater inflater1 = getActivity().getLayoutInflater();
                    View view = inflater1.inflate(R.layout.tool_bar_order, null);

                    TextView header = (TextView) view
                            .findViewById(R.id.toolbartitle);
                    header.setTypeface(face);
                    header.setText("Filter");
                    alertDialogBuilder.setCustomTitle(view);


                    // set title
                    alertDialogBuilder.setTitle("Add Filters");


                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Choose your filter preferences")
                            .setCancelable(false)
                            .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    //new placeOrder().execute();
                                    filter = true;
                                    try {
                                        JSONObject jo = new JSONObject();


                                        if (chkOnline.isChecked()) {
                                            jo.put("nowactive", "1");

                                        }
                                        if (chkload.isChecked()) {
                                            jo.put("loadshedding", "1");

                                        }
                                        if (chkdel.isChecked()) {
                                            jo.put("delivery", "1");

                                        }
                                        if (chkcollect.isChecked()) {
                                            jo.put("collect", "1");

                                        }
                                        if (chkbreakfast.isChecked()) {
                                            jo.put("breakfast", "1");

                                        }
                                        if (chkcoffee.isChecked()) {
                                            jo.put("coffee", "1");

                                        }
                                        if (chkdrive.isChecked()) {
                                            jo.put("drivethru", "1");

                                        }
                                        if (chkfullservice.isChecked()) {
                                            jo.put("fullservice", "1");

                                        }
                                        if (chkhalaal.isChecked()) {
                                            jo.put("halaal", "1");

                                        }
                                        if (chkKosher.isChecked()) {
                                            jo.put("kosher", "1");

                                        }
                                        if (chkalcohol.isChecked()) {
                                            jo.put("licensed", "1");

                                        }

                                        filterSend = "[" + jo.toString() + "]";


                                    } catch (JSONException e) {

                                    }

                                    doSearchfilter();

                                }
                            })
                            .setNegativeButton("Close", new DialogInterface.OnClickListener() {
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

                } else {
                    new doSearch().execute();
                }

            }

        });

        if (otherSearch == false) {
            new doSearch().execute();
        }


    }

    public void showFab() {

        menu1.setVisibility(View.VISIBLE);

    }


    class doSearch extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            listingList.clear();
            SearchMap.clear();
            filterButton.setText("Filter");
            filterButton.setTypeface(face);

            Double lat = 0.0;
            Double lon = 0.0;
            try {
                gps = new GPSTracker(getActivity());

                lat = gps.getLatitude();
                lon = gps.getLongitude();

            } catch (Exception e) {

            }
            try {
                JSONObject jo = new JSONObject();


                jo.put("lat", lat);
                jo.put("lon", lon);


                coordsSend = "[" + jo.toString() + "]";


            } catch (JSONException e) {

            }

            pDialog = new ProgressDialog(getActivity());
            // Showing progress dialog before making http request
            pDialog.setMessage("Loading Restaurants...");
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


            url = "http://www.mydeliveries.co.za/ordermanager/nandos/fullstore/" + coordsSend;

            System.out.println("url *********" + url);


            // Creating volley request obj
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject response) {

                            try {

                                JSONArray results = (JSONArray) response.get("stores");

                                System.out.println(results);

                                SearchMap.add(results.toString());


                                //Log.d(" results array", SearchMap.toString());

                                //Log.d("url results", results.toString());

                                // Parsing json
                                for (int i = 0; i < results.length(); i++) {
                                    JSONObject obj = results.getJSONObject(i);
                                    Listing listing = new Listing();
                                    listing.setId(obj.getString("id"));
                                    listing.setName(obj.getString("name"));
                                    listing.setNumber(obj.getString("number"));
                                    listing.setAddress(obj.getString("address"));
                                    listing.setDelivery(obj.getInt("delivery"));
                                    listing.setEmail(obj.getString("email"));
                                    listing.setApi_key(obj.getString("api_key"));
                                    listing.setLastactive(obj.getString("lastactive"));
                                    listing.setNowactive(obj.getInt("nowactive"));
                                    listing.setLat(obj.getString("lat"));
                                    listing.setLon(obj.getString("lon"));
                                    listing.setCollect(obj.getInt("collect"));
                                    listing.setLoadshedding(obj.getInt("loadshedding"));
                                    listing.setBreakfast(obj.getInt("breakfast"));
                                    listing.setCoffee(obj.getInt("coffee"));
                                    listing.setDrivethru(obj.getInt("drivethru"));
                                    listing.setFullservice(obj.getInt("fullservice"));
                                    listing.setHalaal(obj.getInt("halaal"));
                                    listing.setKosher(obj.getInt("kosher"));
                                    listing.setLicensed(obj.getInt("licensed"));
                                    listing.setHours(obj.getString("hours"));
                                    listing.setDistance(obj.getDouble("distance"));


                                    //JSONObject phone = (JSONObject) obj.get("tel");
                                    //	listing.setNumber(phone.getString("main"));


									/*double distanceone = 0;
                                    double lat = 0;
									double lon = 0;


									try {
										gps = new GPSTracker(getActivity());

										lat = gps.getLatitude();
										lon = gps.getLongitude();
									} catch (Exception e) {

									}


									try {


										double mylat = lat;
										double mylon = lon;

										double bizlat = Double.parseDouble(obj.getString("lat"));
										double bizlon = Double.parseDouble(obj.getString("lon"));
										double Radius = 6371;

										double dLat = (mylat - bizlat) * Math.PI / 180;
										double dLon = (mylon - bizlon) * Math.PI / 180;
										double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
												+ Math.cos(bizlat * Math.PI / 180) * Math.cos(mylat * Math.PI / 180)
												* Math.sin(dLon / 2) * Math.sin(dLon / 2);
										double c = 2 * Math.asin(Math.sqrt(a));
										double distance = Radius * c;

										distanceone = distance;

										listing.setDistance(distanceone);



									} catch (Exception e) {

									}
									*/

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
                        Toast toast = Toast.makeText(getActivity(), "No Restaurants Located", Toast.LENGTH_LONG);
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
            adapter = new CustomListAdapter(getActivity(), listingList, ListViewFragment.this);


            return null;
        }


        protected void onPostExecute(String file_url) {

            //setListAdapter(adapter);

            listView.setAdapter(adapter);



		/* set a listener to be invoked when the list reaches the end
		((LoadMoreListView) getListView())
				.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
					public void onLoadMore() {
						// Do the work to load more items at the end of list

						//new LoadDataTask().execute();

					}
				});

*/

            super.onPostExecute(file_url);
        }


    }


    public void doSearchfilter() {

        listingList.clear();
        SearchMap.clear();
        filterButton.setText("Remove Filter");
        filterButton.setTypeface(face);

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading Restaurants...");
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


        double lat = 0;
        double lon = 0;
        try {
            gps = new GPSTracker(getActivity());

            lat = gps.getLatitude();
            lon = gps.getLongitude();

        } catch (Exception e) {

        }


        url = "http://www.mydeliveries.co.za/ordermanager/nandos/fullstorefilter/" + filterSend;

        System.out.println("^^^^^^^^" + filterSend);


        // Creating volley request obj
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray results = (JSONArray) response.get("stores");

                            System.out.println(results);

                            SearchMap.add(results.toString());


                            //Log.d(" results array", SearchMap.toString());

                            //Log.d("url results", results.toString());

                            // Parsing json
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject obj = results.getJSONObject(i);
                                Listing listing = new Listing();
                                listing.setId(obj.getString("id"));
                                listing.setName(obj.getString("name"));
                                listing.setNumber(obj.getString("number"));
                                listing.setAddress(obj.getString("address"));
                                listing.setDelivery(obj.getInt("delivery"));
                                listing.setEmail(obj.getString("email"));
                                listing.setApi_key(obj.getString("api_key"));
                                listing.setLastactive(obj.getString("lastactive"));
                                listing.setNowactive(obj.getInt("nowactive"));
                                listing.setLat(obj.getString("lat"));
                                listing.setLon(obj.getString("lon"));
                                listing.setCollect(obj.getInt("collect"));
                                listing.setLoadshedding(obj.getInt("loadshedding"));
                                listing.setBreakfast(obj.getInt("breakfast"));
                                listing.setCoffee(obj.getInt("coffee"));
                                listing.setDrivethru(obj.getInt("drivethru"));
                                listing.setFullservice(obj.getInt("fullservice"));
                                listing.setHalaal(obj.getInt("halaal"));
                                listing.setKosher(obj.getInt("kosher"));
                                listing.setLicensed(obj.getInt("licensed"));
                                listing.setHours(obj.getString("hours"));


                                //JSONObject phone = (JSONObject) obj.get("tel");
                                //	listing.setNumber(phone.getString("main"));


                                double distanceone = 0;
                                double lat = 0;
                                double lon = 0;


                                try {
                                    gps = new GPSTracker(getActivity());

                                    lat = gps.getLatitude();
                                    lon = gps.getLongitude();
                                } catch (Exception e) {

                                }


                                try {


                                    double mylat = lat;
                                    double mylon = lon;

                                    double bizlat = Double.parseDouble(obj.getString("lat"));
                                    double bizlon = Double.parseDouble(obj.getString("lon"));
                                    double Radius = 6371;

                                    double dLat = (mylat - bizlat) * Math.PI / 180;
                                    double dLon = (mylon - bizlon) * Math.PI / 180;
                                    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                                            + Math.cos(bizlat * Math.PI / 180) * Math.cos(mylat * Math.PI / 180)
                                            * Math.sin(dLon / 2) * Math.sin(dLon / 2);
                                    double c = 2 * Math.asin(Math.sqrt(a));
                                    double distance = Radius * c;

                                    distanceone = distance;

                                    listing.setDistance(distanceone);


                                } catch (Exception e) {

                                }


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
                    Toast toast = Toast.makeText(getActivity(), "No Restaurants Located", Toast.LENGTH_LONG);
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
        adapter = new CustomListAdapter(getActivity(), listingList, ListViewFragment.this);
        //setListAdapter(adapter);
        listView.setAdapter(adapter);


    }

    public void doSearchsearch() {


        listingList.clear();
        SearchMap.clear();
        filterButton.setText("Remove Filter");
        filterButton.setTypeface(face);

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading Restaurants...");
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


        double lat = 0;
        double lon = 0;
        try {
            gps = new GPSTracker(getActivity());

            lat = gps.getLatitude();
            lon = gps.getLongitude();

        } catch (Exception e) {

        }


        url = "http://www.mydeliveries.co.za/ordermanager/nandos/fullstoresearch/" + searchSend;


        // Creating volley request obj
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray results = (JSONArray) response.get("stores");

                            System.out.println(results);

                            SearchMap.add(results.toString());


                            //Log.d(" results array", SearchMap.toString());

                            //Log.d("url results", results.toString());

                            // Parsing json
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject obj = results.getJSONObject(i);
                                Listing listing = new Listing();
                                listing.setId(obj.getString("id"));
                                listing.setName(obj.getString("name"));
                                listing.setNumber(obj.getString("number"));
                                listing.setAddress(obj.getString("address"));
                                listing.setDelivery(obj.getInt("delivery"));
                                listing.setEmail(obj.getString("email"));
                                listing.setApi_key(obj.getString("api_key"));
                                listing.setLastactive(obj.getString("lastactive"));
                                listing.setNowactive(obj.getInt("nowactive"));
                                listing.setLat(obj.getString("lat"));
                                listing.setLon(obj.getString("lon"));
                                listing.setCollect(obj.getInt("collect"));
                                listing.setLoadshedding(obj.getInt("loadshedding"));
                                listing.setBreakfast(obj.getInt("breakfast"));
                                listing.setCoffee(obj.getInt("coffee"));
                                listing.setDrivethru(obj.getInt("drivethru"));
                                listing.setFullservice(obj.getInt("fullservice"));
                                listing.setHalaal(obj.getInt("halaal"));
                                listing.setKosher(obj.getInt("kosher"));
                                listing.setLicensed(obj.getInt("licensed"));
                                listing.setHours(obj.getString("hours"));


                                //JSONObject phone = (JSONObject) obj.get("tel");
                                //	listing.setNumber(phone.getString("main"));


                                double distanceone = 0;
                                double lat = 0;
                                double lon = 0;


                                try {
                                    gps = new GPSTracker(getActivity());

                                    lat = gps.getLatitude();
                                    lon = gps.getLongitude();
                                } catch (Exception e) {

                                }


                                try {


                                    double mylat = lat;
                                    double mylon = lon;

                                    double bizlat = Double.parseDouble(obj.getString("lat"));
                                    double bizlon = Double.parseDouble(obj.getString("lon"));
                                    double Radius = 6371;

                                    double dLat = (mylat - bizlat) * Math.PI / 180;
                                    double dLon = (mylon - bizlon) * Math.PI / 180;
                                    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                                            + Math.cos(bizlat * Math.PI / 180) * Math.cos(mylat * Math.PI / 180)
                                            * Math.sin(dLon / 2) * Math.sin(dLon / 2);
                                    double c = 2 * Math.asin(Math.sqrt(a));
                                    double distance = Radius * c;

                                    distanceone = distance;

                                    listing.setDistance(distanceone);


                                } catch (Exception e) {

                                }


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
                    Toast toast = Toast.makeText(getActivity(), "No Restaurants Located", Toast.LENGTH_LONG);
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
        adapter = new CustomListAdapter(getActivity(), listingList, ListViewFragment.this);
        //setListAdapter(adapter);
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


