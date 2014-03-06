package com.xmog.event;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import com.xmog.web.service.DeviceLocationService;

public class LocationUpdateEvent {
	
	  private static final Logger logger = getLogger(DeviceLocationService.class);

	public LocationUpdateEvent(Long deviceLocationId){
		logger.debug(format("Created event %d", deviceLocationId));
		this.deviceLocationId = deviceLocationId;
	}

	private Long deviceLocationId;

	public Long getDeviceLocationId() {
		return deviceLocationId;
	}

	public void setDeviceLocationId(Long deviceLocationId) {
		this.deviceLocationId = deviceLocationId;
	}
	
}
