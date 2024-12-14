package com.bgvacc.web.services;

import com.bgvacc.web.models.authentication.LoginModel;
import com.bgvacc.web.responses.authentication.AuthAttemptResponse;
import com.bgvacc.web.responses.authentication.AuthenticationSuccessResponse;
import com.bgvacc.web.responses.users.RoleResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public interface AuthenticationService {

  AuthAttemptResponse authenticate(LoginModel login);

  AuthenticationSuccessResponse saveAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException;

  List<RoleResponse> getBaseRoles(Collection<GrantedAuthority> authorities);

  String forgotPassword(String email);

  boolean resetPassword(String newPassword, String passwordResetToken);
}
