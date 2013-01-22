package com.feidroid.sms.autoreply.entity;

import java.io.Serializable;

public class ContactsInfoBean implements Serializable{

	public String getContactId() {
		return contactId;
	}
	public void setContactId(String contactId) {
		this.contactId = contactId;
	}
	public String getContactDisplayName() {
		return contactDisplayName;
	}
	public void setContactDisplayName(String contactDisplayName) {
		this.contactDisplayName = contactDisplayName;
	}
	public String getContactPhoneNums() {
		return contactPhoneNums;
	}
	public void setContactPhoneNums(String contactPhoneNums) {
		this.contactPhoneNums = contactPhoneNums;
	}
	public String getContactshouldRely() {
		return contactshouldRely;
	}
	public void setContactshouldRely(String contactshouldRely) {
		this.contactshouldRely = contactshouldRely;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	public String getContactIMs() {
		return contactIMs;
	}
	public void setContactIMs(String contactIMs) {
		this.contactIMs = contactIMs;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String contactId;
	private String contactDisplayName;
	private String contactPhoneNums;
	private String contactshouldRely;
	private String contactEmail;
	private String contactIMs;
	
}
