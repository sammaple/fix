package com.entity;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity("user")
public class UserEntity {
	   @Id
	    private ObjectId id;

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

		///////more server data
		public String time;// query order uses
		
		public String datetime;

		public String ip;
		
		public UserEntity() {
			super();
		}	
		

		public UserEntity(String deviceid, String sessionid, String version,
				String buildid, String fingerprint, String product,
				String type, String releaseversion, String sdkveriosn,
				String ram, String time, String datetime, String ip) {
			super();
			this.deviceid = deviceid;
			this.sessionid = sessionid;
			this.version = version;
			this.buildid = buildid;
			this.fingerprint = fingerprint;
			this.product = product;
			this.type = type;
			this.releaseversion = releaseversion;
			this.sdkveriosn = sdkveriosn;
			this.ram = ram;
			this.time = time;
			this.datetime = datetime;
			this.ip = ip;
		}

		public ObjectId getId() {
			return id;
		}

		public void setId(ObjectId id) {
			this.id = id;
		}

		public String getDeviceid() {
			return deviceid;
		}

		public void setDeviceid(String deviceid) {
			this.deviceid = deviceid;
		}

		public String getSessionid() {
			return sessionid;
		}

		public void setSessionid(String sessionid) {
			this.sessionid = sessionid;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getBuildid() {
			return buildid;
		}

		public void setBuildid(String buildid) {
			this.buildid = buildid;
		}

		public String getFingerprint() {
			return fingerprint;
		}

		public void setFingerprint(String fingerprint) {
			this.fingerprint = fingerprint;
		}

		public String getProduct() {
			return product;
		}

		public void setProduct(String product) {
			this.product = product;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getReleaseversion() {
			return releaseversion;
		}

		public void setReleaseversion(String releaseversion) {
			this.releaseversion = releaseversion;
		}

		public String getSdkveriosn() {
			return sdkveriosn;
		}

		public void setSdkveriosn(String sdkveriosn) {
			this.sdkveriosn = sdkveriosn;
		}

		public String getRam() {
			return ram;
		}

		public void setRam(String ram) {
			this.ram = ram;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getDatetime() {
			return datetime;
		}

		public void setDatetime(String datetime) {
			this.datetime = datetime;
		}

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}
		
		
}
