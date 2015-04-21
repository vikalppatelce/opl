/**
 * 
 */
package com.application.ui.view;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.application.beans.WaterMark;
import com.application.ui.activity.ImagePickerActivity;
import com.application.ui.activity.MotherActivity;
import com.application.utils.Utilities;
import com.rint.topl.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
public class HListViewAdapter extends BaseAdapter{
	private static final String TAG = HListViewAdapter.class.getSimpleName();
	private ArrayList<WaterMark> mArrayList;
	private Context mContext;
	private ImageLoader mImageLoader;
	private LayoutInflater inflater;
	
	private int mSelection = 0;
	
	public HListViewAdapter(Context mContext, ArrayList<WaterMark> mArrayList, ImageLoader mImageLoader){
		this.mContext = mContext;
		this.mArrayList = mArrayList;
		this.mImageLoader = mImageLoader;
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArrayList.size()+1;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			LayoutInflater li = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			convertView = inflater.inflate(R.layout.item_image_hlistivew, null);
			holder = new ViewHolder();
			holder.imgQueue = (ImageView) convertView
					.findViewById(R.id.itemImageView);

			holder.imgQueueAdd = (ImageButton) convertView
					.findViewById(R.id.itemImageViewAdd);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.imgQueue.setTag(position);
		
		if(position == mSelection){
			holder.imgQueue.setBackgroundColor(Color.parseColor("#FF0000"));
		}else{
			holder.imgQueue.setBackgroundColor(Color.parseColor("#000000"));
		}
		
		try {

			mImageLoader.displayImage("file://" + mArrayList.get(position).getmImagePath(),
					holder.imgQueue, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							super.onLoadingStarted(imageUri, view);
						}
					});

		} catch (Exception e) {
			e.printStackTrace();
		}
		if(position== mArrayList.size()){
			holder.imgQueueAdd.setVisibility(View.VISIBLE);
			holder.imgQueue.setVisibility(View.GONE);
			convertView.setTag(R.id.TAG_IS_ADD, true);
			holder.imgQueueAdd.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					Utilities.saveArrayListInPreferencesUsingGSON(mArrayList);
					ArrayList<WaterMark> mWaterMarkList = Utilities.retrieveArrayListFromPreferencesUsingGSON();
					if(mWaterMarkList!=null && mWaterMarkList.size() < 10){
						Intent mIntent = new Intent(mContext, MotherActivity.class);
						mIntent.putParcelableArrayListExtra("mArrayList<WaterMark>", mArrayList);
						mIntent.putExtra("mFromGallery", false);
						mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						mContext.startActivity(mIntent);
					}else{
						Crouton.cancelAllCroutons();
						Crouton.makeText((ImagePickerActivity)mContext, "You can select & process maximum 10 images together!", Style.ALERT).show();
					}
				}
			});
		}else{
			holder.imgQueueAdd.setVisibility(View.GONE);
			holder.imgQueue.setVisibility(View.VISIBLE);
			convertView.setTag(R.id.TAG_IS_ADD, false);
			convertView.setTag(R.id.TAG_PATH, mArrayList.get(position).getmImagePath());
			convertView.setTag(R.id.TAG_CAPTION, mArrayList.get(position).getmImageCaptions());
		}
		convertView.setTag(R.id.TAG_POSITION, position);
		
		return convertView;
	}
	
	
	private void addWaterMark(WaterMark Obj){
		mArrayList.add(Obj);
		notifyDataSetChanged();
	}
	
	private void addWaterMarkAll(ArrayList<WaterMark> mListWaterMark){
		mArrayList.addAll(mListWaterMark);
		notifyDataSetChanged();
	}
	
	public class ViewHolder {
		ImageView imgQueue;
		ImageButton imgQueueAdd;
	}

	public void clearCache() {
		mImageLoader.clearDiscCache();
		mImageLoader.clearMemoryCache();
	}
	
	public void setSelectionImageQueue(int position){
		mSelection = position;
	}
}
