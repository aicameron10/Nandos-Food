package com.mydeliveries.nandos.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.mydeliveries.nandos.R;
import com.mydeliveries.nandos.app.AppController;
import com.mydeliveries.nandos.model.Notification;

import java.util.List;


public class CustomListAdapterNotifications extends BaseAdapter implements OnClickListener {


    private Activity activity;
    private LayoutInflater inflater;
    private List<Notification> listItems;


    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapterNotifications(Activity activity, List<Notification> listItems) {
        this.activity = activity;
        this.listItems = listItems;

    }


    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int location) {
        return listItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.activity_card_view, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.image);
        //	TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView body = (TextView) convertView.findViewById(R.id.body);


        // getting listing data for the row
        final Notification m = listItems.get(position);
        String logopath = m.getImage();


        Typeface face1 = Typeface.createFromAsset(activity.getAssets(), "fonts/webfont.ttf");

        thumbNail.setImageUrl(logopath, imageLoader);

        body.setText(m.getBody());


        String msg = "<strong>" + m.getTitle().trim() + "</strong>";


        title.setText(Html.fromHtml(msg));
        title.setTypeface(face1);


        return convertView;
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }


}



