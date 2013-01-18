package com.feidroid.sms.autoreply.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.feidroid.sms.autoreply.service.SMSReceivedService;

public class CallInReceiver  extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//拒绝接听来电
		refuseCall(context, intent);

	}
	
	/*
	 * @此函数用来拒绝来电
	 */
	public void refuseCall(Context context, Intent intent){
		//如果监听到来电广播，则执行以下代码
		if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
			//获取TelephonyManager对象，用来管理电话
			TelephonyManager tm = (TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);
			
			switch(tm.getCallState()){
			//如果状态为TelephonyManager.CALL_STATE_RINGING，则挂断电话
			case TelephonyManager.CALL_STATE_RINGING:

				//调用endCall()函数，挂断电话
				new SMSReceivedService().endCall();
			
				//绑定AckSmsService对象
				intent.setClass(context, SMSReceivedService.class);
				
				//跳转到intent绑定的Service
				context.startService(intent);
				break;
			}
		}
	}
}
