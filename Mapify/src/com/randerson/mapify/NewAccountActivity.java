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
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class NewAccountActivity extends Activity {

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
		
		setContentView(R.layout.activity_new_account);
		
		// create the button reference
		Button createAcct = (Button) findViewById(R.id.createAccount);
		
		// verify that the createAcct button is valid
		if (createAcct != null)
		{
			// set the button click listener
			createAcct.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// create a success toast (this will be of a success or fail in future)
					Toast status = UIFactory.createToast("Account Created", false);
					
					// verify that the toast is valid
					if (status != null)
					{
						// show the toast
						status.show();
					}
					
					// call the finish method to end the activity
					finish();
				}
			});
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
			Intent home = UIFactory.makeIntent(SignInActivity.class);
			
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
