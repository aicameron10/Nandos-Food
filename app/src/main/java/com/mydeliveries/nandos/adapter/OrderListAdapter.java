package com.mydeliveries.nandos.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import com.mydeliveries.nandos.R;
import com.mydeliveries.nandos.activity.HomeActivity;
import com.mydeliveries.nandos.app.AppController;
import com.mydeliveries.nandos.model.Orders;
import com.mydeliveries.nandos.util.GPSTracker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class OrderListAdapter extends BaseAdapter implements OnClickListener {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Orders> listItems;
    private Button callButton = null;
    private Button DirectionsButton = null;

    // GPSTracker class
    GPSTracker gps;

    String reference = "";
    String orderdate = "";

    String orderId = "";
    String orderDate = "";

    String StoreAPI = "";


    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public OrderListAdapter(Activity activity, List<Orders> listItems) {
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
            convertView = inflater.inflate(R.layout.list_row_order, null);


        Typeface face = Typeface.createFromAsset(activity.getAssets(), "fonts/webfont.ttf");


        //	TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView details = (TextView) convertView.findViewById(R.id.details);

        final TextView order = (TextView) convertView.findViewById(R.id.order);


        TextView bizClick = (TextView) convertView.findViewById(R.id.bizClick);
        //TextView genre = (TextView) convertView.findViewById(R.id.genre);

        // getting listing data for the row
        final Orders m = listItems.get(position);


        bizClick.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Date date = Calendar.getInstance().getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                String Date = sdf.format(date);

                reference = m.getCapture();
                orderId = m.getId();
                orderDate = m.getOrderdate();
                String[] parts = orderDate.split(" ");
                String part1 = parts[0];

                System.out.println(orderDate);
                System.out.println(Date);
                System.out.println(part1);
                if (reference.equalsIgnoreCase("0") && Date.equals(part1)) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            activity);

                    // set title
                    alertDialogBuilder.setTitle("Cancel Order");


                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Cancel Order Placed!")
                            .setCancelable(false)
                            .setPositiveButton("Cancel Order", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    new deleteOrder().execute();
                                    StoreAPI = m.getStoreapi();

                                }
                            })

                            .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing

                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                } else {


                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            activity);

                    // set title
                    alertDialogBuilder.setTitle("Re-place Order");


                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Place Order Again")
                            .setCancelable(false)
                            .setPositiveButton("Order", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    SharedPreferences pref = activity.getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                                    SharedPreferences.Editor editor = pref.edit();

                                    String neworder = m.getOrder().toString();
                                    neworder = neworder.substring(1, neworder.length() - 1);
                                    editor.putString("Order", neworder);


                                    editor.putString("StoreName", m.getStorename());
                                    editor.putString("StoreAPI", m.getStoreapi());
                                    if (m.getStoredelivery().equalsIgnoreCase("1")) {
                                        editor.putString("StoreDelivery", "TRUE");
                                    } else {
                                        editor.putString("StoreDelivery", "FALSE");
                                    }


                                    editor.commit(); // commit changes

                                    ((HomeActivity) activity).displayView(2);

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
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                }


            }

        });


        order.setText(m.getOrder());


        orderdate = m.getOrderdate();


        String price = "";
        String quantity = "";
        String meal = "";

        try {

            JSONArray array1 = new JSONArray(m.getOrder());


            // Parsing json
            for (int i = 0; i < array1.length(); i++) {
                JSONObject obj = array1.getJSONObject(i);

                price = obj.getString("price");
                quantity = obj.getString("quantity");
                meal = obj.getString("meal");


            }

        } catch (Exception e) {

        }

        if (price.equalsIgnoreCase("")) {
            price = "0.0";
        }
        String strOrder = "";
        try {

            JSONArray orderarr = new JSONArray(m.getOrder());


            for (int i = 0; i < orderarr.length(); i++) {
                JSONObject obj = orderarr.getJSONObject(i);

                String mealarr = obj.getString("meal");
                String quantityarr = obj.getString("quantity");
                String pricearr = obj.getString("price");
                String veggie = obj.getString("veggie");
                String type = obj.getString("type");
                String side = obj.getString("side");
                String drink = obj.getString("drink");
                String extra = obj.getString("extra");

                String[] parts = extra.split(",");
                String cheese = parts[0];
                String pine = parts[1];

                strOrder = strOrder + "<br />" + quantityarr + " x " + mealarr + " - R" + String.format("%.2f", Double.parseDouble(pricearr)) + "<br />-Veggie: " + veggie + "<br />-Side: " + side + "<br />-Drink: " + drink + "<br />-Hotlevel: " + type + "<br />-Extras: Cheese - " + cheese + ", Pine - " + pine;

            }

        } catch (Exception e) {

        }


        String msg = "<strong>Name: </strong>" + m.getName().trim() + " " + m.getSurname() + "<br /><strong>Order: </strong>" + strOrder + "<br /><strong> Order Date: </strong>" + m.getOrderdate() + "<br /><strong>Reference Number: </strong> " + m.getReference();


        details.setText(Html.fromHtml(msg));
        details.setTypeface(face);

		/* genre
        String genreStr = "";
		for (String str : m.getGenre()) {
			genreStr += str + ", ";
		}
		genreStr = genreStr.length() > 0 ? genreStr.substring(0,
				genreStr.length() - 2) : genreStr;
		genre.setText(genreStr);*/


        return convertView;
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }


    class deleteOrder extends AsyncTask<String, String, String> {

        int res = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        /**
         * getting Places JSON
         */
        protected String doInBackground(String... args) {
            // creating Places class object
            String baseURL = "";


            try {

                baseURL = "http://www.mydeliveries.co.za/ordermanager/nandos/orders/" + orderId;

                System.out.println("***********baseURL" + baseURL + "**************");

                URL url = null;
                HttpURLConnection urlConnection = null;

                url = new URL(baseURL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("DELETE");
                urlConnection.setRequestProperty("Authorization", StoreAPI);


                // To post JSON content, set the content type to application/json OR application/jsonrequest
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Cache-Control", "no-cache"); // Post your content
                ;


                res = urlConnection.getResponseCode();
                System.out.println("***********action" + res + "**************");

            } catch (Exception e) {

            }

            return null;
        }


        protected void onPostExecute(String file_url) {

            if (res == 200) {


                Toast toast = Toast.makeText(activity, "Order Cancelled successfully", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();


            } else {


                Toast toast = Toast.makeText(activity, "Error Cancelling , please try again!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();


            }

            notifyDataSetChanged();
            ((HomeActivity) activity).displayView(3);

            super.onPostExecute(file_url);
        }


    }

	/*public void sort() {
		Collections.sort(this, new Comparator<Orders>() {
			@Override
			public int compare(Orders item1, Orders item2) {
				return Double.toString(item2.getDistance()).compareTo(Double.toString(item1.getDistance()));
			}
		});
	}
	*/
}



