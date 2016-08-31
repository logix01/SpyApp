package com.enuke.smartapp;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.asynctask.httpconnect.ConnectToServerWithSpiner;
import com.blinkawards.utility.Utility;

public class UpdateProfile extends Activity {
	private static Context mContext = null;
	private EditText mMob;
	private EditText mEmail;
	private EditText mPassword;
	private EditText mConf_pass;
	private Button mRegister;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_profile);
		mContext = UpdateProfile.this;

		registerControls();
		getprofileValueFromServer();
		mRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (isValid()) {
					sendDataToServer();
					/*
					 * Intent serviceLauncher = new Intent(MainActivity.this,
					 * SendDataService.class);
					 * MainActivity.this.startService(serviceLauncher);
					 */
				}
			}

		});
	}

	private void getprofileValueFromServer() {
		if (Utility.isInternetConnected(mContext)) {
			// You are connected, do something online.

			try {

				String url = Utility.getBaseposturl();

				JSONObject ProfileDetails = new JSONObject();
				ProfileDetails.put("ClientId",Utility.getSaveDataLocally(mContext, "mClientId"));
				JSONObject finaldata = new JSONObject();
				finaldata.put("ProfileRequest", ProfileDetails);
				ConnectToServerWithSpiner connect = new ConnectToServerWithSpiner();
				connect.extConnectToServer(mContext,new ConnectToServerWithSpiner.Callback() {
							public void callFinished(String result) {
								
								displayProfileData(result);
							}
						}, url, finaldata, "POST");
				connect.execute(finaldata);
			} catch (Exception e) {
				////System.out.println("Print execute error " + e);
			}

		} else {
			showToast("Please check your internet connection");
		}

	}

	protected void displayProfileData(String result) {

		// append the data coming form server
		if (result != null) {
			try {
				JSONObject mResult = new JSONObject(result);
				if (mResult.getJSONObject("ProfileRetrunData") != null) {
					JSONObject mStatus = mResult.getJSONObject("ProfileRetrunData");
					if (mStatus.getString("Status").equalsIgnoreCase("ok")) 
					{
						final JSONObject mProfileDetails = mStatus.getJSONObject("ProfileDetails");
						UpdateProfile.this.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub

								try {
										mEmail.setText(mProfileDetails.getString("Email"));								
										mMob.setText(mProfileDetails.getString("Mobile"));
										mPassword.setText(mProfileDetails.getString("Password"));
										mConf_pass.setText(mProfileDetails.getString("Password"));	
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
					}
				}
			} catch (JSONException e) 
			{
				Log.e("Json exception", e.toString());
			}
		}
		// end
	}

	private void registerControls() {
		mMob = (EditText) findViewById(R.id.mob);
		mEmail = (EditText) findViewById(R.id.email);
		mPassword = (EditText) findViewById(R.id.mPassword);
		mConf_pass = (EditText) findViewById(R.id.mConf_pass);
		mRegister = (Button) findViewById(R.id.mRegister);
		TextView mHearText=(TextView)findViewById(R.id.mHeaderText);
		mHearText.setText("Update Profile");
	

	}

	private boolean isValid() {

		if (TextUtils.isEmpty(mMob.getText().toString().trim())
				&& mMob.getText().toString().length() < 10) {

			mMob.setError("Please Enter the correct Mobile Number");
			return false;
		}else{
			mMob.setError(null);
		}if (!android.util.Patterns.EMAIL_ADDRESS.matcher(
				mEmail.getText().toString()).matches()) {

			mEmail.setError("Please enter a valid email address");
			return false;
		} else{
			mEmail.setError(null);
		} if (TextUtils.isEmpty(mPassword.getText().toString())) {

			mPassword.setError("Please enter your PIN password");
			return false;
		}else if(mPassword.getText().toString().trim().length()<6){
			mPassword.setError("Password must be SIX degit ");
			return false;
			
		} else{
			mPassword.setError(null);
		}
		if (TextUtils.isEmpty(mConf_pass.getText().toString())) {

			mConf_pass.setError("Please enter your confirm PIN password");
			return false;
		} else{mConf_pass.setError(null);
		}
		if (!(mPassword.getText().toString().trim().equals(mConf_pass
				.getText().toString().trim()))) {
			if(mPassword.getText().toString().trim().length()>6){
				mPassword.setError("PIN Password must be SIX degit ");
				return false;
				
			}
			mPassword.setError("your password and confirm password not match ");
			mConf_pass
					.setError("your password and confirm password not match ");
			return false;
		}
		else{
			mPassword.setError(null);
			mPassword.setError(null);
		}
		return true;
	}
	private void sendDataToServer() {
		if (Utility.isInternetConnected(mContext)) {
			// You are connected, do something online.

			try {

				String theDevId = Utility.getDeviceKey(mContext);
				String url = Utility.getBaseposturl();
				JSONObject login = new JSONObject();
				login.put("ClientId",Utility.getClientId(mContext));
				login.put("Email", mEmail.getText().toString().toString());
				String pass = mPassword.getText().toString().trim();
				login.put("Mobile", mMob.getText().toString().trim());
				login.put("Password", pass);
				login.put("DeviceId", theDevId);
				JSONObject finaldata = new JSONObject();
				finaldata.put("UpdateRegistration", login);
				ConnectToServerWithSpiner connect = new ConnectToServerWithSpiner();
				connect.extConnectToServer(mContext,new ConnectToServerWithSpiner.Callback() {
							public void callFinished(String result) {
								httpResult(result);
							}
						}, url, finaldata, "POST");
				connect.execute(finaldata);

			} catch (Exception e) {

				////System.out.println("Print execute error " + e);
			}

		} else {
			showToast("Please check your internet connection");
		}

	}

	protected void httpResult(String result) {
		JSONObject resp = null;
		try {
			resp = new JSONObject(result);
			JSONObject response = resp.getJSONObject("UpdateRegistrationResponse");
			if (response.getString("Status").equalsIgnoreCase("OK")) {				
				
				showPopup(mContext, response.getString("Message"), false);
			}
		} catch (final JSONException e) {

		}

	}

	private void showToast(final String msg) {
		((Activity) mContext).runOnUiThread(new Runnable() {

			@Override
			public void run() {

				Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();

			}
		});

	}

	@SuppressWarnings("deprecation")
	private void showPopup(final Context mContext, final String msg,boolean size) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view = inflater.inflate(R.layout.custom_dialog_theme, null);
		final PopupWindow popupWindow;
		((TextView)view.findViewById(R.id.mHeaderText)).setText("Alert");
		if (size) {
			popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT, true);
		} else {
			final Display display = ((Activity) mContext).getWindowManager()
					.getDefaultDisplay();

			popupWindow = new PopupWindow(view,(int) (display.getWidth() * .8),	(int) (display.getHeight() * .8), true);
		}

		((TextView) view.findViewById(R.id.msg_text)).setText(msg);

		Button b = (Button) view.findViewById(R.id.ok_btn);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				popupWindow.dismiss();
			}
		});

		popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
