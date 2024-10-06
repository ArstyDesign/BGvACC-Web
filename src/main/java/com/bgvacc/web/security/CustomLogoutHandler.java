package com.bgvacc.web.security;

import com.bgvacc.web.base.Base;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Component
public class CustomLogoutHandler extends Base implements LogoutHandler {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

//    LoggedUser loggedUser = getLoggedUser(request);
//
//    if (loggedUser != null) {
//      log.debug(loggedUser.getFullName() + " is logging out...");
//    } else {
//      log.debug("Forcing logout...");
//    }

    try {
      request.getSession(false).invalidate();

//      log.debug(loggedUser.getFullName() + " logged out successfully. Redirecting to login page.");
      response.sendRedirect(request.getContextPath() + "/login");

    } catch (IOException ex) {
      log.error("Error", ex);
    }
  }
}
