package com.application.ui.activity;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.beans.Logo;
import com.application.utils.Utilities;
import com.rint.topl.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class LogoAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater infalter;
	private ArrayList<Logo> data;
	ImageLoader imageLoader;

	private boolean isActionMultiplePick;

	public LogoAdapter(Context c, ImageLoader imageLoader, ArrayList<Logo> data) {
		infalter = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContext = c;
		this.imageLoader = imageLoader;
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Logo getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void setSelected(int position){
		for(int i = 0 ;i < data.size() ;i++){
			data.get(i).setSelected(false);
		}
		data.get(position).setSelected(true);
		notifyDataSetChanged();
	}

	public void setMultiplePick(boolean isMultiplePick) {
		this.isActionMultiplePick = isMultiplePick;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
		if (convertView == null) {

			convertView = infalter.inflate(R.layout.item_logo, null);
			holder = new ViewHolder();
			holder.imgQueue = (ImageView) convertView
					.findViewById(R.id.imgQueue);

			holder.imgQueueMultiSelected = (ImageView) convertView
					.findViewById(R.id.imgQueueMultiSelected);
			holder.imgQueueQuality = (TextView)convertView.findViewById(R.id.imgName);

			if (isActionMultiplePick) {
				holder.imgQueueMultiSelected.setVisibility(View.VISIBLE);
			} else {
				holder.imgQueueMultiSelected.setVisibility(View.GONE);
			}

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.imgQueue.setTag(position);
		holder.imgQueueQuality.setText(data.get(position).getLogoName());

		try {

			if(new File(data.get(position).getLogoPath()).exists()){
				holder.imgQueue.setImageBitmap(BitmapFactory.decodeFile(data.get(position).getLogoPath()));
//				imageLoader.displayImage("file://" + data.get(position).getLogoPath(),holder.imgQueue);
			}else{
				imageLoader.displayImage(data.get(position).getLogoLink(),
						holder.imgQueue, new SimpleImageLoadingListener() {
							@Override
							public void onLoadingStarted(String imageUri, View view) {
								holder.imgQueue
										.setImageResource(R.drawable.no_media);
								super.onLoadingStarted(imageUri, view);
							}
							
							@Override
							public void onLoadingComplete(String path, View view, Bitmap mBitmap) {
								// TODO Auto-generated method stub
								String mImagePath = data.get(position).getLogoPath().substring(data.get(position).getLogoPath().lastIndexOf("/"), data.get(position).getLogoPath().length());
								Utilities.writeBitmapToSDCard(mBitmap, mImagePath);
							}
						});				
			}
			
			if(data.get(position).isSelected()){
				holder.imgQueueMultiSelected.setVisibility(View.VISIBLE);
			}else{
				holder.imgQueueMultiSelected.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return convertView;
	}

	public class ViewHolder {
		ImageView imgQueue;
		ImageView imgQueueMultiSelected;
		TextView  imgQueueQuality;
	}

	public void clearCache() {
		imageLoader.clearDiscCache();
		imageLoader.clearMemoryCache();
	}

	public void clear() {
		data.clear();
		notifyDataSetChanged();
	}
}
