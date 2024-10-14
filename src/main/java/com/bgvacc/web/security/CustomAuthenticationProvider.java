package com.bgvacc.web.security;

import com.bgvacc.web.exceptions.GeneralErrorException;
import com.bgvacc.web.responses.authentication.AuthenticationResponse;
import com.bgvacc.web.responses.users.RoleResponse;
import com.bgvacc.web.responses.users.UserResponse;
import com.bgvacc.web.services.UserService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
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

  private final UserService userService;

  private final PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    log.debug("Authenticating");

    String cidEmail = authentication.getName();
    String password = authentication.getCredentials().toString();

    try {
      UserResponse user = userService.getUser(cidEmail);

      List<GrantedAuthority> authorities = new ArrayList<>();

      if (user == null) {
        return null;
      }

      if (!passwordEncoder.matches(password, user.getPassword())) {
        throw new BadCredentialsException("Invalid password");
      }

      for (RoleResponse role : user.getRoles()) {
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
      }

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
