package com.mydeliveries.nandos.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.mydeliveries.nandos.R;
import com.mydeliveries.nandos.util.AlertDialogManager;
import com.mydeliveries.nandos.util.ConnectionDetector;
import com.mydeliveries.nandos.activity.HomeActivity;
import com.mydeliveries.nandos.model.NavItem;


public class StoreFragment extends Fragment implements OnItemClickListener {


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


    View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_home, container, false);


        NavItem nav = new NavItem();

        nav.setPage("Home");


        ((HomeActivity) getActivity()).loadDrawsHide();

        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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


        return rootView;
    }


    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        @SuppressWarnings("unused")
        String str = (String) adapterView.getItemAtPosition(position);

    }


}
