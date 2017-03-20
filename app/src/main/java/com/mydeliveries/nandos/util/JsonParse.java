package com.mydeliveries.nandos.util;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mydeliveries.nandos.model.SuggestNandos;


public class JsonParse {
	
	
     public JsonParse(){}
   
     public List<SuggestNandos> getParseJsonWCF(String sName)
     {
      List<SuggestNandos> ListData = new ArrayList<SuggestNandos>();
      try {
    	  
    	  String baseURL = "http://www.mydeliveries.co.za/ordermanager/nandos/stores/";

          String searchURL = baseURL + sName.trim().replaceAll(" ", "%20");
          
          URL url = new URL(searchURL);

         URLConnection urlConnection = url.openConnection();
         urlConnection.setRequestProperty("Authorization-Token", "057f774e10cd69ce1ad7ea2e3ac708d5");
         urlConnection.setDoInput(true);
         urlConnection.setDoOutput(false);
         
         InputStream is = urlConnection.getInputStream();
         InputStreamReader isr = new InputStreamReader(is);

         int numCharsRead;
         char[] charArray = new char[1024];
         StringBuffer sb = new StringBuffer();
         while ((numCharsRead = isr.read(charArray)) > 0) {
             sb.append(charArray, 0, numCharsRead);
         }
         String json = sb.toString();

         JSONArray jsonResponse = new JSONArray(json);
         for(int i = 0; i < 5; i++){
             JSONObject r = jsonResponse.getJSONObject(i);
             ListData.add(new SuggestNandos(r.getString("w").toLowerCase()));
         }
     } catch (Exception e1) {
         // TODO Auto-generated catch block
         e1.printStackTrace();
     }
      return ListData;

     }

}