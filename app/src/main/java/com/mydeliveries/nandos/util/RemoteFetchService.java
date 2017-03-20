package com.mydeliveries.nandos.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.mydeliveries.nandos.app.AppController;
import com.mydeliveries.nandos.model.Orders;

public class RemoteFetchService extends Service {

	private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	String userEmail;
	String url = "";


	public static ArrayList<Orders> listItemList;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	/*
	 * Retrieve appwidget id from intent it is needed to update widget later
	 * initialize our AQuery class
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID))
			appWidgetId = intent.getIntExtra(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);

		fetchDataFromWeb();
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * method which fetches data(json) from web aquery takes params
	 * remoteJsonUrl = from where data to be fetched String.class = return
	 * format of data once fetched i.e. in which format the fetched data be
	 * returned AjaxCallback = class to notify with data once it is fetched
	 */
	private void fetchDataFromWeb() {

		System.out.println("fetch data from web api %%$%$%$%$%$%$%$%$%$%$%$%$%");
		new getOrder().execute();

	}





	/**
	 * Method which sends broadcast to WidgetProvider
	 * so that widget is notified to do necessary action
	 * and here action == WidgetProvider.DATA_FETCHED
	 */
	private void populateWidget() {

		Intent widgetUpdateIntent = new Intent();
		widgetUpdateIntent.setAction(WidgetProvider.DATA_FETCHED);
		widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
				appWidgetId);
		sendBroadcast(widgetUpdateIntent);

		this.stopSelf();
	}

	class getOrder extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {



			SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
			SharedPreferences.Editor editor = pref.edit();
			String User ="";
			if( pref.getString("User", null) != null &&(!pref.getString("User", null).equalsIgnoreCase("")) ) {
				User   = pref.getString("User", null);
				try {
					JSONObject jsonObjorder = new JSONObject(User);


					userEmail = jsonObjorder.getString("email");


				} catch (JSONException e) {
					e.printStackTrace();
				}


			}else{


				userEmail = "false";


			}
			super.onPreExecute();

		}

		/**
		 * getting Places JSON
		 * */
		protected String doInBackground(String... args) {
			// creating Places class object

			url = "http://www.mydeliveries.co.za/ordermanager/nandos/userorders/" + userEmail;

			// Creating volley request obj
			JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
					new Response.Listener<JSONObject>() {
						public void onResponse(JSONObject response) {


							try {

								JSONArray results = (JSONArray) response.get("orders");

								System.out.println("results widget*************"+ results);

								// Parsing json
								for (int i = 0; i < results.length(); i++) {
									JSONObject obj = results.getJSONObject(i);
									Orders order = new Orders();
									order.setId(obj.getString("id"));
									order.setOrder(obj.getString("order"));
									order.setStatus(obj.getString("status"));
									order.setName(obj.getString("name"));
									order.setSurname(obj.getString("surname"));
									order.setNumber(obj.getString("number"));
									order.setEmail(obj.getString("email"));
									order.setAddress(obj.getString("address"));
									order.setCollection(obj.getString("collection"));
									order.setDelivery(obj.getString("delivery"));
									order.setPreorder(obj.getString("preorder"));
									order.setOrderdate(obj.getString("orderdate"));
									order.setCapture(obj.getString("capture"));
									order.setReference(obj.getString("reference"));
									order.setStoredelivery(obj.getString("storedelivery"));
									order.setStorecollect(obj.getString("storecollect"));
									order.setStorehours(obj.getString("storehours"));
									order.setNowactive(obj.getString("nowactive"));
									order.setStorename(obj.getString("storename"));
									order.setStoreapi(obj.getString("storeapi"));



									listItemList.add(order);


								}



							} catch (JSONException e) {
								e.printStackTrace();
							} catch (Exception e) {
								e.printStackTrace();
							}





						}
					}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {

					try {
						Log.d("error results", error.toString());



					} catch (Exception e) {

					}

				}
			}) {

				/**
				 * Passing some request headers
				 * */
				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					HashMap<String, String> headers = new HashMap<String, String>();
					headers.put("Content-Type", "application/json");
					headers.put("Authorization", "057f774e10cd69ce1ad7ea2e3ac708d5");
					return headers;
				}

			};

			int socketTimeout = 15000;//15 seconds - change to what you want
			RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
			getRequest.setRetryPolicy(policy);

			// Adding request to request queue
			AppController.getInstance().addToRequestQueue(getRequest);
			return null;
		}


		protected void onPostExecute(String file_url) {
			populateWidget();
			super.onPostExecute(file_url);
		}


	}

}
