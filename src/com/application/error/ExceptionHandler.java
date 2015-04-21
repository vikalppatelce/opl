/*
Copyright (c) 2009 nullwire aps

Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without
restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

Contributors: 
Mads Kristiansen, mads.kristiansen@nullwire.com
Glen Humphrey
Evan Charlton
Peter Hewitt
*/

package com.application.error;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.application.utils.Utilities;

public class ExceptionHandler {
	
	public static String TAG = "com.nullwire.trace.ExceptionsHandler";
	
	private static String[] stackTraceFileList = null;
	
	/**
	 * Register handler for unhandled exceptions.
	 * @param context
	 */
	public static boolean register(Context context) {
		Log.i(TAG, "Registering default exceptions handler");
		// Get information about the Package
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo pi;
			// Version
			pi = pm.getPackageInfo(context.getPackageName(), 0);
			G.APP_VERSION = pi.versionName;
			// Version Code name
			G.APP_VERSION_CODE = String.valueOf(pi.versionCode);//ADDED VERSION CODE
			// Package name
			G.APP_PACKAGE = pi.packageName;
			// Files dir for storing the stack traces
			G.FILES_PATH = context.getFilesDir().getAbsolutePath();
			// Device model
            G.PHONE_MODEL = android.os.Build.MODEL;
            // Device Size
            try{
            	G.PHONE_SIZE = String.valueOf(Utilities.checkDisplaySize());
            }catch(Exception e){
            	G.PHONE_SIZE = "DEVICE SIZE NOT FOUND";
            }
            // Android version
            G.ANDROID_VERSION = android.os.Build.VERSION.RELEASE;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		Log.i(TAG, "TRACE_VERSION: " + G.TraceVersion);
		Log.d(TAG, "APP_VERSION: " + G.APP_VERSION);
		Log.d(TAG, "APP_VERSION_CODE: " + G.APP_VERSION_CODE);//ADDED VERSION CODE
		Log.d(TAG, "APP_PACKAGE: " + G.APP_PACKAGE);
		Log.d(TAG, "FILES_PATH: " + G.FILES_PATH);
		Log.d(TAG, "URL: " + G.URL);
		
		boolean stackTracesFound = false;
		// We'll return true if any stack traces were found
		if ( searchForStackTraces().length > 0 ) {
			stackTracesFound = true;
		}
		
		new Thread() {
			@Override
			public void run() {
				// First of all transmit any stack traces that may be lying around
				submitStackTraces();
				UncaughtExceptionHandler currentHandler = Thread.getDefaultUncaughtExceptionHandler();
				if (currentHandler != null) {
					Log.d(TAG, "current handler class="+currentHandler.getClass().getName());
				}	
				// don't register again if already registered
				if (!(currentHandler instanceof DefaultExceptionHandler)) {
					// Register default exceptions handler
					Thread.setDefaultUncaughtExceptionHandler(
							new DefaultExceptionHandler(currentHandler));
				}
			}
       	}.start();
		
		return stackTracesFound;
	}
	
	/**
	 * Register handler for unhandled exceptions.
	 * @param context
	 * @param Url
	 */
	public static void register(Context context, String url) {
		Log.i(TAG, "Registering default exceptions handler: " + url);
		// Use custom URL
		G.URL = url;
		// Call the default register method
		register(context);
	}

	
	/**
	 * Search for stack trace files.
	 * @return
	 */
	private static String[] searchForStackTraces() {
		if ( stackTraceFileList != null ) {
			return stackTraceFileList;
		}
		File dir = new File(G.FILES_PATH + "/");
		// Try to create the files folder if it doesn't exist
		dir.mkdir();
		// Filter for ".stacktrace" files
		FilenameFilter filter = new FilenameFilter() { 
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".stacktrace"); 
			} 
		}; 
		return (stackTraceFileList = dir.list(filter));	
	}
	
	/**
	 * Look into the files folder to see if there are any "*.stacktrace" files.
	 * If any are present, submit them to the trace server.
	 */
	public static void submitStackTraces() {
		try {
			Log.d(TAG, "Looking for exceptions in: " + G.FILES_PATH);
			String[] list = searchForStackTraces();
			if ( list != null && list.length > 0 ) {
				Log.d(TAG, "Found "+list.length+" stacktrace(s)");
				for (int i=0; i < list.length; i++) {
					String filePath = G.FILES_PATH+"/"+list[i];
					// Extract the version from the filename: "packagename-version-...."
					String version = list[i].split("-")[0];
					Log.d(TAG, "Stacktrace in file '"+filePath+"' belongs to version " + version);
					// Read contents of stacktrace
					StringBuilder contents = new StringBuilder();
					BufferedReader input =  new BufferedReader(new FileReader(filePath));
					String line = null;
					String androidVersion = null;
	                String phoneModel = null;
	                while (( line = input.readLine()) != null){
                        if (androidVersion == null) {
                            androidVersion = line;
                            continue;
                        }
                        else if (phoneModel == null) {
                            phoneModel = line;
                            continue;
                        }
                        contents.append(line);
			            contents.append(System.getProperty("line.separator"));
			        }
			        input.close();
			        String stacktrace;
			        stacktrace = contents.toString();
			        Log.d(TAG, "Transmitting stack trace: " + stacktrace);
			        // Transmit stack trace with POST request
					DefaultHttpClient httpClient = new DefaultHttpClient(); 
					HttpPost httpPost = new HttpPost(G.URL);
					List <NameValuePair> nvps = new ArrayList <NameValuePair>(); 
					nvps.add(new BasicNameValuePair("package_name", G.APP_PACKAGE));
					nvps.add(new BasicNameValuePair("package_version", version));
                    nvps.add(new BasicNameValuePair("phone_model", phoneModel));
                    nvps.add(new BasicNameValuePair("android_version", androidVersion));
                    nvps.add(new BasicNameValuePair("stacktrace", stacktrace));
					httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8)); 
					// We don't care about the response, so we just hope it went well and on with it
					httpClient.execute(httpPost);					
				}
			}
		} catch( Exception e ) {
			e.printStackTrace();
		} finally {
			try {
				String[] list = searchForStackTraces();
				for ( int i = 0; i < list.length; i ++ ) {
					File file = new File(G.FILES_PATH+"/"+list[i]);
					file.delete();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
