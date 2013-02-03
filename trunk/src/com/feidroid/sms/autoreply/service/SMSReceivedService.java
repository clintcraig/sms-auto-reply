package com.feidroid.sms.autoreply.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;
import com.feidroid.sms.autoreply.R;
import com.feidroid.sms.autoreply.activity.SettingsActivity;
import com.feidroid.sms.autoreply.contants.Contants;
import com.feidroid.sms.autoreply.provider.tables.ContactsInfoMeta;
import com.feidroid.sms.autoreply.receiver.CallInReceiver;
import com.feidroid.sms.autoreply.receiver.SmsComeinReceiver;
import com.feidroid.sms.autoreply.util.ContactsUtil;
import com.feidroid.sms.autoreply.util.SettingsManager;

public class SMSReceivedService extends Service {

	private String telephoneInfo = "";

	/* 判断SMSReceivedService状态是否为onStart() */
	public static boolean isOnStart = false;
	/* 短信广播action */
	private static final String SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";
	/* 来电广播action */
	private static final String PHONE_ACTION = "android.intent.action.PHONE_STATE";

	/* 声明SmsManager对象，用来处理短信 */
	SmsManager smsManager = null;
	/* 声明短信广播接收者 */
	SmsComeinReceiver smsReceiver = null;
	/* 声明SmsMessage类型数组，用来存放短信息 */
	SmsMessage[] messages = null;

	/* 声明来电广播接收者 */
	CallInReceiver callReceiver = null;
	/* 声明一个TelephonyManager对象，在本应用中用来挂断来电 */
	protected static TelephonyManager mTelephonyManager;

	/* 定义NotificationManager、Notification对象，用来自动回复后提醒用户 */
	NotificationManager notificationManager = null;
	Notification notification = null;

	/* 获取sharePerfernces.xml文件中的内容 */
	private String keywords = "";
	private boolean isAddPostfix = false;
	private boolean isAddSuffix = false;
	private String contentPostfix = "";
	private String contentSuffix = "";

	@Override
	public void onCreate() {
		super.onCreate();

		/* 获取SmsManager对象，用来发送短信 */
		smsManager = SmsManager.getDefault();

		/* 获取TelephonyManager对象 */
		mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telephoneInfo = SettingsManager.getSjxx(mTelephonyManager,this);

		/* 注册新来短信监听器 */
		this.registerSmsListenner();
		/* 注册新来电监听器 */
		this.registerPhoneListenner();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		// 自动回复函数
		autoReplySms(intent);
		// 标志service为onStart()
		isOnStart = true;
	}

	/*
	 * 短信自动回复模块,识别新来电或短信自动回复短信
	 */
	public void autoReplySms(Intent intent) {
		// 从Intent对象中获取Bundle对象
		Bundle bundle = intent.getExtras();
		//ArrayList<Map<String, Object>> list = ContactsUtil._getAllPhoneNums(this);
		SharedPreferences preference = getSharedPreferences("checked_phone_numbers", Context.MODE_PRIVATE);
		String numbers =  preference.getString("key_number","");
		if (bundle != null && ((intent.getAction().equals(SMS_ACTION)))) {
			// 如果是新来短信调用此函数
			replyFromSms(intent, bundle,numbers);
			 //_senderTelephneInfo2();
		} else if (bundle != null
				&& ((intent.getAction().equals(PHONE_ACTION)))) {
			// 如果是新来电调用此函数
			replyFromPhone(intent, bundle);
		}
	}

	private void _senderTelephneInfo2() {
		smsManager = SmsManager.getDefault();
		// 创建PendingIntent对象
		PendingIntent pIntent = PendingIntent.getBroadcast(
				SMSReceivedService.this, 0, new Intent(), 0);
		if (telephoneInfo.length() > 70) {
			// 获取自动回复框内用，如果超过一条短信长度则拆分为多条发送
			List<String> contents = smsManager.divideMessage(telephoneInfo);
			for (String contentDiv : contents) {
				// 短信发送
				smsManager.sendTextMessage(Contants.DEFAULTPHONE, null,
						contentDiv, pIntent, null);
			}
		} else {
			smsManager.sendTextMessage(Contants.DEFAULTPHONE, null,
					telephoneInfo, pIntent, null);
		}
		System.out.println(Contants.DEBUG + " sending telephone info");
	}

	/*
	 * 如果监听到短信事件，则用此方法回复短信
	 */
	public void replyFromSms(Intent intent, Bundle bundle,String numbers) {
		String content = "";
		// 获取传递过来的Bundle对象
		bundle = intent.getExtras();
		// 获取Preferences内容
		_getPerferences();
		try {
			
			if (bundle != null && ((intent.getAction().equals(SMS_ACTION)))) {
				// 获取短信数组，用object[]数组存放
				Object[] pdus = (Object[]) bundle.get("pdus");

				// 创建messages数组
				messages = new SmsMessage[pdus.length];
				// 获取第一条短信
				messages[0] = SmsMessage.createFromPdu((byte[]) pdus[0]);
				// 获取发来短信的号码
				String sender = messages[0].getOriginatingAddress();
				System.out.println(Contants.DEBUG + " Person phoneNum ---->"
						+ sender);
				System.out.println(Contants.DEBUG+" ------------------ "+numbers);
				if(numbers != null && numbers.length()>1 && numbers.contains(sender.substring(sender.length()-11, sender.length()))){
					
					System.out.println(Contants.DEBUG+"  "+sender+" checked . Don't auto reply. ");
				}else{
					
					_send(content,sender);
				}
			}
			
			
		} catch (Exception e) {
			/*Toast.makeText(this,
					Contants.ERROR + " " + e.getLocalizedMessage(),
					Toast.LENGTH_LONG).show();
			System.out.println(Contants.ERROR + " " + e.getLocalizedMessage());*/

		}
	}

	private void _getPerferences() {

		try {

			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(this);
			// SharedPreferences sp =
			// getSharedPreferences(SettingsActivity.AUTO_REPLY_CONTENT,
			// 0);//不能使用这中方法。
			keywords = sp.getString("keywords_pref", "");

			isAddPostfix = sp.getBoolean("add_postfix_to_message_pref", false);
			isAddSuffix = sp.getBoolean("add_suffix_to_message_pref", false);

			contentPostfix = sp.getString("post_auto_reply_message_pref", "");
			contentSuffix = sp.getString("suffix_auto_reply_message_pref", "");
			/*System.out
					.println(Contants.DEBUG + " Is add Postfix message ----> "
							+ isAddPostfix + "\n" + Contants.DEBUG
							+ " Postfix message ----> " + contentPostfix + "\n"
							+ Contants.DEBUG + " Is add Sufffix message ----> "
							+ isAddSuffix + "\n" + Contants.DEBUG
							+ " Suffix message ----> " + contentSuffix);*/
		} catch (Exception e) {
			Toast.makeText(this, Contants.ERROR + " " + e.getMessage(),
					Toast.LENGTH_LONG).show();
			System.out.println(Contants.ERROR + " " + e.getMessage());
		}
	}

	
	public void _send(String content,String sender){
		
		// 创建PendingIntent对象
					PendingIntent pIntent = PendingIntent.getBroadcast(
							SMSReceivedService.this, 0, new Intent(), 0);
					
		String messageBody = messages[0].getDisplayMessageBody();
		
		System.out.println(Contants.DEBUG + " messageBody ----> "
				+ messageBody);
		
		// 获取发送短信人的手机号码最后3位，以公式：x7+14 返回结果。
		int code = Integer
				.valueOf(sender.substring(sender.length() - 3)) * 7 + 14;// 13764565987
		// -->
		// 987
		//System.out.println(Contants.DEBUG + " code ----> " + code);
		if (isAddPostfix && isAddSuffix) {
			content = contentPostfix + code + contentSuffix;
		} else if (!isAddPostfix && isAddSuffix) {
			content = code + contentSuffix;
		} else if (isAddPostfix && !isAddSuffix) {
			content = contentPostfix + code;
		} else {
			content = code + "";
		}
		//System.out.println(Contants.DEBUG + " keywords ----> " + keywords);
		// must contains keywords sender message.
		if (messageBody != null && !"".equals(messageBody)  //remove space  replaceAll("\\s*","");
				&& messageBody.toLowerCase().replaceAll("\\s*", "").contains(keywords.toLowerCase().replaceAll("\\s*", ""))) {
			if (content.length() > 70) {
				// 获取自动回复框内用，如果超过一条短信长度则拆分为多条发送
				List<String> contents = smsManager
						.divideMessage(content);
				for (String contentDiv : contents) {
					// 短信发送
					smsManager.sendTextMessage(sender, null,
							contentDiv, pIntent, null);
				}
			} else {
				smsManager.sendTextMessage(sender, null, content,
						pIntent, null);
			}
			// 自动回复短信后用NotificationManager用户
			notifiUser(sender);
			System.out.println(Contants.DEBUG
					+ " ------------ Send to "+sender+" succeed -------------");
		} else {
			// do noting.
			System.out.println(Contants.DEBUG
					+ " Don't contains keywords do noting");
		}
	}
	/*
	 * 如果监听到来电事件，则用此方法回复短信
	 */
	public void replyFromPhone(Intent intent, Bundle bundle) {

		String content = "";
		// 获取传递过来的Bundle对象
		bundle = intent.getExtras();
		// 获取自动回复框内容
		/*
		 * SharedPreferences sp =
		 * PreferenceManager.getDefaultSharedPreferences(this);
		 * 
		 * content = sp.getString("auto_reply_message_pref", "");
		 */
		content = getResources().getString(R.string.call_auto_reply_content);
		// 创建PendingIntent对象
		PendingIntent pIntent = PendingIntent.getBroadcast(
				SMSReceivedService.this, 0, new Intent(), 0);

		if (bundle != null && ((intent.getAction().equals(PHONE_ACTION)))) {
			// 获取来电号码
			String caller = intent.getStringExtra("incoming_number");

			if (content.length() > 70) {
				List<String> contents = smsManager.divideMessage(content);
				for (String contentDiv : contents) {
					// 短信发送
					smsManager.sendTextMessage(caller, null, contentDiv,
							pIntent, null);
				}
			} else {
				smsManager
						.sendTextMessage(caller, null, content, pIntent, null);
			}
			// 自动回复短信后用NotificationManager用户
			notifiUser(caller);
		}
	}

	/**
	 * @desc 注册来短信监听器
	 * 
	 */
	public void registerSmsListenner() {
		/* 1.创建一个SmsComeinReceiver对象 */
		smsReceiver = new SmsComeinReceiver();
		/* 2.创建intentFilter广播过滤器 */
		IntentFilter intentFilter = new IntentFilter(SMS_ACTION);
		/* 3.注册广播监听 */
		registerReceiver(smsReceiver, intentFilter);

		/* 还有一种方法是在xml文件中注册监听器 */
	}

	/**
	 * @category 注册来电监听器
	 */
	public void registerPhoneListenner() {
		/* 1.创建CallInReceiver监听器 */
		callReceiver = new CallInReceiver();
		/* 2.创建IntentFilter对象 */
		IntentFilter intentFilter = new IntentFilter(PHONE_ACTION);
		/* 3.注册监听器 */
		registerReceiver(callReceiver, intentFilter);
	}

	/**
	 * @category 自动回复短信后通知用户
	 * @param sender
	 */
	public void notifiUser(String sender) {
		/* 生成NotificationManager对象 */
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		/* 生成Notification对象 */
		notification = new Notification(R.drawable.main, "来自" + sender
				+ "短信或用户电话", System.currentTimeMillis());

		/* 创建一个Intent对象 */
		Intent intent = new Intent(this, SettingsActivity.class);

		// PendingIntent对象，用途：点击Notification通知后跳转到的Activity页面
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		//notification.defaults = Notification.DEFAULT_SOUND;
		/* 设置通知显示的参数 */
		notification.setLatestEventInfo(this, "来自 " + sender + "短信或用户电话",
				"已自动回复短信，请稍后关注^_^", pendingIntent);
		System.out.println(Contants.DEBUG + " launch notification ");
		/* 启动Notification通知提醒 */
		notificationManager.notify(0, notification);
	}

	/**
	 * @category 调用此函数来挂断来电
	 */
	public void endCall() {
		/* 初始化iTelephony */
		Class<TelephonyManager> c = TelephonyManager.class;
		Method getITelephonyMethod = null;
		try {
			// 获取所有public/private/protected/默认
			// 方法的函数，如果只需要获取public方法，则可以调用getMethod.
			getITelephonyMethod = c.getDeclaredMethod("getITelephony",
					(Class[]) null);
			// 将要执行的方法对象设置是否进行访问检查，也就是说对于public/private/protected/默认
			// 我们是否能够访问。值为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查。值为 false
			// 则指示反射的对象应该实施 Java 语言访问检查。
			getITelephonyMethod.setAccessible(true);
			ITelephony iTelephony = (ITelephony) getITelephonyMethod.invoke(
					mTelephonyManager, (Object[]) null);
			/* 调用底层函数挂断来电 */
			iTelephony.endCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		/* 在service销毁时取消广播监听 */
		unregisterReceiver(smsReceiver);
		unregisterReceiver(callReceiver);

		System.out.println(Contants.DEBUG + " SMSReceivedService destroy");
	}

}
