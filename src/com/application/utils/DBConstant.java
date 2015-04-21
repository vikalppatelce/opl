package com.application.utils;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class DBConstant {

	public static final String DB_NAME                          = "ApplicationDB";
	public static final String TABLE_BRAND 			    		= "brand";

	
//	public static final Uri DISTINCT_CONTENT_URI = Uri.parse("content://"+ ZnameDB.AUTHORITY + "/contacts");
	
	public static class Brand_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ ApplicationDB.AUTHORITY + "/brand");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/brand";
		
		public static final String COLUMN_ID 							= "_id";
		public static final String COLUMN_BRAND_ID 					    = "_brand_id";
		public static final String COLUMN_BRAND_NAME					= "_brand_name";
		public static final String COLUMN_LOW_LINK				        = "_low_link";
		public static final String COLUMN_MED_LINK						= "_med_link";
		public static final String COLUMN_DARK_LINK			            = "_dark_link";
		public static final String COLUMN_LOW_PATH					    = "_low_path";
		public static final String COLUMN_MED_PATH			            = "_med_path";
		public static final String COLUMN_DARK_PATH						= "_dark_path";
	}
}