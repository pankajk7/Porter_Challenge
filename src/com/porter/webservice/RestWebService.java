package com.porter.webservice;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.porter.PorterApplication;
import com.porter.R;
import com.porter.utils.Constants;
import com.porter.utils.TransparentProgressDialog;

import android.content.Context;
import android.util.Log;

public class RestWebService {
	
	Context context;
	
	public RestWebService(Context context) {
		baseURL = Constants.BASE_URL;
		urlSuffix = Constants.SUFFIX_URL;
		this.context = context;
		transparentProgressDialog = new TransparentProgressDialog(context,
				R.drawable.needle);
	}
	
	String baseURL;
	String urlSuffix;
	TransparentProgressDialog transparentProgressDialog;


	private String getServiceURL(String resourceName, String extraParameters) {
		return baseURL + urlSuffix + resourceName + extraParameters;
	}

	public void onComplete() {
		if (transparentProgressDialog.isShowing()) {
			transparentProgressDialog.dismiss();
		}
	}


	public void onSuccess(String data) {

	}
	
	public void onError(VolleyError error){
		
	}


	public void serviceCall(String resourceName, String extraParameters, boolean showLoading) {
		String url = getServiceURL(resourceName, extraParameters);
		if(showLoading){
			if(!transparentProgressDialog.isShowing()){
				transparentProgressDialog.show();
			}
		}

		if (resourceName.equalsIgnoreCase(Constants.API_GET_PARCEL_LIST)) {
			getCall(url);
		} else if (resourceName.equalsIgnoreCase(Constants.API_GET_HIT_COUNT)) {
			getCall(url);
		}

	}

	private void getCall(String url) {
		StringRequest req = new StringRequest(Method.GET, url, new Response.Listener<String>() {

			@Override
			public void onResponse(String data) {
				onComplete();
				onSuccess(data);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				onComplete();
				Log.d("Error", error.toString());
				onError(error);
			}
			
		});
		
		// add the request object to the queue to be executed
		PorterApplication.getInstance().getRequestQueue().add(req);
	}

}
