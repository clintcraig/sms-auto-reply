package com.feidroid.sms.autoreply.provider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.feidroid.sms.autoreply.contants.Contants;
import com.feidroid.sms.autoreply.datebase.DBHelper;
import com.feidroid.sms.autoreply.provider.tables.ContactsInfoMeta;

public class ContactsInfoProvider extends ContentProvider {

	private DBHelper dbHelper;
	
	//the code that is returned when a URI is matched against the given components. 
	private static final int CONTACTSINFO = 1;
	private static final int CONTACTSINFO_ID = 2;

	//the object use to mach the uri in content provider
	private static final UriMatcher mUriMatch;
	
	private static HashMap<String, String> contactsInfoProjections;
	
	static{
		mUriMatch = new UriMatcher(UriMatcher.NO_MATCH);
		mUriMatch.addURI(ContactsInfoMeta.AUTHORITY, "contactsinfo", CONTACTSINFO);
		mUriMatch.addURI(ContactsInfoMeta.AUTHORITY, "contactsinfo/#", CONTACTSINFO_ID);
		
		contactsInfoProjections = new HashMap<String, String>();
	
		contactsInfoProjections.put(ContactsInfoMeta._ID, ContactsInfoMeta._ID);
		//contactsInfoProjections.put(ContactsInfoMeta.CONTACTSINFO_ID, ContactsInfoMeta.CONTACTSINFO_ID);
		contactsInfoProjections.put(ContactsInfoMeta.CONTACTSINFO_DISPLAYNAME, ContactsInfoMeta.CONTACTSINFO_DISPLAYNAME);
		contactsInfoProjections.put(ContactsInfoMeta.CONTACTSINFO_PHONENUMS, ContactsInfoMeta.CONTACTSINFO_PHONENUMS);
		contactsInfoProjections.put(ContactsInfoMeta.CONTACTSINFO_SHOULDREPLY, ContactsInfoMeta.CONTACTSINFO_SHOULDREPLY);
		contactsInfoProjections.put(ContactsInfoMeta.CONTACTSINFO_EMAILS, ContactsInfoMeta.CONTACTSINFO_EMAILS);
		contactsInfoProjections.put(ContactsInfoMeta.CONTACTSINFO_IMS, ContactsInfoMeta.CONTACTSINFO_IMS);
	}
	
	
	
	
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int result = db.delete(ContactsInfoMeta.TABLENAME, selection, selectionArgs);
		return result;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long rowId = db.insert(ContactsInfoMeta.TABLENAME, null, values);
		if(rowId>0){
			Uri tempUri = ContentUris.withAppendedId(ContactsInfoMeta.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(tempUri, null);
			return tempUri;
		}
		
		return null;//if return null means that the insert operation is failure
	}

	@Override
	public boolean onCreate() {
		dbHelper = new DBHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
		switch(mUriMatch.match(uri)){
		case CONTACTSINFO:
			sqlBuilder.setTables(ContactsInfoMeta.TABLENAME);
			sqlBuilder.setProjectionMap(contactsInfoProjections);
			break;
		case CONTACTSINFO_ID:
			sqlBuilder.setTables(ContactsInfoMeta.TABLENAME);
			sqlBuilder.setProjectionMap(contactsInfoProjections);
			sqlBuilder.appendWhere(ContactsInfoMeta._ID +  "=" + uri.getPathSegments().get(1));
			break;
		}
		// sort by 
		String orderBy;
		if(TextUtils.isEmpty(sortOrder)){
			orderBy = ContactsInfoMeta.DEFAULT_SORT_ORDER;
		}else{
			orderBy = sortOrder;
		}
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = sqlBuilder.query(db, projection, selection, selectionArgs, null, null, orderBy);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
		switch(mUriMatch.match(uri)){
		case CONTACTSINFO:
			sqlBuilder.setTables(ContactsInfoMeta.TABLENAME);
			sqlBuilder.setProjectionMap(contactsInfoProjections);
			break;
		case CONTACTSINFO_ID:
			sqlBuilder.setTables(ContactsInfoMeta.TABLENAME);
			sqlBuilder.setProjectionMap(contactsInfoProjections);
			sqlBuilder.appendWhere(ContactsInfoMeta._ID +  "=" + uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Uri error: " + uri);
		}
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int result = db.update(sqlBuilder.getTables(), values, selection, selectionArgs);
		return result;
	}

}
