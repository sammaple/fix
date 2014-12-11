package com.androd.bugreporter;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.util.Log;

import com.androd.bugreporter.utils.IshareContentData.DeviceData;

public class MyApplication extends Application {

	DeviceData user = new DeviceData();

	public DeviceData getUser() {
		return user;
	}

	public void setUser(DeviceData user) {
		this.user = user;
	}

	public static String TAG = "Bugreporter";

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d(TAG, "application completed ... ");

		Cursor cursor = getContentResolver().query(DeviceData.CONTENT_URI,
				null, null, null, null);
		if (null != cursor && cursor.getCount() > 0) {
			// Log.i(TAG, "start.");
			cursor.moveToFirst();
			user.restore(cursor);

			PackageManager packageManager = getPackageManager();
			String version = "";
			PackageInfo pi;
			try {
				pi = packageManager.getPackageInfo("com.androd.bugreporter",
						PackageManager.GET_ACTIVITIES);
				version = pi.versionName;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (!user.version.equals(version)) {

				Log.d(TAG, "application restart deviceinfo... ... ");
				getContentResolver().delete(DeviceData.CONTENT_URI, null, null);
				user = new DeviceData();
			}

			cursor.close();
			cursor = null;
		}

		/*
		 * else{ String android_id = Secure.getString(getContentResolver(),
		 * Secure.ANDROID_ID); }
		 */
	}
}
