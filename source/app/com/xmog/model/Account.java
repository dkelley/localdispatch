package com.xmog.model;

import static com.google.common.base.Objects.toStringHelper;

import java.util.Locale;
import java.util.TimeZone;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.google.common.base.Objects.ToStringHelper;
import com.xmog.stack.web.context.StackAccount;

/**
 * @author Transmogrify LLC.
 */
public class Account implements StackAccount<Long> {
  private Long accountId;
  private Long roleId;
  private String emailAddress;
  private String apiToken;
  private String rememberMeToken;
  private String timeZoneIdentifier;
  private String languageIdentifier;
  private String countryIdentifier;  

  @Override
  @JsonIgnore
  public TimeZone getTimeZone() {
    return TimeZone.getTimeZone(getTimeZoneIdentifier());
  }
  
  @Override
  @JsonIgnore
  public Locale getLocale() {
    return new Locale(getLanguageIdentifier(), getCountryIdentifier());
  }

  @Override
  public String toString() {
    ToStringHelper toStringHelper = toStringHelper(this);
    toStringHelper.add("accountId", getAccountId());
    toStringHelper.add("emailAddress", getEmailAddress());
    return toStringHelper.toString();
  }

  public Long getAccountId() {
    return accountId;
  }

  public void setAccountId(Long accountId) {
    this.accountId = accountId;
  }

  public Long getRoleId() {
    return roleId;
  }

  public void setRoleId(Long roleId) {
    this.roleId = roleId;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public String getApiToken() {
    return apiToken;
  }

  public void setApiToken(String apiToken) {
    this.apiToken = apiToken;
  }

  public String getRememberMeToken() {
    return rememberMeToken;
  }

  public void setRememberMeToken(String rememberMeToken) {
    this.rememberMeToken = rememberMeToken;
  }

  public String getTimeZoneIdentifier() {
    return timeZoneIdentifier;
  }

  public void setTimeZoneIdentifier(String timeZoneIdentifier) {
    this.timeZoneIdentifier = timeZoneIdentifier;
  }
  
  public String getLanguageIdentifier() {
    return languageIdentifier;
  }

  public void setLanguageIdentifier(String languageIdentifier) {
    this.languageIdentifier = languageIdentifier;
  }

  public String getCountryIdentifier() {
    return countryIdentifier;
  }

  public void setCountryIdentifier(String countryIdentifier) {
    this.countryIdentifier = countryIdentifier;
  }  
}