package com.mydeliveries.nandos.fragments;

import java.util.ArrayList;
import java.util.List;

import com.mydeliveries.nandos.util.AlertDialogManager;
import com.mydeliveries.nandos.activity.HomeActivity;
import com.mydeliveries.nandos.adapter.ProfileAdapter;
import com.mydeliveries.nandos.app.AppController;
import com.mydeliveries.nandos.model.NavItem;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mydeliveries.nandos.R;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class ProfileFragment extends Fragment {

    ListView list;
    String[] web = {
            "User Details",
            "Manage Address Book"


    };
    Integer[] imageId = {
            R.drawable.ic_user,
            R.drawable.ic_house


    };
    String url = "";

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();
    private List<String> Profile = new ArrayList<String>();

    private ProgressDialog pDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        // Get tracker.
        Tracker t = ((AppController) getActivity().getApplication()).getTracker(
                AppController.TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Profile Page");

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());


        NavItem nav = new NavItem();

        nav.setPage("Home");

        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/webfont.ttf");
        final TextView heading = (TextView) rootView
                .findViewById(R.id.heading);
        heading.setTypeface(face);

        ((HomeActivity) getActivity()).loadDrawsHide();


        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((HomeActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        ProfileAdapter adapter = new
                ProfileAdapter(getActivity(), web, imageId);
        list = (ListView) rootView.findViewById(R.id.listprofile);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (web[+position].equalsIgnoreCase("User Details")) {

                    ((HomeActivity) getActivity()).displayView(7);

                } else if (web[+position].equalsIgnoreCase("Manage Address Book")) {


                    ((HomeActivity) getActivity()).displayView(8);

                } else {

                    //((HomeActivity) getActivity()).displayView(5);
                }
                // Toast.makeText(getActivity(), "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
            }
        });


        return rootView;
    }


}
