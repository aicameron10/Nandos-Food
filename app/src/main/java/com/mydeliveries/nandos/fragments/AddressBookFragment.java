package com.mydeliveries.nandos.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mydeliveries.nandos.R;
import com.mydeliveries.nandos.activity.HomeActivity;
import com.mydeliveries.nandos.adapter.AddressAdapter;
import com.mydeliveries.nandos.model.NavItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 10/07/2015.
 */


public class AddressBookFragment extends Fragment {


    List<String> listingname = new ArrayList<String>();
    List<String> listingstreet = new ArrayList<String>();
    List<String> listingunitno = new ArrayList<String>();
    List<String> listingcomplex = new ArrayList<String>();
    List<String> listingsuburb = new ArrayList<String>();
    List<String> listingcity = new ArrayList<String>();
    List<String> listingprovince = new ArrayList<String>();
    JSONArray oparray;
    ListView list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        listingname.clear();
        listingstreet.clear();
        listingunitno.clear();
        listingcomplex.clear();
        listingsuburb.clear();
        listingcity.clear();
        listingprovince.clear();


        NavItem nav = new NavItem();

        nav.setPage("Profile");

        Typeface face1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/webfont.ttf");


        ((HomeActivity) getActivity()).loadDrawsHide();


        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((HomeActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        final View rootView = inflater.inflate(R.layout.fragment_addressbook, container, false);

        TextView head = (TextView) rootView.findViewById(R.id.heading);

        head.setTypeface(face1);

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        editor.remove("TempAddress");
        editor.commit();

        String addresses = "[" + pref.getString("Address", null) + "]";


        try {
            oparray = new JSONArray(addresses);


            for (int i = 0; i < oparray.length(); i++) {
                JSONObject obj = oparray.getJSONObject(i);

                String name = (String) obj.get("name");
                String street = (String) obj.get("street");
                String unitno = (String) obj.get("unitno");
                String complex = (String) obj.get("complex");
                String suburb = (String) obj.get("suburb");
                String city = (String) obj.get("city");
                String province = (String) obj.get("province");


                listingname.add(name);
                listingstreet.add(street);
                listingunitno.add(unitno);
                listingcomplex.add(complex);
                listingsuburb.add(suburb);
                listingcity.add(city);
                listingprovince.add(province);


            }
            //listingname.add("Create New List");
            //listingList.add(Integer.toString(1));

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //final String[] web = (String[]) listingname.toArray();

        final String[] name = listingname.toArray(new String[listingname.size()]);

        final String[] street = listingstreet.toArray(new String[listingstreet.size()]);
        final String[] unitno = listingunitno.toArray(new String[listingunitno.size()]);

        final String[] complex = listingcomplex.toArray(new String[listingcomplex.size()]);
        final String[] suburb = listingsuburb.toArray(new String[listingsuburb.size()]);

        final String[] city = listingcity.toArray(new String[listingcity.size()]);
        final String[] province = listingprovince.toArray(new String[listingprovince.size()]);


        AddressAdapter adapter = new
                AddressAdapter(getActivity(), name, street, unitno, complex, suburb, city, province);
        list = (ListView) rootView.findViewById(R.id.list_lists);
        list.setAdapter(adapter);
        listingname.clear();
        listingstreet.clear();
        listingunitno.clear();
        listingcomplex.clear();
        listingsuburb.clear();
        listingcity.clear();
        listingprovince.clear();
        setListViewHeightBasedOnChildren(list);


        Button AddButton = (Button) rootView.findViewById(R.id.buttonAdd);

        AddButton.setTypeface(face1);
        AddButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ((HomeActivity) getActivity()).displayView(6);
            }

        });


        return rootView;
    }


    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


}
