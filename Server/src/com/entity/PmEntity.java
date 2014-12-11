package com.entity;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity("pminfo")
public class PmEntity {
	@Id
	private ObjectId id;

	String  deviceid;

	String time;// query order uses
	String datetime;

	List<PackageInfoData> pmList = new ArrayList<PackageInfoData>();

	public String getId() {
		return id.toString();
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public List<PackageInfoData> getPmList() {
		return pmList;
	}

	public void setPmList(List<PackageInfoData> pmList) {
		this.pmList = pmList;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

}
