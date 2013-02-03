package com.feidroid.sms.autoreply.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;

import com.feidroid.sms.autoreply.contants.Contants;

public class SMSUtil {

	private static final String DEFAULT_SMS_QUERY_SORT_ORDER = "date desc";//order by date desc
	
	public static ArrayList<Map<String, Object>> getSMS(Context context, Uri uri) {

		ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		//query _id,address(phonenumber),body(smsbody),date
		String[] projection = new String[] { "_id", "address",
				// "person",
				"body", 
				"date",
				// "type"
			};
		Cursor cusor = context.getContentResolver().query(uri, projection,
				null, null, SMSUtil.DEFAULT_SMS_QUERY_SORT_ORDER);
		// int nameColumn = cusor.getColumnIndex("person");
		int phoneNumberColumn = cusor.getColumnIndex("address");
		int smsbodyColumn = cusor.getColumnIndex("body");
		int dateColumn = cusor.getColumnIndex("date");
		// int typeColumn = cusor.getColumnIndex("type");
		ArrayList<String> listRepeat = new ArrayList<String>();
		if (cusor != null) {
			while (cusor.moveToNext()) {
				String date = cusor.getString(dateColumn);

				Map<String, Object> map = new HashMap<String, Object>();
				String phoneNumber = cusor.getString(phoneNumberColumn);
				/*
				 * String name = cusor.getString(nameColumn);
				 * 返回的是int，不是Phone.DisplayName
				 */
				
				String name = getPeopleNameFromPerson(context, phoneNumber) != null
						&& getPeopleNameFromPerson(context, phoneNumber)
								.length() > 0 ? getPeopleNameFromPerson(
						context, phoneNumber) : phoneNumber;

				String smsbody = cusor.getString(smsbodyColumn);
				int counts = getCountsByPhoneNumber(context, phoneNumber, uri);
				/*
				 * System.out.println( Contants.DEBUG+" name -- "+name+"\n"+
				 * Contants.DEBUG+" smsbody -- "+smsbody+"\n"+
				 * Contants.DEBUG+" date -- "+date+"\n"+
				 * Contants.DEBUG+" phoneNumber -- "+phoneNumber+"\n" );
				 */
				if (listRepeat != null && listRepeat.contains(phoneNumber)) {
					Log.d(Contants.DEBUG, " don't put ");
				} else {
					listRepeat.add(phoneNumber);
					map.put(Contants.KEY_SMS_NAME_AND_COUNT,
							(name != null ? name : phoneNumber) + " (" + counts
									+ ")");
					map.put(Contants.KEY_SMS_LAST_CONTENT, smsbody);
					map.put(Contants.KEY_SMS_DATE, DateUtils.long2String(
							Long.valueOf(date),
							DateUtils.TRADITIONAL_DATE_FORMAT));
					data.add(map);
				}

			}
			cusor.close();
		}
		System.out.println(Contants.DEBUG + " listview adapter data size  ----- " + data.size());
		return data;

	}

	
	/**
	 * @param context
	 * @param address
	 * @return displayname(it is contact's name)'
	 * @author feidroid
	 * @since 2013/1/26
	 */
	private static String getPeopleNameFromPerson(Context context,
			String address) {
		String strPerson = "";
		Cursor cursor = null;
		try{
			
			if ( address != null && address.length() > 0 ) {
			
				Uri uri_Person = Uri.withAppendedPath(
						ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI,
						address); //content filter 
				cursor = context.getContentResolver().query(uri_Person,
						new String[] { Phone.DISPLAY_NAME, Phone.NUMBER }, null, null, null);
				
				
				if ( cursor != null && cursor.moveToFirst()) {
					strPerson = cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME));
				}
			}
			
		}catch (Exception e) {
			// TODO: handle exception
		}finally{
			if(cursor != null){
				cursor.close();
			}
		}
		return strPerson;
	}

	/**
	 * @param context
	 * @param phoneNumber
	 * @param uri
	 * @return smsCounts
	 */
	private static int getCountsByPhoneNumber(Context context,
			String phoneNumber, Uri uri) {
		int count = 0;
		Cursor cursor = null;
		try{
			cursor = context.getContentResolver().query(uri, null,
				"address = ?", new String[] { phoneNumber }, "date desc");
			if (cursor != null) {
				count = cursor.getCount();
			}
		}catch (Exception e) {
			// TODO: handle exception
		}finally{
			if(cursor != null){
				cursor.close();
			}
		}
		Log.d(Contants.DEBUG, phoneNumber+" has SMS counts ----- " + count);
		return count;
	}

}
