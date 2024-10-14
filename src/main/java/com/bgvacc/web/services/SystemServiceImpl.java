package com.bgvacc.web.services;

import com.bgvacc.web.utils.AppConstants;
import javax.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Service
public class SystemServiceImpl implements SystemService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Override
  public String getLocale(HttpServletRequest request) {
    String locale = AppConstants.LANG_DEFAULT;

    for (int i = 0; request.getCookies() != null && i < request.getCookies().length; i++) {
      Cookie c = request.getCookies()[i];

      if ((AppConstants.LANG_COOKIE_NAME).equals(c.getName())) {
        locale = c.getValue();
      }
    }

    return locale;
  }

  @Override
  public Object getSessionAttribute(HttpServletRequest request, String attributeName) {
    HttpSession session = request.getSession();
    return session.getAttribute(attributeName);
  }
}
