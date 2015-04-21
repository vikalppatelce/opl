/**
 * 
 */
package com.application.ui.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.application.ui.view.Crouton;
import com.application.ui.view.Style;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.BuildVars;
import com.application.utils.RequestBuilder;
import com.application.utils.RestClient;
import com.application.utils.Utilities;
import com.rint.topl.R;
import com.flurry.android.FlurryAgent;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class LoginActivity extends ActionBarActivity {
	private static final String TAG = LoginActivity.class.getSimpleName();
	
	private EditText mUserNameEv;
	private EditText mPasswordEv;
	
	private View mUserNameV;
	private View mPasswordV;
	
	private Button mLoginBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setWindowFullScreen();
		setContentView(R.layout.activity_login);
		initUi();
		hideActionBar();
		setUiListener();
		redirectToMotherScreen();
	}

	private void setWindowFullScreen() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	private void initUi() {
		mUserNameEv = (EditText)findViewById(R.id.loginUserName);
		mPasswordEv = (EditText)findViewById(R.id.loginPassword);
		
		mUserNameV = (View)findViewById(R.id.loginUserNameLine);
		mPasswordV = (View)findViewById(R.id.loginPasswordLine);
		
		mLoginBtn = (Button)findViewById(R.id.loginButton);
	}

	private void hideActionBar() {
		try {
			if (Build.VERSION.SDK_INT < 11) {
				getSupportActionBar().hide();
			}
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}
	
	private void redirectToMotherScreen(){
		ApplicationLoader.getPreferences().setWaterMarkListObject(null);
		ApplicationLoader.getPreferences().setTempCount(0);
		if(!TextUtils.isEmpty(ApplicationLoader.getPreferences().getUserId())){
			Intent mIntent = new Intent(LoginActivity.this,MotherActivity.class);
			startActivity(mIntent);
			finish();
		}
	}
	
	private void setUiListener(){
		mUserNameEv.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean isFocused) {
				// TODO Auto-generated method stub
				if(isFocused){
					mUserNameV.setBackgroundDrawable(getResources().getDrawable(R.drawable.line_green));
				}else{
					mUserNameV.setBackgroundDrawable(getResources().getDrawable(R.drawable.line_gray));
				}
			}
		});
		
		mPasswordEv.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean isFocused) {
				// TODO Auto-generated method stub
				if(isFocused){
					mPasswordV.setBackgroundDrawable(getResources().getDrawable(R.drawable.line_green));
				}else{
					mPasswordV.setBackgroundDrawable(getResources().getDrawable(R.drawable.line_gray));
				}
			}
		});
		
		mLoginBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(validate()){
					if(Utilities.isInternetConnected()){
						new AsyncLogin(RequestBuilder.getPostLoginData(mUserNameEv.getText().toString(), mPasswordEv.getText().toString())).execute();
					}else{
						Crouton.makeText(LoginActivity.this, getResources().getString(R.string.no_internet_connection), Style.ALERT).show();
					}
				}
			}
		});
	}
	
	private boolean validate(){
		if(TextUtils.isEmpty(mUserNameEv.getText().toString())){
			Crouton.makeText(LoginActivity.this, "Please Enter Username!", Style.ALERT).show();
			return false;
		}
		
		if(TextUtils.isEmpty(mPasswordEv.getText().toString())){
			Crouton.makeText(LoginActivity.this, "Please Enter Password!", Style.ALERT).show();
			return false;
		}
		
		if (!TextUtils.isEmpty(mUserNameEv.getText().toString())) {
			if (!android.util.Patterns.EMAIL_ADDRESS.matcher(
					mUserNameEv.getText().toString()).matches()) {
				Crouton.makeText(LoginActivity.this,
						"Please Enter Valid Email Address", Style.ALERT).show();
				return false;
			}
		}
		return true;
	}
	
	private void parseJSONFromApi(String mResponseFromApi){
		try{
			JSONObject mJSONObject = new JSONObject(mResponseFromApi);
			if(mJSONObject.getBoolean("success")){
				JSONObject mJSONObjectData = mJSONObject.getJSONObject("data");
				ApplicationLoader.getPreferences().setUserId(mJSONObjectData.getString("user_id"));
				ApplicationLoader.getPreferences().setUserEmailId(mJSONObjectData.getString("email"));
				ApplicationLoader.getPreferences().setApiKey(mJSONObjectData.getString("hash"));
				Intent mIntent = new Intent(LoginActivity.this, MotherActivity.class);
				startActivity(mIntent);
				finish();
			}
		}catch(Exception e){
			Log.i(TAG, e.toString());
		}
	}
	
	public class AsyncLogin extends AsyncTask<Void, Void, Void>{
		private JSONObject mJSONObject;
		private String mResponseFromApi;
		private boolean isSuccess = false;
		private String mErrorMessage;
		private ProgressDialog mProgressDialog;
		
		public AsyncLogin(JSONObject mJSONObject){
			this.mJSONObject = mJSONObject;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = new ProgressDialog(LoginActivity.this);
			mProgressDialog.setMessage("Logging");
			mProgressDialog.show();
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			try {
				mResponseFromApi = RestClient.postJSON(AppConstants.API.URL_POST_LOGIN, mJSONObject);
				JSONObject mJSONObject = new JSONObject(mResponseFromApi);
				if(mJSONObject.getBoolean("success")){
					isSuccess = true;
				}else{
					mErrorMessage = mJSONObject.getString("error");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(Exception ex){
				ex.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(mProgressDialog!=null){
				mProgressDialog.dismiss();
			}
			
			if(isSuccess){
				parseJSONFromApi(mResponseFromApi);
			}else{
				if(!TextUtils.isEmpty(mErrorMessage)){
					Crouton.makeText(LoginActivity.this, mErrorMessage, Style.ALERT).show();
				}
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keycode, KeyEvent e) {
	    switch(keycode) {
	        case KeyEvent.KEYCODE_MENU:
	            return true;
	    }
	    return super.onKeyDown(keycode, e);
	}
	
	/*
	 * Flurry Analytics
	 */
	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, BuildVars.FLURRY_ID);
	}

	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}
}
