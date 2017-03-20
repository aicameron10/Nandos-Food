package com.mydeliveries.nandos.adapter;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.mydeliveries.nandos.R;
import com.mydeliveries.nandos.fragments.AddressBookFragment;
import com.mydeliveries.nandos.activity.HomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AddressAdapter extends ArrayAdapter<String> {

    private FragmentActivity context;
    private final String[] name;
    private final String[] street;
    private final String[] unitno;
    private final String[] complex;
    private final String[] suburb;
    private final String[] city;
    private final String[] province;


    private ProgressDialog pDialog;
    JSONArray jarray = null;

    Integer pos = -1;
    Double counter = 0.0;

    AlertDialog alertDialog;

    public AddressAdapter(FragmentActivity context,
                          String[] name, String[] street, String[] unitno, String[] complex, String[] suburb, String[] city, String[] province) {
        super(context, R.layout.list_add, name);
        this.context = context;
        this.name = name;
        this.street = street;
        this.unitno = unitno;
        this.complex = complex;
        this.suburb = suburb;
        this.city = city;
        this.province = province;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_add, null, true);
        final Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/webfont.ttf");
        TextView txtName = (TextView) rowView.findViewById(R.id.name);

        txtName.setTypeface(face);

        Button delete = (Button) rowView.findViewById(R.id.delete_list);

        Button edit = (Button) rowView.findViewById(R.id.edit_list);


        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set title
                alertDialogBuilder.setTitle("Remove - " + name[position]);

                // Use an EditText view to get user input.

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity

                                pos = position;

                                new deleteadd().execute();

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing


                                dialog.cancel();
                            }
                        });

                // create alert dialog
                alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();


            }

        });

        edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                Editor editor = pref.edit();
                String addresses = "[" + pref.getString("Address", null) + "]";
                try {
                    JSONArray jsonObjorder = new JSONArray(addresses);

                    JSONObject first = jsonObjorder.getJSONObject(position);

                    first.put("pos", position);


                    editor.putString("TempAddress", first.toString());

                    editor.commit(); // commit changes

                    System.out.println(pref.getString("TempAddress", null));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ((HomeActivity) context).displayView(6);

            }

        });


        txtName.setText(name[position]);


        return rowView;
    }


    private class deleteadd extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            // Showing progress dialog before sending http request

        }

        @Override
        protected String doInBackground(String... params) {


            SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
            Editor editor = pref.edit();
            String fullOrder = "";

            fullOrder = "[" + pref.getString("Address", null) + "]";
            JSONArray oparray = null;
            try {
                oparray = new JSONArray(fullOrder);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            remove(pos, oparray);


            String newAdd = jarray.toString();
            String add = newAdd.substring(1);
            String finalorder = add.substring(0, add.length() - 1);
            editor.putString("Address", finalorder);

            editor.commit(); // commit changes


            AddressBookFragment AddressBookFragment = new AddressBookFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction;
            fragmentTransaction = context.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, AddressBookFragment);
            fragmentTransaction.commit();

            return null;
        }


        @Override
        protected void onPostExecute(String file) {

            notifyDataSetChanged();

            super.onPostExecute(file);


        }


    }


    public JSONArray remove(final int index, final JSONArray from) {
        final List<JSONObject> objs = getList(from);
        objs.remove(index);

        jarray = new JSONArray();
        for (final JSONObject obj : objs) {
            jarray.put(obj);
        }

        return jarray;
    }

    public static List<JSONObject> getList(final JSONArray jarray) {
        final int len = jarray.length();
        final ArrayList<JSONObject> result = new ArrayList<JSONObject>(len);
        for (int i = 0; i < len; i++) {
            final JSONObject obj = jarray.optJSONObject(i);
            if (obj != null) {
                result.add(obj);
            }
        }
        return result;
    }


}

