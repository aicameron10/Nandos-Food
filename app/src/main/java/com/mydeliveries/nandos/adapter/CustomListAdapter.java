package com.mydeliveries.nandos.adapter;

import com.mydeliveries.nandos.R;
import com.mydeliveries.nandos.fragments.ListViewFragment;
import com.mydeliveries.nandos.app.AppController;
import com.mydeliveries.nandos.model.Listing;
import com.mydeliveries.nandos.util.GPSTracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;

import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;


public class CustomListAdapter extends BaseAdapter implements OnClickListener {

    private final ListViewFragment fragment;
    private Activity activity;
    private LayoutInflater inflater;
    private List<Listing> listItems;
    private Button callButton = null;
    private Button DirectionsButton = null;

    // GPSTracker class
    GPSTracker gps;

    String reference = "";
    Double dis = 0.0;


    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<Listing> listItems, ListViewFragment fragment) {
        this.activity = activity;
        this.listItems = listItems;
        this.fragment = fragment;
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

        Typeface face = Typeface.createFromAsset(activity.getAssets(), "fonts/webfont.ttf");
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        ImageView thumbNail = (ImageView) convertView
                .findViewById(R.id.thumbnail);
        //	TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView address = (TextView) convertView.findViewById(R.id.address);
        TextView active = (TextView) convertView.findViewById(R.id.Active);
        final TextView bizidtxt = (TextView) convertView.findViewById(R.id.bizid);
        final TextView disttxt = (TextView) convertView.findViewById(R.id.distance);

        ImageView cat1 = (ImageView) convertView.findViewById(R.id.cat1);
        ImageView cat2 = (ImageView) convertView.findViewById(R.id.cat2);
        ImageView cat3 = (ImageView) convertView.findViewById(R.id.cat3);
        ImageView cat4 = (ImageView) convertView.findViewById(R.id.cat4);
        ImageView cat5 = (ImageView) convertView.findViewById(R.id.cat5);
        ImageView cat6 = (ImageView) convertView.findViewById(R.id.cat6);
        ImageView cat7 = (ImageView) convertView.findViewById(R.id.cat7);
        ImageView cat8 = (ImageView) convertView.findViewById(R.id.cat8);
        ImageView cat9 = (ImageView) convertView.findViewById(R.id.cat9);
        ImageView cat10 = (ImageView) convertView.findViewById(R.id.cat10);
        TextView bizClick = (TextView) convertView.findViewById(R.id.bizClick);

        final TextView sun = (TextView) convertView.findViewById(R.id.sun);
        final TextView mon = (TextView) convertView.findViewById(R.id.mon);
        final TextView tue = (TextView) convertView.findViewById(R.id.tue);
        final TextView wed = (TextView) convertView.findViewById(R.id.wed);
        final TextView thur = (TextView) convertView.findViewById(R.id.thur);
        final TextView fri = (TextView) convertView.findViewById(R.id.fri);
        final TextView sat = (TextView) convertView.findViewById(R.id.sat);

        TextView hours = (TextView) convertView.findViewById(R.id.hours);

        final LinearLayout mainLayout = (LinearLayout) convertView.findViewById(R.id.linearLayout4);
        //TextView genre = (TextView) convertView.findViewById(R.id.genre);

        // getting listing data for the row
        final Listing m = listItems.get(position);

        final ArrayList<String> day = new ArrayList<String>();
        final ArrayList<String> open = new ArrayList<String>();
        final ArrayList<String> close = new ArrayList<String>();

        final int nowactive = m.getNowactive();

        try {

            JSONArray hoursjson = new JSONArray(m.getHours());


            for (int i = 0; i < hoursjson.length(); i++) {
                JSONObject obj = hoursjson.getJSONObject(i);

                day.add(obj.getString("day"));
                open.add(obj.getString("open"));
                close.add(obj.getString("close"));


            }

        } catch (Exception e) {

        }


        mainLayout.setVisibility(View.GONE);


        bizClick.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                int dayweek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                Calendar currentDate = Calendar.getInstance();
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                String dateNow = formatter.format(currentDate.getTime());

                String storeHours = "";

                if (dayweek == 1) {
                    String storeclose = close.get(6).substring(0, 2);
                    String timenow = dateNow.substring(0, 2);
                    storeHours = close.get(6).substring(0, 2);

                    int store = Integer.parseInt(storeclose) - 1;
                    int currenttime = Integer.parseInt(timenow);
                    if (currenttime >= store || nowactive == 0) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                activity);
                        // set title
                        alertDialogBuilder.setTitle("Store Closed");
                        // set dialog message
                        alertDialogBuilder
                                .setMessage("This store is currently closed/offline and only pre-orders for tomorrow can be made")
                                .setCancelable(false)

                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
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

                } else if (dayweek == 2) {

                    String storeclose = close.get(0).substring(0, 2);
                    String timenow = dateNow.substring(0, 2);
                    storeHours = close.get(0).substring(0, 2);

                    int store = Integer.parseInt(storeclose) - 1;
                    int currenttime = Integer.parseInt(timenow);
                    if (currenttime >= store || nowactive == 0) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                activity);
                        // set title
                        alertDialogBuilder.setTitle("Store Closed");
                        // set dialog message
                        alertDialogBuilder
                                .setMessage("This store is currently closed/offline and only pre-orders for tomorrow can be made")
                                .setCancelable(false)

                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
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

                } else if (dayweek == 3) {

                    String storeclose = close.get(1).substring(0, 2);
                    String timenow = dateNow.substring(0, 2);
                    storeHours = close.get(1).substring(0, 2);

                    int store = Integer.parseInt(storeclose) - 1;
                    int currenttime = Integer.parseInt(timenow);


                    if (currenttime >= store || nowactive == 0) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                activity);
                        // set title
                        alertDialogBuilder.setTitle("Store Closed");
                        // set dialog message
                        alertDialogBuilder
                                .setMessage("This store is currently closed/offline and only pre-orders for tomorrow can be made")
                                .setCancelable(false)

                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
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

                } else if (dayweek == 4) {

                    String storeclose = close.get(2).substring(0, 2);
                    String timenow = dateNow.substring(0, 2);
                    storeHours = close.get(2).substring(0, 2);

                    int store = Integer.parseInt(storeclose) - 1;
                    int currenttime = Integer.parseInt(timenow);
                    if (currenttime >= store || nowactive == 0) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                activity);
                        // set title
                        alertDialogBuilder.setTitle("Store Closed");
                        // set dialog message
                        alertDialogBuilder
                                .setMessage("This store is currently closed/offline and only pre-orders for tomorrow can be made")
                                .setCancelable(false)

                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
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

                } else if (dayweek == 5) {

                    String storeclose = close.get(3).substring(0, 2);
                    String timenow = dateNow.substring(0, 2);
                    storeHours = close.get(3).substring(0, 2);

                    int store = Integer.parseInt(storeclose) - 1;
                    int currenttime = Integer.parseInt(timenow);
                    if (currenttime >= store || nowactive == 0) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                activity);
                        // set title
                        alertDialogBuilder.setTitle("Store Closed");
                        // set dialog message
                        alertDialogBuilder
                                .setMessage("This store is currently closed/offline and only pre-orders for tomorrow can be made")
                                .setCancelable(false)

                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
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

                } else if (dayweek == 6) {

                    String storeclose = close.get(4).substring(0, 2);
                    String timenow = dateNow.substring(0, 2);
                    storeHours = close.get(4).substring(0, 2);

                    int store = Integer.parseInt(storeclose) - 1;
                    int currenttime = Integer.parseInt(timenow);
                    if (currenttime >= store || nowactive == 0) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                activity);
                        // set title
                        alertDialogBuilder.setTitle("Store Closed");
                        // set dialog message
                        alertDialogBuilder
                                .setMessage("This store is currently closed/offline and only pre-orders for tomorrow can be made")
                                .setCancelable(false)

                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
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
                } else if (dayweek == 7) {

                    String storeclose = close.get(5).substring(0, 2);
                    String timenow = dateNow.substring(0, 2);
                    storeHours = close.get(5).substring(0, 2);

                    int store = Integer.parseInt(storeclose) - 1;
                    int currenttime = Integer.parseInt(timenow);
                    if (currenttime >= store || nowactive == 0) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                activity);
                        // set title
                        alertDialogBuilder.setTitle("Store Closed");
                        // set dialog message
                        alertDialogBuilder
                                .setMessage("This store is currently closed/offline and only pre-orders for tomorrow can be made")
                                .setCancelable(false)

                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
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

                SharedPreferences pref = activity.getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                String jsonString = "";


                editor.putString("StoreName", m.getName());
                editor.putString("StoreAPI", m.getApi_key());
                editor.putString("StoreHours", storeHours);
                if (m.getDelivery() == 1) {
                    editor.putString("StoreDelivery", "TRUE");
                } else {
                    editor.putString("StoreDelivery", "FALSE");
                }
                editor.commit(); // commit changes

                Toast.makeText(
                        activity,
                        "Nando's " + pref.getString("StoreName", null) + " Selected",
                        Toast.LENGTH_LONG).show();

                fragment.showFab();


            }

        });


        hours.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mainLayout.getVisibility() == View.VISIBLE) {
                            mainLayout.setVisibility(View.GONE);
                        } else {
                            int dayweek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                            if (dayweek == 1) {
                                sun.setTypeface(sun.getTypeface(), Typeface.BOLD);
                            } else if (dayweek == 2) {
                                mon.setTypeface(mon.getTypeface(), Typeface.BOLD);

                            } else if (dayweek == 3) {
                                tue.setTypeface(tue.getTypeface(), Typeface.BOLD);

                            } else if (dayweek == 4) {
                                wed.setTypeface(wed.getTypeface(), Typeface.BOLD);

                            } else if (dayweek == 5) {
                                thur.setTypeface(thur.getTypeface(), Typeface.BOLD);

                            } else if (dayweek == 6) {
                                fri.setTypeface(fri.getTypeface(), Typeface.BOLD);

                            } else if (dayweek == 7) {
                                sat.setTypeface(sat.getTypeface(), Typeface.BOLD);
                            }
                            mainLayout.setVisibility(View.VISIBLE);
                            sun.setText(day.get(6) + " " + open.get(6) + " - " + close.get(6));
                            mon.setText(day.get(0) + " " + open.get(0) + " - " + close.get(0));
                            tue.setText(day.get(1) + " " + open.get(1) + " - " + close.get(1));
                            wed.setText(day.get(2) + " " + open.get(2) + " - " + close.get(2));
                            thur.setText(day.get(3) + " " + open.get(3) + " - " + close.get(3));
                            fri.setText(day.get(4) + " " + open.get(4) + " - " + close.get(4));
                            sat.setText(day.get(5) + " " + open.get(5) + " - " + close.get(5));
                        }
                    }
                });


        callButton = (Button) convertView.findViewById(R.id.call_btn);


        callButton.setTag(position);


        String latstr = m.getLat();
        String lonstr = m.getLon();


        DirectionsButton = (Button) convertView.findViewById(R.id.direction_btn);
        DirectionsButton.setTag(position);

        DirectionsButton.setTypeface(face);
        callButton.setTypeface(face);

        callButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String num = m.getNumber();
                if (num == null || num.equalsIgnoreCase("null")) {
                    Toast.makeText(activity, "Number not available", Toast.LENGTH_SHORT).show();
                } else {
                    Intent myIntent = new Intent(Intent.ACTION_CALL);
                    String phNum = "tel:" + m.getNumber();
                    myIntent.setData(Uri.parse(phNum));
                    v.getContext().startActivity(myIntent);
                }
            }

        });


        DirectionsButton.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View w) {


                String latstr = m.getLat();
                String lonstr = m.getLon();


                if (latstr == null || lonstr == null || latstr.equalsIgnoreCase("null") || lonstr.equalsIgnoreCase("null") || latstr.equalsIgnoreCase("") || lonstr.equalsIgnoreCase("")) {
                    Toast.makeText(activity, "Location is not available", Toast.LENGTH_SHORT).show();
                } else {
                    if (isGoogleMapsInstalled()) {
                        try {

                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://maps.google.com/maps?daddr=" + latstr + "," + lonstr));
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                            w.getContext().startActivity(intent);
                        } catch (Exception e) {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("geo:0,0?q=" + latstr + "," + lonstr + ""));
                            w.getContext().startActivity(intent);
                        }
                    } else {

                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("geo:0,0?q=" + latstr + "," + lonstr + ""));
                        w.getContext().startActivity(intent);
                    }
                }


            }

        });


        // thumbnail image

        //String logo = "";


        //String logopath = m.getId();


		/*if (logopath != null && logopath.length() > 0 && !logopath.equalsIgnoreCase("null")) {
            logo = "http://www.yellowpages.co.za/timgs//" + logopath + ".gif";
		} else {
			logo = "http://m.yellowpages.co.za/Assets/images/noimage_yp.png";
		}
		*/
        thumbNail.setImageResource(R.drawable.listicon);


        bizidtxt.setText(m.getApi_key());

        dis = m.getDistance();

        System.out.println("distance ************" + dis);


        gps = new GPSTracker(activity);

        try {
            // check if GPS enabled
            if (gps.canGetLocation()) {
                if (dis != 0.0 && dis < 4000) {

                    disttxt.setText((String.format("%.1f", dis) + "km"));
                } else {
                    disttxt.setText("");
                }

            } else {
                disttxt.setText("");
            }
        } catch (Exception e) {

        }


        String msg = "<strong>" + m.getName().trim() + "</strong><br />Address: " + m.getAddress();


        address.setText(Html.fromHtml(msg));

        if (m.getNowactive() == 1) {
            active.setText("Store Online");
        } else {
            active.setText("Store Offline");
        }


        if (m.getLoadshedding() == 1) {

            cat1.setImageResource(R.drawable.loadshedding);
        }

        if (m.getDelivery() == 1) {


            cat2.setImageResource(R.drawable.delivery);
        }
        if (m.getCollect() == 1) {

            cat3.setImageResource(R.drawable.callandcollect);
        }
        if (m.getBreakfast() == 1) {

            cat4.setImageResource(R.drawable.breakfast);
        }
        if (m.getCoffee() == 1) {

            cat5.setImageResource(R.drawable.coffee);
        }
        if (m.getDrivethru() == 1) {

            cat6.setImageResource(R.drawable.drivethru);
        }
        if (m.getFullservice() == 1) {

            cat7.setImageResource(R.drawable.fullservice);
        }
        if (m.getHalaal() == 1) {

            cat8.setImageResource(R.drawable.halaal);
        }
        if (m.getKosher() == 1) {

            cat9.setImageResource(R.drawable.kosher);
        }
        if (m.getLicensed() == 1) {

            cat10.setImageResource(R.drawable.alcohol);
        }


        //phone.setText ("Number: " + m.getNumber());

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


    public boolean isGoogleMapsInstalled() {
        try {
            ApplicationInfo info = activity.getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }


    }

	/*public void sort() {
		Collections.sort(this, new Comparator<Listing>() {
			@Override
			public int compare(Listing item1, Listing item2) {
				return Double.toString(item2.getDistance()).compareTo(Double.toString(item1.getDistance()));
			}
		});
	}
	*/
}



