package com.feidroid.sms.autoreply.activity;

import java.util.ArrayList;
import java.util.Map;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.feidroid.sms.autoreply.R;
import com.feidroid.sms.autoreply.contants.Contants;
import com.feidroid.sms.autoreply.util.SMSUtil;
import com.feidroid.sms.autoreply.widgets.CustomProgressDialog;

public class ToReplyActivity extends ListActivity {

	private ArrayList<Map<String, Object>> strPersons = null;
	private SMSAdapter smsAdapter;
	private Button clearBtn;

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {

		// Save away the original text, so we still have it if the activity

		// needs to be killed while paused.

		savedInstanceState.putSerializable(Contants.KEY_LISTVIEW_DATA,
				strPersons);
		super.onSaveInstanceState(savedInstanceState);

		Log.d(Contants.DEBUG, "----onSaveInstanceState----");

	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {

		super.onRestoreInstanceState(savedInstanceState);
		strPersons = (ArrayList<Map<String, Object>>) savedInstanceState.get(Contants.KEY_LISTVIEW_DATA);
		Log.d(Contants.DEBUG, "----onRestoInstanceState----");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.sms_bg);

		
		if (strPersons != null && savedInstanceState != null) {
			smsAdapter = new SMSAdapter(this, strPersons);
			setListAdapter(smsAdapter);
			Log.i(Contants.INFO, "listview data from savedInstanceState");
		}else{
			//Async 
			new InitSMSAsyncTask().execute(this);
			Log.i(Contants.INFO, "listview data from smsinbox");
			
			//sync 
			
		}
		
		
		// listview item onclicklistener
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.setType("vnd.android-dir/mms-sms");
				startActivity(intent);

			}
		});

	}

	private ProgressDialog dialog = null;

	private class InitSMSAsyncTask extends AsyncTask<Context, Void, String> {

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null) {
				if (result.length() > 0) {
					// exception message
				} else {
					smsAdapter = new SMSAdapter(ToReplyActivity.this,
							strPersons);
					setListAdapter(smsAdapter);
				}
			}
			dialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = new ProgressDialog(ToReplyActivity.this);
			dialog.setMessage(getResources().getString(
					R.string.textview_loading));
			dialog.show();
		}

		@Override
		protected String doInBackground(Context... arg0) {
			// TODO Auto-generated method stub
			String result = "";
			try {

				strPersons = SMSUtil.getSMS(arg0[0],
						Uri.parse(Contants.SMS_URI_INBOX));

			} catch (Exception e) {
				result = e.getMessage();
			}
			return result;
		}

	}

	// adapter for listview
	private static class SMSAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<Map<String, Object>> strPersons;
		// private ArrayList<SmsInfo> strPersons;
		private Bitmap mIcon1;
		private Bitmap mIcon2;

		public SMSAdapter(Context context,
				ArrayList<Map<String, Object>> strPersons) {
			// Cache the LayoutInflate to avoid asking for a new one each time.
			mInflater = LayoutInflater.from(context);
			// this.strPersons = strPersons;
			this.strPersons = strPersons;
			mIcon1 = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.icon48x48_1);
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
			return strPersons.size();
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
		public synchronized View getView(int position, View convertView,
				ViewGroup parent) {
			long startTime = System.nanoTime();
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
				convertView = mInflater.inflate(R.layout.list_item_sms, null);

				// Creates a ViewHolder and store references to the two children
				// views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.text_sms = (TextView) convertView
						.findViewById(R.id.text_sms);
				holder.text_sms_last_content = (TextView) convertView
						.findViewById(R.id.text_sms_last_content);
				holder.text_date = (TextView) convertView
						.findViewById(R.id.text_sms_date);
				holder.icon = (ImageView) convertView
						.findViewById(R.id.icon_sms);

				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}

			// Bind the data efficiently with the holder.
			if (strPersons != null && strPersons.size() > 0) {
				holder.text_sms.setText((String) strPersons.get(position).get(
						Contants.KEY_SMS_NAME_AND_COUNT));
				holder.text_sms_last_content.setText((String) strPersons.get(
						position).get(Contants.KEY_SMS_LAST_CONTENT));
				holder.text_date.setText((String) strPersons.get(position).get(
						Contants.KEY_SMS_DATE));

				holder.icon.setImageBitmap((position & 1) == 1 ? mIcon1
						: mIcon2);
			}
			long endTime = System.nanoTime();
			long val = (endTime - startTime) / 1000l;
			Log.i(Contants.INFO, " SMSAdapter " + position + " item cost time ----> "
					+ val);
			return convertView;
		}

		static class ViewHolder {
			TextView text_sms;
			TextView text_sms_last_content;
			TextView text_date;
			ImageView icon;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// new InitSMSAsyncTask().execute(this);
		// System.out.println(Contants.DEBUG+" sms onresume ");
		Log.d(Contants.DEBUG, " SMS onresume ");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}
