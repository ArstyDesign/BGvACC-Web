package com.bgvacc.web.resultmappers;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @param <T> the object to be mapped
 * @since 1.0.0
 */
@FunctionalInterface
public interface ResultSetMapper<T> {

  T map(ResultSet rs) throws SQLException;
}
