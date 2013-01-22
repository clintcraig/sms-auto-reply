package com.feidroid.sms.autoreply.util;

import android.content.ContentValues;

import com.feidroid.sms.autoreply.entity.ContactsInfoBean;
import com.feidroid.sms.autoreply.provider.tables.ContactsInfoMeta;

public class GetContentValuesFromBean {

	public static ContentValues getContactsValues(ContactsInfoBean cib) {
		ContentValues values = new ContentValues();
		
		//values.put(ContactsInfoMeta._ID, cib.getContactId());  _id 
		values.put(ContactsInfoMeta.CONTACTSINFO_ID, cib.getContactId());  //contact_id = _id(from contacts _id);
		values.put(ContactsInfoMeta.CONTACTSINFO_DISPLAYNAME, cib.getContactDisplayName());
		values.put(ContactsInfoMeta.CONTACTSINFO_PHONENUMS, cib.getContactPhoneNums());
		values.put(ContactsInfoMeta.CONTACTSINFO_SHOULDREPLY, cib.getContactshouldRely());
		values.put(ContactsInfoMeta.CONTACTSINFO_EMAILS, cib.getContactEmail());
		values.put(ContactsInfoMeta.CONTACTSINFO_IMS, cib.getContactIMs());
		return values;
	}
}
