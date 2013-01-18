package com.feidroid.sms.autoreply.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.ListAdapter;

import com.feidroid.sms.autoreply.R;
import com.feidroid.sms.autoreply.contants.Contants;
import com.feidroid.sms.autoreply.service.SMSReceivedService;

public class SettingsActivity extends PreferenceActivity {


	private boolean isAutoListening = false;
	private boolean isAddPostfixToMessage = false;
	
	private int entryValue = 0;

	private Intent serviceIntent;

	private CheckBoxPreference startListeningP = null;
	private EditTextPreference autoReplyEP = null;
	private ListPreference listPreferenceTimer = null;
	
	public static String AUTO_REPLY_CONTENT = "AUTO_REPLY_CONTENT"; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.config_preferences);

		ListAdapter adapter = this.getPreferenceScreen().getRootAdapter();

		for (int idx = 0; idx < adapter.getCount(); idx++) {
			Object object = adapter.getItem(idx);
			// CheckBoxPreference
			if (object instanceof CheckBoxPreference) {
				if (((CheckBoxPreference) object).getKey().equals(
						getResources().getString(R.string.key_is_listening))) {
						_setStartListening(object);
				} 
				/*else if (((CheckBoxPreference) object).getKey().equals(
						getResources().getString(
								R.string.key_add_postfix_to_message))) {
					_setAddPostfix(object);
				}*/
			} //EditTextPreference default auto content
			else if (object instanceof EditTextPreference) {
				if(  getResources().getString(R.string.key_set_keywords)
						.equals(((EditTextPreference)object).getKey())    
				){
					System.out.println(Contants.DEBUG+" set keywords");
				}else if(getResources().getString(R.string.key_auto_reply_message)
						.equals(((EditTextPreference)object).getKey())){
					_getAutoReplyContent(object);
				}
			}//ListPreference
			else if(object instanceof ListPreference){
				_getTimer();
			}
		}
		
		//判断开机后，服务是否开启
		isAutoListening = SMSReceivedService.isOnStart;
		System.out.println(Contants.DEBUG+ " isAutoListening ---> "+isAutoListening);
		if(isAutoListening){
			startListeningP.setChecked(true);
		}
		
		serviceIntent = new Intent(SettingsActivity.this,SMSReceivedService.class);
		if (startListeningP != null) {
			startListeningP.setOnPreferenceClickListener(new OnPreferenceClickListener(){

				@Override
				public boolean onPreferenceClick(Preference preference) {
					
					boolean isChecked = startListeningP.isChecked();
					System.out.println("isChecked ---> "+isChecked);
					if(!isChecked){
						Log.d(Contants.DEBUG, " Stop SMSReceivedService.");
						stopService(serviceIntent);
					}else{
						Log.d(Contants.DEBUG, " Start SMSReceivedService.");
						startService(serviceIntent);
					}
					
					return true;
				}
			});
		}
		
		
		
	}

	private void _getTimer() {
		
	}

	public   String autoReplyContent = "";
	private    String _getAutoReplyContent(Object object) {
		autoReplyEP = (EditTextPreference) object;
		
			
			autoReplyContent = autoReplyEP.getText();
			Log.d(Contants.DEBUG, " default_auto_reply_content ---> "
					+ autoReplyContent);
		return autoReplyContent;
	}
	
	/*private void _setAddPostfix(Object object) {
		boolean defaultValue = ((CheckBoxPreference) object)
				.getSharedPreferences()
				.getBoolean(
						getResources()
								.getString(
										R.string.key_add_postfix_to_message),
						true);
		isAddPostfixToMessage = defaultValue;
		Log.d(Contants.DEBUG, " Add postfix  to message  default value ---> "
				+ defaultValue);
		
	}*/

	private void _setStartListening(Object object) {
		startListeningP = (CheckBoxPreference) object;
	}

	
}
