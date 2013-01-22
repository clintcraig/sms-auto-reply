package com.feidroid.sms.autoreply.datebase;


import com.feidroid.sms.autoreply.contants.Contants;
import com.feidroid.sms.autoreply.provider.tables.ContactsInfoMeta;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	// private SQLiteDatabase db;
	private static int miscount = 0;
	// DB Name constant
	public static final String DATABASE_NAME = "smsautoreply.db";
	// DB Version constant
	private static final int DATABASE_VERSION = 4;
	// Table name constant

	private final Context myContext;

	// constructor
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.myContext = context;
	}

	// Call while DB is created
	@Override
	public void onCreate(SQLiteDatabase db) {

		// create table job
		String sql = "CREATE TABLE " + ContactsInfoMeta.TABLENAME + " ("
				+ ContactsInfoMeta._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ ContactsInfoMeta.CONTACTSINFO_ID + " NUMERIC, "
				+ ContactsInfoMeta.CONTACTSINFO_DISPLAYNAME + " TEXT, "
				+ ContactsInfoMeta.CONTACTSINFO_PHONENUMS + " TEXT, "
				+ ContactsInfoMeta.CONTACTSINFO_SHOULDREPLY + " TEXT,"
				+ ContactsInfoMeta.CONTACTSINFO_EMAILS + " TEXT,"
				+ ContactsInfoMeta.CONTACTSINFO_IMS + " TEXT);";
		db.execSQL(sql);
		// create index for ContactsInfoMeta
		sql = "CREATE INDEX idx_contactsinfo ON " + ContactsInfoMeta.TABLENAME
				+ "(" + ContactsInfoMeta._ID + " ASC);";
		db.execSQL(sql);
	}


	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS"+ContactsInfoMeta.TABLENAME);
		onCreate(db);
	}
	

}
