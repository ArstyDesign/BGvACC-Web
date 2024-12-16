package com.bgvacc.web.security;

import com.bgvacc.web.services.UserATCAuthorizedPositionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Component
public class SecurityChecks {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private UserATCAuthorizedPositionsService userATCAuthorizedPositionsService;

  public Boolean hasRole(String role) {
    role = role.toUpperCase();

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null) {
      return false;
    }

    for (GrantedAuthority authority : authentication.getAuthorities()) {
      if (authority.getAuthority().equals("ROLE_" + role)) {
        return true;
      }
    }
    return false;
  }

  public Boolean isLoggedIn() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null) {
      return false;
    }

    for (GrantedAuthority authority : authentication.getAuthorities()) {
      if (authority.getAuthority().equals("ROLE_ANONYMOUS")) {
        return false;
      }
    }

    return true;
  }

  public boolean hasUserAuthorizedPositions(String cid) {
    return userATCAuthorizedPositionsService.hasUserAuthorizedPositions(cid);
  }
}
