package com.bgvacc.web.services;

import com.bgvacc.web.models.atcreservations.CreateATCReservationModel;
import com.bgvacc.web.responses.atc.ATCReservationResponse;
import com.bgvacc.web.utils.Names;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
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
public class ATCReservationServiceImpl implements ATCReservationService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final static String DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm'z'";

  private final JdbcTemplate jdbcTemplate;

  @Override
  public List<ATCReservationResponse> getAllFutureATCReservations() {

    final String getAllFutureATCReservationsSql = "SELECT ar.reservation_id, ar.reservation_type, ar.position_id, ar.user_cid, u.first_name user_first_name, u.last_name user_last_name, ar.trainee_cid, tu.first_name trainee_first_name, tu.last_name trainee_last_name, ar.from_time, ar.to_time, ar.created_at FROM atc_reservations ar JOIN users u ON ar.user_cid = u.cid LEFT JOIN users tu ON ar.trainee_cid = tu.cid WHERE ar.from_time > NOW() ORDER BY ar.from_time, ar.to_time";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
         PreparedStatement getAllFutureATCReservationsPstmt = conn.prepareStatement(getAllFutureATCReservationsSql)) {

      try {

        conn.setAutoCommit(false);

        List<ATCReservationResponse> atcReservations = new ArrayList<>();

        ResultSet getAllFutureATCReservationsRset = getAllFutureATCReservationsPstmt.executeQuery();

        while (getAllFutureATCReservationsRset.next()) {

          ATCReservationResponse ar = getATCReservation(getAllFutureATCReservationsRset);

          atcReservations.add(ar);
        }

        return atcReservations;

      } catch (SQLException ex) {
        log.error("Error getting all future ATC reservations.", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting all future ATC reservations.", e);
    }

    return null;
  }

  @Override
  public boolean createNewATCReservation(CreateATCReservationModel carm) {

    final String createNewATCReservationSql = "INSERT INTO atc_reservations (reservation_type, position_id, user_cid, trainee_cid, from_time, to_time) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
         PreparedStatement createNewATCReservationPstmt = conn.prepareStatement(createNewATCReservationSql)) {

      try {

        conn.setAutoCommit(false);

        if (carm.getType() != null) {
          switch (carm.getType()) {
            case "t":
              createNewATCReservationPstmt.setString(1, "Training");
              break;
            default:
              createNewATCReservationPstmt.setString(1, "Normal");
              break;
          }
        } else {
          createNewATCReservationPstmt.setString(1, "Normal");
        }

        createNewATCReservationPstmt.setString(2, carm.getPosition());
        createNewATCReservationPstmt.setString(3, carm.getUserCid());

        if (carm.getTraineeCid() == null || carm.getTraineeCid().trim().isEmpty()) {
          createNewATCReservationPstmt.setString(4, carm.getTraineeCid());
        }

        createNewATCReservationPstmt.setTimestamp(5, getTimestampFromString(carm.getStartTime(), DATE_TIME_FORMAT));
        createNewATCReservationPstmt.setTimestamp(6, getTimestampFromString(carm.getEndTime(), DATE_TIME_FORMAT));

        int rows = createNewATCReservationPstmt.executeUpdate();

        conn.commit();

        return rows > 0;

      } catch (SQLException ex) {
        log.error("Error creating new ATC reservation.", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error creating new ATC reservation.", e);
    }

    return false;
  }

  @Override
  public boolean isPositionFreeForTimeSlot(String position, String startTime, String endTime) {

    final String isPositionFreeForTimeSlotSql = "SELECT EXISTS (SELECT 1 FROM atc_reservations WHERE position_id = ? AND (from_time <= ? AND to_time >= ?)) AS are_overlapping";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
         PreparedStatement isPositionFreeForTimeSlotPstmt = conn.prepareStatement(isPositionFreeForTimeSlotSql)) {

      try {

//        conn.setAutoCommit(false);
        isPositionFreeForTimeSlotPstmt.setString(1, position);
        isPositionFreeForTimeSlotPstmt.setTimestamp(2, getTimestampFromString(endTime, DATE_TIME_FORMAT));
        isPositionFreeForTimeSlotPstmt.setTimestamp(3, getTimestampFromString(startTime, DATE_TIME_FORMAT));

        ResultSet isPositionFreeForTimeSlotRset = isPositionFreeForTimeSlotPstmt.executeQuery();

        if (isPositionFreeForTimeSlotRset.next()) {
          return !isPositionFreeForTimeSlotRset.getBoolean(1);
        }

      } catch (SQLException ex) {
        log.error("Error checking if position '" + position + "' is free for time slot.", ex);
//        conn.rollback();
      } finally {
//        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error checking if position '" + position + "' is free for time slot.", e);
    }

    return false;
  }

  @Override
  public boolean isFromBeforeToTime(String startTime, String endTime) {

    return false;
  }

  @Override
  public boolean isReservationLongerThan(int minutes) {

    return true;
  }

  @Override
  public boolean hasUserReservedAnotherPositionForTime(String userCid, String startTime, String endTime) {

    return false;
  }

  private Timestamp getTimestampFromString(String time, String pattern) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

    LocalDateTime localDateTime = LocalDateTime.parse(time, formatter);

    Timestamp dateTime = Timestamp.valueOf(localDateTime.atZone(ZoneId.of("UTC")).toLocalDateTime());

    return dateTime;
  }

  private ATCReservationResponse getATCReservation(ResultSet rset) throws SQLException {

    ATCReservationResponse ar = new ATCReservationResponse();
    ar.setReservationId(rset.getString("reservation_id"));
    ar.setReservationType(rset.getString("reservation_type"));
    ar.setPosition(rset.getString("position_id"));
    ar.setUserCid(rset.getString("user_cid"));

    String userFirstName = rset.getString("user_first_name");
    String userLastName = rset.getString("user_last_name");

    Names userNames = Names.builder().firstName(userFirstName).lastName(userLastName).build();
    ar.setUserNames(userNames);

    ar.setTraineeCid(rset.getString("trainee_cid"));

    if (ar.getTraineeCid() != null) {
      String traineeFirstName = rset.getString("trainee_first_name");
      String traineeLastName = rset.getString("trainee_last_name");

      Names traineeNames = Names.builder().firstName(traineeFirstName).lastName(traineeLastName).build();
      ar.setTraineeNames(traineeNames);
    }

    Timestamp fromTimeTimestamp = rset.getTimestamp("from_time");

    if (fromTimeTimestamp != null) {
      // Преобразуване на Timestamp в ZonedDateTime в желаната времева зона
      ZonedDateTime fromTimeZonedDateTime = fromTimeTimestamp.toInstant()
              .atZone(ZoneId.of("UTC")) // Използваме UTC времева зона
              .withZoneSameInstant(ZoneId.of("Europe/Sofia")); // Конвертиране в желаната времева зона

//            System.out.println("Event Start Time: " + zonedDateTime);
      ar.setFromTime(fromTimeZonedDateTime);
    }

    Timestamp toTimeTimestamp = rset.getTimestamp("to_time");

    if (toTimeTimestamp != null) {
      // Преобразуване на Timestamp в ZonedDateTime в желаната времева зона
      ZonedDateTime toTimeZonedDateTime = toTimeTimestamp.toInstant()
              .atZone(ZoneId.of("UTC")) // Използваме UTC времева зона
              .withZoneSameInstant(ZoneId.of("Europe/Sofia")); // Конвертиране в желаната времева зона

//            System.out.println("Event Start Time: " + zonedDateTime);
      ar.setToTime(toTimeZonedDateTime);
    }

    Timestamp createdAtTimestamp = rset.getTimestamp("created_at");

    if (createdAtTimestamp != null) {
      // Преобразуване на Timestamp в ZonedDateTime в желаната времева зона
      ZonedDateTime createdAtZonedDateTime = createdAtTimestamp.toInstant()
              .atZone(ZoneId.of("UTC")) // Използваме UTC времева зона
              .withZoneSameInstant(ZoneId.of("Europe/Sofia")); // Конвертиране в желаната времева зона

//            System.out.println("Event Start Time: " + zonedDateTime);
      ar.setCreatedAt(createdAtZonedDateTime);
    }

    return ar;
  }
}
