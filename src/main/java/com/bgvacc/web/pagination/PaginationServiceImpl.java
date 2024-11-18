package com.bgvacc.web.pagination;

import com.bgvacc.web.resultmappers.ResultSetMapper;
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
 * @param <T>
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class PaginationServiceImpl<T> implements PaginationService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final JdbcTemplate jdbcTemplate;

  @Override
  public PagedResponse<T> getPagedResults(String query, String countQuery, int pageNumber, int pageSize, ResultSetMapper mapper) throws SQLException {

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement dataPstm = conn.prepareStatement(query);
            PreparedStatement countPstmt = conn.prepareStatement(countQuery)) {

      try {

        conn.setAutoCommit(false);

        int offset = (pageNumber - 1) * pageSize;
        List<T> content = new ArrayList<>();
        long totalElements = 0;

        return null;

      } catch (SQLException ex) {
        log.error("Error", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error", e);
    }

    return null;
  }
}
