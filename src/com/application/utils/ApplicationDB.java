package com.application.utils;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.rint.topl.BuildConfig;

public class ApplicationDB extends ContentProvider{
	
	public static final String AUTHORITY = "com.digitattva.topl.ApplicationDB";
	
	private static final UriMatcher sUriMatcher;
	private static final String TAG = ApplicationDB.class.getSimpleName();
	
	private static final int BRAND = 1;
	
	private static HashMap<String, String> brandProjectionMap;
	
	private static class OpenHelper extends SQLiteOpenHelper {

		OpenHelper(Context context) {
			super(context, DBConstant.DB_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			
			//allcontacts
			StringBuilder strBuilder = new StringBuilder();
			strBuilder.append("CREATE TABLE ");
			strBuilder.append(DBConstant.TABLE_BRAND);
			strBuilder.append('(');
			strBuilder.append(DBConstant.Brand_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," );
			strBuilder.append(DBConstant.Brand_Columns.COLUMN_BRAND_ID +" TEXT UNIQUE," );
			strBuilder.append(DBConstant.Brand_Columns.COLUMN_BRAND_NAME +" TEXT ," );
			strBuilder.append(DBConstant.Brand_Columns.COLUMN_LOW_LINK +" TEXT ," );
			strBuilder.append(DBConstant.Brand_Columns.COLUMN_MED_LINK +" TEXT ," );
			strBuilder.append(DBConstant.Brand_Columns.COLUMN_DARK_LINK +" TEXT ," );
			strBuilder.append(DBConstant.Brand_Columns.COLUMN_LOW_PATH +" TEXT ," );
			strBuilder.append(DBConstant.Brand_Columns.COLUMN_MED_PATH +" TEXT ," );
			strBuilder.append(DBConstant.Brand_Columns.COLUMN_DARK_PATH +" TEXT" );
			strBuilder.append(')');
			db.execSQL(strBuilder.toString());
			if (BuildConfig.DEBUG) {
				Log.i(TAG, strBuilder.toString());
			}
		}

		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_BRAND);
			onCreate(db);
		}
	}

	/* VERSION      DATABASE_VERSION      MODIFIED            BY
	 * ----------------------------------------------------------------
	 * V 0.0.1             1              24/03/15        VIKALP PATEL
	 * -----------------------------------------------------------------
	 */
	private static final int DATABASE_VERSION = 2;
		
	OpenHelper openHelper;


	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = openHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case BRAND:
			count = db.delete(DBConstant.TABLE_BRAND, where, whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}


	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch (sUriMatcher.match(uri)) {
		case BRAND:
			return DBConstant.Brand_Columns.CONTENT_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}


	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// TODO Auto-generated method stub
		if (sUriMatcher.match(uri) != BRAND) 
		{ 
			throw new IllegalArgumentException("Unknown URI " + uri); 
		}
		
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} 
		else {
			values = new ContentValues();
		}
		
		SQLiteDatabase db = openHelper.getWritableDatabase();
		long rowId = 0;
		
		switch (sUriMatcher.match(uri)) 
		{
			case BRAND:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_BRAND, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Brand_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
				
		}
		throw new SQLException("Failed to insert row into " + uri);
	}


	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		openHelper 		= new OpenHelper(getContext());
		return true;
	}


	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (sUriMatcher.match(uri)) {
		case BRAND:
			qb.setTables(DBConstant.TABLE_BRAND);
			qb.setProjectionMap(brandProjectionMap);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
//		SQLiteDatabase db = openHelper.getReadableDatabase();
		SQLiteDatabase db = openHelper.getWritableDatabase();
//		Cursor c;
//		if(sUriMatcher.match(uri) == GROUPS_D){
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);	
//		}else{
//			String distinctQuery = SQLiteQueryBuilder.buildQueryString(true, DBConstant.TABLE_GROUPS, new String[]{DBConstant.Group_Columns.COLUMN_GROUP_ID_MYSQl}, null, null, null, null, null);
//			c = db.rawQuery(distinctQuery, null);
//		}
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}
	
	
	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = openHelper.getWritableDatabase();
		int count = -1;
		switch (sUriMatcher.match(uri)) {
		case BRAND:
			count = db.update(DBConstant.TABLE_BRAND, values, where, whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
	
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_BRAND, BRAND);

		brandProjectionMap = new HashMap<String, String>();
		brandProjectionMap.put(DBConstant.Brand_Columns.COLUMN_ID, DBConstant.Brand_Columns.COLUMN_ID);
		brandProjectionMap.put(DBConstant.Brand_Columns.COLUMN_BRAND_ID, DBConstant.Brand_Columns.COLUMN_BRAND_ID);
		brandProjectionMap.put(DBConstant.Brand_Columns.COLUMN_BRAND_NAME, DBConstant.Brand_Columns.COLUMN_BRAND_NAME);
		brandProjectionMap.put(DBConstant.Brand_Columns.COLUMN_LOW_LINK, DBConstant.Brand_Columns.COLUMN_LOW_LINK);
		brandProjectionMap.put(DBConstant.Brand_Columns.COLUMN_MED_LINK, DBConstant.Brand_Columns.COLUMN_MED_LINK);
		brandProjectionMap.put(DBConstant.Brand_Columns.COLUMN_DARK_LINK, DBConstant.Brand_Columns.COLUMN_DARK_LINK);
		brandProjectionMap.put(DBConstant.Brand_Columns.COLUMN_LOW_PATH, DBConstant.Brand_Columns.COLUMN_LOW_PATH);
		brandProjectionMap.put(DBConstant.Brand_Columns.COLUMN_MED_PATH, DBConstant.Brand_Columns.COLUMN_MED_PATH);
		brandProjectionMap.put(DBConstant.Brand_Columns.COLUMN_DARK_PATH, DBConstant.Brand_Columns.COLUMN_DARK_PATH);

		}
}
