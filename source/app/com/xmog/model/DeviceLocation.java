package com.xmog.model;

import static java.lang.String.format;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.inject.Inject;

import com.xmog.stack.util.DateUtils;
import com.xmog.stack.util.DateFormatter;

public class DeviceLocation {

	@Inject
	DateFormatter dateFormatter;

	public static final String DEFAULT_DATE_TIME_FORMAT = "MMMM d, yyyy 'at' h:mm a"; // z
	public static final String DEFAULT_DATE_FORMAT = "MMMM d, yyyy";
	public static final String DEFAULT_TIME_FORMAT = "h:mm a";

	private long deviceLocationId;
	private String deviceIdentifier;
	private Double speed;
	private Double course;
	private Double latitude;
	private Double longitude;
	private Date createdDate;

	private static final Double MULTIPLIER = 2.236936284;

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public String getMilesPerHour() {
		if (getSpeed() != null && getSpeed() > 0)
			return format("%.1f", MULTIPLIER * getSpeed());
		else
			return format("%.1f", 0d);
	}

	public String getWarningStyle() {
		long dateDiff = new Date().getTime() - createdDate.getTime();
		// logger.debug("Date Diff: " + dateDiff);
		if (dateDiff > 84600000)
			return "error";
		else if (dateDiff > 3525000)
			return "warning";
		else
			return "";
	}

	public String getDirection() {
		// logger.debug("Checking course of " + course);
		if (course == null || course == -1l)
			return "--";
		else {
			if (course <= 22.5 || course > 337.5)
				return "N";
			else if (course > 22.5 && course <= 67.5)
				return "NE";
			else if (course > 67.5 && course <= 112.5)
				return "E";
			else if (course > 112.5 && course <= 157.5)
				return "SE";
			else if (course > 157.5 && course <= 202.5)
				return "S";
			else if (course > 202.5 && course <= 247.5)
				return "SW";
			else if (course > 247.5 && course <= 292.5)
				return "W";
			else if (course > 292.5 && course <= 337.5)
				return "NW";
			else
				return "--";
		}
	}

	public String getDeviceIdentifier() {
		return deviceIdentifier;
	}

	public void setDeviceIdentifier(String deviceIdentifier) {
		this.deviceIdentifier = deviceIdentifier;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	// public String getFormattedCreatedDate(){
	// return dateFormatter.formatDate(createdDate,
	// DeviceLocation.DEFAULT_DATE_FORMAT);
	// }

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Double getSpeed() {
		return speed;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	public Double getCourse() {
		return course;
	}

	public void setCourse(Double course) {
		this.course = course;
	}

	public long getDeviceLocationId() {
		return deviceLocationId;
	}

	public void setDeviceLocationId(long deviceLocationId) {
		this.deviceLocationId = deviceLocationId;
	}

}
