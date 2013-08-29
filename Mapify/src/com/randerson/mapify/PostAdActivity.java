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
import com.randerson.classes.JSONStringService;
import com.randerson.classes.RequestService;

import libs.FileSystem;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class PostAdActivity extends Activity {

InterfaceManager UIFactory;
String details;
String price;
String title;
String catString;
UniArray sessionData;

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
		
		setContentView(R.layout.activity_post_ad);
		
		// loads the session data object
		sessionData = (UniArray) FileSystem.readObjectFile(this, "session", true);
		
		final Handler updateHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
				if (msg.arg1 == RESULT_OK)
				{
					String response = (String) msg.obj;
					
					Log.i("Response", response);
					
					// create an intent to launch the home activity
					Intent exit = UIFactory.makeIntent(HomeActivity.class);
					
					// verify that the exit intent is valid
					if (exit !=  null)
					{
						// launch the home activity
						startActivity(exit);
					}
				}
			}
		};
		
		final Handler postHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
				if (msg.arg1 == RESULT_OK)
				{
					// set the string to the returned result
					String result = (String) msg.obj;
					
					Log.i("Result", result);
					
					// create a jsonhandler class with the result string
					JSONhandler json = new JSONhandler(result);
					
					// create a jsonobject from the result with the handler
					JSONObject jsData = json.returnJSONobject();
					
					// update the json object and return it
					jsData = updateAdData(jsData);
					
					// create the intent for the json string service
					Intent updateIntent = UIFactory.makeIntent(JSONStringService.class);
					
					Messenger msgr = new Messenger(updateHandler);
					
					// add the intent extras for making the request and handling the response
					updateIntent.putExtra(RequestService.MESSENGER_KEY, msgr);
					updateIntent.putExtra(RequestService.URL_KEY, getString(R.string.accounts_uri));
					updateIntent.putExtra("jsonString", jsData.toString());
					
					// start the service
					startService(updateIntent);
					
				}
			}
		};
		
		Button submit = (Button) findViewById(R.id.add_ad_btn);
		
		if (submit != null)
		{
			submit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// create a messenger object for the handler
					Messenger msngr = new Messenger(postHandler);
					
					// create a new intent for making the posting request
					Intent request = UIFactory.makeIntent(DetailService.class);
					
					// verify that the intent is valid
					if (request != null)
					{
						EditText adTitle = (EditText) findViewById(R.id.ad_title);
						EditText adPrice = (EditText) findViewById(R.id.ad_price);
						EditText adDetails = (EditText) findViewById(R.id.ad_details);
						RadioGroup adCats = (RadioGroup) findViewById(R.id.ad_categories);
						RadioButton category;
						
						boolean errors = false;
						
						// check that the title is valid
						if (adTitle != null && adTitle.getText().toString().length() > 1)
						{
							// set the string from the field
							title = adTitle.getText().toString();
						}
						else
						{
							// set the error flag bool
							errors = true;
							
							// display error toast
							UIFactory.displayToast("Title is required", false);
						}
						
						// check that the title is valid
						if (adPrice != null && adPrice.getText().toString().length() > 1)
						{
							price = adPrice.getText().toString();
						}
						else
						{
							// set the error flag bool
							errors = true;
							
							// display error toast
							UIFactory.displayToast("Price is required", false);
						}
						
						// check that the title is valid
						if (adDetails != null && adDetails.getText().toString().length() > 1)
						{
							details = adDetails.getText().toString();
						}
						else
						{
							// set the error flag bool
							errors = true;
							
							// display error toast
							UIFactory.displayToast("Details is required", false);
						}
						
						// check that the adcategory radio group is valid
						if (adCats != null)
						{
							// return the id of the selected radio button
							int selected = adCats.getCheckedRadioButtonId();
							
							// find the radio button that is selected in the group
							category = (RadioButton) adCats.findViewById(selected);
							
							// get the category string
							catString = category.getText().toString();
							
						}
						
						// verify that there were no form errors
						if (errors == false)
						{
							
							// add the intent extras for making the request and handling the response
							request.putExtra(RequestService.MESSENGER_KEY, msngr);
							request.putExtra(RequestService.URL_KEY, getString(R.string.account_ad_posting));
							
							// start the service
							startService(request);
						}
					}
					
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
			Intent home = UIFactory.makeIntent(HomeActivity.class);
			
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
	
	public JSONObject updateAdData(JSONObject json)
	{
		JSONObject ad = new JSONObject();
		
		String phoneNum = "";
		String email = "";
		
		// check if user allows phone contact
		if (sessionData.getString("acct_use_phone").equals("true"))
		{
			phoneNum = sessionData.getString("contact_phone");
		}
		
		// check if user allows email contact
		if (sessionData.getString("acct_use_email").equals("true"))
		{
			email = sessionData.getString("contact_email");
		}
		
		try {
			ad.put("title", title);
			ad.put("price", price);
			ad.put("details", details);
			ad.put("city", sessionData.getString("loc_city"));
			ad.put("state", sessionData.getString("loc_state"));
			ad.put("country", sessionData.getString("loc_country"));
			ad.put("contact-phone", phoneNum);
			ad.put("contact-email", email);
			ad.put("poster", sessionData.getString("acct_username"));
			
			// adds the new json object to the ad category
			json.accumulate(catString, ad);
		} catch (JSONException e) {
			
			e.printStackTrace();
			Log.e("JSONException", "Creating JSON object in ad posting activity");
		}
		
		Log.i("JSON Object", json.toString());
		
		return json;
	}
	
}
