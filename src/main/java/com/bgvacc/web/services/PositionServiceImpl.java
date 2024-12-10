package com.bgvacc.web.services;

import com.bgvacc.web.responses.users.atc.PositionResponse;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author Atanas Yordanov Arshinkove
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final JdbcTemplate jdbcTemplate;

  @Override
  public List<PositionResponse> getPositionsExcept(String... excludedPositions) {

    StringBuilder placeholders = new StringBuilder();
    for (int i = 0; i < excludedPositions.length; i++) {
      placeholders.append("?");
      if (i < excludedPositions.length - 1) {
        placeholders.append(", ");
      }
    }

    final String getPositionsExceptSql = "SELECT * FROM positions WHERE position_id NOT IN (" + placeholders + ") ORDER BY order_priority";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
         PreparedStatement getPositionsExceptPstmt = conn.prepareStatement(getPositionsExceptSql)) {

      try {

        conn.setAutoCommit(false);

        List<PositionResponse> positions = new ArrayList<>();

        for (int i = 0; i < excludedPositions.length; i++) {
          getPositionsExceptPstmt.setString(i + 1, excludedPositions[i]);
        }

        ResultSet getPositionsExceptRset = getPositionsExceptPstmt.executeQuery();

        while (getPositionsExceptRset.next()) {

          PositionResponse p = new PositionResponse();
          p.setPosition(getPositionsExceptRset.getString("position_id"));
          p.setName(getPositionsExceptRset.getString("name"));
          p.setOrderPriority(getPositionsExceptRset.getInt("order_priority"));

          positions.add(p);
        }

        return positions;

      } catch (SQLException ex) {
        log.error("Error getting positions excluding some.", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting positions excluding some.", e);
    }

    return null;
  }

  @Override
  public List<PositionResponse> getPositionsNotAssignedForEvent(Long eventId) {

    final String getPositionsNotAssignedForEventSql = "SELECT * FROM positions WHERE position_id NOT IN (SELECT position_id FROM event_positions WHERE event_id = ?)";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
         PreparedStatement getPositionsNotAssignedForEventPstmt = conn.prepareStatement(getPositionsNotAssignedForEventSql)) {

      try {

        conn.setAutoCommit(false);

        List<PositionResponse> positions = new ArrayList<>();

        getPositionsNotAssignedForEventPstmt.setLong(1, eventId);

        ResultSet getPositionsNotAssignedForEventRset = getPositionsNotAssignedForEventPstmt.executeQuery();

        while (getPositionsNotAssignedForEventRset.next()) {

          PositionResponse p = new PositionResponse();
          p.setPosition(getPositionsNotAssignedForEventRset.getString("position_id"));
          p.setName(getPositionsNotAssignedForEventRset.getString("name"));
          p.setOrderPriority(getPositionsNotAssignedForEventRset.getInt("order_priority"));

          positions.add(p);
        }

        return positions;

      } catch (SQLException ex) {
        log.error("Error getting positions not assigned to event with ID '" + eventId + "'.", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting positions not assigned to event with ID '" + eventId + "'.", e);
    }

    return null;
  }
}
