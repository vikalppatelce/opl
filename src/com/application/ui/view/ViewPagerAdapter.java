/* HISTORY
 * CATEGORY			 :- VIEW | HELPER 
 * DEVELOPER		 :- VIKALP PATEL
 * AIM      		 :- PROVIDE FRAGMENT FOR TABS
 * NOTE: HANDLE WITH CARE 
 * 
 * S - START E- END  C- COMMENTED  U -EDITED A -ADDED
 * --------------------------------------------------------------------------------------------------------------------
 * INDEX       DEVELOPER		DATE			FUNCTION		DESCRIPTION
 * --------------------------------------------------------------------------------------------------------------------
 * TU001      VIKALP PATEL     29/07/2014                       CREATED
 * --------------------------------------------------------------------------------------------------------------------
 * 
 * *****************************************METHODS INFORMATION******************************************************** 
 * ********************************************************************************************************************
 * DEVELOPER		  METHOD								DESCRIPTION
 * ********************************************************************************************************************
 * VIKALP PATEL                          			
 * ********************************************************************************************************************
 *
 */

package com.application.ui.view;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.application.beans.WaterMark;
import com.application.ui.activity.ImagePickerFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author Vikalp Patel (vikalppatelce@yahoo.com)
 * @category Ui Helper
 * 
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter{
	private static final String TAG  = ViewPagerAdapter.class.getSimpleName();
	private ArrayList<WaterMark> mListWaterMark;
	private ImageLoader imageLoader;
	public ViewPagerAdapter(FragmentManager fm, ArrayList<WaterMark> mListWaterMark, ImageLoader imageLoader) {
		super(fm);
		this.mListWaterMark = mListWaterMark;
		this.imageLoader = imageLoader;
	}

	@Override
	public Fragment getItem(int position) {
		return ImagePickerFragment.newInstance(mListWaterMark, position, imageLoader);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListWaterMark.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return "Title";
	}
}
