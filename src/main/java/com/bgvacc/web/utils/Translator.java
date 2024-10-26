package com.bgvacc.web.utils;

import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Component
public class Translator {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private MessageSource messageSource;

  public String toLanguage(String message, String locale, String... params) {
    Locale loc = new Locale.Builder().setLanguage(locale).build();
    return messageSource.getMessage(message, params, loc);
  }
}
