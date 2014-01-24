package com.xmog.service;

import static com.xmog.model.Role.ROLE_ADMINISTRATOR;
import static com.xmog.model.Role.ROLE_USER;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.xmog.model.Account;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.xmog.stack.db.Database;
import com.xmog.stack.service.StackAccountService;

/**
 * @author Transmogrify LLC.
 */
@Singleton
public class AccountService implements StackAccountService<Account, Long, Long> {
  @Inject
  @SuppressWarnings("unused")
  private Database database;

  public Account findAccountByEmailAddressAndPassword(String emailAddress, String password) {
    emailAddress = trimToEmpty(emailAddress).toLowerCase();

    // TODO: pull from database instead
    for (Account account : ACCOUNTS)
      if (account.getEmailAddress().equals(emailAddress) && "test".equals(password))
        return account;

    return null;
  }  

  public Account findAccountByApiToken(String apiToken) {
    // TODO: pull from database instead
    for (Account account : ACCOUNTS)
      if (account.getApiToken().equals(apiToken))
        return account;

    return null;
  }

  public Account findAccountByRememberMeToken(String rememberMeToken) {
    // TODO: pull from database instead
    for (Account account : ACCOUNTS)
      if (account.getRememberMeToken().equals(rememberMeToken))
        return account;

    return null;
  }

  public List<Long> findRolesByAccountId(Long accountId) {
    // TODO: pull from database instead
    for (Account account : ACCOUNTS)
      if (account.getAccountId().equals(accountId))
        return ImmutableList.of(account.getRoleId());

    return ImmutableList.of();
  }

  private static final Set<Account> ACCOUNTS = new HashSet<Account>() {
    {
      add(new Account() {
        {
          setAccountId(1L);
          setEmailAddress("test@xmog.com");
          setRoleId(ROLE_USER);
          setApiToken("ABC");
          setRememberMeToken("123");
        }
      });      
      add(new Account() {
        {
          setAccountId(2L);
          setEmailAddress("admin@xmog.com");
          setRoleId(ROLE_ADMINISTRATOR);
          setApiToken("DEF");
          setRememberMeToken("456");
        }
      });
    }
  };
}
