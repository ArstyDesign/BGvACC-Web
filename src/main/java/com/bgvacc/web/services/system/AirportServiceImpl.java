package com.bgvacc.web.services.system;

import com.bgvacc.web.responses.system.airports.*;
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
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final JdbcTemplate jdbcTemplate;

  @Override
  public List<AirportResponse> getAirports() {

    final String getAirportsSql = "SELECT * FROM airports";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getAirportsPstmt = conn.prepareStatement(getAirportsSql)) {

      try {

        conn.setAutoCommit(false);

        List<AirportResponse> airports = new ArrayList<>();

        ResultSet getAirportsRset = getAirportsPstmt.executeQuery();

        while (getAirportsRset.next()) {

          AirportResponse ap = new AirportResponse();
          ap.setAirportId(getAirportsRset.getString("airport_id"));
          ap.setName(getAirportsRset.getString("name"));
          ap.setLocation(getAirportsRset.getString("location"));
          ap.setImagePath(getAirportsRset.getString("image_path"));

          airports.add(ap);
        }

        return airports;

      } catch (SQLException ex) {
        log.error("Error getting airports", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting airports", e);
    }

    return null;
  }

  @Override
  public AirportResponse getAirport(String airportId) {

    final String getAirportSql = "SELECT * FROM airports WHERE airport_id = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getAirportPstmt = conn.prepareStatement(getAirportSql)) {

      try {

        conn.setAutoCommit(false);

        getAirportPstmt.setString(1, airportId);

        ResultSet getAirportRset = getAirportPstmt.executeQuery();

        if (getAirportRset.next()) {

          AirportResponse ap = new AirportResponse();
          ap.setAirportId(getAirportRset.getString("airport_id"));
          ap.setName(getAirportRset.getString("name"));
          ap.setLocation(getAirportRset.getString("location"));
          ap.setImagePath(getAirportRset.getString("image_path"));

          return ap;
        }

        return null;

      } catch (SQLException ex) {
        log.error("Error getting airport with ID '" + airportId + "'", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting airport with ID '" + airportId + "'", e);
    }

    return null;
  }

  @Override
  public AirportDetailsResponse getAirportDetails(String airportId) {

    final String getAirportSql = "SELECT * FROM airports WHERE airport_id = ?";
    final String getAirportDetailsSql = "SELECT * FROM airport_details WHERE airport_id = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getAirportPstmt = conn.prepareStatement(getAirportSql);
            PreparedStatement getAirportDetailsPstmt = conn.prepareStatement(getAirportDetailsSql)) {

      try {

        conn.setAutoCommit(false);

        getAirportPstmt.setString(1, airportId);

        ResultSet getAirportRset = getAirportPstmt.executeQuery();

        if (getAirportRset.next()) {

          AirportDetailsResponse ap = new AirportDetailsResponse();
          ap.setAirportId(getAirportRset.getString("airport_id"));
          ap.setName(getAirportRset.getString("name"));
          ap.setLocation(getAirportRset.getString("location"));
          ap.setImagePath(getAirportRset.getString("image_path"));

          getAirportDetailsPstmt.setString(1, airportId);

          ResultSet getAirportDetailsRset = getAirportDetailsPstmt.executeQuery();

          if (getAirportDetailsRset.next()) {
            ap.setAirportDetailId(getAirportDetailsRset.getString("airport_detail_id"));
            ap.setIcao(getAirportDetailsRset.getString("icao"));
            ap.setIata(getAirportDetailsRset.getString("iata"));
            ap.setIsMajor(getAirportDetailsRset.getBoolean("is_major"));
            ap.setElevation(getAirportDetailsRset.getInt("elevation"));
            ap.setTransitionAltitude(getAirportDetailsRset.getInt("transition_altitude"));
            ap.setTransitionLevel(getAirportDetailsRset.getInt("transition_level"));
            ap.setMsa(getAirportDetailsRset.getInt("msa"));
            ap.setLatitude(getAirportDetailsRset.getDouble("latitude"));
            ap.setLongitude(getAirportDetailsRset.getDouble("longitude"));
          }

          return ap;
        }

        return null;

      } catch (SQLException ex) {
        log.error("Error getting airport details for airport with ID '" + airportId + "'", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting airport details for airport with ID '" + airportId + "'", e);
    }

    return null;
  }

  @Override
  public List<AirportRunwayResponse> getAirportRunways(String airportId) {

    final String getAirportRunwaysSql = "SELECT * FROM runways WHERE airport_id = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getAirportRunwaysPstmt = conn.prepareStatement(getAirportRunwaysSql)) {

      try {

        conn.setAutoCommit(false);

        List<AirportRunwayResponse> runways = new ArrayList<>();

        getAirportRunwaysPstmt.setString(1, airportId);

        ResultSet getAirportRunwaysRset = getAirportRunwaysPstmt.executeQuery();

        while (getAirportRunwaysRset.next()) {

          AirportRunwayResponse ar = new AirportRunwayResponse();
          ar.setRunwayId(getAirportRunwaysRset.getString("runway_id"));
          ar.setAirportId(getAirportRunwaysRset.getString("airport_id"));
          ar.setName(getAirportRunwaysRset.getString("name"));
          ar.setDescription(getAirportRunwaysRset.getString("description"));
          ar.setIsAutomaticallyCreated(getAirportRunwaysRset.getBoolean("is_automatically_created"));

          runways.add(ar);
        }

        return runways;

      } catch (SQLException ex) {
        log.error("Error getting runways for airport with ID '" + airportId + "'", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting runways for airport with ID '" + airportId + "'", e);
    }

    return null;
  }

  @Override
  public boolean addRunwayToAirport(String airportId, int runwayNumber, String description) {

    final String addRunwayToAirportSql = "INSERT INTO runways (airport_id, name, description) VALUES (?, ?, ?)";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement addRunwayToAirportPstmt = conn.prepareStatement(addRunwayToAirportSql)) {

      try {

        conn.setAutoCommit(false);

        String runwayName = String.format("%02d", runwayNumber);

        addRunwayToAirportPstmt.setString(1, airportId);
        addRunwayToAirportPstmt.setString(2, runwayName);
        addRunwayToAirportPstmt.setString(3, description);

        int rows = addRunwayToAirportPstmt.executeUpdate();

        conn.commit();

        return rows > 0;

      } catch (SQLException ex) {
        log.error("Error adding runway to airport with ID '" + airportId + "'", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error adding runway to airport with ID '" + airportId + "'", e);
    }

    return false;
  }
}
