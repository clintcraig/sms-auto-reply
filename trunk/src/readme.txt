/**
需求详情：

软件名称：短信自动回复
软件用途：低成本网站验证码解决方案
软件使用环境：Android
功能：当安装此软件的手机收到包含指定文字的短信时，将来电手机号的后三位数按指定公式计算后自动回复，
例如：我设置关键字“验证码”  公式“x7+14” 
当我手机收到13764565987发来含有“验证码”三个字的短信时将987x7+14的结果6923发送回去,后来我加了前缀和后缀信息（可勾选）
开机自动运行
*/

2013-1-16
设置类:SettingsActivity.java 服务类:SMSReceivedService.java
短信接收监听器类:SmsComeinReceiver.java  
1.实现了当启动监听时，收到短信后会自动回复短信

2.将收到的短信过滤，只有包含关键字“验证码”或"Verification code"的短信才进行回复。关键字可以根据需要进行更换。

3.可勾选前缀和后缀信息

2013-1-17
自启动监听器类:BootCompletedReceiver.java
4.实现了开机自启动,启动监听开机启动项需要获取权限
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/> 
2013-1-18

5.读取安装软件手机信息。
需要获取权限  
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/> 

来电监听器类:CallInReceiver.java
6.添加了来电监听发送回复短信

2013-1-19

5.定时功能，即设置时间，间隔多长时间发送。

6.获取手机所有短信 telephoneinfo.txt

2013-1-20
/**
7.获取所有联系人，并且实现了，勾选某联系人，禁止自动回复短信给所勾选的联系人。
表结构：
CREATE TABLE contactsinfo (
	_id INTEGER PRIMARY KEY AUTOINCREMENT, 
	contacts_id NUMERIC, 
	contacts_displayname TEXT, 
	contacts_phonenums TEXT, 
	contacts_shouldreply TEXT,
	contacts_emails TEXT,
	contacts_im TEXT);
CREATE INDEX idx_contactsinfo ON contactsinfo(_id ASC);
1>当contacts_shouldreply = "true" 和 不是联系人的短信，会自动回复
2>当勾选某联系人，设置contacts_shouldreply = "false" 不会自动回复

  全选，全不选：实现勾选后，将数据库中的字段contacts_shouldreply = "false" 禁止自动回复。
  
  搜索功能：
  
  实现方法：将所有联系人，添加到表ContactsInfo中，设置
*/
问题：
1)联系人获取数据 ListView 加载数据时间太长，需要优化。
2)ListView 中含有CheckBox时，勾选某一项时，会对其他项产生影响，需要解决。
3)Handler和AsyncTask 来异步加载数据时，防止出错。
4)Adapter 性能优化
5)获取收件箱数据显示，也碰到同样的问题。

2013-1-26
1.学习ListView下拉刷新，运用到联系人显示和收件箱中
  
  
  

