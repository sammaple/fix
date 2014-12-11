package com.androd.bugreporter.utils;

import com.androd.bugreporter.utils.IshareContentData.DeviceData;
import com.androd.bugreporter.utils.IshareContentData.PackageInfoData;
import com.androd.bugreporter.utils.IshareContentData.PropInfoData;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

public class IshareProvider extends ContentProvider {
	private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

	private static final int DEVICE = 1;
	private static final int PROP = 2;
	private static final int PACKAGE = 3;
	private static final int DEVICE_ID = 4;
	private static final int PROP_ID = 5;
	private static final int PACKAGE_ID = 6;

	private DatabaseHelper dbHelper;

	static {
		matcher.addURI(IshareContentData.AUTHORITIES, DeviceData.TABLE_NAME,
				DEVICE);
		matcher.addURI(IshareContentData.AUTHORITIES, DeviceData.TABLE_NAME
				+ "/#", DEVICE_ID);
		matcher.addURI(IshareContentData.AUTHORITIES, PropInfoData.TABLE_NAME,
				PROP);
		matcher.addURI(IshareContentData.AUTHORITIES, PropInfoData.TABLE_NAME
				+ "/#", PROP_ID);
		matcher.addURI(IshareContentData.AUTHORITIES,
				PackageInfoData.TABLE_NAME, PACKAGE);
		matcher.addURI(IshareContentData.AUTHORITIES,
				PackageInfoData.TABLE_NAME + "/#", PACKAGE_ID);
	}

	public boolean onCreate() {
		dbHelper = new DatabaseHelper(this.getContext(),
				IshareContentData.DATABASE_NAME);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		return true;
	}

	private String whereWithId(String id, String selection) {
		StringBuilder sb = new StringBuilder(256);
		sb.append("id=");
		sb.append(id);
		if (selection != null) {
			sb.append(" AND (");
			sb.append(selection);
			sb.append(')');
		}
		return sb.toString();
	}

	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long rowId;

		switch (matcher.match(uri)) {
		case DEVICE: // 向表中添加新纪录并返回其行号
			rowId = db.insert(DeviceData.TABLE_NAME, DeviceData.DEVICEID, values);
			return ContentUris.withAppendedId(uri, rowId);
		case PACKAGE:
			rowId = db.insert(PackageInfoData.TABLE_NAME,PackageInfoData.LABEL, values);
			return ContentUris.withAppendedId(uri, rowId);
		case PROP:
			rowId = db.insert(PropInfoData.TABLE_NAME, PropInfoData.BUILDID, values);
			return ContentUris.withAppendedId(uri, rowId);
		default:
			throw new IllegalArgumentException("Unknow Uri:" + uri);
		}
	}

	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String id;
		switch (matcher.match(uri)) {
		case DEVICE:
			return db.query(DeviceData.TABLE_NAME, projection, selection,
					selectionArgs, null, null, sortOrder);
		case DEVICE_ID:
			id = uri.getPathSegments().get(1);
			return db.query(DeviceData.TABLE_NAME, projection,
					whereWithId(id, selection), selectionArgs, null, null,
					sortOrder);

		case PACKAGE:
			return db.query(PackageInfoData.TABLE_NAME, projection, selection,
					selectionArgs, null, null, sortOrder);
		case PACKAGE_ID:
			id = uri.getPathSegments().get(1);
			return db.query(PackageInfoData.TABLE_NAME, projection,
					whereWithId(id, selection), selectionArgs, null, null,
					sortOrder);
		case PROP:
			return db.query(PropInfoData.TABLE_NAME, projection, selection,
					selectionArgs, null, null, sortOrder);
		case PROP_ID:
			id = uri.getPathSegments().get(1);
			return db.query(PropInfoData.TABLE_NAME, projection,
					whereWithId(id, selection), selectionArgs, null, null,
					sortOrder);
		default:
			throw new IllegalArgumentException("Unknown Uri:" + uri);
		}
	}

	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String id;
		switch (matcher.match(uri)) {
		case DEVICE: // 更新指定记录
			return db.update(DeviceData.TABLE_NAME, values, selection,
					selectionArgs);

		case DEVICE_ID:
			id = uri.getPathSegments().get(1);
			return db.update(DeviceData.TABLE_NAME, values,
					whereWithId(id, selection), selectionArgs);
		case PACKAGE: // 更新指定记录
			return db.update(PackageInfoData.TABLE_NAME, values, selection,
					selectionArgs);

		case PACKAGE_ID:
			id = uri.getPathSegments().get(1);
			return db.update(PackageInfoData.TABLE_NAME, values,
					whereWithId(id, selection), selectionArgs);
		case PROP: // 更新指定记录
			return db.update(PropInfoData.TABLE_NAME, values, selection,
					selectionArgs);

		case PROP_ID:
			id = uri.getPathSegments().get(1);
			return db.update(PropInfoData.TABLE_NAME, values,
					whereWithId(id, selection), selectionArgs);

		default:
			throw new IllegalArgumentException("Unknow Uri" + uri);
		}

	}

	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int num = 0;
		switch (matcher.match(uri)) {
		case DEVICE:
			num = db.delete(DeviceData.TABLE_NAME, selection, selectionArgs);
			break;
		case PACKAGE:
			num = db.delete(PackageInfoData.TABLE_NAME, selection,
					selectionArgs);
			break;
		case PROP:
			num = db.delete(PropInfoData.TABLE_NAME, selection, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown Uri:" + uri);
		}
		return num;
	}


	public String getType(Uri uri) {
		switch (matcher.match(uri)) {
		case DEVICE:
			return DeviceData.CONTENT_TYPE;
		case DEVICE_ID:
			return DeviceData.CONTENT_TYPE;
		case PROP:
			return PropInfoData.CONTENT_TYPE;
		case PROP_ID:
			return PropInfoData.CONTENT_TYPE;
		case PACKAGE:
			return PackageInfoData.CONTENT_TYPE;
		case PACKAGE_ID:
			return PackageInfoData.CONTENT_TYPE;
		default:
			throw new IllegalArgumentException("Unknown Uri:" + uri);
		}
	}

	public static void createDeviceTable(SQLiteDatabase db) {
		String s = " (" + DeviceData.ID
				+ " integer primary key autoincrement, "

				+ DeviceData.DEVICEID + " text, " + DeviceData.SESSIONID
				+ " text, " + DeviceData.VERSION + " text, " + DeviceData.DATA1
				+ " text, " + DeviceData.DATA2 + " text, " + DeviceData.DATA3
				+ " text, " + DeviceData.DATA4 + " text " + ");";

		db.execSQL("create table " + DeviceData.TABLE_NAME + s);
	}

	public static void createPackageTable(SQLiteDatabase db) {
		String s = " (" + PackageInfoData.ID
				+ " integer primary key autoincrement, "

				+ PackageInfoData.NAME + " text, " + PackageInfoData.VERISON
				+ " text, " + PackageInfoData.CODE + " text, "
				+ PackageInfoData.LABEL + " text, " + PackageInfoData.DATA1
				+ " int, " + PackageInfoData.DATA2 + " text, "
				+ PackageInfoData.DATA3 + " text, " + PackageInfoData.DATA4
				+ " text" + ");";

		db.execSQL("create table " + PackageInfoData.TABLE_NAME + s);
	}

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

	public static void createPropTable(SQLiteDatabase db) {
		String s = " (" + PropInfoData.ID
				+ " integer primary key autoincrement, "

				+ PropInfoData.BUILDID + " text, " + PropInfoData.FINGERPRINT
				+ " int, " + PropInfoData.PRODUCT + " int, "
				+ PropInfoData.TYPE + " text, " + PropInfoData.REL + " text, "
				+ PropInfoData.SDK + " text," + PropInfoData.RAM + " text, "
				+ PropInfoData.MODEL + " text," + PropInfoData.DATA1
				+ " text, " + PropInfoData.DATA2 + " text,"
				+ PropInfoData.DATA3 + " text, " + PropInfoData.DATA4 + " text"

				+ ");";

		db.execSQL("create table " + PropInfoData.TABLE_NAME + s);
	}

	/* package */static SQLiteDatabase getReadableDatabase(Context context) {
		DatabaseHelper helper = new IshareProvider().new DatabaseHelper(
				context, IshareContentData.DATABASE_NAME);
		return helper.getReadableDatabase();
	}

	private class DatabaseHelper extends SQLiteOpenHelper {
		Context mContext;

		DatabaseHelper(Context context, String name) {
			super(context, name, null, 1);
			mContext = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			createDeviceTable(db);
			createPropTable(db);
			createPackageTable(db);
		}

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}
	}
}