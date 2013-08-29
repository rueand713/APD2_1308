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

import org.json.JSONException;
import org.json.JSONObject;

import com.randerson.classes.DetailService;

import libs.ApplicationDefaults;
import libs.FileSystem;
import libs.InterfaceManager;
import libs.JSONhandler;
import libs.UniArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class SignInActivity extends Activity {

	InterfaceManager UIFactory;
	String USERNAME = null;
	String PASSWORD = null;
	String revision;
	
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
		
		final Handler accountHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
				if (msg.arg1 == RESULT_OK && msg.obj != null)
				{
					// set the string to the response text
					String result = (String) msg.obj;
					
					// init a JSONhandler singleton for parsing the data
					JSONhandler jsOn = new JSONhandler(result);
					
					// cache the user data
					saveSessionData(jsOn.returnJSONobject());

					// set the password from the json data
					String userPassword = jsOn.getValue("acct_password");
					
					// verify that the password supplied matches the password in the json
					if (userPassword != null && userPassword.equals(PASSWORD))
					{
						
						// inform the user of invalid credentials
						UIFactory.displayToast("Welcome back, " + USERNAME + "!", false);
						
						// create the home screen intent
						Intent homeScreen = UIFactory.makeIntent(HomeActivity.class);
						
						if (homeScreen != null)
						{
							// start the home screen activity
							startActivity(homeScreen);
						}
					}
					else
					{
						// inform the user of invalid credentials (username)
						UIFactory.displayToast("Incorrect Username or Password", false);
					}
				}
			}
		};
		
		// create the service callback handler
		final Handler requestHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
				// verify that the data was returned fine
				if (msg.arg1 == RESULT_OK && msg.obj != null)
				{
					// set the string to the response text
					String m = (String) msg.obj;
					
					// init a JSONhandler singleton for parsing the data
					JSONhandler jsOn = new JSONhandler(m);

					// set the value string to the parsed json
					JSONObject accounts = jsOn.getJSONArray("rows").getObjectAtIndex(0).returnJSONobject();
					revision = jsOn.getJSONObject("value").getValue("rev");
					boolean userExists = accounts.has("key");
					
					// verify that the user account is valid
					if (userExists)
					{
						
						Messenger accountMsngr = new Messenger(accountHandler);
						
						// create the intent to gather the detailed account data
						Intent accountRequest = UIFactory.makeIntent(DetailService.class);
						
						// retrieve the mapify cloudant query url
						String urlValue = getString(R.string.accounts_uri) + USERNAME;
						
						// add the url and messenger objects to the intent
						accountRequest.putExtra("Url", urlValue);
						accountRequest.putExtra("Messenger", accountMsngr);
						
						// begin the service
						startService(accountRequest);
					}
					else
					{
						// inform the user of invalid credentials (username)
						UIFactory.displayToast("Incorrect Username or Password", false);
					}
				}
			}
		};
		
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
						
						// create EditText reference objects 
						EditText usernameField = (EditText) findViewById(R.id.signin_username);
						EditText passwordField = (EditText) findViewById(R.id.signin_password);
						
						// set the credential strings to the EditText values
						String username = usernameField.getText().toString();
						String password = passwordField.getText().toString();
						
						if (username.length() >= 5 && username.length() < 16)
						{
							if (password.length() >= 5 && password.length() < 16)
							{
								// set the glabal credential values to the verified local values
								USERNAME = username;
								PASSWORD = password;
								
								// create the messenger object for the handler
								Messenger msngr = new Messenger(requestHandler);
								
								// create the intent to gather the app account details
								Intent fetchAccountData = UIFactory.makeIntent(DetailService.class);
								
								// retrieve the mapify cloudant query url
								String urlValue = getString(R.string.accounts_query) + '"' + USERNAME + '"';
								
								// add the url and messenger objects to the intent
								fetchAccountData.putExtra("Url", urlValue);
								fetchAccountData.putExtra("Messenger", msngr);
								
								// begin the service
								startService(fetchAccountData);
							}
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
	
	public void saveSessionData(JSONObject json)
	{
		// save object for holding the session data
		UniArray sessionData = new UniArray();
		
		// parse the json user data object for saving
		try 
		{
			// store the account details string objects
			sessionData.putString("user_fname", json.getString("user_fname"));
			sessionData.putString("user_lname", json.getString("user_lname"));
			sessionData.putString("user_details", json.getString("user_details"));
			sessionData.putString("loc_city", json.getString("loc_city"));
			sessionData.putString("loc_state", json.getString("loc_state"));
			sessionData.putString("loc_state_index", json.getString("loc_state_index"));
			sessionData.putString("loc_country", json.getString("loc_country"));
			sessionData.putString("contact_email", json.getString("contact_email"));
			sessionData.putString("contact_phone", json.getString("contact_phone"));
			sessionData.putString("acct_username", json.getString("acct_username"));
			sessionData.putString("acct_password", json.getString("acct_password"));
			sessionData.putString("dob_day", json.getString("dob_day"));
			sessionData.putString("dob_month", json.getString("dob_month"));
			sessionData.putString("dob_year", json.getString("dob_year"));
			sessionData.putString("acct_use_email", json.getString("acct_use_email"));
			sessionData.putString("acct_use_phone", json.getString("acct_use_phone"));
			sessionData.putString("acct_user_rating", json.getString("acct_user_rating"));
			sessionData.putString("acct_type", json.getString("acct_type"));
			sessionData.putString("app_theme", json.getString("app_theme"));
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		// save the session data to storage
		FileSystem.writeObjectFile(this, sessionData, "session", true);
		
		// save the revision value to shared preferences
		ApplicationDefaults defaults = new ApplicationDefaults(this);
		defaults.setString("revision", revision);
	}

}
