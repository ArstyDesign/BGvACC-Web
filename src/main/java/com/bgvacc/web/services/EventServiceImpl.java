package com.bgvacc.web.services;

import com.bgvacc.web.responses.events.*;
import com.bgvacc.web.utils.Names;
import com.bgvacc.web.vatsim.events.VatsimEventData;
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

  @Override
  public List<EventResponse> getAllEvents() {

    final String getEventsSql = "SELECT * FROM events";

    return getEvents(getEventsSql);
  }

  @Override
  public List<EventResponse> getPastEvents() {

    final String getEventsSql = "SELECT * FROM events WHERE end_at < NOW()";

    return getEvents(getEventsSql);
  }

  @Override
  public List<EventResponse> getUpcomingEvents() {

    final String getEventsSql = "SELECT * FROM events WHERE start_at > NOW()";

    return getEvents(getEventsSql);
  }

  @Override
  public List<EventResponse> getEvents(String sql) {

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getEventsPstmt = conn.prepareStatement(sql)) {

      try {

        conn.setAutoCommit(false);

        List<EventResponse> events = new ArrayList<>();

        ResultSet getEventsRset = getEventsPstmt.executeQuery();

        while (getEventsRset.next()) {

          EventResponse ved = new EventResponse();
          ved.setEventId(getEventsRset.getLong("event_id"));
          ved.setName(getEventsRset.getString("name"));
          ved.setType(getEventsRset.getString("type"));
          ved.setDescription(getEventsRset.getString("description"));
          ved.setShortDescription(getEventsRset.getString("short_description"));
          ved.setImageUrl(getEventsRset.getString("image_url"));

          Timestamp startAtTimestamp = getEventsRset.getTimestamp("start_at");

          if (startAtTimestamp != null) {
            // Преобразуване на Timestamp в ZonedDateTime в желаната времева зона
            ZonedDateTime startAtZonedDateTime = startAtTimestamp.toInstant()
                    .atZone(ZoneId.of("UTC")) // Използваме UTC времева зона
                    .withZoneSameInstant(ZoneId.of("Europe/Sofia")); // Конвертиране в желаната времева зона

//            System.out.println("Event Start Time: " + zonedDateTime);
            ved.setStartAt(startAtZonedDateTime);
          }

          Timestamp endAtTimestamp = getEventsRset.getTimestamp("end_at");

          if (endAtTimestamp != null) {
            // Преобразуване на Timestamp в ZonedDateTime в желаната времева зона
            ZonedDateTime endAtZonedDateTime = endAtTimestamp.toInstant()
                    .atZone(ZoneId.of("UTC")) // Използваме UTC времева зона
                    .withZoneSameInstant(ZoneId.of("Europe/Sofia")); // Конвертиране в желаната времева зона

//            System.out.println("Event Start Time: " + zonedDateTime);
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
  public EventResponse getEvent(Long eventId) {

    final String getEventSql = "SELECT * FROM events WHERE event_id = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getEventPstmt = conn.prepareStatement(getEventSql)) {

      try {

        conn.setAutoCommit(false);

        getEventPstmt.setLong(1, eventId);

        ResultSet getEventsRset = getEventPstmt.executeQuery();

        if (getEventsRset.next()) {

          EventResponse event = new EventResponse();
          event.setEventId(getEventsRset.getLong("event_id"));
          event.setName(getEventsRset.getString("name"));
          event.setType(getEventsRset.getString("type"));
          event.setDescription(getEventsRset.getString("description"));
          event.setShortDescription(getEventsRset.getString("short_description"));
          event.setImageUrl(getEventsRset.getString("image_url"));

          Timestamp startAtTimestamp = getEventsRset.getTimestamp("start_at");

          if (startAtTimestamp != null) {
            // Преобразуване на Timestamp в ZonedDateTime в желаната времева зона
            ZonedDateTime startAtZonedDateTime = startAtTimestamp.toInstant()
                    .atZone(ZoneId.of("UTC")) // Използваме UTC времева зона
                    .withZoneSameInstant(ZoneId.of("Europe/Sofia")); // Конвертиране в желаната времева зона

//            System.out.println("Event Start Time: " + zonedDateTime);
            event.setStartAt(startAtZonedDateTime);
          }

          Timestamp endAtTimestamp = getEventsRset.getTimestamp("end_at");

          if (endAtTimestamp != null) {
            // Преобразуване на Timestamp в ZonedDateTime в желаната времева зона
            ZonedDateTime endAtZonedDateTime = endAtTimestamp.toInstant()
                    .atZone(ZoneId.of("UTC")) // Използваме UTC времева зона
                    .withZoneSameInstant(ZoneId.of("Europe/Sofia")); // Конвертиране в желаната времева зона

//            System.out.println("Event Start Time: " + zonedDateTime);
            event.setEndAt(endAtZonedDateTime);
          }

//          ZonedDateTime zonedDateTime = getEventsRset.getObject("start_at", ZonedDateTime.class);
//          ved.setEndAt(getEventsRset.getObject("end_at", ZonedDateTime.class));
          event.setCreatedAt(getEventsRset.getTimestamp("created_at"));
          event.setUpdatedAt(getEventsRset.getTimestamp("updated_at"));

          return event;
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

    final String getEventPositionsSql = "SELECT ep.event_position_id, ep.event_id, ep.position_id, p.name AS position_name, p.order_priority, ep.minimum_rating, ep.is_approved FROM event_positions ep JOIN positions p ON ep.position_id = p.position_id WHERE ep.event_id = ? ORDER BY p.order_priority";
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
  public void synchroniseVatsimEventsToDatabase(List<VatsimEventData> data) {

    final String checkIfEventExistsInDatabaseSql = "SELECT EXISTS (SELECT 1 FROM events WHERE event_id = ?)";
    final String synchoniseVatsimEventSql = "INSERT INTO events (event_id, name, type, description, short_description, image_url, start_at, end_at, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

//    for (VatsimEventData ved : data) {
//      log.debug("ved: " + ved.getStart());
//    }
    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement checkIfEventExistsInDatabasePstmt = conn.prepareStatement(checkIfEventExistsInDatabaseSql);
            PreparedStatement synchoniseVatsimEventPstmt = conn.prepareStatement(synchoniseVatsimEventSql)) {

      try {

        conn.setAutoCommit(false);

        for (VatsimEventData ved : data) {

          checkIfEventExistsInDatabasePstmt.setLong(1, ved.getId());

          ResultSet checkIfEventExistsInDatabaseRset = checkIfEventExistsInDatabasePstmt.executeQuery();

          if (checkIfEventExistsInDatabaseRset.next()) {

            if (!checkIfEventExistsInDatabaseRset.getBoolean(1)) {

              log.debug("Event with ID '" + ved.getId() + "' does not exist.");

              synchoniseVatsimEventPstmt.setLong(1, ved.getId());
              synchoniseVatsimEventPstmt.setString(2, ved.getName());

              if (ved.getName().toLowerCase().contains("exam")) {
                synchoniseVatsimEventPstmt.setString(3, "cpt");
              } else {
                synchoniseVatsimEventPstmt.setString(3, "event");
              }

              synchoniseVatsimEventPstmt.setString(4, ved.getDescription());
              synchoniseVatsimEventPstmt.setString(5, ved.getShortDescription());
              synchoniseVatsimEventPstmt.setString(6, ved.getImageUrl());

//              ZoneId zoneId = ZoneId.of("Europe/Sofia");
              LocalDateTime startDateTime = ved.getFromDateTime();
//              ZonedDateTime zonedStartAtDateTimeUTC = fromDateTime.atZone(ZoneId.of("UTC"));
//              ZonedDateTime zonedStartAtDateTimeLocal = zonedStartAtDateTimeUTC.withZoneSameInstant(zoneId);

              LocalDateTime endDateTime = ved.getToDateTime();
//              ZonedDateTime zonedEndAtDateTimeUTC = toDateTime.atZone(ZoneId.of("UTC"));
//              ZonedDateTime zonedEndAtDateTimeLocal = zonedEndAtDateTimeUTC.withZoneSameInstant(zoneId);

              synchoniseVatsimEventPstmt.setTimestamp(7, Timestamp.valueOf(startDateTime));
              synchoniseVatsimEventPstmt.setTimestamp(8, Timestamp.valueOf(endDateTime));
              synchoniseVatsimEventPstmt.setTimestamp(9, ved.getCreatedAt());

              if (ved.getUpdatedAt() != null) {
                synchoniseVatsimEventPstmt.setTimestamp(10, ved.getUpdatedAt());
              } else {
                synchoniseVatsimEventPstmt.setNull(10, Types.TIMESTAMP);
              }

              synchoniseVatsimEventPstmt.executeUpdate();

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
}
