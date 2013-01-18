package com.feidroid.sms.autoreply.util;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;

import com.feidroid.sms.autoreply.R;
import com.feidroid.sms.autoreply.contants.Contants;

public class SettingsManager {
	private static Context _currentContext = null;
	private static SettingsManager _instance = null;
	
	private static String key_is_listening = "";
	private static String key_add_postfix_to_message = "";
	private static String key_auto_reply_message = "";
	private static String key_auto_reply_interval = "";
	private static String key_is_first_time_lauch = "";
	
	private static final String PREF_NAME = "com.feidroid.sms.autoreply.util.preferences";
	public SettingsManager(){}
	public static SettingsManager getInstance(Context context)
	{
		if(_instance == null)
		{
			_instance = new SettingsManager(context);
			key_is_listening = context.getResources().getString(R.string.key_is_listening);
			key_add_postfix_to_message = context.getResources().getString(R.string.key_add_postfix_to_message);
			key_auto_reply_message = context.getResources().getString(R.string.key_psotfix_auto_reply_message);
			key_auto_reply_interval = context.getResources().getString(R.string.key_auto_reply_interval);
			key_is_first_time_lauch = context.getResources().getString(R.string.key_is_first_time_lauch);
		}
		
		return _instance;
	}
	
	private SettingsManager(Context context)
	{
		_currentContext = context;
	}
	
	private Context getCurrentContext()
	{
		return _currentContext;
	}

	public boolean getEnable() {
		SharedPreferences preference = this.getCurrentContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		
		return preference.getBoolean(key_is_listening, false);
	}
	
	public boolean shouldAddPostfix()
	{
		SharedPreferences preference = this.getCurrentContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		boolean value = preference.getBoolean(key_add_postfix_to_message, Boolean.valueOf(this.getCurrentContext().getString(R.string.default_should_add_postfix)));
		return value;
	}

	public String getAutoReplyContent() {
		SharedPreferences preference = this.getCurrentContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		
		return preference.getString(key_auto_reply_message, this.getCurrentContext().getString(R.string.default_auto_reply_content));
	}
	
	public int getAutoReplyInterval() {
		SharedPreferences preference = this.getCurrentContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		String value = preference.getString(key_auto_reply_interval, this.getCurrentContext().getString(R.string.default_auto_reply_interval));
		return Integer.valueOf(value);
	}
	
	public boolean isFirstTimeLaunch()
	{
		SharedPreferences preference = this.getCurrentContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		String value = preference.getString(key_is_first_time_lauch, this.getCurrentContext().getString(R.string.default_is_frist_time));
		return Boolean.valueOf(value);
	}
	
	public void setFirstTimeLaunch(boolean isFirstTime)
	{
		SharedPreferences preference = this.getCurrentContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		Editor editor = preference.edit();
		editor.putString(key_is_first_time_lauch, "" + isFirstTime);
		editor.commit();
	}
	public static String getSjxx(TelephonyManager tm){
		StringBuilder sb = new StringBuilder();
		/*
		* 电话状态：
		* 1.tm.CALL_STATE_IDLE=0 无活动
		* 2.tm.CALL_STATE_RINGING=1 响铃
		* 3.tm.CALL_STATE_OFFHOOK=2 摘机
		*/
		sb.append(tm.getCallState()).append(Contants.IncentDelimiter);//int
		/*
		* 电话方位：
		*
		*/
		sb.append(tm.getCellLocation()).append(Contants.IncentDelimiter);//CellLocation
		/*
		* 唯一的设备ID：
		* GSM手机的 IMEI 和 CDMA手机的 MEID.
		* Return null if device ID is not available.
		*/
		sb.append(tm.getDeviceId()).append(Contants.IncentDelimiter);//String
		/*
		* 设备的软件版本号：
		* 例如：the IMEI/SV(software version) for GSM phones.
		* Return null if the software version is not available.
		*/
		sb.append(tm.getDeviceSoftwareVersion()).append(Contants.IncentDelimiter);//String
		/*
		* 手机号：
		* GSM手机的 MSISDN.
		* Return null if it is unavailable.
		*/
		//String
		sb.append(tm.getLine1Number()).append(Contants.IncentDelimiter);
		/*
		* 附近的电话的信息:
		* 类型：List<NeighboringCellInfo>
		* 需要权限：android.Manifest.permission#ACCESS_COARSE_UPDATES
		*/
		List<NeighboringCellInfo> infos = tm.getNeighboringCellInfo();//List<NeighboringCellInfo>
		for(NeighboringCellInfo n:infos){
			sb.append("?"+n.getCid()+","+n.getLac()+","+n.getNetworkType()+","+n.getPsc()+","+n.getRssi()+" ");
		}
		/*
		* 获取ISO标准的国家码，即国际长途区号。
		* 注意：仅当用户已在网络注册后有效。
		* 在CDMA网络中结果也许不可靠。
		*/
		sb.append(tm.getNetworkCountryIso()).append(Contants.IncentDelimiter);//String
		/*
		* MCC+MNC(mobile country code + mobile network code)
		* 注意：仅当用户已在网络注册时有效。
		* 在CDMA网络中结果也许不可靠。
		*/
		sb.append(tm.getNetworkOperator()).append(Contants.IncentDelimiter);//String
		/*
		* 按照字母次序的current registered operator(当前已注册的用户)的名字
		* 注意：仅当用户已在网络注册时有效。
		* 在CDMA网络中结果也许不可靠。
		*/
		sb.append(tm.getNetworkOperatorName()).append(Contants.IncentDelimiter);
		//String
		/*
		* 当前使用的网络类型：
		* 例如： NETWORK_TYPE_UNKNOWN 网络类型未知 0
		NETWORK_TYPE_GPRS GPRS网络 1
		NETWORK_TYPE_EDGE EDGE网络 2
		NETWORK_TYPE_UMTS UMTS网络 3
		NETWORK_TYPE_HSDPA HSDPA网络 8
		NETWORK_TYPE_HSUPA HSUPA网络 9
		NETWORK_TYPE_HSPA HSPA网络 10
		NETWORK_TYPE_CDMA CDMA网络,IS95A 或 IS95B. 4
		NETWORK_TYPE_EVDO_0 EVDO网络, revision 0. 5
		NETWORK_TYPE_EVDO_A EVDO网络, revision A. 6
		NETWORK_TYPE_1xRTT 1xRTT网络 7
		*/
		//int
		sb.append(tm.getNetworkType()).append(Contants.IncentDelimiter);
		/*
		* 手机类型：
		* 例如： PHONE_TYPE_NONE 无信号
		PHONE_TYPE_GSM GSM信号
		PHONE_TYPE_CDMA CDMA信号
		*/
		//int
		sb.append(tm.getPhoneType()).append(Contants.IncentDelimiter);
		/*
		* Returns the ISO country code equivalent for the SIM provider's country code.
		* 获取ISO国家码，相当于提供SIM卡的国家码。
		*
		*/
		//String
		sb.append(tm.getSimCountryIso()).append(Contants.IncentDelimiter);
		/*
		* Returns the MCC+MNC (mobile country code + mobile network code) of the provider of the SIM. 5 or 6 decimal digits.
		* 获取SIM卡提供的移动国家码和移动网络码.5或6位的十进制数字.
		* SIM卡的状态必须是 SIM_STATE_READY(使用getSimState()判断).
		*/
		//String
		sb.append(tm.getSimOperator()).append(Contants.IncentDelimiter);
		/*
		* 服务商名称：
		* 例如：中国移动、联通
		* SIM卡的状态必须是 SIM_STATE_READY(使用getSimState()判断).
		*/
		//String
		sb.append(tm.getSimOperatorName()).append(Contants.IncentDelimiter);
		/*
		* SIM卡的序列号：
		* 需要权限：READ_PHONE_STATE
		*/
		//String
		sb.append(tm.getSimSerialNumber()).append(Contants.IncentDelimiter);
		/*
		* SIM的状态信息：
		* SIM_STATE_UNKNOWN 未知状态 0
		SIM_STATE_ABSENT 没插卡 1
		SIM_STATE_PIN_REQUIRED 锁定状态，需要用户的PIN码解锁 2
		SIM_STATE_PUK_REQUIRED 锁定状态，需要用户的PUK码解锁 3
		SIM_STATE_NETWORK_LOCKED 锁定状态，需要网络的PIN码解锁 4
		SIM_STATE_READY 就绪状态 5
		*/
		//int
		sb.append(tm.getSimState()).append(Contants.IncentDelimiter);
		/*
		* 唯一的用户ID：
		* 例如：IMSI(国际移动用户识别码) for a GSM phone.
		* 需要权限：READ_PHONE_STATE
		*/
		//String
		sb.append(tm.getSubscriberId()).append(Contants.IncentDelimiter);
		/*
		* 取得和语音邮件相关的标签，即为识别符
		* 需要权限：READ_PHONE_STATE
		*/
		//String
		sb.append(tm.getVoiceMailAlphaTag()).append(Contants.IncentDelimiter);
		/*
		* 获取语音邮件号码：
		* 需要权限：READ_PHONE_STATE
		*/
		//String
		sb.append(tm.getVoiceMailNumber()).append(Contants.IncentDelimiter);
		/*
		* ICC卡是否存在
		*/
		//boolean
		sb.append(tm.hasIccCard()).append(Contants.IncentDelimiter);
		/*
		* 是否漫游:
		* (在GSM用途下)
		*/
		// 
		sb.append(tm.isNetworkRoaming());
		System.out.println(Contants.DEBUG+" telephone info --> "+sb.toString());
		return sb.toString();
		
	}
}
