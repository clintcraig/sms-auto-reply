package com.feidroid.sms.autoreply.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.feidroid.sms.autoreply.R;
import com.feidroid.sms.autoreply.contants.Contants;
import com.feidroid.sms.autoreply.entity.ContactsInfoBean;
import com.feidroid.sms.autoreply.provider.tables.ContactsInfoMeta;
import com.feidroid.sms.autoreply.util.ContactsUtil;
import com.feidroid.sms.autoreply.util.GetContentValuesFromBean;
import com.feidroid.sms.autoreply.widgets.CustomProgressDialog;

public class ContactsActivity extends ListActivity implements
		ListView.OnScrollListener {

	private RemoveWindow mRemoveWindow = new RemoveWindow();
	Handler mHandler = new Handler();
	private WindowManager mWindowManager;
	private TextView mDialogText;
	private boolean mShowing;
	private boolean mReady;
	private char mPrevLetter = Character.MIN_VALUE;
	private ArrayList<Map<String, Object>> contactsDataIdAndDisplayName;
	private ArrayList<Map<String, Object>> contactsData;
	private EfficientAdapter efficientAdapter = null;
	private Handler checkboxHandler;
	private Handler insertHandler;

	private static class EfficientAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private Bitmap mIcon2;
		private ArrayList<Map<String, Object>> contactsData;
		private Handler checkboxHandler;

		public EfficientAdapter(Context context,
				ArrayList<Map<String, Object>> contactsData,
				Handler checkboxHandler

		) {

			this.contactsData = contactsData;
			this.checkboxHandler = checkboxHandler;
			// Cache the LayoutInflate to avoid asking for a new one each time.
			mInflater = LayoutInflater.from(context);

			// Icons bound to the rows.
			mIcon2 = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.icon48x48_2);
		}

		/**
		 * The number of items in the list is determined by the number of
		 * speeches in our array.
		 * 
		 * @see android.widget.ListAdapter#getCount()
		 */
		public int getCount() {
			if (contactsData != null) {
				return contactsData.size();
			}
			return 0;
		}

		/**
		 * Since the data comes from an array, just returning the index is
		 * sufficent to get at the data. If we were using a more complex data
		 * structure, we would return whatever object represents one row in the
		 * list.
		 * 
		 * @see android.widget.ListAdapter#getItem(int)
		 */
		public Object getItem(int position) {
			return position;
		}

		/**
		 * Use the array index as a unique id.
		 * 
		 * @see android.widget.ListAdapter#getItemId(int)
		 */
		public long getItemId(int position) {
			return position;
		}

		/**
		 * Make a view to hold each row.
		 * 
		 * @see android.widget.ListAdapter#getView(int, android.view.View,
		 *      android.view.ViewGroup)
		 */
		public synchronized View getView(final int position, View convertView,
				final ViewGroup parent) {
			// A ViewHolder keeps references to children views to avoid
			// unneccessary calls
			// to findViewById() on each row.
			ViewHolder holder;

			// When convertView is not null, we can reuse it directly, there is
			// no need
			// to reinflate it. We only inflate a new View when the convertView
			// supplied
			// by ListView is null.
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.list_item_icon_text,
						null);

				// Creates a ViewHolder and store references to the two children
				// views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.text = (TextView) convertView.findViewById(R.id.text);
				holder.textId = (TextView) convertView
						.findViewById(R.id.textId);
				holder.icon = (ImageView) convertView.findViewById(R.id.icon);
				holder.checkbox = (CheckBox) convertView
						.findViewById(R.id.checkbox);
				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}

			if (holder != null) {
				// Bind the data efficiently with the holder.
				if (contactsData.size() > 0 && position < contactsData.size()) {
					final String displayName = (String) contactsData.get(
							position).get(Contants.KEY_CONTACTS_DISPLAYNAME);
					holder.text.setText(displayName);
					holder.textId.setText((String) contactsData.get(position)
							.get(Contants.KEY_CONTACTS_ID));

					holder.icon.setImageBitmap(contactsData.get(position).get(
							Contants.KEY_CONTACTS_PHOTOBMP) == null ? mIcon2
							: (Bitmap) contactsData.get(position).get(
									Contants.KEY_CONTACTS_PHOTOBMP));
					final boolean isChecked = (Boolean) contactsData.get(
							position).get(Contants.KEY_CONTACTS_ISCHECKED);
					holder.checkbox.setChecked(isChecked);
					holder.checkbox.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {

							if (isChecked) {
								contactsData.get(position).put(
										Contants.KEY_CONTACTS_ISCHECKED, false);
							} else {
								contactsData.get(position).put(
										Contants.KEY_CONTACTS_ISCHECKED, true);
							}
							// checkboxHandler.sendEmptyMessage(0);
						}
					});
				}
			}
			return convertView;
		}

		static class ViewHolder {
			TextView text;
			TextView textId;
			ImageView icon;
			CheckBox checkbox;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

		// Use an existing ListAdapter that will map an array
		// of strings to TextViews
		/*
		 * ArrayAdapter<Object> arrayAdapter = new ArrayAdapter<Object>(this,
		 * android.R.layout.simple_list_item_1, DATA);
		 */
		_setListView();

		/*
		 * efficientAdapter = new
		 * EfficientAdapter(this,contactsData,checkboxHandler);
		 * setListAdapter(efficientAdapter);
		 */

		new InitDataAsyncTask().execute(this);
		checkboxHandler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Log.i(Contants.DEBUG, "refreshing...");
				new RefreshContactsDataAsyncTask().execute();
				Log.i(Contants.DEBUG, "refreshing  completed");
			}
		};

		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mDialogText = (TextView) inflate.inflate(R.layout.list_position, null);
		mDialogText.setVisibility(View.INVISIBLE);

		getListView().setOnScrollListener(this);
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

		insertHandler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Log.i(Contants.DEBUG, "inserting contactsinfo to database...");
				_insertContactsInfo();
				Log.i(Contants.DEBUG, "insert into table complete...");
			}
		};

	}

	private void _insertContactsInfo() {

		contactsData = ContactsUtil._setContactsData(this);
		List<ContactsInfoBean> list = _getContactInfoBean(contactsData);
		for (ContactsInfoBean contactsInfoBean : list) {
			if (!ContactsUtil._existByContactsId(contactsInfoBean, this)) {

				Uri uri = getContentResolver().insert(
						ContactsInfoMeta.CONTENT_URI,
						GetContentValuesFromBean
								.getContactsValues(contactsInfoBean));
				if (uri != null) {
					//System.out.println(Contants.DEBUG +">"+ contactsInfoBean.getContactId()+" uri  isnert success ----> " + uri);
				}
			}
			else {
				int update = getContentResolver().update(
						ContactsInfoMeta.CONTENT_URI,
						GetContentValuesFromBean
								.getContactsValues(contactsInfoBean), ContactsInfoMeta.CONTACTSINFO_ID+" = ?",
						new String[]{contactsInfoBean.getContactId()});
				//System.out.println(Contants.DEBUG + " update success ----->" + update);

			}
		}

	}

	private List<ContactsInfoBean> _getContactInfoBean(
			ArrayList<Map<String, Object>> contactsData2) {
		// TODO Auto-generated method stub
		List<ContactsInfoBean> list = new ArrayList<ContactsInfoBean>();
		for (Map<String, Object> map : contactsData2) {

			ContactsInfoBean cib = new ContactsInfoBean();
			// System.out.println(Contants.DEBUG+" _ID -----> "+
			// (String)map.get(Contants.KEY_CONTACTS_ID));
			cib.setContactId((String) map.get(Contants.KEY_CONTACTS_ID));
			cib.setContactDisplayName((String) (map
					.get(Contants.KEY_CONTACTS_DISPLAYNAME)));
			cib.setContactPhoneNums((String) map
					.get(Contants.KEY_CONTACTS_PHONENUMS));
			cib.setContactshouldRely((Boolean) (map
					.get(Contants.KEY_CONTACTS_ISCHECKED)) == false ? "true"
					: "false");
			cib.setContactEmail((String) map.get(Contants.KEY_CONTACTS_EMAILS));
			cib.setContactIMs((String) map.get(Contants.KEY_CONTACTS_IMS));
			list.add(cib);
		}
		return list;
	}

	private CustomProgressDialog loadingDialog = null;

	private class RefreshContactsDataAsyncTask extends
			AsyncTask<Context, Void, String> {
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null) {
				if (result.length() > 0) {
					Toast.makeText(ContactsActivity.this,
							Contants.ERROR + " " + result, Toast.LENGTH_SHORT)
							.show();
				} else {
					efficientAdapter.notifyDataSetChanged();
					// setListAdapter(efficientAdapter);
				}
			}
			loadingDialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			loadingDialog = new CustomProgressDialog(ContactsActivity.this);
			loadingDialog.show();

		}

		@Override
		protected String doInBackground(Context... params) {
			// TODO Auto-generated method stub
			String resultString = "";
			try {

			} catch (Exception e) {
				resultString = e.getMessage();
			}
			return resultString;
		}
	}

	/**
	 * @author feidroid InitDataAsyncTask
	 */
	private class InitDataAsyncTask extends AsyncTask<Context, Void, String> {

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null) {
				if (result.length() > 0) {
					Toast.makeText(ContactsActivity.this,
							Contants.ERROR + " " + result, Toast.LENGTH_SHORT)
							.show();
				} else {
					efficientAdapter = new EfficientAdapter(
							ContactsActivity.this, contactsDataIdAndDisplayName,
							checkboxHandler);
					setListAdapter(efficientAdapter);
				    insertHandler.sendEmptyMessage(0);
				}
			}
			loadingDialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			loadingDialog = new CustomProgressDialog(ContactsActivity.this);
			loadingDialog.show();

		}

		@Override
		protected String doInBackground(Context... params) {
			// TODO Auto-generated method stub
			String resultString = "";
			try {
				contactsDataIdAndDisplayName = ContactsUtil
						._setContactsIdAndDisplayName(params[0]);
				System.out.println(Contants.DEBUG
						+ " contactsDataIdAndDisplayName size  ---> "
						+ contactsDataIdAndDisplayName.size());
			} catch (Exception e) {
				resultString = e.getMessage();
			}
			return resultString;
		}

	}

	private void _setListView() {
		// TODO Auto-generated method stub
		/* 快速滚动滑块 */
		getListView().setFastScrollEnabled(true);
		/* false:点击某条记录不放，颜色会在记录的后面，成为背景色，但是记录内容的文字是可见的,true则相反 */
		getListView().setDrawSelectorOnTop(false);
		/* 实现滚动条的自动隐藏和显示 */
		getListView().setFadingEdgeLength(0);

		getListView().setCacheColorHint(Color.TRANSPARENT);

		getListView().setTranscriptMode(
				AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		/* 设置了该属性之后你做好的列表就会显示你列表的最下面 */
		// getListView().setStackFromBottom(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		// Inflate the currently selected menu XML resource.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.title_icon, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// For "Title only": Examples of matching an ID with one assigned in
		// the XML
		case R.id.select_all_contacts:
			Toast.makeText(this,
					getResources().getString(R.string.select_all_checkbox),
					Toast.LENGTH_SHORT).show();
			_selectAllContacts();
			return true;

		case R.id.deselect_all_contacts:
			Toast.makeText(this,
					getResources().getString(R.string.deselect_all_checkbox),
					Toast.LENGTH_SHORT).show();
			_deselectAllContacts();
			return true;
		case R.id.search_contacts:
			// The refresh item is part of the browser group
			Toast.makeText(this,
					getResources().getString(R.string.searchform_all_checkbox),
					Toast.LENGTH_SHORT).show();
			_searchFromAllContacts();
			return true;
		}
		return false;
	}

	private void _searchFromAllContacts() {
		// TODO search contacts

	}

	private void _deselectAllContacts() {
		int count = getListView().getCount();
		for (int index = 0; index < count; index++) {
			contactsDataIdAndDisplayName.get(index).put(Contants.KEY_CONTACTS_ISCHECKED, false);
		}
		checkboxHandler.sendEmptyMessage(0);
		//取消全选，true 允许自动回复
		_updateContactsInfo("true");
		
	}

	private void _selectAllContacts() {
		// TODO select all contacts
		int count = getListView().getCount();
		for (int i = 0; i < count; i++) {
			contactsDataIdAndDisplayName.get(i).put(Contants.KEY_CONTACTS_ISCHECKED, true);
		}
		 System.out.println(Contants.DEBUG+" count ----> "+count
		 +" --------> ");
		checkboxHandler.sendEmptyMessage(0);
		//全选   "false"将contacts_shouldreply  = "false"
		_updateContactsInfo("false");
	}

	public void _updateContactsInfo(String string) {
		
		ContentValues values = new  ContentValues();
		values.put(ContactsInfoMeta.CONTACTSINFO_SHOULDREPLY, string);
				int update = getContentResolver().update(
						ContactsInfoMeta.CONTENT_URI
						, values
						,null,
						null);
		System.out.println(Contants.DEBUG + " update success ----->" + update);
	}


	@Override
	protected void onResume() {
		super.onResume();
		mReady = true;
		Intent intent = getIntent();
		if("com.feidroid.sms.autoreply.action.CONTACTSINFO"
				.equals(intent.getAction())){
			contactsDataIdAndDisplayName = ContactsUtil._setContactsIdAndDisplayName(this);
			efficientAdapter.notifyDataSetChanged();
			System.out.println(Contants.DEBUG + " come back......  ");
		}
		System.out.println(Contants.DEBUG + " on resume  ");
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
		if (mReady) {
			/* char firstLetter = DATA[firstVisibleItem].charAt(0); */

			if (contactsData != null && contactsData.size() > 0
					&& firstVisibleItem < contactsData.size()) {

				char firstLetter = ((String) contactsData.get(firstVisibleItem)
						.get(Contants.KEY_CONTACTS_DISPLAYNAME)).charAt(0);
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
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	private void removeWindow() {
		if (mShowing) {
			mShowing = false;
			mDialogText.setVisibility(View.INVISIBLE);
		}
	}

}
