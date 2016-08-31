package com.enuke.smartapp.gps;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.enuke.smartapp.DBHelper;

public class GPSLogProvider 
{

	private static final String TABLE_NAME = "geolocation";

	private static final String COLUMN_NAME_KEY_ROWID = "rowid";
	private static final String COLUMN_NAME_KEY_LATITUDE = "latitude";
	private static final String COLUMN_NAME_KEY_LONGITUDE = "longitude";
	private static final String COLUMN_NAME_KEY_TIMESTAMP = "timestamp";
	private static final String COLUMN_NAME_KEY_DIRECTION = "direction";
	

	
	
	private static int COLUMN_INDEX_KEY_ROWID = 0;
	private static int COLUMN_INDEX_KEY_LATITUDE = 1;
	private static int COLUMN_INDEX_KEY_LONGITUDE = 2;	
	private static int COLUMN_INDEX_KEY_DIRECTION = 3;
	private static int COLUMN_INDEX_KEY_TIMESTAMP = 4;


	private static void setColumns(Cursor cursor) 
	{
	
		COLUMN_INDEX_KEY_ROWID=cursor.getColumnIndex(COLUMN_NAME_KEY_ROWID);
		COLUMN_INDEX_KEY_LATITUDE=cursor.getColumnIndex(COLUMN_NAME_KEY_LATITUDE);	
		COLUMN_INDEX_KEY_LONGITUDE=cursor.getColumnIndex(COLUMN_NAME_KEY_LONGITUDE);
		COLUMN_INDEX_KEY_TIMESTAMP=cursor.getColumnIndex(COLUMN_NAME_KEY_TIMESTAMP);
		COLUMN_INDEX_KEY_DIRECTION=cursor.getColumnIndex(COLUMN_NAME_KEY_DIRECTION);
	
		
	}

	public static ArrayList<GPSBean> getData(Context ctx) 
	{
		ArrayList<GPSBean> references = new ArrayList<GPSBean>();
		DBHelper dbHelper = DBHelper.getInstance(ctx);
		synchronized (dbHelper) {
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null,	null);
			if (cursor != null && cursor.getCount() > 0)
			{
				//setColumns(cursor);
				for (int i = 0; i < cursor.getCount(); i++) 
				{
					try{
					cursor.moveToPosition(i);
					//GPS reference = new GPS(cursor.getString(COLUMN_INDEX_KEY_LATITUDE));
					GPSBean reference=new GPSBean(cursor.getString(COLUMN_INDEX_KEY_LATITUDE),cursor.getString(COLUMN_INDEX_KEY_LONGITUDE),cursor.getString(COLUMN_INDEX_KEY_DIRECTION),cursor.getString(COLUMN_INDEX_KEY_TIMESTAMP),cursor.getString(COLUMN_INDEX_KEY_ROWID));
					
					references.add(reference);
					}catch(Exception e)
					{
						////System.out.println("Database error" + e);
					}
				}
			}
			cursor.close();
			db.close();
		}

		return references;
	}
	public static boolean insertReferences(Context ctx,GPSBean referencePojo)
	{
		long status = 0;
		DBHelper dbHelper = DBHelper.getInstance(ctx);
		synchronized (dbHelper) {
			try{
			
			SQLiteDatabase db = dbHelper.getWritableDatabase();
	
				ContentValues values = new ContentValues();		
				
				
				values.put(COLUMN_NAME_KEY_LATITUDE,referencePojo.getlat());
				values.put(COLUMN_NAME_KEY_LONGITUDE ,referencePojo.getLon());
				values.put(COLUMN_NAME_KEY_TIMESTAMP,referencePojo.getdate());
				values.put(COLUMN_NAME_KEY_DIRECTION, referencePojo.getdir());
				
				
				status=db.insert(TABLE_NAME, null, values);
				Log.e("Tag", "values saving in db="+referencePojo.getlat()+"lon= "+referencePojo.getLon());
			

			db.close();
			
			}catch(Exception e){
				System.out.println("error in insertReferences: "+ e);
			}
		}
		return status > 0;
	}
	
}
