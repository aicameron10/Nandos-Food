package com.mydeliveries.nandos.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mydeliveries.nandos.R;
import com.mydeliveries.nandos.activity.HomeActivity;
import com.mydeliveries.nandos.model.NavItem;


public class TermsWebViewFragment extends Fragment {
    // Log tag

    private ProgressDialog pDialog;

    String option = "";
    String URL = "";
    private WebView webView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            option = bundle.getString("option");


        }


        View rootView = inflater.inflate(R.layout.activity_termswebview, container, false);

        ((HomeActivity) getActivity()).loadDrawsHide();


        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((HomeActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);


        NavItem nav = new NavItem();
        nav.setPage("Home");


        final ProgressBar Pbar;
        final TextView txtview = (TextView) rootView.findViewById(R.id.tV1);
        Pbar = (ProgressBar) rootView.findViewById(R.id.pB1);

        webView = (WebView) rootView.findViewById(R.id.webViewInfo);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new MyWebViewClient());


        URL = "http://www.mydeliveries.co.za/terms/terms.html";


        webView.loadUrl(URL);

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100 && Pbar.getVisibility() == ProgressBar.GONE) {
                    Pbar.setVisibility(ProgressBar.VISIBLE);
                    txtview.setVisibility(View.VISIBLE);
                }
                Pbar.setProgress(progress);
                if (progress == 100) {
                    Pbar.setVisibility(ProgressBar.GONE);
                    txtview.setVisibility(View.GONE);
                }
            }
        });

        return rootView;
    }


    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }
    }


}
