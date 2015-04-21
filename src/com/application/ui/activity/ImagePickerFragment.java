/* HISTORY
 * CATEGORY			 :- UI : FRAGMENTS
 * DEVELOPER	     :- VIKALP PATEL(vikalppatelce@yahoo.com)
 * AIM 				 :- LOOK BOOK PAGER FRAGMENT
 * DESCRIPTION 		 :- SUPPLY FRAGMENTS TO VIEWPAGER OF LOOK BOOK ACTIVITY
 * 
 * S - START E- END C- COMMENTED U -EDITED A -ADDED
 * --------------------------------------------------------------------------------------------------------------------
 * INDEX		 DEVELOPER 		DATE 			FUNCTION 		DESCRIPTION
 * --------------------------------------------------------------------------------------------------------------------
 * 1000A      VIKALP PATEL     20/11/14                         CREATED
 * --------------------------------------------------------------------------------------------------------------------
 */
package com.application.ui.activity;

import java.io.File;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.application.beans.WaterMark;
import com.application.utils.Utilities;
import com.rint.topl.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

public final class ImagePickerFragment extends Fragment {
	private static final String KEY_CONTENT = "LookBookFragment:Content";
	private static String TAG = ImagePickerFragment.class.getSimpleName();

	private ArrayList<WaterMark> mList;

	private ImageView mImageView;
	private EditText mEditText;
	
	private View mEditTextLine;
	
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	
	private int mPosition;
	
	public static ImagePickerFragment newInstance(
			ArrayList<WaterMark> mArrayList, int position, ImageLoader imageLoader) {
		ImagePickerFragment fragment = new ImagePickerFragment();
		fragment.mList = mArrayList;
		fragment.mPosition = position;
		fragment.imageLoader = imageLoader;
		Log.i(TAG, "size " + fragment.mList.size());
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if ((savedInstanceState != null)
				&& savedInstanceState.containsKey(KEY_CONTENT)) {
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_image_picker, container,
				false);
		mImageView = (ImageView)view.findViewById(R.id.imagePickerIv);
		mEditText = (EditText)view.findViewById(R.id.imagePickerCaptions);
		mEditTextLine = (View)view.findViewById(R.id.imagePickerView);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setDataFromList();
		setUiListener();
	}
	
	private void setDataFromList(){
		try {
			imageLoader.displayImage("file://" + mList.get(mPosition).getmImagePath(),
					mImageView, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							mImageView
									.setImageResource(R.drawable.no_media);
							super.onLoadingStarted(imageUri, view);
						}
					});
			if(!TextUtils.isEmpty(mList.get(mPosition).getmImageCaptions())){
				mEditText.setText(mList.get(mPosition).getmImageCaptions());	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setUiListener(){
		mEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable mEditTable) {
				// TODO Auto-generated method stub
				mList.get(mPosition).setmImageCaptions(mEditTable.toString());
				Utilities.saveArrayListInPreferencesUsingGSON(mList);
			}
		});
		
		mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean isFocused) {
				// TODO Auto-generated method stub
				if(isFocused){
					mEditTextLine.setBackgroundColor(Color.WHITE);
				}else{
					mEditTextLine.setBackgroundDrawable(getResources().getDrawable(R.drawable.line_gray));
				}
			}
		});
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// outState.putParcelable(KEY_CONTENT, (Parcelable) mList);
	}
	
	private void initImageLoader() {
		try {
			String CACHE_DIR = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/.temp_tmp";
			new File(CACHE_DIR).mkdirs();

			File cacheDir = StorageUtils.getOwnCacheDirectory(getActivity(),
					CACHE_DIR);

			DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
					.cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
					.bitmapConfig(Bitmap.Config.RGB_565).build();
			ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
					getActivity())
					.defaultDisplayImageOptions(defaultOptions)
					.discCache(new UnlimitedDiscCache(cacheDir))
					.memoryCache(new WeakMemoryCache());

			ImageLoaderConfiguration config = builder.build();
			imageLoader = ImageLoader.getInstance();
			imageLoader.init(config);

		} catch (Exception e) {

		}
	}
}
