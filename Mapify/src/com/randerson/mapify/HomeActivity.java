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


import com.randerson.classes.Finals;

import libs.InterfaceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class HomeActivity extends Activity {

	// credentials
	String USERNAME = null;
	String PASSWORD = null;
	
	// app globals
	InterfaceManager UIFactory;
	Intent userSelection;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// init the UIFactory
		UIFactory = new InterfaceManager(this);
		
		// set the window and orientation state
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		
		setContentView(R.layout.activity_home);
		
		// create the activity intent for receiving the passed in data
		Intent actIntent = getIntent();
		Bundle reference = null;
		
		// verify the intent is valid
		if (actIntent != null)
		{
			// get the app intent extras
			reference = actIntent.getExtras();
			
			// verify that the bundle is valid
			if (reference != null)
			{
				// verify that the bundle has the credential keys
				if (reference.containsKey("username") && reference.containsKey("password"))
				{
					// set the credential strings from the bundle data
					USERNAME = reference.getString("username");
					PASSWORD = reference.getString("password");
				}
			}
		}
		
		// create the button references from layout
		Button postBtn 			= (Button) findViewById(R.id.post_btn);
		Button autoBtn 			= (Button) findViewById(R.id.autos_btn);
		Button boatBtn 			= (Button) findViewById(R.id.boats_btn);
		Button clothesBtn 		= (Button) findViewById(R.id.clothes_btn);
		Button appliancesBtn 	= (Button) findViewById(R.id.appliances_btn);
		Button electronicsBtn 	= (Button) findViewById(R.id.electronics_btn);
		Button furnitureBtn 	= (Button) findViewById(R.id.furniture_btn);
		Button homesBtn 		= (Button) findViewById(R.id.homes_btn);
		Button jewelryBtn 		= (Button) findViewById(R.id.jewelry_btn);
		Button servicesBtn 		= (Button) findViewById(R.id.services_btn);
		
		// verify that the button is valid and set the button click listener
		if (postBtn != null)
		{
			postBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// init the intent for starting the posting activity
					userSelection = UIFactory.makeIntent(PostAdActivity.class);
					
					// set the intent credential extras
					addCredentials(userSelection, Finals.NULL);
					
					// start the activity
					startActivity(userSelection);
				}
			});
		}
		
		// verify that the button is valid and set the button click listener
		if (autoBtn != null)
		{
			autoBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// init the intent for starting the posting activity
					userSelection = UIFactory.makeIntent(ViewAdActivity.class);
					
					// set the intent credential extras
					addCredentials(userSelection, Finals.AUTOS);
					
					// start the activity
					startActivity(userSelection);
				}
			});
		}
		
		// verify that the button is valid and set the click listener
		if (boatBtn != null)
		{
			boatBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// init the intent for starting the posting activity
					userSelection = UIFactory.makeIntent(ViewAdActivity.class);
					
					// set the intent credential extras
					addCredentials(userSelection, Finals.BOATS);
					
					// start the activity
					startActivity(userSelection);
				}
			});
		}
		
		// verify that the button is valid and set the button click listener
		if (clothesBtn != null)
		{
			clothesBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// init the intent for starting the posting activity
					userSelection = UIFactory.makeIntent(ViewAdActivity.class);
					
					// set the intent credential extras
					addCredentials(userSelection, Finals.CLOTHES);
					
					// start the activity
					startActivity(userSelection);
				}
			});
		}
		
		// verify that the button is valid and set the button click listener
		if (appliancesBtn != null)
		{
			appliancesBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// init the intent for starting the posting activity
					userSelection = UIFactory.makeIntent(ViewAdActivity.class);
					
					// set the intent credential extras
					addCredentials(userSelection, Finals.APPLIANCES);
					
					// start the activity
					startActivity(userSelection);
				}
			});
		}
		
		// verify that the button is valid and set the button click listener
		if (electronicsBtn != null)
		{
			electronicsBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// init the intent for starting the posting activity
					userSelection = UIFactory.makeIntent(ViewAdActivity.class);
					
					// set the intent credential extras
					addCredentials(userSelection, Finals.ELECTRONICS);
					
					// start the activity
					startActivity(userSelection);
				}
			});
		}
		
		// verify that the button is valid and set the button click listener
		if (furnitureBtn != null)
		{
			furnitureBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// init the intent for starting the posting activity
					userSelection = UIFactory.makeIntent(ViewAdActivity.class);
					
					// set the intent credential extras
					addCredentials(userSelection, Finals.FURNITURE);
					
					// start the activity
					startActivity(userSelection);
				}
			});
		}
		
		// verify that the button is valid and set the button click listener
		if (homesBtn != null)
		{
			homesBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// init the intent for starting the posting activity
					userSelection = UIFactory.makeIntent(ViewAdActivity.class);
					
					// set the intent credential extras
					addCredentials(userSelection, Finals.HOMES);
					
					// start the activity
					startActivity(userSelection);
				}
			});
		}
		
		// verify that the button is valid and set the button click listener
		if (jewelryBtn != null)
		{
			jewelryBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// init the intent for starting the posting activity
					userSelection = UIFactory.makeIntent(ViewAdActivity.class);
					
					// set the intent credential extras
					addCredentials(userSelection, Finals.JEWELRY);
					
					// start the activity
					startActivity(userSelection);
				}
			});
		}
		
		// verify that the button is valid and set the button click listener
		if (servicesBtn != null)
		{
			servicesBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// init the intent for starting the posting activity
					userSelection = UIFactory.makeIntent(ViewAdActivity.class);
					
					// set the intent credential extras
					addCredentials(userSelection, Finals.SERVICES);
					
					// start the activity
					startActivity(userSelection);
				}
			});
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	// method for adding the credential data to activity intents
	public void addCredentials(Intent intent, int category)
	{
		// verify that the referenced intent is valid
		if (intent != null)
		{
			// put the data into the intent
			intent.putExtra("username", USERNAME);
			intent.putExtra("password", PASSWORD);
			intent.putExtra("category", category);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (item.getTitle().equals(getString(R.string.action_settings)))
		{
			// create the activity intent for the settings
			Intent launchSettings = UIFactory.makeIntent(SettingsActivity.class);
			
			// verify that the settings intent is valid
			if (launchSettings != null)
			{
				// start the settings activity
				startActivityForResult(launchSettings, 0);
			}
		}
		else if (item.getTitle().equals(getString(R.string.quit)))
		{
			// end this activity and return to sign on screen
			super.finish();
		}
		
		return super.onOptionsItemSelected(item);
	}

	
	
}
