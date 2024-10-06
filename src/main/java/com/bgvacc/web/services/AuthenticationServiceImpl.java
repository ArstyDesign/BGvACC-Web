package com.bgvacc.web.services;

import com.bgvacc.web.responses.authentication.AuthenticationResponse;
import com.bgvacc.web.responses.authentication.AuthenticationSuccessResponse;
import com.bgvacc.web.responses.users.RoleResponse;
import com.bgvacc.web.responses.users.UserResponse;
import com.bgvacc.web.security.LoggedUser;
import com.bgvacc.web.utils.Names;
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

  private final JdbcTemplate jdbcTemplate;

  private final RequestCache requestCache = new HttpSessionRequestCache();

  private final SessionRegistry sessionRegistry;

  @Override
  public AuthenticationResponse authenticate(String cidEmail, String password) {

    final String userSql = "SELECT * FROM users WHERE (cid = ? OR email = ? OR email_vatsim = ?) AND password = ?";
    final String userRolesSql = "SELECT * from user_roles WHERE cid = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement userPstmt = conn.prepareStatement(userSql);
            PreparedStatement userRolesPstmt = conn.prepareStatement(userRolesSql)) {

      try {

        conn.setAutoCommit(false);

        userPstmt.setString(1, cidEmail);
        userPstmt.setString(2, cidEmail);
        userPstmt.setString(3, cidEmail);
        userPstmt.setString(4, password);

        ResultSet userRset = userPstmt.executeQuery();

        if (userRset.next()) {

          AuthenticationResponse response = new AuthenticationResponse();

          UserResponse user = new UserResponse();

          user.setCid(userRset.getString("cid"));
          user.setEmail(userRset.getString("email"));
          user.setEmailVatsim(userRset.getString("email_vatsim"));
          user.setFirstName(userRset.getString("first_name"));
          user.setLastName(userRset.getString("last_name"));
          user.setIsActive(userRset.getBoolean("is_active"));
          user.setLastLogin(userRset.getTimestamp("last_login"));
          user.setCreatedOn(userRset.getTimestamp("created_on"));
          user.setEditedOn(userRset.getTimestamp("edited_on"));

          ResultSet userRolesRset = userRolesPstmt.executeQuery();

          List<RoleResponse> roles = new ArrayList<>();

          while (userRolesRset.next()) {
            RoleResponse role = new RoleResponse(userRolesRset.getString("rolename"));
            roles.add(role);
          }

          user.setRoles(roles);
          response.setUser(user);

          return response;

        } else {
          log.info("No user found.");
        }

      } catch (SQLException ex) {
        log.error("Error authenticating user", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error authenticating user", e);
    }

    return null;
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

    Names names = Names.builder().firstName(loggedUser.getFirstName()).lastName(loggedUser.getLastName()).build();

    session.setAttribute("user", names);
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
}
