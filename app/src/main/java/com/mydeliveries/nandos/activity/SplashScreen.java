package com.mydeliveries.nandos.activity;

/**
 * Created by Andrew on 09/07/2015.
 */

import com.mydeliveries.nandos.R;
import com.mydeliveries.nandos.util.CSVFile;
import com.mydeliveries.nandos.util.CSVFileDrinks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new getMenu().execute();


    }


    class getMenu extends AsyncTask<Void, Void, Void> {
        ArrayList<HashMap<String, String>> csvdata;
        ArrayList<HashMap<String, String>> csvdatadrinks;

        @Override
        protected Void doInBackground(Void... params) {
            CSVFile csvreader = new CSVFile(getBaseContext(),
                    "menu.csv");
            CSVFileDrinks csvreaderdrinks = new CSVFileDrinks(getBaseContext(),
                    "drinks.csv");

            try {
                csvdata = csvreader.ReadCSV();
                csvdatadrinks = csvreaderdrinks.ReadCSV();
                //  System.out.println("*********************" + csvdata);
                // System.out.println("*********************" + csvdatadrinks);

                SharedPreferences pref = getBaseContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();

                try {

                    editor.putString("Menu", csvdata.toString());
                    editor.putString("Drinks", csvdatadrinks.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }


                editor.commit(); // commit changes

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Intent i = new Intent(SplashScreen.this, HomeActivity.class);
            startActivity(i);

            // close this activity
            finish();

        }

    }


}
