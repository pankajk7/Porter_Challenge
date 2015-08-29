package com.porter.fragments;

import com.porter.entities.Parcel;

import android.app.Activity;
import android.view.View;

public class ParcelLocationInfoFragment{

	Parcel objParcel;
	Activity activity;

	public View getFragmentView(View rootView, Parcel objParcel,
			Activity activity) {
		this.objParcel = objParcel;
		this.activity = activity;
		findViews(rootView);
		setViewsValues();
		return rootView;
	}

	private void findViews(View rootView) {

	}

	private void setViewsValues() {

	}
}
