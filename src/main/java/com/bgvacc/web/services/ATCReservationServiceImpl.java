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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm'z'");

        LocalDateTime startLocalDateTime = LocalDateTime.parse(carm.getStartTime(), formatter);
        LocalDateTime endLocalDateTime = LocalDateTime.parse(carm.getEndTime(), formatter);

        // Задаваме времева зона UTC (Zulu)
        Timestamp startDateTime = Timestamp.valueOf(startLocalDateTime.atZone(ZoneId.of("UTC")).toLocalDateTime());
        Timestamp endDateTime = Timestamp.valueOf(endLocalDateTime.atZone(ZoneId.of("UTC")).toLocalDateTime());

        createNewATCReservationPstmt.setTimestamp(5, startDateTime);
        createNewATCReservationPstmt.setTimestamp(6, endDateTime);

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
