package com.feidroid.sms.autoreply.activity;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Window;
import android.widget.TabHost;

import com.feidroid.sms.autoreply.R;
import com.feidroid.sms.autoreply.contants.Contants;
import com.feidroid.sms.autoreply.util.SettingsManager;

/**
 * @author feidroid
 * @since 2013-1-16
 */
public class SMSAutoReplyActivity extends TabActivity {
	private TabHost tabHost = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		setTabView();

		SettingsManager manager = SettingsManager.getInstance(this);
		/* Checks if the user runs the system for the very first time! */
		// System.out.println(Contants.DEBUG
		// +"  the first time running the SMSAutoReplier  --->  "+manager.isFirstTimeLaunch());
		if (manager.isFirstTimeLaunch()) {

			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle(this.getResources().getString(
					R.string.quick_tutorial_title));
			dialog.setMessage(R.string.quick_tutorial_content);
			dialog.setPositiveButton(
					this.getResources().getString(
							R.string.quick_tutorial_confirm),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

						}
					});

			dialog.show();
			manager.setFirstTimeLaunch(false); // not the first time!
		}

		//getIMSI();
	}


	private String getSimType() {
		// 获得SIMType 　　
		String simType = "";
		// 获得系统服务，从而取得sim数据 　　
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE); 
		int type = tm.getNetworkType();
		System.out.println(Contants.DEBUG+" NetWorkType:"+type);
		//Test1:我的手机卡是联通USIM卡，在这儿取出来的值为10-------NETWORK_TYPE_HSPA
	    //Test2:中国移动 神州行OTA 2-----NETWORK_TYPE_EDGE
		//Test3:中国移动 动感地带OTA 2-----NETWORK_TYPE_EDGE
		//...

		switch (type) {
			case TelephonyManager.NETWORK_TYPE_UNKNOWN://0
				simType= "UNKOWN";
				break;
			case TelephonyManager.NETWORK_TYPE_GPRS://1
				simType= "SIM";
				break;
			case TelephonyManager.NETWORK_TYPE_EDGE://2
				simType= "SIM";
				break;
			case TelephonyManager.NETWORK_TYPE_UMTS://3
				simType= "USIM";
				break;
			case TelephonyManager.NETWORK_TYPE_CDMA://4
				simType= "UIM";
				break;
			case TelephonyManager.NETWORK_TYPE_EVDO_0://5
				simType= "UIM";
				break;
			case TelephonyManager.NETWORK_TYPE_EVDO_A://6
				simType= "UIM";
				break;
			case TelephonyManager.NETWORK_TYPE_1xRTT://7
				simType= "UIM";
				break;
			case TelephonyManager.NETWORK_TYPE_HSDPA://8
				simType= "HSDPA Card";
				break;
			case TelephonyManager.NETWORK_TYPE_HSUPA://9
				simType= "HSUPA Card";
				break;
			case TelephonyManager.NETWORK_TYPE_HSPA://10
				simType= "USIM";
				break;
			
		}
		System.out.println(Contants.DEBUG+" sim type :"+simType);
		return simType;
	}
	/**
	 * @author Administrator
	 * @version 2013-01-30
	 * @category IMSI(15位) = MCC(3位) + MNC(2位) + MSIN(10位)
	 */
	private void getIMSI() {
		getSimType();
		String imsi = "";
		// GSM PHONE ---> sim
		TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		imsi = tManager.getSubscriberId();
		// CDMA PHONE --> ruim
		
	}

	private void setTabView() {
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();

		TabHost.TabSpec specSettings = tabHost.newTabSpec("tagSettings");
		specSettings.setContent(new Intent(this, SettingsActivity.class));
		specSettings.setIndicator(getString(R.string.tab_settings_title)

		, getResources().getDrawable(android.R.drawable.ic_menu_preferences));

		tabHost.addTab(specSettings);

	/*	TabHost.TabSpec specToReply = tabHost.newTabSpec("tagToReply");
		
		 * Intent intent = new Intent(); intent.setAction(Intent.ACTION_MAIN);
		 * intent.addCategory(Intent.CATEGORY_DEFAULT);
		 * intent.setType("vnd.android-dir/mms-sms");
		 * specToReply.setContent(intent);
		 
		specToReply.setContent(new Intent(this, ToReplyActivity.class));
		specToReply.setIndicator(getString(R.string.tab_to_reply_title),
				getResources().getDrawable(android.R.drawable.ic_menu_agenda));

		tabHost.addTab(specToReply);*/

		TabHost.TabSpec specContacts = tabHost.newTabSpec("tagContacts");
		specContacts.setContent(new Intent(this, ContactsActivity.class));
		specContacts
				.setIndicator(
						getString(R.string.tab_contacts_title),
						getResources().getDrawable(
								android.R.drawable.ic_dialog_dialer));

		tabHost.addTab(specContacts);
	}
}