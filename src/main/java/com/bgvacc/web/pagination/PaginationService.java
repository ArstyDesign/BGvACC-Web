package com.bgvacc.web.pagination;

import com.bgvacc.web.resultmappers.ResultSetMapper;
import java.sql.SQLException;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @param <T> the objects type to be paged
 * @since 1.0.0
 */
public interface PaginationService<T> {

  <T> PagedResponse<T> getPagedResults(String query, String countQuery, int pageNumber, int pageSize, ResultSetMapper<T> mapper) throws SQLException;
}
