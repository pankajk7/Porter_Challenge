package com.porter.utils;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

public class AlertMessage {

	Activity activity;
	Toast toast;
	
	public AlertMessage(Activity activity) {
		this.activity = activity;
		toast = new Toast(activity);
	}
	
	public void showTostMessage(String message){
		if (toast.getView() != null) {
			toast.setText(message);
		} else {
			toast = Toast.makeText(activity, message, Toast.LENGTH_LONG);
		}
		toast.show();
	}
	
	public void showTostMessage(int id){
		String message = activity.getResources().getString(id);
		if (toast.getView() != null) {
			toast.setText(message);
		} else {
			toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
		}
		toast.show();
	}
}
