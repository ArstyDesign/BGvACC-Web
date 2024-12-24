package com.bgvacc.web.services;

import com.aarshinkov.datetimecalculator.utils.TimeUtils;
import com.bgvacc.web.api.vateud.VatEudCoreApi;
import com.bgvacc.web.responses.paging.PaginationResponse;
import com.bgvacc.web.responses.sessions.*;
import static com.bgvacc.web.utils.ControllerPositionUtils.getPositionFrequency;
import com.bgvacc.web.utils.Names;
import com.bgvacc.web.vatsim.atc.VatsimATC;
import com.bgvacc.web.vatsim.atc.VatsimATCInfo;
import com.bgvacc.web.vatsim.vateud.VatEudUser;
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

  private final VatEudCoreApi vatEudCoreApi;

  @Override
  public PaginationResponse<ControllerOnlineLogResponse> getControllerSessions(int page, int limit, String cid) {

    if (page < 1) {
      page = 1;
    }

    if (limit < 1 && limit != -1) {
      limit = -1;
    }

    String getControllerSessionsSql = "SELECT * FROM controllers_online_log WHERE cid = ? AND session_ended IS NOT NULL ORDER BY session_started DESC" + (limit != -1 ? " LIMIT ? OFFSET ?" : "");
    String getControllerSessionsTotalSql = "SELECT COUNT(1) FROM controllers_online_log WHERE cid = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getControllerSessionsTotalPstmt = conn.prepareStatement(getControllerSessionsTotalSql);
            PreparedStatement getControllerSessionsPstmt = conn.prepareStatement(getControllerSessionsSql)) {

      try {

        conn.setAutoCommit(false);

        getControllerSessionsTotalPstmt.setString(1, cid);

        ResultSet getControllerSessionsTotalRset = getControllerSessionsTotalPstmt.executeQuery();

        int totalItems = 0;

        if (getControllerSessionsTotalRset.next()) {
          totalItems = getControllerSessionsTotalRset.getInt(1);
        }

        int totalPages = (int) Math.ceil((double) totalItems / (limit == -1 ? totalItems : limit));

        if (page > totalPages) {
          throw new IllegalArgumentException("Current page exceeds total pages");
        }

        int offset = (page - 1) * limit;

        getControllerSessionsPstmt.setString(1, cid);

        if (limit > 0) {
          getControllerSessionsPstmt.setInt(2, limit);
          getControllerSessionsPstmt.setInt(3, offset);
        }

        ResultSet getNotCompletedControllerSessionsRset = getControllerSessionsPstmt.executeQuery();

        List<ControllerOnlineLogResponse> controllerLastOnlineSessions = new ArrayList<>();

        while (getNotCompletedControllerSessionsRset.next()) {

          ControllerOnlineLogResponse colr = new ControllerOnlineLogResponse();
          colr.setControllerOnlineId(getNotCompletedControllerSessionsRset.getString("controller_online_log_id"));
          colr.setCid(getNotCompletedControllerSessionsRset.getString("cid"));
          colr.setRating(getNotCompletedControllerSessionsRset.getInt("rating"));
          colr.setServer(getNotCompletedControllerSessionsRset.getString("server"));
          colr.setPosition(getNotCompletedControllerSessionsRset.getString("position"));
          colr.setSessionStarted(getNotCompletedControllerSessionsRset.getTimestamp("session_started"));
          colr.setSessionEnded(getNotCompletedControllerSessionsRset.getTimestamp("session_ended"));

          controllerLastOnlineSessions.add(colr);
        }

        return new PaginationResponse<>(controllerLastOnlineSessions, page, totalPages);

      } catch (SQLException ex) {
        log.error("Error getting controller sessions", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting controller sessions", e);
    }

    return null;
  }

  @Override
  public List<ControllerOnlineLogResponse> getControllerLastOnlineSessions(String cid, int numberOfConnections, boolean shouldIncludeNonCompleted) {

    String getControllerLastOnlineSessionsSql = "SELECT * FROM controllers_online_log WHERE cid = ?";

    if (!shouldIncludeNonCompleted) {
      getControllerLastOnlineSessionsSql += " AND session_ended IS NOT NULL";
    }

    getControllerLastOnlineSessionsSql += " ORDER BY session_started DESC";

    if (numberOfConnections > 0) {
      getControllerLastOnlineSessionsSql += " LIMIT " + numberOfConnections;
    }

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getControllerLastOnlineSessionsPstmt = conn.prepareStatement(getControllerLastOnlineSessionsSql)) {

      try {

        conn.setAutoCommit(false);

        getControllerLastOnlineSessionsPstmt.setString(1, cid);

        ResultSet getNotCompletedControllerSessionsRset = getControllerLastOnlineSessionsPstmt.executeQuery();

        List<ControllerOnlineLogResponse> controllerLastOnlineSessions = new ArrayList<>();

        while (getNotCompletedControllerSessionsRset.next()) {

          ControllerOnlineLogResponse colr = new ControllerOnlineLogResponse();
          colr.setControllerOnlineId(getNotCompletedControllerSessionsRset.getString("controller_online_log_id"));
          colr.setCid(getNotCompletedControllerSessionsRset.getString("cid"));
          colr.setRating(getNotCompletedControllerSessionsRset.getInt("rating"));
          colr.setServer(getNotCompletedControllerSessionsRset.getString("server"));
          colr.setPosition(getNotCompletedControllerSessionsRset.getString("position"));
          colr.setSessionStarted(getNotCompletedControllerSessionsRset.getTimestamp("session_started"));
          colr.setSessionEnded(getNotCompletedControllerSessionsRset.getTimestamp("session_ended"));

          controllerLastOnlineSessions.add(colr);
        }

        return controllerLastOnlineSessions;

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
  public List<ControllerOnlineLogResponse> getControllerOnlineSessions(String cid, int numberOfConnections) {

    String getControllerLastOnlineSessionsSql = "SELECT * FROM controllers_online_log WHERE cid = ? AND session_ended IS NULL ORDER BY session_started DESC";

    if (numberOfConnections > 0) {
      getControllerLastOnlineSessionsSql += " LIMIT " + numberOfConnections;
    }

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getControllerLastOnlineSessionsPstmt = conn.prepareStatement(getControllerLastOnlineSessionsSql)) {

      try {

        conn.setAutoCommit(false);

        getControllerLastOnlineSessionsPstmt.setString(1, cid);

        ResultSet getNotCompletedControllerSessionsRset = getControllerLastOnlineSessionsPstmt.executeQuery();

        List<ControllerOnlineLogResponse> controllerLastOnlineSessions = new ArrayList<>();

        while (getNotCompletedControllerSessionsRset.next()) {

          ControllerOnlineLogResponse colr = new ControllerOnlineLogResponse();
          colr.setControllerOnlineId(getNotCompletedControllerSessionsRset.getString("controller_online_log_id"));
          colr.setCid(getNotCompletedControllerSessionsRset.getString("cid"));
          colr.setRating(getNotCompletedControllerSessionsRset.getInt("rating"));
          colr.setServer(getNotCompletedControllerSessionsRset.getString("server"));
          colr.setPosition(getNotCompletedControllerSessionsRset.getString("position"));
          colr.setSessionStarted(getNotCompletedControllerSessionsRset.getTimestamp("session_started"));
          colr.setSessionEnded(getNotCompletedControllerSessionsRset.getTimestamp("session_ended"));

          controllerLastOnlineSessions.add(colr);
        }

        return controllerLastOnlineSessions;

      } catch (SQLException ex) {
        log.error("Error getting controller online sessions", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting controller online sessions", e);
    }

    return new ArrayList<>();
  }

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
  public List<ControllerOnlineLogResponse> getAllControllerSessions(String cid, int numberOfConnections) {

    String getControllerSessionsSql = "SELECT * FROM controllers_online_log WHERE cid = ? AND session_ended IS NOT NULL ORDER BY session_started DESC";

    if (numberOfConnections > 0) {
      getControllerSessionsSql += " LIMIT " + numberOfConnections;
    }

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getControllerLastOnlineSessionsPstmt = conn.prepareStatement(getControllerSessionsSql)) {

      try {

        conn.setAutoCommit(false);

        getControllerLastOnlineSessionsPstmt.setString(1, cid);

        ResultSet getNotCompletedControllerSessionsRset = getControllerLastOnlineSessionsPstmt.executeQuery();

        List<ControllerOnlineLogResponse> controllerLastOnlineSessions = new ArrayList<>();

        while (getNotCompletedControllerSessionsRset.next()) {

          ControllerOnlineLogResponse colr = new ControllerOnlineLogResponse();
          colr.setControllerOnlineId(getNotCompletedControllerSessionsRset.getString("controller_online_log_id"));
          colr.setCid(getNotCompletedControllerSessionsRset.getString("cid"));
          colr.setRating(getNotCompletedControllerSessionsRset.getInt("rating"));
          colr.setServer(getNotCompletedControllerSessionsRset.getString("server"));
          colr.setPosition(getNotCompletedControllerSessionsRset.getString("position"));
          colr.setSessionStarted(getNotCompletedControllerSessionsRset.getTimestamp("session_started"));
          colr.setSessionEnded(getNotCompletedControllerSessionsRset.getTimestamp("session_ended"));

          controllerLastOnlineSessions.add(colr);
        }

        return controllerLastOnlineSessions;

      } catch (SQLException ex) {
        log.error("Error getting controller sessions", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting controller sessions", e);
    }

    return new ArrayList<>();
  }

  @Override
  public NewlyOpenedControllerSession openNewControllerSession(VatsimATC onlineAtc) {

    final String openNewControllerSessionSql = "INSERT INTO controllers_online_log (cid, rating, server, position, session_started) VALUES (?, ?, ?, ?, ?)";
    final String checkIfSessionIsActiveSql = "SELECT EXISTS (SELECT 1 FROM controllers_online_log WHERE session_ended IS NULL AND cid = ? AND position = ?)";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement openNewControllerSessionPstmt = conn.prepareStatement(openNewControllerSessionSql);
            PreparedStatement checkIfSessionIsActivePstmt = conn.prepareStatement(checkIfSessionIsActiveSql)) {

      try {

        conn.setAutoCommit(false);

        checkIfSessionIsActivePstmt.setString(1, String.valueOf(onlineAtc.getId()));
        checkIfSessionIsActivePstmt.setString(2, onlineAtc.getCallsign());

        ResultSet checkIfSessionIsActiveRset = checkIfSessionIsActivePstmt.executeQuery();

        if (checkIfSessionIsActiveRset.next()) {
          if (!checkIfSessionIsActiveRset.getBoolean(1)) {
            log.info("Session is not active. Opening a new session.");

            openNewControllerSessionPstmt.setString(1, String.valueOf(onlineAtc.getId()));
            openNewControllerSessionPstmt.setInt(2, onlineAtc.getRating());
            openNewControllerSessionPstmt.setString(3, onlineAtc.getServer());
            openNewControllerSessionPstmt.setString(4, onlineAtc.getCallsign());

            Timestamp loggedAt = TimeUtils.getNowTimeUTC();

            openNewControllerSessionPstmt.setTimestamp(5, loggedAt);
//            openNewControllerSessionPstmt.setLong(6, onlineAtc.getId());

            boolean result = openNewControllerSessionPstmt.executeUpdate() > 0;

            if (result) {

              conn.commit();

              NewlyOpenedControllerSession nocs = new NewlyOpenedControllerSession();

              nocs.setCid(onlineAtc.getId());

              VatsimATCInfo positionInfo = getPositionFrequency(onlineAtc.getCallsign());

              nocs.setPositionCallsign(positionInfo.getCallsign());
              nocs.setPositionName(positionInfo.getName());
              nocs.setFrequency(positionInfo.getFrequency());
              nocs.setLoggedAt(loggedAt);

              return nocs;
            } else {
              conn.rollback();
            }
          }
        }

        return null;

      } catch (SQLException ex) {
        log.error("Error opening new controller session.", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error opening new controller session.", e);
    }

    return null;
  }

  @Override
  public ClosedControllerSession endControllerSessionWithId(String controllerOnlineLogId, String callsign) {

    final String getControllerSessionWithIdSql = "SELECT * FROM controllers_online_log WHERE controller_online_log_id = ?";
    final String endControllerSessionWithIdSql = "UPDATE controllers_online_log SET session_ended = ? WHERE controller_online_log_id = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getControllerSessionWithIdPstmt = conn.prepareStatement(getControllerSessionWithIdSql);
            PreparedStatement endControllerSessionWithIdPstmt = conn.prepareStatement(endControllerSessionWithIdSql)) {

      try {

        conn.setAutoCommit(false);

        getControllerSessionWithIdPstmt.setString(1, controllerOnlineLogId);

        ResultSet getControllerSessionWithIdRset = getControllerSessionWithIdPstmt.executeQuery();

        if (getControllerSessionWithIdRset.next()) {

          Timestamp staredAt = getControllerSessionWithIdRset.getTimestamp("session_started");
          Timestamp loggedOffAt = TimeUtils.getNowTimeUTC();

          long differenceSeconds = (loggedOffAt.getTime() - staredAt.getTime()) / 1000;

          com.aarshinkov.datetimecalculator.domain.Time activeTime = new com.aarshinkov.datetimecalculator.domain.Time(differenceSeconds);

          endControllerSessionWithIdPstmt.setTimestamp(1, loggedOffAt);
          endControllerSessionWithIdPstmt.setString(2, controllerOnlineLogId);

          boolean result = endControllerSessionWithIdPstmt.executeUpdate() > 0;

          if (result) {

            conn.commit();

            ClosedControllerSession ccs = new ClosedControllerSession();

            VatsimATCInfo positionInfo = getPositionFrequency(callsign);

            ccs.setPositionCallsign(positionInfo.getCallsign());
            ccs.setPositionName(positionInfo.getName());
            ccs.setFrequency(positionInfo.getFrequency());
            ccs.setActiveTime(activeTime);
            ccs.setLoggedOffAt(loggedOffAt);

            return ccs;
          } else {
            conn.rollback();
          }
        }

      } catch (SQLException ex) {
        log.error("Error ending controller session with ID: '" + controllerOnlineLogId + "'.", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error ending controller session with ID: '" + controllerOnlineLogId + "'.", e);
    }

    return null;
  }

  @Override
  public ControllersOnlineReportResponse getControllersOnlinePastWeekReport() {

    final String refreshPastWeekReportSql = "REFRESH MATERIALIZED VIEW weekly_controller_report";
    final String getControllersOnlinePastWeekReportSql = "SELECT wcr.cid, u.first_name, u.last_name, wcr.position, wcr.time_in_seconds FROM weekly_controller_report wcr LEFT JOIN users u ON wcr.cid = u.cid";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement refreshPastWeekReportPstmt = conn.prepareStatement(refreshPastWeekReportSql);
            PreparedStatement getControllersOnlinePastWeekReportPstmt = conn.prepareStatement(getControllersOnlinePastWeekReportSql)) {

      try {

        conn.setAutoCommit(false);

        boolean isResult = refreshPastWeekReportPstmt.execute();

        if (!isResult) {

          ResultSet getControllersOnlinePastWeekReportRset = getControllersOnlinePastWeekReportPstmt.executeQuery();

          List<ControllersOnlineReportItemResponse> towerPositions = new ArrayList<>();
          List<ControllersOnlineReportItemResponse> approachPositions = new ArrayList<>();
          List<ControllersOnlineReportItemResponse> controlPositions = new ArrayList<>();

          Map<String, Names> controllers = new HashMap<>();

          while (getControllersOnlinePastWeekReportRset.next()) {

            ControllersOnlineReportItemResponse cori = new ControllersOnlineReportItemResponse();
            cori.setCid(getControllersOnlinePastWeekReportRset.getString("cid"));
            cori.setPosition(getControllersOnlinePastWeekReportRset.getString("position"));
            cori.setSecondsControlled(getControllersOnlinePastWeekReportRset.getLong("time_in_seconds"));

            Names names = null;

            if (getControllersOnlinePastWeekReportRset.getString("first_name") == null) {
              if (!controllers.containsKey(cori.getCid())) {
                VatEudUser memberDetails = vatEudCoreApi.getMemberDetails(Long.valueOf(cori.getCid()));
                if (memberDetails != null) {
                  names = Names.builder().firstName(memberDetails.getData().getFirstName()).lastName(memberDetails.getData().getLastName()).build();
                  controllers.put(cori.getCid(), names);
                }
              } else {
                names = controllers.get(cori.getCid());
              }
            } else {
              if (!controllers.containsKey(cori.getCid())) {
                names = Names.builder().firstName(getControllersOnlinePastWeekReportRset.getString("first_name")).lastName(getControllersOnlinePastWeekReportRset.getString("last_name")).build();
                controllers.put(cori.getCid(), names);
              } else {
                names = controllers.get(cori.getCid());
              }
            }

            cori.setControllerName(names);

            if (cori.getPosition().contains("_TWR")) {
              towerPositions.add(cori);
            }

            if (cori.getPosition().contains("_APP")) {
              approachPositions.add(cori);
            }

            if (cori.getPosition().contains("_CTR")) {
              controlPositions.add(cori);
            }
          }

//        log.debug("cidColumnMaxWidth: " + cidColumnMaxWidth);
//        log.debug("namesMostLetters: " + namesMostLetters);
//        log.debug("namesColumnMaxWidth: " + namesColumnMaxWidth);
          ControllersOnlineReportResponse response = new ControllersOnlineReportResponse();
          response.setTowerPositions(towerPositions);
          response.setApproachPositions(approachPositions);
          response.setControlPositions(controlPositions);
          
          conn.commit();

          return response;
        }
        
        conn.rollback();

        return null;

      } catch (SQLException ex) {
        log.error("Error getting controllers online past week report.", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting controllers online past week report.", e);
    }

    return null;
  }

  @Override
  public Long getTotalATCSessionsForUser(String cid) {

    final String getTotalATCSessionsForUserSql = "SELECT COUNT(1) session_count FROM controllers_online_log WHERE cid = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getTotalATCSessionsForUserPstmt = conn.prepareStatement(getTotalATCSessionsForUserSql)) {

      try {

        conn.setAutoCommit(false);

        getTotalATCSessionsForUserPstmt.setString(1, cid);

        ResultSet getTotalATCSessionsForUserRset = getTotalATCSessionsForUserPstmt.executeQuery();

        if (getTotalATCSessionsForUserRset.next()) {

          return getTotalATCSessionsForUserRset.getLong("session_count");
        }

      } catch (SQLException ex) {
        log.error("Error getting total ATC sessions for controller with ID: '" + cid + "'.", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting total ATC sessions for controller with ID: '" + cid + "'.", e);
    }

    return 0L;

  }
}
