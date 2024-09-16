package com.bgvacc.web.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
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
