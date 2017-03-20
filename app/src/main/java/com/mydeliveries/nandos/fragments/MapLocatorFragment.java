package com.mydeliveries.nandos.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mydeliveries.nandos.util.AlertDialogManager;
import com.mydeliveries.nandos.util.ConnectionDetector;
import com.mydeliveries.nandos.activity.HomeActivity;
import com.mydeliveries.nandos.util.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.mydeliveries.nandos.R;

/**
 * Created by Andrew on 09/07/2015.
 */


public class MapLocatorFragment extends Fragment {


    private GoogleMap map;


    String name = "";
    String number = "";
    String lat = "";
    String lon = "";


    // flag for Internet connection status
    Boolean isInternetPresent = false;

    // Connection detector class
    ConnectionDetector cd;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();


    // Progress dialog
    ProgressDialog pDialog;

    // GPS Location
    GPSTracker gps;

    String reference = "";


    String noLoc = "";

    double moveLat = 0;
    double moveLon = 0;


    private Button listButton = null;
    private Button mapButton = null;


    String SearchMap = "";
    String userloc = "";


    TextView bizidtxt;


    private Button callButton = null;
    private Button DirectionsButton = null;
    private Button ShareButton = null;

    String newNowactive = "";

    TextView bizClick = null;


    double dis = 0;

    ProgressBar progressBar;

    View rootView;

    private View mViewGroup;

    final ArrayList<String> day = new ArrayList<String>();
    final ArrayList<String> open = new ArrayList<String>();
    final ArrayList<String> close = new ArrayList<String>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {

            SearchMap = bundle.getString("searchmap");


        }

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            Toast.makeText(getActivity(), "Need Map Permission", Toast.LENGTH_LONG).show();
        }

        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/webfont.ttf");

        if (userloc == null || userloc.length() <= 0) {
            userloc = "no";
        }


        rootView = inflater.inflate(R.layout.fragment_storelocator, container, false);

        ((HomeActivity) getActivity()).loadDrawsHide();


        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((HomeActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);


        mViewGroup = rootView.findViewById(R.id.viewsContainer);

        mViewGroup.setVisibility(View.GONE);

        final ImageButton fab = (ImageButton) rootView.findViewById(R.id.fab_image_button);
        fab.setVisibility(View.GONE);

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        String API = pref.getString("StoreAPI", null);
        if (pref.getString("StoreAPI", null) != null && (!pref.getString("StoreAPI", null).equalsIgnoreCase(""))) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.GONE);
        }


        bizidtxt = (TextView) rootView.findViewById(R.id.bizid);


        bizClick = (TextView) rootView.findViewById(R.id.bizClick);
        listButton = (Button) rootView.findViewById(R.id.list_btn);
        mapButton = (Button) rootView.findViewById(R.id.map_btn);

        listButton.setTypeface(face);
        mapButton.setTypeface(face);


        // mapButton.setBackgroundColor(Color.parseColor("#eae7e7"));

        listButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //  listButton.setBackgroundColor(Color.parseColor("#eae7e7"));
                //  mapButton.setBackgroundColor(Color.parseColor("#ffffff"));

                ListViewFragment ListViewFragment = new ListViewFragment();

                FragmentTransaction fragmentTransaction =
                        getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, ListViewFragment);
                fragmentTransaction.commit();
            }

        });


        LatLng userLocation;
        try {
            gps = new GPSTracker(getActivity());

            userLocation = new LatLng(gps.getLatitude(), gps.getLongitude());
            userloc = "Yes";

        } catch (Exception e) {

            noLoc = "No";
            userLocation = new LatLng(-30.683189, 24.089581);
        }

        if (userLocation.latitude == 0.0 && userLocation.longitude == 0.0) {
            noLoc = "No";
            userLocation = new LatLng(-30.683189, 24.089581);
        }

        progressBar = (ProgressBar) rootView.findViewById(R.id.pg_loadmap);

        progressBar.setVisibility(View.GONE);


        map = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
        // Changing map type
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Showing / hiding your current location
        map.setMyLocationEnabled(true);

        // Enable / Disable zooming controls
        map.getUiSettings().setZoomControlsEnabled(true);

        // Enable / Disable my location button
        map.getUiSettings().setMyLocationButtonEnabled(true);

        // Enable / Disable Compass icon
        map.getUiSettings().setCompassEnabled(true);

        // Enable / Disable Rotate gesture
        map.getUiSettings().setRotateGesturesEnabled(true);

        // Enable / Disable zooming functionality
        map.getUiSettings().setZoomGesturesEnabled(true);


        MapsInitializer.initialize(this.getActivity());


        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker marker) {

                // Getting view from the layout file info_window_layout
                View v = getActivity().getLayoutInflater().inflate(R.layout.info_window_layout, null);


                // Getting reference to the TextView to set latitude
                TextView poititle = (TextView) v.findViewById(R.id.poititle);


                // Setting the latitude
                poititle.setText(marker.getTitle());


                // Returning the view containing InfoWindow contents
                return v;

            }
        });


        try {

            String SearchMapNew = "{\"results\":[" + SearchMap.replaceAll("\\[", "").replaceAll("\\]", "") + "]}";


            JSONObject jsnobject = new JSONObject(SearchMapNew);


            System.out.println("jsonobect" + jsnobject);

            JSONArray jsonArray;

            jsonArray = jsnobject.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                //  bizId =  obj.get("docid").toString();

                String name = obj.get("name").toString();

                String address = obj.get("address").toString();

                String number = obj.get("number").toString();
                String id = obj.get("id").toString();
                String delivery = obj.get("delivery").toString();
                String email = obj.get("email").toString();
                String api = obj.get("api_key").toString();

                String lastactive = obj.get("lastactive").toString();
                String nowactive = obj.get("nowactive").toString();
                String latmap = obj.get("lat").toString();
                String lonmap = obj.get("lon").toString();
                String collect = obj.get("collect").toString();
                String loadshedding = obj.get("loadshedding").toString();
                String breakfast = obj.get("breakfast").toString();
                String coffee = obj.get("coffee").toString();
                String drivethru = obj.get("drivethru").toString();
                String fullservice = obj.get("fullservice").toString();
                String halaal = obj.get("halaal").toString();
                String kosher = obj.get("kosher").toString();
                String licensed = obj.get("licensed").toString();
                String hoursmap = obj.get("hours").toString();
                String distance = obj.get("distance").toString();


                double lon = 0;
                double lat = 0;


                lon = obj.getDouble("lon");
                lat = obj.getDouble("lat");


                // create marker
                MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, lon)).title(name);


                // Changing marker icon
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.mappinn));
                marker.snippet(name + "~" + address + "~" + number + "~" + api + "~" + delivery + "~" + email + "~" + lastactive + "~" + nowactive + "~" + latmap + "~" + lonmap + "~" + collect + "~" + loadshedding + "~" + breakfast + "~" + coffee + "~" + drivethru + "~" + fullservice + "~" + halaal + "~" + kosher + "~" + licensed + "~" + hoursmap + "~" + distance);

                // adding marker
                map.addMarker(marker);

            }


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // create marker
        final MarkerOptions marker = new MarkerOptions().position(userLocation).title("My Location");

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.pinme));

        // adding marker
        map.addMarker(marker);


        if (userloc.equalsIgnoreCase("yes")) {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    userLocation).zoom(14).build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } else {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    userLocation).zoom(5).build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }


        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                // TODO Auto-generated method stub
                if (!marker.getTitle().equalsIgnoreCase("My Location") || marker.getSnippet() != null) {


                    mViewGroup.setVisibility(View.VISIBLE);
                    // progressBar1.setVisibility(View.VISIBLE);

                    ImageView thumbNail = (ImageView) rootView
                            .findViewById(R.id.thumbnail);


                    TextView active = (TextView) rootView.findViewById(R.id.Active);
                    TextView disttxt = (TextView) rootView.findViewById(R.id.distance);

                    ImageView cat1 = (ImageView) rootView.findViewById(R.id.cat1);
                    ImageView cat2 = (ImageView) rootView.findViewById(R.id.cat2);
                    ImageView cat3 = (ImageView) rootView.findViewById(R.id.cat3);
                    ImageView cat4 = (ImageView) rootView.findViewById(R.id.cat4);
                    ImageView cat5 = (ImageView) rootView.findViewById(R.id.cat5);
                    ImageView cat6 = (ImageView) rootView.findViewById(R.id.cat6);
                    ImageView cat7 = (ImageView) rootView.findViewById(R.id.cat7);
                    ImageView cat8 = (ImageView) rootView.findViewById(R.id.cat8);
                    ImageView cat9 = (ImageView) rootView.findViewById(R.id.cat9);
                    ImageView cat10 = (ImageView) rootView.findViewById(R.id.cat10);

                    final TextView sun = (TextView) rootView.findViewById(R.id.sun);
                    final TextView mon = (TextView) rootView.findViewById(R.id.mon);
                    final TextView tue = (TextView) rootView.findViewById(R.id.tue);
                    final TextView wed = (TextView) rootView.findViewById(R.id.wed);
                    final TextView thur = (TextView) rootView.findViewById(R.id.thur);
                    final TextView fri = (TextView) rootView.findViewById(R.id.fri);
                    final TextView sat = (TextView) rootView.findViewById(R.id.sat);

                    TextView hours = (TextView) rootView.findViewById(R.id.hours);

                    final LinearLayout mainLayout = (LinearLayout) rootView.findViewById(R.id.linearLayout4);


                    TextView lbl_address = (TextView) rootView.findViewById(R.id.address);

                    mainLayout.setVisibility(View.GONE);


                    cat1.setImageDrawable(null);
                    cat2.setImageDrawable(null);
                    cat3.setImageDrawable(null);
                    cat4.setImageDrawable(null);
                    cat5.setImageDrawable(null);
                    cat6.setImageDrawable(null);
                    cat7.setImageDrawable(null);
                    cat8.setImageDrawable(null);
                    cat9.setImageDrawable(null);
                    cat10.setImageDrawable(null);


                    reference = marker.getSnippet();


                    String[] temp = reference.split("~");
                    String newName = temp[0];
                    String newAddress = temp[1];
                    String newNumber = temp[2];
                    String newApi = temp[3];
                    String newDelivery = temp[4];
                    String newEmail = temp[5];
                    String newLastActive = temp[6];
                    newNowactive = temp[7];
                    String newLatMap = temp[8];
                    String newLonMap = temp[9];
                    String newCollect = temp[10];
                    String newLoadshedding = temp[11];
                    String newBreakfast = temp[12];
                    String newCoffee = temp[13];
                    String newDriveThru = temp[14];
                    String newFullService = temp[15];
                    String newhalaal = temp[16];
                    String newKosher = temp[17];
                    String newLisenced = temp[18];
                    String newHoursMap = temp[19];
                    String newDistance = temp[20];


                    try {

                        JSONArray hoursjson = new JSONArray("[" + newHoursMap + "]");


                        for (int i = 0; i < hoursjson.length(); i++) {
                            JSONObject obj = hoursjson.getJSONObject(i);


                            day.add(obj.getString("day"));
                            open.add(obj.getString("open"));
                            close.add(obj.getString("close"));

                            System.out.println("day ******" + day.toString());

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();

                    editor.putString("TempStore", newName + "-" + newApi + "-" + newDelivery + "-" + newHoursMap);

                    editor.commit(); // commit changes

                    String msg = "<strong>" + newName + "</strong><br />Address: " + newAddress;

                    number = newNumber;
                    lbl_address.setText(Html.fromHtml(msg));


                    if (newNowactive.equalsIgnoreCase("1")) {
                        active.setText("Store Online");
                    } else {
                        active.setText("Store Offline");
                    }

                    lat = newLatMap;
                    lon = newLonMap;

                    dis = Double.parseDouble(newDistance);
                    gps = new GPSTracker(getActivity());

                    try {
                        // check if GPS enabled
                        if (gps.canGetLocation()) {
                            if (dis != 0.0 && dis < 4000) {

                                disttxt.setText((String.format("%.1f", dis) + "km"));
                            } else {
                                disttxt.setText("");
                            }

                        } else {
                            disttxt.setText("");
                        }
                    } catch (Exception e) {

                    }


                    thumbNail.setImageResource(R.drawable.listicon);

                    hours.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mainLayout.getVisibility() == View.VISIBLE) {
                                        mainLayout.setVisibility(View.GONE);
                                    } else {

                                        int dayweek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                                        if (dayweek == 1) {
                                            sun.setTypeface(sun.getTypeface(), Typeface.BOLD);
                                        } else if (dayweek == 2) {
                                            mon.setTypeface(mon.getTypeface(), Typeface.BOLD);

                                        } else if (dayweek == 3) {
                                            tue.setTypeface(tue.getTypeface(), Typeface.BOLD);

                                        } else if (dayweek == 4) {
                                            wed.setTypeface(wed.getTypeface(), Typeface.BOLD);

                                        } else if (dayweek == 5) {
                                            thur.setTypeface(thur.getTypeface(), Typeface.BOLD);

                                        } else if (dayweek == 6) {
                                            fri.setTypeface(fri.getTypeface(), Typeface.BOLD);

                                        } else if (dayweek == 7) {
                                            sat.setTypeface(sat.getTypeface(), Typeface.BOLD);
                                        }
                                        mainLayout.setVisibility(View.VISIBLE);
                                        sun.setText(day.get(6) + " " + open.get(6) + " - " + close.get(6));
                                        mon.setText(day.get(0) + " " + open.get(0) + " - " + close.get(0));
                                        tue.setText(day.get(1) + " " + open.get(1) + " - " + close.get(1));
                                        wed.setText(day.get(2) + " " + open.get(2) + " - " + close.get(2));
                                        thur.setText(day.get(3) + " " + open.get(3) + " - " + close.get(3));
                                        fri.setText(day.get(4) + " " + open.get(4) + " - " + close.get(4));
                                        sat.setText(day.get(5) + " " + open.get(5) + " - " + close.get(5));
                                    }
                                }
                            });

                    if (newLoadshedding.equalsIgnoreCase("1")) {

                        cat1.setImageResource(R.drawable.loadshedding);
                    }

                    if (newDelivery.equalsIgnoreCase("1")) {


                        cat2.setImageResource(R.drawable.delivery);
                    }
                    if (newCollect.equalsIgnoreCase("1")) {

                        cat3.setImageResource(R.drawable.callandcollect);
                    }
                    if (newBreakfast.equalsIgnoreCase("1")) {

                        cat4.setImageResource(R.drawable.breakfast);
                    }
                    if (newCoffee.equalsIgnoreCase("1")) {

                        cat5.setImageResource(R.drawable.coffee);
                    }
                    if (newDriveThru.equalsIgnoreCase("1")) {

                        cat6.setImageResource(R.drawable.drivethru);
                    }
                    if (newFullService.equalsIgnoreCase("1")) {

                        cat7.setImageResource(R.drawable.fullservice);
                    }
                    if (newhalaal.equalsIgnoreCase("1")) {

                        cat8.setImageResource(R.drawable.halaal);
                    }
                    if (newKosher.equalsIgnoreCase("1")) {

                        cat9.setImageResource(R.drawable.kosher);
                    }
                    if (newLisenced.equalsIgnoreCase("1")) {

                        cat10.setImageResource(R.drawable.alcohol);
                    }


                }


                return false;
            }
        });


        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                mViewGroup.setVisibility(View.GONE);
            }
        });


        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition arg0) {
                // TODO Auto-generated method stub


                moveLat = map.getCameraPosition().target.latitude;
                moveLon = map.getCameraPosition().target.longitude;

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
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


        bizClick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int dayweek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                Calendar currentDate = Calendar.getInstance();
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                String dateNow = formatter.format(currentDate.getTime());
                String storeHours = "";

                if (dayweek == 1) {
                    String storeclose = close.get(6).substring(0, 2);
                    ;
                    String timenow = dateNow.substring(0, 2);
                    storeHours = close.get(6).substring(0, 2);

                    int store = Integer.parseInt(storeclose) - 1;
                    int currenttime = Integer.parseInt(timenow);
                    if (currenttime > store || newNowactive.equalsIgnoreCase("0")) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                getActivity());
                        // set title
                        alertDialogBuilder.setTitle("Store Closed");
                        // set dialog message
                        alertDialogBuilder
                                .setMessage("This store is currently closed/offline and only pre-orders for tomorrow can be made")
                                .setCancelable(false)

                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
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

                } else if (dayweek == 2) {

                    String storeclose = close.get(0).substring(0, 2);
                    ;
                    String timenow = dateNow.substring(0, 2);
                    storeHours = close.get(0).substring(0, 2);

                    int store = Integer.parseInt(storeclose) - 1;
                    int currenttime = Integer.parseInt(timenow);
                    if (currenttime > store || newNowactive.equalsIgnoreCase("0")) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                getActivity());
                        // set title
                        alertDialogBuilder.setTitle("Store Closed");
                        // set dialog message
                        alertDialogBuilder
                                .setMessage("This store is currently closed/offline and only pre-orders for tomorrow can be made")
                                .setCancelable(false)

                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
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

                } else if (dayweek == 3) {

                    String storeclose = close.get(1).substring(0, 2);
                    ;
                    String timenow = dateNow.substring(0, 2);
                    storeHours = close.get(1).substring(0, 2);

                    int store = Integer.parseInt(storeclose) - 1;
                    int currenttime = Integer.parseInt(timenow);
                    if (currenttime > store || newNowactive.equalsIgnoreCase("0")) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                getActivity());
                        // set title
                        alertDialogBuilder.setTitle("Store Closed");
                        // set dialog message
                        alertDialogBuilder
                                .setMessage("This store is currently closed/offline and only pre-orders for tomorrow can be made")
                                .setCancelable(false)

                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
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

                } else if (dayweek == 4) {

                    String storeclose = close.get(2).substring(0, 2);
                    ;
                    String timenow = dateNow.substring(0, 2);
                    storeHours = close.get(2).substring(0, 2);

                    int store = Integer.parseInt(storeclose) - 1;
                    int currenttime = Integer.parseInt(timenow);
                    if (currenttime > store || newNowactive.equalsIgnoreCase("0")) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                getActivity());
                        // set title
                        alertDialogBuilder.setTitle("Store Closed");
                        // set dialog message
                        alertDialogBuilder
                                .setMessage("This store is currently closed/offline and only pre-orders for tomorrow can be made")
                                .setCancelable(false)

                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
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

                } else if (dayweek == 5) {

                    String storeclose = close.get(3).substring(0, 2);
                    ;
                    String timenow = dateNow.substring(0, 2);
                    storeHours = close.get(3).substring(0, 2);

                    int store = Integer.parseInt(storeclose) - 1;
                    int currenttime = Integer.parseInt(timenow);
                    if (currenttime > store || newNowactive.equalsIgnoreCase("0")) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                getActivity());
                        // set title
                        alertDialogBuilder.setTitle("Store Closed");
                        // set dialog message
                        alertDialogBuilder
                                .setMessage("This store is currently closed/offline and only pre-orders for tomorrow can be made")
                                .setCancelable(false)

                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
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

                } else if (dayweek == 6) {

                    String storeclose = close.get(4).substring(0, 2);
                    ;
                    String timenow = dateNow.substring(0, 2);
                    storeHours = close.get(5).substring(0, 2);

                    int store = Integer.parseInt(storeclose) - 1;
                    int currenttime = Integer.parseInt(timenow);
                    if (currenttime > store || newNowactive.equalsIgnoreCase("0")) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                getActivity());
                        // set title
                        alertDialogBuilder.setTitle("Store Closed");
                        // set dialog message
                        alertDialogBuilder
                                .setMessage("This store is currently closed/offline and only pre-orders for tomorrow can be made")
                                .setCancelable(false)

                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
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
                } else if (dayweek == 7) {

                    String storeclose = close.get(5).substring(0, 2);
                    ;
                    String timenow = dateNow.substring(0, 2);
                    storeHours = close.get(5).substring(0, 2);

                    int store = Integer.parseInt(storeclose) - 1;
                    int currenttime = Integer.parseInt(timenow);
                    if (currenttime > store || newNowactive.equalsIgnoreCase("0")) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                getActivity());
                        // set title
                        alertDialogBuilder.setTitle("Store Closed");
                        // set dialog message
                        alertDialogBuilder
                                .setMessage("This store is currently closed/offline and only pre-orders for tomorrow can be made")
                                .setCancelable(false)

                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
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


                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                String jsonString = "";


                String[] temp = pref.getString("TempStore", null).split("-");
                String newName = temp[0];
                String newApi = temp[1];
                String newDelivery = temp[2];
                String newHours = temp[3];


                editor.putString("StoreName", newName);
                editor.putString("StoreAPI", newApi);
                editor.putString("StoreHours", storeHours);

                if (newDelivery.equalsIgnoreCase("1")) {
                    editor.putString("StoreDelivery", "TRUE");
                } else {
                    editor.putString("StoreDelivery", "FALSE");
                }
                editor.commit(); // commit changes


                Toast.makeText(
                        getActivity(),
                        "Nando's " + pref.getString("StoreName", null) + " Selected",
                        Toast.LENGTH_LONG).show();
                if (pref.getString("StoreAPI", null) != null && (!pref.getString("StoreAPI", null).equalsIgnoreCase(""))) {
                    fab.setVisibility(View.VISIBLE);
                } else {
                    fab.setVisibility(View.GONE);
                }


            }

        });


        callButton = (Button) rootView.findViewById(R.id.call_btn);

        DirectionsButton = (Button) rootView.findViewById(R.id.direction_btn);

        callButton.setTypeface(face);
        DirectionsButton.setTypeface(face);

        callButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                String num = number;
                if (num == null || num.equalsIgnoreCase("null")) {
                    Toast.makeText(getActivity(), "Number not available", Toast.LENGTH_SHORT).show();
                } else {
                    Intent myIntent = new Intent(Intent.ACTION_CALL);
                    String phNum = "tel:" + number;
                    myIntent.setData(Uri.parse(phNum));
                    v.getContext().startActivity(myIntent);
                }
            }

        });


        DirectionsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View w) {

                String latstr = lat;
                String lonstr = lon;


                if (latstr == null || lonstr == null || latstr.equalsIgnoreCase("null") || lonstr.equalsIgnoreCase("null") || latstr.equalsIgnoreCase("") || lonstr.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Location is not available", Toast.LENGTH_SHORT).show();
                } else {

                    if (isGoogleMapsInstalled()) {
                        try {

                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse("http://maps.google.com/maps?daddr=" + latstr + "," + lonstr));
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                            w.getContext().startActivity(intent);
                        } catch (Exception e) {
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse("geo:0,0?q=" + latstr + "," + lonstr + ""));
                            w.getContext().startActivity(intent);
                        }
                    } else {

                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("geo:0,0?q=" + latstr + "," + lonstr + ""));
                        w.getContext().startActivity(intent);
                    }

                }

            }

        });


        return rootView;

    }


    protected boolean isRouteDisplayed() {
        return false;
    }


    public boolean isGoogleMapsInstalled() {
        try {
            ApplicationInfo info = getActivity().getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


}
