package com.xmog.web.controller.page;

import static com.xmog.stack.web.ContentTypes.CONTENT_TYPE_HTML;
import static java.lang.String.format;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Singleton;
import com.xmog.stack.web.Page;
import com.xmog.stack.web.annotation.Public;

/**
 * @author Transmogrify LLC.
 */
@Singleton
@Path("/app")
@Produces(CONTENT_TYPE_HTML)
public class ApplicationPageController {
  @GET  
  @Public
  public Page indexPage() {
    return dashboardPage("index");
  }

  protected Page dashboardPage(String name) {
    return dashboardPage(name, ImmutableMap.<String, Object> of());
  }
  
  protected Page dashboardPage(final String name, Map<String, Object> model) {
    return new Page("templates/application.html", new HashMap<String, Object>(model) {
      {
        put("body", format("pages/application/%s/body.html", name));
        put("javascript", format("pages/application/%s/javascript.html", name));
      }
    });
  }
}