package com.bgvacc.web.services;

import com.bgvacc.web.responses.users.atc.PositionResponse;
import com.bgvacc.web.responses.users.atc.UserATCAuthorizedPositionResponse;
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
public class UserATCAuthorizedPositionsServiceImpl implements UserATCAuthorizedPositionsService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final JdbcTemplate jdbcTemplate;

  @Override
  public List<PositionResponse> getAllPositions() {

    final String getAllPositionsSql = "SELECT * FROM positions ORDER BY order_priority";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getAllPositionsPstmt = conn.prepareStatement(getAllPositionsSql)) {

      try {

        conn.setAutoCommit(false);

        List<PositionResponse> allPositions = new ArrayList<>();

        ResultSet getAllPositionsRset = getAllPositionsPstmt.executeQuery();

        while (getAllPositionsRset.next()) {

          PositionResponse p = getPosition(getAllPositionsRset);

          allPositions.add(p);
        }

        return allPositions;

      } catch (SQLException ex) {
        log.error("Error getting all positions.", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting all positions.", e);
    }

    return null;
  }

  @Override
  public List<PositionResponse> getUnauthorizedPositionsForUser(String userCid) {

    final String getUnauthorizedPositionsForUserSql = "SELECT p.* FROM positions p LEFT JOIN user_atc_authorized_positions uap ON p.position_id = uap.position_id AND uap.user_cid = ? WHERE uap.position_id IS NULL";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getUnauthorizedPositionsForUserPstmt = conn.prepareStatement(getUnauthorizedPositionsForUserSql)) {

      try {

        conn.setAutoCommit(false);

        getUnauthorizedPositionsForUserPstmt.setString(1, userCid);

        List<PositionResponse> allPositions = new ArrayList<>();

        ResultSet getUnauthorizedPositionsForUserRset = getUnauthorizedPositionsForUserPstmt.executeQuery();

        while (getUnauthorizedPositionsForUserRset.next()) {

          PositionResponse p = getPosition(getUnauthorizedPositionsForUserRset);

          allPositions.add(p);
        }

        return allPositions;

      } catch (SQLException ex) {
        log.error("Error getting all positions.", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting all positions.", e);
    }

    return null;
  }

  @Override
  public boolean addUserATCPosition(String cid, String position) {

    final String addUserATCPositionSql = "INSERT INTO user_atc_authorized_positions (user_cid, position_id) VALUES (?, ?)";
    final String checkIfPositionForUserExistsSql = "SELECT EXISTS (SELECT 1 FROM user_atc_authorized_positions WHERE user_cid = ? AND position_id = ?)";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement addUserATCPositionPstmt = conn.prepareStatement(addUserATCPositionSql);
            PreparedStatement checkIfPositionForUserExistsPstmt = conn.prepareStatement(checkIfPositionForUserExistsSql)) {

      try {

        conn.setAutoCommit(false);

        checkIfPositionForUserExistsPstmt.setString(1, cid);
        checkIfPositionForUserExistsPstmt.setString(2, position);

        ResultSet checkIfUserRoleForUserExistsRset = checkIfPositionForUserExistsPstmt.executeQuery();

        log.info("Checking if position '" + position + "' exists for user with CID '" + cid + "'");

        if (checkIfUserRoleForUserExistsRset.next()) {
          if (checkIfUserRoleForUserExistsRset.getBoolean(1)) {
            log.info("The position '" + position + "' is already authorized for user with CID '" + cid + "'");
            return false;
          }
        }

        log.info("Adding position '" + position + "' for user with CID '" + cid + "'");

        addUserATCPositionPstmt.setString(1, cid);
        addUserATCPositionPstmt.setString(2, position);

        int rows = addUserATCPositionPstmt.executeUpdate();

        conn.commit();

        return rows > 0;

      } catch (SQLException ex) {
        log.error("Error adding user ATC position for user with CID '" + cid + "' and position '" + position + "'", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error adding user ATC position for user with CID '" + cid + "' and position '" + position + "'", e);
    }

    return false;
  }

  @Override
  public boolean addAllUserATCPositions(String cid) {

    final String getPositionsSql = "SELECT * FROM positions ORDER BY order_priority";
    final String addUserATCPositionSql = "INSERT INTO user_atc_authorized_positions (user_cid, position_id) VALUES (?, ?)";
    final String checkIfPositionForUserExistsSql = "SELECT EXISTS (SELECT 1 FROM user_atc_authorized_positions WHERE user_cid = ? AND position_id = ?)";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getPositionsPstmt = conn.prepareStatement(getPositionsSql);
            PreparedStatement addUserATCPositionPstmt = conn.prepareStatement(addUserATCPositionSql);
            PreparedStatement checkIfPositionForUserExistsPstmt = conn.prepareStatement(checkIfPositionForUserExistsSql)) {

      try {

        conn.setAutoCommit(false);

        ResultSet getPositionsRset = getPositionsPstmt.executeQuery();

        while (getPositionsRset.next()) {

          String position = getPositionsRset.getString("position_id");

          checkIfPositionForUserExistsPstmt.setString(1, cid);
          checkIfPositionForUserExistsPstmt.setString(2, position);

          ResultSet checkIfUserRoleForUserExistsRset = checkIfPositionForUserExistsPstmt.executeQuery();

          log.info("Checking if position '" + position + "' exists for user with CID '" + cid + "'");

          if (checkIfUserRoleForUserExistsRset.next()) {
            if (checkIfUserRoleForUserExistsRset.getBoolean(1)) {
              log.info("The position '" + position + "' is already authorized for user with CID '" + cid + "'");
              continue;
            }
          }

          log.info("Adding position '" + position + "' for user with CID '" + cid + "'");

          addUserATCPositionPstmt.setString(1, cid);
          addUserATCPositionPstmt.setString(2, position);

          addUserATCPositionPstmt.executeUpdate();
        }

        conn.commit();

        return true;

      } catch (SQLException ex) {
        log.error("Error adding all user ATC position for user with CID '" + cid + "'", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error adding all user ATC position for user with CID '" + cid + "'", e);
    }

    return false;
  }

  @Override
  public boolean removeUserATCPosition(String cid, String position) {

    final String removeUserPositionSql = "DELETE FROM user_atc_authorized_positions WHERE user_cid = ? AND position_id = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement removeUserPositionPstmt = conn.prepareStatement(removeUserPositionSql)) {

      try {

        conn.setAutoCommit(false);

        removeUserPositionPstmt.setString(1, cid);
        removeUserPositionPstmt.setString(2, position);

        int rows = removeUserPositionPstmt.executeUpdate();

        conn.commit();

        return rows > 0;

      } catch (SQLException ex) {
        log.error("Error removing user ATC position for user with CID '" + cid + "' and position '" + position + "'", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error removing user ATC position for user with CID '" + cid + "' and position '" + position + "'", e);
    }

    return false;
  }

  @Override
  public boolean removeAllUserATCPositions(String cid) {

    final String removeUserPositionSql = "DELETE FROM user_atc_authorized_positions WHERE user_cid = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement removeUserPositionPstmt = conn.prepareStatement(removeUserPositionSql)) {

      try {

        conn.setAutoCommit(false);

        removeUserPositionPstmt.setString(1, cid);

        int rows = removeUserPositionPstmt.executeUpdate();

        conn.commit();

        return rows > 0;

      } catch (SQLException ex) {
        log.error("Error removing all user ATC position for user with CID '" + cid + "'", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error removing all user ATC position for user with CID '" + cid + "'", e);
    }

    return false;
  }

  @Override
  public List<UserATCAuthorizedPositionResponse> getAllATCCIDsWithAuthorizedPosition(String position) {

    final String getAllATCWithAuthorizedPositionSql = "SELECT * FROM user_atc_authorized_positions WHERE position_id = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getAllATCWithAuthorizedPositionPstmt = conn.prepareStatement(getAllATCWithAuthorizedPositionSql)) {

      try {

        conn.setAutoCommit(false);

        getAllATCWithAuthorizedPositionPstmt.setString(1, position);

        List<UserATCAuthorizedPositionResponse> getAllATCsWithAuthorizedPosition = new ArrayList<>();

        ResultSet getAllATCsWithAuthorizedPositionRset = getAllATCWithAuthorizedPositionPstmt.executeQuery();

        while (getAllATCsWithAuthorizedPositionRset.next()) {

          UserATCAuthorizedPositionResponse ap = getAuthorizedPosition(getAllATCsWithAuthorizedPositionRset);

          getAllATCsWithAuthorizedPosition.add(ap);
        }

        return getAllATCsWithAuthorizedPosition;

      } catch (SQLException ex) {
        log.error("Error getting all ATCs with authorized position for user with CID '" + position + "'.", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting all ATCs with authorized position for user with CID '" + position + "'.", e);
    }

    return null;
  }

  @Override
  public List<UserATCAuthorizedPositionResponse> getUserATCAuthorizedPositions(String userCid) {

    final String getAuthorizedPositionsSql = "SELECT uaap.*, p.order_priority FROM user_atc_authorized_positions uaap JOIN positions p ON uaap.position_id = p.position_id WHERE uaap.user_cid = ? ORDER BY order_priority";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getAuthorizedPositionsPstmt = conn.prepareStatement(getAuthorizedPositionsSql)) {

      try {

        conn.setAutoCommit(false);

        getAuthorizedPositionsPstmt.setString(1, userCid);

        List<UserATCAuthorizedPositionResponse> authorizedPositions = new ArrayList<>();

        ResultSet getAuthorizedPositionsRset = getAuthorizedPositionsPstmt.executeQuery();

        while (getAuthorizedPositionsRset.next()) {

          UserATCAuthorizedPositionResponse ap = getAuthorizedPosition(getAuthorizedPositionsRset);

          authorizedPositions.add(ap);
        }

        return authorizedPositions;

      } catch (SQLException ex) {
        log.error("Error getting user ATC authorized positions for user with CID '" + userCid + "'.", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting user ATC authorized positions for user with CID '" + userCid + "'.", e);
    }

    return null;
  }

  private PositionResponse getPosition(ResultSet rset) throws SQLException {

    PositionResponse p = new PositionResponse();
    p.setPosition(rset.getString("position_id"));
    p.setName(rset.getString("name"));
    p.setOrderPriority(rset.getInt("order_priority"));

    return p;
  }

  private UserATCAuthorizedPositionResponse getAuthorizedPosition(ResultSet rset) throws SQLException {

    UserATCAuthorizedPositionResponse ap = new UserATCAuthorizedPositionResponse();
    ap.setId(rset.getString("id"));
    ap.setUserCid(rset.getString("user_cid"));
    ap.setPosition(rset.getString("position_id"));
    ap.setIsPositionManuallyAdded(rset.getBoolean("is_position_manually_added"));
    ap.setExpiresOn(rset.getTimestamp("expires_on"));
    ap.setCreatedOn(rset.getTimestamp("created_at"));

    return ap;
  }
}
