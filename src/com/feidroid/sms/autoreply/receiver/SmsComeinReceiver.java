package com.feidroid.sms.autoreply.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.feidroid.sms.autoreply.service.SMSReceivedService;

public class SmsComeinReceiver  extends BroadcastReceiver {

	private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(ACTION)) {
			// 获取Bundle对象
			Bundle bundle = intent.getExtras();
				intent.setClass(context, SMSReceivedService.class);
			//将intent传递到Service对象，并启动Service
			context.startService(intent);
		}
	}
}
