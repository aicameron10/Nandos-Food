package com.mydeliveries.nandos.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mydeliveries.nandos.R;
import com.mydeliveries.nandos.activity.HomeActivity;
import com.mydeliveries.nandos.app.AppController;
import com.mydeliveries.nandos.model.NavItem;


public class RewardsFragment extends Fragment {


    TextView name;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_rewards, container, false);

        // Get tracker.
        Tracker t = ((AppController) getActivity().getApplication()).getTracker(
                AppController.TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Rewards Page");

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());

        ((HomeActivity) getActivity()).loadDrawsHide();


        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((HomeActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        NavItem nav = new NavItem();
        nav.setPage("Home");

        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/webfont.ttf");
        final TextView heading = (TextView) rootView.findViewById(R.id.heading);
        final TextView twitter = (TextView) rootView.findViewById(R.id.textView1);
        final TextView facebook = (TextView) rootView.findViewById(R.id.textView2);
        final TextView youtube = (TextView) rootView.findViewById(R.id.textView3);
        final TextView pinterest = (TextView) rootView.findViewById(R.id.textView4);
        heading.setTypeface(face);


        return rootView;
    }


}
