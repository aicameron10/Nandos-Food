package com.mydeliveries.nandos.fragments;

import com.github.clans.fab.FloatingActionMenu;
import com.mydeliveries.nandos.R;
import com.mydeliveries.nandos.util.AlertDialogManager;
import com.mydeliveries.nandos.util.ConnectionDetector;
import com.mydeliveries.nandos.activity.HomeActivity;
import com.mydeliveries.nandos.adapter.ViewPagerAdapter;
import com.mydeliveries.nandos.model.NavItem;
import com.mydeliveries.nandos.util.SlidingTabLayout;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import android.widget.Toast;

import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener {


    protected static final String TAG = null;


    double latitude;
    double longitude;

    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    SharedPreferences sharedpreferences;

    FloatingActionMenu menu1;


    private List<FloatingActionMenu> menus = new ArrayList<>();


    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[] = {"Chicken", "Burgers,Wraps/Pitas", "Salads", "Taste Sensations", "Nandinos", "Sides", "Drinks", "Extras", "Desserts"};
    int Numboftabs = 9;


    View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((HomeActivity) getActivity()).loadDrawsHide();
        ((HomeActivity) getActivity()).loadDrawsDisplay();


        NavItem nav = new NavItem();
        nav.setPage("HomePage");


        try {
            cd = new ConnectionDetector(getActivity().getApplicationContext());

            // Check if Internet present
            isInternetPresent = cd.isConnectingToInternet();
            System.out.println("connection - " + isInternetPresent);

            if (!isInternetPresent) {
                // Internet Connection is not present
                alert.showAlertDialog(getActivity(), "Internet Connection Error",
                        "No Internet connection found. Please connect and retry.", false);
                // stop executing code by return
                // return;

            }

        } catch (Exception e) {

        }


        menu1 = (FloatingActionMenu) rootView.findViewById(R.id.fab_image_button);

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
                            "Please add order to cart",
                            Toast.LENGTH_LONG).show();
                }
            }
        });


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), Titles, Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) rootView.findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) rootView.findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width


        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);


        return rootView;
    }


    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        @SuppressWarnings("unused")
        String str = (String) adapterView.getItemAtPosition(position);

    }


}
