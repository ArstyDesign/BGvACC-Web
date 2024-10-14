package com.bgvacc.web.services;

import com.bgvacc.web.responses.authentication.AuthenticationResponse;
import com.bgvacc.web.responses.authentication.AuthenticationSuccessResponse;
import com.bgvacc.web.responses.users.RoleResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public interface AuthenticationService {

  AuthenticationSuccessResponse saveAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException;

  List<RoleResponse> getBaseRoles(Collection<GrantedAuthority> authorities);

}
