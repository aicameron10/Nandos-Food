package com.mydeliveries.nandos.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.mydeliveries.nandos.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class Tab6 extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_6, container, false);
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        String Menu = pref.getString("Menu", null); // getting String
        String newMenu = Menu.replaceAll("=", ":");


        // Hashmap for ListView
        ArrayList<HashMap<String, String>> menu = new ArrayList<HashMap<String, String>>();


        // getting JSON string from URL
        JSONArray contacts = null;
        try {
            contacts = new JSONArray(newMenu);

            for (int i = 0; i < contacts.length(); i++) {
                JSONObject json_data = contacts.getJSONObject(i);

                // Storing each json item in variable
                String cat = json_data.getString("row(0)");
                String name = json_data.getString("row(1)");
                String desc = json_data.getString("row(2)");
                String price = json_data.getString("row(3)");

                HashMap<String, String> map = new HashMap<String, String>();
                if (cat.equalsIgnoreCase("sides")) {
                    map.put("row(1)", name);
                    map.put("row(2)", desc);
                    map.put("row(3)", price);

                    menu.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" + menu);

        // keys of hashmap
        String[] from = {"row(1)", "row(2)", "row(3)"};

        // view id's to which data to be binded
        int[] to = {R.id.name, R.id.description, R.id.price};

        // Creating Adapter
        ListAdapter adapter = new SimpleAdapter(getActivity(), menu,
                R.layout.list_items, from, to);

        // Setting Adapter to ListView
        setListAdapter(adapter);
        return v;
    }
}