

2013-1-16
1.实现了当启动监听时，收到短信后会自动回复短信，短信内容可以随时更换。

2.将收到的短信过滤，只有包含关键字“验证码”的短信才进行回复。关键字可以根据需要进行更换。

2013-1-17
3.实现了开机自启动
启动监听开机启动项
需要获取权限
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/> 
2013-1-18
4.读取安装软件手机信息。
需要获取权限  
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/> 

2013-1-19
5.定时功能，即设置时间，间隔多长时间发送。
6.获取手机所有短信和联系人。

