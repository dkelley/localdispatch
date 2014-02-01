package com.xmog.web.controller.api;

import static com.xmog.stack.web.ContentTypes.CONTENT_TYPE_JSON;
import static java.lang.String.format;
import static java.util.Collections.singleton;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.xmog.command.AccountSignInCommand;
import com.xmog.model.Account;
import com.xmog.service.AccountService;
import com.xmog.stack.exception.NotFoundException;
import com.xmog.stack.web.annotation.Public;
import com.xmog.web.context.CurrentContext;

/**
 * @author Transmogrify LLC.
 */
@Singleton
@Path("/api/accounts")
@Produces(CONTENT_TYPE_JSON)
public class AccountApiController {
  @Inject
  private CurrentContext currentContext;

  @Inject
  private AccountService accountService;
  
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @POST
  @Public
  @Path("sign-in")
  @Consumes(CONTENT_TYPE_JSON)
  public Map<String, Object> signIn(AccountSignInCommand command) {
    Account account =
        accountService.findAccountByEmailAddressAndPassword(command.getEmailAddress(), command.getPassword());

    if (account == null)
      throw new NotFoundException("Sorry, we were unable to sign in with the credentials you provided.");

    logger.debug(format("%s %s",account.getEmailAddress(), account.getRoleId()));
    currentContext.signIn(account, singleton(account.getRoleId()), command.getRememberMe());

    return new HashMap<String, Object>() {
      {
        put("destinationUrl", "/dashboard");
      }
    };
  }

  @POST
  @Public
  @Path("sign-out")
  @Consumes(CONTENT_TYPE_JSON)
  public Map<String, Object> signOut() {
    currentContext.signOut();

    return new HashMap<String, Object>() {
      {
        put("destinationUrl", "/");
      }
    };
  }
}