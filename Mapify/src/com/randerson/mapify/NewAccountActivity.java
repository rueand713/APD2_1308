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

import com.randerson.classes.RequestService;

import libs.CalendarPicker;
import libs.FileSystem;
import libs.InterfaceManager;
import libs.RegExManager;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

public class NewAccountActivity extends Activity {

	InterfaceManager UIFactory;
	CalendarPicker calendarPicker;
	UniArray accountDetails = null;
	
	// the database revision object
	String revision = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// init the UIFactory
		UIFactory = new InterfaceManager(this);
		
		// init the picker
		calendarPicker = new CalendarPicker();
		
		// set the min date range to 18 years
		calendarPicker.setRange(18, 0);
		
		// init the uniarray object
		accountDetails = new UniArray();
		
		// set the window and orientation state
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		
		// allow the icon on the actionbar to act as navigation
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		setContentView(R.layout.activity_new_account);
		
		Spinner spinner = (Spinner) findViewById(R.id.newacct_state);
		
		// create a array adapter from resource file
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.states_array, android.R.layout.simple_spinner_item);
		
		// set the dropdown layout for the spinner and apply the adapter
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		// creaete the post request handling object
		final Handler requestHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
				if (msg.arg1 == RESULT_OK)
				{
					String response = (String) msg.obj;
					
					Log.i("Response", response);
					
					// create a success toast (this will be of a success or fail in future)
					UIFactory.displayToast("Account Created", false);
					
					// call the finish method to end the activity
					finish();
				}
			}
		};
		
		// create button reference for date picker
		Button setDOB = (Button) findViewById(R.id.newacct_dob);
		
		// verify that the button is valid
		if (setDOB != null)
		{
			// set the click listener for the button
			setDOB.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// show the calendar date picker
					calendarPicker.show(getFragmentManager(), "CalendarPicker");
				}
			});
		}
		
		// create the button reference
		Button createAcct = (Button) findViewById(R.id.createAccount);
		
		// verify that the createAcct button is valid
		if (createAcct != null)
		{
			// set the button click listener
			createAcct.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// set the EditText reference from layout
					EditText usernameField = (EditText) findViewById(R.id.newacct_username);
					EditText passwordField = (EditText) findViewById(R.id.newacct_password);
					EditText passConfirmField = (EditText) findViewById(R.id.newacct_pwd_confirm);
					EditText cityField = (EditText) findViewById(R.id.newacct_city);
					EditText phoneField = (EditText) findViewById(R.id.newacct_phone);
					EditText emailField = (EditText) findViewById(R.id.newacct_email);
					EditText fnameField = (EditText) findViewById(R.id.newacct_fname);
					EditText lnameField = (EditText) findViewById(R.id.newacct_lname);
					
					// grab the spinner data
					Spinner stateField = (Spinner) findViewById(R.id.newacct_state);
					
					// set the string values from the referenced elements
					String username = usernameField.getText().toString();
					String password = passwordField.getText().toString();
					String passConfirm = passConfirmField.getText().toString();
					String city = cityField.getText().toString();
					String state = stateField.getSelectedItem().toString();
					String phone = phoneField.getText().toString();
					String email = emailField.getText().toString();
					String fname = fnameField.getText().toString();
					String lname = lnameField.getText().toString();
					String birthDay = "" + calendarPicker.getPickerDate()[0];
					String birthMonth = "" + calendarPicker.getPickerDate()[1];
					String birthYear = "" + calendarPicker.getPickerDate()[2];
					
					// store the field text in string array for quick iteration during validation
					String[] validateFields = { username, password, passConfirm, city, 
												phone, email, fname, lname, birthYear};
					
					// create the list array for storing the validation errors
					ArrayList<String> errors = new ArrayList<String>();
					
					// iterate over the field data and set any error msgs to the error array
					for (int i = 0; i < validateFields.length; i++)
					{
						if (validateFields[i] != null && validateFields[i].equals("") == false)
						{
							// switch statement for evaluating the text fields using i
							switch(i) {
							
							// each switch case will compare the current field string with the proper regular expression
							// from the RegExManager class method checkPattern. A message will be added to the errors array list
							// for each conditional fail that is met
							
							case 0:// username
								if (RegExManager.checkPattern(validateFields[i], RegExManager.USERNAME_PATTERN) == false)
								{
									errors.add("Invalid Username formatting a-z_a-z0-9");
								}
								else if (validateFields[i].length() < 5 || validateFields[i].length() > 16)
								{
									errors.add("Username must be between 5 and 16 alpha-numeric characters");
								}
								break;
								
							case 1:
							case 2:// password and confirmation password
								if (RegExManager.checkPattern(validateFields[i], RegExManager.PASSWORD_PATTERN) == false)
								{
									// check which password case was hit for adding the proper message
									if (i == 1)
									{
										errors.add("Password is not formatted properly a-zA-Z0-9");
									}
									else if (i == 2)
									{
										errors.add("Confirmation Password is not formatted properly a-zA-Z0-9");
									}
								}		// check the length of the password and confirmation passwords
								else if (validateFields[i].length() < 5 || validateFields[i].length() > 16)
								{
									// check which password case was hit for adding the proper message
									if (i == 1)
									{
										errors.add("Password must be 5 to 16 alpha-characters");
									}
									else if (i == 2)
									{
										errors.add("Confirmation Password must be 5 to 16 alpha-characters");
									}
								}		// check that the password and confirmation are exact match
								else if (validateFields[1].equals(validateFields[2]) == false && i == 2)
								{
									errors.add("Passwords do not match");
								}
								break;
								
							case 3:	// city
								if (RegExManager.checkPattern(validateFields[i], "^[a-zA-z]{3,18}") == false)
								{
									errors.add("City name must be 2 to 18 characters");
								}
								break;
								
							case 4:	// phone number
								if (RegExManager.checkPattern(validateFields[i], RegExManager.DOMESTIC_PHONE_PATTERN) == false)
								{
									errors.add("Invalid number formatting xxx-xxx-xxxx");
								}
								break;
								
							case 5:	// email
								if (RegExManager.checkPattern(validateFields[i], RegExManager.EMAIL_PATTERN) == false)
								{
									errors.add("Invalid E-mail formatting");
								}
								break;
								
							case 6:	// first name
								if (RegExManager.checkPattern(validateFields[i], "^[a-zA-Z]{2,16}") == false)
								{
									errors.add("Name must be 2 to 16 characters");
								}
								break;
								
							case 7:	// last name
								if (RegExManager.checkPattern(validateFields[i], "^[a-zA-Z]{2,18}") == false)
								{
									errors.add("Name must be 2 to 18 characters");
								}
								break;
								
							case 8:	// birth year
								
								// grab the current year
								int yr = calendarPicker.getPickerDate()[2];
								
								// checks that the current year selected is within supplied range values
								if (calendarPicker.checkRange(yr) == false)
								{									
									errors.add("You must be at least 18 to create an account");
								}
								break;
								
								default:
									break;
							}
								
						}
						else
						{
							UIFactory.displayToast("All fields are required", false);
							break;
						}
					}
					
					
					// check that there were no errors reported before attempting account creation
					if (errors.size() == 0)
					{
							
						// add the user data to the JSON object
						accountDetails.putString("_id", username);
						accountDetails.putString("acct_username", username);
						accountDetails.putString("acct_password", password);
						accountDetails.putString("loc_city", city);
						accountDetails.putString("loc_state", state);
						accountDetails.putString("loc_country", "United States");
						accountDetails.putString("contact_phone", phone);
						accountDetails.putString("contact_email", email);
						accountDetails.putString("user_fname", fname);
						accountDetails.putString("user_lname", lname);
						accountDetails.putString("dob_day", birthDay);
						accountDetails.putString("dob_month", birthMonth);
						accountDetails.putString("dob_year", birthYear);
						
						// save the stored user JSON data object
						FileSystem.writeObjectFile(getApplication(), accountDetails, "data", true);
						
						// create the messenger for the handler
						Messenger msngr = new Messenger(requestHandler);
						
						// create an intent for the service
						Intent createAccount = UIFactory.makeIntent(RequestService.class);
						
						// verify that the intent is valid
						if (createAccount != null)
						{
						
							// add the intent extras for communicating the post and response
							createAccount.putExtra(RequestService.URL_KEY, getString(R.string.accounts_uri));
							createAccount.putExtra(RequestService.MESSENGER_KEY, msngr);
							//createAccount.putExtra(RequestService.AUTH_KEY, getString(R.string.account_session));
							
							// start the service
							startService(createAccount);
						
							
						}
						
					}
					else
					{
						// set the scroll view reference from layout
						ScrollView errorView = (ScrollView) findViewById(R.id.errors_view);
						
						// set the linearlayout reference from the layout
						LinearLayout errorLayout = (LinearLayout) findViewById(R.id.error_layout);
						
						// clear the error layout
						errorLayout.removeAllViews();
						
						// verify that the scroll view is valid
						if (errorView != null)
						{
							// set the scroll view to be visible
							errorView.setVisibility(ScrollView.VISIBLE);
						}
						
						// verify that the linear layout is valid
						if (errorLayout != null)
						{
							// iterate over the errors list and add a text view of each to the error layout
							for (int i = 0; i < errors.size(); i++)
							{
								// set the error text
								String errorText = errors.get(i);
								
								// create a textView for the current error
								TextView err = UIFactory.createTextView(errorText, i+2);
								
								// add the textView to the layout
								errorLayout.addView(err);
							}
							
							// inform the user error correction is needed
							UIFactory.displayToast("Please correct the form errors", false);
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
