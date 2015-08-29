package com.porter.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.porter.R;
import com.porter.activities.ParcelInfoActivity;
import com.porter.adapters.ListBaseAdapter;
import com.porter.entities.Parcel;
import com.porter.entities.ParcelListInfo;
import com.porter.utils.AlertMessage;
import com.porter.utils.ConnectionDetector;
import com.porter.utils.Constants;
import com.porter.utils.ReadWriteJsonFileUtils;
import com.porter.webservice.RestWebService;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ParcelListFragment extends Fragment {
	ListView listView;
	TextView apiHitsTextView;
	TextView totalParcelTextView;
	Button priceButton;
	Button ratingButton;
	Button listAllButton;
	EditText searchEditText;

	List<Parcel> parcelInfoList;
	ListBaseAdapter<?> adapter;

	List<Parcel> tempList = new ArrayList<Parcel>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		init();
		View rootView = inflater.inflate(R.layout.list_parcel_layout,
				container, false);
		findViews(rootView);
		listeners();
		hideKeypad();
		getList();
		getApiHits();
		return rootView;
	}

	private void init() {
		parcelInfoList = new ArrayList<Parcel>();
	}

	private void findViews(View rootView) {
		listView = (ListView) rootView.findViewById(R.id.listview_listCars);
		apiHitsTextView = (TextView) rootView
				.findViewById(R.id.textView_listParcels_apiHit);
		totalParcelTextView = (TextView) rootView
				.findViewById(R.id.textView_listParcels_totalParcels);
		priceButton = (Button) rootView
				.findViewById(R.id.button_listParcels_price);
		ratingButton = (Button) rootView
				.findViewById(R.id.button_listParcels_weight);
		searchEditText = (EditText) rootView.findViewById(R.id.editText_search);
		listAllButton = (Button) rootView
				.findViewById(R.id.button_listParcels_listAll);
	}

	private void listeners() {
		listAllButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter.addList(parcelInfoList);
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Parcel objParcel = (Parcel) parent.getAdapter().getItem(
						position);
				Intent intent = new Intent(getActivity(),
						ParcelInfoActivity.class);
				intent.putExtra(Constants.PARAMETER_PARCEL_INFO, objParcel);
				getActivity().startActivity(intent);
			}
		});

		priceButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Collections.sort(parcelInfoList, byValue());
				setAdapter();
			}
		});

		ratingButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Collections.sort(parcelInfoList, byWeight());
				setAdapter();
			}
		});

		searchEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				tempList = new ArrayList<Parcel>();
			}

			@Override
			public void afterTextChanged(Editable s) {
				String string = s.toString();
				if (s.length() > 1) {
					if (parcelInfoList != null) {
						for (int i = 0; i < parcelInfoList.size(); i++) {
							string = string.toLowerCase();
							String name = parcelInfoList.get(i).getName()
									.toLowerCase();
							String price = parcelInfoList.get(i).getPrice()
									.toLowerCase();
							String weight = parcelInfoList.get(i).getWeight()
									.toLowerCase();
							if (name.startsWith(string)) {
								tempList.add(parcelInfoList.get(i));
							} else if (price.contains(string)) {
								tempList.add(parcelInfoList.get(i));
							} else if (weight.contains(string)) {
								tempList.add(parcelInfoList.get(i));
							}
						}
						adapter.addList(tempList);
					}
				} else {
					adapter.addList(parcelInfoList);
				}
			}
		});
	}

	private Comparator<Parcel> byValue() {
		return new Comparator<Parcel>() {
			@Override
			public int compare(Parcel lhs, Parcel rhs) {
				Float fLHS = Float.parseFloat(lhs.getPrice());
				Float fRHS = Float.parseFloat(rhs.getPrice());
				return fRHS.compareTo(fLHS);
			}
		};
	}

	private Comparator<Parcel> byWeight() {
		return new Comparator<Parcel>() {
			@Override
			public int compare(Parcel lhs, Parcel rhs) {
				Float fLHS = Float.parseFloat(lhs.getWeight());
				Float fRHS = Float.parseFloat(rhs.getWeight());
				return fRHS.compareTo(fLHS);
			}
		};
	}

	private void getList() {
		boolean showLoading = true;

		// for checking data in file if exist
		String data = new ReadWriteJsonFileUtils(getActivity())
				.readJsonFileData(Constants.PARAMETER_JSON_FILE_PARCEL_INFO);
		// if there is data then assign to arraylist
		if (data != null) {
			ParcelListInfo objParcelListInfo = new Gson().fromJson(data, ParcelListInfo.class);
			parcelInfoList = new ArrayList<Parcel>(Arrays.asList(objParcelListInfo.getParcels()));
			if (parcelInfoList.size() > 0) {
				setAdapter();
				totalParcelTextView.setText(String.format(getActivity()
						.getResources().getString(R.string.total_parcels),
						parcelInfoList.size()));
			}
			showLoading = false;
		}

		if (!new ConnectionDetector(getActivity()).isConnectedToInternet()) {
			new AlertMessage(getActivity())
					.showTostMessage(Constants.NO_Internet);
			return;
		}

		callAPIParcelInfo(showLoading);
	}

	private void callAPIParcelInfo(final boolean showLoading) {
		new RestWebService(getActivity()) {
			public void onSuccess(String data) {
				if (showLoading) {
					ParcelListInfo objParcelListInfo = new Gson()
							.fromJson(data, ParcelListInfo.class);
					parcelInfoList = new ArrayList<Parcel>(Arrays.asList(objParcelListInfo.getParcels()));
					new ReadWriteJsonFileUtils(getActivity())
							.createJsonFileData(
									Constants.PARAMETER_JSON_FILE_PARCEL_INFO,
									data);
					setAdapter();
					totalParcelTextView.setText(String.format(getActivity()
							.getResources().getString(R.string.total_parcels),
							parcelInfoList.size()));
				} else {
					new ReadWriteJsonFileUtils(getActivity())
							.createJsonFileData(
									Constants.PARAMETER_JSON_FILE_PARCEL_INFO,
									data);
				}
			};

			public void onError(VolleyError error) {
				Log.d("Error", error.toString());
			};
		}.serviceCall(Constants.API_GET_PARCEL_LIST, "", showLoading);
	}

	private void getApiHits() {
		if (!new ConnectionDetector(getActivity()).isConnectedToInternet()) {
			return;
		}

		new RestWebService(getActivity()) {
			public void onSuccess(String data) {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(data);
					String apiHits = jsonObject
							.getString(Constants.PARAMETER_API_HITS);
					apiHitsTextView.setText(String.format(getActivity()
							.getResources().getString(R.string.api_hit),
							apiHits));
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			};

			public void onError(VolleyError error) {
				Log.d("Error", error.toString());
			};
		}.serviceCall(Constants.API_GET_HIT_COUNT, "", false); // false for not
																// showing
																// loading
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setAdapter() {
		adapter = new ListBaseAdapter(getActivity(), parcelInfoList){
			@Override
			public void setValues(ViewHolder holder, Object object) {
				super.setValues(holder, object);
				if(object instanceof Parcel){
					Parcel obj = (Parcel) object;
					holder.nameTextView.setText(obj.getName()+", "+obj.getWeight());
		
					Resources res = getResources();
					String inapp = String.format(res.getString(R.string.rupee),
							obj.getPrice());
		
					holder.priceTextView.setText(inapp);
					
				}
			}
		};
		listView.setAdapter(adapter);
	}

	private void hideKeypad() {
		View view = getActivity().getCurrentFocus();

		InputMethodManager inputManager = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (view instanceof EditText) {
			inputManager.hideSoftInputFromWindow(getActivity()
					.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

}
