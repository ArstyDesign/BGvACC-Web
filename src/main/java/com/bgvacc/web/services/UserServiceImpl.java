package com.bgvacc.web.services;

import com.aarshinkov.random.Randomy;
import com.bgvacc.web.enums.UserRoles;
import com.bgvacc.web.models.portal.users.UserCreateModel;
import com.bgvacc.web.models.portal.users.UserSearchModel;
import com.bgvacc.web.responses.users.*;
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

  private final Randomy randomy;

  @Override
  public List<UserResponse> getUsers() {

    final String usersSql = "SELECT * FROM users ORDER BY created_on DESC";
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
  public List<UserResponse> searchUsers(UserSearchModel search) {

    String usersSql = "SELECT * FROM users";
    String whereSql = "WHERE ";
    String orderBySql = "ORDER BY created_on DESC";

    if (search != null && !search.areAllFieldsEmpty()) {

      boolean hasPrevious = false;

      if (search.getCid() != null && !search.getCid().trim().isEmpty()) {
        whereSql += "LOWER(cid) = LOWER(?)";
        hasPrevious = true;
      } else {
        search.setCid(null);
      }

      if (search.getName() != null && !search.getName().trim().isEmpty()) {
        whereSql += (hasPrevious ? " " : "") + "LOWER(first_name) = LOWER(?) OR LOWER(last_name) = LOWER(?) OR LOWER(CONCAT(first_name, ' ', last_name)) = LOWER(?)";
      } else {
        search.setName(null);
      }

      usersSql += " " + whereSql + " " + orderBySql;

    } else {
      usersSql += " " + orderBySql;
    }

    log.debug(usersSql);

    String userRolesSql = "SELECT * FROM user_roles WHERE cid = ?";

    if (search != null && search.getRole() != null && !search.getRole().trim().isEmpty()) {
      userRolesSql += " AND rolename = ?";
    }

    log.debug(userRolesSql);

    // TODO REWORK
    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement usersPstmt = conn.prepareStatement(usersSql);
            PreparedStatement userRolesPstmt = conn.prepareStatement(userRolesSql)) {

      try {

        conn.setAutoCommit(false);

        List<UserResponse> users = new ArrayList<>();

        if (search != null && !search.areAllFieldsEmpty()) {

          int i = 1;

          if (search.getCid() != null) {
            usersPstmt.setString(i++, search.getCid().trim());
          }

          if (search.getName() != null) {
            usersPstmt.setString(i++, search.getName().trim());
            usersPstmt.setString(i++, search.getName().trim());
            usersPstmt.setString(i++, search.getName().trim());
          }
        }

        ResultSet usersRset = usersPstmt.executeQuery();

        while (usersRset.next()) {

          UserResponse user = getUserResponseFromResultSet(usersRset);

          userRolesPstmt.setString(1, user.getCid());

          if (search != null && search.getRole() != null && !search.getRole().trim().isEmpty()) {
            userRolesPstmt.setString(2, search.getRole().trim());
          }

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
  public UserResponse getUserByPasswordResetToken(String passwordResetToken) {

    final String userSql = "SELECT * FROM users WHERE password_reset_token = ?";
    final String userRolesSql = "SELECT * from user_roles WHERE cid = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement userPstmt = conn.prepareStatement(userSql);
            PreparedStatement userRolesPstmt = conn.prepareStatement(userRolesSql)) {

      try {

        conn.setAutoCommit(false);

        userPstmt.setString(1, passwordResetToken);

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
  public boolean doUserExistByEmail(String email) {

    final String doUserExistByEmailSql = "SELECT EXISTS (SELECT 1 FROM users WHERE email = ?)";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement doUserExistByEmailPstmt = conn.prepareStatement(doUserExistByEmailSql)) {

      try {

        conn.setAutoCommit(false);

        doUserExistByEmailPstmt.setString(1, email);

        ResultSet doUserExistRset = doUserExistByEmailPstmt.executeQuery();

        if (doUserExistRset.next()) {
          if (doUserExistRset.getBoolean(1)) {
            return true;
          }
        }

        return false;

      } catch (SQLException ex) {
        log.error("Error checking if user with email '" + email + "' exists", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error checking if user with email '" + email + "' exists", e);
    }

    return false;
  }

  @Override
  public boolean doPasswordMatch(String cid, String password) {

    boolean doPasswordMatch = false;

    UserResponse user = getUser(cid);

    if (user != null) {
      return passwordEncoder.matches(password, user.getPassword());
    }

    return doPasswordMatch;
  }

  @Override
  public boolean isUserActive(String cid) {
    return getUser(cid).getIsActive();
  }

  @Override
  public String createUser(UserCreateModel ucm) {

    final String createUserSql = "INSERT INTO users (cid, email, email_vatsim, password, first_name, last_name, highest_controller_rating, password_reset_token) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    final String addDefaultRoleSql = "INSERT INTO user_roles (cid, rolename) VALUES (?, ?)";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement createUserPstmt = conn.prepareStatement(createUserSql);
            PreparedStatement addDefaultRolePstmt = conn.prepareStatement(addDefaultRoleSql)) {

      try {

        conn.setAutoCommit(false);

        createUserPstmt.setString(1, ucm.getCid());
        createUserPstmt.setString(2, ucm.getEmail());
        createUserPstmt.setString(3, ucm.getEmailVatsim());

        String generatedPassword = randomy.generateRandomString(6, true, true, true);

        log.debug("Generated password: " + generatedPassword);

        createUserPstmt.setString(4, passwordEncoder.encode(generatedPassword));
        createUserPstmt.setString(5, ucm.getFirstName());
        createUserPstmt.setString(6, ucm.getLastName());
        createUserPstmt.setInt(7, ucm.getCurrentRating());
        createUserPstmt.setString(8, UUID.randomUUID().toString());

        createUserPstmt.executeUpdate();

        addDefaultRolePstmt.setString(1, ucm.getCid());
        addDefaultRolePstmt.setString(2, "USER");

        addDefaultRolePstmt.executeUpdate();

        String ratingNumberToUserRole = VatsimRatingUtils.getRatingNumberToUserRole(ucm.getCurrentRating());

        if (ratingNumberToUserRole != null) {
          addDefaultRolePstmt.setString(1, ucm.getCid());
          addDefaultRolePstmt.setString(2, VatsimRatingUtils.getRatingNumberToUserRole(ucm.getCurrentRating()));

          addDefaultRolePstmt.executeUpdate();
        }

        conn.commit();

        return generatedPassword;

      } catch (SQLException ex) {
        log.error("Error creating user with CID - '" + ucm.getCid() + "'", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error creating user with CID - '" + ucm.getCid() + "'", e);
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
          userRole.setRoleName(getUserRolesRset.getString("rolename"));

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

    // TODO implement reversing user role to update highest_controller_rating column in USERS table
    final String addUserRoleSql = "INSERT INTO user_roles (cid, rolename) VALUES (?, ?)";
    final String checkIfUserRoleForUserExistsSql = "SELECT EXISTS (SELECT 1 FROM user_roles WHERE cid = ? AND rolename = ?)";
    final String checkForHigherControllerRatingSql = "SELECT highest_controller_rating FROM users WHERE cid = ?";
    final String updateHighestControllerRatingSql = "UPDATE users SET highest_controller_rating = ? WHERE cid = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement addUserRolePstmt = conn.prepareStatement(addUserRoleSql);
            PreparedStatement checkIfUserRoleForUserExistsPstmt = conn.prepareStatement(checkIfUserRoleForUserExistsSql);
            PreparedStatement checkForHigherControllerRatingPstmt = conn.prepareStatement(checkForHigherControllerRatingSql);
            PreparedStatement updateHighestControllerRatingPstmt = conn.prepareStatement(updateHighestControllerRatingSql)) {

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

        boolean userRolesInsertResult = addUserRolePstmt.executeUpdate() > 0;

        checkForHigherControllerRatingPstmt.setString(1, cid);

        ResultSet checkForHigherControllerRatingRset = checkForHigherControllerRatingPstmt.executeQuery();

        if (checkForHigherControllerRatingRset.next()) {

          int currentHighestControllerRating = checkForHigherControllerRatingRset.getInt("highest_controller_rating");

          Integer newHighestControllerRating = VatsimRatingUtils.getUserRoleToNumber(role);

          if (newHighestControllerRating != null) {
            if (currentHighestControllerRating < newHighestControllerRating) {

              updateHighestControllerRatingPstmt.setInt(1, newHighestControllerRating);
              updateHighestControllerRatingPstmt.setString(2, cid);

              boolean updateHighestControllerRatingResult = updateHighestControllerRatingPstmt.executeUpdate() > 0;

              if (userRolesInsertResult && updateHighestControllerRatingResult) {
                conn.commit();
                return true;
              } else {
                log.error("Error adding user role for user with CID '" + cid + "' and role '" + role + "'");
                conn.rollback();
                return false;
              }
            }
          }
          
//          conn.commit();
//          
//          return true;

        } else {
          log.error("Error adding user role for user with CID '" + cid + "' and role '" + role + "'");
          conn.rollback();
          return false;
        }

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

        conn.commit();

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

  @Override
  public Long getUsersCountByRole(UserRoles userRole) {

    final String getUsersCountByRoleSql = "SELECT COUNT(1) user_by_role FROM user_roles WHERE rolename = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getUsersCountByRolePstmt = conn.prepareStatement(getUsersCountByRoleSql)) {

      try {

        conn.setAutoCommit(false);

        getUsersCountByRolePstmt.setString(1, userRole.getRoleName());

        ResultSet getUsersCountByRoleRset = getUsersCountByRolePstmt.executeQuery();

        if (getUsersCountByRoleRset.next()) {

          return getUsersCountByRoleRset.getLong("user_by_role");
        }

      } catch (SQLException ex) {
        log.error("Error getting users count by role for role '" + userRole.getRoleName() + "'.", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting users count by role for role '" + userRole.getRoleName() + "'.", e);
    }

    return 0L;

  }

  @Override
  public Long getUsersCountByRoles(UserRoles... userRoles) {

    StringBuilder placeholders = new StringBuilder();
    for (int i = 0; i < userRoles.length; i++) {
      placeholders.append("?");
      if (i < userRoles.length - 1) {
        placeholders.append(", ");
      }
    }

    final String getUsersCountByRoleSql = "SELECT COUNT(1) user_by_role FROM user_roles WHERE rolename IN (" + placeholders + ")";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getUsersCountByRolePstmt = conn.prepareStatement(getUsersCountByRoleSql)) {

      try {

        conn.setAutoCommit(false);

        for (int i = 0; i < userRoles.length; i++) {
          getUsersCountByRolePstmt.setString(i + 1, userRoles[i].getRoleName());
        }

        ResultSet getUsersCountByRoleRset = getUsersCountByRolePstmt.executeQuery();

        if (getUsersCountByRoleRset.next()) {

          return getUsersCountByRoleRset.getLong("user_by_role");
        }

      } catch (SQLException ex) {
        log.error("Error getting users count by role for selected roles.", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting users count by role for selected roles.", e);
    }

    return 0L;
  }

  @Override
  public UsersCount getUsersCount() {

    final String getTotalUsersCountSql = "SELECT COUNT(1) total_users_count FROM users";
    final String getActiveUsersCountSql = "SELECT COUNT(1) active_users_count FROM users WHERE is_active = true";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getTotalUsersCountPstmt = conn.prepareStatement(getTotalUsersCountSql);
            PreparedStatement getActiveUsersCountPstmt = conn.prepareStatement(getActiveUsersCountSql)) {

      try {

        conn.setAutoCommit(false);

        ResultSet getTotalUsersCountRset = getTotalUsersCountPstmt.executeQuery();

        Long totalUsers = 0L;

        if (getTotalUsersCountRset.next()) {
          totalUsers = getTotalUsersCountRset.getLong("total_users_count");
        }

        Long activeUsers = 0L;

        ResultSet getActiveUsersCountRset = getActiveUsersCountPstmt.executeQuery();

        if (getActiveUsersCountRset.next()) {
          activeUsers = getActiveUsersCountRset.getLong("active_users_count");
        }

        return new UsersCount(totalUsers, activeUsers);

      } catch (SQLException ex) {
        log.error("Error getting users count.", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting users count.", e);
    }

    return new UsersCount(0L, 0L);
  }

  @Override
  public boolean changePassword(String cid, String password) {

    final String changePasswordSql = "UPDATE users SET password = ? WHERE cid = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement changePasswordPstmt = conn.prepareStatement(changePasswordSql)) {

      try {

        conn.setAutoCommit(false);

        changePasswordPstmt.setString(1, passwordEncoder.encode(password));
        changePasswordPstmt.setString(2, cid);

        int rows = changePasswordPstmt.executeUpdate();

        conn.commit();

        return rows > 0;

      } catch (SQLException ex) {
        log.error("Error changing password for user with CID '" + cid + "'.", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error changing password for user with CID '" + cid + "'.", e);
    }

    return false;
  }

  @Override
  public boolean activateUserAccount(String cid, String password) {

    final String activateUserAccountSql = "UPDATE users SET password = ?, is_active = true, password_reset_token = null, activated_on = NOW() WHERE cid = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement activateUserAccountPstmt = conn.prepareStatement(activateUserAccountSql)) {

      try {

        conn.setAutoCommit(false);

        activateUserAccountPstmt.setString(1, passwordEncoder.encode(password));
        activateUserAccountPstmt.setString(2, cid);

        int rows = activateUserAccountPstmt.executeUpdate();

        conn.commit();

        return rows > 0;

      } catch (SQLException ex) {
        log.error("Error activating user account for user with CID '" + cid + "'.", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error activating user account for user with CID '" + cid + "'.", e);
    }

    return false;
  }

  @Override
  public List<SavedSearchUser> getUserSavedUserSearches(String cid) {

    final String getUserSavedUserSearchesSql = "SELECT sus.id, sus.user_cid, u.email u_email, u.email_vatsim u_email_vatsim, u.first_name u_first_name, u.last_name u_last_name, sus.searched_user_cid, su.email su_email, su.email_vatsim su_email_vatsim, su.first_name su_first_name, su.last_name su_last_name, sus.added_at FROM saved_user_searches sus JOIN users u ON sus.user_cid = u.cid JOIN users su ON sus.searched_user_cid = su.cid WHERE sus.user_cid = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getUserSavedUserSearchesPstmt = conn.prepareStatement(getUserSavedUserSearchesSql)) {

      try {

//        conn.setAutoCommit(false);
        getUserSavedUserSearchesPstmt.setString(1, cid);

        List<SavedSearchUser> savedSearches = new ArrayList<>();

        ResultSet getUserSavedUserSearchesRset = getUserSavedUserSearchesPstmt.executeQuery();

        while (getUserSavedUserSearchesRset.next()) {

          SavedSearchUser ss = new SavedSearchUser();
          ss.setCid(getUserSavedUserSearchesRset.getString("searched_user_cid"));
          ss.setEmail(getUserSavedUserSearchesRset.getString("su_email"));
          ss.setEmailVatsim(getUserSavedUserSearchesRset.getString("su_email_vatsim"));

          Names names = Names.builder()
                  .firstName(getUserSavedUserSearchesRset.getString("su_first_name"))
                  .lastName(getUserSavedUserSearchesRset.getString("su_last_name"))
                  .build();
          ss.setNames(names);

          savedSearches.add(ss);
        }

        return savedSearches;

      } catch (SQLException ex) {
        log.error("Error get user saved searches for user with CID '" + cid + "'.", ex);
//        conn.rollback();
      } finally {
//        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error get user saved searches for user with CID '" + cid + "'.", e);
    }

    return null;
  }

  @Override
  public boolean addSavedUserSearch(String cid, String cidToAdd) {

    final String addSavedUserSearchSql = "INSERT INTO saved_user_searches (user_cid, searched_user_cid) VALUES (?, ?)";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement addSavedUserSearchPstmt = conn.prepareStatement(addSavedUserSearchSql)) {

      try {

        conn.setAutoCommit(false);

        addSavedUserSearchPstmt.setString(1, cid);
        addSavedUserSearchPstmt.setString(2, cidToAdd);

        int rows = addSavedUserSearchPstmt.executeUpdate();

        conn.commit();

        return rows > 0;

      } catch (SQLException ex) {
        log.error("Error adding user saved search for user with CID '" + cid + "'.", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error adding user saved search for user with CID '" + cid + "'.", e);
    }

    return false;
  }

  @Override
  public boolean removeUserFromSavedSearches(String cid, String cidToRemove) {

    final String removeUserFromSavedSearchSql = "DELETE FROM saved_user_searches WHERE user_cid = ? AND searched_user_cid = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement removeUserFromSavedSearchPstmt = conn.prepareStatement(removeUserFromSavedSearchSql)) {

      try {

        conn.setAutoCommit(false);

        removeUserFromSavedSearchPstmt.setString(1, cid);
        removeUserFromSavedSearchPstmt.setString(2, cidToRemove);

        int rows = removeUserFromSavedSearchPstmt.executeUpdate();

        conn.commit();

        return rows > 0;

      } catch (SQLException ex) {
        log.error("Error removing user from saved search for user with CID '" + cid + "'.", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error removing user from saved search for user with CID '" + cid + "'.", e);
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
    user.setHighestControllerRating(rset.getInt("highest_controller_rating"));
    user.setPasswordResetToken(rset.getString("password_reset_token"));
    return user;
  }
}
