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

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import libs.InterfaceManager;
import libs.UniArray;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
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
				
				// set detail string
				String detail = "A unique item description should be placed here";
				
				// add the details to the intent
				detailView.putExtra("title", title);
				detailView.putExtra("price", price);
				detailView.putExtra("location", location);
				detailView.putExtra("details", detail);
				
				
				// verify that the intent is valid
				if (detailView != null)
				{
					// launch the activity
					startActivity(detailView);
				}
				
			}
		});
		
		populateList();
		
		
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
	
	public void populateList()
	{	
		// init a new copy of details
		details = new UniArray();
		
		// init the list field objects
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> listData = new HashMap<String, String>();
		
		// dummy data (temp)
		listData.put("title", "Filler Title");
		listData.put("price", "$500");
		listData.put("location", "Houston, Tx");
		
		// add the listData to the uniarray object for referencing
		details.putObject("item1", listData);
		
		data.add(listData);
		
		UIFactory.createList(list, data, R.layout.list_view, new String[]{"title", "price", "location"}, new int[]{R.id.list_title, R.id.list_price, R.id.list_location});	
	}
	
}
