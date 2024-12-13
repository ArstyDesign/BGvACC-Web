package com.bgvacc.web.services;

import com.aarshinkov.datetimecalculator.utils.TimeUtils;
import com.bgvacc.web.beans.SlotsGenerator;
import com.bgvacc.web.domains.Slot;
import com.bgvacc.web.responses.events.*;
import com.bgvacc.web.responses.events.reports.*;
import com.bgvacc.web.utils.Names;
import com.bgvacc.web.vatsim.events.VatsimEventData;
import com.bgvacc.web.vatsim.utils.VatsimRatingUtils;
import java.sql.*;
import java.time.*;
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
public class EventServiceImpl implements EventService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final JdbcTemplate jdbcTemplate;

  private final SlotsGenerator slotsGenerator;

  @Override
  public List<EventResponse> getAllEvents() {

    final String getEventsSql = "SELECT * FROM events";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
         PreparedStatement getEventsPstmt = conn.prepareStatement(getEventsSql)) {

      try {

        conn.setAutoCommit(false);

        List<EventResponse> events = new ArrayList<>();

        ResultSet getEventsRset = getEventsPstmt.executeQuery();

        while (getEventsRset.next()) {

          EventResponse ved = new EventResponse();
          ved.setEventId(getEventsRset.getLong("event_id"));
          ved.setName(getEventsRset.getString("name"));
          ved.setType(getEventsRset.getString("type"));
          ved.setPriority(getEventsRset.getInt("priority"));
          ved.setDescription(getEventsRset.getString("description"));
          ved.setShortDescription(getEventsRset.getString("short_description"));
          ved.setVatsimEventUrl(getEventsRset.getString("vatsim_event_url"));
          ved.setVateudEventUrl(getEventsRset.getString("vateud_event_url"));
          ved.setImageUrl(getEventsRset.getString("image_url"));

          Timestamp startAtTimestamp = getEventsRset.getTimestamp("start_at");

          if (startAtTimestamp != null) {
            ZonedDateTime startAtZonedDateTime = startAtTimestamp.toLocalDateTime()
                    .atZone(ZoneId.of("UTC"));
            ved.setStartAt(startAtZonedDateTime);
          }

          Timestamp endAtTimestamp = getEventsRset.getTimestamp("end_at");

          if (endAtTimestamp != null) {
            ZonedDateTime endAtZonedDateTime = endAtTimestamp.toInstant()
                    .atZone(ZoneId.of("UTC"));

            ved.setEndAt(endAtZonedDateTime);
          }

//          ZonedDateTime zonedDateTime = getEventsRset.getObject("start_at", ZonedDateTime.class);
//          ved.setEndAt(getEventsRset.getObject("end_at", ZonedDateTime.class));
          ved.setCreatedAt(getEventsRset.getTimestamp("created_at"));
          ved.setUpdatedAt(getEventsRset.getTimestamp("updated_at"));

          events.add(ved);
        }

        return events;

      } catch (SQLException ex) {
        log.error("Error getting VATSIM events", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting VATSIM events", e);
    }

    return null;
  }

  @Override
  public List<EventResponse> getPastEvents() {

    final String getEventsSql = "SELECT * FROM events WHERE end_at < ? ORDER BY start_at DESC";

    return getEventsBeforeAfterNow(getEventsSql);
  }

  @Override
  public List<EventResponse> getUpcomingEvents() {

//    final String getEventsSql = "SELECT * FROM events WHERE start_at > ? ORDER BY start_at, created_at";
    final String getEventsSql = "SELECT * FROM events WHERE end_at > ? ORDER BY start_at, created_at";

    return getEventsBeforeAfterNow(getEventsSql);
  }

  @Override
  public List<EventResponse> getEventsBeforeAfterNow(String sql) {

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
         PreparedStatement getEventsPstmt = conn.prepareStatement(sql)) {

      try {

        conn.setAutoCommit(false);

        List<EventResponse> events = new ArrayList<>();

        Timestamp now = TimeUtils.getNowTimeUTC();

        getEventsPstmt.setTimestamp(1, now);

        ResultSet getEventsRset = getEventsPstmt.executeQuery();

        while (getEventsRset.next()) {

          EventResponse ved = getEventResponseFromResultSet(getEventsRset);
          events.add(ved);
        }

        return events;

      } catch (SQLException ex) {
        log.error("Error getting VATSIM events", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting VATSIM events", e);
    }

    return null;
  }

  @Override
  public UpcomingEventsResponse getUpcomingEventsAfterDays(Integer days) {

    final String getUpcomingEventsAfterDaysSql = "SELECT * FROM events WHERE DATE(start_at) = CURRENT_DATE + INTERVAL '" + days + " days' ORDER BY priority";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
         PreparedStatement getUpcomingEventsAfterDaysPstmt = conn.prepareStatement(getUpcomingEventsAfterDaysSql)) {

      try {

        conn.setAutoCommit(false);

        List<EventResponse> events = new ArrayList<>();

//        getUpcomingEventsAfterDaysPstmt.setInt(1, days);
        ResultSet getEventsRset = getUpcomingEventsAfterDaysPstmt.executeQuery();

        while (getEventsRset.next()) {

          EventResponse ved = getEventResponseFromResultSet(getEventsRset);
          events.add(ved);
        }

        UpcomingEventsResponse uer = new UpcomingEventsResponse(events);

        return uer;

      } catch (SQLException ex) {
        log.error("Error getting upcoming VATSIM events", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting upcoming VATSIM events", e);
    }

    return null;
  }

  @Override
  public EventResponse getEvent(Long eventId) {

    final String getEventSql = "SELECT * FROM events WHERE event_id = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
         PreparedStatement getEventPstmt = conn.prepareStatement(getEventSql)) {

      try {

        conn.setAutoCommit(false);

        getEventPstmt.setLong(1, eventId);

        ResultSet getEventsRset = getEventPstmt.executeQuery();

        if (getEventsRset.next()) {

          EventResponse ved = getEventResponseFromResultSet(getEventsRset);
          return ved;
        }

      } catch (SQLException ex) {
        log.error("Error getting event with ID '" + eventId + "'", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting event with ID '" + eventId + "'", e);
    }

    return null;
  }

  public List<String> getEventRoster(Long eventId) {

    return null;
  }

  @Override
  public List<EventPositionsResponse> getEventPositions(Long eventId) {

    final String getEventPositionsSql = "SELECT ep.event_position_id, ep.event_id, ep.position_id, p.name AS position_name, p.order_priority, ep.minimum_rating, ep.can_trainees_apply, ep.is_approved FROM event_positions ep JOIN positions p ON ep.position_id = p.position_id WHERE ep.event_id = ? ORDER BY p.order_priority";
    final String getEventPositionSlotsSql = "SELECT s.slot_id, s.start_time, s.end_time, s.user_cid, u.first_name AS user_first_name, u.last_name AS user_last_name, s.is_approved FROM slots s LEFT JOIN users u ON s.user_cid = u.cid WHERE s.event_position_id = ?";
    final String getUserEventApplicationsSql = "SELECT uea.application_id, uea.user_cid, u.first_name AS user_first_name, u.last_name AS user_last_name, u.highest_controller_rating, uea.slot_id, uea.status, uea.applied_at FROM user_event_applications uea JOIN users u ON uea.user_cid = u.cid WHERE uea.slot_id = ? ORDER BY uea.applied_at";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
         PreparedStatement getEventPositionsPstmt = conn.prepareStatement(getEventPositionsSql);
         PreparedStatement getEventPositionSlotsPstmt = conn.prepareStatement(getEventPositionSlotsSql);
         PreparedStatement getUserEventApplicationsPstmt = conn.prepareStatement(getUserEventApplicationsSql)) {

      try {

        conn.setAutoCommit(false);

        getEventPositionsPstmt.setLong(1, eventId);

        List<EventPositionsResponse> eventPositions = new ArrayList<>();

        ResultSet getEventPositionsRset = getEventPositionsPstmt.executeQuery();

        while (getEventPositionsRset.next()) {

          EventPositionsResponse epr = new EventPositionsResponse();
          epr.setEventPositionId(getEventPositionsRset.getString("event_position_id"));
          epr.setEventId(getEventPositionsRset.getLong("event_id"));
          epr.setPositionId(getEventPositionsRset.getString("position_id"));
          epr.setPositionName(getEventPositionsRset.getString("position_name"));
          epr.setMinimumRating(getEventPositionsRset.getInt("minimum_rating"));
          epr.setCanTraineesApply(getEventPositionsRset.getBoolean("can_trainees_apply"));
          epr.setIsApproved(getEventPositionsRset.getBoolean("is_approved"));

          getEventPositionSlotsPstmt.setString(1, epr.getEventPositionId());

          List<EventSlotResponse> eventSlots = new ArrayList<>();

          ResultSet getEventPositionSlotsRset = getEventPositionSlotsPstmt.executeQuery();

          while (getEventPositionSlotsRset.next()) {

            EventSlotResponse eventSlot = new EventSlotResponse();
            eventSlot.setSlotId(getEventPositionSlotsRset.getString("slot_id"));
            eventSlot.setStartTime(getEventPositionSlotsRset.getTimestamp("start_time"));
            eventSlot.setEndTime(getEventPositionSlotsRset.getTimestamp("end_time"));

            String userCid = getEventPositionSlotsRset.getString("user_cid");

            if (userCid != null) {

              EventUserResponse eventUser = new EventUserResponse();
              eventUser.setCid(userCid);
              Names names = Names.builder()
                      .firstName(getEventPositionSlotsRset.getString("user_first_name"))
                      .lastName(getEventPositionSlotsRset.getString("user_last_name"))
                      .build();
              eventUser.setNames(names);

              eventSlot.setUser(eventUser);
            }

            eventSlot.setIsApproved(getEventPositionSlotsRset.getBoolean("is_approved"));

            getUserEventApplicationsPstmt.setString(1, eventSlot.getSlotId());

            List<EventUserApplicationResponse> userEventApplications = new ArrayList<>();

            ResultSet getUserEventApplicationsRset = getUserEventApplicationsPstmt.executeQuery();

            while (getUserEventApplicationsRset.next()) {

              EventUserApplicationResponse euar = new EventUserApplicationResponse();
              euar.setApplicationId(getUserEventApplicationsRset.getString("application_id"));

              EventUserResponse eventApplicationUser = new EventUserResponse();
              eventApplicationUser.setCid(getUserEventApplicationsRset.getString("user_cid"));

              Names names = Names.builder()
                      .firstName(getUserEventApplicationsRset.getString("user_first_name"))
                      .lastName(getUserEventApplicationsRset.getString("user_last_name"))
                      .build();

              eventApplicationUser.setHighestControllerRating(getUserEventApplicationsRset.getInt("highest_controller_rating"));

              eventApplicationUser.setNames(names);
              euar.setUser(eventApplicationUser);
              euar.setSlotId(getUserEventApplicationsRset.getString("slot_id"));

              euar.setStatus(getUserEventApplicationsRset.getBoolean("status"));

              if (getUserEventApplicationsRset.wasNull()) {
                euar.setStatus(null);
              }

              euar.setAppliedAt(getUserEventApplicationsRset.getTimestamp("applied_at"));

              userEventApplications.add(euar);
            }

            eventSlot.setUserEventApplications(userEventApplications);

            eventSlots.add(eventSlot);
          }

          epr.setSlots(eventSlots);

          eventPositions.add(epr);
        }

        return eventPositions;

      } catch (SQLException ex) {
        log.error("Error getting event positions for event with ID '" + eventId + "'", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting event positions for event with ID '" + eventId + "'", e);
    }

    return new ArrayList<>();
  }

  @Override
  public boolean addEventPosition(Long eventId, String position, Integer minimumRating, boolean canTraineesApply) {

    final String addEventPositionSql = "INSERT INTO event_positions (event_id, position_id, minimum_rating, can_trainees_apply) VALUES (?, ?, ?, ?)";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
         PreparedStatement addEventPositionPstmt = conn.prepareStatement(addEventPositionSql)) {

      try {

        conn.setAutoCommit(false);

        addEventPositionPstmt.setLong(1, eventId);
        addEventPositionPstmt.setString(2, position);
        addEventPositionPstmt.setInt(3, minimumRating);
        addEventPositionPstmt.setBoolean(4, canTraineesApply);

        int rows = addEventPositionPstmt.executeUpdate();

        conn.commit();

        return rows > 0;

      } catch (SQLException ex) {
        log.error("Error adding position '" + position + "' for event with ID '" + eventId + "'.", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error adding position '" + position + "' for event with ID '" + eventId + "'.", e);
    }

    return false;
  }

  @Override
  public boolean removeEventPosition(Long eventId, String eventPositionId) {

    final String removeEventPositionSql = "DELETE FROM event_positions WHERE event_position_id = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
         PreparedStatement removeEventPositionPstmt = conn.prepareStatement(removeEventPositionSql)) {

      try {

        conn.setAutoCommit(false);

//        removeEventPositionPstmt.setLong(1, eventId);
        removeEventPositionPstmt.setString(1, eventPositionId);

        int rows = removeEventPositionPstmt.executeUpdate();

        conn.commit();

        return rows > 0;

      } catch (SQLException ex) {
        log.error("Error removing event position with ID '" + eventPositionId + "' for event with ID '" + eventId + "'.", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error removing event position with ID '" + eventPositionId + "' for event with ID '" + eventId + "'.", e);
    }

    return false;
  }

  @Override
  public boolean addSlotsToPosition(Long eventId, String eventPositionId, Integer slotsCount) {

    final String addSlotsToPositionSql = "INSERT INTO slots (event_position_id, start_time, end_time) VALUES (?, ?, ?)";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
         PreparedStatement addSlotsToPositionPstmt = conn.prepareStatement(addSlotsToPositionSql)) {

      try {

        conn.setAutoCommit(false);

        EventResponse event = getEvent(eventId);

        List<Slot> slots = slotsGenerator.generateSlots(event.getStartAt().toLocalDateTime(), event.getEndAt().toLocalDateTime(), slotsCount);

        boolean result = false;

        for (Slot slot : slots) {

          addSlotsToPositionPstmt.setString(1, eventPositionId);
          addSlotsToPositionPstmt.setTimestamp(2, Timestamp.valueOf(slot.getStart()));
          addSlotsToPositionPstmt.setTimestamp(3, Timestamp.valueOf(slot.getEnd()));

          int rows = addSlotsToPositionPstmt.executeUpdate();

          result = rows > 0;
        }

        conn.commit();

        return result;

      } catch (SQLException ex) {
        log.error("Error adding slots for position with ID '" + eventPositionId + "' for event with ID '" + eventId + "'.", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error adding slots for position with ID '" + eventPositionId + "' for event with ID '" + eventId + "'.", e);
    }

    return false;
  }

  @Override
  public void synchroniseVatsimEventsToDatabase(List<VatsimEventData> data) {

    final String checkIfEventExistsInDatabaseSql = "SELECT EXISTS (SELECT 1 FROM events WHERE event_id = ?)";
    final String synchoniseVatsimEventSql = "INSERT INTO events (event_id, name, type, priority, cpt_rating_number, cpt_rating_symbol, cpt_examinee, description, short_description, vatsim_event_url, vateud_event_url, image_url, start_at, end_at, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

//    for (VatsimEventData ved : data) {
//      log.debug("ved: " + ved.getStart());
//    }
    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
         PreparedStatement checkIfEventExistsInDatabasePstmt = conn.prepareStatement(checkIfEventExistsInDatabaseSql);
         PreparedStatement synchroniseVatsimEventPstmt = conn.prepareStatement(synchoniseVatsimEventSql)) {

      try {

        conn.setAutoCommit(false);

        for (VatsimEventData ved : data) {

          checkIfEventExistsInDatabasePstmt.setLong(1, ved.getId());

          ResultSet checkIfEventExistsInDatabaseRset = checkIfEventExistsInDatabasePstmt.executeQuery();

          if (checkIfEventExistsInDatabaseRset.next()) {

            if (!checkIfEventExistsInDatabaseRset.getBoolean(1)) {

              log.debug("Event with ID '" + ved.getId() + "' does not exist.");

              synchroniseVatsimEventPstmt.setLong(1, ved.getId());
              synchroniseVatsimEventPstmt.setString(2, ved.getName());

              if (ved.getName().toLowerCase().contains("exam")) {
                synchroniseVatsimEventPstmt.setString(3, "cpt");
                synchroniseVatsimEventPstmt.setInt(4, 2); // CPT priority - 2

                Integer cptRatingInt = null;
                String cptRatingSymbol;

                String cptRating = ved.getName().toLowerCase();

                if (cptRating.contains("s2")) {
                  cptRatingInt = 3;
                } else if (cptRating.contains("s3")) {
                  cptRatingInt = 4;
                } else if (cptRating.contains("c1")) {
                  cptRatingInt = 5;
                } else if (cptRating.contains("c2")) {
                  cptRatingInt = 6;
                } else if (cptRating.contains("c3")) {
                  cptRatingInt = 7;
                } else if (cptRating.contains("i1")) {
                  cptRatingInt = 8;
                } else if (cptRating.contains("i2")) {
                  cptRatingInt = 9;
                } else if (cptRating.contains("i3")) {
                  cptRatingInt = 10;
                }

                cptRatingSymbol = VatsimRatingUtils.getATCRatingSymbol(cptRatingInt);

                if (cptRatingInt == null) {
                  synchroniseVatsimEventPstmt.setNull(5, Types.INTEGER);
                } else {
                  synchroniseVatsimEventPstmt.setInt(5, cptRatingInt);
                }

                synchroniseVatsimEventPstmt.setString(6, cptRatingSymbol);
                synchroniseVatsimEventPstmt.setString(7, null); // Examinee name

              } else {
                synchroniseVatsimEventPstmt.setString(3, "event");
                synchroniseVatsimEventPstmt.setInt(4, 1); // Event priority - 1

                synchroniseVatsimEventPstmt.setNull(5, Types.INTEGER);
                synchroniseVatsimEventPstmt.setString(6, null);
                synchroniseVatsimEventPstmt.setString(7, null); // Examinee name
              }

              synchroniseVatsimEventPstmt.setString(8, ved.getDescription());
              synchroniseVatsimEventPstmt.setString(9, ved.getShortDescription());
              synchroniseVatsimEventPstmt.setString(10, null); // VATSIM Event URL
              synchroniseVatsimEventPstmt.setString(11, "https://core.vateud.net/division/events/" + ved.getId()); // VATEUD Event URL
              synchroniseVatsimEventPstmt.setString(12, ved.getImageUrl());

//              ZoneId zoneId = ZoneId.of("Europe/Sofia");
              LocalDateTime startDateTime = ved.getFromDateTime();
//              ZonedDateTime zonedStartAtDateTimeUTC = fromDateTime.atZone(ZoneId.of("UTC"));
//              ZonedDateTime zonedStartAtDateTimeLocal = zonedStartAtDateTimeUTC.withZoneSameInstant(zoneId);

              LocalDateTime endDateTime = ved.getToDateTime();
//              ZonedDateTime zonedEndAtDateTimeUTC = toDateTime.atZone(ZoneId.of("UTC"));
//              ZonedDateTime zonedEndAtDateTimeLocal = zonedEndAtDateTimeUTC.withZoneSameInstant(zoneId);

              synchroniseVatsimEventPstmt.setTimestamp(13, Timestamp.valueOf(startDateTime));
              synchroniseVatsimEventPstmt.setTimestamp(14, Timestamp.valueOf(endDateTime));
              synchroniseVatsimEventPstmt.setTimestamp(15, ved.getCreatedAt());

              if (ved.getUpdatedAt() != null) {
                synchroniseVatsimEventPstmt.setTimestamp(16, ved.getUpdatedAt());
              } else {
                synchroniseVatsimEventPstmt.setNull(16, Types.TIMESTAMP);
              }

              synchroniseVatsimEventPstmt.executeUpdate();

            } else {

              log.debug("Event with ID '" + ved.getId() + "' exists. Ignoring.");
            }
          }
        }

        conn.commit();

      } catch (SQLException ex) {
        log.error("Error synchronising VATSIM events to database", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error synchronising VATSIM events to database", e);
    }
  }

  @Override
  public Long getTotalUserEventApplications(String cid) {

    final String getTotalUserEventApplicationsSql = "SELECT COUNT(1) total_user_events_applications FROM user_event_applications where user_cid = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
         PreparedStatement getTotalUserEventApplicationsPstmt = conn.prepareStatement(getTotalUserEventApplicationsSql)) {

      try {

        conn.setAutoCommit(false);

        getTotalUserEventApplicationsPstmt.setString(1, cid);

        ResultSet getTotalATCSessionsForUserRset = getTotalUserEventApplicationsPstmt.executeQuery();

        if (getTotalATCSessionsForUserRset.next()) {

          return getTotalATCSessionsForUserRset.getLong("total_user_events_applications");
        }

      } catch (SQLException ex) {
        log.error("Error getting total user event application for controller with ID: '" + cid + "'.", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting total user event application for controller with ID: '" + cid + "'.", e);
    }

    return 0L;
  }

  @Override
  public EventsYearlyReportResponse getEventsYearlyReportForYear(Integer year) {

//    final String getEventsYearlyReportForYearSql = "SELECT month_series.month, COALESCE(event_counts.type, 'No events') AS type, COALESCE(event_counts.event_count, 0) AS event_count FROM (SELECT generate_series(1, 12) AS month) AS month_series LEFT JOIN (SELECT EXTRACT(MONTH FROM start_at) AS month, type, COUNT(*) AS event_count FROM events WHERE EXTRACT(YEAR FROM start_at) = ? AND EXTRACT(YEAR FROM end_at) = ? GROUP BY month, type) AS event_counts ON month_series.month = event_counts.month ORDER BY month_series.month, type";
    final String getEventsYearlyReportForYearSql = "SELECT month_series.month, ? AS type, COALESCE(event_counts.event_count, 0) AS event_count FROM (SELECT generate_series(1, 12) AS month) AS month_series LEFT JOIN (SELECT EXTRACT(MONTH FROM start_at) AS month, type, COUNT(*) AS event_count FROM events WHERE EXTRACT(YEAR FROM start_at) = ? AND EXTRACT(YEAR FROM end_at) = ? AND type = ? GROUP BY month, type) AS event_counts ON month_series.month = event_counts.month ORDER BY month_series.month";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
         PreparedStatement getEventsYearlyReportForYearPstmt = conn.prepareStatement(getEventsYearlyReportForYearSql)) {

      try {

        conn.setAutoCommit(false);

        EventsYearlyReportResponse eventsYearlyReport = new EventsYearlyReportResponse();
        eventsYearlyReport.setYear(year);

        List<EventYearlyReportResponse> eventsCount = getEventsCount(getEventsYearlyReportForYearPstmt, year, "event");
        EventTypeReportResponse events = new EventTypeReportResponse();
        events.setEvents(eventsCount);
        eventsYearlyReport.setEvents(events);

        List<EventYearlyReportResponse> cptsCount = getEventsCount(getEventsYearlyReportForYearPstmt, year, "cpt");
        EventTypeReportResponse cpts = new EventTypeReportResponse();
        cpts.setEvents(cptsCount);
        eventsYearlyReport.setCpts(cpts);

        List<EventYearlyReportResponse> vasopsCount = getEventsCount(getEventsYearlyReportForYearPstmt, year, "vasops");
        EventTypeReportResponse vasops = new EventTypeReportResponse();
        vasops.setEvents(vasopsCount);
        eventsYearlyReport.setVasops(vasops);

        return eventsYearlyReport;

      } catch (SQLException ex) {
        log.error("Error getting events yearly report for year '" + year + "'.", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting events yearly report for year '" + year + "'.", e);
    }

    return null;
  }

  private List<EventYearlyReportResponse> getEventsCount(PreparedStatement pstmt, Integer year, String type) throws SQLException {

    pstmt.setString(1, type);
    pstmt.setInt(2, year);
    pstmt.setInt(3, year);
    pstmt.setString(4, type);

    List<EventYearlyReportResponse> eventsCount = new ArrayList<>();

    ResultSet rset = pstmt.executeQuery();

    while (rset.next()) {
      EventYearlyReportResponse eyrr = new EventYearlyReportResponse();

      eyrr.setMonth(rset.getInt("month"));
      eyrr.setType(rset.getString("type"));
      eyrr.setCount(rset.getInt("event_count"));

      eventsCount.add(eyrr);
    }

    return eventsCount;
  }

  private EventResponse getEventResponseFromResultSet(ResultSet rset) throws SQLException {

    EventResponse ved = new EventResponse();
    ved.setEventId(rset.getLong("event_id"));
    ved.setName(rset.getString("name"));
    ved.setType(rset.getString("type"));
    ved.setPriority(rset.getInt("priority"));
    ved.setCptRatingNumber(rset.getInt("cpt_rating_number"));

    if (rset.wasNull()) {
      ved.setCptRatingNumber(null);
    }

    ved.setCptRatingSymbol(rset.getString("cpt_rating_symbol"));
    ved.setCptExaminee(rset.getString("cpt_examinee"));
    ved.setDescription(rset.getString("description"));
    ved.setShortDescription(rset.getString("short_description"));
    ved.setVatsimEventUrl(rset.getString("vatsim_event_url"));
    ved.setVateudEventUrl(rset.getString("vateud_event_url"));
    ved.setImageUrl(rset.getString("image_url"));

    Timestamp startAtTimestamp = rset.getTimestamp("start_at");

    if (startAtTimestamp != null) {
      ZonedDateTime startAtZonedDateTime = startAtTimestamp.toLocalDateTime()
              .atZone(ZoneId.of("UTC"));

      ved.setStartAt(startAtZonedDateTime);
    }

    Timestamp endAtTimestamp = rset.getTimestamp("end_at");

    if (endAtTimestamp != null) {
      ZonedDateTime endAtZonedDateTime = endAtTimestamp.toLocalDateTime()
              .atZone(ZoneId.of("UTC"));

      ved.setEndAt(endAtZonedDateTime);
    }

//          ZonedDateTime zonedDateTime = getEventsRset.getObject("start_at", ZonedDateTime.class);
//          ved.setEndAt(getEventsRset.getObject("end_at", ZonedDateTime.class));
    ved.setCreatedAt(rset.getTimestamp("created_at"));
    ved.setUpdatedAt(rset.getTimestamp("updated_at"));

    return ved;
  }
}
