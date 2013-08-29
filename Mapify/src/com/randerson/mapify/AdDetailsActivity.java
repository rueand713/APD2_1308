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

import libs.InterfaceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

public class AdDetailsActivity extends Activity {

InterfaceManager UIFactory;
	
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
		
		setContentView(R.layout.activity_ad_details);
		
		Intent dataDetails = getIntent();
		
		if (dataDetails != null)
		{
			Bundle bundle = dataDetails.getExtras();
			
			if (bundle != null)
			{
				// set bundle properties to strings
				String title = bundle.getString("title");
				String price = bundle.getString("price");
				String location = bundle.getString("location");
				String details = bundle.getString("details");
				String seller = bundle.getString("poster");
				
				// set the textView references from layout
				TextView titleView = (TextView) findViewById(R.id.title_field);
				TextView priceView = (TextView) findViewById(R.id.price_field);
				TextView locationView = (TextView) findViewById(R.id.location_field);
				TextView detailView = (TextView) findViewById(R.id.details_field);
				TextView sellerView = (TextView) findViewById(R.id.seller_field);
				
				// set the textview text
				titleView.setText(title);
				priceView.setText(price);
				locationView.setText(location);
				detailView.setText(details);
				sellerView.setText(seller);
			}
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
		/*
		if (item.getItemId() == android.R.id.home)
		{
			Intent home = UIFactory.makeIntent(ViewAdActivity.class);
			
			// verify that the home intent has been set properly
			if (home != null)
			{
				// set the clear top flag to prevent duplicate activities
				home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				
				startActivity(home);
			}
		}
		*/
		return super.onOptionsItemSelected(item);
	}
	
}
