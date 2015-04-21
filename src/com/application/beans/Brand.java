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
public class Brand implements Parcelable {
	private String brandId;
	private String brandName;
	private String brandLowLink;
	private String brandMedLink;
	private String brandDarkLink;
	private String brandLowPath;
	private String brandMedPath;
	private String brandDarkPath;

	public Brand() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Brand(String brandId, String brandName, String brandLowLink,
			String brandMedLink, String brandDarkLink, String brandLowPath,
			String brandMedPath, String brandDarkPath) {
		super();
		this.brandId = brandId;
		this.brandName = brandName;
		this.brandLowLink = brandLowLink;
		this.brandMedLink = brandMedLink;
		this.brandDarkLink = brandDarkLink;
		this.brandLowPath = brandLowPath;
		this.brandMedPath = brandMedPath;
		this.brandDarkPath = brandDarkPath;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBrandLowLink() {
		return brandLowLink;
	}

	public void setBrandLowLink(String brandLowLink) {
		this.brandLowLink = brandLowLink;
	}

	public String getBrandMedLink() {
		return brandMedLink;
	}

	public void setBrandMedLink(String brandMedLink) {
		this.brandMedLink = brandMedLink;
	}

	public String getBrandDarkLink() {
		return brandDarkLink;
	}

	public void setBrandDarkLink(String brandDarkLink) {
		this.brandDarkLink = brandDarkLink;
	}

	public String getBrandLowPath() {
		return brandLowPath;
	}

	public void setBrandLowPath(String brandLowPath) {
		this.brandLowPath = brandLowPath;
	}

	public String getBrandMedPath() {
		return brandMedPath;
	}

	public void setBrandMedPath(String brandMedPath) {
		this.brandMedPath = brandMedPath;
	}

	public String getBrandDarkPath() {
		return brandDarkPath;
	}

	public void setBrandDarkPath(String brandDarkPath) {
		this.brandDarkPath = brandDarkPath;
	}

	protected Brand(Parcel in) {
		brandId = in.readString();
		brandName = in.readString();
		brandLowLink = in.readString();
		brandMedLink = in.readString();
		brandDarkLink = in.readString();
		brandLowPath = in.readString();
		brandMedPath = in.readString();
		brandDarkPath = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(brandId);
		dest.writeString(brandName);
		dest.writeString(brandLowLink);
		dest.writeString(brandMedLink);
		dest.writeString(brandDarkLink);
		dest.writeString(brandLowPath);
		dest.writeString(brandMedPath);
		dest.writeString(brandDarkPath);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<Brand> CREATOR = new Parcelable.Creator<Brand>() {
		@Override
		public Brand createFromParcel(Parcel in) {
			return new Brand(in);
		}

		@Override
		public Brand[] newArray(int size) {
			return new Brand[size];
		}
	};
}
