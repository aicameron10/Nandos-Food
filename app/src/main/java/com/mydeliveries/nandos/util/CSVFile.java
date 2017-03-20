package com.mydeliveries.nandos.util;

/**
 * Created by Andrew on 04/07/2015.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

public class CSVFile {

    Context context;

    String file_name;

    ArrayList<HashMap<String, String>> CSVData;

    public CSVFile(Context context, String file_name) {
        this.context = context;

        this.file_name = file_name;

    }

    public ArrayList<HashMap<String, String>> ReadCSV() throws IOException {

        URL url = new URL("http://www.mydeliveries.co.za/ordermanager/nandos/menu.csv");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");


        InputStreamReader isr = new InputStreamReader( conn.getInputStream());

        BufferedReader br = new BufferedReader(isr);

        String line;

        String cvsSplitBy = ",";

        br.readLine();

        CSVData = new ArrayList<HashMap<String, String>>();

        while ((line = br.readLine()) != null) {

            String[] row = line.split(cvsSplitBy);

            HashMap<String, String> hm = new HashMap<String, String>();

            for (int i = 0; i < row.length; i++) {

                hm.put("\"row(" + i + ")\"", "\"" + row[i] + "\"");

            }
            CSVData.add(hm);
        }
        return CSVData;

    }

}