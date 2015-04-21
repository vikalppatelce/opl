package com.application.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;

import com.application.utils.BuildVars;
import com.rint.topl.R;
import com.flurry.android.FlurryAgent;


public class SplashActivity extends ActionBarActivity {
	private static final String TAG = SplashActivity.class.getSimpleName();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindowFullScreen();
        setContentView(R.layout.activity_splash);
		initUi();
		hideActionBar();
		redirectToLoginScreen();
    }
    
	private void initUi() {
	}
	
	private void hideActionBar(){
		try{
			if(Build.VERSION.SDK_INT < 11){
				getSupportActionBar().hide();
			}
		}catch(Exception e){
			Log.i(TAG, e.toString());
		}
	}

	private void redirectToLoginScreen() {
		Thread timer = new Thread() {
			public void run() {
				try {
					sleep(2000);
					intentToLoginScreen();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
				}
			}
		};
		timer.start();
	}

	private void intentToLoginScreen() {
			Intent mIntent = new Intent(SplashActivity.this, LoginActivity.class);
			startActivity(mIntent);
			finish();
	}

	private void setWindowFullScreen() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
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
