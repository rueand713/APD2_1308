/*
 * project 		Mapify
 * 
 * package 		com.randerson.mapify
 * 
 * @author 		Rueben Anderson
 * 
 * date			Aug 22, 2013
 * 
 */
package com.randerson.mapify;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.randerson.classes.DetailService;
import com.randerson.mapclass.MapService;

import libs.InterfaceManager;
import libs.JSONhandler;
import libs.UniArray;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ViewAdActivity extends Activity {

// google geo encoding base url
final String BASE_GEO_CODE_URL = "http://maps.googleapis.com/maps/api/geocode/json?address=";

// globals
InterfaceManager UIFactory;
GoogleMap map;
ListView list;
UniArray details;
String category;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// init the UIFactory
		UIFactory = new InterfaceManager(this);
		
		// set the window and orientation state
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		
		// allow the icon on the actionbar to act as navigation
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		setContentView(R.layout.activity_view_ad);
		
		// get the passed in intent data
		Intent intent = getIntent();
		
		// verify that the intent is valid
		if (intent != null)
		{
			// create the bundle of intent extras
			Bundle bundle = intent.getExtras();
			
			// verify that the bundle is valid
			if (bundle != null && bundle.containsKey("category"))
			{
				// retrieve the category from the button clicked on the home screen
				category = bundle.getString("category");
			}
		}
		
		final Handler getHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
				if (msg.arg1 == RESULT_OK)
				{
					// set the result string
					String result = (String) msg.obj;
					
					Log.i("Result", result);
					
					// call the method to populate the map and list view
					populateList(new JSONhandler(result).getJSONArray(category).returnJSONArray());
				}
			}
		};
		
		// get the fragment from the layout file
		MapFragment mf = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		
		// get the google map from the map fragment
		map = mf.getMap();
		
		// use the user location
		map.setMyLocationEnabled(true);
		
		// init a listview referencing the list from the layout
		list = (ListView) findViewById(R.id.list_data);
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				Intent detailView = UIFactory.makeIntent(AdDetailsActivity.class);
				
				@SuppressWarnings("unchecked")
				HashMap<String, String> item = (HashMap<String, String>) details.getObject(arg2);
				
				// set the item properties to strings
				String title = item.get("title");
				String price = item.get("price");
				String location = item.get("location");
				String detail = item.get("details");
				String seller = item.get("poster");
				
				// add the details to the intent
				detailView.putExtra("title", title);
				detailView.putExtra("price", "$" + price);
				detailView.putExtra("location", location);
				detailView.putExtra("details", detail);
				detailView.putExtra("poster", seller);
				
				
				// verify that the intent is valid
				if (detailView != null)
				{
					// launch the activity
					startActivity(detailView);
				}
				
			}
		});
		
		Messenger msngr = new Messenger(getHandler);
		
		// create the get request intent
		Intent makeRequest = UIFactory.makeIntent(DetailService.class);
		
		// verify that the intent is valid
		if (makeRequest != null)
		{
			// add the messenger and url to the intent
			makeRequest.putExtra(DetailService.MESSENGER_KEY, msngr);
			makeRequest.putExtra(DetailService.URL_KEY, getString(R.string.account_ad_posting));
			
			startService(makeRequest);
		}
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.no_opts, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		return super.onOptionsItemSelected(item);
	}
	
	public void populateList(JSONArray json)
	{	
		// set the number of ads in this array
		int numberOfAds = json.length();
		
		// init a the details object
		details = new UniArray();
		
		// init the list field objects
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		
		// iterate over the json data for each ad in the category
		for (int i = 0; i < numberOfAds; i++)
		{
			// create a json object for the current ad
			JSONObject currentAd;
			
			try {
				
				// create a local hashmap to hold the ad data
				HashMap<String, String> listData = new HashMap<String, String>();
				
				// get the JSON object at the current index
				currentAd = json.getJSONObject(i);
				
				// get the strings from the json object
				String title = currentAd.getString("title");
				String price = currentAd.getString("price");
				String description = currentAd.getString("details");
				String poster = currentAd.getString("poster");
				String city = currentAd.getString("city");
				String state = currentAd.getString("state");
				String country = currentAd.getString("country");
				String contactPhone = currentAd.getString("contact-phone");
				String contactEmail = currentAd.getString("contact-email");
				String latlon = currentAd.getString("latlon");
				
				// add the ad details to the session hashmap
				listData.put("title", title);
				listData.put("price", price);
				listData.put("details", description);
				listData.put("poster", poster);
				listData.put("location", city + ", " + state);
				listData.put("latlon", latlon);
				listData.put("city", city);
				listData.put("state", state);
				listData.put("country", country);
				listData.put("contact-phone", contactPhone);
				listData.put("contact-email", contactEmail);
				
				// add the listData to the uniarray object for referencing
				details.putObject("item" + (i + 1), listData);
				
				// add the listData to the listArray for the listView
				data.add(listData);
				
			} catch (JSONException e) {
				e.printStackTrace();
				Log.e("JSONException", "Retrieving the JSON object at the current index");
			}
		}
		
		// populate the list view
		UIFactory.createList(list, data, R.layout.list_view, new String[]{"title", "price", "location"}, new int[]{R.id.list_title, R.id.list_price, R.id.list_location});
		
		// add the map markers
		addMarker();
	}
	
	public void addMarker()
	{
		for (int i = 0; i < details.objectsLength(); i++)
		{
			// retrieve the inner hashmap from the uniarray
			@SuppressWarnings("unchecked")
			HashMap<String, String> listData = (HashMap<String, String>) details.getObject(i);
			
			// set the strings for the map data
			String title = listData.get("title");
			String snippet = listData.get("details");
			
			// get the lat lon string data
			String latlon = listData.get("latlon");
			String lat_ = latlon.split(":")[0];
			String lon_ = latlon.split(":")[1];
			
			// parse the coord strings into doubles
			double lat = Double.parseDouble(lat_);
			double lon = Double.parseDouble(lon_);
			
			// create a blip on the map for the current ad
			MapService.addMarker(map, title, snippet, lat, lon);
		}
	}
	
}
