package com.xmog.web.context;

import javax.servlet.http.HttpServletRequest;

import com.xmog.model.Account;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.xmog.stack.web.RememberMeManager;
import com.xmog.stack.web.RequestTypeManager;
import com.xmog.stack.web.context.AbstractCurrentContext;

/**
 * @author Transmogrify LLC.
 */
public class CurrentContext extends AbstractCurrentContext<Account, Long, Long> {
  @Inject
  public CurrentContext(Provider<HttpServletRequest> httpRequestProvider, RequestTypeManager requestTypeManager,
      RememberMeManager rememberMeManager) {
    super(httpRequestProvider, requestTypeManager, rememberMeManager);
  }
}