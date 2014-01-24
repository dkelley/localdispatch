package com.xmog.web;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.xmog.stack.templating.json.JsonMapper;
import com.xmog.stack.web.CachedStaticFileMapper;
import com.xmog.stack.web.StackCommonPageModelProvider;
import com.xmog.web.context.CurrentContext;

/**
 * @author Transmogrify LLC.
 */
@Singleton
public class CommonPageModelProvider implements StackCommonPageModelProvider {
  @Inject
  private CurrentContext currentContext;
  
  @Inject
  private CachedStaticFileMapper  cachedStaticFileMapper;

  @Inject
  private JsonMapper jsonMapper;
  
  public Map<String, Object> getCommonPageModel() {
    return new HashMap<String, Object>() {
      {
        put("currentContext", currentContext);
        put("cachedUrls", jsonMapper.toJson(cachedStaticFileMapper.getCachedFilenameMapping()));
        // TODO: add any other data you'd like to be exposed to page Velocity templates
      }
    };
  }
}