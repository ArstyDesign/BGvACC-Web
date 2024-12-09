package com.bgvacc.web.base;

import com.bgvacc.web.security.LoggedUser;
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

  public MessageSource getMessageSource() {
    return messageSource;
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
    return getLoggedUser(request).getCid();
  }

  protected String getLoggedUserName(HttpServletRequest request) {
    return getLoggedUser(request).getNames().getFullName();
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
}
