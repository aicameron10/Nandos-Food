package com.mydeliveries.nandos.activity;


import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mydeliveries.nandos.R;
import com.mydeliveries.nandos.adapter.MyAdapter;
import com.mydeliveries.nandos.app.AppController;
import com.mydeliveries.nandos.fragments.AddressBookFragment;
import com.mydeliveries.nandos.fragments.AddressFragment;
import com.mydeliveries.nandos.fragments.CheckOutFragment;
import com.mydeliveries.nandos.fragments.FeedbackFragment;
import com.mydeliveries.nandos.fragments.HomeFragment;
import com.mydeliveries.nandos.fragments.InviteFragment;
import com.mydeliveries.nandos.fragments.ListViewFragment;
import com.mydeliveries.nandos.fragments.NotificationFragment;
import com.mydeliveries.nandos.fragments.OrderFragment;
import com.mydeliveries.nandos.fragments.ProfileFragment;
import com.mydeliveries.nandos.fragments.RewardsFragment;
import com.mydeliveries.nandos.fragments.SocialFragment;
import com.mydeliveries.nandos.fragments.TermsWebViewFragment;
import com.mydeliveries.nandos.fragments.UserDetailsFragment;
import com.mydeliveries.nandos.fragments.WebViewFragment;
import com.mydeliveries.nandos.model.NavItem;
import com.mydeliveries.nandos.util.ApplicationConstants;
import com.mydeliveries.nandos.util.GPSTracker;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.app.AlertDialog;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HomeActivity extends ActionBarActivity {
    TextView msgET, usertitleET;
    ImageButton FAB;
    private static long back_pressed;


    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    GoogleCloudMessaging gcmObj;
    Context applicationContext;
    String regId = "";

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    // Declaring Your View and Variables
    Toolbar toolbar;

    // GPSTracker class
    GPSTracker gps;


    String TITLES[] = {"", "", "", "", "", "", "", "", "", ""};
    int ICONS[] = {R.drawable.menumenu, R.drawable.restaurantmenu, R.drawable.checkoutordermenu, R.drawable.myorderhistorymenu, R.drawable.myprofilemenu, R.drawable.notificationmenu, R.drawable.invitemenu, R.drawable.socialmedia, R.drawable.rewardsmenu, R.drawable.feedbackmenu};

    //Similarly we Create a String Resource for the name and email in the header view
    //And we also create a int resource for profile picture in the header view

    String NAME = "";
    String EMAIL = "";
    int PROFILE = R.drawable.success;

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout

    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        applicationContext = getApplicationContext();

        SharedPreferences pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        String User = "";
        if (pref.getString("User", null) != null && (!pref.getString("User", null).equalsIgnoreCase(""))) {
            User = pref.getString("User", null);
            try {
                JSONObject jsonObjorder = new JSONObject(User);

                NAME = "Welcome " + jsonObjorder.getString("name") + " " + jsonObjorder.getString("surname");
                EMAIL = "Discover Our Menu!";


            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {

            NAME = "Welcome User";
            EMAIL = "Discover Our Menu!";


        }


        editor.remove("Order");
        editor.remove("TotalOrderPrice");

        editor.commit(); // commit changes

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/webfont.ttf");

        final TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setTypeface(face, Typeface.BOLD);
        String msg = "<strong>Nando&rsquo;s</strong>";
        title.setText(Html.fromHtml(msg));

        new UpdateApp().execute();
        new ServerCheck().execute();


        loadDraws();


        displayView(0);


        if (pref.getString("NewUser", null) == null || (pref.getString("NewUser", null).equalsIgnoreCase(""))) {
            editor.putString("NewUser", "TRUE");
            editor.commit(); // commit changes
        }

        String newuser = pref.getString("NewUser", null);

        if (newuser.equalsIgnoreCase("TRUE")) {

            registerInBackground();
        }


        // Intent Message sent from Broadcast Receiver
        String str = getIntent().getStringExtra("msg");


        if (!checkPlayServices()) {
            Toast.makeText(
                    getApplicationContext(),
                    "This device doesn't support Play services, App will not work normally",
                    Toast.LENGTH_LONG).show();
        }

        //usertitleET.setText("Hello " + eMailId + " !");
        // When Message sent from Broadcase Receiver is not empty
        if (str != null) {
            //System.out.println("***********************"+str.toString());
            String image = "";
            String titlemsg = "";
            String body = "";
            try {
                JSONObject jsonObj = new JSONObject(str);

                image = jsonObj.getString("image");
                titlemsg = jsonObj.getString("title");
                body = jsonObj.getString("body");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View formElementsView = inflater.inflate(R.layout.notification,
                    null, false);

            TextView txt = (TextView) formElementsView.findViewById(R.id.title);
            TextView txtbody = (TextView) formElementsView.findViewById(R.id.body);
            txt.setTypeface(face);
            if (imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();
            NetworkImageView thumbNail = (NetworkImageView) formElementsView
                    .findViewById(R.id.image);
            txt.setText(titlemsg);
            txtbody.setText(body);
            thumbNail.setImageUrl(image, imageLoader);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    this).setView(formElementsView);
            LayoutInflater inflater1 = this.getLayoutInflater();
            View view = inflater1.inflate(R.layout.tool_bar_order, null);

            TextView header = (TextView) view
                    .findViewById(R.id.toolbartitle);
            header.setTypeface(face);
            header.setText("New Message");
            alertDialogBuilder.setCustomTitle(view);

            // set dialog message
            alertDialogBuilder
                    .setCancelable(true)
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


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        "This device doesn't support Play services, App will not work normally",
                        Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        } else {
            //Toast.makeText(
            //	getApplicationContext(),
            //	"This device supports Play services, App will work normally",
            //	Toast.LENGTH_LONG).show();
        }
        return true;
    }


    @Override
    public void onBackPressed() {

        NavItem nav = new NavItem();
        String navi = nav.getPage();

        switch (navi) {
            case "HomePage":
                if (back_pressed + 2000 > System.currentTimeMillis()) {
                    super.onBackPressed();
                    SharedPreferences pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();

                    editor.remove("locationfound");
                    editor.remove("userlocation");
                    editor.remove("locationStreet");
                    editor.remove("lat");
                    editor.remove("lon");
                    editor.remove("StoreAPI");
                    editor.remove("TempAddress");
                    editor.remove("StoreAPI");
                    editor.remove("StoreName");
                    editor.remove("StoreDelivery");
                    editor.remove("Order");


                    editor.commit();

                } else {

                    Toast toast = Toast.makeText(getBaseContext(), "Press back once again to exit Nando's App!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    back_pressed = System.currentTimeMillis();
                }
                return;
            case "Home":
                displayView(0);
                return;
            case "Profile":
                displayView(4);
                return;
            case "AddressBook":
                displayView(8);
                return;

            case "Order":
                displayView(2);
                return;


        }


    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcmObj == null) {
                        gcmObj = GoogleCloudMessaging
                                .getInstance(applicationContext);
                    }
                    regId = gcmObj
                            .register(ApplicationConstants.GOOGLE_PROJ_ID);
                    msg = "Registration ID :" + regId;

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(regId)) {
                    SharedPreferences pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();

                    editor.putString("GCM", regId);
                    editor.putString("NewUser", "FALSE");

                    editor.commit(); // commit changes

                } else {
                    //Toast.makeText(
                    //	applicationContext,
                    //	"Reg ID Creation Failed.\n\nEither you haven't enabled Internet or GCM server is busy right now. Make sure you enabled Internet and try registering again after some time."
                    //		+ msg, Toast.LENGTH_LONG).show();
                }
            }
        }.execute(null, null, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        NavItem nav = new NavItem();
        // Handle action bar actions click
        switch (item.getItemId()) {
            case android.R.id.home:
                System.out.println("back pressed");
                String navi = nav.getPage();
                switch (navi) {
                    case "Home":
                        displayView(0);
                        return true;

                    case "Profile":
                        displayView(4);
                        return true;

                    case "AddressBook":
                        displayView(8);
                        return true;

                    case "Order":
                        displayView(2);
                        return true;


                }

                return true;
            case R.id.action_locate:
                displayView(1);
                return true;
            case R.id.action_check:
                // location found
                SharedPreferences pref = getSharedPreferences("MyPref", 0); // 0 - for private mode
                if (pref.getString("Order", null) != null) { // getting String
                    displayView(2);
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Please add order to cart",
                            Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.action_menu:
                // help action
                displayView(0);
                return true;

            case R.id.action_order_history:
                // help action
                displayView(3);
                return true;

            case R.id.action_my_profile:
                // help action
                displayView(4);
                return true;
            case R.id.action_notifications:
                // help action
                displayView(11);
                return true;
            case R.id.action_friends:
                // help action
                displayView(12);
                return true;
            case R.id.action_social:
                // help action
                displayView(13);
                return true;

            case R.id.action_feedback:
                // help action
                displayView(5);
                return true;

            case R.id.action_terms:
                // help action
                displayView(10);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }


    }

    public void loadDrawsDisplay() {


        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerToggle.syncState();
    }

    public void loadDrawsHide() {


        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    public void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:

                fragment = new ListViewFragment();

                break;
            case 2:
                fragment = new CheckOutFragment();
                break;
            case 3:
                fragment = new OrderFragment();

                break;
            case 4:
                fragment = new ProfileFragment();

                break;

            case 5:
                fragment = new FeedbackFragment();

                break;

            case 6:
                fragment = new AddressFragment();

                break;

            case 7:
                fragment = new UserDetailsFragment();

                break;
            case 8:
                fragment = new AddressBookFragment();

                break;

            case 9:
                fragment = new WebViewFragment();

                break;
            case 10:
                fragment = new TermsWebViewFragment();

                break;
            case 11:
                fragment = new NotificationFragment();

                break;

            case 12:
                fragment = new InviteFragment();

                break;

            case 13:
                fragment = new SocialFragment();

                break;
            case 14:
                fragment = new RewardsFragment();

                break;


            default:
                break;
        }

        if (fragment != null) {

            try {
                //InputMethodManager input = (InputMethodManager) this
                //	.getSystemService(Activity.INPUT_METHOD_SERVICE);
                //input.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();

            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


            fragmentTransaction.replace(R.id.frame_container, fragment);
            fragmentTransaction.commit();


        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }


    class UpdateApp extends AsyncTask<String, String, String> {

        String str = "";

        StringBuilder text = new StringBuilder();
        boolean update = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        /**
         * getting Places JSON
         */
        protected String doInBackground(String... args) {
            // creating Places class object


            try {

                URL url = new URL("http://www.mydeliveries.co.za/apk.txt");

                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

                while ((str = in.readLine()) != null) {
                    text.append(str);
                }
                in.close();
            } catch (MalformedURLException e1) {
            } catch (IOException e) {
            } catch (Exception e) {

            }

            PackageManager manager = HomeActivity.this.getPackageManager();
            PackageInfo info;
            try {
                info = manager.getPackageInfo(HomeActivity.this.getPackageName(), 0);
                double version = Double.parseDouble(info.versionName);
                System.out.println("version - " + version);
                double appVersion = Double.parseDouble(text.toString());
                System.out.println("appVersion - " + appVersion);
                if (version == appVersion) {
                    update = false;
                } else {
                    update = true;
                }

            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {

            }


            //System.out.println("my mesage" + text);
            return null;
        }


        protected void onPostExecute(String file_url) {

            if (update == true) {


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        HomeActivity.this);

                // set title
                alertDialogBuilder.setTitle("Update Required");


                // set dialog message
                alertDialogBuilder
                        .setMessage("A new version of this app is available to download!")
                        .setCancelable(false)
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent goToMarket = new Intent(Intent.ACTION_VIEW)
                                        .setData(Uri.parse("market://details?id=com.mydeliveries.nandos"));
                                startActivity(goToMarket);

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

            super.onPostExecute(file_url);
        }


    }


    public void loadDraws() {


        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        mAdapter = new MyAdapter(TITLES, ICONS, NAME, EMAIL, PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView

        final GestureDetector mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());


                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {

                    if (recyclerView.getChildPosition(child) == 1) {
                        displayView(0);
                    } else if (recyclerView.getChildPosition(child) == 2) {
                        displayView(1);
                    } else if (recyclerView.getChildPosition(child) == 3) {
                        SharedPreferences pref = getSharedPreferences("MyPref", 0); // 0 - for private mode
                        if (pref.getString("Order", null) != null) { // getting String
                            displayView(2);
                        } else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Please Add Order to Cart",
                                    Toast.LENGTH_LONG).show();
                        }

                    } else if (recyclerView.getChildPosition(child) == 4) {
                        displayView(3);
                    } else if (recyclerView.getChildPosition(child) == 5) {
                        displayView(4);
                    } else if (recyclerView.getChildPosition(child) == 6) {
                        displayView(11);
                    } else if (recyclerView.getChildPosition(child) == 7) {
                        displayView(12);
                    } else if (recyclerView.getChildPosition(child) == 8) {
                        displayView(13);
                    } else if (recyclerView.getChildPosition(child) == 9) {
                        displayView(14);

                    } else if (recyclerView.getChildPosition(child) == 10) {
                        displayView(5);
                    }
                    //Toast.makeText(HomeActivity.this,"The Item Clicked is: "+recyclerView.getChildPosition(child),Toast.LENGTH_SHORT).show();
                    Drawer.closeDrawers();
                    return true;

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager


        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }


        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State


        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    class ServerCheck extends AsyncTask<String, String, String> {


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
            URL url = null;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL("http://www.mydeliveries.co.za/ordermanager/nandos/stores/Mobile");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }


            urlConnection.setRequestProperty("Authorization-Token", "057f774e10cd69ce1ad7ea2e3ac708d5");


            try {
                res = urlConnection.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println("***********action" + res + "**************");

            return null;
        }


        protected void onPostExecute(String file_url) {

            if (res == 200) {


            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        HomeActivity.this);

                // set title
                alertDialogBuilder.setTitle("Sorry!");


                // set dialog message
                alertDialogBuilder
                        .setMessage("There is an issue with our server, No orders will be accepted , Please check back shortly we are working on the issue to bring you back your favourite chicken!")
                        .setCancelable(false)

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

            super.onPostExecute(file_url);
        }


    }
}
