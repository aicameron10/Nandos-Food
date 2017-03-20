package com.mydeliveries.nandos.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mydeliveries.nandos.R;
import com.mydeliveries.nandos.activity.HomeActivity;
import com.mydeliveries.nandos.app.AppController;
import com.mydeliveries.nandos.model.NavItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class SocialFragment extends Fragment {


    TextView name;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_social, container, false);

        // Get tracker.
        Tracker t = ((AppController) getActivity().getApplication()).getTracker(
                AppController.TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Social Media Page");

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
        twitter.setTypeface(face);
        facebook.setTypeface(face);
        youtube.setTypeface(face);
        pinterest.setTypeface(face);

        final ImageView twitterimg = (ImageView) rootView.findViewById(R.id.twitter);
        final ImageView facebookimg = (ImageView) rootView.findViewById(R.id.facebook);
        final ImageView youtubeimg = (ImageView) rootView.findViewById(R.id.youtube);
        final ImageView pinterestimg = (ImageView) rootView.findViewById(R.id.pinterest);


        twitterimg.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/NandosSA"));
                        startActivity(intent);
                    }
                });
        facebookimg.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/NandosSouthAfrica"));
                        startActivity(intent);
                    }
                });
        youtubeimg.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/user/NandosADS"));
                        startActivity(intent);
                    }
                });
        pinterestimg.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.pinterest.com/nandosoriginal/"));
                        startActivity(intent);
                    }
                });

        twitter.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/NandosSA"));
                        startActivity(intent);
                    }
                });
        facebook.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/NandosSouthAfrica"));
                        startActivity(intent);
                    }
                });
        youtube.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/user/NandosADS"));
                        startActivity(intent);
                    }
                });
        pinterest.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.pinterest.com/nandosoriginal/"));
                        startActivity(intent);
                    }
                });


        return rootView;
    }


}
