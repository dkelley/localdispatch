package com.xmog.web.controller.api;

import static com.xmog.stack.web.ContentTypes.CONTENT_TYPE_JSON;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.xmog.command.SaveLocationCommand;
import com.xmog.stack.exception.NotFoundException;
import com.xmog.stack.web.annotation.Public;
import com.xmog.web.context.CurrentContext;
import com.xmog.web.service.DeviceLocationService;

/**
 * @author Transmogrify LLC.
 */
@Singleton
@Path("/api/location")
@Produces(CONTENT_TYPE_JSON)
public class LocationController {
	@Inject
	private CurrentContext currentContext;

	@Inject
	private DeviceLocationService deviceLocationService;

	@POST
	@Public
	@Consumes(CONTENT_TYPE_JSON)
	public void saveLocation(SaveLocationCommand command) {
		if (command == null)
			throw new NotFoundException("Sorry, we were unable to store empty location.");

		deviceLocationService.saveDeviceLocation(command);

	}

}
