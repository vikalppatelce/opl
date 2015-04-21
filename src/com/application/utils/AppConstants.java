/* HISTORY
 * CATEGORY 		:- ACTIVITY
 * DEVELOPER		:- VIKALP PATEL
 * AIM			    :- ADD IPD ACTIVITY
 * DESCRIPTION 		:- SAVE IPD
 * 
 * S - START E- END  C- COMMENTED  U -EDITED A -ADDED
 * --------------------------------------------------------------------------------------------------------------------
 * INDEX       DEVELOPER		DATE			FUNCTION		DESCRIPTION
 * --------------------------------------------------------------------------------------------------------------------
 * 1000B      VIKALP PATEL    07/02/2014        RELEASE         ADD VIDEO EXTENSION
 * 1000E      VIKALP PATEL    15/02/2014        RELEASE         ADDED PASS HASH IN JSON
 * --------------------------------------------------------------------------------------------------------------------
 */
package com.application.utils;

import android.os.Environment;

public class AppConstants {
	public interface API {
		// API V1
		public static final String URL_POST_LOGIN = "http://digiteriasoftsol.com/topl/services/loginvalidate.php";
		public static final String URL_POST_lOGO  = "http://digiteriasoftsol.com/topl/services/brand_new.php";
	}
	public interface PUSH{
	}

	public interface HEADERS {
		public static final String USER = "netdoersadmin";
		public static final String PASSWORD = "538f25fc32727";
	}

	public interface TAGS {
		public static final String GROUP_ID = "group_id";
		public static final String GROUP_NAME = "group_name";
		public static final String POST_ID = "post_id";
		public static final String FEED_POSITION = "feed_position";
		public static final String GROUP_IMAGE = "group_image";
		public static final String GROUP_SUBSCRIBE = "is_subscribe";
		public static final String GROUP_ADMIN = "is_admin";
		public static final String WHATSAPP_ID = "whatsapp_id";
	}

	public interface INTENT {
		public static final int FEED_COMMENT = 10001;
		public static final String COMMENT_DATA = "refresh_post";
	}

	public interface BROADCAST_ACTION {
		public static final String POST_SERVICE_MEDIA = "post_service_media";
	}

	public static final String NETWORK_NOT_AVAILABLE = "Network not available";
	public static final String IMAGE_DIRECTORY_PATH = Environment
			.getExternalStorageDirectory().getPath() + "/TOPL/TOPL_WaterMarkImages/";
	
	public static final String IMAGE_CAPTURE_DIRECTORY_PATH = Environment
			.getExternalStorageDirectory().getPath() + "/TOPL/CaptureImages/";
	
	public static final String IMAGE_DIRECTORY_PATH_DATA = ApplicationLoader
			.getApplication().getApplicationContext().getFilesDir()
			.getAbsolutePath();
	
	public static final String LOG_DIRECTORY_PATH = Environment
			.getExternalStorageDirectory().getPath() + "/TOPL/Logs/";
	
	public static final String WATERMARK_DIRECTORY_PATH = Environment
			.getExternalStorageDirectory().getPath() + "/TOPL/.WaterMark/";
	
	public static final String EXTENSION = ".jpg";
	public static final String VIDEO_EXTENSION = ".mp4";
	public static final boolean DEBUG = false;
	public static final String WHATSAPP_DOMAIN = "@s.whatsapp.net";

	public static final String GREEN = "#006400";
	public static final String BLUE = "#01AFD2";
	
	public static final int NOTIFICATION_ID = 434;

}
