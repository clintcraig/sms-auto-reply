package com.feidroid.sms.autoreply.activity;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
		System.out.println(Contants.DEBUG +"  the first time running the SMSAutoReplier  --->  "+manager.isFirstTimeLaunch());
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

	}

	private void setTabView() {
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();

		TabHost.TabSpec specSettings = tabHost.newTabSpec("tagSettings");
		specSettings.setContent(new Intent(this, SettingsActivity.class));
		specSettings.setIndicator(getString(R.string.tab_settings_title)

		, getResources().getDrawable(android.R.drawable.ic_menu_preferences));

		tabHost.addTab(specSettings);

		TabHost.TabSpec specToReply = tabHost.newTabSpec("tagToReply");
		specToReply.setContent(new Intent(this, ToReplyActivity.class));
		specToReply.setIndicator(getString(R.string.tab_to_reply_title),
				getResources().getDrawable(android.R.drawable.ic_menu_agenda));

		tabHost.addTab(specToReply);

		TabHost.TabSpec specContacts = tabHost.newTabSpec("tagContacts");
		specContacts.setContent(new Intent(this, ContactsActivity.class));
		specContacts.setIndicator(getString(R.string.tab_contacts_title),
				getResources().getDrawable(android.R.drawable.ic_input_add));

		tabHost.addTab(specContacts);
	}
}