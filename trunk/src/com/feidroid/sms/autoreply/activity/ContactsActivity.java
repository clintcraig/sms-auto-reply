package com.feidroid.sms.autoreply.activity;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.feidroid.sms.autoreply.R;
import com.feidroid.sms.autoreply.contants.Contants;
import com.feidroid.sms.autoreply.entity.ContactInfo;

public class ContactsActivity extends Activity implements
		ListView.OnScrollListener {

	// show textview dialog
	private RemoveWindow mRemoveWindow = new RemoveWindow();
	Handler mHandler = new Handler();
	private WindowManager mWindowManager;
	private TextView mDialogText;
	private boolean mShowing;
	private boolean mReady;
	private char mPrevLetter = Character.MIN_VALUE;

	private ListView listView;
	AutoCompleteTextView textView;
	TextView emptytextView;
	protected CursorAdapter mCursorAdapter;
	protected Cursor mCursor = null;
	protected ContactAdapter ca;
	ArrayList<ContactInfo> contactList = new ArrayList<ContactInfo>();
	// 选中的手机号
	protected String numberStr = "";
	protected String[] autoContact = null;
	private static final int DIALOG_KEY = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.contacts_bg);

		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mDialogText = (TextView) inflate.inflate(R.layout.list_position, null);

		mDialogText.setVisibility(View.INVISIBLE);

		listView = (ListView) this.findViewById(R.id.list);
		textView = (AutoCompleteTextView) findViewById(R.id.edit);
		emptytextView = (TextView) findViewById(R.id.empty);
		Button btn_add = (Button) findViewById(R.id.btn_add);
		Button btn_back = (Button) findViewById(R.id.btn_back);

		emptytextView.setVisibility(View.GONE);

		// 启动进程
		new GetContactTask().execute("");

		// 列表点击事件监听
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View view,
					int position, long id) {
				LinearLayout ll = (LinearLayout) view;
				CheckBox cb = (CheckBox) ll.getChildAt(0).findViewById(
						R.id.check);
				// 选中则加入选中字符串中,取消则从字符串中删除
				if (cb.isChecked()) {
					cb.setChecked(false);
					numberStr = numberStr.replace(","+ contactList.get(position).getUserNumber(), "");
					contactList.get(position).isChecked = false;
				} else {
					cb.setChecked(true);
					numberStr += ","+ contactList.get(position).getUserNumber();
					contactList.get(position).isChecked = true;
				}
			}
		});

		btn_add.setOnClickListener(btnClick);
		btn_back.setOnClickListener(btnClick);

		listView.setOnScrollListener(this);
		// handler for handle dialog textview
		mHandler.post(new Runnable() {

			public void run() {
				mReady = true;
				WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
						LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT,
						WindowManager.LayoutParams.TYPE_APPLICATION,
						WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
								| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
						PixelFormat.TRANSLUCENT);
				mWindowManager.addView(mDialogText, lp);
			}
		});

		
	}

	// 按钮监听
	private OnClickListener btnClick = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_add:
				// 点击确认将选中的手机号回传
				Log.i("eoe", numberStr);
				SharedPreferences preference = getSharedPreferences("checked_phone_numbers", Context.MODE_PRIVATE);
				Editor editor = preference.edit();
				editor.putString("key_number",numberStr);
				editor.commit();
				break;

			case R.id.btn_back:
				//finish();
				break;
			}
		}
	};
	// 监听AUTOTEXT内容变化,当出现符合选中联系人[联系人(手机号)]的情况下,将该勾中,并加入选中手机号中
	private TextWatcher mTextWatcher = new TextWatcher() {
		public void beforeTextChanged(CharSequence s, int start, int before,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int after) {

			String autoText = s.toString();
			if (autoText.length() >= 13) {
				Pattern pt = Pattern.compile("\\(([1][3,5,8]+\\d{9})\\)");
				Matcher mc = pt.matcher(autoText);
				if (mc.find()) {
					String sNumber = mc.group(1);
					DealWithAutoComplete(contactList, sNumber);
					// 刷新列表
					Toast.makeText(ContactsActivity.this, getResources().getString(R.string.has_checked_your_search), 1000)
							.show();
					ca.setItemList(contactList);
					ca.notifyDataSetChanged();
				}
			}
		}

		public void afterTextChanged(Editable s) {
		}

	};

	// 获取通讯录进程
	private class GetContactTask extends AsyncTask<String, String, String> {
		public String doInBackground(String... params) {
			/*
			 * try{ Thread.sleep(4000); } catch(Exception e){}
			 */
			// 从本地手机中获取
			GetLocalContact();
			// 从SIM卡中获取
			GetSimContact("content://icc/adn");
			// 发现有得手机的SIM卡联系人在这个路径...所以都取了(每次验证是否已存在)
			GetSimContact("content://sim/adn");
			return "";
		}

		@Override
		protected void onPreExecute() {
			showDialog(DIALOG_KEY);
		}

		@Override
		public void onPostExecute(String Re) {
			// 绑定LISTVIEW
			if (contactList.size() == 0) {
				emptytextView.setVisibility(View.VISIBLE);
			} else {
				// 按中文拼音顺序排序
				Comparator comp = new Mycomparator();
				Collections.sort(contactList, comp);

				// numberStr = GetNotInContactNumber(wNumStr, contactList) +
				// numberStr;
				ca = new ContactAdapter(ContactsActivity.this, contactList);
				listView.setAdapter(ca);
				listView.setTextFilterEnabled(true);
				// 编辑AUTOCOMPLETE数组
				autoContact = new String[contactList.size()];
				for (int c = 0; c < contactList.size(); c++) {
					autoContact[c] = contactList.get(c).contactName + "("
							+ contactList.get(c).userNumber + ")";
				}
				// 绑定AUTOCOMPLETE
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						ContactsActivity.this,
						android.R.layout.simple_dropdown_item_1line,
						autoContact);
				textView.setAdapter(adapter);
				textView.addTextChangedListener(mTextWatcher);
			}
			removeDialog(DIALOG_KEY);
		}
	}

	// 弹出"查看"对话框
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_KEY: {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage(getResources().getString(R.string.loading_contacts));
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;
		}
		}
		return null;
	}

	// 从本机中取号
	private void GetLocalContact() {
		// 读取手机中手机号
		Cursor cursor = getContentResolver().query(People.CONTENT_URI, null,null, null, null);
		//Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,null, null, null);
		while (cursor.moveToNext()) {
			ContactInfo cci = new ContactInfo();
			// 取得联系人名字
			int nameFieldColumnIndex = cursor.getColumnIndex(People.NAME);
			//int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
			cci.contactName = cursor.getString(nameFieldColumnIndex);
			// 取得电话号码1.6版本或之前的获取方法
			int numberFieldColumnIndex = cursor.getColumnIndex(People.NUMBER);
			//2.1版本及之后获取方法-------------------------------------------------------------------------
			/*String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			StringBuilder phoneNumbers = new StringBuilder();
			int numberFieldColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
			 // 查看该联系人有多少个电话号码。如果没有这返回值为0  
            int phoneCount = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));  
            if (phoneCount > 0) {  
                // 获得联系人的电话号码  
                Cursor phones = getContentResolver().query(  
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,  
                        null,  
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID  
                                + " = " + contactId, null, null);  
               while (phones != null && phones.moveToNext()) {
            	   phoneNumbers.append(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)))
            	   .append(Contants.IncentDelimiter);
               }
               phoneNumbers.deleteCharAt(phoneNumbers.length());
            }  */
            //--------------------------------------------------------------------------------------------------
			cci.userNumber = cursor.getString(numberFieldColumnIndex);
			cci.userNumber = GetNumber(cci.userNumber);
			cci.isChecked = false;
			if (IsUserNumber(cci.userNumber)) {
				if (!IsContain(contactList, cci.userNumber)) {
					/*
					 * if(IsAlreadyCheck(wNumStr, cci.userNumber)){
					 * cci.isChecked = true; numberStr += "," + cci.userNumber;
					 * }
					 */
					contactList.add(cci);
					Log.i("eoe", "*********" + cci.userNumber);
				}
			}
		}
		cursor.close();
	}

	// 从SIM卡中取号
	private void GetSimContact(String add) {
		// 读取SIM卡手机号,有两种可能:content://icc/adn与content://sim/adn
		try {
			Intent intent = new Intent();
			intent.setData(Uri.parse(add));
			Uri uri = intent.getData();
			mCursor = getContentResolver().query(uri, null, null, null, null);
			if (mCursor != null) {
				while (mCursor.moveToNext()) {
					ContactInfo sci = new ContactInfo();
					// 取得联系人名字
					int nameFieldColumnIndex = mCursor.getColumnIndex("name");
					sci.contactName = mCursor.getString(nameFieldColumnIndex);
					// 取得电话号码
					int numberFieldColumnIndex = mCursor
							.getColumnIndex("number");
					sci.userNumber = mCursor.getString(numberFieldColumnIndex);

					sci.userNumber = GetNumber(sci.userNumber);
					sci.isChecked = false;

					if (IsUserNumber(sci.userNumber)) {
						if (!IsContain(contactList, sci.userNumber)) {
							/*
							 * if(IsAlreadyCheck(wNumStr, sci.userNumber)){
							 * sci.isChecked = true; numberStr += "," +
							 * sci.userNumber; }
							 */
							contactList.add(sci);
							Log.i("eoe", "*********" + sci.userNumber);
						}
					}
				}
				mCursor.close();
			}
		} catch (Exception e) {
			Log.i("eoe", e.toString());
		}
	}

	// 是否在LIST有值
	private boolean IsContain(ArrayList<ContactInfo> list, String un) {
		for (int i = 0; i < list.size(); i++) {
			if (un.equals(list.get(i).userNumber)) {
				return true;
			}
		}
		return false;
	}

	// 手输手机号的是否在通讯录中
	private boolean IsAlreadyCheck(String[] Str, String un) {
		for (int i = 0; i < Str.length; i++) {
			if (un.equals(Str[i])) {
				return true;
			}
		}
		return false;
	}

	// 获取手输手机号不在通讯录中的号码
	private String GetNotInContactNumber(String[] Str,
			ArrayList<ContactInfo> list) {
		String re = "";
		for (int i = 0; i < Str.length; i++) {
			if (IsUserNumber(Str[i])) {
				for (int l = 0; l < list.size(); l++) {
					if (Str[i].equals(list.get(l).userNumber)) {
						Str[i] = "";
						break;
					}
				}
				if (!Str[i].equals("")) {
					re += "," + Str[i];
				}
			}
		}
		return re;
	}

	// 处理搜索框选中的手机号
	private void DealWithAutoComplete(ArrayList<ContactInfo> list, String un) {
		for (int i = 0; i < list.size(); i++) {
			if (un.equals(list.get(i).userNumber)) {
				if (!list.get(i).isChecked) {
					list.get(i).isChecked = true;
					numberStr += "," + un;
					textView.setText("");
				}
			}
		}
	}

	// 通讯社按中文拼音排序
	public class Mycomparator implements Comparator {
		public int compare(Object o1, Object o2) {
			ContactInfo c1 = (ContactInfo) o1;
			ContactInfo c2 = (ContactInfo) o2;
			Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
			return cmp.compare(c1.contactName, c2.contactName);
		}

	}

	// 是否为手机号码
	public static boolean IsUserNumber(String num) {
		boolean re = false;
		if (num.length() == 11) {
			if (num.startsWith("13")) {
				re = true;
			} else if (num.startsWith("15")) {
				re = true;
			} else if (num.startsWith("18")) {
				re = true;
			}
		}
		return re;
	}

	// 还原11位手机号
	public static String GetNumber(String num) {
		if (num != null) {
			if (num.startsWith("+86")) {
				num = num.substring(3);
			} else if (num.startsWith("86")) {
				num = num.substring(2);
			}
		} else {
			num = "";
		}
		return num;
	}

	@Override
	protected void onResume() {
		super.onResume();
		mReady = true;
		Log.d(Contants.DEBUG, "contacts  on resume  ");
	}

	@Override
	protected void onPause() {
		super.onPause();
		removeWindow();
		mReady = false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mWindowManager.removeView(mDialogText);
		mReady = false;
	}

	private final class RemoveWindow implements Runnable {
		public void run() {
			removeWindow();
		}
	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		int lastItem = firstVisibleItem + visibleItemCount - 1;
		if (mReady && contactList != null && contactList.size()>0) {
			char firstLetter = contactList.get(firstVisibleItem).getContactName().charAt(0);

			if (!mShowing && firstLetter != mPrevLetter) {

				mShowing = true;
				mDialogText.setVisibility(View.VISIBLE);

			}
			mDialogText.setText(((Character) firstLetter).toString());
			mHandler.removeCallbacks(mRemoveWindow);
			mHandler.postDelayed(mRemoveWindow, 3000);
			mPrevLetter = firstLetter;

		}

	}

	int listPosition;

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// 不滚动时保存当前滚动到的位置
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			/*
			 * if (currentMenuInfo != null) { scrolledX = listView.getScrollX();
			 * scrolledY = listView.getScrollY(); }
			 */
			listPosition = listView.getFirstVisiblePosition();
			System.out.println(Contants.DEBUG + " SCROLL_STATE_IDLE "
					+ listPosition);
		}
	}

	private void removeWindow() {
		if (mShowing) {
			mShowing = false;
			mDialogText.setVisibility(View.INVISIBLE);
		}
	}

	class ContactAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		ArrayList<ContactInfo> itemList;

		public ContactAdapter(Context context, ArrayList<ContactInfo> itemList) {
			mInflater = LayoutInflater.from(context);
			this.itemList = itemList;
		}

		public ArrayList<ContactInfo> getItemList() {
			return itemList;
		}

		public void setItemList(ArrayList<ContactInfo> itemList) {
			this.itemList = itemList;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return itemList.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		// 这个比较特殊,adapter是在页面变化的时候,重新获取当前页面的数据
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;

			convertView = mInflater.inflate(R.layout.list_item_contactsinfo,
					null);
			holder = new ViewHolder();
			holder.mname = (TextView) convertView.findViewById(R.id.mname);
			//holder.mname.setSelected(true);
			holder.msisdn = (TextView) convertView.findViewById(R.id.msisdn);
			//holder.msisdn.setSelected(true);
			holder.check = (CheckBox) convertView.findViewById(R.id.check);

			convertView.setTag(holder);
			
			holder.mname.setText(itemList.get(position).getContactName());
			holder.msisdn.setText(getResources().getString(R.string.phone)
					+ itemList.get(position).getUserNumber());
			holder.check.setChecked(itemList.get(position).getIsChecked());
			
			return convertView;
		}

		class ViewHolder {
			TextView mname;
			TextView msisdn;
			CheckBox check;
		}

		class ViewProgressHolder {
			TextView text;
		}

	}

}
