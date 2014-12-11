package com.androd.bugreporter.utils;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class IshareContentData {
	public static final String AUTHORITIES = "com.androd.bugreporter.provider";

	public static final String DATABASE_NAME = "fix.db";

	public static final int DATABASE_VERSION = 1;

	public static final class DeviceData implements BaseColumns {

		public static final String TABLE_NAME = "device";

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITIES + "/" + TABLE_NAME);

		public static final String CONTENT_TYPE = AUTHORITIES + "/"
				+ TABLE_NAME;

		public static final String ID = "id";
		public static final String DEVICEID = "deviceid";
		public static final String SESSIONID = "sessionid";// form server
		public static final String VERSION = "self_version";
		public static final String DATA1 = "data1";
		public static final String DATA2 = "data2";
		public static final String DATA3 = "data3";
		public static final String DATA4 = "data4";

		public long id;
		public String deviceid = "";
		public String sessionid = "";
		public String version = "";
		public String data1 = "";
		public String data2 = "";
		public String data3 = "";
		public String data4 = "";

		public void restore(Cursor cursor) {
			this.id = cursor.getLong(cursor.getColumnIndex(ID));
			this.deviceid = cursor.getString(cursor.getColumnIndex(DEVICEID));
			this.sessionid = cursor.getString(cursor.getColumnIndex(SESSIONID));
			this.version = cursor.getString(cursor.getColumnIndex(VERSION));
			this.data1 = cursor.getString(cursor.getColumnIndex(DATA1));
			this.data2 = cursor.getString(cursor.getColumnIndex(DATA2));
			this.data3 = cursor.getString(cursor.getColumnIndex(DATA3));
			this.data4 = cursor.getString(cursor.getColumnIndex(DATA4));
		}

	}

	public static final class PropInfoData implements BaseColumns {

		public static final String TABLE_NAME = "prop";

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITIES + "/" + TABLE_NAME);

		public static final String CONTENT_TYPE = AUTHORITIES + "/"
				+ TABLE_NAME;

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

		public static final String ID = "id";
		public static final String BUILDID = "buildid";
		public static final String FINGERPRINT = "fingerprint";
		public static final String PRODUCT = "product";
		public static final String TYPE = "type";
		public static final String REL = "releaseversion";
		public static final String SDK = "sdkveriosn";
		public static final String RAM = "ram";
		public static final String MODEL = "model";
		public static final String DATA1 = "data1";
		public static final String DATA2 = "data2";
		public static final String DATA3 = "data3";
		public static final String DATA4 = "data4";

		public long id;
		public String buildid = "";
		public String fingerprint = "";
		public String product = "";
		public String type = "";
		public String releaseversion = "";
		public String sdkveriosn = "";
		public String ram = "";
		public String model = "";
		public String data1 = "";
		public String data2 = "";
		public String data3 = "";
		public String data4 = "";

		public void restore(Cursor cursor) {
			this.id = cursor.getLong(cursor.getColumnIndex(ID));
			this.buildid = cursor.getString(cursor.getColumnIndex(BUILDID));
			this.fingerprint = cursor.getString(cursor
					.getColumnIndex(FINGERPRINT));
			this.product = cursor.getString(cursor.getColumnIndex(PRODUCT));
			this.type = cursor.getString(cursor.getColumnIndex(TYPE));
			this.releaseversion = cursor.getString(cursor.getColumnIndex(REL));
			this.sdkveriosn = cursor.getString(cursor.getColumnIndex(SDK));
			this.ram = cursor.getString(cursor.getColumnIndex(RAM));
			this.model = cursor.getString(cursor.getColumnIndex(MODEL));
			this.data1 = cursor.getString(cursor.getColumnIndex(DATA1));
			this.data2 = cursor.getString(cursor.getColumnIndex(DATA2));
			this.data3 = cursor.getString(cursor.getColumnIndex(DATA3));
			this.data4 = cursor.getString(cursor.getColumnIndex(DATA4));
		}
	}

	public static class PackageInfoData implements BaseColumns {
		public static final String TABLE_NAME = "package";

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITIES + "/" + TABLE_NAME);

		public static final String CONTENT_TYPE = AUTHORITIES + "/"
				+ TABLE_NAME;

		public static final String ID = "id";
		public static final String NAME = "packagename";
		public static final String VERISON = "packageversion";//NAME
		public static final String CODE = "packagecode";
		public static final String LABEL = "packagelabel";
		public static final String DATA1 = "data1";
		public static final String DATA2 = "data2";
		public static final String DATA3 = "data3";
		public static final String DATA4 = "data4";

		public long id;
		public String packagename;
		public String packageversion;
		public String packagecode;
		public String packagelabel;
		public String data1 = "";
		public String data2 = "";
		public String data3 = "";
		public String data4 = "";

		public void restore(Cursor cursor) {
			this.id = cursor.getLong(cursor.getColumnIndex(ID));
			this.packagename = cursor.getString(cursor.getColumnIndex(NAME));
			this.packageversion = cursor.getString(cursor
					.getColumnIndex(VERISON));
			this.packagecode = cursor.getString(cursor.getColumnIndex(CODE));
			this.packagelabel = cursor.getString(cursor.getColumnIndex(LABEL));
			this.data1 = cursor.getString(cursor.getColumnIndex(DATA1));
			this.data2 = cursor.getString(cursor.getColumnIndex(DATA2));
			this.data3 = cursor.getString(cursor.getColumnIndex(DATA3));
			this.data4 = cursor.getString(cursor.getColumnIndex(DATA4));
		}
	}
}
