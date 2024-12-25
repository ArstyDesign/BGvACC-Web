package com.bgvacc.web.base;

import com.bgvacc.web.configurations.properties.EnvironmentProperties;
import com.bgvacc.web.security.LoggedUser;
import com.bgvacc.web.security.SecurityChecks;
import com.bgvacc.web.services.SystemService;
import com.bgvacc.web.utils.AppConstants;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Component
public class Base {

  @Autowired
  private MessageSource messageSource;

  @Autowired
  private SystemService systemService;

  @Autowired
  private SecurityChecks securityChecks;

  @Autowired
  private EnvironmentProperties environmentProperties;

  public MessageSource getMessageSource() {
    return messageSource;
  }

  protected boolean isLoggedIn() {
    return securityChecks.isLoggedIn();
  }

  protected LoggedUser getLoggedUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    try {
      return (LoggedUser) auth.getPrincipal();
    } catch (Exception e) {
      return null;
    }
  }

  protected String getLoggedUserCid(HttpServletRequest request) {

    LoggedUser loggedUser = getLoggedUser(request);

    if (loggedUser != null) {
      return loggedUser.getCid();
    }

    return null;
  }

  protected String getLoggedUserName(HttpServletRequest request) {

    LoggedUser loggedUser = getLoggedUser(request);

    if (loggedUser != null) {
      return loggedUser.getNames().getFullName();
    }

    return null;
  }

  protected LoggedUser getLoggedUser(HttpServletRequest request) {
    return (LoggedUser) systemService.getSessionAttribute(request, AppConstants.SESSION_LOGGED_USER);
  }

  protected String getMessage(String key) {
    try {
      return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    } catch (NoSuchMessageException e) {
      return "";
    }
  }

  protected String getMessage(String key, Object... params) {
    try {
      return messageSource.getMessage(key, params, LocaleContextHolder.getLocale());
    } catch (NoSuchMessageException e) {
      return "";
    }
  }

  protected String getEnvironment() {
    return environmentProperties.getEnvironment();
  }

  protected boolean isTestEnvironment() {

    String environment = getEnvironment();

    if (environment != null) {
      return environment.equalsIgnoreCase("test");
    }

    return false;
  }

  protected boolean isUATEnvironment() {

    String environment = getEnvironment();

    if (environment != null) {
      return environment.equalsIgnoreCase("uat");
    }

    return false;
  }

  protected boolean isProductionEnvironment() {

    String environment = getEnvironment();

    if (environment != null) {
      return environment.equalsIgnoreCase("prod");
    }

    return true;
  }

  protected String getTestEmailReceiver() {
    return environmentProperties.getTestEmailReceiver();
  }

  protected String getBaseUrl() {
    return environmentProperties.getBaseUrl();
  }
}
