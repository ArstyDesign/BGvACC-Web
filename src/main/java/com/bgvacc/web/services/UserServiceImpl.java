package com.bgvacc.web.services;

import com.bgvacc.web.responses.authentication.AuthenticationResponse;
import com.bgvacc.web.responses.users.RoleResponse;
import com.bgvacc.web.responses.users.UserResponse;
import java.sql.*;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final JdbcTemplate jdbcTemplate;

  @Override
  public UserResponse getUser(String cidEmail) {

    final String userSql = "SELECT * FROM users WHERE cid = ? OR email = ? OR email_vatsim = ?";
    final String userRolesSql = "SELECT * from user_roles WHERE cid = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement userPstmt = conn.prepareStatement(userSql);
            PreparedStatement userRolesPstmt = conn.prepareStatement(userRolesSql)) {

      try {

        conn.setAutoCommit(false);

        userPstmt.setString(1, cidEmail);
        userPstmt.setString(2, cidEmail);
        userPstmt.setString(3, cidEmail);

        ResultSet userRset = userPstmt.executeQuery();

        if (userRset.next()) {

          UserResponse user = new UserResponse();

          user.setCid(userRset.getString("cid"));
          user.setEmail(userRset.getString("email"));
          user.setEmailVatsim(userRset.getString("email_vatsim"));
          user.setPassword(userRset.getString("password"));
          user.setFirstName(userRset.getString("first_name"));
          user.setLastName(userRset.getString("last_name"));
          user.setIsActive(userRset.getBoolean("is_active"));
          user.setLastLogin(userRset.getTimestamp("last_login"));
          user.setCreatedOn(userRset.getTimestamp("created_on"));
          user.setEditedOn(userRset.getTimestamp("edited_on"));
          
          userRolesPstmt.setString(1, user.getCid());
          
          ResultSet userRolesRset = userRolesPstmt.executeQuery();

          List<RoleResponse> roles = new ArrayList<>();

          while (userRolesRset.next()) {
            RoleResponse role = new RoleResponse(userRolesRset.getString("rolename"));
            roles.add(role);
          }

          user.setRoles(roles);

          return user;

        } else {
          log.info("No user found.");
        }

      } catch (SQLException ex) {
        log.error("Error getting user", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting user", e);
    }

    return null;
  }
}
