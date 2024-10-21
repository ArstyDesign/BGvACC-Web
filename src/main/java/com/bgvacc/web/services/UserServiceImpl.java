package com.bgvacc.web.services;

import com.bgvacc.web.models.portal.users.UserCreateModel;
import com.bgvacc.web.responses.users.RoleResponse;
import com.bgvacc.web.responses.users.UserResponse;
import com.bgvacc.web.utils.Names;
import com.bgvacc.web.vatsim.utils.VatsimRatingUtils;
import java.sql.*;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
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

  private final PasswordEncoder passwordEncoder;

  @Override
  public List<UserResponse> getUsers() {

    final String usersSql = "SELECT * FROM users ORDER BY cid DESC";
    final String userRolesSql = "SELECT * FROM user_roles WHERE cid = ?";

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
  public boolean doUserExist(String cid) {

    final String doUserExistSql = "SELECT EXISTS (SELECT 1 FROM users WHERE cid = ?)";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement doUserExistPstmt = conn.prepareStatement(doUserExistSql)) {

      try {

        conn.setAutoCommit(false);

        doUserExistPstmt.setString(1, cid);

        ResultSet doUserExistRset = doUserExistPstmt.executeQuery();

        if (doUserExistRset.next()) {
          if (doUserExistRset.getBoolean(1)) {
            return true;
          }
        }

        return false;

      } catch (SQLException ex) {
        log.error("Error checking if user with CID '" + cid + "' exists", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error checking if user with CID '" + cid + "' exists", e);
    }

    return false;
  }

  @Override
  public boolean createUser(UserCreateModel ucm) {

    final String createUserSql = "INSERT INTO users (cid, email, email_vatsim, password, first_name, last_name) VALUES (?, ?, ?, ?, ?, ?)";
    final String addDefaultRoleSql = "INSERT INTO user_roles (cid, rolename) VALUES (?, ?)";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement createUserPstmt = conn.prepareStatement(createUserSql);
            PreparedStatement addDefaultRolePstmt = conn.prepareStatement(addDefaultRoleSql)) {

      try {

        conn.setAutoCommit(false);

        createUserPstmt.setString(1, ucm.getCid());
        createUserPstmt.setString(2, ucm.getEmail());
        createUserPstmt.setString(3, ucm.getEmailVatsim());

        String password = "Test-1234";

        createUserPstmt.setString(4, passwordEncoder.encode(password));
        createUserPstmt.setString(5, ucm.getFirstName());
        createUserPstmt.setString(6, ucm.getLastName());

        createUserPstmt.executeUpdate();

        addDefaultRolePstmt.setString(1, ucm.getCid());
        addDefaultRolePstmt.setString(2, "USER");

        addDefaultRolePstmt.executeUpdate();

        addDefaultRolePstmt.setString(1, ucm.getCid());
        addDefaultRolePstmt.setString(2, VatsimRatingUtils.getRatingNumberToUserRole(ucm.getCurrentRating()));

        addDefaultRolePstmt.executeUpdate();

        conn.commit();

        return true;

      } catch (SQLException ex) {
        log.error("Error creating user with CID - '" + ucm.getCid() + "'", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error creating user with CID - '" + ucm.getCid() + "'", e);
    }

    return false;
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

  @Override
  public List<RoleResponse> getAllUserRoles() {

    final String getUserRolesSql = "SELECT * FROM roles";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getUserRolesPstmt = conn.prepareStatement(getUserRolesSql);) {

      try {

        conn.setAutoCommit(false);

        List<RoleResponse> userRoles = new ArrayList<>();

        ResultSet getUserRolesRset = getUserRolesPstmt.executeQuery();

        while (getUserRolesRset.next()) {

          RoleResponse userRole = new RoleResponse();

          userRoles.add(userRole);
        }

        return userRoles;

//        return users;
      } catch (SQLException ex) {
        log.error("Error getting user roles", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting user roles", e);
    }

    return new ArrayList<>();
  }

  @Override
  public List<RoleResponse> getUserUnassignedRoles(String cid) {

    final String getUserRolesSql = "SELECT r.rolename FROM roles r LEFT JOIN user_roles ur ON r.rolename = ur.rolename AND ur.cid = ? WHERE ur.rolename IS NULL;";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getUserRolesPstmt = conn.prepareStatement(getUserRolesSql);) {

      try {

        conn.setAutoCommit(false);

        getUserRolesPstmt.setString(1, cid);

        List<RoleResponse> userRoles = new ArrayList<>();

        ResultSet getUserRolesRset = getUserRolesPstmt.executeQuery();

        while (getUserRolesRset.next()) {

          RoleResponse userRole = new RoleResponse();
          userRole.setRoleName(getUserRolesRset.getString("rolename"));

          userRoles.add(userRole);
        }

        return userRoles;

      } catch (SQLException ex) {
        log.error("Error getting user unassigned roles", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting user unassigned roles", e);
    }

    return new ArrayList<>();
  }

  @Override
  public boolean addUserRole(String cid, String role) {

    final String addUserRoleSql = "INSERT INTO user_roles (cid, rolename) VALUES (?, ?)";
    final String checkIfUserRoleForUserExistsSql = "SELECT EXISTS (SELECT 1 FROM user_roles WHERE cid = ? AND rolename = ?)";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement addUserRolePstmt = conn.prepareStatement(addUserRoleSql);
            PreparedStatement checkIfUserRoleForUserExistsPstmt = conn.prepareStatement(checkIfUserRoleForUserExistsSql)) {

      try {

        conn.setAutoCommit(false);

        checkIfUserRoleForUserExistsPstmt.setString(1, cid);
        checkIfUserRoleForUserExistsPstmt.setString(2, role);

        ResultSet checkIfUserRoleForUserExistsRset = checkIfUserRoleForUserExistsPstmt.executeQuery();

        log.info("Checking if role '" + role + "' exists for user with CID '" + cid + "'");

        if (checkIfUserRoleForUserExistsRset.next()) {
          if (checkIfUserRoleForUserExistsRset.getBoolean(1)) {
            log.info("The role '" + role + "' is already assigned to user with CID '" + cid + "'");
            return false;
          }
        }

        log.info("Adding role '" + role + "' for user with CID '" + cid + "'");

        addUserRolePstmt.setString(1, cid);
        addUserRolePstmt.setString(2, role);

        int rows = addUserRolePstmt.executeUpdate();

        return rows > 0;

      } catch (SQLException ex) {
        log.error("Error adding user role for user with CID '" + cid + "' and role '" + role + "'", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error adding user role for user with CID '" + cid + "' and role '" + role + "'", e);
    }

    return false;
  }

  @Override
  public boolean removeUserRole(String cid, String role) {

    final String removeUserRoleSql = "DELETE FROM user_roles WHERE cid = ? AND rolename = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement removeUserRolePstmt = conn.prepareStatement(removeUserRoleSql)) {

      try {

        conn.setAutoCommit(false);

        removeUserRolePstmt.setString(1, cid);
        removeUserRolePstmt.setString(2, role);

        int rows = removeUserRolePstmt.executeUpdate();

        return rows > 0;

      } catch (SQLException ex) {
        log.error("Error removing user role for user with CID '" + cid + "' and role '" + role + "'", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error removing user role for user with CID '" + cid + "' and role '" + role + "'", e);
    }

    return false;
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
