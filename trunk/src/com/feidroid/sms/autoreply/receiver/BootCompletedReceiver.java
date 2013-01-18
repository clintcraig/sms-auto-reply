package com.feidroid.sms.autoreply.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.feidroid.sms.autoreply.activity.SMSAutoReplyActivity;
import com.feidroid.sms.autoreply.contants.Contants;
import com.feidroid.sms.autoreply.service.SMSReceivedService;

public class BootCompletedReceiver extends BroadcastReceiver {

	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
			//开机自启动程序
			Intent intentActivity = new Intent(context,SMSAutoReplyActivity.class);
			intentActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intentActivity);
			System.out.println(Contants.DEBUG+" Boot completed ");
			
			//启动某些服务
			Intent intentService = new Intent(context,SMSReceivedService.class);
			context.startService(intentService);
			System.out.println(Contants.DEBUG+" Start SMSReceivedService ");
		}
		
	}

}
