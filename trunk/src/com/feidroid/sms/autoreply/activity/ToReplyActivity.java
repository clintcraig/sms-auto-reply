package com.feidroid.sms.autoreply.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.R.integer;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.feidroid.sms.autoreply.R;
import com.feidroid.sms.autoreply.contants.Contants;
import com.feidroid.sms.autoreply.entity.SmsInfo;
import com.feidroid.sms.autoreply.util.DateUtils;

public class ToReplyActivity extends ListActivity{

	private ArrayList<Map<String, Object>> strPersons = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.sms_bg);
		
		strPersons = new  ArrayList<Map<String, Object>>();
		strPersons = getSMS(Uri.parse(Contants.SMS_URI_INBOX));
		
/*		SMSAdapter smsAdapter = new SMSAdapter(this,getSmsInfo(Uri.parse(Contants.SMS_URI_INBOX)));
*/		SMSAdapter smsAdapter = new SMSAdapter(this,strPersons);
	
		this.setListAdapter(smsAdapter);
		
		
	}
	 private static class SMSAdapter extends BaseAdapter {
	        private LayoutInflater mInflater;
	        private ArrayList<Map<String, Object>> strPersons;
	        //private ArrayList<SmsInfo> strPersons;
	        private Bitmap mIcon1;
	        private Bitmap mIcon2;
	        
	        public SMSAdapter(Context context,ArrayList<Map<String, Object>> strPersons) {
	            // Cache the LayoutInflate to avoid asking for a new one each time.
	            mInflater = LayoutInflater.from(context);
	            //this.strPersons = strPersons;
	            this.strPersons = strPersons;
	            mIcon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon48x48_1);
	            mIcon2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon48x48_2);
	        }

	        /**
	         * The number of items in the list is determined by the number of speeches
	         * in our array.
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
	        public View getView(int position, View convertView, ViewGroup parent) {
	            // A ViewHolder keeps references to children views to avoid unneccessary calls
	            // to findViewById() on each row.
	            ViewHolder holder;

	            // When convertView is not null, we can reuse it directly, there is no need
	            // to reinflate it. We only inflate a new View when the convertView supplied
	            // by ListView is null.
	            if (convertView == null) {
	                convertView = mInflater.inflate(R.layout.list_item_sms, null);

	             // Creates a ViewHolder and store references to the two children views
	                // we want to bind data to.
	                holder = new ViewHolder();
	                holder.text_sms = (TextView) convertView.findViewById(R.id.text_sms);
	                holder.text_sms_last_content = (TextView) convertView.findViewById(R.id.text_sms_last_content);
	                holder.text_date = (TextView) convertView.findViewById(R.id.text_sms_date);
	                holder.icon = (ImageView) convertView.findViewById(R.id.icon_sms);

	                convertView.setTag(holder);
	            } else {
	                // Get the ViewHolder back to get fast access to the TextView
	                // and the ImageView.
	                holder = (ViewHolder) convertView.getTag();
	            }

	            // Bind the data efficiently with the holder.
	            if(strPersons != null && strPersons.size() > 0){
	            	holder.text_sms.setText((String)strPersons.get(position).get(Contants.KEY_SMS_NAME_AND_COUNT));
	            	holder.text_sms_last_content.setText(
	            			(String) strPersons.get(position).get(Contants.KEY_SMS_LAST_CONTENT)
	            			);
	            	holder.text_date.setText((String)strPersons.get(position).get(Contants.KEY_SMS_DATE));
	            	
	            	holder.icon.setImageBitmap((position & 1) == 1 ? mIcon1 : mIcon2);
	            }

	            return convertView;
	        }

	        static class ViewHolder {
	            TextView text_sms;
	            TextView text_sms_last_content;
	            TextView text_date;
	            ImageView icon;
	        }
	    }
	 
	 
	public ArrayList<SmsInfo> getSmsInfo(Uri uri) {
		ArrayList<SmsInfo> infos = new ArrayList<SmsInfo>();
		
		
		String[] projection = new String[] { "_id", "address", "person",
				"body", "date", "type" };
		Cursor cusor = managedQuery(uri, projection, null, null,
				"date desc");
		int nameColumn = cusor.getColumnIndex("person");
		int phoneNumberColumn = cusor.getColumnIndex("address");
		int smsbodyColumn = cusor.getColumnIndex("body");
		int dateColumn = cusor.getColumnIndex("date");
		int typeColumn = cusor.getColumnIndex("type");
		
		if (cusor != null) {
			while (cusor.moveToNext()) {
				SmsInfo smsinfo = new SmsInfo();
				String name = cusor.getString(nameColumn);
				smsinfo.setName(name);
				String date = cusor.getString(dateColumn);
				smsinfo.setDate(date);
				String phoneNumber = cusor.getString(phoneNumberColumn);
				smsinfo.setPhoneNumber(phoneNumber);
				smsinfo.setSmsbody(cusor.getString(smsbodyColumn));
				smsinfo.setType(cusor.getString(typeColumn));
				infos.add(smsinfo);
				
			}
			cusor.close();
		}
		return infos;
	}
	
	
	
	public ArrayList<Map<String, Object>> getSMS(Uri uri) {
		
		ArrayList<Map<String, Object>> strPersons = new  ArrayList<Map<String, Object>>();
		String[] projection = new String[] { "_id", "address", "person",
				"body", "date", "type" };
		Cursor cusor = managedQuery(uri, projection, null, null,
				"date desc");
		int nameColumn = cusor.getColumnIndex("person");
		int phoneNumberColumn = cusor.getColumnIndex("address");
		int smsbodyColumn = cusor.getColumnIndex("body");
		int dateColumn = cusor.getColumnIndex("date");
		int typeColumn = cusor.getColumnIndex("type");
		ArrayList<String> listRepeat = new ArrayList<String>();
		if (cusor != null) {
			while (cusor.moveToNext()) {
				Map<String, Object> map = new HashMap<String,Object>();
				String name = cusor.getString(nameColumn);
				String date = cusor.getString(dateColumn);
				String phoneNumber = cusor.getString(phoneNumberColumn);
				String smsbody = cusor.getString(smsbodyColumn);
				int counts = getCountsByPhoneNumber(phoneNumber,uri);
				System.out.println(
						Contants.DEBUG+" name -- "+name+"\n"+
								Contants.DEBUG+" date -- "+date+"\n"+
								Contants.DEBUG+" phoneNumber -- "+phoneNumber+"\n"
						);
				if(listRepeat != null && listRepeat.contains(phoneNumber)){
					System.out.println(Contants.DEBUG+" don't put ");
				}else{
					listRepeat.add(phoneNumber);
					map.put(Contants.KEY_SMS_NAME_AND_COUNT, (name!=null?name:phoneNumber)+"("+counts+")");
					map.put(Contants.KEY_SMS_LAST_CONTENT, smsbody);
					map.put(Contants.KEY_SMS_DATE, DateUtils.long2String(Long.valueOf(date), DateUtils.TRADITIONAL_DATE_FORMAT));
					strPersons.add(map);
				}
				
				
			}
			cusor.close();
		}
		System.out.println(Contants.DEBUG+" strPersons ----- "+strPersons);
		return strPersons;
		
	}
	private int getCountsByPhoneNumber(String phoneNumber,Uri uri) {
		int count = 0;
		Cursor cursor = managedQuery(uri, null, "address = ?", new String[]{phoneNumber},
				"date desc");
		if(cursor != null){
			count =  cursor.getCount();
		}
		System.out.println(Contants.DEBUG+" count ----- "+count);
		return count;
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
