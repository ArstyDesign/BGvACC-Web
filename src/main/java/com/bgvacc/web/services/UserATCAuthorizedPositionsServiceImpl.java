package com.bgvacc.web.services;

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

    final String getAuthorizedPositionsSql = "SELECT * FROM user_atc_authorized_positions WHERE user_cid = ?";

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
