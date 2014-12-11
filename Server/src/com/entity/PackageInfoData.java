package com.entity;

import java.io.Serializable;

public class PackageInfoData extends UserEntity implements Serializable {



	public PackageInfoData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PackageInfoData(String deviceid, String sessionid, String version,
			String buildid, String fingerprint, String product, String type,
			String releaseversion, String sdkveriosn, String ram, String time,
			String datetime, String ip, String packagename,
			String packageversion, String packagecode, String packagelabel) {
		super(deviceid, sessionid, version, buildid, fingerprint, product,
				type, releaseversion, sdkveriosn, ram, time, datetime, ip);
		this.packagename = packagename;
		this.packageversion = packageversion;
		this.packagecode = packagecode;
		this.packagelabel = packagelabel;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public String packagename;
	public String packageversion;
	public String packagecode;
	public String packagelabel;


	public String getPackagename() {
		return packagename;
	}

	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}

	public String getPackageversion() {
		return packageversion;
	}

	public void setPackageversion(String packageversion) {
		this.packageversion = packageversion;
	}

	public String getPackagecode() {
		return packagecode;
	}

	public void setPackagecode(String packagecode) {
		this.packagecode = packagecode;
	}

	public String getPackagelabel() {
		return packagelabel;
	}

	public void setPackagelabel(String packagelabel) {
		this.packagelabel = packagelabel;
	}


}
