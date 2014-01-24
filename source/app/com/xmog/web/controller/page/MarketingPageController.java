package com.xmog.web.controller.page;

import static com.xmog.stack.web.ContentTypes.CONTENT_TYPE_HTML;
import static java.lang.String.format;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.google.inject.Singleton;
import com.xmog.stack.web.Page;
import com.xmog.stack.web.annotation.Public;

/**
 * @author Transmogrify LLC.
 */
@Singleton
@Path("/")
@Produces(CONTENT_TYPE_HTML)
public class MarketingPageController {
  @GET
  @Public
  public Page indexPage() {
    return marketingPage("index", new HashMap<String, Object>() {
      {
        put("title", "Hello, World!");
        put("appName", "LocalDispatch");
      }
    });
  }
  
  @GET
  @Public
  @Path("/sign-in")
  public Page signInPage() {
    return marketingPage("sign-in", new HashMap<String, Object>() {
      {
        put("title", "Sign In");        
      }
    });
  }  

  protected Page marketingPage(final String name, Map<String, Object> model) {
    return new Page("templates/standard.html", new HashMap<String, Object>(model) {
      {
        put("body", format("pages/marketing/%s/body.html", name));
        put("javascript", format("pages/marketing/%s/javascript.html", name));
      }
    });
  }
}