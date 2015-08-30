package com.porter.fragments;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.android.volley.toolbox.NetworkImageView;
import com.porter.PorterApplication;
import com.porter.R;
import com.porter.entities.Parcel;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
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
	TextView dateTextView;
	TextView linkTextView;

	ImageView colorView;
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
		colorView = (ImageView) rootView
				.findViewById(R.id.imageView_parcelInfo_color);
		dateTextView = (TextView) rootView
				.findViewById(R.id.textView_parcelInfo_date);
		linkTextView = (TextView) rootView
				.findViewById(R.id.textView_parcelInfo_link);
	}

	private void setViewsValues() {
		try {
			imageView.setImageUrl(objParcel.getImage(), PorterApplication
					.getInstance().getImageLoader());
			nameTextView.setText(objParcel.getName());
			typeTextView.setText(String.format(activity.getResources()
					.getString(R.string.type), objParcel.getType()));
			priceTextView.setText(String.format(activity.getResources()
					.getString(R.string.price), objParcel.getPrice()));
			weightTextView.setText(String.format(activity.getResources()
					.getString(R.string.weight_info), objParcel.getWeight()));
			qntyTextView.setText(String.format(activity.getResources()
					.getString(R.string.qnty), objParcel.getQuantity()));
			phoneTextView.setText(String.format(activity.getResources()
					.getString(R.string.phone), objParcel.getPhone()));
			dateTextView.setText(String.format(activity.getResources()
					.getString(R.string.date_info), convertToDate(objParcel
					.getDate())));
			linkTextView.setText(String.format(activity.getResources()
					.getString(R.string.link_info), objParcel.getLink()));
			colorView
					.setBackgroundColor(Color.parseColor(objParcel.getColor()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String convertToDate(String timeStamp) {
		try {
			Timestamp stamp = new Timestamp(Long.parseLong(timeStamp));
			Date date = new Date(stamp.getTime());
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			return sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "N/A";
	}
}
