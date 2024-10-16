package com.bgvacc.web.services;

import com.bgvacc.web.responses.users.RoleResponse;
import com.bgvacc.web.responses.users.UserResponse;
import com.bgvacc.web.utils.Names;
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
  public List<UserResponse> getUsers() {

    final String usersSql = "SELECT * FROM users ORDER BY cid DESC";
    final String userRolesSql = "SELECT * from user_roles WHERE cid = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement usersPstmt = conn.prepareStatement(usersSql);
            PreparedStatement userRolesPstmt = conn.prepareStatement(userRolesSql)) {

      try {

        conn.setAutoCommit(false);

        List<UserResponse> users = new ArrayList<>();

        ResultSet usersRset = usersPstmt.executeQuery();

        while (usersRset.next()) {

          UserResponse user = getUserResponseFromResultSet(usersRset);

          userRolesPstmt.setString(1, user.getCid());

          ResultSet userRolesRset = userRolesPstmt.executeQuery();

          List<RoleResponse> roles = new ArrayList<>();

          while (userRolesRset.next()) {
            RoleResponse role = new RoleResponse(userRolesRset.getString("rolename"));
            roles.add(role);
          }

          user.setRoles(roles);

          users.add(user);
        }

        return users;

      } catch (SQLException ex) {
        log.error("Error getting users", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting users", e);
    }

    return new ArrayList<>();
  }

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

          UserResponse user = getUserResponseFromResultSet(userRset);

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

  @Override
  public void updateLastLogin(String cid) {

    final String updateUserLastLoginSql = "UPDATE users SET last_login = NOW() WHERE cid = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement updateUserLastLoginPstmt = conn.prepareStatement(updateUserLastLoginSql)) {

      try {

        conn.setAutoCommit(false);

        updateUserLastLoginPstmt.setString(1, cid);

        updateUserLastLoginPstmt.executeUpdate();

        conn.commit();

      } catch (SQLException ex) {
        log.error("Error updating user's last login (CID - '" + cid + "')", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error updating user's last login (CID - '" + cid + "')", e);
    }
  }

  private UserResponse getUserResponseFromResultSet(ResultSet rset) throws SQLException {

    UserResponse user = new UserResponse();
    user.setCid(rset.getString("cid"));
    user.setEmail(rset.getString("email"));
    user.setEmailVatsim(rset.getString("email_vatsim"));
    user.setPassword(rset.getString("password"));
    Names names = Names.builder().firstName(rset.getString("first_name")).lastName(rset.getString("last_name")).build();
    user.setNames(names);
    user.setIsActive(rset.getBoolean("is_active"));
    user.setLastLogin(rset.getTimestamp("last_login"));
    user.setCreatedOn(rset.getTimestamp("created_on"));
    user.setEditedOn(rset.getTimestamp("edited_on"));
    return user;
  }
}
