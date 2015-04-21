/**
 * 
 */
package com.application.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class WaterMark implements Parcelable {
	private String mImagePath;
	private String mImageCaptions;
	private String mWaterMarkImage;

	public WaterMark(String mImagePath, String mImageCaptions,
			String mWaterMarkImage) {
		super();
		this.mImagePath = mImagePath;
		this.mImageCaptions = mImageCaptions;
		this.mWaterMarkImage = mWaterMarkImage;
	}

	public WaterMark() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getmImagePath() {
		return mImagePath;
	}

	public void setmImagePath(String mImagePath) {
		this.mImagePath = mImagePath;
	}

	public String getmImageCaptions() {
		return mImageCaptions;
	}

	public void setmImageCaptions(String mImageCaptions) {
		this.mImageCaptions = mImageCaptions;
	}

	public String getmWaterMarkImage() {
		return mWaterMarkImage;
	}

	public void setmWaterMarkImage(String mWaterMarkImage) {
		this.mWaterMarkImage = mWaterMarkImage;
	}

	protected WaterMark(Parcel in) {
		mImagePath = in.readString();
		mImageCaptions = in.readString();
		mWaterMarkImage = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mImagePath);
		dest.writeString(mImageCaptions);
		dest.writeString(mWaterMarkImage);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<WaterMark> CREATOR = new Parcelable.Creator<WaterMark>() {
		@Override
		public WaterMark createFromParcel(Parcel in) {
			return new WaterMark(in);
		}

		@Override
		public WaterMark[] newArray(int size) {
			return new WaterMark[size];
		}
	};
}