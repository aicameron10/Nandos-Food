package com.mydeliveries.nandos.adapter;


import com.mydeliveries.nandos.R;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;

    public ProfileAdapter(Activity context,
                          String[] web, Integer[] imageId) {
        super(context, R.layout.list_profile, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_profile, null, true);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/webfont.ttf");
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        txtTitle.setTypeface(face);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(web[position]);
        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}

