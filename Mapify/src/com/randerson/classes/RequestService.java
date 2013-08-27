package com.randerson.classes;

import org.json.JSONObject;

import com.randerson.mapify.R;

import libs.FileSystem;
import libs.IOManager;
import libs.JSONhandler;
import libs.UniArray;
import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class RequestService extends IntentService {
	
	// service identifier keys
	public static final String MESSENGER_KEY = "Messenger";
	public static final String URL_KEY = "Url";
	public static final String AUTH_KEY = "AuthKey";

	public RequestService() {
		super("RequestService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		// grab the handled intent extras
		Bundle bundle = intent.getExtras();
		
		// verify that the bundle is valid
		if (bundle != null)
		{
			
			// set the string url to the bundle's url string
			String url = bundle.getString(URL_KEY);
			
			// set the string url to the bundle's url string
			String authUrl = bundle.getString(AUTH_KEY);
			
			// grab the handled intent messenger
			Messenger messenger = (Messenger) bundle.get(MESSENGER_KEY);
			
			// checks if there is a network connection
			boolean connected = IOManager.getConnectionStatus(this);
			
			// create a empty response string
			String response = "";
			
			// verify that the device has data connection
			if (connected == true)
			{
				UniArray userData = (UniArray) FileSystem.readObjectFile(getApplication(), "data", true);
				
				JSONObject json = JSONhandler.jsonify(userData);
				
				Log.i("JSON DATA", json.toString());
				
				//String params = "name=" + getString(R.string.api_key) + "&password=" + getString(R.string.api_pass);
				
				String params = "username=" + getString(R.string.api_key) + "&database=rueand713/mapify&roles=_reader&roles=_writer";
				
				// make the request and set the resonse
				response = IOManager.makePostRequest(url, authUrl, json, params);
			}
			
			// obtain message for the message object
			Message message = Message.obtain();
			
			// set the result ok to message argument 1
			message.arg1 = Activity.RESULT_OK;
			
			// set the response text to the message object
			message.obj = response;
			
			// try to send the message
			try {
				messenger.send(message);
			} catch (RemoteException e) {
				Log.e("REMOTE EXCEPTION", "Sending message in 'onHandleIntent()'");
			}
		}
		
	}

}
