package com.feidroid.sms.autoreply.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.Data;

import com.feidroid.sms.autoreply.R;
import com.feidroid.sms.autoreply.contants.Contants;
import com.feidroid.sms.autoreply.entity.ContactsInfoBean;
import com.feidroid.sms.autoreply.provider.tables.ContactsInfoMeta;

public class ContactsUtil {

	public static ArrayList<Map<String, Object>> _setContactsData(
			Context context) {

		ArrayList<Map<String, Object>> contactsData = new ArrayList<Map<String, Object>>();
		// Get a cursor with all phones

		Cursor c  = null;
		try{
			c= context.getContentResolver().query(
					ContactsContract.Contacts.CONTENT_URI,
					null,
					null,
					null,
					ContactsContract.Contacts.DISPLAY_NAME
					+ " COLLATE LOCALIZED ASC");
			/* startManagingCursor(c); */
			while (c != null && c.moveToNext()) {
				String displayName = c.getString(c
						.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				String _id = c.getString(c
						.getColumnIndex(ContactsContract.Contacts._ID));
				StringBuilder phoneNums = new StringBuilder();
				int phoneCount = c
						.getInt(c
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER));
				if (phoneCount > 0) {
					Cursor phones = context.getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
							+ " = " + _id, null, null);
					
					if(phones != null){
						
						while (phones.moveToNext()) {
							String phoneno = phones
									.getString(phones
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							phoneNums.append(phoneno).append(Contants.IncentDelimiter);
						}
					}
				}
				// 获取该联系人邮箱
				StringBuilder emailsString = new StringBuilder();
				Cursor emailsCursor = context.getContentResolver().query(
						ContactsContract.CommonDataKinds.Email.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
								+ _id, null, null);
				while (emailsCursor.moveToNext()) {
					String email = emailsCursor
							.getString(emailsCursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
					emailsString.append(email).append(Contants.IncentDelimiter);
				}
				
				// 获取该联系人IM
				String imString = "";
				Cursor IMs = context.getContentResolver().query(
						Data.CONTENT_URI,
						new String[] { Data._ID, Im.PROTOCOL, Im.DATA },
						Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='"
								+ Im.CONTENT_ITEM_TYPE + "'", new String[] { _id },
								null);
				while (IMs.moveToNext()) {
					imString = IMs.getString(IMs.getColumnIndex(Im.DATA));
				}
				
				// System.out.println(Contants.DEBUG+" phones  ----> "+
				// phoneNums.toString());
				long photo_id = c.getLong(c.getColumnIndex(Photo.PHOTO_ID));
				Bitmap photoBmp = null;
				/* photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的 */
				if (photo_id > 0) {
					
					Uri uri = ContentUris.withAppendedId(
							ContactsContract.Contacts.CONTENT_URI,
							c.getColumnIndex(ContactsContract.Contacts._ID));
					
					InputStream input = ContactsContract.Contacts
							.openContactPhotoInputStream(
									context.getContentResolver(), uri);
					if (null != input) {
						photoBmp = BitmapFactory.decodeStream(input);
					}
				} else {
					photoBmp = BitmapFactory.decodeResource(context.getResources(),
							R.drawable.icon48x48_1);
				}
				
				Map<String, Object> contactsMap = new HashMap<String, Object>();
				contactsMap.put(Contants.KEY_CONTACTS_DISPLAYNAME, displayName);
				// System.out.println(Contants.DEBUG+"  _id -------> "+_id);
				contactsMap.put(Contants.KEY_CONTACTS_ID, _id);
				if (phoneNums != null && phoneNums.toString().length() > 0) {
					
					contactsMap.put(Contants.KEY_CONTACTS_PHONENUMS,
							phoneNums.deleteCharAt(phoneNums.length()-1).toString());
				} else {
					
				}
				contactsMap.put(Contants.KEY_CONTACTS_PHOTOBMP, photoBmp);
				//初始化checkbox,如果勾选true，但是contactsinfo表中的contacts_shouldreply='false'
				//所以默认，不勾选，contacts_shouldreply='true';
				boolean exist = _existByContactsId(_id,context);
				if(exist){
					System.out.println(Contants.DEBUG 
							+ " exist 2  -------> " + exist);
					contactsMap.put(Contants.KEY_CONTACTS_ISCHECKED, "true".equals(_getContactsShouldreply(_id,context))?false:true);
					
				}else{
					
					contactsMap.put(Contants.KEY_CONTACTS_ISCHECKED, false);
				}
				contactsMap.put(Contants.KEY_CONTACTS_EMAILS,
						emailsString.toString());
				contactsMap.put(Contants.KEY_CONTACTS_IMS, imString);
				contactsData.add(contactsMap);
			}
			
		}catch(Exception e){
			
		}finally{
			c.close();
		}
		return contactsData;
	}

	

	public static ArrayList<Map<String, Object>> _getAllPhoneNums(Context context){
		ArrayList<Map<String, Object>>  list = new ArrayList<Map<String, Object>>();
		// Get a cursor with all phones
		Cursor c = context.getContentResolver().query(
				ContactsInfoMeta.CONTENT_URI, 
				null,null,null,null);
		//System.out.println(Contants.DEBUG+" c -----> "+c);
		while(c != null && c.moveToNext()){
			String phoneNums = c.getString(c.getColumnIndex(ContactsInfoMeta.CONTACTSINFO_PHONENUMS));
			String shouldReply = c.getString(c.getColumnIndex(ContactsInfoMeta.CONTACTSINFO_SHOULDREPLY));
			Map<String, Object>  map = new HashMap<String, Object>();
			map.put(ContactsInfoMeta.CONTACTSINFO_PHONENUMS, phoneNums);
			map.put(ContactsInfoMeta.CONTACTSINFO_SHOULDREPLY, shouldReply);
			list.add(map);
		}
		System.out.println(Contants.DEBUG+" all phone nums -----> "+list);
		return list;
	}
	
	
	public static ArrayList<Map<String, Object>> _setContactsIdAndDisplayName(
			Context context) {
		ArrayList<Map<String, Object>> contactsData = new ArrayList<Map<String, Object>>();
		// Get a cursor with all phones

		Cursor c = context.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI,
				new String[]{ContactsContract.Contacts._ID,ContactsContract.Contacts.DISPLAY_NAME,Photo.PHOTO_ID},
				null,
				null,
				ContactsContract.Contacts.DISPLAY_NAME
						+ " COLLATE LOCALIZED ASC");
		/* startManagingCursor(c); */
		while (c.moveToNext()) {
			String displayName = c.getString(c
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			String _id = c.getString(c
					.getColumnIndex(ContactsContract.Contacts._ID));
			long photo_id = c.getLong(c.getColumnIndex(Photo.PHOTO_ID));
			Bitmap photoBmp = null;
			/* photoid >0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的 */
			if (photo_id > 0) {
				Uri uri = ContentUris.withAppendedId(
						ContactsContract.Contacts.CONTENT_URI,
						c.getColumnIndex(ContactsContract.Contacts._ID));

				InputStream input = ContactsContract.Contacts
						.openContactPhotoInputStream(
								context.getContentResolver(), uri);
				if (null != input) {
					photoBmp = BitmapFactory.decodeStream(input);
				}
			} else {
				photoBmp = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.icon48x48_1);
			}

			Map<String, Object> contactsMap = new HashMap<String, Object>();
			contactsMap.put(Contants.KEY_CONTACTS_ID, _id);
			contactsMap.put(Contants.KEY_CONTACTS_DISPLAYNAME, displayName);
			contactsMap.put(Contants.KEY_CONTACTS_PHOTOBMP, photoBmp);
			//初始化checkbox,如果勾选true，但是contactsinfo表中的contacts_shouldreply='false'
			//所以默认，不勾选，contacts_shouldreply='true';
			boolean exist = _existByContactsId(_id,context);
			if(exist){
				System.out.println(Contants.DEBUG 
						+ " exist 1  -------> " + exist);
				contactsMap.put(Contants.KEY_CONTACTS_ISCHECKED, "true".equals(_getContactsShouldreply(_id,context))?false:true);
				
			}else{
				
				contactsMap.put(Contants.KEY_CONTACTS_ISCHECKED, false);
			}
			contactsData.add(contactsMap);
		}
		return contactsData;
	}

	public static boolean _existByContactsId(String contacts_id,
			Context context) {
		// TODO Auto-generated method stub
		boolean exist = false;
		Cursor c = context.getContentResolver().query(
				ContactsInfoMeta.CONTENT_URI, 
				null,
				ContactsInfoMeta.CONTACTSINFO_ID + "=?",
				new String[] { contacts_id+"" }, null);
		
		if( c != null  &&  c.getCount()>0){
			exist = true;	
		}
		
		return exist;
	}
	private static String _getContactsShouldreply(String contacts_id, Context context) {
		String valueString = "";
		Cursor c = context.getContentResolver().query(
				ContactsInfoMeta.CONTENT_URI, 
				new String[]{ContactsInfoMeta.CONTACTSINFO_SHOULDREPLY},
				ContactsInfoMeta.CONTACTSINFO_ID + "=?",
				new String[] { contacts_id+"" }, null);
		while(c.moveToNext()){
			valueString = c.getString(c.getColumnIndex(ContactsInfoMeta.CONTACTSINFO_SHOULDREPLY));
		}
		//System.out.println(Contants.DEBUG+" _id="+contacts_id+",valueString="+valueString);
		return valueString;
	}
}
