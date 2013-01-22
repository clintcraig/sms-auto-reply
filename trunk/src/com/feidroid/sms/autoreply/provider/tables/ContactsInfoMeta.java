package com.feidroid.sms.autoreply.provider.tables;

import android.net.Uri;
import android.provider.BaseColumns;

public class ContactsInfoMeta implements BaseColumns {
	
		public static final String TABLENAME = "contactsinfo";
		// Authority constant
		public static final String AUTHORITY = "com.feidroid.sms.autoreply.provider.contactsinfo.auth";

		// Access URI
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/contactsinfo");

		// Default sort constant
		public static final String DEFAULT_SORT_ORDER = "_ID DESC";// order by jobId
		
		public static final String CONTACTSINFO_ID = "contacts_id";
		public static final String CONTACTSINFO_DISPLAYNAME = "contacts_displayname";
		public static final String CONTACTSINFO_PHONENUMS = "contacts_phonenums";
		//public static final String CONTACTSINFO_PHOTOID = "contacts_photoid";
		public static final String CONTACTSINFO_SHOULDREPLY = "contacts_shouldreply";
		public static final String CONTACTSINFO_EMAILS = "contacts_emails";
		public static final String CONTACTSINFO_IMS = "contacts_im";
}
