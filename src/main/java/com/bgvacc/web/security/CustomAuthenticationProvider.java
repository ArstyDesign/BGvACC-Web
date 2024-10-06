package com.bgvacc.web.security;

import com.bgvacc.web.exceptions.GeneralErrorException;
import com.bgvacc.web.responses.authentication.AuthenticationResponse;
import com.bgvacc.web.responses.users.RoleResponse;
import com.bgvacc.web.responses.users.UserResponse;
import com.bgvacc.web.services.AuthenticationService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final AuthenticationService authenticationService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    log.debug("Authenticating");

    String cidEmail = authentication.getName();
    String password = authentication.getCredentials().toString();

    try {
      AuthenticationResponse authenticated = authenticationService.authenticate(cidEmail, password);

      List<GrantedAuthority> authorities = new ArrayList<>();

      if (authenticated == null) {
        return null;
      }
      for (RoleResponse role : authenticated.getUser().getRoles()) {
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
      }

      UserResponse user = authenticated.getUser();

      final LoggedUser loggedUser = new LoggedUser(user.getCid(), password,
              true, true, true, true,
              authorities, user.getEmail(), user.getEmailVatsim(),
              user.getFirstName(), user.getLastName(),
              user.getIsActive(), user.getLastLogin(),
              user.getCreatedOn(), user.getEditedOn());

      return new UsernamePasswordAuthenticationToken(loggedUser, password, authorities);
    } catch (GeneralErrorException ex) {
      log.error("Authentication exception", ex);
      throw ex;
    }

//    if (authenticated != null)
//    {
//    }
//    return null;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
