package com.porter.fragments;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.porter.R;
import com.porter.activities.ParcelInfoActivity;
import com.porter.entities.Parcel;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ParcelLocationInfoFragment implements OnMapReadyCallback {

	Button shareButton;
	Button smsButton;

	Parcel objParcel;
	Activity activity;

	public View getFragmentView(View rootView, Parcel objParcel,
			Activity activity) {
		this.objParcel = objParcel;
		this.activity = activity;
		init();
		findViews(rootView);
		listeners();
		return rootView;
	}

	private void init() {
		SupportMapFragment mapFragment = (SupportMapFragment) ((ParcelInfoActivity) activity)
				.getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
	}

	private void findViews(View rootView) {
		shareButton = (Button) rootView
				.findViewById(R.id.button_parcelLocation_share);
		smsButton = (Button) rootView
				.findViewById(R.id.button_parcelLocation_sms);
	}

	private void listeners() {
		shareButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(
						Intent.EXTRA_TEXT,
						"Parcel information as follows: \n"
								+ objParcel.toString()); // overriding toString
															// in Parcel modal
															// to return all
															// information
				sendIntent.setType("text/plain");
				activity.startActivity(Intent.createChooser(sendIntent,
						activity.getResources()
								.getText(R.string.share_location)));
			}
		});

		smsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog();
			}
		});
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		if (googleMap != null) {
			addMarkerAnimateCamera(googleMap);
		}
	}

	private void addMarkerAnimateCamera(GoogleMap googleMap) {
		try {
			double lat = Double.parseDouble(objParcel.getLive_location()
					.getLatitude());
			double lng = Double.parseDouble(objParcel.getLive_location()
					.getLongitude());
			LatLng latlng = new LatLng(lat, lng);
			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(latlng).zoom(9).build();

			MarkerOptions markerOption = new MarkerOptions().position(latlng)
					.alpha(0.7f);
			markerOption.title(objParcel.getName());
			googleMap.addMarker(markerOption);
			googleMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showDialog() {
		final Dialog dialog = new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.sms_dialog_layout);
		dialog.setCancelable(false);
		
		final EditText mobileEditText = (EditText) dialog
				.findViewById(R.id.editText_sms_phone1);
		final TextView messageTextView = (TextView) dialog
				.findViewById(R.id.textView_sms_message);
		TextView noteTextView = (TextView)dialog.findViewById(R.id.textView_sms_note);
		
		Button sendButton = (Button) dialog.findViewById(R.id.button_sms_send);
		Button cancelButton = (Button) dialog
				.findViewById(R.id.button_sms_cancel);
		messageTextView.setText(objParcel.getLink());

		String formatString = activity.getResources().getString(R.string.note);
		noteTextView.setText(Html.fromHtml(formatString));
		
		sendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String phoneString = mobileEditText.getText().toString().trim();
				if (phoneString.isEmpty()) {
					mobileEditText.setError(activity.getResources().getString(
							R.string.phone_empty));
					return;
				}
				if (phoneString.length() < 10) {
					mobileEditText.setError(activity.getResources().getString(
							R.string.phone_digit_validation));
					return;
				} else {
					sendSMS(phoneString, objParcel.getLink());
					dialog.dismiss();
				}
			}
		});

		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private void sendSMS(String phoneNumber, String message) {
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";

		PendingIntent sentPI = PendingIntent.getBroadcast(activity, 0,
				new Intent(SENT), 0);

		PendingIntent deliveredPI = PendingIntent.getBroadcast(activity, 0,
				new Intent(DELIVERED), 0);

		// ---when the SMS has been sent---
		activity.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (this.getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(activity, "SMS sent", Toast.LENGTH_SHORT)
							.show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(activity, "Generic failure",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(activity, "No service", Toast.LENGTH_SHORT)
							.show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(activity, "Null PDU", Toast.LENGTH_SHORT)
							.show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(activity, "Radio off", Toast.LENGTH_SHORT)
							.show();
					break;
				}
			}
		}, new IntentFilter(SENT));

		// ---when the SMS has been delivered---
		activity.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent arg1) {
				switch (this.getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(activity, "SMS delivered",
							Toast.LENGTH_SHORT).show();
					break;
				case Activity.RESULT_CANCELED:
					Toast.makeText(activity, "SMS not delivered",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(DELIVERED));

		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
	}
}
