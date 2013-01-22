package com.feidroid.sms.autoreply.widgets;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.feidroid.sms.autoreply.R;

public class CustomProgressDialog extends ProgressDialog{

	@Override
	public Button getButton(int whichButton) {
		// TODO Auto-generated method stub
		return super.getButton(whichButton);
	}

	@Override
	public ListView getListView() {
		// TODO Auto-generated method stub
		return super.getListView();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public void setButton(CharSequence text, Message msg) {
		// TODO Auto-generated method stub
		super.setButton(text, msg);
	}

	@Override
	public void setButton(CharSequence text, OnClickListener listener) {
		// TODO Auto-generated method stub
		super.setButton(text, listener);
	}

	@Override
	public void setButton(int whichButton, CharSequence text, Message msg) {
		// TODO Auto-generated method stub
		super.setButton(whichButton, text, msg);
	}

	@Override
	public void setButton(int whichButton, CharSequence text,
			OnClickListener listener) {
		// TODO Auto-generated method stub
		super.setButton(whichButton, text, listener);
	}

	@Override
	public void setButton2(CharSequence text, Message msg) {
		// TODO Auto-generated method stub
		super.setButton2(text, msg);
	}

	@Override
	public void setButton2(CharSequence text, OnClickListener listener) {
		// TODO Auto-generated method stub
		super.setButton2(text, listener);
	}

	@Override
	public void setButton3(CharSequence text, Message msg) {
		// TODO Auto-generated method stub
		super.setButton3(text, msg);
	}

	@Override
	public void setButton3(CharSequence text, OnClickListener listener) {
		// TODO Auto-generated method stub
		super.setButton3(text, listener);
	}

	@Override
	public void setCustomTitle(View customTitleView) {
		// TODO Auto-generated method stub
		super.setCustomTitle(customTitleView);
	}

	@Override
	public void setIcon(Drawable icon) {
		// TODO Auto-generated method stub
		super.setIcon(icon);
	}

	@Override
	public void setIcon(int resId) {
		// TODO Auto-generated method stub
		super.setIcon(resId);
	}

	@Override
	public void setInverseBackgroundForced(boolean forceInverseBackground) {
		// TODO Auto-generated method stub
		super.setInverseBackgroundForced(forceInverseBackground);
	}

	@Override
	public void setTitle(CharSequence title) {
		// TODO Auto-generated method stub
		super.setTitle(title);
	}

	@Override
	public void setView(View view, int viewSpacingLeft, int viewSpacingTop,
			int viewSpacingRight, int viewSpacingBottom) {
		// TODO Auto-generated method stub
		super.setView(view, viewSpacingLeft, viewSpacingTop, viewSpacingRight,
				viewSpacingBottom);
	}

	@Override
	public void setView(View view) {
		// TODO Auto-generated method stub
		super.setView(view);
	}

	@Override
	public int getMax() {
		// TODO Auto-generated method stub
		return super.getMax();
	}

	@Override
	public int getProgress() {
		// TODO Auto-generated method stub
		return super.getProgress();
	}

	@Override
	public int getSecondaryProgress() {
		// TODO Auto-generated method stub
		return super.getSecondaryProgress();
	}

	@Override
	public void incrementProgressBy(int diff) {
		// TODO Auto-generated method stub
		super.incrementProgressBy(diff);
	}

	@Override
	public void incrementSecondaryProgressBy(int diff) {
		// TODO Auto-generated method stub
		super.incrementSecondaryProgressBy(diff);
	}

	@Override
	public boolean isIndeterminate() {
		// TODO Auto-generated method stub
		return super.isIndeterminate();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void setIndeterminate(boolean indeterminate) {
		// TODO Auto-generated method stub
		super.setIndeterminate(indeterminate);
	}

	@Override
	public void setIndeterminateDrawable(Drawable d) {
		// TODO Auto-generated method stub
		super.setIndeterminateDrawable(d);
	}

	@Override
	public void setMax(int max) {
		// TODO Auto-generated method stub
		super.setMax(max);
	}

	@Override
	public void setMessage(CharSequence message) {
		// TODO Auto-generated method stub
		super.setMessage(message);
	}

	@Override
	public void setProgress(int value) {
		// TODO Auto-generated method stub
		super.setProgress(value);
	}

	@Override
	public void setProgressDrawable(Drawable d) {
		// TODO Auto-generated method stub
		super.setProgressDrawable(d);
	}

	@Override
	public void setProgressStyle(int style) {
		// TODO Auto-generated method stub
		super.setProgressStyle(style);
	}

	@Override
	public void setSecondaryProgress(int secondaryProgress) {
		// TODO Auto-generated method stub
		super.setSecondaryProgress(secondaryProgress);
	}

	public CustomProgressDialog(Context context) {
		super(context);
	}
	
	public CustomProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_progress);
	}
	
	public static CustomProgressDialog show(Context ctx){
		CustomProgressDialog d = new CustomProgressDialog(ctx);
		d.show();
		return d;
	}
}