package com.xmog.web.websocket;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

import javax.ws.rs.Path;

import org.slf4j.Logger;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.xmog.event.LocationUpdateEvent;
import com.xmog.model.DeviceLocation;
import com.xmog.service.DeviceLocationService;
import com.xmog.stack.templating.json.JsonMapper;
import com.xmog.stack.web.websocket.StackWebSocket;

/**
 * @author Dan Kelley
 */
@Path("/websocket/location/update")
public class DeviceLocationWebSocket extends StackWebSocket {
  private Long questionId;

  @Inject
  private DeviceLocationService deviceLocationService;

  @Inject
  private JsonMapper jsonMapper;
  
  private EventBus eventBus;

  private Logger logger = getLogger(getClass());

  @Inject
  public DeviceLocationWebSocket(EventBus eventBus) {
    super();
    eventBus.register(this);
    this.eventBus = eventBus;
    logger.debug(format("Register DeviceLocationWebSocket with %s", eventBus));
  }
  
  @Override
  protected void onClose(int statusCode, String reason) {
    getEventBus().unregister(this);
  }

  @Subscribe
  public void handleLocationUpdateEvent(LocationUpdateEvent event) {

    logger.debug(format("%s received %s. a location was updated or inserted...", this, event));

    DeviceLocation deviceLocation = deviceLocationService.findDeviceLocationById(event.getDeviceLocationId());
    
    getRemote().sendStringByFuture(jsonMapper.toJson(deviceLocation));
  }

  @Override
  protected void onMessage(String json) {
	  logger.debug("onMessage /websocket/location/update");
	  // nothing coming down now
  }
  
  public EventBus getEventBus() {
    return eventBus;
  }
}