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
public class Logo implements Parcelable {
	private String logoName;
	private String logoLink;
	private String logoPath;
	private boolean isSelected;

	public Logo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Logo(String logoName, String logoLink, String logoPath,
			boolean isSelected) {
		super();
		this.logoName = logoName;
		this.logoLink = logoLink;
		this.logoPath = logoPath;
		this.isSelected = isSelected;
	}

	public String getLogoName() {
		return logoName;
	}

	public void setLogoName(String logoName) {
		this.logoName = logoName;
	}

	public String getLogoLink() {
		return logoLink;
	}

	public void setLogoLink(String logoLink) {
		this.logoLink = logoLink;
	}

	public String getLogoPath() {
		return logoPath;
	}

	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	protected Logo(Parcel in) {
		logoName = in.readString();
		logoLink = in.readString();
		logoPath = in.readString();
		isSelected = in.readByte() != 0x00;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(logoName);
		dest.writeString(logoLink);
		dest.writeString(logoPath);
		dest.writeByte((byte) (isSelected ? 0x01 : 0x00));
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<Logo> CREATOR = new Parcelable.Creator<Logo>() {
		@Override
		public Logo createFromParcel(Parcel in) {
			return new Logo(in);
		}

		@Override
		public Logo[] newArray(int size) {
			return new Logo[size];
		}
	};
}
