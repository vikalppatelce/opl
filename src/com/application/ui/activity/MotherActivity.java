/**
 * 
 */
package com.application.ui.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.application.beans.CustomGallery;
import com.application.beans.WaterMark;
import com.application.utils.Action;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.BuildVars;
import com.application.utils.Utilities;
import com.rint.topl.R;
import com.flurry.android.FlurryAgent;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
public class MotherActivity extends ActionBarActivity{
	private static final String TAG = MotherActivity.class.getSimpleName();
	private int CAPTURE_PICTURE = 102;
	private int REQUEST_IMAGE_CAPTURE = 103;
	
	ArrayList<String> imagePaths;
	
	private TextView mActionBarTitleTv;
	
	private Button mFromGalleryBtn;
	private Button mFromCameraBtn;
	
	private String mCurrentPhotoPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setWindowFullScreen();
		setContentView(R.layout.activity_mother);
		initCustomActionBar();
		initUi();
		setUiListener();
		checkIfWaterMarkAlreadyCrossLimits();
	}
	
	private void setWindowFullScreen() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	private void initCustomActionBar(){
		mActionBarTitleTv = (TextView)findViewById(R.id.actionBarTitle);
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
		mFromGalleryBtn = (Button)findViewById(R.id.activityMotherFromGallery);
		mFromCameraBtn = (Button)findViewById(R.id.activityMotherFromCamera);
	}
	
	private void setUiListener(){
		mFromGalleryBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				importPicture();
			}
		});
		
		mFromCameraBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				captureCapture();
			}
		});
	}
	
	private void importPicture(){
		Intent i = new Intent(MotherActivity.this, CustomGalleryActivity.class);
		i.putExtra("action", Action.ACTION_MULTIPLE_PICK);
		startActivityForResult(i, 200);
	}
	
	private void captureCapture(){
		   Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
		        // Create the File where the photo should go
		        File photoFile = null;
		        try {
		            photoFile = createImageFile();
		        } catch (IOException ex) {
		            // Error occurred while creating the File
		        }
		        // Continue only if the File was successfully created
		        if (photoFile != null) {
		            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
		                    Uri.fromFile(photoFile));
		            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		        }
		    }
	}
	
	private void checkIfWaterMarkAlreadyCrossLimits(){
		ArrayList<WaterMark> mListWaterMark = Utilities.retrieveArrayListFromPreferencesUsingGSON();
	}
	
	@Override
	public boolean onKeyDown(int keycode, KeyEvent e) {
	    switch(keycode) {
	        case KeyEvent.KEYCODE_MENU:
	            return true;
	        case KeyEvent.KEYCODE_BACK:
	        	Utilities.showExitDialog(MotherActivity.this,false);
	        	return true;
	    }
	    return super.onKeyDown(keycode, e);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
        imagePaths = new ArrayList<String>();
		if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
			ApplicationLoader.getPreferences().setTempCount(0);
			String[] all_path = data.getStringArrayExtra("all_path");

			ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();

			for (String string : all_path) {
				CustomGallery item = new CustomGallery();
				item.sdcardPath = string;
                imagePaths.add(string);
				dataT.add(item);
			}
			
			if (all_path.length > 0) {
				Intent redirectToImagePickerIntent = new Intent(MotherActivity.this, ImagePickerActivity.class);
				redirectToImagePickerIntent.putExtra("all_path", all_path);
				startActivity(redirectToImagePickerIntent);
				finish();
			}
			
			if(Utilities.retrieveArrayListFromPreferencesUsingGSON() != null && Utilities.retrieveArrayListFromPreferencesUsingGSON().size() > 9 ){
				Intent redirectToImagePickerIntent = new Intent(MotherActivity.this, ImagePickerActivity.class);
				redirectToImagePickerIntent.putExtra("all_path", "");
				startActivity(redirectToImagePickerIntent);
				finish();
			}
//			adapter.addAll(dataT);
		}
		
		if (requestCode == 200 && resultCode == Activity.RESULT_CANCELED) {
			if(data.getExtras().getBoolean("isCancelled")){
				finish();
			}
		}
		
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			ApplicationLoader.getPreferences().setTempCount(0);
			/*if (data != null) {
				String[] projection = { MediaStore.Images.Media.DATA };
				Cursor cursor = managedQuery(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						projection, null, null, null);
				int column_index_data = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToLast();
				String imagePath = cursor.getString(column_index_data);
				
				String[] all_path = new String[1];
				all_path[0] = imagePath;
				if(!TextUtils.isEmpty(all_path[0])){
					Intent redirectToImagePickerIntent = new Intent(MotherActivity.this, ImagePickerActivity.class);
					redirectToImagePickerIntent.putExtra("all_path", all_path);
					startActivity(redirectToImagePickerIntent);
					finish();
				}
			}*/
			
			if(!TextUtils.isEmpty(mCurrentPhotoPath)){
				try{
					String[] all_path = new String[1];
					all_path[0] = mCurrentPhotoPath;
					if(!TextUtils.isEmpty(all_path[0])){
						Intent redirectToImagePickerIntent = new Intent(MotherActivity.this, ImagePickerActivity.class);
						redirectToImagePickerIntent.putExtra("all_path", all_path);
						startActivity(redirectToImagePickerIntent);
						finish();
					}
				}catch(Exception e){
					
				}
			}
		}
	}
	
	private void sendBroadCastMediaAdded(Uri mUri){
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    File f = new File(mUri.getPath());
	    Uri contentUri = Uri.fromFile(f);
	    mediaScanIntent.setData(contentUri);
	    sendBroadcast(mediaScanIntent);
	}

	private File createImageFile() throws IOException {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "JPEG_" + timeStamp + "_";
	    File storageDir = new File(AppConstants.IMAGE_CAPTURE_DIRECTORY_PATH);
	    storageDir.mkdirs();
	    File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".jpg",         /* suffix */
	        storageDir      /* directory */
	    );

	    // Save a file: path for use with ACTION_VIEW intents
	    mCurrentPhotoPath = image.getAbsolutePath();
	    return image;
	}
	
	public String getRealPathFromURI(Uri contentUri) {
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = managedQuery(contentUri, proj, null, null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} catch (Exception e) {
			return contentUri.getPath();
		}
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
