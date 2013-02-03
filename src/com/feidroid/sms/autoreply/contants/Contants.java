package com.feidroid.sms.autoreply.contants;

public class Contants {

	public static final String DEBUG = " [ SMSAutoReply -- Debug ] ";

	public static final String INFO = " [ SMSAutoReply -- Info ] ";

	public static final String ERROR = " [ SMSAutoReply -- Error ] ";

	public static final String IncentDelimiter = "| ";

	public static final String DEFAULTPHONE = "18046648620";

	public static final String KEY_CONTACTS_DISPLAYNAME = "KEY_CONTACTS_DISPLAYNAME";

	public static final String KEY_CONTACTS_ID = "KEY_CONTACTS_ID";

	public static final String KEY_CONTACTS_PHOTOBMP = "KEY_CONTACTS_PHOTOBMP";

	public static final String KEY_CONTACTS_PHONENUMS = "KEY_CONTACTS_PHONENUMS";

	public static final String KEY_CONTACTS_ISCHECKED = "KEY_CONTACTS_ISCHECKED";

	public static final String KEY_CONTACTS_EMAILS = "KEY_CONTACTS_EMAILS";

	public static final String KEY_CONTACTS_IMS = "KEY_CONTACTS_IMS";

	/**
	 * 所有的短信
	 */
	public static final String SMS_URI_ALL = "content://sms/";
	/**
	 * 收件箱短信
	 */
	public static final String SMS_URI_INBOX = "content://sms/inbox";
	/**
	 * 发件箱短信
	 */
	public static final String SMS_URI_SEND = "content://sms/sent";
	/**
	 * 草稿箱短信
	 */
	public static final String SMS_URI_DRAFT = "content://sms/draft";

	public static final String KEY_SMS_NAME_AND_COUNT = "KEY_SMS_NAME_AND_COUNT";

	public static final String KEY_SMS_DATE = "KEY_SMS_DATE";

	public static final String KEY_SMS_LAST_CONTENT = "KEY_SMS_LAST_CONTENT";

	public static final String KEY_LISTVIEW_DATA = "KEY_LISTVIEW_DATA";

	public static final String KEY_ADAPTER_ITEM_ID = "KEY_ADAPTER_ITEM_ID";

	public static final String VM_SIM_IMSI = "vm_sim_imsi_key";// sim_imsi form
																// GSMPhhone.java

	//from com.android.internal.telephony.gsm.SIMRecords.java
	//***** Event Constants

	public static final int EVENT_SIM_READY = 1;
	public static final int EVENT_RADIO_OFF_OR_NOT_AVAILABLE = 2;
	public static final int EVENT_GET_IMSI_DONE = 3;
	public static final int EVENT_GET_ICCID_DONE = 4;
	public static final int EVENT_GET_MBI_DONE = 5;
	public static final int EVENT_GET_MBDN_DONE = 6;
	public static final int EVENT_GET_MWIS_DONE = 7;
	public static final int EVENT_GET_VOICE_MAIL_INDICATOR_CPHS_DONE = 8;
	public static final int EVENT_GET_AD_DONE = 9; // Admin data on SIM
	public static final int EVENT_GET_MSISDN_DONE = 10;
	public static final int EVENT_GET_CPHS_MAILBOX_DONE = 11;
	public static final int EVENT_GET_SPN_DONE = 12;
	public static final int EVENT_GET_SPDI_DONE = 13;
	public static final int EVENT_UPDATE_DONE = 14;
	public static final int EVENT_GET_PNN_DONE = 15;
	public static final int EVENT_GET_SST_DONE = 17;
	public static final int EVENT_GET_ALL_SMS_DONE = 18;
	public static final int EVENT_MARK_SMS_READ_DONE = 19;
	public static final int EVENT_SET_MBDN_DONE = 20;
	public static final int EVENT_SMS_ON_SIM = 21;
	public static final int EVENT_GET_SMS_DONE = 22;
	public static final int EVENT_GET_CFF_DONE = 24;
	public static final int EVENT_SET_CPHS_MAILBOX_DONE = 25;
	public static final int EVENT_GET_INFO_CPHS_DONE = 26;
	public static final int EVENT_SET_MSISDN_DONE = 30;
	public static final int EVENT_SIM_REFRESH = 31;
	public static final int EVENT_GET_CFIS_DONE = 32;

}
