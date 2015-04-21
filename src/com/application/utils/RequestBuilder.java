package com.application.utils;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

public class RequestBuilder {

	private static final String TAG = RequestBuilder.class.getSimpleName();

	public static JSONObject getPostFailMessage() {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put("success", false);
			stringBuffer.put("message", "Please Try Again!");
			stringBuffer.put("error", "Please Try Again!");
			stringBuffer.put("messageError", "Please Try Again!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuffer;
	}

	public static JSONObject getPostLoginData(String mUserName, String mPassword) {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put("email", mUserName);
			stringBuffer.put("user_password", mPassword);
			stringBuffer.put("action", "loginvalidate");
			if (BuildVars.DEBUG_IMEI) {
				stringBuffer.put("device_id", "124123412423");
			} else {
				stringBuffer.put("device_id", Utilities.getDeviceId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuffer;
	}
	
	
	public static JSONObject getPostLogoData() {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put("user_id", ApplicationLoader.getPreferences().getUserId());
			stringBuffer.put("action", "getbrand");
			if (BuildVars.DEBUG_IMEI) {
				stringBuffer.put("device_id", "124123412423");
			} else {
				stringBuffer.put("device_id", Utilities.getDeviceId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuffer;
	}

}
