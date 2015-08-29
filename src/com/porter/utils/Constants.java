package com.porter.utils;

import java.util.ArrayList;
import java.util.Map;

public class Constants {

	
	// Base Url
	public final static String BASE_URL = "http://porter.0x10.info/api/";
	public final static String SUFFIX_URL = "";
	public final static String URL_TAIL = ".json";
	
	// Fonts
	public final static String FONT_CIRCULAR = "fonts/Circular_Air-Book.ttf";


	
	//Common
	public final static int TIMEOUT = 25000;
	public final static String NO_Internet = "No Internet found. Please enable mobile data or wifi";
	public final static String SHARED_PREF_FILE_NAME = "porter_file";
	public final static String ERROR_MESSAGE = "error_message";
	public final static String PARAMETER_BUNDLE = "bundle";
	
	
	//step2
	public final static String FRAGMENT_TAG_1 = "fragment1";
	public final static String FRAGMENT_TAG_2 = "fragment2";
	
	//Parameters
	public final static String PARAMETER_JSON_FILE_PARCEL_INFO = "parcel_info";
	public final static String PARAMETER_API_HITS = "api_hits";
	public final static String PARAMETER_PARCEL_INFO = "parcel_info";
	
	//Messages
	public final static String SUCCESS = "Success";
	public final static String ERROR = "Error";
	
	public final static String API_GET_PARCEL_LIST = "parcel?type=json&query=list_parcel";
	public final static String API_GET_HIT_COUNT = "parcel?type=json&query=api_hits";
}
