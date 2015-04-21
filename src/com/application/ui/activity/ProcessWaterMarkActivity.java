/**
 * 
 */
package com.application.ui.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.application.beans.WaterMark;
import com.application.utils.AppConstants;
import com.application.utils.BuildVars;
import com.application.utils.Utilities;
import com.rint.topl.R;
import com.flurry.android.FlurryAgent;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
public class ProcessWaterMarkActivity extends ActionBarActivity{
	private static final String TAG = ProcessWaterMarkActivity.class.getSimpleName();
	
	ArrayList<String> imagePaths;
	
	private TextView mActionBarTitleTv;
	private ImageView mActionBarForward;
	
	private TextView mImagesProcessedTv;
	
	private FrameLayout mFrameLayout;
	private TextView mFrameTextView;
	
	private LinearLayout mLinearGridView; 
	private LinearLayout mLinearGridView1;
	private LinearLayout mLinearGridView2;
	private LinearLayout mLinearGridView3;
	
	private ImageView mFrameImage;
	
	private ImageView mFrameImageLogo[];
	
	private Button mRestartBtn;
	
	private ProgressBar mProgressBar;
	
	private String mLogoPath;
	private ArrayList<WaterMark> mArrayListWaterMark;
	
	private Intent mIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setWindowFullScreen();
		setContentView(R.layout.activity_process_watermark);
		initCustomActionBar();
		initUi();
		getIntentData();
		displayProgress();
		processWaterMarkWithCanvas();
		showProgress();
		setUiListener();
		
	}
	
	private void setWindowFullScreen() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	private void initCustomActionBar(){
		mActionBarTitleTv = (TextView)findViewById(R.id.actionBarTitle);
		mActionBarForward= (ImageView)findViewById(R.id.actionBarForward);
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
		mImagesProcessedTv  = (TextView)findViewById(R.id.activityProcessTv);
		
		mFrameLayout = (FrameLayout)findViewById(R.id.processImageLayout);
		mFrameTextView = (TextView)findViewById(R.id.processImageWaterMark);
		
		mLinearGridView = (LinearLayout)findViewById(R.id.processImageGrid);
		
		mFrameImage = (ImageView)findViewById(R.id.processImage);
		
		mProgressBar = (ProgressBar)findViewById(R.id.activityProcessProgress);
		
		mLinearGridView1 = (LinearLayout)findViewById(R.id.processImageGrid1);
		mLinearGridView2 = (LinearLayout)findViewById(R.id.processImageGrid2);
		mLinearGridView3 = (LinearLayout)findViewById(R.id.processImageGrid3);
		
		mFrameImageLogo = new ImageView[9];
		for (int i = 0; i < 9; i++) {
			String mTag = "processImageLogo"+i;
			mFrameImageLogo[i] = (ImageView)mLinearGridView.findViewWithTag(mTag);
		}
		
		mRestartBtn = (Button)findViewById(R.id.activityProcessRestart);
	}
	
	private void getIntentData(){
		mIntent = getIntent();
		mArrayListWaterMark = mIntent.getParcelableArrayListExtra("mArrayList<WaterMark>");
		mLogoPath = mIntent.getStringExtra("imageToWaterMarkWith");
	}
	
	
	private void processWaterMarkWithCanvas(){
		try{
			for(int i = 0 ; i < mArrayListWaterMark.size();i++){
				saveFrameLayout(addWaterMark(mArrayListWaterMark.get(i).getmImagePath(),
						mLogoPath,
						mArrayListWaterMark.get(i).getmImageCaptions()));
			}
		}catch(Exception e){
			
		}
	}
	
	private Bitmap addWaterMark(String originalImagePath, String waterMarkImagePath, String waterMarkCaptions) {
		int scaleToUse  = 12;
		Options mDisplayOptions = new Options();
		mDisplayOptions.inSampleSize = 2;
		Bitmap src = BitmapFactory.decodeFile(originalImagePath, mDisplayOptions);
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);
        
		Bitmap waterMark = scaleWaterMarkBitmap(result, waterMarkImagePath, scaleToUse);
		
		while (!(waterMark.getWidth() < src.getWidth() / 3
				&& waterMark.getHeight() < src.getHeight() / 3)) {
			scaleToUse--;
			waterMark = scaleWaterMarkBitmap(result, waterMarkImagePath, scaleToUse);
		}
		
        int ww = waterMark.getWidth();
        int hh = waterMark.getHeight();
        
        int captionHeight= h/12;
        
        canvas.drawBitmap(waterMark, 0, 0, null);
        canvas.drawBitmap(waterMark, w/2-ww/2, 0, null);
        canvas.drawBitmap(waterMark, w-ww, 0, null);

        canvas.drawBitmap(waterMark, 0, (h/2)-(hh/2)-captionHeight, null);
        canvas.drawBitmap(waterMark, w/2-ww/2, (h/2)-(hh/2)-captionHeight, null);
        canvas.drawBitmap(waterMark, w-ww, (h/2)-(hh/2)-captionHeight, null);
        
        canvas.drawBitmap(waterMark, 0, h-hh- captionHeight, null);
        canvas.drawBitmap(waterMark, w/2-ww/2, h-hh-captionHeight, null);
        canvas.drawBitmap(waterMark, w-ww, h-hh-captionHeight, null);
        
        Paint mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#BC000000"));
        canvas.drawRect(0, h-captionHeight, w, h, mPaint);
        
        String captionString = " ";
        if(!TextUtils.isEmpty(waterMarkCaptions)){
        	captionString = waterMarkCaptions;
        }
        
        Paint rPaint = new Paint();
        rPaint.setTextSize((float)captionHeight/2);
        Rect areaRect = new Rect(0, h-captionHeight, w, h);
        RectF bounds = new RectF(areaRect);
        // measure text width
        bounds.right = rPaint.measureText(captionString, 0, captionString.length());
        // measure text height
        bounds.left = w/2 - bounds.right/2;
       
        
        Paint paint = new Paint();
        paint.setTextSize((float)captionHeight/2);
        paint.setColor(Color.WHITE);
        canvas.drawText(captionString, bounds.left, h-(captionHeight/4), paint);
		cleanBitmap(src, waterMark);
        return result;
    }
	
	private void cleanBitmap(Bitmap mBitmap, Bitmap mBitmap1) {
		try {
			mBitmap.recycle();
			mBitmap1.recycle();
			mBitmap = null;
			mBitmap1 = null;
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}
	
	private Bitmap scaleWaterMarkBitmap(Bitmap mBitmapImage,
			String waterMarkImagePath, int scaleToUse) {
		Bitmap bmp = BitmapFactory.decodeFile(waterMarkImagePath);
		int sizeY = mBitmapImage.getHeight() * scaleToUse / 100;
		int sizeX = bmp.getWidth() * sizeY / bmp.getHeight();
		return Bitmap.createScaledBitmap(bmp, sizeX, sizeY, false);
	}
	
	private void processWaterMark(){
		try{
			for (int i = 0; i < mArrayListWaterMark.size(); i++) {
				for(int j = 0 ; j < 9 ;j++){
					mFrameImageLogo[j].setImageBitmap(BitmapFactory.decodeFile(mLogoPath));
				}
				
				mFrameTextView.setText(mArrayListWaterMark.get(i).getmImageCaptions());
				Bitmap mBitmapImage = BitmapFactory.decodeFile(mArrayListWaterMark.get(i).getmImagePath());
				BitmapDrawable mDrawableBitmap = new BitmapDrawable(getResources(),mBitmapImage);
				Drawable mDrawable = (Drawable)mDrawableBitmap;
				mFrameImage.setImageBitmap(mBitmapImage);
				mFrameLayout.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), 
			            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
				mFrameLayout.layout(0, 0, mFrameLayout.getMeasuredWidth(), mFrameLayout.getMeasuredHeight()); 

				FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(mBitmapImage.getWidth(), mBitmapImage.getHeight());
				mFrameLayout.setLayoutParams(lp);
			    
				android.view.ViewGroup.LayoutParams mLayoutGridViewParams = mLinearGridView.getLayoutParams();
				mLayoutGridViewParams.height = mFrameLayout.getMeasuredHeight();
				mLayoutGridViewParams.width = mFrameLayout.getMeasuredWidth();

				mFrameLayout.setDrawingCacheEnabled(true);
				
				mFrameLayout.buildDrawingCache(true);
				Bitmap mBitmap = mFrameLayout.getDrawingCache();
				saveFrameLayout(mBitmap);
				mFrameLayout.destroyDrawingCache();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	private void saveFrameLayout(Bitmap mBitmap) {
	    try {
	    	File mFile = new File(AppConstants.IMAGE_DIRECTORY_PATH+System.currentTimeMillis()+".jpg");
	    	mFile.mkdirs();
	    	if(mFile.exists()){
	    		mFile.delete();
	    	}
	        FileOutputStream fileOutputStream = new FileOutputStream(mFile.getAbsolutePath());
	        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
	        fileOutputStream.flush();
	        fileOutputStream.close();
	        sendBroadCastMediaAdded(Uri.parse(mFile.getAbsolutePath()));
	    } catch (Exception e) {
	        // TODO: handle exception
	    }
	}
	
	private void sendBroadCastMediaAdded(Uri mUri){
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    File f = new File(mUri.getPath());
	    Uri contentUri = Uri.fromFile(f);
	    mediaScanIntent.setData(contentUri);
	    sendBroadcast(mediaScanIntent);
	}
	
	private void displayProgress(){
		mProgressBar.setVisibility(View.VISIBLE);
		mImagesProcessedTv.setText(mArrayListWaterMark.size() + " Image(s) are in processing for watermark");
		mRestartBtn.setVisibility(View.GONE);
		mActionBarForward.setVisibility(View.GONE);
	}
	
	private void showProgress(){
		Thread timer = new Thread() {
			public void run() {
				try {
					sleep(5000);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							displayRestart();							
						}
					});
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
				}
			}
		};
		timer.start();
	}
	
	private void displayRestart(){
		mProgressBar.setVisibility(View.GONE);
		mImagesProcessedTv.setText(mArrayListWaterMark.size() + " Image(s) have been\n processed for watermark\n and saved in gallery.");
		mRestartBtn.setVisibility(View.VISIBLE);
		mActionBarForward.setVisibility(View.VISIBLE);
	}
	
	private void setUiListener(){
		mRestartBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Utilities.saveArrayListInPreferencesUsingGSON(null);
				Intent mIntent = new Intent(ProcessWaterMarkActivity.this,MotherActivity.class);
				mIntent.putParcelableArrayListExtra("mArrayList<WaterMark>", null);
				mIntent.putExtra("mFromGallery", false);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(mIntent);
				finish();
			}
		});
		
		mActionBarForward.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Utilities.saveArrayListInPreferencesUsingGSON(null);
				Intent mIntent = new Intent(ProcessWaterMarkActivity.this,MotherActivity.class);
				mIntent.putParcelableArrayListExtra("mArrayList<WaterMark>", null);
				mIntent.putExtra("mFromGallery", false);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(mIntent);
				finish();
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keycode, KeyEvent e) {
	    switch(keycode) {
	        case KeyEvent.KEYCODE_MENU:
	            return true;
	        case KeyEvent.KEYCODE_BACK:
	        	Utilities.showExitDialog(ProcessWaterMarkActivity.this,false);
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
