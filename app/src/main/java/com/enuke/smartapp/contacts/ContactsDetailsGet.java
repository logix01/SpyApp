package com.enuke.smartapp.contacts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.SQLException;
import android.provider.ContactsContract;

import com.blinkawards.utility.Constants;
import com.blinkawards.utility.DataBaseUtils;

public class ContactsDetailsGet {
	
	private static ArrayList<Map<String, String>> phoneList=new ArrayList<Map<String,String>>();

	public static ArrayList<Map<String,String>> contactInfo(Context context){
		 DataBaseUtils db=new DataBaseUtils(context);
		
		phoneList.clear();
		Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null,null); 
		while (phones.moveToNext()) 
		{
			 Map<String,String> phoneMap=new HashMap<String, String>();
		 String phoneName=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)); 
		 String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		 phoneNumber=phoneNumber.replace(" ", "").replace("(", "").replace(")", "").replace("[", "").replace(" ]", "").replace("-", "");
		 String email=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
		
		 try {
			 
			db.open(Constants.DataBaseName);
			Cursor cursor=db.query(Constants.CONTACTS_DETAILS_TABLE, null, "contact_number=?",new String []{phoneNumber}, null);

			
			
			
			if(cursor.getCount()==0){
				 ContentValues values=new ContentValues();
				 values.put("contact_name", phoneName);
				 values.put("contact_number", phoneNumber);
				 values.put("contact_email", email);
				 db.insert(Constants.CONTACTS_DETAILS_TABLE, null, values);
				 phoneMap.put("Email", email);
				 phoneMap.put("PhoneNumber", phoneNumber);
				 phoneMap.put("ContactName",phoneName);				 
				 phoneList.add(phoneMap);
			}
			cursor.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 db.close();
		
		}
		phones.close();
		return phoneList;
	}

}
