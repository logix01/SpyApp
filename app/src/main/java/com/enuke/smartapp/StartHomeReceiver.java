package com.enuke.smartapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.blinkawards.utility.Utility;

public class StartHomeReceiver extends BroadcastReceiver {
@Override
	public void onReceive(Context context, Intent intent) {
	//System.out.println("start home receiver");
		if(intent.getAction().equalsIgnoreCase("android.intent.action.NEW_OUTGOING_CALL")){
			String number=intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			// System.out.println("aciton event:"+intent.getAction());
		        String compare_num="*"+Utility.getSaveDataLocally(context, "PINpass")+"#";
		       // System.out.println(compare_num);
		       if(number.equals(compare_num))
		        {
		             
		            abortBroadcast();
		            setResultData(null);
		            Intent myintent=new Intent(context,SignUpActivity.class);
		            myintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		            context.startActivity(myintent);
		          
		          
		       }
		      
		}
		 ////System.out.println("phone number::::::"+intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER)); 
	}

	

	
}
