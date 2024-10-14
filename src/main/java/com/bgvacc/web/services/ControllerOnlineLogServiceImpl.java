package com.bgvacc.web.services;

import com.bgvacc.web.responses.sessions.NotCompletedControllerSession;
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
public class ControllerOnlineLogServiceImpl implements ControllerOnlineLogService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final JdbcTemplate jdbcTemplate;

  @Override
  public List<NotCompletedControllerSession> getNotCompletedControllerSessions() {

    final String getNotCompletedControllerSessionsSql = "SELECT controller_online_log_id, cid, position FROM controllers_online_log WHERE session_ended IS NULL";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getNotCompletedControllerSessionsPstmt = conn.prepareStatement(getNotCompletedControllerSessionsSql)) {

      try {

        conn.setAutoCommit(false);

        ResultSet getNotCompletedControllerSessionsRset = getNotCompletedControllerSessionsPstmt.executeQuery();

        List<NotCompletedControllerSession> notCompletedControllerSessions = new ArrayList<>();

        while (getNotCompletedControllerSessionsRset.next()) {

          NotCompletedControllerSession nccs = new NotCompletedControllerSession();
          nccs.setControllerOnlineId(getNotCompletedControllerSessionsRset.getString("controller_online_log_id"));
          nccs.setCid(getNotCompletedControllerSessionsRset.getString("cid"));
          nccs.setPosition(getNotCompletedControllerSessionsRset.getString("position"));
          notCompletedControllerSessions.add(nccs);
        }

        return notCompletedControllerSessions;

      } catch (SQLException ex) {
        log.error("Error getting not completed controller sessions", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting not completed controller sessions", e);
    }

    return new ArrayList<>();
  }

  @Override
  public boolean endControllerSessionWithId(String controllerOnlineLogId) {

    final String endControllerSessionWithIdSql = "UPDATE controllers_online_log SET session_ended = NOW() WHERE controller_online_log_id = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement endControllerSessionWithIdPstmt = conn.prepareStatement(endControllerSessionWithIdSql)) {

      try {

        conn.setAutoCommit(false);

        endControllerSessionWithIdPstmt.setString(1, controllerOnlineLogId);

        boolean result = endControllerSessionWithIdPstmt.executeUpdate() > 0;

        conn.commit();

        return result;

      } catch (SQLException ex) {
        log.error("Error ending controller session with ID: '" + controllerOnlineLogId + "'.", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error ending controller session with ID: '" + controllerOnlineLogId + "'.", e);
    }

    return false;
  }
}
