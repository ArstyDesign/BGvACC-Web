package com.bgvacc.web.services;

import com.bgvacc.web.responses.events.EventResponse;
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

              ZoneId zoneId = ZoneId.of("Europe/Sofia");

              LocalDateTime fromDateTime = ved.getFromDateTime();
              ZonedDateTime zonedStartAtDateTimeUTC = fromDateTime.atZone(ZoneId.of("UTC"));
              ZonedDateTime zonedStartAtDateTimeLocal = zonedStartAtDateTimeUTC.withZoneSameInstant(zoneId);

              LocalDateTime toDateTime = ved.getToDateTime();
              ZonedDateTime zonedEndAtDateTimeUTC = toDateTime.atZone(ZoneId.of("UTC"));
              ZonedDateTime zonedEndAtDateTimeLocal = zonedEndAtDateTimeUTC.withZoneSameInstant(zoneId);

              synchoniseVatsimEventPstmt.setTimestamp(7, Timestamp.from(zonedStartAtDateTimeLocal.toInstant()));
              synchoniseVatsimEventPstmt.setTimestamp(8, Timestamp.from(zonedEndAtDateTimeLocal.toInstant()));
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
