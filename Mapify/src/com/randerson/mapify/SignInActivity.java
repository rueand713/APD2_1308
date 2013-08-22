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
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class SignInActivity extends Activity {

	InterfaceManager UIFactory;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// init UIFactory
		UIFactory = new InterfaceManager(this);
		
		// set the window and orientation state
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		
		setContentView(R.layout.activity_signin);
		
		// create the newAcct button reference
		Button newAcct = (Button) findViewById(R.id.newuser_button);
		
		// verify the newAcct button is valid
		if (newAcct != null)
		{
			// set the button click listener
			newAcct.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// create the new account intent
					Intent newAccountScreen = UIFactory.makeIntent(NewAccountActivity.class);
					
					// verify the intent is valid
					if (newAccountScreen != null)
					{
						// start the account creation activity to return a result
						startActivityForResult(newAccountScreen, 0);
					}
				}
			});
			
			// create the signIn button reference
			Button signOn = (Button) findViewById(R.id.signin_button);
			
			// verify that the signOn button is valid
			if (signOn != null)
			{
				// set the button click listener
				signOn.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						// create the home screen intent
						Intent homeScreen = UIFactory.makeIntent(HomeActivity.class);
						
						if (homeScreen != null)
						{
							// start the home screen activity
							startActivity(homeScreen);
						}
					}
				});
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.no_opts, menu);
		return true;
	}

}
