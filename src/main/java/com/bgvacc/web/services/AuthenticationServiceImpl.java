package com.bgvacc.web.services;

import com.bgvacc.web.enums.AuthenticationError;
import com.bgvacc.web.models.authentication.LoginModel;
import com.bgvacc.web.responses.authentication.AuthAttemptResponse;
import com.bgvacc.web.responses.authentication.AuthenticationSuccessResponse;
import com.bgvacc.web.responses.users.RoleResponse;
import com.bgvacc.web.responses.users.UserResponse;
import com.bgvacc.web.security.LoggedUser;
import com.bgvacc.web.utils.Utils;
import eu.bitwalker.useragentutils.UserAgent;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
import org.springframework.security.web.savedrequest.*;
import org.springframework.stereotype.Service;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final RequestCache requestCache = new HttpSessionRequestCache();

  private final SessionRegistry sessionRegistry;

  private final UserService userService;

  private final JdbcTemplate jdbcTemplate;

  private final PasswordEncoder passwordEncoder;

  @Override
  public AuthAttemptResponse authenticate(LoginModel login) {

    UserResponse user = userService.getUser(login.getCidEmail());

    AuthAttemptResponse response = new AuthAttemptResponse();

    response.setUser(null);

    if (user == null) {
      response.setError(AuthenticationError.USER_NOT_FOUND);

      return response;
    }

    if (!passwordEncoder.matches(login.getPassword(), user.getPassword())) {

      response.setError(AuthenticationError.BAD_CREDENTIALS);

      return response;
    }

    response.setUser(user);
    response.setError(null);

    return response;
  }

  @Override
  public AuthenticationSuccessResponse saveAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

    log.debug("Authentication successful.");

    HttpSession session = request.getSession(true);

    sessionRegistry.registerNewSession(session.getId(), authentication.getPrincipal());

    SecurityContext sc = SecurityContextHolder.getContext();
    sc.setAuthentication(authentication);
    session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);

    final Timestamp NOW = new Timestamp(System.currentTimeMillis());

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    LoggedUser loggedUser = (LoggedUser) auth.getPrincipal();
    loggedUser.setLoggedOn(NOW);

//    session.setMaxInactiveInterval(loggedUser.getSessionTimeout());
//    loggedUser.setUserAgent(request.getHeader("User-Agent"));
    UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
    loggedUser.setUserAgent(userAgent);
    loggedUser.setIpAddress(Utils.getIpAddress(request));

    if (loggedUser.getLastLogin() == null) {
      log.debug("User with CID: " + loggedUser.getCid() + ", e: " + loggedUser.getEmail() + "logginng in for the first time");
      session.setAttribute("isFirstLogin", true);
    }

    log.info("Updating last login for user with CID: '" + loggedUser.getCid());
    userService.updateLastLogin(loggedUser.getCid());

    session.setAttribute("user", loggedUser.getNames());
    session.setAttribute("loggedUser", loggedUser);
//    session.setAttribute(SESSION_AUTHORIZATION, loggedUser.getToken());
    session.setAttribute("authRoles", loggedUser.getAuthorities());

    session.setAttribute("roles", getBaseRoles(loggedUser.getAuthorities()));

    SavedRequest savedRequest = requestCache.getRequest(request, response);

    AuthenticationSuccessResponse authResponse = new AuthenticationSuccessResponse();
    authResponse.setRedirectUrl("/");

    if (savedRequest == null) {
      log.debug("Redirecting to url: " + authResponse.getRedirectUrl());
      return authResponse;
    } else {
      // Use the DefaultSavedRequest URL
      if (savedRequest.getRedirectUrl() != null) {
        if (!savedRequest.getRedirectUrl().trim().isEmpty()) {
          authResponse.setRedirectUrl(savedRequest.getRedirectUrl());
        }
      }
    }

    return authResponse;
  }

  @Override
  public List<RoleResponse> getBaseRoles(Collection<GrantedAuthority> authorities) {

    List<RoleResponse> roles = new ArrayList<>();

    for (GrantedAuthority authority : authorities) {
      RoleResponse role = new RoleResponse();
      role.setRoleName(authority.getAuthority().replace("ROLE_", ""));
      roles.add(role);
    }

    return roles;
  }

  @Override
  public String forgotPassword(String email) {

    final String forgotPasswordSql = "UPDATE users SET password_reset_token = ? WHERE email = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement forgotPasswordPstmt = conn.prepareStatement(forgotPasswordSql)) {

      try {

        conn.setAutoCommit(false);

        String passwordResetToken = UUID.randomUUID().toString();

        forgotPasswordPstmt.setString(1, passwordResetToken);
        forgotPasswordPstmt.setString(2, email);

        int rows = forgotPasswordPstmt.executeUpdate();

        conn.commit();

        return rows > 0 ? passwordResetToken : null;

      } catch (SQLException ex) {
        log.error("Error marking password as forgotten for user with email '" + email + "'.", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error marking password as forgotten for user with email '" + email + "'.", e);
    }

    return null;
  }

  @Override
  public boolean resetPassword(String newPassword, String passwordResetToken) {

    final String resetPasswordSql = "UPDATE users SET password = ?, password_reset_token = null WHERE password_reset_token = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement resetPasswordPstmt = conn.prepareStatement(resetPasswordSql)) {

      try {

        conn.setAutoCommit(false);

        resetPasswordPstmt.setString(1, passwordEncoder.encode(newPassword));
        resetPasswordPstmt.setString(2, passwordResetToken);

        int rows = resetPasswordPstmt.executeUpdate();

        conn.commit();

        return rows > 0;

      } catch (SQLException ex) {
        log.error("Error resetting password for user with password reset token '" + passwordResetToken + "'.", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error resetting password for user with password reset token '" + passwordResetToken + "'.", e);
    }

    return false;
  }
}
