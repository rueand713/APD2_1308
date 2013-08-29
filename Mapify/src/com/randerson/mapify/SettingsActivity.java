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

import libs.ApplicationDefaults;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends Activity {

InterfaceManager UIFactory;
UniArray sessionData;
Spinner statesField;
Handler requestHandler;
	
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
		
		setContentView(R.layout.activity_settings);
		
		// load the session data from the device
		sessionData = (UniArray) FileSystem.readObjectFile(this, "session", true);
		
		// set the user data to the fields
		updateCurrentSettings();
		
		// load the button from layout
		Button saveBtn = (Button) findViewById(R.id.settings_save_btn);
		
		// verify that the button is valid
		if (saveBtn != null)
		{
			// set the button click listener
			saveBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// call the method to update the settings
					updateUserSettings();
				}
			});
			
			requestHandler = new Handler()
			{
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					
					if (msg.arg1 == RESULT_OK)
					{
						String response = (String) msg.obj;
						
						Log.i("Response", response);
						
						//revision = jsOn.getJSONObject("value").getValue("rev");
					}
				}
			};
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
	
	public void updateCurrentSettings()
	{
		// current settings objects
		String currentEmail = sessionData.getString("contact_email");
		String currentPhone = sessionData.getString("contact_phone");
		String sellerDetails = sessionData.getString("user_details");
		String contactEmail = sessionData.getString("acct_use_email");
		String contactPhone = sessionData.getString("acct_use_phone");
		String city = sessionData.getString("loc_city");
		String theme = sessionData.getString("app_theme");
		int stateIndex = Integer.parseInt(sessionData.getString("loc_state_index"));
		
		// create a spinner object from the layout
		statesField = (Spinner) findViewById(R.id.settings_state);
		
		// create a array adapter from resource file
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.states_array, android.R.layout.simple_spinner_item);
		
		// set the dropdown layout for the spinner and apply the adapter
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		statesField.setAdapter(adapter);
		
		// verify that the spinner is valid
		if (statesField != null)
		{
			// set the current state
			statesField.setSelection(stateIndex);
		}
		
		// create a reference to the radio group in layout
		RadioGroup themes = (RadioGroup) findViewById(R.id.radio_theme);
		
		// verify that the referenced radio group is valid
		if (themes != null)
		{
			
			// create a null radio button
			RadioButton themeValues = null;
			
			// compare the value of the theme string and set the radio button to reference that theme button from layout
			if (theme.equals("Theme A"))
			{
				themeValues = (RadioButton) themes.findViewById(R.id.t1);
			}
			else if (theme.equals("Theme B"))
			{
				themeValues = (RadioButton) themes.findViewById(R.id.t2);
			}
			else if (theme.equals("Theme C"))
			{
				themeValues = (RadioButton) themes.findViewById(R.id.t3);
			}
			else if (theme.equals("Theme D"))
			{
				themeValues = (RadioButton) themes.findViewById(R.id.t4);
			}
			
			// check the radio button that corresponds with the theme
			if (themeValues != null)
			{
				themeValues.setChecked(true);
			}
			
		}
		
		// create the switch reference objects from the layout
		Switch email = (Switch) findViewById(R.id.email_switch);
		Switch phone = (Switch) findViewById(R.id.call_switch);
		
		// verify that the email switch is valid
		if (email != null)
		{
			// set the email switch based on the saved data
			if (contactEmail.equals("true"))
			{
				email.setChecked(true);
			}
			else if (contactEmail.equals("false"))
			{
				email.setChecked(false);
			}
		}
		
		// verify that the phone switch is valid
		if (phone != null)
		{
			// set the phone switch based on the saved data
			if (contactPhone.equals("true"))
			{
				phone.setChecked(true);
			}
			else if (contactPhone.equals("false"))
			{
				phone.setChecked(false);
			}
		}
		
		// create a reference to the email field
		EditText cityField = (EditText) findViewById(R.id.settings_city);
		
		// verify that the edit text is valid
		if (cityField != null)
		{
			// set the saved user city
			cityField.setText(city);
		}
		
		// create a reference to the email field
		EditText userEmail = (EditText) findViewById(R.id.settings_email);
		
		// verify that the edit text is valid
		if (userEmail != null)
		{
			// set the saved user email
			userEmail.setText(currentEmail);
		}
		
		// create a reference to the email field
		EditText userPhone = (EditText) findViewById(R.id.settings_phone);
		
		// verify that the edit text is valid
		if (userPhone != null)
		{
			// set the saved user phone
			userPhone.setText(currentPhone);
		}
		
		// create a reference to the seller details field
		EditText details = (EditText) findViewById(R.id.settings_user_details);
		
		// verify that the details object is valid
		if (details != null)
		{
			// set the saved user details
			details.setText(sellerDetails);
		}
	}
	
	
	public void updateUserSettings()
	{
		// settings saving objects
		boolean allowEmail = false;
		boolean allowCall = false;
		String theme = "";
		String emailAddr = "";
		String sellerDetails = "";
		String city = "";
		String phoneNum = "";
		String state = "";
		int stateIndex = 0;
		
		// verify that the spinner is valid
		if (statesField != null)
		{
			// get the selected value
			state = statesField.getSelectedItem().toString();
			
			// get the selected item index
			stateIndex = statesField.getSelectedItemPosition();
		}
		
		// create the switch reference objects from the layout
		Switch email = (Switch) findViewById(R.id.email_switch);
		Switch phone = (Switch) findViewById(R.id.call_switch);
		
		// verify that the email switch is valid
		if (email != null)
		{
			// check if the email is checked on
			allowEmail = email.isChecked();
		}
		
		// verify that the phone switch is valid
		if (phone != null)
		{
			// check if the phone is checked on
			allowCall = phone.isChecked();
		}
		
		// create a reference to the radio group in layout
		RadioGroup themes = (RadioGroup) findViewById(R.id.radio_theme);
		
		// verify that the referenced radio group is valid
		if (themes != null)
		{
			// get the selected radio button id from the radio group
			int selectedId = themes.getCheckedRadioButtonId();
			
			// create a radio button from the radiogroup's returned selected id
			RadioButton themeValues = (RadioButton) themes.findViewById(selectedId);
			
			// set the string to the text of the selected radio button
			theme = themeValues.getText().toString();
		}
		
		// create a reference to the email field
		EditText cityField = (EditText) findViewById(R.id.settings_city);
		
		// verify that the edit text is valid
		if (cityField != null)
		{
			// get the text from the editText field and set it to the string
			city = cityField.getText().toString();
		}
		
		// create a reference to the email field
		EditText phoneField = (EditText) findViewById(R.id.settings_phone);
		
		// verify that the edit text is valid
		if (phoneField != null)
		{
			// get the text from the editText field and set it to the string
			phoneNum = phoneField.getText().toString();
		}
		
		// create a reference to the email field
		EditText userEmail = (EditText) findViewById(R.id.settings_email);
		
		// verify that the edit text is valid
		if (userEmail != null)
		{
			// get the text from the editText field and set it to the string
			emailAddr = userEmail.getText().toString();
		}
		
		// create a reference to the seller details field
		EditText details = (EditText) findViewById(R.id.settings_user_details);
		
		// verify that the details object is valid
		if (details != null)
		{
			// set the string to the text from the textfield
			sellerDetails = details.getText().toString();
		}
		
		// call the method to validate user intention and save out the changes
		validateAndSave(emailAddr, sellerDetails, city, state, stateIndex, phoneNum, theme, allowCall, allowEmail);
	}
	
	public void validateAndSave(String email, String details, String city, String state, int stateIndex, String number, String theme, boolean contactByPhone, boolean contactByEmail)
	{
		// create the list array for storing the validation errors
		ArrayList<String> errors = new ArrayList<String>();
		
		// create editText references from the layout
		EditText passwordField = (EditText) findViewById(R.id.settings_old_password);
		EditText newPasswordField = (EditText) findViewById(R.id.settings_new_password);
		EditText confirmPasswordField = (EditText) findViewById(R.id.settings_confirm_password);
		
		// verifcation strings
		String oldPassword = "";
		String newPassword = "";
		String confirmPassword = "";
		
		// verify that the phone number is valid format
		if (RegExManager.checkPattern(number, RegExManager.DOMESTIC_PHONE_PATTERN) == false || number.length() != 12)
		{
			errors.add("Invalid number formatting xxx-xxx-xxxx");
		}
		
		// verify that the email is valid format
		if (RegExManager.checkPattern(email, RegExManager.EMAIL_PATTERN) == false)
		{
			errors.add("Invalid E-mail formatting");
		}
		
		// verify that the city is valid format
		if (RegExManager.checkPattern(city, "^[a-zA-z]{2,18}") == false)
		{
			errors.add("City name must be 2 to 18 characters");
		}
		
		// bool to verify the user intention to change passwords
		boolean changePassword = false;
		
		// verify that the password field is valid
		if (passwordField != null)
		{
			// set the old password to the password field string
			oldPassword = passwordField.getText().toString();
			
			// verify that the password is valid length
			if (oldPassword.length() < 5 || oldPassword.length() > 16)
			{
				// verify that the password is a valid password
				if (RegExManager.checkPattern(oldPassword, RegExManager.PASSWORD_PATTERN))
				{
					errors.add("Curret Password must be: a-z0-9, 5-16 chars");
				}
			}
		}
		
		// verify that the new and confirm password fields are valid
		if (newPasswordField != null && confirmPasswordField != null)
		{
			// set the confirm password to the password field string
			confirmPassword = confirmPasswordField.getText().toString();
			
			// set the new password to the password field string
			newPassword = newPasswordField.getText().toString();
			
			// check if the user intends to change the password
			if (newPassword.length() > 0 || confirmPassword.length() > 0)
			{
				
				// the user input text into the new or confirm password fields
				// so must want to change their password
				changePassword = true;
				
				// verify that the password is valid length
				if (newPassword.length() < 5 || newPassword.length() > 16)
				{
					// verify that the password is a valid password
					if (RegExManager.checkPattern(newPassword, RegExManager.PASSWORD_PATTERN))
					{
						errors.add("New Password must be: a-z0-9, 5-16 chars");
					}
				}
				
				// verify that the password is valid length
				if (confirmPassword.length() < 5 || confirmPassword.length() > 16)
				{
					// verify that the password is a valid password
					if (RegExManager.checkPattern(confirmPassword, RegExManager.PASSWORD_PATTERN))
					{
						errors.add("Confirmation Password must be: a-z0-9, 5-16 chars");
					}
				}
				
				// check that the passwords are equal
				if (oldPassword.equals(newPassword) == false || oldPassword.equals(confirmPassword) == false)
				{
					errors.add("Passwords do not match");
				}
			}
		}
		
		// verify that there were no errors found on the form
		if (errors.size() == 0)
		{	
			// verify that the session data is valid
			if (sessionData != null)
			{
				// get the stored password for comparing
				String sessionPass = sessionData.getString("acct_password");
				
				// verify that the stored password is equal to the current password
				// for security purposes
				if (oldPassword.equals(sessionPass))
				{
					// verify that the user wants to change their password
					if (changePassword)
					{
						// save the new user password
						sessionData.putString("acct_password", newPassword);
					}
					
					// save the new user email
					sessionData.putString("contact_email", email);
					
					// save the user details
					sessionData.putString("user_details", details);
					
					// save the user theme setting
					sessionData.putString("app_theme", theme);
					
					String phoneBool = "false";
					String emailBool = "false";
					
					
					// set text representation of the bool value
					if (contactByPhone)
					{
						phoneBool = "true";
					}
					
					// set text representation of the bool value
					if (contactByEmail)
					{
						emailBool = "true";
					}
					
					// save the user contact settings
					sessionData.putString("acct_use_phone", phoneBool);
					sessionData.putString("acct_use_email", emailBool);
					
					// save the user phone
					sessionData.putString("contact_phone", number);
					
					// save the user location data
					sessionData.putString("loc_city", city);
					sessionData.putString("loc_state", state);
					sessionData.putString("loc_state_index", stateIndex + "");
					
					// store the current session data
					FileSystem.writeObjectFile(this, sessionData, "session", true);
					
					// create an accountDetails copy of the sessionData
					UniArray accountDetails = sessionData;
					
					// retrieve the revision value
					String rev = new ApplicationDefaults(this).getData().getString("revision", "");
					
					// add the revision and id field to the accountDetails for updating the data
					accountDetails.putString("_rev", rev);
					accountDetails.putString("_id", sessionData.getString("acct_username").toLowerCase());
					
					// store the accountDetails for access from the request service class
					FileSystem.writeObjectFile(this, accountDetails, "data", true);
					
					// create the messenger for the handler
					Messenger msngr = new Messenger(requestHandler);
					
					// create an intent for the service
					Intent updateAccount = UIFactory.makeIntent(RequestService.class);
					
					// verify that the intent is valid
					if (updateAccount != null)
					{
					
						// add the intent extras for communicating the post and response
						updateAccount.putExtra(RequestService.URL_KEY, getString(R.string.accounts_uri));
						updateAccount.putExtra(RequestService.MESSENGER_KEY, msngr);
						
						// start the service
						startService(updateAccount);	
					}
				}
				else
				{
					UIFactory.displayToast("Data not saved. Invalid password", true);
				}
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
	
}
