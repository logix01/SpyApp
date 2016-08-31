package com.asynctask.httpconnect;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {

	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";
	
	
	

	// constructor
	public JSONParser() {

	}

	// function get json from url
	// by making HTTP POST or GET mehtod
	public JSONObject makeHttpRequest(String url, String method,
			JSONObject params) {
	
		String jsonString = null;
		
	
		// Making HTTP request
		try {

			// check for request method
			if (method == "POST") {

				DefaultHttpClient httpClient = new DefaultHttpClient();
				 HttpConnectionParams.setSoTimeout(httpClient.getParams(), 60*1000); 
                 HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),60*1000); 
               
				HttpPost httpPost = new HttpPost(url);				
				httpPost.setEntity(new StringEntity(params.toString()));
				//System.out.println("Josn data param:"+params.toString());
				httpPost.setHeader("Accept", "application/json");
				httpPost.setHeader("Content-type", "application/json");
			
				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();

				try {
					jsonString = EntityUtils.toString(httpEntity);
					Log.e("XXX", jsonString);
					} catch (Exception e) {
						//System.out.println("eeeeeeeeeeeeeeeeeeeee:"+e);
					e.printStackTrace();
					
				}
				Log.i("postData", httpResponse.getStatusLine().toString());

				Log.d("WebInvoke", "Saving : "+ httpResponse.getStatusLine().getStatusCode());

			
			} else if (method == "GET") {
				// request method is GET
				DefaultHttpClient httpClient = new DefaultHttpClient();

				HttpConnectionParams.setSoTimeout(httpClient.getParams(),2 * 60 * 1000);
				HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 2 * 60 * 1000);
				HttpGet httpGet = new HttpGet(url);
				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
			}

		} catch (UnsupportedEncodingException e) {
			//System.out.println("UnsupportedEncodingException " + e);
			jsonString="{\"ok\":\"ok\"}";
		
		} catch (ClientProtocolException e) {
			//System.out.println("ClientProtocolException " + e);
			jsonString="{\"ok\":\"ok\"}";
		
		} catch (HttpHostConnectException e) {
			//System.out.println("CONNECT " + e);
			jsonString="{\"ok\":\"ok\"}";
		
		} 
		catch (NullPointerException e) {
			//System.out.println("NULL POINTER " + e);
			
			jsonString="{\"ok\":\"ok\"}";
		} 
		catch (final IllegalArgumentException	e) {
			//System.out.println("IllegalArgumentException " + e);
			jsonString="{\"ok\":\"ok\"}";
		
		}catch (Exception e) {
			//System.out.println("IOException " + e);
			jsonString="{\"ok\":\"ok\"}";
		
		}


		// try parse the string to a JSON object
		try {
			//System.out.println("json data is:"+jsonString);
			jObj = new JSONObject(jsonString);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
			jsonString="{\"ok\":\"ok\"}";
			try {
				jObj=new JSONObject(jsonString);
			} catch (JSONException e1) {

				e1.printStackTrace();
			}
		
		}

		// return JSON String
		// return jObj;

		return jObj;

	}
	
	
}