/**
 * 
 */
package com.application.ui.activity;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.application.beans.Brand;
import com.application.beans.Logo;
import com.application.beans.WaterMark;
import com.application.ui.view.Crouton;
import com.application.ui.view.Style;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.BuildVars;
import com.application.utils.DBConstant;
import com.application.utils.RequestBuilder;
import com.application.utils.RestClient;
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
public class SelectLogoActivity extends ActionBarActivity {
	private static final String TAG = SelectLogoActivity.class.getSimpleName();
	
	private TextView mActionBarTitleTv;
	private ImageView mActionBarForward;
	
	private TextView mImagesSelectedTv;
	
	private GridView mGridView;
	
	private ProgressBar  mProgressBar;
	
	private Button mCancelBtn;
	private Button mAddmoreBtn;
	private Button mProcessBtn;
	
	private ImageLoader imageLoader;
	private ArrayList<Brand> mArrayListBrand;
	private ArrayList<Logo> mArrayListLogo;
	
	private LogoAdapter mAdapter;
	
	private Intent mIntent;
	private ArrayList<WaterMark> mListWaterMark;
	
	private String mImageToWaterMarkWith;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setWindowFullScreen();
		setContentView(R.layout.activity_select_logo);
		initCustomActionBar();
		initUi();
		initImageLoader();
		getIntentData();
		getDataFromApi();
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
		mGridView = (GridView)findViewById(R.id.gridLogo);
		
		mImagesSelectedTv = (TextView)findViewById(R.id.noImageSelected);
		
		mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
		
		mCancelBtn = (Button)findViewById(R.id.btnSelectLogoCancel);
		mProcessBtn = (Button)findViewById(R.id.btnSelectLogoProcess);
		mAddmoreBtn = (Button)findViewById(R.id.btnSelectLogoAddMore);
	}
	
	private void getIntentData(){
		mIntent= getIntent();
//		mListWaterMark = mIntent.getParcelableArrayListExtra("mArrayList<WaterMark>");
		mListWaterMark = Utilities.retrieveArrayListFromPreferencesUsingGSON();
		
		if(mListWaterMark!=null && mListWaterMark.size() > 0){
			mImagesSelectedTv.setText(String.valueOf(mListWaterMark.size())+ " images selected for process");	
		}
	}
	
	private void setUiListener(){
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> viewGroup, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				mAdapter.setSelected(position);
				mImageToWaterMarkWith = mArrayListLogo.get(position).getLogoPath();
			}
		});
		
		mCancelBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Utilities.saveArrayListInPreferencesUsingGSON(null);
				ApplicationLoader.getPreferences().setTempCount(0);
				Intent mIntent = new Intent(SelectLogoActivity.this,MotherActivity.class);
				mIntent.putParcelableArrayListExtra("mArrayList<WaterMark>", mListWaterMark);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(mIntent);
				finish();
			}
		});
		
		mProcessBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!TextUtils.isEmpty(mImageToWaterMarkWith)){
					if(mListWaterMark!=null && mListWaterMark.size() > 0){
						Intent mIntent = new Intent(SelectLogoActivity.this, ProcessWaterMarkActivity.class);
						mIntent.putParcelableArrayListExtra("mArrayList<WaterMark>", mListWaterMark);
						mIntent.putExtra("imageToWaterMarkWith", mImageToWaterMarkWith);
						mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(mIntent);
						finish();	
					}else{
						Crouton.cancelAllCroutons();
						Crouton.makeText(SelectLogoActivity.this, "Please select picture to watermark!", Style.ALERT).show();
					}
				}else{
					Crouton.cancelAllCroutons();
					Crouton.makeText(SelectLogoActivity.this, "Please select logo!", Style.ALERT).show();
				}
			}
		});
		
		mActionBarForward.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!TextUtils.isEmpty(mImageToWaterMarkWith)){
					if(mListWaterMark!=null && mListWaterMark.size() > 0){
						Intent mIntent = new Intent(SelectLogoActivity.this, ProcessWaterMarkActivity.class);
						mIntent.putParcelableArrayListExtra("mArrayList<WaterMark>", mListWaterMark);
						mIntent.putExtra("imageToWaterMarkWith", mImageToWaterMarkWith);
						mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(mIntent);
						finish();	
					}else{
						Crouton.cancelAllCroutons();
						Crouton.makeText(SelectLogoActivity.this, "Please select picture to watermark!", Style.ALERT).show();
					}
				}else{
					Crouton.cancelAllCroutons();
					Crouton.makeText(SelectLogoActivity.this, "Please select logo!", Style.ALERT).show();
				}		
			}
		});
		
		mAddmoreBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(SelectLogoActivity.this,MotherActivity.class);
				mIntent.putParcelableArrayListExtra("mArrayList<WaterMark>", mListWaterMark);
				mIntent.putExtra("fromGallery", false);
				startActivity(mIntent);
				finish();
			}
		});
	}
	
	private void getDataFromApi(){
		if(!ApplicationLoader.getPreferences().getPullLogoFirstTime()){
			if(Utilities.isInternetConnected()){
				mProgressBar.setVisibility(View.VISIBLE);
				new AsyncLogo(RequestBuilder.getPostLogoData(), true).execute();
			}else{
				Toast.makeText(
						SelectLogoActivity.this,
						getResources().getString(
								R.string.no_internet_connection),
						Toast.LENGTH_SHORT).show();
			}
		}else{
			mProgressBar.setVisibility(View.GONE);
			mGridView.setVisibility(View.VISIBLE);
			setGridView();
			if(Utilities.isInternetConnected()){
				new AsyncLogo(RequestBuilder.getPostLogoData(), false).execute();
			}
		}
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
	
	private void parseJSONFromApi(String mResponseFromApi){
		try {
			JSONObject mJSONObject = new JSONObject(mResponseFromApi);
			if(mJSONObject.getBoolean("success")){
				ApplicationLoader.getPreferences().setUserId(mJSONObject.getString("user_id"));
				JSONArray mJSONArrayData = mJSONObject.getJSONArray("data");
				mArrayListBrand = new ArrayList<Brand>();
				for (int i = 0; i < mJSONArrayData.length(); i++) {
					JSONObject mJSONObjectData = mJSONArrayData.getJSONObject(i);
					Brand obj = new Brand();
					obj.setBrandId(mJSONObjectData.getString("brand_id"));
					obj.setBrandName(mJSONObjectData.getString("brand_name"));
					
					JSONObject mJSONObjectImages = mJSONObjectData.getJSONObject("images");
					
					obj.setBrandLowLink(mJSONObjectImages.getString("low"));
					obj.setBrandMedLink(mJSONObjectImages.getString("medium"));
					obj.setBrandDarkLink(mJSONObjectImages.getString("dark"));
					obj.setBrandLowPath(AppConstants.WATERMARK_DIRECTORY_PATH+Utilities.getFileNameFromPath(mJSONObjectImages.getString("low")));
					obj.setBrandMedPath(AppConstants.WATERMARK_DIRECTORY_PATH+Utilities.getFileNameFromPath(mJSONObjectImages.getString("medium")));
					obj.setBrandDarkPath(AppConstants.WATERMARK_DIRECTORY_PATH+Utilities.getFileNameFromPath(mJSONObjectImages.getString("dark")));
					mArrayListBrand.add(obj);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void addObjectsToDB(){
		if(mArrayListBrand!=null && mArrayListBrand.size() > 0){
			for(int i = 0 ; i < mArrayListBrand.size() ; i++){
				ContentValues values = new ContentValues();
				values.put(DBConstant.Brand_Columns.COLUMN_BRAND_ID, mArrayListBrand.get(i).getBrandId());
				values.put(DBConstant.Brand_Columns.COLUMN_BRAND_NAME, mArrayListBrand.get(i).getBrandName());
				values.put(DBConstant.Brand_Columns.COLUMN_LOW_LINK, mArrayListBrand.get(i).getBrandLowLink());
				values.put(DBConstant.Brand_Columns.COLUMN_MED_LINK, mArrayListBrand.get(i).getBrandMedLink());
				values.put(DBConstant.Brand_Columns.COLUMN_DARK_LINK, mArrayListBrand.get(i).getBrandDarkLink());
				values.put(DBConstant.Brand_Columns.COLUMN_LOW_PATH, mArrayListBrand.get(i).getBrandLowPath());
				values.put(DBConstant.Brand_Columns.COLUMN_MED_PATH, mArrayListBrand.get(i).getBrandMedPath());
				values.put(DBConstant.Brand_Columns.COLUMN_DARK_PATH, mArrayListBrand.get(i).getBrandDarkPath());
				if(!ApplicationLoader.getPreferences().getPullLogoFirstTime()){ //first time insert
					getContentResolver().insert(DBConstant.Brand_Columns.CONTENT_URI, values);
				}else{ //update
					getContentResolver().update(DBConstant.Brand_Columns.CONTENT_URI, values, DBConstant.Brand_Columns.COLUMN_BRAND_ID + "=?", new String[]{mArrayListBrand.get(i).getBrandId()});
				}
			}
		}
		ApplicationLoader.getPreferences().setPullLogoFirstTime(true);
		setGridView();
	}
	
	private void setGridView(){
		addObjectsInList();
		if(mArrayListLogo!=null && mArrayListLogo.size() > 0){
			mAdapter = new LogoAdapter(SelectLogoActivity.this, imageLoader, mArrayListLogo);
			mGridView.setAdapter(mAdapter);
		}
	}
	
	private void addObjectsInList(){
		Cursor mCursor = getContentResolver().query(DBConstant.Brand_Columns.CONTENT_URI, null, null, null, null);
		int mColumnLinkLow = mCursor.getColumnIndex(DBConstant.Brand_Columns.COLUMN_LOW_LINK);
		int mColumnLinkMed = mCursor.getColumnIndex(DBConstant.Brand_Columns.COLUMN_MED_LINK);
		int mColumnLinkDark = mCursor.getColumnIndex(DBConstant.Brand_Columns.COLUMN_DARK_LINK);
		
		int mColumnPathLow = mCursor.getColumnIndex(DBConstant.Brand_Columns.COLUMN_LOW_PATH);
		int mColumnPathMed = mCursor.getColumnIndex(DBConstant.Brand_Columns.COLUMN_MED_PATH);
		int mColumnPathDark = mCursor.getColumnIndex(DBConstant.Brand_Columns.COLUMN_DARK_PATH);
		
		int mColumnBrandName = mCursor.getColumnIndex(DBConstant.Brand_Columns.COLUMN_BRAND_NAME);
		
		int mColumnBrandId = mCursor.getColumnIndex(DBConstant.Brand_Columns.COLUMN_BRAND_ID);
		if(mCursor!=null && mCursor.getCount() > 0){
			mArrayListLogo = new ArrayList<Logo>();
			mCursor.moveToFirst();
			do {
				for (int i = 0; i < 3; i++) {
					Logo obj = new Logo();
					obj.setSelected(false);
					switch(i){
					case 0:
						obj.setLogoName("Low");
						obj.setLogoPath(mCursor.getString(mColumnPathLow));
						obj.setLogoLink(mCursor.getString(mColumnLinkLow));
						break;
					case 1:
						obj.setLogoName("Med");
						obj.setLogoPath(mCursor.getString(mColumnPathMed));
						obj.setLogoLink(mCursor.getString(mColumnLinkMed));
						break;
					case 2: 
						obj.setLogoName("Dark");
						obj.setLogoPath(mCursor.getString(mColumnPathDark));
						obj.setLogoLink(mCursor.getString(mColumnLinkDark));
						break;
					}
					mArrayListLogo.add(obj);
				}
			} while (mCursor.moveToNext());
		}
	}
	
	public class AsyncLogo extends AsyncTask<Void, Void, Void>{
		private JSONObject mJSONObject;
		private String mResponseFromApi;
		private boolean isSuccess = false;
		private String mErrorMessage;
		private ProgressDialog mProgressDialog;
		private boolean isFirstTimePull;
		
		public AsyncLogo(JSONObject mJSONObject, boolean isFirstTimePull){
			this.mJSONObject = mJSONObject;
			this.isFirstTimePull = isFirstTimePull;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if(isFirstTimePull){
				mProgressDialog = new ProgressDialog(SelectLogoActivity.this);
				mProgressDialog.setMessage("Please wait");
//				mProgressDialog.show();	
			}
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			try {
				mResponseFromApi = RestClient.postJSON(AppConstants.API.URL_POST_lOGO, mJSONObject);
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
			
			if(isFirstTimePull){
				mProgressBar.setVisibility(View.GONE);
			}
			
			if(isSuccess){
				parseJSONFromApi(mResponseFromApi);
				addObjectsToDB();
				mGridView.setVisibility(View.VISIBLE);
			}else{
				if(!TextUtils.isEmpty(mErrorMessage)){
					Crouton.makeText(SelectLogoActivity.this, mErrorMessage, Style.ALERT).show();
				}
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keycode, KeyEvent e) {
	    switch(keycode) {
	        case KeyEvent.KEYCODE_MENU:
	            return true;
	        case KeyEvent.KEYCODE_BACK:
	        	Utilities.showExitDialog(SelectLogoActivity.this,false);
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
