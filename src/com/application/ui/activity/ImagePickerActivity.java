/**
 * 
 */
package com.application.ui.activity;

import it.sephiroth.android.library.widget.AdapterView;
import it.sephiroth.android.library.widget.AdapterView.OnItemClickListener;
import it.sephiroth.android.library.widget.HListView;

import java.io.File;
import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.beans.WaterMark;
import com.application.ui.view.HListViewAdapter;
import com.application.ui.view.ViewPagerAdapter;
import com.application.utils.ApplicationLoader;
import com.application.utils.BuildVars;
import com.application.utils.Utilities;
import com.rint.topl.R;
import com.flurry.android.FlurryAgent;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
public class ImagePickerActivity extends ActionBarActivity{
	private static final String TAG = ImagePickerActivity.class.getSimpleName();
	
	private TextView mActionBarTitleTv;
	private ImageView mActionBarForward;
	
	private HListView mHListView;
	
	private Button mCancelIv;
	private Button mDoneIv;
	
	private ViewPager mPager;
	
	private Intent mIntent;
	private ArrayList<WaterMark> mListWaterMark;
	
	private String mImagePath[];
	
	private HListViewAdapter mAdapter;
	private ViewPagerAdapter mPagerAdapter;
	
	private ViewPager.SimpleOnPageChangeListener ViewPagerListener;
	
	private ImageLoader imageLoader;
	
	private boolean isSelectedFromHListView = false;
	private boolean isSelectedFromPager = false;
	
	private boolean isToFocusOnNewImageAdded = false;
	private int isToFocusOnNewImageWhere = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setWindowFullScreen();
		setContentView(R.layout.activity_image_pager);
		initCustomActionBar();
		initUi();
		initImageLoader();
		getIntentData();
		setViewPager();
		setUiListener();
	}
	
	private void setWindowFullScreen() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	private void initCustomActionBar(){
		mActionBarTitleTv = (TextView)findViewById(R.id.actionBarTitle);
		mActionBarForward = (ImageView)findViewById(R.id.actionBarForward);
		hideActionBar();
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
	
	private void initUi(){
		mHListView = (HListView)findViewById(R.id.HListView);
		mPager = (ViewPager)findViewById(R.id.pager);
		
		mDoneIv = (Button)findViewById(R.id.done);
		mCancelIv = (Button)findViewById(R.id.cancel);
	}
	
	private void getIntentData(){
		mIntent = getIntent();
		try{
			mImagePath = mIntent.getStringArrayExtra("all_path");
			addObjectsToWaterMarkList();
			setHListAdapter();
			if(isToFocusOnNewImageAdded && isToFocusOnNewImageWhere > 0){
				mAdapter.setSelectionImageQueue(isToFocusOnNewImageWhere);
				mAdapter.notifyDataSetChanged();
				mHListView.setSelection(isToFocusOnNewImageWhere);
			}
		}catch(Exception e){
			e.toString();
		}
	}
	
	private void addObjectsToWaterMarkList(){
		mListWaterMark = Utilities.retrieveArrayListFromPreferencesUsingGSON();
		if (mListWaterMark!=null && mListWaterMark.size() > 0) {
			isToFocusOnNewImageAdded = true;
			isToFocusOnNewImageWhere = mListWaterMark.size();
			int counter = 0;
			int mSizeOfListWaterMark = mListWaterMark.size();
			for (int i = mListWaterMark.size(); i < mImagePath.length + mSizeOfListWaterMark; i++) {
				WaterMark obj = new WaterMark();
				obj.setmImagePath(mImagePath[counter]);
				mListWaterMark.add(obj);
				counter++;
			}
		}else{
			mListWaterMark = new ArrayList<WaterMark>();
			for(int i = 0 ; i < mImagePath.length ;i++){
				WaterMark obj = new WaterMark();
				obj.setmImagePath(mImagePath[i]);
				mListWaterMark.add(obj);
			}
		}
	}
	
	private void setHListAdapter(){
		if(mListWaterMark!=null && mListWaterMark.size() > 0){
			mAdapter = new HListViewAdapter(ImagePickerActivity.this, mListWaterMark, imageLoader);
			mHListView.setAdapter(mAdapter);
		}
	}
	
	private void setViewPager(){
		mPagerAdapter = null;
		mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mListWaterMark, imageLoader);
		mPager.setAdapter(mPagerAdapter);
		setViewPagerListener();
		mPager.setOnPageChangeListener(ViewPagerListener);
		setViewPagerToItem();
	}
	
	
	private void setViewPagerToItem() {
		try {
			if (isToFocusOnNewImageAdded && isToFocusOnNewImageWhere > 0) {
				mPager.setCurrentItem(isToFocusOnNewImageWhere);
			}
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}
	
	private void setUiListener(){
		mHListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				isSelectedFromHListView = true;
//				if(!isSelectedFromPager){
				mPager.setCurrentItem(position);	
//				}
				isSelectedFromHListView = false;
			}
		});
		
		mCancelIv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Utilities.saveArrayListInPreferencesUsingGSON(null);
				ApplicationLoader.getPreferences().setTempCount(0);
				Intent mIntent = new Intent(ImagePickerActivity.this,MotherActivity.class);
				mIntent.putParcelableArrayListExtra("mArrayList<WaterMark>", mListWaterMark);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(mIntent);
				finish();
			}
		});
		
		mDoneIv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Utilities.saveArrayListInPreferencesUsingGSON(mListWaterMark);
				Intent mIntent = new Intent(ImagePickerActivity.this,SelectLogoActivity.class);
				mIntent.putParcelableArrayListExtra("mArrayList<WaterMark>", mListWaterMark);
				startActivity(mIntent);
				finish();
			}
		});
		
		mActionBarForward.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Utilities.saveArrayListInPreferencesUsingGSON(mListWaterMark);
				Intent mIntent = new Intent(ImagePickerActivity.this,SelectLogoActivity.class);
				mIntent.putParcelableArrayListExtra("mArrayList<WaterMark>", mListWaterMark);
				startActivity(mIntent);
				finish();
			}
		});
	}
	
	private void setViewPagerListener(){
		ViewPagerListener = new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				super.onPageSelected(position);
				// Find the ViewPager Position
//				if(!isSelectedFromHListView){
					isSelectedFromPager = true;
					mAdapter.setSelectionImageQueue(position);
					mAdapter.notifyDataSetChanged();
					mHListView.setSelection(position);
					isSelectedFromPager = false;
//				}
			}
		};
	}

	private void initImageLoader() {
		try {
			String CACHE_DIR = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/.temp_tmp";
			new File(CACHE_DIR).mkdirs();

			File cacheDir = StorageUtils.getOwnCacheDirectory(getBaseContext(),
					CACHE_DIR);

			DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
					.cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
					.bitmapConfig(Bitmap.Config.RGB_565).build();
			ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
					getBaseContext())
					.defaultDisplayImageOptions(defaultOptions)
					.discCache(new UnlimitedDiscCache(cacheDir))
					.memoryCache(new WeakMemoryCache());

			ImageLoaderConfiguration config = builder.build();
			imageLoader = ImageLoader.getInstance();
			imageLoader.init(config);

		} catch (Exception e) {

		}
	}
	
	
	@Override
	public boolean onKeyDown(int keycode, KeyEvent e) {
	    switch(keycode) {
	        case KeyEvent.KEYCODE_MENU:
	            return true;
	        case KeyEvent.KEYCODE_BACK:
	        	Utilities.showExitDialog(ImagePickerActivity.this,false);
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
