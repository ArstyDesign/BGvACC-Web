package com.bgvacc.web.services;

import com.aarshinkov.datetimecalculator.utils.TimeUtils;
import com.bgvacc.web.api.vateud.VatEudCoreApi;
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

    final String getControllersOnlinePastWeekReportSql = "SELECT cid, position, SUM(CAST(EXTRACT(EPOCH FROM (session_ended - session_started)) AS INTEGER)) AS seconds_controlled FROM controllers_online_log WHERE session_started >= date_trunc('week', NOW() - INTERVAL '1 week') AND session_ended <= date_trunc('week', NOW()) + INTERVAL '4 hour' GROUP BY cid, position ORDER BY position, seconds_controlled DESC";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getControllersOnlinePastWeekReportPstmt = conn.prepareStatement(getControllersOnlinePastWeekReportSql)) {

      try {

        conn.setAutoCommit(false);

        ResultSet getControllersOnlinePastWeekReportRset = getControllersOnlinePastWeekReportPstmt.executeQuery();

        List<ControllersOnlineReportItemResponse> towerPositions = new ArrayList<>();
        List<ControllersOnlineReportItemResponse> approachPositions = new ArrayList<>();
        List<ControllersOnlineReportItemResponse> controlPositions = new ArrayList<>();

        Map<String, Names> controllers = new HashMap<>();

        int cidColumnMaxWidth = 0;
        String namesMostLetters = "";
        int namesColumnMaxWidth = 0;

        while (getControllersOnlinePastWeekReportRset.next()) {

          ControllersOnlineReportItemResponse cori = new ControllersOnlineReportItemResponse();
          cori.setCid(getControllersOnlinePastWeekReportRset.getString("cid"));
          cori.setPosition(getControllersOnlinePastWeekReportRset.getString("position"));
          cori.setSecondsControlled(getControllersOnlinePastWeekReportRset.getLong("seconds_controlled"));

          Names names;

          if (!controllers.containsKey(cori.getCid())) {
            VatEudUser memberDetails = vatEudCoreApi.getMemberDetails(Long.valueOf(cori.getCid()));
            names = Names.builder().firstName(memberDetails.getData().getFirstName()).lastName(memberDetails.getData().getLastName()).build();
            controllers.put(cori.getCid(), names);
          } else {
            names = controllers.get(cori.getCid());
          }

          if (cori.getCid().length() > cidColumnMaxWidth) {
            cidColumnMaxWidth = cori.getCid().length();
          }

          if (names.getFullName().length() > namesColumnMaxWidth) {
            namesColumnMaxWidth = names.getFullName().length();
            namesMostLetters = names.getFullName();
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
        response.setCidColumnMaxWidth(cidColumnMaxWidth);
        response.setNamesColumnMaxWidth(namesColumnMaxWidth);

        return response;

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
}
