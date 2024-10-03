package com.bgvacc.web.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class DatabaseServiceImpl implements DatabaseService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private final JdbcTemplate jdbcTemplate;

  @Override
  public boolean isDatabaseConnected() {

    final String isDatabaseConnectedSql = "SELECT name, value FROM sys_params WHERE name = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection(); PreparedStatement isDatabaseConnectedPstmt = conn.prepareStatement(isDatabaseConnectedSql)) {

      try {

        conn.setAutoCommit(false);

        isDatabaseConnectedPstmt.setString(1, "is_db_active");

        ResultSet isDatabaseConnectedRset = isDatabaseConnectedPstmt.executeQuery();

        if (isDatabaseConnectedRset.next()) {

          String result = isDatabaseConnectedRset.getString("value");

          return result.equals("1");
        }

      } catch (SQLException ex) {
        log.error("Error getting system parameter", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting system parameter", e);
    }

    return false;
  }
}
