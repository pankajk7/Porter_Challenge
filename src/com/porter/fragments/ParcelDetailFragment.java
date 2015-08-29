package com.porter.fragments;

import com.android.volley.toolbox.NetworkImageView;
import com.porter.PorterApplication;
import com.porter.R;
import com.porter.entities.Parcel;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

public class ParcelDetailFragment {

	NetworkImageView imageView;

	TextView nameTextView;
	TextView typeTextView;
	TextView weightTextView;
	TextView priceTextView;
	TextView qntyTextView;
	TextView phoneTextView;
	TextView colorTextView;

	View colorView;
	Activity activity;

	Parcel objParcel;

	public View getFragmentView(View rootView, Parcel objParcel,
			Activity activity) {
		this.objParcel = objParcel;
		this.activity = activity;
		findView(rootView);
		setViewsValues();
		return rootView;
	}

	private void findView(View rootView) {
		imageView = (NetworkImageView) rootView
				.findViewById(R.id.imageView_parcelInfo);
		nameTextView = (TextView) rootView
				.findViewById(R.id.textview_parcelInfo_name);
		typeTextView = (TextView) rootView
				.findViewById(R.id.textView_parcelInfo_type);
		priceTextView = (TextView) rootView
				.findViewById(R.id.textView_parcelInfo_value);
		weightTextView = (TextView) rootView
				.findViewById(R.id.textView_parcelInfo_weight);
		qntyTextView = (TextView) rootView
				.findViewById(R.id.textView_parcelInfo_qnty);
		phoneTextView = (TextView) rootView
				.findViewById(R.id.textView_parcelInfo_ph);
		colorView = (View) rootView.findViewById(R.id.view_parcelInfo_color);
	}

	private void setViewsValues() {
		try {
			imageView.setImageUrl(objParcel.getImage(), PorterApplication
					.getInstance().getImageLoader());
			nameTextView.setText(objParcel.getName());
			typeTextView.setText(String.format(activity
					.getResources().getString(R.string.type),
					objParcel.getType()));
			priceTextView.setText(String.format(activity
					.getResources().getString(R.string.price),
					objParcel.getPrice()));
			weightTextView.setText(String.format(activity
					.getResources().getString(R.string.weight_info),
					objParcel.getWeight()));
			qntyTextView.setText(String.format(activity
					.getResources().getString(R.string.qnty),
					objParcel.getQuantity()));
			phoneTextView.setText(String.format(activity
					.getResources().getString(R.string.phone),
					objParcel.getPhone()));
			colorView
					.setBackgroundColor(Color.parseColor(objParcel.getColor()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
